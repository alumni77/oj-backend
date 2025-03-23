package com.zjedu.account.controller;

import com.zjedu.account.service.AccountService;
import com.zjedu.annotation.AnonApi;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.ChangePasswordDTO;
import com.zjedu.pojo.dto.CheckUsernameDTO;
import com.zjedu.pojo.vo.ChangeAccountVO;
import com.zjedu.pojo.vo.CheckUsernameVO;
import com.zjedu.pojo.vo.UserCalendarHeatmapVO;
import com.zjedu.pojo.vo.UserHomeVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Zhong
 * @Create 2025/3/20 15:59
 * @Version 1.0
 * @Description 主要负责处理账号的相关操作
 */

@RestController
public class AccountController
{
    @Resource
    private AccountService accountService;

    /**
     * 检验用户名和邮箱是否存在
     *
     * @param checkUsernameDTO
     * @return
     */
    @PostMapping("/check-username")
    @AnonApi
    public CommonResult<CheckUsernameVO> checkUsername(@RequestBody CheckUsernameDTO checkUsernameDTO)
    {
        return accountService.checkUsername(checkUsernameDTO);
    }

    /**
     * 前端userHome用户个人主页的数据请求，主要是返回解决题目数，AC的题目列表，提交总数，AC总数，Rating分，
     *
     * @param uid
     * @param username
     * @return
     */
    @GetMapping("/get-user-home-info")
    public CommonResult<UserHomeVO> getUserHomeInfo(@RequestParam(value = "uid", required = false) String uid,
                                                    @RequestParam(value = "username", required = false) String username)
    {
        return accountService.getUserHomeInfo(uid, username);
    }


    /**
     * 获取用户最近一年的提交热力图数据
     *
     * @param uid
     * @param username
     * @return
     */
    @GetMapping("/get-user-calendar-heatmap")
    @AnonApi
    public CommonResult<UserCalendarHeatmapVO> getUserCalendarHeatmap(@RequestParam(value = "uid", required = false) String uid,
                                                                      @RequestParam(value = "username", required = false) String username)
    {
        return accountService.getUserCalendarHeatmap(uid, username);
    }

    /**
     * 修改密码的操作，连续半小时内修改密码错误5次，则需要半个小时后才可以再次尝试修改密码
     *
     * @param changePasswordDto
     * @return
     */
    @PostMapping("/change-password")
    public CommonResult<ChangeAccountVO> changePassword(@RequestBody ChangePasswordDTO changePasswordDto)
    {
        return accountService.changePassword(changePasswordDto);
    }

}
