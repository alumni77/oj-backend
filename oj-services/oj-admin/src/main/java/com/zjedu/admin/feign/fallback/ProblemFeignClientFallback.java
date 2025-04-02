package com.zjedu.admin.feign.fallback;

import com.zjedu.admin.feign.ProblemFeignClient;
import com.zjedu.pojo.dto.ProblemDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author Zhong
 * @Create 2025/4/1 20:54
 * @Version 1.0
 * @Description
 */

@Slf4j
@Component
public class ProblemFeignClientFallback implements ProblemFeignClient
{
    @Override
    public boolean adminAddProblem(ProblemDTO problemDto)
    {
        log.error("调用problem-adminAddProblem服务失败——兜底回调");

        return false;
    }

    @Override
    public boolean adminUpdateProblem(ProblemDTO problemDto)
    {
        log.error("调用problem-adminUpdateProblem服务失败——兜底回调");

        return false;
    }
}
