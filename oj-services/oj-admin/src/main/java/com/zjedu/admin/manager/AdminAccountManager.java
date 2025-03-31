package com.zjedu.admin.manager;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import com.zjedu.admin.dao.SessionEntityService;
import com.zjedu.admin.feign.PassportFeignClient;
import com.zjedu.common.exception.StatusAccessDeniedException;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.pojo.dto.LoginDTO;
import com.zjedu.pojo.entity.user.Role;
import com.zjedu.pojo.entity.user.Session;
import com.zjedu.pojo.vo.UserInfoVO;
import com.zjedu.pojo.vo.UserRolesVO;
import com.zjedu.utils.Constants;
import com.zjedu.utils.IpUtils;
import com.zjedu.utils.JwtUtils;
import com.zjedu.utils.RedisUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Zhong
 * @Create 2025/3/31 15:46
 * @Version 1.0
 * @Description
 */

@Slf4j
@Component
public class AdminAccountManager
{
    @Resource
    private RedisUtils redisUtils;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private SessionEntityService sessionEntityService;

    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private HttpServletRequest request;

    public UserInfoVO login(LoginDTO loginDto) throws StatusFailException, StatusAccessDeniedException
    {

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (servletRequestAttributes != null)
        {
            request = servletRequestAttributes.getRequest();
        }
        HttpServletResponse response = null;
        if (servletRequestAttributes != null)
        {
            response = servletRequestAttributes.getResponse();
        }

        String userIpAddr = IpUtils.getUserIpAddr(request);
        String key = Constants.Account.TRY_LOGIN_NUM.getCode() + loginDto.getUsername() + "_" + userIpAddr;
        Integer tryLoginCount = (Integer) redisUtils.get(key);

        if (tryLoginCount != null && tryLoginCount >= 20)
        {
            throw new StatusFailException("对不起！登录失败次数过多！您的账号有风险，半个小时内暂时无法登录！");
        }

        UserRolesVO userRolesVo = passportFeignClient.getUserRoles("", loginDto.getUsername());

        if (userRolesVo == null)
        {
            throw new StatusFailException("用户名或密码错误");
        }

        if (!userRolesVo.getPassword().equals(SecureUtil.md5(loginDto.getPassword())))
        {
            if (tryLoginCount == null)
            {
                redisUtils.set(key, 1, 60 * 30); // 三十分钟不尝试，该限制会自动清空消失
            } else
            {
                redisUtils.set(key, tryLoginCount + 1, 60 * 30);
            }
            throw new StatusFailException("用户名或密码错误");
        }

        if (userRolesVo.getStatus() != 0)
        {
            throw new StatusFailException("该账户已被封禁，请联系管理员进行处理！");
        }

        // 认证成功，清除锁定限制
        if (tryLoginCount != null)
        {
            redisUtils.del(key);
        }

        // 查询用户角色
        List<String> rolesList = new LinkedList<>();
        userRolesVo.getRoles().stream()
                .forEach(role -> rolesList.add(role.getRole()));


        if (rolesList.contains("admin") || rolesList.contains("root") || rolesList.contains("problem_admin"))
        { // 超级管理员或管理员、题目管理员
            String jwt = jwtUtils.generateToken(userRolesVo.getUid());

            response.setHeader("Authorization", jwt); //放到信息头部
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
            // 会话记录
            if (request != null)
            {
                sessionEntityService.save(new Session().setUid(userRolesVo.getUid())
                        .setIp(IpUtils.getUserIpAddr(request)).setUserAgent(request.getHeader("User-Agent")));
            } else
            {
                throw new StatusFailException("登录失败，请重试！");
            }

            UserInfoVO userInfoVo = new UserInfoVO();
            BeanUtil.copyProperties(userRolesVo, userInfoVo, "roles");
            userInfoVo.setRoleList(userRolesVo.getRoles()
                    .stream()
                    .map(Role::getRole)
                    .collect(Collectors.toList()));

            return userInfoVo;
        } else
        {
            throw new StatusAccessDeniedException("该账号并非管理员账号，无权登录！");
        }
    }

    public void logout()
    {
        String userId = request.getHeader("X-User-ID");
        if (StringUtils.hasText(userId))
        {
            jwtUtils.cleanToken(userId);
        }
    }
}
