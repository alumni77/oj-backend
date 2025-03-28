package com.zjedu.judgeserve.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.common.exception.*;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.judgeserve.manager.JudgeManager;
import com.zjedu.judgeserve.service.JudgeService;
import com.zjedu.pojo.dto.SubmitIdListDTO;
import com.zjedu.pojo.dto.SubmitJudgeDTO;
import com.zjedu.pojo.dto.TestJudgeDTO;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.vo.JudgeCaseVO;
import com.zjedu.pojo.vo.JudgeVO;
import com.zjedu.pojo.vo.SubmissionInfoVO;
import com.zjedu.pojo.vo.TestJudgeVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @Author Zhong
 * @Create 2025/3/26 19:04
 * @Version 1.0
 * @Description
 */

@Service
public class JudgeServiceImpl implements JudgeService
{

    @Resource
    private JudgeManager judgeManager;

    @Override
    public CommonResult<IPage<JudgeVO>> getJudgeList(Integer limit,
                                                     Integer currentPage,
                                                     Boolean onlyMine,
                                                     String searchPid,
                                                     Integer searchStatus,
                                                     String searchUsername,
                                                     Boolean completeProblemID)
    {
        try
        {
            return CommonResult.successResponse(judgeManager.getJudgeList(limit,
                    currentPage,
                    onlyMine,
                    searchPid,
                    searchStatus,
                    searchUsername,
                    completeProblemID));
        } catch (StatusAccessDeniedException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.ACCESS_DENIED);
        }
    }

    @Override
    public CommonResult<SubmissionInfoVO> getSubmission(Long submitId)
    {
        try
        {
            return CommonResult.successResponse(judgeManager.getSubmission(submitId));
        } catch (StatusNotFoundException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.NOT_FOUND);
        } catch (StatusAccessDeniedException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.ACCESS_DENIED);
        }
    }

    @Override
    public CommonResult<Judge> submitProblemJudge(SubmitJudgeDTO judgeDto)
    {
        try
        {
            return CommonResult.successResponse(judgeManager.submitProblemJudge(judgeDto));
        } catch (StatusForbiddenException | AccessException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        } catch (StatusNotFoundException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.NOT_FOUND);
        } catch (StatusAccessDeniedException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.ACCESS_DENIED);
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<String> submitProblemTestJudge(TestJudgeDTO testJudgeDto)
    {
        try
        {
            return CommonResult.successResponse(judgeManager.submitProblemTestJudge(testJudgeDto), "success");
        } catch (StatusForbiddenException | AccessException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusSystemErrorException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.SYSTEM_ERROR);
        }
    }

    @Override
    public CommonResult<TestJudgeVO> getTestJudgeResult(String testJudgeKey)
    {
        try
        {
            return CommonResult.successResponse(judgeManager.getTestJudgeResult(testJudgeKey));
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<Judge> resubmit(Long submitId)
    {
        try
        {
            return CommonResult.successResponse(judgeManager.resubmit(submitId));
        } catch (StatusNotFoundException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.NOT_FOUND);
        }
    }

    @Override
    public CommonResult<Boolean> updateSubmission(Judge judge)
    {
        try
        {
            judge = judgeManager.updateSubmission(judge);
            return CommonResult.successResponse(judge.getShare());
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<HashMap<Long, Object>> checkCommonJudgeResult(SubmitIdListDTO submitIdListDto)
    {
        return CommonResult.successResponse(judgeManager.checkCommonJudgeResult(submitIdListDto));
    }

    @Override
    public CommonResult<JudgeCaseVO> getALLCaseResult(Long submitId)
    {
        try
        {
            return CommonResult.successResponse(judgeManager.getALLCaseResult(submitId));
        } catch (StatusNotFoundException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.NOT_FOUND);
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }
}
