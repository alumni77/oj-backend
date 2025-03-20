package com.zjedu.passport.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import com.zjedu.annotation.AnonApi;
import com.zjedu.common.result.CommonResult;
import com.zjedu.passport.service.PassportService;
import com.zjedu.pojo.dto.LoginDTO;
import com.zjedu.pojo.dto.RegisterDTO;
import com.zjedu.pojo.dto.ResetPasswordDTO;
import com.zjedu.pojo.vo.CodeVO;
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
     *
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
     * 发送 6 位随机验证码
     *
     * @param username
     * @return
     */
    @GetMapping(value = "/get-code")
    @AnonApi
    public CommonResult<CodeVO> getCode(@RequestParam(value = "username") String username)
    {
        return passportService.getCode(username);
    }

    /**
     * 注册逻辑
     *
     * @param registerDTO
     * @return
     */
    @PostMapping("/register")
    @AnonApi
    public CommonResult<Void> register(@Validated @RequestBody RegisterDTO registerDTO)
    {
        return passportService.register(registerDTO);
    }

    /**
     * 用户重置密码
     *
     * @param resetPasswordDTO
     * @return
     */
    @PostMapping("/reset-password")
    @AnonApi
    public CommonResult<Void> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO)
    {
        return passportService.resetPassword(resetPasswordDTO);
    }

    /**
     * 退出逻辑，将jwt在redis中清除，下次需要再次登录。
     *
     * @return
     */
    @GetMapping("/logout")
    @RequiresAuthentication
    public CommonResult<Void> logout()
    {
        return passportService.logout();
    }
}