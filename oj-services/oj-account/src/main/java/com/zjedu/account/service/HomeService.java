package com.zjedu.account.service;

import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.vo.ACMRankVO;
import com.zjedu.pojo.vo.RecentUpdatedProblemVO;
import com.zjedu.pojo.vo.SubmissionStatisticsVO;

import java.util.HashMap;
import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/23 21:16
 * @Version 1.0
 * @Description
 */

public interface HomeService
{
    CommonResult<List<HashMap<String, Object>>> getHomeCarousel();

    CommonResult<List<ACMRankVO>> getRecentSevenACRank();

    CommonResult<List<RecentUpdatedProblemVO>> getRecentUpdatedProblemList();

    CommonResult<SubmissionStatisticsVO> getLastWeekSubmissionStatistics(Boolean forceRefresh);
}
