package com.zjedu.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.entity.problem.Problem;

/**
 * @Author Zhong
 * @Create 2025/4/2 14:20
 * @Version 1.0
 * @Description
 */

public interface AdminProblemService
{
    CommonResult<IPage<Problem>> getProblemList(Integer limit, Integer currentPage, String keyword, Integer auth, String oj) throws StatusFailException;
}
