package com.zjedu.problem.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.problem.manager.RankManager;
import com.zjedu.problem.service.RankService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/29 16:08
 * @Version 1.0
 * @Description
 */

@Service
public class RankServiceImpl implements RankService
{

    @Resource
    private RankManager rankManager;

    @Override
    public CommonResult<IPage> getRankList(Integer limit, Integer currentPage, String searchUser, Integer type)
    {
        try
        {
            return CommonResult.successResponse(rankManager.getRankList(limit, currentPage, searchUser, type));
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        }
    }
}
