package com.zjedu.judge.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.judge.common.exception.SystemError;
import com.zjedu.judge.dao.JudgeEntityService;
import com.zjedu.judge.dao.JudgeServerEntityService;
import com.zjedu.judge.service.JudgeService;
import com.zjedu.pojo.dto.CompileDTO;
import com.zjedu.pojo.dto.TestJudgeReq;
import com.zjedu.pojo.dto.TestJudgeRes;
import com.zjedu.pojo.dto.ToJudgeDTO;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.vo.JudgeVO;
import com.zjedu.pojo.vo.ProblemCountVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @Author Zhong
 * @Create 2025/3/24 21:44
 * @Version 1.0
 * @Description 处理代码提交
 */
@RestController
@RefreshScope
@Slf4j
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
                || !StringUtils.hasText(testJudgeReq.getCode())
                || !StringUtils.hasText(testJudgeReq.getLanguage())
                || !StringUtils.hasText(testJudgeReq.getUniqueKey())
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

    @PostMapping(value = "/compile-interactive")
    public CommonResult<Void> compileInteractive(@RequestBody CompileDTO compileDTO)
    {

        if (!Objects.equals(compileDTO.getToken(), judgeToken))
        {
            return CommonResult.errorResponse("对不起！您使用的判题服务调用凭证不正确！访问受限！", ResultStatus.ACCESS_DENIED);
        }

        try
        {
            judgeService.compileInteractive(compileDTO.getCode(), compileDTO.getPid(), compileDTO.getLanguage(), compileDTO.getExtraFiles());
            return CommonResult.successResponse(null, "编译成功！");
        } catch (SystemError systemError)
        {
            return CommonResult.errorResponse(systemError.getStderr(), ResultStatus.SYSTEM_ERROR);
        }
    }

    // 外露给openFeign调用
    @Resource
    private JudgeEntityService judgeEntityService;

    @GetMapping("/common-judge-list")
    public Page<JudgeVO> getCommonJudgeList(
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(value = "searchPid", required = false) String searchPid,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "uid", required = false) String uid,
            @RequestParam(value = "completeProblemID", defaultValue = "false") Boolean completeProblemID)
    {
        return (Page<JudgeVO>) judgeEntityService.getCommonJudgeList(
                limit, currentPage, searchPid, status, username, uid, completeProblemID);
    }


    @PostMapping("/fail-to-use-redis-publish-judge")
    public boolean failToUseRedisPublishJudge(
            @RequestParam("submitId") Long submitId,
            @RequestParam("pid") Long pid,
            @RequestParam("isContest") Boolean isContest)
    {
        judgeEntityService.failToUseRedisPublishJudge(submitId, pid, isContest);
        return true;
    }

    @GetMapping("/get-problem-list-by-pids")
    public List<ProblemCountVO> getProblemListByPids(@RequestParam List<Long> pidList)
    {
        log.info("pidList: {}", pidList);
        return judgeEntityService.getProblemListCount(pidList);
    }

    @GetMapping("/get-problem-count-by-pid")
    public ProblemCountVO getProblemCountByPid(@RequestParam("pid") Long pid)
    {
        return judgeEntityService.getProblemCount(pid);
    }

    @GetMapping("/get-today-judge-num")
    public Integer getTodayJudgeNum()
    {
        return judgeEntityService.getTodayJudgeNum();
    }

}
