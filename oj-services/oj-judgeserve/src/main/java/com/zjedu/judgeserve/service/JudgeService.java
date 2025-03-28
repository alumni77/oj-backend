package com.zjedu.judgeserve.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.SubmitJudgeDTO;
import com.zjedu.pojo.dto.TestJudgeDTO;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.vo.JudgeVO;
import com.zjedu.pojo.vo.SubmissionInfoVO;
import com.zjedu.pojo.vo.TestJudgeVO;

/**
 * @Author Zhong
 * @Create 2025/3/26 19:03
 * @Version 1.0
 * @Description
 */

public interface JudgeService
{
    CommonResult<IPage<JudgeVO>> getJudgeList(Integer limit, Integer currentPage, Boolean onlyMine, String searchPid, Integer searchStatus, String searchUsername, Boolean completeProblemID);

    CommonResult<SubmissionInfoVO> getSubmission(Long submitId);

    CommonResult<Judge> submitProblemJudge(SubmitJudgeDTO judgeDto);

    CommonResult<String> submitProblemTestJudge(TestJudgeDTO testJudgeDto);

    CommonResult<TestJudgeVO> getTestJudgeResult(String testJudgeKey);
}
