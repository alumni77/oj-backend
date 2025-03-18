package com.zjedu.passport.controller;

import com.zjedu.annotation.AnonApi;
import com.zjedu.common.result.CommonResult;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Zhong
 * @Create 2025/3/16 21:35
 * @Version 1.0
 */

@RestController
public class PassportController
{

    @GetMapping("/login")
    @AnonApi
    public CommonResult<T> login()
    {
        return CommonResult.successResponse("登录成功");
    }

}
