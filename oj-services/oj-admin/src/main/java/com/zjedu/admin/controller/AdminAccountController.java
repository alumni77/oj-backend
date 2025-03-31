package com.zjedu.admin.controller;

import com.zjedu.admin.service.AdminAccountService;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.LoginDTO;
import com.zjedu.pojo.vo.UserInfoVO;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Zhong
 * @Create 2025/3/31 15:45
 * @Version 1.0
 * @Description
 */

@RestController
public class AdminAccountController
{

    @Resource
    private AdminAccountService adminAccountService;

    @PostMapping("/login")
    public CommonResult<UserInfoVO> login(@Validated @RequestBody LoginDTO loginDto)
    {
        return adminAccountService.login(loginDto);
    }


    @GetMapping("/logout")
    public CommonResult<Void> logout()
    {
        return adminAccountService.logout();
    }

}
