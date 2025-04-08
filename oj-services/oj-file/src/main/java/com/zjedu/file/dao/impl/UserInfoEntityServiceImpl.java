package com.zjedu.file.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.file.dao.UserInfoEntityService;
import com.zjedu.file.mapper.UserInfoMapper;
import com.zjedu.pojo.entity.user.UserInfo;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/8 15:55
 * @Version 1.0
 * @Description
 */

@Service
public class UserInfoEntityServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoEntityService
{
}
