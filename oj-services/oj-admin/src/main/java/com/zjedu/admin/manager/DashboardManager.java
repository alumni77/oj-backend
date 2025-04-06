package com.zjedu.admin.manager;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjedu.admin.dao.SessionEntityService;
import com.zjedu.admin.dao.UserInfoEntityService;
import com.zjedu.admin.feign.JudgeFeignClient;
import com.zjedu.admin.feign.PassportFeignClient;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.pojo.entity.user.Session;
import com.zjedu.pojo.vo.UserRolesVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author Zhong
 * @Create 2025/4/6 19:57
 * @Version 1.0
 * @Description
 */

@Slf4j
@Component
public class DashboardManager
{
    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private HttpServletRequest request;

    @Resource
    private SessionEntityService sessionEntityService;

    @Resource
    private UserInfoEntityService userInfoEntityService;

    @Resource
    private JudgeFeignClient judgeFeignClient;

    public Session getRecentSession() throws StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有权限进行操作！");
        }

        // 需要获取一下该token对应用户的数据
        String userId = request.getHeader("X-User-ID");
        UserRolesVO userRolesVo = passportFeignClient.getUserRoles(userId, "");
        QueryWrapper<Session> wrapper = new QueryWrapper<Session>().eq("uid", userRolesVo.getUid()).orderByDesc("gmt_create");
        List<Session> sessionList = sessionEntityService.list(wrapper);
        if (sessionList.size() > 1)
        {
            return sessionList.get(1);
        } else
        {
            return sessionList.get(0);
        }
    }

    private boolean checkAuthority()
    {
        String userId = request.getHeader("X-User-ID");
        UserRolesVO userRoles = passportFeignClient.getUserRoles(userId, "");
        // 是否为超级管理员
        boolean isRoot = userRoles.getRoles().stream()
                .anyMatch(role -> "root".equals(role.getRole()));
        // 是否为admin
        boolean isAdmin = userRoles.getRoles().stream()
                .anyMatch(role -> "admin".equals(role.getRole()));
        // 是否为problem_admin
        boolean isProblemAdmin = userRoles.getRoles().stream()
                .anyMatch(role -> "problem_admin".equals(role.getRole()));

        return isRoot || isAdmin || isProblemAdmin;
    }

    public Map<Object, Object> getDashboardInfo() throws StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有权限进行操作！");
        }

        int userNum = (int) userInfoEntityService.count();
        int todayJudgeNum = judgeFeignClient.getTodayJudgeNum();
        return MapUtil.builder()
                .put("userNum", userNum)
                .put("todayJudgeNum", todayJudgeNum).map();
    }
}
