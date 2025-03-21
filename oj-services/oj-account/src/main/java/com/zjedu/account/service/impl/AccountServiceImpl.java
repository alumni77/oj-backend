package com.zjedu.account.service.impl;

import com.zjedu.account.manager.AccountManager;
import com.zjedu.account.service.AccountService;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.CheckUsernameDTO;
import com.zjedu.pojo.vo.CheckUsernameVO;
import com.zjedu.pojo.vo.UserCalendarHeatmapVO;
import com.zjedu.pojo.vo.UserHomeVO;
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
        try {
            return CommonResult.successResponse(accountManager.getUserHomeInfo(uid, username));
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<UserCalendarHeatmapVO> getUserCalendarHeatmap(String uid, String username) {
        try {
            return CommonResult.successResponse(accountManager.getUserCalendarHeatmap(uid, username));
        } catch (StatusFailException e) {
            return CommonResult.errorResponse(e.getMessage());
        }
    }
}
