package com.zjedu.admin.controller;

import com.zjedu.admin.service.DashboardService;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.entity.user.Session;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author Zhong
 * @Create 2025/4/6 19:56
 * @Version 1.0
 * @Description
 */

@RestController
@RequestMapping("/dashboard")
public class DashboardController
{
    @Resource
    private DashboardService dashboardService;


    @GetMapping("/get-sessions")
    public CommonResult<Session> getRecentSession()
    {
        return dashboardService.getRecentSession();
    }

    @GetMapping("/get-dashboard-info")
    public CommonResult<Map<Object, Object>> getDashboardInfo()
    {
        return dashboardService.getDashboardInfo();
    }
}
