package com.zjedu.judgeserve.judge.self;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zjedu.judgeserve.dao.judge.JudgeEntityService;
import com.zjedu.judgeserve.judge.AbstractReceiver;
import com.zjedu.judgeserve.judge.Dispatcher;
import com.zjedu.pojo.dto.TestJudgeReq;
import com.zjedu.pojo.dto.ToJudgeDTO;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.utils.Constants;
import com.zjedu.utils.RedisUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author Zhong
 * @Create 2025/3/26 21:49
 * @Version 1.0
 * @Description
 */

@Component
@Slf4j
public class JudgeReceiver extends AbstractReceiver
{

    @Resource
    private Dispatcher dispatcher;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private JudgeEntityService judgeEntityService;


    @Async("judgeTaskAsyncPool")
    public void processWaitingTask()
    {
        // 优先处理比赛的提交任务
        // 其次处理普通提交的提交任务
        // 最后处理在线调试的任务
        handleWaitingTask(Constants.Queue.GENERAL_JUDGE_WAITING.getName(),
                Constants.Queue.TEST_JUDGE_WAITING.getName());
    }


    @Override
    public String getTaskByRedis(String queue)
    {
        long size = redisUtils.lGetListSize(queue);
        if (size > 0)
        {
            return (String) redisUtils.lrPop(queue);
        } else
        {
            return null;
        }
    }

    @Override
    public void handleJudgeMsg(String taskStr, String queueName)
    {
        if (Constants.Queue.TEST_JUDGE_WAITING.getName().equals(queueName))
        {
            TestJudgeReq testJudgeReq = JSONUtil.toBean(taskStr, TestJudgeReq.class);
            dispatcher.dispatch(Constants.TaskType.TEST_JUDGE, testJudgeReq);
        } else
        {
            JSONObject task = JSONUtil.parseObj(taskStr);
            Long judgeId = task.getLong("judgeId");
            Judge judge = judgeEntityService.getById(judgeId);
            if (judge != null)
            {
                // 调度评测时发现该评测任务被取消，则结束评测
                if (Objects.equals(judge.getStatus(), Constants.Judge.STATUS_CANCELLED.getStatus()))
                {
                    log.info("The judge task[{}] has been cancelled!", judgeId);
                } else
                {
                    String token = task.getStr("token");
                    // 调用判题服务
                    dispatcher.dispatch(Constants.TaskType.JUDGE, new ToJudgeDTO()
                            .setJudge(judge)
                            .setToken(token)
                            .setRemoteJudgeProblem(null));
                }
            }
        }
        // 接着处理任务
        processWaitingTask();
    }

}