package com.zjedu.pojo.vo;

import com.zjedu.pojo.entity.judge.Judge;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author Zhong
 * @Create 2025/3/26 19:36
 * @Version 1.0
 * @Description
 */

@Data
public class SubmissionInfoVO
{

    @Schema(description = "提交详情")
    private Judge submission;

    @Schema(description = "提交者是否可以分享该代码")
    private Boolean codeShare;
}