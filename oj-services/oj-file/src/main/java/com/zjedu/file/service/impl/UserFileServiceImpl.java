package com.zjedu.file.service.impl;

import com.zjedu.file.manager.UserFileManager;
import com.zjedu.file.service.UserFileService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Author Zhong
 * @Create 2025/3/31 14:35
 * @Version 1.0
 * @Description
 */

@Service
public class UserFileServiceImpl implements UserFileService
{
    @Resource
    private UserFileManager userFileManager;


    @Override
    public void generateUserExcel(String key, HttpServletResponse response) throws IOException
    {
        userFileManager.generateUserExcel(key, response);
    }
}
