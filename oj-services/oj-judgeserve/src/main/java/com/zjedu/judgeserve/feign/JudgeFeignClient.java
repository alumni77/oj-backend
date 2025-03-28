package com.zjedu.judgeserve.feign;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.common.result.CommonResult;
import com.zjedu.judgeserve.feign.fallback.JudgeFeignClientFallback;
import com.zjedu.pojo.dto.CompileDTO;
import com.zjedu.pojo.dto.TestJudgeReq;
import com.zjedu.pojo.dto.TestJudgeRes;
import com.zjedu.pojo.dto.ToJudgeDTO;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.entity.judge.JudgeServer;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.vo.JudgeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/26 19:19
 * @Version 1.0
 * @Description
 */

@FeignClient(value = "oj-judge", path = "/api/judge",fallback = JudgeFeignClientFallback.class)
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
    IPage<JudgeVO> getCommonJudgeList(
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(value = "searchPid", required = false) String searchPid,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "uid", required = false) String uid,
            @RequestParam(value = "completeProblemID", defaultValue = "false") Boolean completeProblemID);

    @GetMapping("/get-judge-by-id")
    Judge getJudgeById(@RequestParam("submitId") Long submitId);

    @GetMapping("/get-problem-by-id")
    Problem getProblemById(@RequestParam("id") Long id);

    @GetMapping("/query-problem-by-problemId")
    Problem queryProblemByPId(@RequestParam("problemId") String problemId);

    @PostMapping("/save-judge")
    Judge saveJudge(@RequestBody Judge judge);

    @GetMapping("/query-judge-server-by-urls")
    List<JudgeServer> getJudgeServerList(@RequestParam List<String> urls, @RequestParam Boolean isRemote);

    @PostMapping("/update-judge-server-by-id")
    boolean updateJudgeServerById(@RequestBody JudgeServer judgeServer);

    @PostMapping("/update-judge-server-by-wrapper")
    boolean updateJudgeServerByWrapper(@RequestBody UpdateWrapper<JudgeServer> updateWrapper);

    @PostMapping("/update-judge-by-id")
    boolean updateJudgeById(@RequestBody Judge judge);

    @PostMapping("/fail-to-use-redis-publish-judge")
    boolean failToUseRedisPublishJudge(
            @RequestParam("submitId") Long submitId,
            @RequestParam("pid") Long pid,
            @RequestParam("isContest") Boolean isContest);


    @GetMapping("/get-judge-info")
    Judge getJudgeInfo(@RequestParam("submitId") Long submitId);

    @PostMapping("/update-judge-share")
    boolean updateJudgeShare(@RequestParam("submitId") Long submitId, @RequestParam("share") Boolean share);

    @GetMapping("/get-judge-list-by-ids")
    List<Judge> getJudgeListByIds(@RequestParam List<Long> submitIds);

}
