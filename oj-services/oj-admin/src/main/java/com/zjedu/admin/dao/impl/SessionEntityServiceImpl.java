package com.zjedu.admin.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.admin.dao.SessionEntityService;
import com.zjedu.admin.mapper.SessionMapper;
import com.zjedu.pojo.entity.user.Session;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/31 15:51
 * @Version 1.0
 * @Description
 */

@Service
public class SessionEntityServiceImpl extends ServiceImpl<SessionMapper, Session> implements SessionEntityService
{
}
