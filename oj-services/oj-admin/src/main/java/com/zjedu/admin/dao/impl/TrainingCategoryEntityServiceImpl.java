package com.zjedu.admin.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.admin.dao.TrainingCategoryEntityService;
import com.zjedu.admin.mapper.TrainingCategoryMapper;
import com.zjedu.pojo.entity.training.TrainingCategory;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/2 20:56
 * @Version 1.0
 * @Description
 */

@Service
public class TrainingCategoryEntityServiceImpl extends ServiceImpl<TrainingCategoryMapper, TrainingCategory> implements TrainingCategoryEntityService
{
}
