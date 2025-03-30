package com.zjedu.file.service;

import com.zjedu.common.result.CommonResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Author Zhong
 * @Create 2025/3/30 20:07
 * @Version 1.0
 * @Description
 */

public interface ImageService
{
    CommonResult<Map<Object, Object>> uploadAvatar(MultipartFile image);
}
