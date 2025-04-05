package com.zjedu.admin.service.impl;

import com.zjedu.admin.manager.AdminTrainingProblemManager;
import com.zjedu.admin.service.AdminTrainingProblemService;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.pojo.dto.TrainingProblemDTO;
import com.zjedu.pojo.entity.training.TrainingProblem;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @Author Zhong
 * @Create 2025/4/2 21:15
 * @Version 1.0
 * @Description
 */

@Service
public class AdminTrainingProblemServiceImpl implements AdminTrainingProblemService
{
    @Resource
    private AdminTrainingProblemManager adminTrainingProblemManager;

    @Override
    public CommonResult<HashMap<String, Object>> getProblemList(Integer limit, Integer currentPage, String keyword, Boolean queryExisted, Long tid)
    {
        try
        {
            HashMap<String, Object> problemMap = adminTrainingProblemManager.getProblemList(limit, currentPage, keyword, queryExisted, tid);
            return CommonResult.successResponse(problemMap);
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> updateProblem(TrainingProblem trainingProblem)
    {
        try
        {
            adminTrainingProblemManager.updateProblem(trainingProblem);
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
    public CommonResult<Void> deleteProblem(Long pid, Long tid)
    {
        try
        {
            adminTrainingProblemManager.deleteProblem(pid, tid);
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
    public CommonResult<Void> addProblemFromPublic(TrainingProblemDTO trainingProblemDto)
    {
        try
        {
            adminTrainingProblemManager.addProblemFromPublic(trainingProblemDto);
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
