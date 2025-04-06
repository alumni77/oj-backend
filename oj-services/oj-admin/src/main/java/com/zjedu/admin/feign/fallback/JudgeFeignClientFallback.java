package com.zjedu.admin.feign.fallback;

import com.zjedu.admin.feign.JudgeFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author Zhong
 * @Create 2025/4/6 20:12
 * @Version 1.0
 * @Description
 */

@Slf4j
@Component
public class JudgeFeignClientFallback implements JudgeFeignClient
{
    @Override
    public Integer getTodayJudgeNum()
    {
        log.error("调用judgeserve-getTodayJudgeNum服务失败——兜底回调");

        return 0;
    }
}
