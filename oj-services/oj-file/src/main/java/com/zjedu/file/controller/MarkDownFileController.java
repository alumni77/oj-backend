package com.zjedu.file.controller;

import com.zjedu.common.result.CommonResult;
import com.zjedu.file.service.MarkDownFileService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Author Zhong
 * @Create 2025/3/30 21:56
 * @Version 1.0
 * @Description
 */

@RestController
public class MarkDownFileController
{
    @Resource
    private MarkDownFileService markDownFileService;

    @PostMapping(value = "/upload-md-img")
    public CommonResult<Map<Object, Object>> uploadMDImg(@RequestParam("image") MultipartFile image)
    {
        return markDownFileService.uploadMDImg(image);
    }

    @GetMapping(value = "/delete-md-img")
    public CommonResult<Void> deleteMDImg(@RequestParam("fileId") Long fileId)
    {
        return markDownFileService.deleteMDImg(fileId);
    }

    @PostMapping(value = "/upload-md-file")
    public CommonResult<Map<Object, Object>> uploadMd(@RequestParam("file") MultipartFile file)
    {
        return markDownFileService.uploadMd(file);
    }

}
