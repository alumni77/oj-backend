package com.zjedu.judgeserve.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.annotation.AnonApi;
import com.zjedu.common.result.CommonResult;
import com.zjedu.judgeserve.judge.Dispatcher;
import com.zjedu.judgeserve.judge.self.JudgeDispatcher;
import com.zjedu.judgeserve.service.JudgeService;
import com.zjedu.pojo.dto.CompileDTO;
import com.zjedu.pojo.dto.SubmitIdListDTO;
import com.zjedu.pojo.dto.SubmitJudgeDTO;
import com.zjedu.pojo.dto.TestJudgeDTO;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.vo.JudgeCaseVO;
import com.zjedu.pojo.vo.JudgeVO;
import com.zjedu.pojo.vo.SubmissionInfoVO;
import com.zjedu.pojo.vo.TestJudgeVO;
import com.zjedu.utils.Constants;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @Author Zhong
 * @Create 2025/3/25 22:09
 * @Version 1.0
 * @Description
 */

@RestController
public class JudgeController
{
    @Resource
    private JudgeService judgeService;

    /**
     * 通用查询判题记录列表
     *
     * @param limit
     * @param currentPage
     * @param onlyMine
     * @param searchPid
     * @param searchStatus
     * @param searchUsername
     * @param completeProblemID
     * @return
     */
    @GetMapping("/get-submission-list")
    @AnonApi
    public CommonResult<IPage<JudgeVO>> getJudgeList(@RequestParam(value = "limit", required = false) Integer limit,
                                                     @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                     @RequestParam(value = "onlyMine", required = false) Boolean onlyMine,
                                                     @RequestParam(value = "problemID", required = false) String searchPid,
                                                     @RequestParam(value = "status", required = false) Integer searchStatus,
                                                     @RequestParam(value = "username", required = false) String searchUsername,
                                                     @RequestParam(value = "completeProblemID", defaultValue = "false") Boolean completeProblemID)
    {

        return judgeService.getJudgeList(limit, currentPage, onlyMine, searchPid, searchStatus, searchUsername, completeProblemID);
    }

    /**
     * 获取单个提交记录的详情
     *
     * @param submitId
     * @return
     */
    @GetMapping("/get-submission-detail")
    @AnonApi
    public CommonResult<SubmissionInfoVO> getSubmission(@RequestParam(value = "submitId", required = true) Long submitId)
    {
        return judgeService.getSubmission(submitId);
    }

    /**
     * @MethodName submitProblemJudge
     * @Description 核心方法 判题就此开始
     * @Return CommonResult
     * @Since 2020/10/30
     */
    @PostMapping(value = "/submit-problem-judge")
    public CommonResult<Judge> submitProblemJudge(@RequestBody SubmitJudgeDTO judgeDto)
    {
        return judgeService.submitProblemJudge(judgeDto);
    }

    @PostMapping(value = "/submit-problem-test-judge")
    public CommonResult<String> submitProblemTestJudge(@RequestBody TestJudgeDTO testJudgeDto)
    {
        return judgeService.submitProblemTestJudge(testJudgeDto);
    }

    @GetMapping("/get-test-judge-result")
    public CommonResult<TestJudgeVO> getTestJudgeResult(@RequestParam("testJudgeKey") String testJudgeKey)
    {
        return judgeService.getTestJudgeResult(testJudgeKey);
    }

    /**
     * 调用判题服务器提交失败超过60s后，用户点击按钮重新提交判题进入的方法
     *
     * @param submitId
     * @return
     */
    @GetMapping(value = "/resubmit")
    public CommonResult<Judge> resubmit(@RequestParam("submitId") Long submitId)
    {
        return judgeService.resubmit(submitId);
    }

    /**
     * 修改单个提交详情的分享权限
     *
     * @param judge
     * @return
     */
    @PutMapping("/submission")
    public CommonResult<Boolean> updateSubmission(@RequestBody Judge judge)
    {
        return judgeService.updateSubmission(judge);
    }

    /**
     * 对提交列表状态为Pending和Judging的提交进行更新检查
     *
     * @param submitIdListDto
     * @return
     */
    @PostMapping(value = "/check-submissions-status")
    @AnonApi
    public CommonResult<HashMap<Long, Object>> checkCommonJudgeResult(@RequestBody SubmitIdListDTO submitIdListDto)
    {
        return judgeService.checkCommonJudgeResult(submitIdListDto);
    }

    /**
     * 获得指定提交id的测试样例结果，暂不支持查看测试数据，只可看测试点结果，时间，空间，或者IO得分
     *
     * @param submitId
     * @return
     */
    @GetMapping("/get-all-case-result")
    @AnonApi
    public CommonResult<JudgeCaseVO> getALLCaseResult(@RequestParam(value = "submitId", required = true) Long submitId)
    {
        return judgeService.getALLCaseResult(submitId);
    }

    //外露接口给openFeign调用
    @Resource
    private JudgeDispatcher judgeDispatcher;

    @PostMapping(value = "/send-task")
    public void sendTask(@RequestParam("judgeId") Long judgeId,
                         @RequestParam("pid") Long pid,
                         @RequestParam("isContest") Boolean isContest)
    {
        judgeDispatcher.sendTask(judgeId, pid, isContest);
    }

    @Resource
    private Dispatcher dispatcher;

    @PostMapping("/compile-spj")
    public void compileSpj(@RequestBody CompileDTO compileDTO)
    {
        dispatcher.dispatch(Constants.TaskType.COMPILE_INTERACTIVE, compileDTO);
    }

    @PostMapping("/compile-interactive")
    public void compileInteractive(@RequestBody CompileDTO compileDTO)
    {
        dispatcher.dispatch(Constants.TaskType.COMPILE_INTERACTIVE, compileDTO);
    }

}
