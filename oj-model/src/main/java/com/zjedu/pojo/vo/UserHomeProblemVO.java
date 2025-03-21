package com.zjedu.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @Author Zhong
 * @Create 2025/3/21 13:37
 * @Version 1.0
 * @Description
 */

@Data
@Builder
public class UserHomeProblemVO {

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "题目的自定义ID 例如（HOJ-1000）")
    private String problemId;

    @Schema(description = "题目难度")
    private Integer difficulty;
}
