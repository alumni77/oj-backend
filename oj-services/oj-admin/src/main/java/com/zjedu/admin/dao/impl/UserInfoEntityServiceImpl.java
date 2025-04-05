package com.zjedu.admin.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.admin.dao.UserInfoEntityService;
import com.zjedu.admin.mapper.UserInfoMapper;
import com.zjedu.pojo.entity.user.UserInfo;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/5 19:28
 * @Version 1.0
 * @Description
 */

@Service
public class UserInfoEntityServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoEntityService
{
}
