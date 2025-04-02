package com.zjedu.admin.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.admin.dao.TrainingEntityService;
import com.zjedu.admin.mapper.TrainingMapper;
import com.zjedu.pojo.entity.training.Training;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/2 21:19
 * @Version 1.0
 * @Description
 */

@Service
public class TrainingEntityServiceImpl extends ServiceImpl<TrainingMapper, Training> implements TrainingEntityService
{
}
