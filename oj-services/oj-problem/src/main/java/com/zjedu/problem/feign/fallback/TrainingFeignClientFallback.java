package com.zjedu.problem.feign.fallback;

import com.zjedu.common.exception.StatusAccessDeniedException;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.pojo.vo.ProblemFullScreenListVO;
import com.zjedu.problem.feign.TrainingFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/4/8 21:09
 * @Version 1.0
 * @Description
 */

@Slf4j
@Component
public class TrainingFeignClientFallback implements TrainingFeignClient
{
    @Override
    public List<ProblemFullScreenListVO> getProblemFullScreenList(Long tid) throws StatusForbiddenException, StatusFailException, StatusAccessDeniedException
    {
        log.error("调用training-getProblemFullScreenList服务失败——兜底回调");

        return List.of();
    }
}
