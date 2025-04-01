package com.zjedu.account.manager;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjedu.account.dao.problem.ProblemEntityService;
import com.zjedu.account.dao.user.SessionEntityService;
import com.zjedu.account.dao.user.UserAcproblemEntityService;
import com.zjedu.account.feign.PassportFeignClient;
import com.zjedu.account.mapper.JudgeMapper;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusSystemErrorException;
import com.zjedu.pojo.dto.ChangePasswordDTO;
import com.zjedu.pojo.dto.CheckUsernameDTO;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.entity.user.Role;
import com.zjedu.pojo.entity.user.Session;
import com.zjedu.pojo.entity.user.UserAcproblem;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.*;
import com.zjedu.utils.Constants;
import com.zjedu.utils.RedisUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @Author Zhong
 * @Create 2025/3/20 18:31
 * @Version 1.0
 * @Description
 */

@Slf4j
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

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private HttpServletRequest request;


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
     *
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
     * @param uid      用户 ID
     * @param username 用户名
     * @return 解析后的用户 ID
     * @throws StatusFailException 当无法确定用户时抛出异常
     */
    private String getResolvedUid(String uid, String username) throws StatusFailException
    {
        if (!StringUtils.hasText(uid) && !StringUtils.hasText(username))
        {
            String userId = request.getHeader("X-User-Id");
            if (StringUtils.hasText(userId))
            {
                return userId;
            } else
            {
                throw new StatusFailException("请求参数错误：uid和username不能都为空！");
            }
        }
        return uid;
    }

    /**
     * 修改密码的操作，连续半小时内修改密码错误5次，则需要半个小时后才可以再次尝试修改密码
     *
     * @param changePasswordDto
     * @return
     */
    public ChangeAccountVO changePassword(ChangePasswordDTO changePasswordDto) throws StatusSystemErrorException, StatusFailException
    {

        //从请求头获取用户ID
        String userId = request.getHeader("X-User-Id");
        log.info("用户ID：{}", userId);
        if (!StringUtils.hasText(userId))
        {
            throw new StatusFailException("用户未登录，无法修改密码");
        }

        String oldPassword = changePasswordDto.getOldPassword();
        String newPassword = changePasswordDto.getNewPassword();

        // 数据可用性判断
        if (!StringUtils.hasText(oldPassword) || !StringUtils.hasText(newPassword))
        {
            throw new StatusFailException("错误：原始密码或新密码不能为空！");
        }
        if (newPassword.length() < 6 || newPassword.length() > 20)
        {
            throw new StatusFailException("新密码长度应该为6~20位！");
        }

        // 如果已经被锁定半小时，则不能修改
        String lockKey = Constants.Account.CODE_CHANGE_PASSWORD_LOCK + userId;
        // 统计失败的key
        String countKey = Constants.Account.CODE_CHANGE_PASSWORD_FAIL + userId;

        ChangeAccountVO resp = new ChangeAccountVO();
        if (redisUtils.hasKey(lockKey))
        {
            long expire = redisUtils.getExpire(lockKey);
            Date now = new Date();
            long minute = expire / 60;
            long second = expire % 60;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            resp.setCode(403);
            Date afterDate = new Date(now.getTime() + expire * 1000);
            String msg = "由于您多次修改密码失败，修改密码功能已锁定，请在" + minute + "分" + second + "秒后(" + formatter.format(afterDate) + ")再进行尝试！";
            resp.setMsg(msg);
            return resp;
        }

        UserInfo userInfo = passportFeignClient.getByUid(userId);
        // 与当前登录用户的密码进行比较判断
        if (userInfo.getPassword().equals(SecureUtil.md5(oldPassword)))
        {
            // 如果相同，则进行修改密码操作
            boolean isOk = passportFeignClient.updatePassword(userId, newPassword);
            if (isOk)
            {
                resp.setCode(200);
                resp.setMsg("修改密码成功！您将于5秒钟后退出进行重新登录操作！");
                // 清空记录
                redisUtils.del(countKey);
                return resp;
            } else
            {
                throw new StatusSystemErrorException("系统错误：修改密码失败！");
            }
        } else
        { // 如果不同，则进行记录，当失败次数达到5次，半个小时后才可重试
            Integer count = (Integer) redisUtils.get(countKey);
            if (count == null)
            {
                redisUtils.set(countKey, 1, 60 * 30); // 三十分钟不尝试，该限制会自动清空消失
                count = 0;
            } else if (count < 5)
            {
                redisUtils.incr(countKey, 1);
            }
            count++;
            if (count == 5)
            {
                redisUtils.del(countKey); // 清空统计
                redisUtils.set(lockKey, "lock", 60 * 30); // 设置锁定更改
            }
            resp.setCode(400);
            resp.setMsg("原始密码错误！您已累计修改密码失败" + count + "次...");
            return resp;
        }
    }

    public UserInfoVO changeUserInfo(UserInfoVO userInfoVo) throws StatusFailException
    {
        //从请求头获取用户ID
        String userId = request.getHeader("X-User-Id");
        boolean isOk = passportFeignClient.updateUserInfo(userInfoVo, userId);
        UserInfo userRolesVo = passportFeignClient.getByUid(userId);

        if (isOk)
        {
            UserRolesVO userRoles = passportFeignClient.getUserRoles(userRolesVo.getUuid(),"");
            // 更新session
            BeanUtil.copyProperties(userRoles, userRolesVo);
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtil.copyProperties(userRoles, userInfoVO, "roles");
            userInfoVO.setRoleList(userRoles.getRoles().stream().map(Role::getRole).collect(Collectors.toList()));
            return userInfoVO;
        } else
        {
            throw new StatusFailException("更新个人信息失败！");
        }
    }

    public UserAuthInfoVO getUserAuthInfo()
    {
        // 获取当前登录的用户
        //从请求头获取用户ID
        String userId = request.getHeader("X-User-Id");
        UserInfo userRolesVo = passportFeignClient.getByUid(userId);
        //获取该用户角色所有的权限
        List<Role> roles = passportFeignClient.getRolesByUid(userRolesVo.getUuid());
        UserAuthInfoVO authInfoVO = new UserAuthInfoVO();
        authInfoVO.setRoles(roles.stream().map(Role::getRole).collect(Collectors.toList()));
        return authInfoVO;
    }

}

