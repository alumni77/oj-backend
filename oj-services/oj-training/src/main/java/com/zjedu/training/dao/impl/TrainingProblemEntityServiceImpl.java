package com.zjedu.training.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.pojo.entity.training.TrainingProblem;
import com.zjedu.training.dao.TrainingProblemEntityService;
import com.zjedu.training.mapper.TrainingProblemMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/30 15:52
 * @Version 1.0
 * @Description
 */

@Service
public class TrainingProblemEntityServiceImpl extends ServiceImpl<TrainingProblemMapper, TrainingProblem> implements TrainingProblemEntityService
{
    @Resource
    private TrainingProblemMapper trainingProblemMapper;

    @Override
    public List<TrainingProblem> getTrainingListAcceptedCountByUid(List<Long> tidList, String uid)
    {
        return trainingProblemMapper.getTrainingListAcceptedCountByUid(tidList, uid);
    }
}
