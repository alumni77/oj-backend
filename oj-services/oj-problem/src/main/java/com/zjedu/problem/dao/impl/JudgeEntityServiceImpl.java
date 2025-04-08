package com.zjedu.problem.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.problem.dao.JudgeEntityService;
import com.zjedu.problem.mapper.JudgeMapper;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/8 20:25
 * @Version 1.0
 * @Description
 */

@Service
public class JudgeEntityServiceImpl extends ServiceImpl<JudgeMapper, Judge> implements JudgeEntityService
{
}
