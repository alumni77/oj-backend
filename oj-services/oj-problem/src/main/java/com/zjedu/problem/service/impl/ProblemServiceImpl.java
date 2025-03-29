package com.zjedu.problem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.common.exception.StatusAccessDeniedException;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.common.exception.StatusNotFoundException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.pojo.dto.PidListDTO;
import com.zjedu.pojo.vo.*;
import com.zjedu.problem.manager.ProblemManager;
import com.zjedu.problem.service.ProblemService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/28 20:45
 * @Version 1.0
 * @Description
 */

@Service
public class ProblemServiceImpl implements ProblemService
{
    @Resource
    private ProblemManager problemManager;

    @Override
    public CommonResult<Page<ProblemVO>> getProblemList(Integer limit, Integer currentPage, String keyword, List<Long> tagId, Integer difficulty, String oj)
    {
        return CommonResult.successResponse(problemManager.getProblemList(limit, currentPage, keyword, tagId, difficulty, oj));
    }

    @Override
    public CommonResult<RandomProblemVO> getRandomProblem()
    {
        try
        {
            return CommonResult.successResponse(problemManager.getRandomProblem());
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<HashMap<Long, Object>> getUserProblemStatus(PidListDTO pidListDto)
    {
        try
        {
            return CommonResult.successResponse(problemManager.getUserProblemStatus(pidListDto));
        } catch (StatusNotFoundException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.NOT_FOUND);
        }
    }

    @Override
    public CommonResult<ProblemInfoVO> getProblemInfo(String problemId)
    {
        try
        {
            return CommonResult.successResponse(problemManager.getProblemInfo(problemId));
        } catch (StatusNotFoundException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.NOT_FOUND);
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<LastAcceptedCodeVO> getUserLastAcceptedCode(Long pid)
    {
        return CommonResult.successResponse(problemManager.getUserLastAcceptedCode(pid));
    }

    @Override
    public CommonResult<List<ProblemFullScreenListVO>> getFullScreenProblemList(Long tid)
    {
        try {
            return CommonResult.successResponse(problemManager.getFullScreenProblemList(tid));
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        } catch (StatusAccessDeniedException e) {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.ACCESS_DENIED);
        }
    }
}
