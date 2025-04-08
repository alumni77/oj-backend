package com.zjedu.judgeserve.dao.training.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.judgeserve.dao.training.TrainingProblemEntityService;
import com.zjedu.judgeserve.mapper.TrainingProblemMapper;
import com.zjedu.pojo.entity.training.TrainingProblem;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/8 21:23
 * @Version 1.0
 * @Description
 */

@Service
public class TrainingProblemEntityServiceImpl extends ServiceImpl<TrainingProblemMapper, TrainingProblem> implements TrainingProblemEntityService
{
}
