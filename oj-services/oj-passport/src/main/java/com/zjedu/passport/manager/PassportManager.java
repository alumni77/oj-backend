package com.zjedu.passport.manager;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjedu.common.exception.StatusAccessDeniedException;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.config.NacosSwitchConfig;
import com.zjedu.config.WebConfig;
import com.zjedu.passport.dao.user.UserInfoEntityService;
import com.zjedu.passport.dao.user.UserRoleEntityService;
import com.zjedu.pojo.dto.LoginDTO;
import com.zjedu.pojo.entity.user.Role;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.RegisterCodeVO;
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

    @Resource
    private UserInfoEntityService userInfoEntityService;

    @Resource
    private NacosSwitchConfig nacosSwitchConfig;


    public UserInfoVO login(LoginDTO loginDto, HttpServletResponse response, HttpServletRequest request) throws StatusFailException
    {
        // 去掉账号密码首尾的空格
        loginDto.setPassword(loginDto.getPassword().trim());
        loginDto.setUsername(loginDto.getUsername().trim());

        if (!StringUtils.hasText(loginDto.getPassword()) || !StringUtils.hasText(loginDto.getUsername()))
        {
            throw new StatusFailException("用户名或密码不能为空！");
        }

        if (loginDto.getPassword().length() < 6 || loginDto.getPassword().length() > 20)
        {
            throw new StatusFailException("密码长度应该为6~20位！");
        }
        if (loginDto.getUsername().length() > 20)
        {
            throw new StatusFailException("用户名长度不能超过20位!");
        }

        String userIpAddr = IpUtils.getUserIpAddr(request);
        String key = Constants.Account.TRY_LOGIN_NUM.getCode() + loginDto.getUsername() + "_" + userIpAddr;
        Integer tryLoginCount = (Integer) redisUtils.get(key);

        if (tryLoginCount != null && tryLoginCount >= 10)
        {
            throw new StatusFailException("对不起！登录失败次数过多！您的账号有风险，半个小时内暂时无法登录！");
        }

        UserRolesVO userRolesVo = userRoleEntityService.getUserRoles(null, loginDto.getUsername());

        if (userRolesVo == null)
        {
            throw new StatusFailException("用户名或密码错误！请注意大小写！");
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
            throw new StatusFailException("用户名或密码错误！请注意大小写！");
        }

        if (userRolesVo.getStatus() != 0)
        {
            throw new StatusFailException("该账户已被封禁，请联系管理员进行处理！");
        }

        String jwt = jwtUtils.generateToken(userRolesVo.getUid());
        response.setHeader("Authorization", jwt); //放到信息头部
        response.setHeader("Access-Control-Expose-Headers", "Authorization");


        // 登录成功，清除锁定限制
        if (tryLoginCount != null)
        {
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

    public RegisterCodeVO getRegisterCode(String username) throws StatusAccessDeniedException, StatusFailException
    {
        WebConfig webConfig = nacosSwitchConfig.getWebConfig();
        // 需要判断一下网站是否开启注册
        if (!webConfig.getRegister())
        {
            throw new StatusAccessDeniedException("对不起！本站点暂时关闭注册功能！");
        }

        if (!StringUtils.hasText(username))
        {
            throw new StatusFailException("用户名不能为空！");
        }

        username = username.trim();

        String lockKey = Constants.Email.REGISTER_EMAIL_LOCK.getValue() + username;
        if(redisUtils.hasKey(lockKey))
        {
            throw new StatusFailException("对不起,您的操作频率过快,请在"+redisUtils.getExpire(lockKey)+"秒后再次获取验证码");
        }

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        UserInfo userInfo = userInfoEntityService.getOne(queryWrapper, false);
        if (userInfo != null) {
            throw new StatusFailException("对不起！该用户名已被注册，请更换用户名！");
        }

        // 随机生成6位数字的组合
        String numbers = RandomUtil.randomNumbers(6);
        //默认验证码有效10分钟
        redisUtils.set(Constants.Email.REGISTER_KEY_PREFIX.getValue() + username, numbers, 10 * 60);
        redisUtils.set(lockKey, 0, 60);

        RegisterCodeVO registerCodeVo = new RegisterCodeVO();
        // 将验证码直接返回给前端
        registerCodeVo.setCode(numbers);
        // 验证码有效期10分钟
        registerCodeVo.setExpire(10 * 60);

        return registerCodeVo;
    }
}
