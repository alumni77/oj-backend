package com.zjedu.passport.dao.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.passport.dao.user.UserRoleEntityService;
import com.zjedu.passport.mapper.UserRoleMapper;
import com.zjedu.pojo.entity.user.Role;
import com.zjedu.pojo.entity.user.UserRole;
import com.zjedu.pojo.vo.UserRolesVO;
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
}
