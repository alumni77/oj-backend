package com.zjedu.passport.controller;

import com.zjedu.annotation.AnonApi;
import com.zjedu.common.result.CommonResult;
import com.zjedu.passport.service.PassportService;
import com.zjedu.pojo.dto.LoginDTO;
import com.zjedu.pojo.dto.RegisterDTO;
import com.zjedu.pojo.vo.RegisterCodeVO;
import com.zjedu.pojo.vo.UserInfoVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Zhong
 * @Create 2025/3/19 22:01
 * @Version 1.0
 * @Description
 */

@RestController
public class PassportController
{
    @Resource
    private PassportService passportService;

    /**
     * 处理登录逻辑
     * @param loginDto
     * @return CommonResult
     */
    @PostMapping("/login")
    @AnonApi
    public CommonResult<UserInfoVO> login(@Validated @RequestBody LoginDTO loginDto, HttpServletResponse response, HttpServletRequest request)
    {
        return passportService.login(loginDto, response, request);
    }

    /**
     * 发送注册流程的6位随机验证码
     * @param username
     * @return
     */
    @GetMapping(value = "/get-register-code")
    @AnonApi
    public CommonResult<RegisterCodeVO> getRegisterCode(@RequestParam(value = "username", required = true) String username)
    {
        return passportService.getRegisterCode(username);
    }

    @PostMapping("/register")
    @AnonApi
    public CommonResult<Void> register(@Validated @RequestBody RegisterDTO registerDTO)
    {
        return passportService.register(registerDTO);
    }

}