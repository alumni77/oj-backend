package com.zjedu.admin.service.impl;

import com.zjedu.admin.manager.DashboardManager;
import com.zjedu.admin.service.DashboardService;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.pojo.entity.user.Session;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author Zhong
 * @Create 2025/4/6 19:57
 * @Version 1.0
 * @Description
 */

@Service
public class DashboardServiceImpl implements DashboardService
{
    @Resource
    private DashboardManager dashboardManager;

    @Override
    public CommonResult<Session> getRecentSession()
    {
        try
        {
            return CommonResult.successResponse(dashboardManager.getRecentSession());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Map<Object, Object>> getDashboardInfo()
    {
        try
        {
            return CommonResult.successResponse(dashboardManager.getDashboardInfo());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }

    }
}
