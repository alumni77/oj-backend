package com.zjedu.training.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.training.dao.UserInfoEntityService;
import com.zjedu.training.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/8 19:36
 * @Version 1.0
 * @Description
 */

@Service
public class UserInfoEntityServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoEntityService
{
}
