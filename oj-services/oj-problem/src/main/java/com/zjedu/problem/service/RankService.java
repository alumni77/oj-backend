package com.zjedu.problem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.common.result.CommonResult;

/**
 * @Author Zhong
 * @Create 2025/3/29 16:07
 * @Version 1.0
 * @Description
 */

public interface RankService
{
    CommonResult<IPage> getRankList(Integer limit, Integer currentPage, String searchUser, Integer type);
}
