package com.zjedu.admin.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.admin.dao.UserRoleEntityService;
import com.zjedu.admin.mapper.UserRoleMapper;
import com.zjedu.pojo.entity.user.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/5 19:27
 * @Version 1.0
 * @Description
 */

@Service
@Slf4j
public class UserRoleEntityServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleEntityService
{
}
