package com.zjedu.file.service;

import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusSystemErrorException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.file.manager.ImageManager;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Author Zhong
 * @Create 2025/3/30 20:07
 * @Version 1.0
 * @Description
 */

@Service
public class ImageServiceImpl implements ImageService
{
    @Resource
    private ImageManager imageManager;

    @Override
    public CommonResult<Map<Object, Object>> uploadAvatar(MultipartFile image)
    {
        try
        {
            return CommonResult.successResponse(imageManager.uploadAvatar(image));
        } catch (StatusFailException e)
        {
            return CommonResult.errorResponse(e.getMessage());
        } catch (StatusSystemErrorException e)
        {
            return CommonResult.errorResponse(e.getMessage(), ResultStatus.SYSTEM_ERROR);
        }
    }
}
