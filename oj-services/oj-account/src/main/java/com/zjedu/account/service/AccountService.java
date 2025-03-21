package com.zjedu.account.service;

import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.CheckUsernameDTO;
import com.zjedu.pojo.vo.CheckUsernameVO;

/**
 * @Author Zhong
 * @Create 2025/3/20 18:26
 * @Version 1.0
 * @Description
 */

public interface AccountService
{
    CommonResult<CheckUsernameVO> checkUsername(CheckUsernameDTO checkUsernameDTO);
}
