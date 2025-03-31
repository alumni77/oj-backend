package com.zjedu.file.service.impl;

import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusSystemErrorException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.file.manager.ProblemFileManager;
import com.zjedu.file.service.ProblemFileService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/31 10:45
 * @Version 1.0
 * @Description
 */

@Service
public class ProblemFileServiceImpl implements ProblemFileService
{
    @Resource
    private ProblemFileManager problemFileManager;

    @Override
    public CommonResult<Void> importProblem(MultipartFile file)
    {
        try
        {
            problemFileManager.importProblem(file);
            return CommonResult.successResponse();
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusSystemErrorException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.SYSTEM_ERROR);
        }
    }

    @Override
    public void exportProblem(List<Long> pidList, HttpServletResponse response)
    {
        problemFileManager.exportProblem(pidList, response);
    }
}
