package com.zjedu.admin.service.impl;

import cn.hutool.json.JSONObject;
import com.zjedu.admin.manager.ConfigManager;
import com.zjedu.admin.service.ConfigService;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.pojo.dto.DBAndRedisConfigDTO;
import com.zjedu.pojo.dto.SwitchConfigDTO;
import com.zjedu.pojo.dto.WebConfigDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/4/6 15:00
 * @Version 1.0
 * @Description
 */

@Service
public class ConfigServiceImpl implements ConfigService
{
    @Resource
    private ConfigManager configManager;

    @Override
    public CommonResult<JSONObject> getServiceInfo()
    {
        try
        {
            return CommonResult.successResponse(configManager.getServiceInfo());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<List<JSONObject>> getJudgeServiceInfo()
    {
        try
        {
            return CommonResult.successResponse(configManager.getJudgeServiceInfo());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }

    }

    @Override
    public CommonResult<WebConfigDTO> getWebConfig()
    {
        try
        {
            return CommonResult.successResponse(configManager.getWebConfig());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }

    }

    @Override
    public CommonResult<Void> deleteHomeCarousel(Long id)
    {
        try
        {
            configManager.deleteHomeCarousel(id);
            return CommonResult.successResponse();
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }

    }

    @Override
    public CommonResult<Void> setWebConfig(WebConfigDTO config)
    {
        try
        {
            configManager.setWebConfig(config);
            return CommonResult.successResponse();
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<DBAndRedisConfigDTO> getDBAndRedisConfig()
    {
        try
        {
            return CommonResult.successResponse(configManager.getDBAndRedisConfig());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> setDBAndRedisConfig(DBAndRedisConfigDTO config)
    {
        try
        {
            configManager.setDBAndRedisConfig(config);
            return CommonResult.successResponse();
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<SwitchConfigDTO> getSwitchConfig()
    {
        try
        {
            return CommonResult.successResponse(configManager.getSwitchConfig());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Void> setSwitchConfig(SwitchConfigDTO config)
    {
        try
        {
            configManager.setSwitchConfig(config);
            return CommonResult.successResponse();
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }
}
