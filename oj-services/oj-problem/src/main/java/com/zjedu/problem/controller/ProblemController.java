package com.zjedu.problem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.annotation.AnonApi;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.PidListDTO;
import com.zjedu.pojo.vo.ProblemVO;
import com.zjedu.pojo.vo.RandomProblemVO;
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
}
