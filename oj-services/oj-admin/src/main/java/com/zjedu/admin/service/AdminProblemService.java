package com.zjedu.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.CompileDTO;
import com.zjedu.pojo.dto.ProblemDTO;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.entity.problem.ProblemCase;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/4/2 14:20
 * @Version 1.0
 * @Description
 */

public interface AdminProblemService
{
    CommonResult<IPage<Problem>> getProblemList(Integer limit, Integer currentPage, String keyword, Integer auth, String oj) throws StatusFailException;

    CommonResult<Problem> getProblem(Long pid);

    CommonResult<Void> deleteProblem(Long pid);

    CommonResult<Void> addProblem(ProblemDTO problemDto);

    CommonResult<Void> updateProblem(ProblemDTO problemDto);

    CommonResult<List<ProblemCase>> getProblemCases(Long pid, Boolean isUpload);

    CommonResult compileSpj(CompileDTO compileDTO);

    CommonResult compileInteractive(CompileDTO compileDTO);

    CommonResult<Void> changeProblemAuth(Problem problem);
}
