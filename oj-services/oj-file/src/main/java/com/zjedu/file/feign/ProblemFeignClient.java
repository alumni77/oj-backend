package com.zjedu.file.feign;

import com.zjedu.file.feign.fallback.ProblemFeignClientFallback;
import com.zjedu.pojo.dto.ExportProblemParamsDTO;
import com.zjedu.pojo.dto.ProblemDTO;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.vo.ImportProblemVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/build-export-problem")
    ImportProblemVO buildExportProblem(@RequestParam("pid") Long pid,
                                       @RequestBody ExportProblemParamsDTO params);

    @GetMapping("/get-problem-by-pid")
    Problem getProblemByPid(@RequestParam("id") Long pid);
}
