package com.zjedu.common.controller;

import com.zjedu.annotation.AnonApi;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.service.CommonService;
import com.zjedu.pojo.vo.CaptchaVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Zhong
 * @Create 2025/3/30 13:41
 * @Version 1.0
 * @Description
 */

@RestController
public class CommonController
{

    @Resource
    private CommonService commonService;

    @GetMapping("/captcha")
    @AnonApi
    public CommonResult<CaptchaVO> getCaptcha() {
        return commonService.getCaptcha();
    }

    //TODO 需要训练模块相关服务
//    @GetMapping("/get-training-category")
//    @AnonApi
//    public CommonResult<List<TrainingCategory>> getTrainingCategory() {
//        return commonService.getTrainingCategory();
//    }

}
