package com.zjedu.account.service.impl;

import com.zjedu.account.manager.AccountManager;
import com.zjedu.account.service.AccountService;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusSystemErrorException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.pojo.dto.ChangePasswordDTO;
import com.zjedu.pojo.dto.CheckUsernameDTO;
import com.zjedu.pojo.vo.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/20 18:26
 * @Version 1.0
 * @Description
 */

@Service
public class AccountServiceImpl implements AccountService
{
    @Resource
    private AccountManager accountManager;

    @Override
    public CommonResult<CheckUsernameVO> checkUsername(CheckUsernameDTO checkUsernameDTO)
    {
        return CommonResult.successResponse(accountManager.checkUsername(checkUsernameDTO));
    }

    @Override
    public CommonResult<UserHomeVO> getUserHomeInfo(String uid, String username)
    {
        try
        {
            return CommonResult.successResponse(accountManager.getUserHomeInfo(uid, username));
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<UserCalendarHeatmapVO> getUserCalendarHeatmap(String uid, String username)
    {
        try
        {
            return CommonResult.successResponse(accountManager.getUserCalendarHeatmap(uid, username));
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<ChangeAccountVO> changePassword(ChangePasswordDTO changePasswordDto)
    {
        try
        {
            return CommonResult.successResponse(accountManager.changePassword(changePasswordDto));
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusSystemErrorException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.SYSTEM_ERROR);
        }
    }

    @Override
    public CommonResult<UserInfoVO> changeUserInfo(UserInfoVO userInfoVo)
    {
        try
        {
            return CommonResult.successResponse(accountManager.changeUserInfo(userInfoVo));
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<UserAuthInfoVO> getUserAuthInfo()
    {
        return CommonResult.successResponse(accountManager.getUserAuthInfo());
    }
}
