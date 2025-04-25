package com.zjedu.problem.feign;

import com.zjedu.pojo.vo.ProblemCountVO;
import com.zjedu.problem.feign.fallback.JudgeFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/28 20:58
 * @Version 1.0
 * @Description
 */

@FeignClient(value = "oj-judge", url = "http://47.98.112.208:8020",path = "/api/judge",fallback = JudgeFeignClientFallback.class)
public interface JudgeFeignClient
{
    @GetMapping("/get-problem-list-by-pids")
    List<ProblemCountVO> getProblemListByPids(@RequestParam List<Long> pidList);

    @GetMapping("/get-problem-count-by-pid")
    ProblemCountVO getProblemCountByPid(@RequestParam("pid") Long pid);

}
