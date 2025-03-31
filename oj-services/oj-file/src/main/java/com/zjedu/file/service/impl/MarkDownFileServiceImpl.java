package com.zjedu.file.service.impl;

import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.common.exception.StatusSystemErrorException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.file.manager.MarkDownFileManager;
import com.zjedu.file.service.MarkDownFileService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Author Zhong
 * @Create 2025/3/30 21:57
 * @Version 1.0
 * @Description
 */

@Service
public class MarkDownFileServiceImpl implements MarkDownFileService
{
    @Resource
    private MarkDownFileManager markDownFileManager;

    @Override
    public CommonResult<Map<Object, Object>> uploadMDImg(MultipartFile image)
    {
        try
        {
            return CommonResult.successResponse(markDownFileManager.uploadMDImg(image));
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
    public CommonResult<Void> deleteMDImg(Long fileId)
    {
        try
        {
            markDownFileManager.deleteMDImg(fileId);
            return CommonResult.successResponse();
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusForbiddenException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.FORBIDDEN);
        }
    }

    @Override
    public CommonResult<Map<Object, Object>> uploadMd(MultipartFile file)
    {
        try
        {
            return CommonResult.successResponse(markDownFileManager.uploadMd(file));
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
}
