package com.zjedu.passport.controller;

import com.zjedu.annotation.AnonApi;
import com.zjedu.common.result.CommonResult;
import com.zjedu.passport.service.PassportService;
import com.zjedu.pojo.dto.LoginDTO;
import com.zjedu.pojo.vo.UserInfoVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Zhong
 * @Create 2025/3/16 21:35
 * @Version 1.0
 */

@RestController
public class PassportController
{
    @Resource
    private PassportService passportService;

    @PostMapping("/login")
    @AnonApi
    public CommonResult<UserInfoVO> login(@Validated @RequestBody LoginDTO loginDto, HttpServletResponse response, HttpServletRequest request) {
    {
        return passportService.login(loginDto,response,request);
    }
    }

}
