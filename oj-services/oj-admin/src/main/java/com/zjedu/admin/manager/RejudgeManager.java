package com.zjedu.admin.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjedu.admin.dao.JudgeEntityService;
import com.zjedu.admin.dao.ProblemEntityService;
import com.zjedu.admin.dao.UserAcproblemEntityService;
import com.zjedu.admin.feign.JudgeServeFeignClient;
import com.zjedu.admin.feign.PassportFeignClient;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.entity.user.UserAcproblem;
import com.zjedu.pojo.vo.UserRolesVO;
import com.zjedu.utils.Constants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @Author Zhong
 * @Create 2025/4/1 20:48
 * @Version 1.0
 * @Description
 */

@Slf4j
@Component
public class RejudgeManager
{
    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private ProblemEntityService problemEntityService;

    @Resource
    private UserAcproblemEntityService userAcproblemEntityService;

    @Resource
    private JudgeServeFeignClient judgeServeFeignClient;

    @Resource
    private JudgeEntityService judgeEntityService;

    @Resource
    private HttpServletRequest request;

    //TODO:对权限进行选择，权限介入到题目管理员还是admin
    public Judge rejudge(Long submitId) throws StatusFailException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusFailException("没有权限手动操作！");
        }
        Judge judge = judgeEntityService.getById(submitId);

        // 调用判题服务
        judgeServeFeignClient.sendTask(judge.getSubmitId(), judge.getPid(), false);
        return judge;
    }

    @Transactional(rollbackFor = Exception.class)
    public Judge manualJudge(Long submitId, Integer status, Integer score) throws StatusFailException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusFailException("没有权限手动操作！");
        }

        QueryWrapper<Judge> judgeQueryWrapper = new QueryWrapper<>();
        judgeQueryWrapper
                .select("submit_id", "status", "judger", "pid", "uid")
                .eq("submit_id", submitId);
        Judge judge = judgeEntityService.getOne(judgeQueryWrapper);
        if (judge == null)
        {
            throw new StatusFailException("错误：该提交数据已不存在！");
        }
        if (judge.getStatus().equals(Constants.Judge.STATUS_JUDGING.getStatus())
                || judge.getStatus().equals(Constants.Judge.STATUS_COMPILING.getStatus())
                || judge.getStatus().equals(Constants.Judge.STATUS_PENDING.getStatus()))
        {
            throw new StatusFailException("错误：该提交正在评测中，无法修改，请稍后再尝试！");
        }
        if (judge.getStatus().equals(Constants.Judge.STATUS_COMPILE_ERROR.getStatus()))
        {
            throw new StatusFailException("错误：编译失败的提交无法修改！");
        }

        String userId = request.getHeader("X-User-ID");
        UserRolesVO userRolesVo = passportFeignClient.getUserRoles(userId, "");

        UpdateWrapper<Judge> judgeUpdateWrapper = new UpdateWrapper<>();
        judgeUpdateWrapper
                .set("status", status)
                .set("is_manual", true)
                .set("judger", userRolesVo.getUsername())
                .eq("submit_id", judge.getSubmitId());
        Integer oiRankScore = null;
        if (score != null)
        {
            Problem problem = problemEntityService.getById(judge.getPid());
            if (problem != null && Objects.equals(problem.getType(), Constants.Contest.TYPE_OI.getCode())
                    && problem.getIoScore() != null)
            {
                if (score > problem.getIoScore())
                {
                    score = problem.getIoScore();
                } else if (score < 0)
                {
                    score = 0;
                }
                oiRankScore = (int) Math.round(problem.getDifficulty() * 2 + 0.1 * score);
                judgeUpdateWrapper.set("score", score)
                        .set("oi_rank_score", oiRankScore);
            } else
            {
                score = null;
            }
        }

        boolean isUpdateOK = judgeEntityService.update(judgeUpdateWrapper);
        if (!isUpdateOK)
        {
            throw new StatusFailException("错误：该提交正在评测中，无法取消，请稍后再尝试！");
        }

        // 如果原是AC,现在人工评测后不是AC,就移除user_acproblem表对应的记录
        if (Objects.equals(judge.getStatus(), Constants.Judge.STATUS_ACCEPTED.getStatus())
                && !Objects.equals(status, Constants.Judge.STATUS_ACCEPTED.getStatus()))
        {
            QueryWrapper<UserAcproblem> userAcproblemQueryWrapper = new QueryWrapper<>();
            userAcproblemQueryWrapper.eq("submit_id", judge.getSubmitId());
            userAcproblemEntityService.remove(userAcproblemQueryWrapper);
        } else if (!Objects.equals(judge.getStatus(), Constants.Judge.STATUS_ACCEPTED.getStatus())
                && Objects.equals(status, Constants.Judge.STATUS_ACCEPTED.getStatus()))
        {
            // 如果原先不是AC,现在人工评测后是AC,就更新user_acproblem表
            if (status.intValue() == Constants.Judge.STATUS_ACCEPTED.getStatus())
            {
                userAcproblemEntityService.saveOrUpdate(new UserAcproblem()
                        .setPid(judge.getPid())
                        .setUid(judge.getUid())
                        .setSubmitId(submitId)
                );
            }
        }
        Judge res = new Judge();
        res.setSubmitId(submitId)
                .setJudger(userRolesVo.getUsername())
                .setStatus(status)
                .setScore(score)
                .setOiRankScore(oiRankScore);
        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    public Judge cancelJudge(Long submitId) throws StatusFailException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusFailException("没有权限取消判题操作！");
        }

        QueryWrapper<Judge> judgeQueryWrapper = new QueryWrapper<>();
        judgeQueryWrapper
                .select("submit_id", "status", "judger")
                .eq("submit_id", submitId)
                .last("for update");
        Judge judge = judgeEntityService.getOne(judgeQueryWrapper);
        if (judge == null)
        {
            throw new StatusFailException("错误：该提交数据已不存在！");
        }
        if (judge.getStatus().equals(Constants.Judge.STATUS_JUDGING.getStatus())
                || judge.getStatus().equals(Constants.Judge.STATUS_COMPILING.getStatus())
                || (judge.getStatus().equals(Constants.Judge.STATUS_PENDING.getStatus())
                && StringUtils.hasText(judge.getJudger())))
        {
            throw new StatusFailException("错误：该提交正在评测中，无法取消，请稍后再尝试！");
        }

        String userId = request.getHeader("X-User-ID");
        UserRolesVO userRolesVo = passportFeignClient.getUserRoles(userId, "");

        UpdateWrapper<Judge> judgeUpdateWrapper = new UpdateWrapper<>();
        judgeUpdateWrapper
                .setSql("status=-4,score=null,oi_rank_score=null,is_manual=true,judger='" + userRolesVo.getUsername() + "'")
                .eq("submit_id", judge.getSubmitId());
        boolean isUpdateOK = judgeEntityService.update(judgeUpdateWrapper);
        if (!isUpdateOK)
        {
            throw new StatusFailException("错误：该提交正在评测中，无法取消，请稍后再尝试！");
        }

        // 如果该题已经是AC通过状态，更新该题目的用户ac做题表 user_acproblem
        if (judge.getStatus().intValue() == Constants.Judge.STATUS_ACCEPTED.getStatus().intValue())
        {
            QueryWrapper<UserAcproblem> userAcproblemQueryWrapper = new QueryWrapper<>();
            userAcproblemQueryWrapper.eq("submit_id", judge.getSubmitId());
            userAcproblemEntityService.remove(userAcproblemQueryWrapper);
        }

        Judge res = new Judge();
        res.setSubmitId(submitId)
                .setJudger(userRolesVo.getUsername())
                .setStatus(Constants.Judge.STATUS_CANCELLED.getStatus());
        return res;
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
}
