package com.zjedu.file.service;

import com.zjedu.common.result.CommonResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Author Zhong
 * @Create 2025/3/30 21:56
 * @Version 1.0
 * @Description
 */

public interface MarkDownFileService
{
    CommonResult<Map<Object, Object>> uploadMDImg(MultipartFile image);

    CommonResult<Void> deleteMDImg(Long fileId);

    CommonResult<Map<Object, Object>> uploadMd(MultipartFile file);
}
