package com.zjedu.file.controller;

import com.zjedu.file.service.UserFileService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @Author Zhong
 * @Create 2025/3/31 14:34
 * @Version 1.0
 * @Description
 */

@RestController
public class UserFileController
{
    @Resource
    private UserFileService userFileService;

    @GetMapping("/generate-user-excel")
    public void generateUserExcel(@RequestParam("key") String key, HttpServletResponse response) throws IOException
    {
        userFileService.generateUserExcel(key, response);
    }
}
