package com.zjedu.problem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusNotFoundException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.pojo.dto.PidListDTO;
import com.zjedu.pojo.vo.ProblemVO;
import com.zjedu.pojo.vo.RandomProblemVO;
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
}
