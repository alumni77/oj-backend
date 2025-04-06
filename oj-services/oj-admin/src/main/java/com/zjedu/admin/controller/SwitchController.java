package com.zjedu.admin.controller;

import com.zjedu.admin.service.ConfigService;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.SwitchConfigDTO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Zhong
 * @Create 2025/4/6 19:45
 * @Version 1.0
 * @Description
 */

@RestController
@RequestMapping("/switch")
public class SwitchController
{
    @Resource
    private ConfigService configService;

    @GetMapping("/info")
    public CommonResult<SwitchConfigDTO> getSwitchConfig()
    {
        return configService.getSwitchConfig();
    }

    @PutMapping("/update")
    public CommonResult<Void> setSwitchConfig(@RequestBody SwitchConfigDTO config)
    {
        return configService.setSwitchConfig(config);
    }
}
