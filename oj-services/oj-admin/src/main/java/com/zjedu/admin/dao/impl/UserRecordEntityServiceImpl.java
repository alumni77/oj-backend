package com.zjedu.admin.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.admin.dao.UserRecordEntityService;
import com.zjedu.admin.mapper.UserRecordMapper;
import com.zjedu.pojo.entity.user.UserRecord;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/5 20:31
 * @Version 1.0
 * @Description
 */

@Service
public class UserRecordEntityServiceImpl extends ServiceImpl<UserRecordMapper, UserRecord> implements UserRecordEntityService
{
}
