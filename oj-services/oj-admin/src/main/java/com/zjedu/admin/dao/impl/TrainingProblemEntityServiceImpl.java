package com.zjedu.admin.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.admin.dao.TrainingProblemEntityService;
import com.zjedu.admin.mapper.TrainingProblemMapper;
import com.zjedu.pojo.entity.training.TrainingProblem;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/2 22:04
 * @Version 1.0
 * @Description
 */

@Service
public class TrainingProblemEntityServiceImpl extends ServiceImpl<TrainingProblemMapper, TrainingProblem> implements TrainingProblemEntityService
{
}
