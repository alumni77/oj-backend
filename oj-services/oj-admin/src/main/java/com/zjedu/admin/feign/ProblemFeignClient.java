package com.zjedu.admin.feign;

import com.zjedu.admin.feign.fallback.ProblemFeignClientFallback;
import com.zjedu.pojo.dto.ProblemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author Zhong
 * @Create 2025/4/1 20:53
 * @Version 1.0
 * @Description
 */

@FeignClient(value = "oj-problem", path = "/api/problem", fallback = ProblemFeignClientFallback.class)
public interface ProblemFeignClient
{
    @PostMapping("/admin-add-problem")
    boolean adminAddProblem(@RequestBody ProblemDTO problemDto);

    @PostMapping("/admin-update-problem")
    boolean adminUpdateProblem(@RequestBody ProblemDTO problemDto);
}
