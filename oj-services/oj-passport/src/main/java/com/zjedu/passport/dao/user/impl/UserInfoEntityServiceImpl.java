package com.zjedu.passport.dao.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.passport.dao.user.UserInfoEntityService;
import com.zjedu.passport.mapper.UserInfoMapper;
import com.zjedu.pojo.dto.RegisterDTO;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.utils.RedisUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/19 22:16
 * @Version 1.0
 * @Description
 */
@Service
public class UserInfoEntityServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoEntityService
{
    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private RedisUtils redisUtils;


    @Override
    public Boolean addUser(RegisterDTO registerDto)
    {
        return userInfoMapper.addUser(registerDto) == 1;
    }
}
