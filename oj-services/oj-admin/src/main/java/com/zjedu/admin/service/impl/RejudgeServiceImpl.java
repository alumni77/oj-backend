package com.zjedu.admin.service.impl;

import com.zjedu.admin.manager.RejudgeManager;
import com.zjedu.admin.service.RejudgeService;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.entity.judge.Judge;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/1 20:47
 * @Version 1.0
 * @Description
 */

@Service
public class RejudgeServiceImpl implements RejudgeService
{
    @Resource
    private RejudgeManager rejudgeManager;

    @Override
    public CommonResult<Judge> rejudge(Long submitId)
    {
        try
        {
            Judge judge = rejudgeManager.rejudge(submitId);
            return CommonResult.successResponse(judge, "重判成功！该提交已进入判题队列！");
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<Judge> manualJudge(Long submitId, Integer status, Integer score)
    {
        try
        {
            return CommonResult.successResponse(rejudgeManager.manualJudge(submitId, status, score));
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<Judge> cancelJudge(Long submitId)
    {
        try
        {
            return CommonResult.successResponse(rejudgeManager.cancelJudge(submitId));
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        }
    }
}
