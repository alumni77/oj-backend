package com.zjedu.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.admin.manager.AdminTrainingManager;
import com.zjedu.admin.service.AdminTrainingService;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.pojo.dto.TrainingDTO;
import com.zjedu.pojo.entity.training.Training;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/2 21:14
 * @Version 1.0
 * @Description
 */

@Service
public class AdminTrainingServiceImpl implements AdminTrainingService
{
    @Resource
    private AdminTrainingManager adminTrainingManager;

    @Override
    public CommonResult<IPage<Training>> getTrainingList(Integer limit, Integer currentPage, String keyword)
    {
        try
        {
            return CommonResult.successResponse(adminTrainingManager.getTrainingList(limit, currentPage, keyword));
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<TrainingDTO> getTraining(Long tid)
    {
        try
        {
            TrainingDTO training = adminTrainingManager.getTraining(tid);
            return CommonResult.successResponse(training);
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> deleteTraining(Long id)
    {
        try
        {
            adminTrainingManager.deleteTraining(id);
            return CommonResult.successResponse();
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> addTraining(TrainingDTO trainingDto)
    {
        try
        {
            adminTrainingManager.addTraining(trainingDto);
            return CommonResult.successResponse();
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> updateTraining(TrainingDTO trainingDto)
    {
        try
        {
            adminTrainingManager.updateTraining(trainingDto);
            return CommonResult.successResponse();
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> changeTrainingStatus(Long tid, String author, Boolean status)
    {
        try
        {
            adminTrainingManager.changeTrainingStatus(tid, author, status);
            return CommonResult.successResponse();
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }
}
