package com.zjedu.passport.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.annotation.AnonApi;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.passport.dao.user.UserInfoEntityService;
import com.zjedu.passport.dao.user.UserRecordEntityService;
import com.zjedu.passport.dao.user.UserRoleEntityService;
import com.zjedu.passport.service.PassportService;
import com.zjedu.pojo.dto.LoginDTO;
import com.zjedu.pojo.dto.RegisterDTO;
import com.zjedu.pojo.dto.ResetPasswordDTO;
import com.zjedu.pojo.entity.user.Role;
import com.zjedu.pojo.vo.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/19 22:01
 * @Version 1.0
 * @Description
 */

@RestController
public class PassportController
{
    @Resource
    private PassportService passportService;

    /**
     * 处理登录逻辑
     *
     * @param loginDto
     * @return CommonResult
     */
    @PostMapping("/login")
    @AnonApi
    public CommonResult<UserInfoVO> login(@Validated @RequestBody LoginDTO loginDto, HttpServletResponse response, HttpServletRequest request)
    {
        return passportService.login(loginDto, response, request);
    }

    /**
     * 发送 6 位随机验证码
     *
     * @param username
     * @return
     */
    @GetMapping(value = "/get-code")
    @AnonApi
    public CommonResult<CodeVO> getCode(@RequestParam(value = "username") String username)
    {
        return passportService.getCode(username);
    }

    /**
     * 注册逻辑
     *
     * @param registerDTO
     * @return
     */
    @PostMapping("/register")
    @AnonApi
    public CommonResult<Void> register(@Validated @RequestBody RegisterDTO registerDTO)
    {
        return passportService.register(registerDTO);
    }

    /**
     * 用户重置密码
     *
     * @param resetPasswordDTO
     * @return
     */
    @PostMapping("/reset-password")
    @AnonApi
    public CommonResult<Void> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO)
    {
        return passportService.resetPassword(resetPasswordDTO);
    }

    /**
     * 退出逻辑，将jwt在redis中清除，下次需要再次登录。
     *
     * @return
     */
    @GetMapping("/logout")
    public CommonResult<Void> logout()
    {
        return passportService.logout();
    }

    // 外露接口，给openFeign调用
    @Resource
    private UserInfoEntityService userInfoEntityService;

    @GetMapping("/get-user-uid-list")
    public List<String> getSuperAdminUidList()
    {
        return userInfoEntityService.getSuperAdminUidList();
    }

    @PostMapping("/change-user-info")
    public boolean updateUserInfo(@RequestBody UserInfoVO userInfoVo, @RequestParam("userId") String userId) throws StatusFailException
    {
        return passportService.changeUserInfo(userInfoVo, userId);
    }

    @Resource
    private UserRecordEntityService userRecordEntityService;

    @GetMapping("/get-user-home-info")
    public UserHomeVO getUserHomeInfo(String uid, String username)
    {
        return userRecordEntityService.getUserHomeInfo(uid, username);
    }

    @GetMapping("/get-recent7-ac-rank")
    public List<ACMRankVO> getRecent7ACRank()
    {
        return userRecordEntityService.getRecent7ACRank();
    }

    @PostMapping("get-oi-rank-list")
    public Page<OIRankVO> getOIRankList(@RequestBody Page<OIRankVO> page, @RequestParam(required = false) List<String> uidList)
    {
        return (Page<OIRankVO>) userRecordEntityService.getOIRankList(page, uidList);
    }

    @PostMapping("/get-acm-rank-list")
    public Page<ACMRankVO> getACMRankList(@RequestBody Page<ACMRankVO> page, @RequestParam(required = false) List<String> uidList)
    {
        return (Page<ACMRankVO>) userRecordEntityService.getACMRankList(page, uidList);
    }

    @Resource
    private UserRoleEntityService userRoleEntityService;

    @GetMapping("/get-user-role")
    public UserRolesVO getUserRoles(@RequestParam("uid") String uid, @RequestParam("username") String username)
    {
        return userRoleEntityService.getUserRoles(uid, username);
    }

    @GetMapping("/get-roles-by-uid")
    public List<Role> getRolesByUid(@RequestParam("uid") String uid)
    {
        return userRoleEntityService.getRolesByUid(uid);
    }

    @GetMapping("/get-user-list")
    public Page<UserRolesVO> getUserList(@RequestParam(value = "limit", required = false,defaultValue = "10") int limit,
                                         @RequestParam(value = "currentPage", required = false,defaultValue = "1") int currentPage,
                                         @RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "onlyAdmin", defaultValue = "false") Boolean onlyAdmin)
    {
        return (Page<UserRolesVO>) userRoleEntityService.getUserList(limit, currentPage, keyword, onlyAdmin);
    }

    @DeleteMapping("/delete-cache")
    public void deleteCache(@RequestParam("uid") String uid, @RequestParam("isRemoveSession") boolean isRemoveSession)
    {
        userRoleEntityService.deleteCache(uid, isRemoveSession);
    }

}