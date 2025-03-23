package com.zjedu.account.service;

import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.ChangePasswordDTO;
import com.zjedu.pojo.dto.CheckUsernameDTO;
import com.zjedu.pojo.vo.*;

/**
 * @Author Zhong
 * @Create 2025/3/20 18:26
 * @Version 1.0
 * @Description
 */

public interface AccountService
{
    CommonResult<CheckUsernameVO> checkUsername(CheckUsernameDTO checkUsernameDTO);

    CommonResult<UserHomeVO> getUserHomeInfo(String uid, String username);

    CommonResult<UserCalendarHeatmapVO> getUserCalendarHeatmap(String uid, String username);

    CommonResult<ChangeAccountVO> changePassword(ChangePasswordDTO changePasswordDto);

    CommonResult<UserInfoVO> changeUserInfo(UserInfoVO userInfoVo);

    CommonResult<UserAuthInfoVO> getUserAuthInfo();
}
