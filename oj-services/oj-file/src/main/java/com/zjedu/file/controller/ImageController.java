package com.zjedu.file.controller;

import com.zjedu.common.result.CommonResult;
import com.zjedu.file.service.ImageService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Author Zhong
 * @Create 2025/3/30 20:06
 * @Version 1.0
 * @Description
 */

@RestController
public class ImageController
{
    @Resource
    private ImageService imageService;

    @PostMapping(value = "/upload-avatar")
    public CommonResult<Map<Object, Object>> uploadAvatar(@RequestParam("image") MultipartFile image)
    {
        return imageService.uploadAvatar(image);
    }


}
