package com.zjedu.judgeserve.feign.fallback;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.judgeserve.feign.JudgeFeignClient;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.vo.JudgeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author Zhong
 * @Create 2025/3/26 19:20
 * @Version 1.0
 * @Description
 */
@Slf4j
@Component
public class JudgeFeignClientFallback implements JudgeFeignClient
{
    @Override
    public IPage<JudgeVO> getCommonJudgeList(Integer limit, Integer currentPage, String searchPid, Integer status, String username, String uid, Boolean completeProblemID, Long gid)
    {
        log.error("调用judge-getCommonJudgeList服务失败——兜底回调");
        return null;
    }

    @Override
    public Judge getJudgeById(Long submitId)
    {
        log.error("调用judge-getJudgeById服务失败——兜底回调");
        return null;
    }

    @Override
    public Problem getProblemById(Long pid)
    {
        log.error("调用judge-getProblemById服务失败——兜底回调");
        return null;
    }
}
