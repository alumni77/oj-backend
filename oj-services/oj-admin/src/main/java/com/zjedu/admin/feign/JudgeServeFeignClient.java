package com.zjedu.admin.feign;

import com.zjedu.admin.feign.fallback.JudgeServeFeignClientFallback;
import com.zjedu.pojo.dto.CompileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Zhong
 * @Create 2025/4/1 20:52
 * @Version 1.0
 * @Description
 */

@FeignClient(value = "oj-judgeserve", path = "/api/judgeserve", fallback = JudgeServeFeignClientFallback.class)
public interface JudgeServeFeignClient
{
    @PostMapping(value = "/send-task")
    void sendTask(@RequestParam("judgeId") Long judgeId,
                  @RequestParam("pid") Long pid,
                  @RequestParam("isContest") Boolean isContest);

    @PostMapping("/compile-spj")
    void compileSpj(@RequestBody CompileDTO compileDTO);

    @PostMapping("/compile-interactive")
    void compileInteractive(@RequestBody CompileDTO compileDTO);
}