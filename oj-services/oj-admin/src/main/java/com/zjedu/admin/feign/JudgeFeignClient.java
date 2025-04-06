package com.zjedu.admin.feign;

import com.zjedu.admin.feign.fallback.JudgeFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author Zhong
 * @Create 2025/4/6 20:11
 * @Version 1.0
 * @Description
 */

@FeignClient(value = "oj-judge", path = "/api/judge", fallback = JudgeFeignClientFallback.class)
public interface JudgeFeignClient
{
    @GetMapping("/get-today-judge-num")
    Integer getTodayJudgeNum();
}
