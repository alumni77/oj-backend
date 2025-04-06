package com.zjedu.admin.controller;

import cn.hutool.json.JSONObject;
import com.zjedu.admin.service.ConfigService;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.DBAndRedisConfigDTO;
import com.zjedu.pojo.dto.WebConfigDTO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/4/6 14:59
 * @Version 1.0
 * @Description
 */

@RestController
@RequestMapping("/config")
public class ConfigController
{
    @Resource
    private ConfigService configService;

    /**
     * 获取当前服务的相关信息以及当前系统的cpu情况，内存使用情况
     *
     * @return
     */
    @GetMapping("/get-service-info")
    public CommonResult<JSONObject> getServiceInfo()
    {
        return configService.getServiceInfo();
    }

    @GetMapping("/get-judge-service-info")
    public CommonResult<List<JSONObject>> getJudgeServiceInfo()
    {
        return configService.getJudgeServiceInfo();
    }

    @GetMapping("/get-web-config")
    public CommonResult<WebConfigDTO> getWebConfig()
    {
        return configService.getWebConfig();
    }

    @DeleteMapping("/home-carousel")
    public CommonResult<Void> deleteHomeCarousel(@RequestParam("id") Long id)
    {
        return configService.deleteHomeCarousel(id);
    }

    @PostMapping(value = "/set-web-config")
    public CommonResult<Void> setWebConfig(@RequestBody WebConfigDTO config)
    {
        return configService.setWebConfig(config);
    }

    @GetMapping("/get-db-and-redis-config")
    public CommonResult<DBAndRedisConfigDTO> getDBAndRedisConfig()
    {
        return configService.getDBAndRedisConfig();
    }

    @PutMapping("/set-db-and-redis-config")
    public CommonResult<Void> setDBAndRedisConfig(@RequestBody DBAndRedisConfigDTO config)
    {
        return configService.setDBAndRedisConfig(config);
    }
}
