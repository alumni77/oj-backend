package com.zjedu.admin.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.admin.dao.TrainingRecordEntityService;
import com.zjedu.admin.mapper.TrainingRecordMapper;
import com.zjedu.pojo.entity.training.TrainingRecord;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/2 22:01
 * @Version 1.0
 * @Description
 */

@Service
public class TrainingRecordEntityServiceImpl extends ServiceImpl<TrainingRecordMapper, TrainingRecord> implements TrainingRecordEntityService

{
}
