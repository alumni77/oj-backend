package com.zjedu.judgeserve.dao.judge.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.judgeserve.dao.judge.JudgeEntityService;
import com.zjedu.judgeserve.mapper.JudgeMapper;
import com.zjedu.pojo.entity.judge.Judge;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/8 19:56
 * @Version 1.0
 * @Description
 */

@Service
public class JudgeEntityServiceImpl extends ServiceImpl<JudgeMapper, Judge> implements JudgeEntityService
{
}
