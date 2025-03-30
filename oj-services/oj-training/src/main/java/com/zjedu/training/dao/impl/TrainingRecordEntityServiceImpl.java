package com.zjedu.training.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.pojo.entity.training.TrainingRecord;
import com.zjedu.pojo.vo.TrainingRecordVO;
import com.zjedu.training.dao.TrainingRecordEntityService;
import com.zjedu.training.mapper.TrainingRecordMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/30 19:24
 * @Version 1.0
 * @Description
 */

@Service
public class TrainingRecordEntityServiceImpl extends ServiceImpl<TrainingRecordMapper, TrainingRecord> implements TrainingRecordEntityService
{

    @Resource
    private TrainingRecordMapper trainingRecordMapper;

    @Override
    public List<TrainingRecordVO> getTrainingRecord(Long tid)
    {
        return trainingRecordMapper.getTrainingRecord(tid);
    }

}