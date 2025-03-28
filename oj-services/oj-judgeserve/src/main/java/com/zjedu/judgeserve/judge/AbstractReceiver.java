package com.zjedu.judgeserve.judge;

/**
 * @Author Zhong
 * @Create 2025/3/26 21:49
 * @Version 1.0
 * @Description
 */

public abstract class AbstractReceiver
{

    public void handleWaitingTask(String... queues)
    {
        for (String queue : queues)
        {
            String taskStr = getTaskByRedis(queue);
            if (taskStr != null)
            {
                handleJudgeMsg(taskStr, queue);
            }
        }
    }

    public abstract String getTaskByRedis(String queue);

    public abstract void handleJudgeMsg(String taskStr, String queueName);

}