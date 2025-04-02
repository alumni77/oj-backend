package com.zjedu.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.admin.manager.AdminProblemManager;
import com.zjedu.admin.service.AdminProblemService;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.entity.problem.Problem;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
    public CommonResult<IPage<Problem>> getProblemList(Integer limit, Integer currentPage, String keyword, Integer auth, String oj) throws StatusFailException
    {
        IPage<Problem> problemList = adminProblemManager.getProblemList(limit, currentPage, keyword, auth, oj);
        return CommonResult.successResponse(problemList);
    }
}
