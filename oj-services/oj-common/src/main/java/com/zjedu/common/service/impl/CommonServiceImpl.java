package com.zjedu.common.service.impl;

import com.zjedu.common.manager.CommonManager;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.service.CommonService;
import com.zjedu.pojo.vo.CaptchaVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/30 13:41
 * @Version 1.0
 * @Description
 */

@Service
public class CommonServiceImpl implements CommonService
{
    @Resource
    private CommonManager commonManager;

    @Override
    public CommonResult<CaptchaVO> getCaptcha()
    {
        return CommonResult.successResponse(commonManager.getCaptcha());
    }
}
