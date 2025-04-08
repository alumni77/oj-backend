package com.zjedu.problem.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.problem.dao.UserInfoEntityService;
import com.zjedu.problem.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/8 16:18
 * @Version 1.0
 * @Description
 */

@Service
public class UserInfoEntityServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoEntityService
{
}
