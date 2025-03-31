package com.zjedu.file.controller;

import com.zjedu.common.result.CommonResult;
import com.zjedu.file.service.ProblemFileService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author Zhong
 * @Create 2025/3/31 10:45
 * @Version 1.0
 * @Description
 */

@RestController
public class ProblemFileController
{
    @Resource
    private ProblemFileService problemFileService;

    /**
     * zip文件导入题目 仅超级管理员可操作
     *
     * @param file
     * @return
     */
    @PostMapping("/import-problem")
    public CommonResult<Void> importProblem(@RequestParam("file") MultipartFile file)
    {
        return problemFileService.importProblem(file);
    }

}
