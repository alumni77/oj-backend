package com.zjedu.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.AdminEditUserDTO;
import com.zjedu.pojo.vo.UserRolesVO;

import java.util.List;
import java.util.Map;

/**
 * @Author Zhong
 * @Create 2025/4/5 15:44
 * @Version 1.0
 * @Description
 */

public interface AdminUserService
{
    CommonResult<IPage<UserRolesVO>> getUserList(Integer limit, Integer currentPage, Boolean onlyAdmin, String keyword);

    CommonResult<Void> editUser(AdminEditUserDTO adminEditUserDto);

    CommonResult<Void> deleteUser(List<String> ids);

    CommonResult<Void> insertBatchUser(List<List<String>> users);

    CommonResult<Map<Object, Object>> generateUser(Map<String, Object> params);
}
