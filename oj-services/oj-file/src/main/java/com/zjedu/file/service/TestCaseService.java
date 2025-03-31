package com.zjedu.file.service;

import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.common.result.CommonResult;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Author Zhong
 * @Create 2025/3/31 13:58
 * @Version 1.0
 * @Description
 */

public interface TestCaseService
{
    CommonResult<Map<Object, Object>> uploadTestcaseZip(MultipartFile file, String mode);

    void downloadTestcase(Long pid, HttpServletResponse response) throws StatusForbiddenException, StatusFailException;

}
