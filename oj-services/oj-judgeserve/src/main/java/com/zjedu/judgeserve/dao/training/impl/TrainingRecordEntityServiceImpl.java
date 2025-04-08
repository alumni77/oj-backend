package com.zjedu.judgeserve.dao.training.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.judgeserve.dao.training.TrainingRecordEntityService;
import com.zjedu.judgeserve.mapper.TrainingRecordMapper;
import com.zjedu.pojo.entity.training.TrainingRecord;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/8 21:21
 * @Version 1.0
 * @Description
 */

@Service
public class TrainingRecordEntityServiceImpl extends ServiceImpl<TrainingRecordMapper, TrainingRecord> implements TrainingRecordEntityService
{
}
