package com.zjedu.common.service;

import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.vo.CaptchaVO;

/**
 * @Author Zhong
 * @Create 2025/3/30 13:41
 * @Version 1.0
 * @Description
 */

public interface CommonService
{
    CommonResult<CaptchaVO> getCaptcha();
}
