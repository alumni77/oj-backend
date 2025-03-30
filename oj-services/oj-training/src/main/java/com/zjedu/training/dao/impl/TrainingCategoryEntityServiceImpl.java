package com.zjedu.training.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.pojo.entity.training.TrainingCategory;
import com.zjedu.training.dao.TrainingCategoryEntityService;
import com.zjedu.training.mapper.TrainingCategoryMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/30 16:10
 * @Version 1.0
 * @Description
 */

@Service
public class TrainingCategoryEntityServiceImpl extends ServiceImpl<TrainingCategoryMapper, TrainingCategory> implements TrainingCategoryEntityService
{

    @Resource
    private TrainingCategoryMapper trainingCategoryMapper;

    @Override
    public TrainingCategory getTrainingCategoryByTrainingId(Long tid)
    {
        return trainingCategoryMapper.getTrainingCategoryByTrainingId(tid);
    }
}