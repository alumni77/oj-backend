package com.zjedu.file.feign;

import com.zjedu.file.feign.fallback.ProblemFeignClientFallback;
import com.zjedu.pojo.dto.ProblemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author Zhong
 * @Create 2025/3/31 12:03
 * @Version 1.0
 * @Description
 */

@FeignClient(value = "oj-problem", path = "/api/problem", fallback = ProblemFeignClientFallback.class)
public interface ProblemFeignClient
{
    @PostMapping("/admin-add-problem")
    boolean adminAddProblem(@RequestBody ProblemDTO problemDto);

}
