package com.zjedu.account.service.impl;

import com.zjedu.account.manager.HomeManager;
import com.zjedu.account.service.HomeService;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.vo.ACMRankVO;
import com.zjedu.pojo.vo.RecentUpdatedProblemVO;
import com.zjedu.pojo.vo.SubmissionStatisticsVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/23 21:17
 * @Version 1.0
 * @Description
 */

@Service
public class HomeServiceImpl implements HomeService
{

    @Resource
    private HomeManager homeManager;

    @Override
    public CommonResult<List<HashMap<String, Object>>> getHomeCarousel()
    {
        return CommonResult.successResponse(homeManager.getHomeCarousel());
    }

    @Override
    public CommonResult<List<ACMRankVO>> getRecentSevenACRank()
    {
        return CommonResult.successResponse(homeManager.getRecentSevenACRank());
    }

    @Override
    public CommonResult<List<RecentUpdatedProblemVO>> getRecentUpdatedProblemList()
    {
        return CommonResult.successResponse(homeManager.getRecentUpdatedProblemList());
    }

    @Override
    public CommonResult<SubmissionStatisticsVO> getLastWeekSubmissionStatistics(Boolean forceRefresh)
    {
        return CommonResult.successResponse(homeManager.getLastWeekSubmissionStatistics(forceRefresh));
    }
}
