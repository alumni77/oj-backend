package com.zjedu.admin.service;

import cn.hutool.json.JSONObject;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.DBAndRedisConfigDTO;
import com.zjedu.pojo.dto.SwitchConfigDTO;
import com.zjedu.pojo.dto.WebConfigDTO;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/4/6 14:59
 * @Version 1.0
 * @Description
 */

public interface ConfigService
{
    CommonResult<JSONObject> getServiceInfo();

    CommonResult<List<JSONObject>> getJudgeServiceInfo();

    CommonResult<WebConfigDTO> getWebConfig();

    CommonResult<Void> deleteHomeCarousel(Long id);

    CommonResult<Void> setWebConfig(WebConfigDTO config);

    CommonResult<DBAndRedisConfigDTO> getDBAndRedisConfig();

    CommonResult<Void> setDBAndRedisConfig(DBAndRedisConfigDTO config);

    CommonResult<SwitchConfigDTO> getSwitchConfig();

    CommonResult<Void> setSwitchConfig(SwitchConfigDTO config);
}
