package com.zjedu.problem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.annotation.AnonApi;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.ExportProblemParamsDTO;
import com.zjedu.pojo.dto.PidListDTO;
import com.zjedu.pojo.dto.ProblemDTO;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.vo.*;
import com.zjedu.problem.dao.ProblemEntityService;
import com.zjedu.problem.service.ProblemService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/28 20:45
 * @Version 1.0
 * @Description
 */

@RestController
public class ProblemController
{
    @Resource
    private ProblemService problemService;

    /**
     * 获取题目列表分页
     *
     * @param limit
     * @param currentPage
     * @param keyword
     * @param tagId
     * @param difficulty
     * @param oj
     * @return
     */
    @GetMapping(value = "/get-problem-list")
    @AnonApi
    public CommonResult<Page<ProblemVO>> getProblemList(@RequestParam(value = "limit", required = false) Integer limit,
                                                        @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                        @RequestParam(value = "keyword", required = false) String keyword,
                                                        @RequestParam(value = "tagId", required = false) List<Long> tagId,
                                                        @RequestParam(value = "difficulty", required = false) Integer difficulty,
                                                        @RequestParam(value = "oj", required = false) String oj)
    {

        return problemService.getProblemList(limit, currentPage, keyword, tagId, difficulty, oj);
    }

    /**
     * 随机选取一道题目
     *
     * @return
     */
    @GetMapping("/get-random-problem")
    @AnonApi
    public CommonResult<RandomProblemVO> getRandomProblem()
    {
        return problemService.getRandomProblem();
    }

    /**
     * 获取用户对应该题目列表中各个题目的做题情况
     *
     * @param pidListDto
     * @return
     */
    @PostMapping("/get-user-problem-status")
    public CommonResult<HashMap<Long, Object>> getUserProblemStatus(@Validated @RequestBody PidListDTO pidListDto)
    {
        return problemService.getUserProblemStatus(pidListDto);
    }

    /**
     * 获取指定题目的详情信息，标签，所支持语言，做题情况（只能查询公开题目 也就是auth为1）
     *
     * @param problemId
     * @return
     */
    @GetMapping(value = "/get-problem-detail")
    @AnonApi
    public CommonResult<ProblemInfoVO> getProblemInfo(@RequestParam(value = "problemId", required = true) String problemId)
    {
        return problemService.getProblemInfo(problemId);
    }

    /**
     * 获取用户对于该题最近AC的代码
     *
     * @param pid
     * @return
     */
    @GetMapping("/get-last-ac-code")
    public CommonResult<LastAcceptedCodeVO> getUserLastAcceptedCode(@RequestParam(value = "pid") Long pid)
    {
        return problemService.getUserLastAcceptedCode(pid);
    }


    /**
     * 获取专注模式页面底部的题目列表
     *
     * @param tid
     * @return
     */
    @GetMapping("/get-full-screen-problem-list")
    public CommonResult<List<ProblemFullScreenListVO>> getFullScreenProblemList(@RequestParam(value = "tid", required = false) Long tid)
    {
        return problemService.getFullScreenProblemList(tid);
    }

    // 外露接口给openFeign调用
    @Resource
    private ProblemEntityService problemEntityService;

    @PostMapping("/admin-add-problem")
    public boolean adminAddProblem(@RequestBody ProblemDTO problemDto)
    {
        return problemEntityService.adminAddProblem(problemDto);
    }

    @PostMapping("/build-export-problem")
    public ImportProblemVO buildExportProblem(@RequestParam("pid") Long pid,
                                              @RequestBody ExportProblemParamsDTO params)
    {
        return problemEntityService.buildExportProblem(pid,
                params.getProblemCaseList(),
                params.getLanguageMap(),
                params.getTagMap());
    }

    @GetMapping("/get-problem-by-pid")
    public Problem getProblemByPid(@RequestParam("id") Long pid)
    {
        return problemEntityService.getById(pid);
    }


}
