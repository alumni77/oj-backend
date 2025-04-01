package com.zjedu.judgeserve.judge.self;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zjedu.common.exception.StatusSystemErrorException;
import com.zjedu.judgeserve.feign.JudgeFeignClient;
import com.zjedu.pojo.dto.TestJudgeReq;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.utils.Constants;
import com.zjedu.utils.RedisUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Author Zhong
 * @Create 2025/3/26 21:47
 * @Version 1.0
 * @Description
 */

@Component
@Slf4j
@RefreshScope
public class JudgeDispatcher
{
    @Resource
    private RedisUtils redisUtils;

    @Resource
    private JudgeFeignClient judgeFeignClient;

    @Resource
    private JudgeReceiver judgeReceiver;

    @Value("${oj.judge.token}")
    private String judgeToken;

    public void sendTask(Long judgeId, Long pid, Boolean isContest)
    {
        JSONObject task = new JSONObject();
        task.set("judgeId", judgeId);
        task.set("token", judgeToken);
        task.set("isContest", isContest);
        try
        {
            boolean isOk;
            if (isContest)
            {
                isOk = redisUtils.llPush(Constants.Queue.CONTEST_JUDGE_WAITING.getName(), JSONUtil.toJsonStr(task));
            } else
            {
                isOk = redisUtils.llPush(Constants.Queue.GENERAL_JUDGE_WAITING.getName(), JSONUtil.toJsonStr(task));
            }
            if (!isOk)
            {
                judgeFeignClient.updateJudgeById(new Judge()
                        .setSubmitId(judgeId)
                        .setStatus(Constants.Judge.STATUS_SUBMITTED_FAILED.getStatus())
                        .setErrorMessage("Call Redis to push task error. Please try to submit again!"));
            }
            // 调用判题任务处理
            judgeReceiver.processWaitingTask();
        } catch (Exception e)
        {
            log.error("调用redis将判题纳入判题等待队列异常--------------->{}", e.getMessage());
            judgeFeignClient.failToUseRedisPublishJudge(judgeId, pid, isContest);
        }
    }

    public void sendTestJudgeTask(TestJudgeReq testJudgeReq) throws StatusSystemErrorException
    {
        testJudgeReq.setToken(judgeToken);
        try
        {
            boolean isOk = redisUtils.llPush(Constants.Queue.TEST_JUDGE_WAITING.getName(), JSONUtil.toJsonStr(testJudgeReq));
            if (!isOk)
            {
                throw new StatusSystemErrorException("系统错误：当前评测任务进入等待队列失败！");
            }
            // 调用判题任务处理
            judgeReceiver.processWaitingTask();
        } catch (Exception e)
        {
            log.error("调用redis将判题纳入判题等待队列异常--------------->{}", e.getMessage());
            throw new StatusSystemErrorException("系统错误：当前评测任务进入等待队列失败！");
        }
    }
}
