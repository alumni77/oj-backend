package com.zjedu.problem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.vo.ProblemVO;
import com.zjedu.problem.manager.ProblemManager;
import com.zjedu.problem.service.ProblemService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
}
