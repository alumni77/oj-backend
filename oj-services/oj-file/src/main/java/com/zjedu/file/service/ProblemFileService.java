package com.zjedu.file.service;

import com.zjedu.common.result.CommonResult;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/31 10:45
 * @Version 1.0
 * @Description
 */

public interface ProblemFileService
{
    CommonResult<Void> importProblem(MultipartFile file);

    void exportProblem(List<Long> pidList, HttpServletResponse response);
}
