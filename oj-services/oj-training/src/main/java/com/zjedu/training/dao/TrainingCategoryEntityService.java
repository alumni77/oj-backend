package com.zjedu.training.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjedu.pojo.entity.training.TrainingCategory;

/**
 * @Author Zhong
 * @Create 2025/3/30 16:09
 * @Version 1.0
 * @Description
 */

public interface TrainingCategoryEntityService extends IService<TrainingCategory>
{

    TrainingCategory getTrainingCategoryByTrainingId(Long tid);
}
