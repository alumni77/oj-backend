package com.zjedu.judgeserve.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.common.result.CommonResult;
import com.zjedu.judgeserve.feign.fallback.JudgeFeignClientFallback;
import com.zjedu.pojo.dto.CompileDTO;
import com.zjedu.pojo.dto.TestJudgeReq;
import com.zjedu.pojo.dto.TestJudgeRes;
import com.zjedu.pojo.dto.ToJudgeDTO;
import com.zjedu.pojo.vo.JudgeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Zhong
 * @Create 2025/3/26 19:19
 * @Version 1.0
 * @Description
 */

@FeignClient(value = "oj-judge", url = "http://47.98.112.208:8020",path = "/api/judge",fallback = JudgeFeignClientFallback.class)
public interface JudgeFeignClient
{
    // 判题机调用
    @PostMapping("/")
    CommonResult<Void> submitProblemJudge(@RequestBody ToJudgeDTO toJudgeDTO);

    @PostMapping(value = "/test-judge")
    CommonResult<TestJudgeRes> submitProblemTestJudge(@RequestBody TestJudgeReq testJudgeReq);

    @PostMapping(value = "/compile-spj")
    CommonResult<Void> compileSpj(@RequestBody CompileDTO compileDTO);

    @PostMapping(value = "/compile-interactive")
    CommonResult<Void> compileInteractive(@RequestBody CompileDTO compileDTO);

    @GetMapping("/common-judge-list")
    Page<JudgeVO> getCommonJudgeList(
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(value = "searchPid", required = false) String searchPid,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "uid", required = false) String uid,
            @RequestParam(value = "completeProblemID", defaultValue = "false") Boolean completeProblemID);

    @PostMapping("/fail-to-use-redis-publish-judge")
    boolean failToUseRedisPublishJudge(
            @RequestParam("submitId") Long submitId,
            @RequestParam("pid") Long pid,
            @RequestParam("isContest") Boolean isContest);

}
