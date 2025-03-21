package com.zjedu.account.manager;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjedu.account.dao.problem.ProblemEntityService;
import com.zjedu.account.dao.user.SessionEntityService;
import com.zjedu.account.dao.user.UserAcproblemEntityService;
import com.zjedu.account.feign.PassportFeignClient;
import com.zjedu.account.mapper.JudgeMapper;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.pojo.dto.CheckUsernameDTO;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.entity.user.Session;
import com.zjedu.pojo.entity.user.UserAcproblem;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.CheckUsernameVO;
import com.zjedu.pojo.vo.UserCalendarHeatmapVO;
import com.zjedu.pojo.vo.UserHomeProblemVO;
import com.zjedu.pojo.vo.UserHomeVO;
import com.zjedu.shiro.AccountProfile;
import jakarta.annotation.Resource;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.util.CollectionUtils;


import java.util.*;
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

    @Resource
    private JudgeMapper judgeMapper;


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
        uid = getResolvedUid(uid, username);

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

    /**
     * 获取用户最近一年的提交热力图数据
     * @param uid
     * @param username
     * @return
     * @throws StatusFailException
     */
    public UserCalendarHeatmapVO getUserCalendarHeatmap(String uid, String username) throws StatusFailException
    {

        uid = getResolvedUid(uid, username);

        UserCalendarHeatmapVO userCalendarHeatmapVo = new UserCalendarHeatmapVO();
        userCalendarHeatmapVo.setEndDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
        List<Judge> lastYearUserJudgeList = judgeMapper.getLastYearUserJudgeList(uid, username);
        if (CollectionUtils.isEmpty(lastYearUserJudgeList))
        {
            userCalendarHeatmapVo.setDataList(new ArrayList<>());
            return userCalendarHeatmapVo;
        }
        HashMap<String, Integer> tmpRecordMap = new HashMap<>();
        for (Judge judge : lastYearUserJudgeList)
        {
            Date submitTime = judge.getSubmitTime();
            String dateStr = DateUtil.format(submitTime, "yyyy-MM-dd");
            tmpRecordMap.merge(dateStr, 1, Integer::sum);
        }
        List<HashMap<String, Object>> dataList = new ArrayList<>();
        for (Map.Entry<String, Integer> record : tmpRecordMap.entrySet())
        {
            HashMap<String, Object> tmp = new HashMap<>(2);
            tmp.put("date", record.getKey());
            tmp.put("count", record.getValue());
            dataList.add(tmp);
        }
        userCalendarHeatmapVo.setDataList(dataList);
        return userCalendarHeatmapVo;
    }

    /**
     * 解析用户 ID，当 uid 和 username 都为空时尝试获取当前登录用户的 uid
     *
     * @param uid 用户 ID
     * @param username 用户名
     * @return 解析后的用户 ID
     * @throws StatusFailException 当无法确定用户时抛出异常
     */
    private String getResolvedUid(String uid, String username) throws StatusFailException {
        if (!StringUtils.hasText(uid) && !StringUtils.hasText(username)) {
            try {
                // 安全地尝试获取当前用户
                if (SecurityUtils.getSecurityManager() != null && SecurityUtils.getSubject() != null
                        && SecurityUtils.getSubject().getPrincipal() != null) {
                    AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
                    return userRolesVo.getUid();
                } else {
                    throw new StatusFailException("请求参数错误：uid和username不能都为空！");
                }
            } catch (Exception e) {
                throw new StatusFailException("请求参数错误：uid和username不能都为空！");
            }
        }
        return uid;
    }
}

