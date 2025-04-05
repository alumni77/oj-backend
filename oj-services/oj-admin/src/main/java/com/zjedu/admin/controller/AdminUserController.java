package com.zjedu.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.admin.service.AdminUserService;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.AdminEditUserDTO;
import com.zjedu.pojo.vo.UserRolesVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author Zhong
 * @Create 2025/4/5 15:44
 * @Version 1.0
 * @Description
 */

@RestController
@RequestMapping("/user")
public class AdminUserController
{
    @Resource
    private AdminUserService adminUserService;


    @GetMapping("/get-user-list")
    public CommonResult<IPage<UserRolesVO>> getUserList(@RequestParam(value = "limit", required = false) Integer limit,
                                                        @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                        @RequestParam(value = "onlyAdmin", defaultValue = "false") Boolean onlyAdmin,
                                                        @RequestParam(value = "keyword", required = false) String keyword)
    {
        return adminUserService.getUserList(limit, currentPage, onlyAdmin, keyword);
    }

    @PutMapping("/edit-user")
    public CommonResult<Void> editUser(@RequestBody AdminEditUserDTO adminEditUserDto)
    {
        return adminUserService.editUser(adminEditUserDto);
    }

    @DeleteMapping("/delete-user")
    public CommonResult<Void> deleteUser(@RequestBody Map<String, Object> params)
    {
        return adminUserService.deleteUser((List<String>) params.get("ids"));
    }

    @PostMapping("/insert-batch-user")
    public CommonResult<Void> insertBatchUser(@RequestBody Map<String, Object> params)
    {
        return adminUserService.insertBatchUser((List<List<String>>) params.get("users"));
    }

    @PostMapping("/generate-user")
    public CommonResult<Map<Object, Object>> generateUser(@RequestBody Map<String, Object> params)
    {
        return adminUserService.generateUser(params);
    }


}
