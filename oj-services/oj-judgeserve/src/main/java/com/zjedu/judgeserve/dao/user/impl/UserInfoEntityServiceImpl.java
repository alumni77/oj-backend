package com.zjedu.judgeserve.dao.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.judgeserve.dao.user.UserInfoEntityService;
import com.zjedu.judgeserve.mapper.UserInfoMapper;
import com.zjedu.pojo.entity.user.UserInfo;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/8 16:14
 * @Version 1.0
 * @Description
 */

@Service
public class UserInfoEntityServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoEntityService
{
}
