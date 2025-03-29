package com.zjedu.problem.feign.fallback;

import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.vo.ProblemCountVO;
import com.zjedu.problem.feign.JudgeFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/28 20:59
 * @Version 1.0
 * @Description
 */

@Slf4j
@Component
public class JudgeFeignClientFallback implements JudgeFeignClient
{
    @Override
    public List<ProblemCountVO> getProblemListByPids(List<Long> pidList)
    {
        log.error("调用judge-getProblemListByPids服务失败——兜底回调");

        return List.of();
    }

    @Override
    public List<Judge> getJudgeListByPids(List<Long> pidList, String uid)
    {
        log.error("调用judge-queryJudgeListByPids服务失败——兜底回调");

        return List.of();
    }

    @Override
    public ProblemCountVO getProblemCountByPid(Long pid)
    {
        log.error("调用judge-getProblemCountByPid服务失败——兜底回调");

        return null;
    }

    @Override
    public List<Judge> queryJudgeListByWrapper(Long pid, String uid, Integer status)
    {
        log.error("调用judge-queryJudgeListByWrapper服务失败——兜底回调");

        return List.of();
    }
}
