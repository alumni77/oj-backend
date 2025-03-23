package com.zjedu.passport.service;

import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.LoginDTO;
import com.zjedu.pojo.dto.RegisterDTO;
import com.zjedu.pojo.dto.ResetPasswordDTO;
import com.zjedu.pojo.vo.CodeVO;
import com.zjedu.pojo.vo.UserInfoVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @Author Zhong
 * @Create 2025/3/18 15:26
 * @Version 1.0
 * @Description
 */

public interface PassportService
{
    CommonResult<UserInfoVO> login(LoginDTO loginDto, HttpServletResponse response, HttpServletRequest request);

    CommonResult<CodeVO> getCode(String username);

    CommonResult<Void> register(RegisterDTO registerDto);

    CommonResult<Void> resetPassword(ResetPasswordDTO resetPasswordDTO);

    CommonResult<Void> logout();

    boolean changeUserInfo(UserInfoVO userInfoVo, String userId) throws StatusFailException;
}
