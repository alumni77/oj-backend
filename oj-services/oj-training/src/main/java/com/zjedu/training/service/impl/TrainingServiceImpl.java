package com.zjedu.training.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.common.exception.StatusAccessDeniedException;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.pojo.vo.ProblemVO;
import com.zjedu.pojo.vo.TrainingRankVO;
import com.zjedu.pojo.vo.TrainingVO;
import com.zjedu.training.manager.TrainingManager;
import com.zjedu.training.service.TrainingService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public CommonResult<TrainingVO> getTraining(Long tid)
    {
        try
        {
            return CommonResult.successResponse(trainingManager.getTraining(tid));
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusAccessDeniedException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.ACCESS_DENIED);
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<List<ProblemVO>> getTrainingProblemList(Long tid)
    {
        try
        {
            return CommonResult.successResponse(trainingManager.getTrainingProblemList(tid));
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusAccessDeniedException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.ACCESS_DENIED);
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<IPage<TrainingRankVO>> getTrainingRank(Long tid, Integer limit, Integer currentPage, String keyword)
    {
        try
        {
            return CommonResult.successResponse(trainingManager.getTrainingRank(tid, limit, currentPage, keyword));
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusAccessDeniedException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.ACCESS_DENIED);
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }
}
