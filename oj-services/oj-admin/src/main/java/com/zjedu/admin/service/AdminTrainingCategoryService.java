package com.zjedu.admin.service;

import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.entity.training.TrainingCategory;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/4/2 20:51
 * @Version 1.0
 * @Description
 */

public interface AdminTrainingCategoryService
{
    CommonResult<TrainingCategory> addTrainingCategory(TrainingCategory trainingCategory);

    CommonResult<Void> updateTrainingCategory(TrainingCategory trainingCategory);

    CommonResult<Void> deleteTrainingCategory(Long id);

    CommonResult<List<TrainingCategory>> getTrainingCategoryList();
}
