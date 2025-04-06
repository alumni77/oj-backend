package com.zjedu.admin.service;

import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.entity.user.Session;

import java.util.Map;

/**
 * @Author Zhong
 * @Create 2025/4/6 19:57
 * @Version 1.0
 * @Description
 */

public interface DashboardService
{
    CommonResult<Session> getRecentSession();

    CommonResult<Map<Object, Object>> getDashboardInfo();

}
