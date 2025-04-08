package com.zjedu.common.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.common.dao.TrainingCategoryEntityService;
import com.zjedu.common.mapper.TrainingCategoryMapper;
import com.zjedu.pojo.entity.training.TrainingCategory;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/8 21:05
 * @Version 1.0
 * @Description
 */

@Service
public class TrainingCategoryEntityServiceImpl extends ServiceImpl<TrainingCategoryMapper, TrainingCategory> implements TrainingCategoryEntityService
{
}
