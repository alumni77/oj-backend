package com.zjedu.judgeserve.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.vo.JudgeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Zhong
 * @Create 2025/3/26 19:19
 * @Version 1.0
 * @Description
 */

@FeignClient(value = "oj-judge", path = "/api/judge")
public interface JudgeFeignClient
{
    @GetMapping("/api/judge/common-judge-list")
    IPage<JudgeVO> getCommonJudgeList(
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "currentPage", required = false) Integer currentPage,
            @RequestParam(value = "searchPid", required = false) String searchPid,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "uid", required = false) String uid,
            @RequestParam(value = "completeProblemID", required = false) Boolean completeProblemID,
            @RequestParam(value = "gid", required = false) Long gid);

    @GetMapping("/get-judge-by-id")
    Judge getJudgeById(@RequestParam("submitId") Long submitId);

    @GetMapping("/get-problem-by-id")
    Problem getProblemById(@RequestParam("pid") Long pid);
}
