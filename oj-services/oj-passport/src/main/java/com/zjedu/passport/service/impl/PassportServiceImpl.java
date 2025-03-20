package com.zjedu.passport.service.impl;

import com.zjedu.common.exception.StatusAccessDeniedException;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.passport.manager.PassportManager;
import com.zjedu.passport.service.PassportService;
import com.zjedu.pojo.dto.LoginDTO;
import com.zjedu.pojo.vo.RegisterCodeVO;
import com.zjedu.pojo.vo.UserInfoVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/18 15:27
 * @Version 1.0
 * @Description
 */

@Service
public class PassportServiceImpl implements PassportService
{
    @Resource
    private PassportManager passportManager;

    @Override
    public CommonResult<UserInfoVO> login(LoginDTO loginDto, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            return CommonResult.successResponse(passportManager.login(loginDto, response, request));
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        }
    }

    @Override
    public CommonResult<RegisterCodeVO> getRegisterCode(String email)
    {
        try
        {
            return CommonResult.successResponse(passportManager.getRegisterCode(email));
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusAccessDeniedException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.ACCESS_DENIED);
        }
    }
}
