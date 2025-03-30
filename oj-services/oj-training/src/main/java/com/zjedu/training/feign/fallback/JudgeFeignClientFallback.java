package com.zjedu.training.feign.fallback;

import com.zjedu.training.feign.JudgeFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/30 16:35
 * @Version 1.0
 * @Description
 */

@Slf4j
@Component
public class JudgeFeignClientFallback implements JudgeFeignClient
{

    @Override
    public Integer getACProblemCount(List<Long> pidList, String uid, Integer status)
    {
        log.error("调用passport-getACProblemCount服务失败——兜底回调");
        return 0;
    }
}
