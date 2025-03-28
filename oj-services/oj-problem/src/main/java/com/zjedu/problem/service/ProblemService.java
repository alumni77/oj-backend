package com.zjedu.problem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.vo.ProblemVO;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/28 20:44
 * @Version 1.0
 * @Description
 */

public interface ProblemService
{
    CommonResult<Page<ProblemVO>> getProblemList(Integer limit, Integer currentPage, String keyword, List<Long> tagId, Integer difficulty, String oj);
}
