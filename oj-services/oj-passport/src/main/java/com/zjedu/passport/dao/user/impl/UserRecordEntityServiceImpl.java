package com.zjedu.passport.dao.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.passport.dao.user.UserRecordEntityService;
import com.zjedu.passport.mapper.UserRecordMapper;
import com.zjedu.pojo.entity.user.UserRecord;
import com.zjedu.pojo.vo.UserHomeVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/20 11:59
 * @Version 1.0
 * @Description 服务实现类
 */

@Service
public class UserRecordEntityServiceImpl extends ServiceImpl<UserRecordMapper, UserRecord> implements UserRecordEntityService
{
    @Resource
    private UserRecordMapper userRecordMapper;

    @Override
    public UserHomeVO getUserHomeInfo(String uid, String username)
    {
        return userRecordMapper.getUserHomeInfo(uid, username);
    }
}
