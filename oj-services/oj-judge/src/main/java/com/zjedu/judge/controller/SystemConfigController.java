package com.zjedu.judge.controller;

import com.zjedu.judge.service.SystemConfigService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @Author Zhong
 * @Create 2025/3/25 20:27
 * @Version 1.0
 * @Description
 */

@RestController
public class SystemConfigController
{

    @Resource
    private SystemConfigService systemConfigService;

    @GetMapping("/get-sys-config")
    public HashMap<String, Object> getSystemConfig()
    {
        return systemConfigService.getSystemConfig();
    }
}