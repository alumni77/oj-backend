package com.zjedu.admin.service.impl;

import com.zjedu.admin.manager.AdminAccountManager;
import com.zjedu.admin.service.AdminAccountService;
import com.zjedu.common.exception.StatusAccessDeniedException;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.pojo.dto.LoginDTO;
import com.zjedu.pojo.vo.UserInfoVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/31 15:46
 * @Version 1.0
 * @Description
 */

@Service
public class AdminAccountServiceImpl implements AdminAccountService
{
    @Resource
    private AdminAccountManager adminAccountManager;

    @Override
    public CommonResult<UserInfoVO> login(LoginDTO loginDto)
    {
        try
        {
            return CommonResult.successResponse(adminAccountManager.login(loginDto));
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusAccessDeniedException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.ACCESS_DENIED);
        }
    }

    @Override
    public CommonResult<Void> logout()
    {
        adminAccountManager.logout();
        return CommonResult.successResponse("退出登录成功！");
    }

}
