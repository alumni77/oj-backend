package com.zjedu.training.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.vo.TrainingVO;
import com.zjedu.training.manager.TrainingManager;
import com.zjedu.training.service.TrainingService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/30 15:37
 * @Version 1.0
 * @Description
 */

@Service
public class TrainingServiceImpl implements TrainingService
{

    @Resource
    private TrainingManager trainingManager;

    @Override
    public CommonResult<IPage<TrainingVO>> getTrainingList(Integer limit, Integer currentPage, String keyword, Long categoryId, String auth)
    {
        return CommonResult.successResponse(trainingManager.getTrainingList(limit, currentPage, keyword, categoryId, auth));

    }
}
