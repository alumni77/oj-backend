package com.zjedu.admin.feign;

import com.zjedu.admin.feign.fallback.ProblemFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author Zhong
 * @Create 2025/4/1 20:53
 * @Version 1.0
 * @Description
 */

@FeignClient(value = "oj-problem", path = "/api/problem", fallback = ProblemFeignClientFallback.class)
public interface ProblemFeignClient
{
}
