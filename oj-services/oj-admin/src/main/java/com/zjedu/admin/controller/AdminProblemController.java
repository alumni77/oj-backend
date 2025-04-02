package com.zjedu.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.admin.service.AdminProblemService;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.CompileDTO;
import com.zjedu.pojo.dto.ProblemDTO;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.entity.problem.ProblemCase;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/4/2 14:19
 * @Version 1.0
 * @Description
 */

@RestController
@RequestMapping("/problem")
public class AdminProblemController
{
    @Resource
    private AdminProblemService adminProblemService;

    @GetMapping("/get-problem-list")
    public CommonResult<IPage<Problem>> getProblemList(@RequestParam(value = "limit", required = false) Integer limit,
                                                       @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                       @RequestParam(value = "keyword", required = false) String keyword,
                                                       @RequestParam(value = "auth", required = false) Integer auth,
                                                       @RequestParam(value = "oj", required = false) String oj) throws StatusFailException
    {
        return adminProblemService.getProblemList(limit, currentPage, keyword, auth, oj);
    }

    @GetMapping("/")
    public CommonResult<Problem> getProblem(@RequestParam("pid") Long pid)
    {
        return adminProblemService.getProblem(pid);
    }

    @DeleteMapping("/")
    public CommonResult<Void> deleteProblem(@RequestParam("pid") Long pid)
    {
        return adminProblemService.deleteProblem(pid);
    }

    @PostMapping("/")
    public CommonResult<Void> addProblem(@RequestBody ProblemDTO problemDto)
    {
        return adminProblemService.addProblem(problemDto);
    }

    @PutMapping("/")
    public CommonResult<Void> updateProblem(@RequestBody ProblemDTO problemDto)
    {
        return adminProblemService.updateProblem(problemDto);
    }

    @GetMapping("/get-problem-cases")
    public CommonResult<List<ProblemCase>> getProblemCases(@RequestParam("pid") Long pid,
                                                           @RequestParam(value = "isUpload", defaultValue = "true") Boolean isUpload)
    {
        return adminProblemService.getProblemCases(pid, isUpload);
    }

    @PostMapping("/compile-spj")
    public CommonResult compileSpj(@RequestBody CompileDTO compileDTO)
    {
        return adminProblemService.compileSpj(compileDTO);
    }

    @PostMapping("/compile-interactive")
    public CommonResult compileInteractive(@RequestBody CompileDTO compileDTO)
    {
        return adminProblemService.compileInteractive(compileDTO);
    }

    @PutMapping("/change-problem-auth")
    public CommonResult<Void> changeProblemAuth(@RequestBody Problem problem)
    {
        return adminProblemService.changeProblemAuth(problem);
    }


}
