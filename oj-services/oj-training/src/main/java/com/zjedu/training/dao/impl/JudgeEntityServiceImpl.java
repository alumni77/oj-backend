package com.zjedu.training.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.training.dao.JudgeEntityService;
import com.zjedu.training.mapper.JudgeMapper;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/8 20:36
 * @Version 1.0
 * @Description
 */

@Service
public class JudgeEntityServiceImpl extends ServiceImpl<JudgeMapper, Judge> implements JudgeEntityService
{
}
