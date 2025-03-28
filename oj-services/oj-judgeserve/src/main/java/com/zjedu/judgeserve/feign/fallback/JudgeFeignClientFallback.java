package com.zjedu.judgeserve.feign.fallback;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.common.result.CommonResult;
import com.zjedu.judgeserve.feign.JudgeFeignClient;
import com.zjedu.pojo.dto.CompileDTO;
import com.zjedu.pojo.dto.TestJudgeReq;
import com.zjedu.pojo.dto.TestJudgeRes;
import com.zjedu.pojo.dto.ToJudgeDTO;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.entity.judge.JudgeServer;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.vo.JudgeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
    public IPage<JudgeVO> getCommonJudgeList(
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(value = "searchPid", required = false) String searchPid,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "uid", required = false) String uid,
            @RequestParam(value = "completeProblemID", defaultValue = "false") Boolean completeProblemID)
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
    public Problem getProblemById(Long id)
    {
        log.error("调用judge-getProblemById服务失败——兜底回调");
        return null;
    }

    @Override
    public Problem queryProblemByPId(String problemId)
    {
        log.error("调用judge-queryProblemByPId服务失败——兜底回调");
        return null;
    }


    @Override
    public Judge saveJudge(Judge judge)
    {
        log.error("调用judge-saveJudge服务失败——兜底回调");
        return null;
    }

    @Override
    public List<JudgeServer> getJudgeServerList(List<String> urls, Boolean isRemote)
    {
        log.error("调用judge-getJudgeServerList服务失败——兜底回调");
        return List.of();
    }

    @Override
    public boolean updateJudgeServerById(JudgeServer judgeServer)
    {
        log.error("调用judge-updateJudgeServerById服务失败——兜底回调");
        return false;
    }

    @Override
    public boolean updateJudgeServerByWrapper(UpdateWrapper<JudgeServer> updateWrapper)
    {
        log.error("调用judge-updateJudgeServerByWrapper服务失败——兜底回调");
        return false;
    }

    @Override
    public boolean updateJudgeById(Judge judge)
    {
        log.error("调用judge-updateJudgeById服务失败——兜底回调");
        return false;
    }

    @Override
    public boolean failToUseRedisPublishJudge(Long submitId, Long pid, Boolean isContest)
    {
        log.error("调用judge-failToUseRedisPublishJudge服务失败——兜底回调");
        return false;
    }

    @Override
    public CommonResult<Void> submitProblemJudge(ToJudgeDTO toJudgeDTO)
    {
        log.error("调用judge-submitProblemJudge服务失败——兜底回调");

        return null;
    }

    @Override
    public CommonResult<TestJudgeRes> submitProblemTestJudge(TestJudgeReq testJudgeReq)
    {
        log.error("调用judge-submitProblemTestJudge服务失败——兜底回调");

        return null;
    }

    @Override
    public CommonResult<Void> compileSpj(CompileDTO compileDTO)
    {
        log.error("调用judge-compileSpj服务失败——兜底回调");

        return null;
    }

    @Override
    public CommonResult<Void> compileInteractive(CompileDTO compileDTO)
    {
        log.error("调用judge-compileInteractive服务失败——兜底回调");

        return null;
    }
}
