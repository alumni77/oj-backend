package com.zjedu.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.admin.service.AdminProblemService;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.entity.problem.Problem;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
