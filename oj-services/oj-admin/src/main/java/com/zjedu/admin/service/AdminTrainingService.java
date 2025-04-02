package com.zjedu.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.TrainingDTO;
import com.zjedu.pojo.entity.training.Training;

/**
 * @Author Zhong
 * @Create 2025/4/2 21:14
 * @Version 1.0
 * @Description
 */

public interface AdminTrainingService
{
    CommonResult<IPage<Training>> getTrainingList(Integer limit, Integer currentPage, String keyword);

    CommonResult<TrainingDTO> getTraining(Long tid);

    CommonResult<Void> deleteTraining(Long id);

    CommonResult<Void> addTraining(TrainingDTO trainingDto);

    CommonResult<Void> updateTraining(TrainingDTO trainingDto);

    CommonResult<Void> changeTrainingStatus(Long tid, String author, Boolean status);
}
