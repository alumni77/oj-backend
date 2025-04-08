package com.zjedu.problem.feign;

import com.zjedu.pojo.vo.ProblemCountVO;
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

@FeignClient(value = "oj-judge", path = "/api/judge")
public interface JudgeFeignClient
{
    @GetMapping("/get-problem-list-by-pids")
    List<ProblemCountVO> getProblemListByPids(@RequestParam List<Long> pidList);

    @GetMapping("/get-problem-count-by-pid")
    ProblemCountVO getProblemCountByPid(@RequestParam("pid") Long pid);

}
