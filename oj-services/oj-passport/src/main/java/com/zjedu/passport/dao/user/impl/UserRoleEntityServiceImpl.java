package com.zjedu.passport.dao.user.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.passport.dao.user.UserRoleEntityService;
import com.zjedu.passport.mapper.UserRoleMapper;
import com.zjedu.pojo.entity.user.Role;
import com.zjedu.pojo.entity.user.UserRole;
import com.zjedu.pojo.vo.UserRolesVO;
import com.zjedu.shiro.ShiroConstant;
import com.zjedu.utils.RedisUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/18 16:12
 * @Version 1.0
 * @Description 服务实现类
 */
@Service
@Slf4j(topic = "oj")
public class UserRoleEntityServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleEntityService
{
    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RedisUtils redisUtils;


    @Override
    public UserRolesVO getUserRoles(String uid, String username)
    {
        return userRoleMapper.getUserRoles(uid, username);
    }

    @Override
    public List<Role> getRolesByUid(String uid)
    {
        return userRoleMapper.getRolesByUid(uid);
    }

    @Override
    public IPage<UserRolesVO> getUserList(int limit, int currentPage, String keyword, Boolean onlyAdmin)
    {
        //新建分页
        Page<UserRolesVO> page = new Page<>(currentPage, limit);
        if (onlyAdmin)
        {
            return userRoleMapper.getAdminUserList(page, limit, currentPage, keyword);
        } else
        {
            return userRoleMapper.getUserList(page, limit, currentPage, keyword);
        }
    }

    /**
     * @param uid             当前需要操作的用户id
     * @param isRemoveSession 如果为true则会强行删除该用户session，必须重新登陆，false的话 在访问受限接口时会重新授权
     */
    @Override
    public void deleteCache(String uid, boolean isRemoveSession)
    {
        //从缓存中获取Session
//        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
//        for (Session sessionInfo : sessions) {
//            //遍历Session,找到该用户名称对应的Session
//            Object attribute = sessionInfo.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
//            if (attribute == null) {
//                continue;
//            }
//            AccountProfile accountProfile = (AccountProfile) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
//            if (accountProfile == null) {
//                continue;
//            }
//            // 如果该session是指定的uid用户的
//            if (Objects.equals(accountProfile.getUid(), uid)) {
//                deleteSession(isRemoveSession, sessionInfo, uid);
//            }
//        }

        if (isRemoveSession)
        {
            redisUtils.del(ShiroConstant.SHIRO_TOKEN_KEY + uid,
                    ShiroConstant.SHIRO_TOKEN_REFRESH + uid,
                    ShiroConstant.SHIRO_AUTHORIZATION_CACHE + uid);
        } else
        {
            redisUtils.del(ShiroConstant.SHIRO_AUTHORIZATION_CACHE + uid);
        }

    }
}
