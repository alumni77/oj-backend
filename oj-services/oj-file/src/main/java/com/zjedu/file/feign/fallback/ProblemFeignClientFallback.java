package com.zjedu.file.feign.fallback;

import com.zjedu.file.feign.ProblemFeignClient;
import com.zjedu.pojo.dto.ExportProblemParamsDTO;
import com.zjedu.pojo.dto.ProblemDTO;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.vo.ImportProblemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author Zhong
 * @Create 2025/3/31 12:04
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
        log.error("调用passport-adminAddProblem服务失败——兜底回调");
        return false;
    }

    @Override
    public ImportProblemVO buildExportProblem(Long pid, ExportProblemParamsDTO params)
    {
        log.error("调用passport-buildExportProblem服务失败——兜底回调");

        return null;
    }

    @Override
    public Problem getProblemByPid(Long pid)
    {
        log.error("调用passport-getProblemByPid服务失败——兜底回调");

        return null;
    }
}
