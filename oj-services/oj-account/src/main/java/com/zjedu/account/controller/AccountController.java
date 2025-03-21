package com.zjedu.account.controller;

import com.zjedu.account.service.AccountService;
import com.zjedu.annotation.AnonApi;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.CheckUsernameDTO;
import com.zjedu.pojo.vo.CheckUsernameVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Zhong
 * @Create 2025/3/20 15:59
 * @Version 1.0
 * @Description 主要负责处理账号的相关操作
 */

@RestController
public class AccountController
{
    @Resource
    private AccountService accountService;

    @PostMapping("/check-username")
    @AnonApi
    public CommonResult<CheckUsernameVO>  checkUsername (@RequestBody CheckUsernameDTO checkUsernameDTO)
    {
        return accountService.checkUsername(checkUsernameDTO);
    }

}
