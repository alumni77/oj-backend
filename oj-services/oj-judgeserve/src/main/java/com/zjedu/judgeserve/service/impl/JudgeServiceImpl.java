package com.zjedu.judgeserve.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.common.exception.StatusAccessDeniedException;
import com.zjedu.common.exception.StatusNotFoundException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.judgeserve.manager.JudgeManager;
import com.zjedu.judgeserve.service.JudgeService;
import com.zjedu.pojo.vo.JudgeVO;
import com.zjedu.pojo.vo.SubmissionInfoVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
                                                     Boolean completeProblemID,
                                                     Long gid)
    {
        try
        {
            return CommonResult.successResponse(judgeManager.getJudgeList(limit,
                    currentPage,
                    onlyMine,
                    searchPid,
                    searchStatus,
                    searchUsername,
                    completeProblemID,
                    gid));
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
}
