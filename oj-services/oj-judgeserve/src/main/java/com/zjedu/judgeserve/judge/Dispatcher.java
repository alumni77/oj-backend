package com.zjedu.judgeserve.judge;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.judgeserve.dao.judge.JudgeEntityService;
import com.zjedu.judgeserve.dao.judge.JudgeServerEntityService;
import com.zjedu.judgeserve.feign.JudgeFeignClient;
import com.zjedu.pojo.dto.CompileDTO;
import com.zjedu.pojo.dto.TestJudgeReq;
import com.zjedu.pojo.dto.TestJudgeRes;
import com.zjedu.pojo.dto.ToJudgeDTO;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.entity.judge.JudgeServer;
import com.zjedu.utils.Constants;
import com.zjedu.utils.RedisUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Zhong
 * @Create 2025/3/26 21:49
 * @Version 1.0
 * @Description 分发调用判题机执行任务
 */

@Component
@Slf4j
public class Dispatcher
{

//    @Resource
//    private RestTemplate restTemplate;

    @Resource
    private JudgeFeignClient judgeFeignClient;

    @Resource
    private JudgeServerEntityService judgeServerEntityService;

    @Resource
    private ChooseUtils chooseUtils;

    @Resource
    private RedisUtils redisUtils;

    private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(20);

    private final static Map<String, Future> futureTaskMap = new ConcurrentHashMap<>(20);

    // 每个提交任务尝试300次失败则判为提交失败
    protected final static Integer maxTryNum = 300;
    @Autowired
    private JudgeEntityService judgeEntityService;

    public void dispatch(Constants.TaskType taskType, Object data)
    {
        switch (taskType)
        {
            case JUDGE:
                defaultJudge((ToJudgeDTO) data, taskType.getPath());
                break;
            case TEST_JUDGE:
                testJudge((TestJudgeReq) data, taskType.getPath());
                break;
            case COMPILE_SPJ:
            case COMPILE_INTERACTIVE:
                compile((CompileDTO) data, taskType.getPath());
                return;
            default:
                throw new IllegalArgumentException("判题机不支持此调用类型");
        }
    }

    /**
     * 普通评测
     *
     * @param data
     * @param path
     */
    public void defaultJudge(ToJudgeDTO data, String path)
    {

        Long submitId = data.getJudge().getSubmitId();
        AtomicInteger count = new AtomicInteger(0);
        String taskKey = UUID.randomUUID().toString() + submitId;

        Runnable getResultTask = () ->
        {
            if (count.get() > maxTryNum)
            {
                checkResult(null, submitId);
                releaseTaskThread(taskKey);
                return;
            }
            count.getAndIncrement();
            JudgeServer judgeServer = chooseUtils.chooseServer(false);
            if (judgeServer != null)
            { // 获取到判题机资源
                CommonResult result = null;
                try
                {
                    //TODO 自定义负载均衡策略
                    result = judgeFeignClient.submitProblemJudge(data);
//                    result = restTemplate.postForObject("http://" + judgeServer.getUrl() + path, data, CommonResult.class);
                } catch (Exception e)
                {
                    log.error("[Self Judge] Request the judge server [{}] error -------------->{}", judgeServer.getUrl(), e.getMessage());
                } finally
                {
                    checkResult(result, submitId);
                    //TODO 自定义负载均衡策略
                    releaseJudgeServer(judgeServer.getId());
                    releaseTaskThread(taskKey);
                }
            }
        };
        ScheduledFuture<?> scheduledFuture = scheduler.scheduleWithFixedDelay(getResultTask, 0, 2, TimeUnit.SECONDS);
        futureTaskMap.put(taskKey, scheduledFuture);
    }

    /**
     * 在线调试
     *
     * @param testJudgeReq
     * @param path
     */
    public void testJudge(TestJudgeReq testJudgeReq, String path)
    {
        AtomicInteger count = new AtomicInteger(0);
        String taskKey = testJudgeReq.getUniqueKey();
        Runnable getResultTask = () ->
        {
            if (count.get() > maxTryNum)
            {
                releaseTaskThread(taskKey);
                return;
            }
            count.getAndIncrement();
            JudgeServer judgeServer = chooseUtils.chooseServer(false);
            if (judgeServer != null)
            {
                try
                {
                    CommonResult<TestJudgeRes> result = judgeFeignClient.submitProblemTestJudge(testJudgeReq);

                    if (result != null)
                    {
                        if (result.getCode() == ResultStatus.SUCCESS.getStatus())
                        {
                            TestJudgeRes testJudgeRes = result.getData();
                            testJudgeRes.setInput(testJudgeReq.getTestCaseInput());
                            testJudgeRes.setExpectedOutput(testJudgeReq.getExpectedOutput());
                            testJudgeRes.setProblemJudgeMode(testJudgeReq.getProblemJudgeMode());
                            redisUtils.set(testJudgeReq.getUniqueKey(), testJudgeRes, 60);
                        } else
                        {
                            TestJudgeRes testJudgeRes = TestJudgeRes.builder()
                                    .status(Constants.Judge.STATUS_SYSTEM_ERROR.getStatus())
                                    .time(0L)
                                    .memory(0L)
                                    .stderr(result.getMsg())
                                    .build();
                            redisUtils.set(testJudgeReq.getUniqueKey(), testJudgeRes, 60);
                        }
                    }
                } catch (Exception e)
                {
                    log.error("[Self Judge] Request the judge server [{}] error -------------->{}",
                            judgeServer.getUrl(), e.getMessage());
                    TestJudgeRes testJudgeRes = TestJudgeRes.builder()
                            .status(Constants.Judge.STATUS_SYSTEM_ERROR.getStatus())
                            .time(0L)
                            .memory(0L)
                            .stderr("Failed to connect the judgeServer. Please resubmit this submission again!")
                            .build();
                    redisUtils.set(testJudgeReq.getUniqueKey(), testJudgeRes, 60);
                } finally
                {
                    releaseJudgeServer(judgeServer.getId());
                    releaseTaskThread(taskKey);
                }
            }
        };
        ScheduledFuture<?> scheduledFuture = scheduler.scheduleWithFixedDelay(getResultTask, 0, 1, TimeUnit.SECONDS);
        futureTaskMap.put(taskKey, scheduledFuture);
    }

    /**
     * 编译特殊判题程序或交互程序
     *
     * @param data
     * @param path
     */
    public CommonResult compile(CompileDTO data, String path)
    {
        CommonResult result = CommonResult.errorResponse("没有可用的判题服务器，请重新尝试！");
        JudgeServer judgeServer = chooseUtils.chooseServer(false);
        if (judgeServer != null)
        {
            try
            {
                if (path.equals("/compile-spj"))
                {
                    result = judgeFeignClient.compileSpj(data);
                } else if (path.equals("/compile-interactive"))
                {
                    result = judgeFeignClient.compileInteractive(data);
                }
//                result = restTemplate.postForObject("http://" + judgeServer.getUrl() + path, data, CommonResult.class);
            } catch (Exception e)
            {
                log.error("[Self Judge] Request the judge server [" + judgeServer.getUrl() + "] error -------------->", e);
            } finally
            {
                // 无论成功与否，都要将对应的当前判题机当前判题数减1
                releaseJudgeServer(judgeServer.getId());
            }
        }
        return result;
    }

    private void checkResult(CommonResult<Void> result, Long submitId)
    {
        Judge judge = new Judge();
        if (result == null)
        {
            // 调用失败
            judge.setSubmitId(submitId);
            judge.setStatus(Constants.Judge.STATUS_SUBMITTED_FAILED.getStatus());
            judge.setErrorMessage("Failed to connect the judgeServer. Please resubmit this submission again!");
            judgeEntityService.updateById(judge);
        } else
        {
            if (result.getCode() != ResultStatus.SUCCESS.getStatus())
            {
                // 如果是结果码不是200 说明调用有错误
                // 判为系统错误
                judge.setStatus(Constants.Judge.STATUS_SYSTEM_ERROR.getStatus())
                        .setErrorMessage(result.getMsg());
                judgeEntityService.updateById(judge);
            }
        }
    }

    /**
     * 成功与否，皆需移除任务，释放线程
     *
     * @param taskKey
     */
    private void releaseTaskThread(String taskKey)
    {
        Future future = futureTaskMap.get(taskKey);
        if (future != null)
        {
            boolean isCanceled = future.cancel(true);
            if (isCanceled)
            {
                futureTaskMap.remove(taskKey);
            }
        }
    }

    /**
     * 释放评测机资源
     *
     * @param judgeServerId
     */
    public void releaseJudgeServer(Integer judgeServerId)
    {
        UpdateWrapper<JudgeServer> judgeServerUpdateWrapper = new UpdateWrapper<>();
        judgeServerUpdateWrapper.setSql("task_number = task_number-1")
                .eq("id", judgeServerId);
        boolean isOk = judgeServerEntityService.update(judgeServerUpdateWrapper);
        if (!isOk)
        { // 重试八次
            tryAgainUpdateJudge(judgeServerUpdateWrapper);
        }
    }

    public void tryAgainUpdateJudge(UpdateWrapper<JudgeServer> updateWrapper)
    {
        boolean retryable;
        int attemptNumber = 0;
        do
        {
            boolean success = judgeServerEntityService.update(updateWrapper);
            if (success)
            {
                return;
            } else
            {
                attemptNumber++;
                retryable = attemptNumber < 8;
                if (attemptNumber == 8)
                {
                    break;
                }
                try
                {
                    Thread.sleep(300);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        } while (retryable);
    }
}