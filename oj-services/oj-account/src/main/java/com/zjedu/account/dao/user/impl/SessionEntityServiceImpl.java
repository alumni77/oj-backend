package com.zjedu.account.dao.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.account.dao.user.SessionEntityService;
import com.zjedu.account.mapper.SessionMapper;
import com.zjedu.pojo.entity.user.Session;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/21 14:55
 * @Version 1.0
 * @Description
 */
/**
 * 简化版会话服务实现类
 */
@Service
public class SessionEntityServiceImpl extends ServiceImpl<SessionMapper, Session> implements SessionEntityService {

    @Resource
    private SessionMapper sessionMapper;

    @Override
    @Async
    public void checkRemoteLogin(String uid) {
        // 简化方法，仅保留记录功能，不进行消息通知
        QueryWrapper<Session> sessionQueryWrapper = new QueryWrapper<>();
        sessionQueryWrapper.eq("uid", uid)
                .orderByDesc("gmt_create")
                .last("limit 2");
        List<Session> sessionList = sessionMapper.selectList(sessionQueryWrapper);
        if (sessionList.size() < 2) {
            return;
        }
    }
}