package com.zjedu.judgeserve.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjedu.common.exception.StatusAccessDeniedException;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.judgeserve.dao.judge.JudgeEntityService;
import com.zjedu.judgeserve.dao.problem.ProblemEntityService;
import com.zjedu.judgeserve.dao.training.TrainingEntityService;
import com.zjedu.judgeserve.dao.training.TrainingProblemEntityService;
import com.zjedu.judgeserve.dao.training.TrainingRecordEntityService;
import com.zjedu.judgeserve.validator.TrainingValidator;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.entity.training.Training;
import com.zjedu.pojo.entity.training.TrainingProblem;
import com.zjedu.pojo.entity.training.TrainingRecord;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.utils.Constants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Zhong
 * @Create 2025/3/26 21:09
 * @Version 1.0
 * @Description 移除Contest和Group相关代码的简化版本
 */

@Slf4j
@Component
public class BeforeDispatchInitManager
{
    @Resource
    private JudgeEntityService judgeEntityService;

    @Resource
    private ProblemEntityService problemEntityService;

    @Resource
    private TrainingEntityService trainingEntityService;

    @Resource
    private TrainingProblemEntityService trainingProblemEntityService;

    @Resource
    private TrainingRecordEntityService trainingRecordEntityService;

    @Resource
    private TrainingValidator trainingValidator;

    /**
     * 初始化普通题目提交
     *
     * @param problemId 题目ID
     * @param judge     判题对象
     * @throws StatusForbiddenException 当题目不存在或不可提交时抛出
     */
    public Judge initCommonSubmission(String problemId, Judge judge) throws StatusForbiddenException
    {
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "problem_id", "auth");
        queryWrapper.eq("problem_id", problemId);
        Problem problem = problemEntityService.getOne(queryWrapper, false);

        if (problem == null)
        {
            throw new StatusForbiddenException("错误！当前题目已不存在，不可提交！");
        }

        if (problem.getAuth() == 2)
        {
            throw new StatusForbiddenException("错误！当前题目不可提交！");
        }

        // 设置判题信息
        judge.setPid(problem.getId())
                .setDisplayPid(problem.getProblemId());

        //TODO 可能judge submitId不一定返回
        // 将新提交数据插入数据库
        judgeEntityService.save(judge);

        log.info("submit:{}", judge.getSubmitId());

        // 检查和同步训练记录
//        trainingManager.checkAndSyncTrainingRecord(problem.getId(), judge.getSubmitId(), judge.getUid());
        return judge;
    }

    /**
     * 初始化训练题目提交
     *
     * @param tid         训练ID
     * @param displayId   展示ID
     * @param userRolesVo 用户信息
     * @param judge       判题对象
     * @throws StatusForbiddenException    当题目不存在或不可提交时抛出
     * @throws StatusFailException         当训练不存在或不可显示时抛出
     * @throws StatusAccessDeniedException 当用户无权访问训练时抛出
     */
    @Transactional(rollbackFor = Exception.class)
    public void initTrainingSubmission(Long tid, String displayId, UserInfo userRolesVo, Judge judge) throws StatusForbiddenException, StatusFailException, StatusAccessDeniedException
    {
        Training training = trainingEntityService.getById(tid);
        if (training == null || !training.getStatus())
        {
            throw new StatusFailException("该训练不存在或不允许显示！");
        }

        trainingValidator.validateTrainingAuth(training, userRolesVo);

        // 查询获取对应的pid
        QueryWrapper<TrainingProblem> trainingProblemQueryWrapper = new QueryWrapper<>();
        trainingProblemQueryWrapper.eq("tid", tid)
                .eq("display_id", displayId);
        TrainingProblem trainingProblem = trainingProblemEntityService.getOne(trainingProblemQueryWrapper);
        judge.setPid(trainingProblem.getPid());

        Problem problem = problemEntityService.getById(trainingProblem.getPid());

        if (problem == null)
        {
            throw new StatusForbiddenException("错误！当前题目已不存在，不可提交！");
        }

        if (problem.getAuth() == 2)
        {
            throw new StatusForbiddenException("错误！当前题目不可提交！");
        }

        judge.setDisplayPid(problem.getProblemId());

        // 将新提交数据插入数据库
        judgeEntityService.save(judge);

        // 非私有训练不记录
        if (!training.getAuth().equals(Constants.Training.AUTH_PRIVATE.getValue()))
        {
            return;
        }

        TrainingRecord trainingRecord = new TrainingRecord();
        trainingRecord.setPid(problem.getId())
                .setTid(tid)
                .setTpid(trainingProblem.getId())
                .setSubmitId(judge.getSubmitId())
                .setUid(userRolesVo.getUuid());
        trainingRecordEntityService.save(trainingRecord);
    }
}