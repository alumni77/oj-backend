package com.zjedu.account.dao.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.account.dao.user.UserInfoEntityService;
import com.zjedu.account.mapper.UserInfoMapper;
import com.zjedu.pojo.entity.user.UserInfo;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/8 15:40
 * @Version 1.0
 * @Description
 */

@Service
public class UserInfoEntityServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoEntityService
{
}
