package com.zjedu.admin.service.impl;

import com.zjedu.admin.manager.AdminTrainingCategoryManager;
import com.zjedu.admin.service.AdminTrainingCategoryService;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.pojo.entity.training.TrainingCategory;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/2 20:52
 * @Version 1.0
 * @Description
 */

@Service
public class AdminTrainingCategoryServiceImpl implements AdminTrainingCategoryService
{
    @Resource
    private AdminTrainingCategoryManager adminTrainingCategoryManager;

    @Override
    public CommonResult<TrainingCategory> addTrainingCategory(TrainingCategory trainingCategory)
    {
        try
        {
            return CommonResult.successResponse(adminTrainingCategoryManager.addTrainingCategory(trainingCategory));
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> updateTrainingCategory(TrainingCategory trainingCategory)
    {
        try
        {
            adminTrainingCategoryManager.updateTrainingCategory(trainingCategory);
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
    public CommonResult<Void> deleteTrainingCategory(Long id)
    {
        try
        {
            adminTrainingCategoryManager.deleteTrainingCategory(id);
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
