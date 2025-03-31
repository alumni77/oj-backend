package com.zjedu.file.controller;

import com.zjedu.common.result.CommonResult;
import com.zjedu.file.service.ProblemFileService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @GetMapping("/export-problem")
    public void exportProblem(@RequestParam("pid") List<Long> pidList, HttpServletResponse response)
    {
        problemFileService.exportProblem(pidList, response);
    }


}
