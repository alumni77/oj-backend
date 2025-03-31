package com.zjedu.file.controller;

import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.file.service.TestCaseService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Author Zhong
 * @Create 2025/3/31 13:58
 * @Version 1.0
 * @Description
 */

@RestController
public class TestCaseController
{
    @Resource
    private TestCaseService testCaseService;


    @PostMapping("/upload-testcase-zip")
    public CommonResult<Map<Object, Object>> uploadTestcaseZip(@RequestParam("file") MultipartFile file,
                                                               @RequestParam(value = "mode", defaultValue = "default") String mode)
    {
        return testCaseService.uploadTestcaseZip(file, mode);
    }

    @GetMapping("/download-testcase")
    public void downloadTestcase(@RequestParam("pid") Long pid, HttpServletResponse response) throws StatusFailException, StatusForbiddenException
    {
        testCaseService.downloadTestcase(pid, response);
    }
}
