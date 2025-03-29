package com.zjedu.problem.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.annotation.AnonApi;
import com.zjedu.common.result.CommonResult;
import com.zjedu.problem.service.RankService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Zhong
 * @Create 2025/3/29 16:07
 * @Version 1.0
 * @Description
 */

@RestController
@AnonApi
public class RankController
{

    @Resource
    private RankService rankService;

    /**
     * 获取排行榜数据
     *
     * @param limit
     * @param currentPage
     * @param searchUser
     * @param type
     * @return
     */
    @GetMapping("/get-rank-list")
    public CommonResult<IPage> getRankList(@RequestParam(value = "limit", required = false) Integer limit,
                                           @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                           @RequestParam(value = "searchUser", required = false) String searchUser,
                                           @RequestParam(value = "type") Integer type)
    {
        return rankService.getRankList(limit, currentPage, searchUser, type);
    }
}
