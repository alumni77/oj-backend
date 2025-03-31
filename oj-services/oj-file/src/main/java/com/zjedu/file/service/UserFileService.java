package com.zjedu.file.service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @Author Zhong
 * @Create 2025/3/31 14:34
 * @Version 1.0
 * @Description
 */

public interface UserFileService
{
    void generateUserExcel(String key, HttpServletResponse response) throws IOException;
}
