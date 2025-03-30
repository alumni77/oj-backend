package com.zjedu.training.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjedu.pojo.entity.training.TrainingRecord;
import com.zjedu.pojo.vo.TrainingRecordVO;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/30 19:22
 * @Version 1.0
 * @Description
 */

public interface TrainingRecordEntityService extends IService<TrainingRecord>
{
    List<TrainingRecordVO> getTrainingRecord(Long tid);
}