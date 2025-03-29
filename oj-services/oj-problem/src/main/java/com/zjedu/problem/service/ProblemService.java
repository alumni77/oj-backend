package com.zjedu.problem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.PidListDTO;
import com.zjedu.pojo.vo.*;

import java.util.HashMap;
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

    CommonResult<RandomProblemVO> getRandomProblem();

    CommonResult<HashMap<Long, Object>> getUserProblemStatus(PidListDTO pidListDto);

    CommonResult<ProblemInfoVO> getProblemInfo(String problemId);

    CommonResult<LastAcceptedCodeVO> getUserLastAcceptedCode(Long pid);

    CommonResult<List<ProblemFullScreenListVO>> getFullScreenProblemList(Long tid);

}
