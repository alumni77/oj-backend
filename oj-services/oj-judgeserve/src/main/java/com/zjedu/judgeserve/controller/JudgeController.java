package com.zjedu.judgeserve.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.annotation.AnonApi;
import com.zjedu.common.result.CommonResult;
import com.zjedu.judgeserve.service.JudgeService;
import com.zjedu.pojo.vo.JudgeVO;
import com.zjedu.pojo.vo.SubmissionInfoVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * @param limit
     * @param currentPage
     * @param onlyMine
     * @param searchPid
     * @param searchStatus
     * @param searchUsername
     * @param completeProblemID
     * @param gid
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
                                                     @RequestParam(value = "completeProblemID", defaultValue = "false") Boolean completeProblemID,
                                                     @RequestParam(value = "gid", required = false) Long gid)
    {

        return judgeService.getJudgeList(limit, currentPage, onlyMine, searchPid, searchStatus, searchUsername, completeProblemID, gid);
    }

    /**
     * 获取单个提交记录的详情
     * @param submitId
     * @return
     */
    @GetMapping("/get-submission-detail")
    @AnonApi
    public CommonResult<SubmissionInfoVO> getSubmission(@RequestParam(value = "submitId", required = true) Long submitId) {
        return judgeService.getSubmission(submitId);
    }


}
