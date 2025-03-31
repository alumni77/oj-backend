package com.zjedu.file.service;

import com.zjedu.common.result.CommonResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author Zhong
 * @Create 2025/3/31 10:45
 * @Version 1.0
 * @Description
 */

public interface ProblemFileService
{
    CommonResult<Void> importProblem(MultipartFile file);

}
