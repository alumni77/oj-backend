package com.zjedu.passport.manager;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import com.zjedu.exception.StatusFailException;
import com.zjedu.passport.dao.user.UserRoleEntityService;
import com.zjedu.pojo.dto.LoginDTO;
import com.zjedu.pojo.entity.user.Role;
import com.zjedu.pojo.vo.UserInfoVO;
import com.zjedu.pojo.vo.UserRolesVO;
import com.zjedu.utils.Constants;
import com.zjedu.utils.IpUtils;
import com.zjedu.utils.JwtUtils;
import com.zjedu.utils.RedisUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

/**
 * @Author Zhong
 * @Create 2025/3/18 15:30
 * @Version 1.0
 * @Description
 */

@Component
public class PassportManager
{

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private UserRoleEntityService userRoleEntityService;


    public UserInfoVO login(LoginDTO loginDto, HttpServletResponse response, HttpServletRequest request) throws StatusFailException {
        // 去掉账号密码首尾的空格
        loginDto.setPassword(loginDto.getPassword().trim());
        loginDto.setUsername(loginDto.getUsername().trim());

        if (StringUtils.isEmpty(loginDto.getUsername()) || StringUtils.isEmpty(loginDto.getPassword())) {
            throw new StatusFailException("用户名或密码不能为空！");
        }

        if (loginDto.getPassword().length() < 6 || loginDto.getPassword().length() > 20) {
            throw new StatusFailException("密码长度应该为6~20位！");
        }
        if (loginDto.getUsername().length() > 20) {
            throw new StatusFailException("用户名长度不能超过20位!");
        }

        String userIpAddr = IpUtils.getUserIpAddr(request);
        String key = Constants.Account.TRY_LOGIN_NUM.getCode() + loginDto.getUsername() + "_" + userIpAddr;
        Integer tryLoginCount = (Integer) redisUtils.get(key);

        if (tryLoginCount != null && tryLoginCount >= 20) {
            throw new StatusFailException("对不起！登录失败次数过多！您的账号有风险，半个小时内暂时无法登录！");
        }

        UserRolesVO userRolesVo = userRoleEntityService.getUserRoles(null, loginDto.getUsername());

        if (userRolesVo == null) {
            throw new StatusFailException("用户名或密码错误！请注意大小写！");
        }

        if (!userRolesVo.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))) {
            if (tryLoginCount == null) {
                redisUtils.set(key, 1, 60 * 30); // 三十分钟不尝试，该限制会自动清空消失
            } else {
                redisUtils.set(key, tryLoginCount + 1, 60 * 30);
            }
            throw new StatusFailException("用户名或密码错误！请注意大小写！");
        }

        if (userRolesVo.getStatus() != 0) {
            throw new StatusFailException("该账户已被封禁，请联系管理员进行处理！");
        }

        String jwt = jwtUtils.generateToken(userRolesVo.getUid());
        response.setHeader("Authorization", jwt); //放到信息头部
        response.setHeader("Access-Control-Expose-Headers", "Authorization");


        // 登录成功，清除锁定限制
        if (tryLoginCount != null) {
            redisUtils.del(key);
        }

        UserInfoVO userInfoVo = new UserInfoVO();
        BeanUtil.copyProperties(userRolesVo, userInfoVo, "roles");
        userInfoVo.setRoleList(userRolesVo.getRoles()
                .stream()
                .map(Role::getRole)
                .collect(Collectors.toList()));
        return userInfoVo;
    }
}
