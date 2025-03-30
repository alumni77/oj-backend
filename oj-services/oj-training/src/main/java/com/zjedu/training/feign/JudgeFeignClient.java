package com.zjedu.training.feign;

import com.zjedu.training.feign.fallback.JudgeFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/30 16:35
 * @Version 1.0
 * @Description
 */

@FeignClient(value = "oj-judge", path = "/api/judge", fallback = JudgeFeignClientFallback.class)
public interface JudgeFeignClient
{
    @GetMapping("/get-ac-problem-count")
    Integer getACProblemCount(@RequestParam List<Long> pidList, @RequestParam("uid") String uid, @RequestParam("status") Integer status);
}
