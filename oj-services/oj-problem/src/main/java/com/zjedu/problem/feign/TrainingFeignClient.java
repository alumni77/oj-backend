package com.zjedu.problem.feign;

import com.zjedu.common.exception.StatusAccessDeniedException;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.pojo.vo.ProblemFullScreenListVO;
import com.zjedu.problem.feign.fallback.TrainingFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/4/8 21:08
 * @Version 1.0
 * @Description
 */

@FeignClient(value = "oj-training", path = "/api/training", fallback = TrainingFeignClientFallback.class)
public interface TrainingFeignClient
{
    @GetMapping("/get-training-problem-full-screen-list")
    List<ProblemFullScreenListVO> getProblemFullScreenList(@RequestParam(value = "tid") Long tid) throws StatusForbiddenException, StatusFailException, StatusAccessDeniedException;

}
