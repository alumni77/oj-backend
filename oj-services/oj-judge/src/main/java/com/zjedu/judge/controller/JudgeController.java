package com.zjedu.judge.controller;

import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.judge.common.exception.SystemError;
import com.zjedu.judge.dao.JudgeServerEntityService;
import com.zjedu.judge.service.JudgeService;
import com.zjedu.pojo.dto.CompileDTO;
import com.zjedu.pojo.dto.TestJudgeReq;
import com.zjedu.pojo.dto.TestJudgeRes;
import com.zjedu.pojo.dto.ToJudgeDTO;
import com.zjedu.pojo.entity.judge.Judge;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Objects;

/**
 * @Author Zhong
 * @Create 2025/3/24 21:44
 * @Version 1.0
 * @Description 处理代码提交
 */
@RestController
@RefreshScope
public class JudgeController
{
    @Resource
    private JudgeService judgeService;

    @Value("${oj.judge.token}")
    private String judgeToken;

    @Resource
    private JudgeServerEntityService judgeServerEntityService;

    @GetMapping("/version")
    public CommonResult<HashMap<String, Object>> getVersion()
    {
        return CommonResult.successResponse(judgeServerEntityService.getJudgeServerInfo(), "运行正常");
    }

    @PostMapping("/")
    public CommonResult<Void> submitProblemJudge(@RequestBody ToJudgeDTO toJudgeDTO)
    {

        if (!Objects.equals(toJudgeDTO.getToken(), judgeToken))
        {
            return CommonResult.errorResponse("对不起！您使用的判题服务调用凭证不正确！访问受限！", ResultStatus.ACCESS_DENIED);
        }

        Judge judge = toJudgeDTO.getJudge();

        if (judge == null || judge.getSubmitId() == null || judge.getUid() == null || judge.getPid() == null)
        {
            return CommonResult.errorResponse("调用参数错误！请检查您的调用参数！");
        }

        judgeService.judge(judge);
        return CommonResult.successResponse("判题机评测完成！");
    }

    @PostMapping(value = "/test-judge")
    public CommonResult<TestJudgeRes> submitProblemTestJudge(@RequestBody TestJudgeReq testJudgeReq)
    {
        if (testJudgeReq == null
                || StringUtils.isEmpty(testJudgeReq.getCode())
                || StringUtils.isEmpty(testJudgeReq.getLanguage())
                || StringUtils.isEmpty(testJudgeReq.getUniqueKey())
                || testJudgeReq.getTimeLimit() == null
                || testJudgeReq.getMemoryLimit() == null
                || testJudgeReq.getStackLimit() == null)
        {
            return CommonResult.errorResponse("调用参数错误！请检查您的调用参数！");
        }

        if (!Objects.equals(testJudgeReq.getToken(), judgeToken))
        {
            return CommonResult.errorResponse("对不起！您使用的判题服务调用凭证不正确！访问受限！", ResultStatus.ACCESS_DENIED);
        }
        return CommonResult.successResponse(judgeService.testJudge(testJudgeReq));
    }

    @PostMapping(value = "/compile-spj")
    public CommonResult<Void> compileSpj(@RequestBody CompileDTO compileDTO)
    {
        if (!Objects.equals(compileDTO.getToken(), judgeToken))
        {
            return CommonResult.errorResponse("对不起！您使用的判题服务调用凭证不正确！访问受限！", ResultStatus.ACCESS_DENIED);
        }

        try
        {
            judgeService.compileSpj(compileDTO.getCode(), compileDTO.getPid(), compileDTO.getLanguage(), compileDTO.getExtraFiles());
            return CommonResult.successResponse(null, "编译成功！");
        } catch (SystemError systemError)
        {
            return CommonResult.errorResponse(systemError.getStderr(), ResultStatus.SYSTEM_ERROR);
        }
    }

}
