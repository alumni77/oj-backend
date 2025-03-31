package com.zjedu.admin.service;

import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.LoginDTO;
import com.zjedu.pojo.vo.UserInfoVO;

/**
 * @Author Zhong
 * @Create 2025/3/31 15:46
 * @Version 1.0
 * @Description
 */

public interface AdminAccountService
{
    CommonResult<UserInfoVO> login(LoginDTO loginDto);

    CommonResult<Void> logout();

}
