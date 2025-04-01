package com.zjedu.admin.controller;

import com.zjedu.admin.service.RejudgeService;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.entity.judge.Judge;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Zhong
 * @Create 2025/4/1 20:46
 * @Version 1.0
 * @Description 超管重判提交
 */

@RestController
@RequestMapping("/judge")
public class AdminJudgeController
{
    @Resource
    private RejudgeService rejudgeService;

    @GetMapping("/rejudge")
    public CommonResult<Judge> rejudge(@RequestParam("submitId") Long submitId)
    {
        return rejudgeService.rejudge(submitId);
    }

    @GetMapping("/manual-judge")
    public CommonResult<Judge> manualJudge(@RequestParam("submitId") Long submitId,
                                           @RequestParam("status") Integer status,
                                           @RequestParam(value = "score", required = false) Integer score)
    {
        return rejudgeService.manualJudge(submitId, status, score);
    }

    @GetMapping("/cancel-judge")
    public CommonResult<Judge> cancelJudge(@RequestParam("submitId") Long submitId)
    {
        return rejudgeService.cancelJudge(submitId);
    }


}
