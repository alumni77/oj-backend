package com.zjedu.file.service.impl;

import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.common.exception.StatusSystemErrorException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.file.manager.TestCaseManager;
import com.zjedu.file.service.TestCaseService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Author Zhong
 * @Create 2025/3/31 13:59
 * @Version 1.0
 * @Description
 */

@Service
public class TestCaseServiceImpl implements TestCaseService
{
    @Resource
    private TestCaseManager testCaseManager;

    @Override
    public CommonResult<Map<Object, Object>> uploadTestcaseZip(MultipartFile file, String mode)
    {
        try
        {
            return CommonResult.successResponse(testCaseManager.uploadTestcaseZip(file, mode));
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusSystemErrorException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.SYSTEM_ERROR);
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public void downloadTestcase(Long pid, HttpServletResponse response) throws StatusForbiddenException, StatusFailException
    {
        testCaseManager.downloadTestcase(pid, response);
    }
}
