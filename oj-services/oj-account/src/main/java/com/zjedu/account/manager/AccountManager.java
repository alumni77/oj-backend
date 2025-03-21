package com.zjedu.account.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjedu.account.dao.problem.ProblemEntityService;
import com.zjedu.account.dao.user.SessionEntityService;
import com.zjedu.account.dao.user.UserAcproblemEntityService;
import com.zjedu.account.feign.PassportFeignClient;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.pojo.dto.CheckUsernameDTO;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.entity.user.Session;
import com.zjedu.pojo.entity.user.UserAcproblem;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.CheckUsernameVO;
import com.zjedu.pojo.vo.UserHomeProblemVO;
import com.zjedu.pojo.vo.UserHomeVO;
import com.zjedu.shiro.AccountProfile;
import jakarta.annotation.Resource;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @Author Zhong
 * @Create 2025/3/20 18:31
 * @Version 1.0
 * @Description
 */

@Component
public class AccountManager
{
    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private UserAcproblemEntityService userAcproblemEntityService;

    @Resource
    private ProblemEntityService problemEntityService;

    @Resource
    private SessionEntityService sessionEntityService;


    /**
     * 检验用户名是否存在
     *
     * @param checkUsernameDTO
     * @return
     */
    public CheckUsernameVO checkUsername(CheckUsernameDTO checkUsernameDTO)
    {
        String username = checkUsernameDTO.getUsername();
        boolean rightUsername = false;

        if (StringUtils.hasText(username))
        {
            username = username.trim();
            UserInfo user = passportFeignClient.getByUsername(username);
            rightUsername = user != null;
        }

        CheckUsernameVO checkUsernameVO = new CheckUsernameVO();
        checkUsernameVO.setUsername(rightUsername);
        return checkUsernameVO;
    }

    /**
     * 前端userHome用户个人主页的数据请求，主要是返回解决题目数，AC的题目列表，提交总数，AC总数，Rating分
     *
     * @param uid
     * @param username
     * @return
     */
    public UserHomeVO getUserHomeInfo(String uid, String username) throws StatusFailException
    {
        // 先检查传入的参数
        if (!StringUtils.hasText(uid) && !StringUtils.hasText(username))
        {
            try
            {
                // 安全地尝试获取当前用户
                if (SecurityUtils.getSecurityManager() != null && SecurityUtils.getSubject() != null
                        && SecurityUtils.getSubject().getPrincipal() != null)
                {
                    AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
                    uid = userRolesVo.getUid();
                } else
                {
                    throw new StatusFailException("请求参数错误：uid和username不能都为空！");
                }
            } catch (Exception e)
            {
                throw new StatusFailException("请求参数错误：uid和username不能都为空！");
            }
        }

        UserHomeVO userHomeInfo = passportFeignClient.getUserHomeInfo(uid, username);
        if (userHomeInfo == null)
        {
            throw new StatusFailException("用户不存在");
        }
        QueryWrapper<UserAcproblem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", userHomeInfo.getUid())
                .select("distinct pid", "submit_id")
                .orderByAsc("submit_id");

        List<UserAcproblem> acProblemList = userAcproblemEntityService.list(queryWrapper);
        List<Long> pidList = acProblemList.stream().map(UserAcproblem::getPid).collect(Collectors.toList());

        List<String> disPlayIdList = new LinkedList<>();

        if (!pidList.isEmpty())
        {
            QueryWrapper<Problem> problemQueryWrapper = new QueryWrapper<>();
            problemQueryWrapper.select("id", "problem_id", "difficulty");
            problemQueryWrapper.in("id", pidList);
            List<Problem> problems = problemEntityService.list(problemQueryWrapper);
            Map<Integer, List<UserHomeProblemVO>> map = problems.stream()
                    .map(this::convertProblemVO)
                    .collect(Collectors.groupingBy(UserHomeProblemVO::getDifficulty));
            userHomeInfo.setSolvedGroupByDifficulty(map);
            disPlayIdList = problems.stream().map(Problem::getProblemId).collect(Collectors.toList());
        }
        userHomeInfo.setSolvedList(disPlayIdList);
        QueryWrapper<Session> sessionQueryWrapper = new QueryWrapper<>();
        sessionQueryWrapper.eq("uid", userHomeInfo.getUid())
                .orderByDesc("gmt_create")
                .last("limit 1");

        Session recentSession = sessionEntityService.getOne(sessionQueryWrapper, false);
        if (recentSession != null)
        {
            userHomeInfo.setRecentLoginTime(recentSession.getGmtCreate());
        }
        return userHomeInfo;
    }

    private UserHomeProblemVO convertProblemVO(Problem problem)
    {
        return UserHomeProblemVO.builder()
                .problemId(problem.getProblemId())
                .id(problem.getId())
                .difficulty(problem.getDifficulty())
                .build();
    }

}
