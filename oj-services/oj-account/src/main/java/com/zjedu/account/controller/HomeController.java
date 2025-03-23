package com.zjedu.account.controller;

import com.zjedu.account.service.HomeService;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.vo.ACMRankVO;
import com.zjedu.pojo.vo.RecentUpdatedProblemVO;
import com.zjedu.pojo.vo.SubmissionStatisticsVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/23 21:16
 * @Version 1.0
 * @Description 处理首页的请求
 */

@RestController
public class HomeController
{
    @Resource
    private HomeService homeService;

    /**
     * 获取主页轮播图
     *
     * @return
     */
    @GetMapping("/home-carousel")
    public CommonResult<List<HashMap<String, Object>>> getHomeCarousel()
    {
        return homeService.getHomeCarousel();
    }

    /**
     * 获取最近7天用户做题榜单
     *
     * @return
     */
    @GetMapping("/get-recent-seven-ac-rank")
    public CommonResult<List<ACMRankVO>> getRecentSevenACRank()
    {
        return homeService.getRecentSevenACRank();
    }

    /**
     * 获取最近前十更新的题目（不包括比赛题目、私有题目）
     *
     * @return
     */
    @GetMapping("/get-recent-updated-problem")
    public CommonResult<List<RecentUpdatedProblemVO>> getRecentUpdatedProblemList()
    {
        return homeService.getRecentUpdatedProblemList();
    }

    /**
     * 获取最近一周提交统计
     *
     * @param forceRefresh
     * @return
     */
    @GetMapping("/get-last-week-submission-statistics")
    public CommonResult<SubmissionStatisticsVO> getLastWeekSubmissionStatistics(
            @RequestParam(value = "forceRefresh", defaultValue = "false") Boolean forceRefresh)
    {
        return homeService.getLastWeekSubmissionStatistics(forceRefresh);
    }
}
