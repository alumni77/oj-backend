package com.zjedu.admin.feign.fallback;

import com.zjedu.admin.feign.JudgeServeFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author Zhong
 * @Create 2025/4/1 20:53
 * @Version 1.0
 * @Description
 */

@Slf4j
@Component
public class JudgeServeFeignClientFallback implements JudgeServeFeignClient
{
    @Override
    public void sendTask(Long judgeId, Long pid, Boolean isContest)
    {
        log.error("调用judgeserve-sendTask服务失败——兜底回调");

    }
}
