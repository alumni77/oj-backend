package com.zjedu.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.admin.manager.AdminProblemManager;
import com.zjedu.admin.service.AdminProblemService;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.pojo.dto.CompileDTO;
import com.zjedu.pojo.dto.ProblemDTO;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.entity.problem.ProblemCase;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/4/2 14:20
 * @Version 1.0
 * @Description
 */

@Service
public class AdminProblemServiceImpl implements AdminProblemService
{
    @Resource
    private AdminProblemManager adminProblemManager;

    @Override
    public CommonResult<IPage<Problem>> getProblemList(Integer limit, Integer currentPage, String keyword, Integer auth, String oj)
    {
        try
        {
            IPage<Problem> problemList = adminProblemManager.getProblemList(limit, currentPage, keyword, auth, oj);
            return CommonResult.successResponse(problemList);
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<Problem> getProblem(Long pid)
    {
        try
        {
            Problem problem = adminProblemManager.getProblem(pid);
            return CommonResult.successResponse(problem);
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<Void> deleteProblem(Long pid)
    {
        try
        {
            adminProblemManager.deleteProblem(pid);
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
    public CommonResult<Void> addProblem(ProblemDTO problemDto)
    {
        try
        {
            adminProblemManager.addProblem(problemDto);
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
    public CommonResult<Void> updateProblem(ProblemDTO problemDto)
    {
        try
        {
            adminProblemManager.updateProblem(problemDto);
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
    public CommonResult<List<ProblemCase>> getProblemCases(Long pid, Boolean isUpload)
    {
        try
        {
            List<ProblemCase> problemCaseList = adminProblemManager.getProblemCases(pid, isUpload);
            return CommonResult.successResponse(problemCaseList);
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult compileSpj(CompileDTO compileDTO)
    {
        try
        {
            return adminProblemManager.compileSpj(compileDTO);
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult compileInteractive(CompileDTO compileDTO)
    {
        try
        {
            return adminProblemManager.compileInteractive(compileDTO);
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> changeProblemAuth(Problem problem)
    {
        try
        {
            adminProblemManager.changeProblemAuth(problem);
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
