package com.zjedu.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/23 21:46
 * @Version 1.0
 * @Description
 */

@Data
public class SubmissionStatisticsVO
{

    @Schema(description = "最近七天日期格式 mm-dd,升序")
    private List<String> dateStrList;

    @Schema(description = "最近七天每天AC数量")
    private List<Long> acCountList;

    @Schema(description = "最近七天每天提交数量")
    private List<Long> totalCountList;
}
