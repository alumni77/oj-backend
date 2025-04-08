package com.zjedu.judgeserve.dao.judge.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.judgeserve.dao.judge.JudgeServerEntityService;
import com.zjedu.judgeserve.mapper.JudgeServerMapper;
import com.zjedu.pojo.entity.judge.JudgeServer;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/8 20:48
 * @Version 1.0
 * @Description
 */

@Service
public class JudgeServerEntityServiceImpl extends ServiceImpl<JudgeServerMapper, JudgeServer> implements JudgeServerEntityService
{
}
