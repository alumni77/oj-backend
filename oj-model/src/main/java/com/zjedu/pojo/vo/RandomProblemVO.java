package com.zjedu.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author Zhong
 * @Create 2025/3/28 21:53
 * @Version 1.0
 * @Description
 */

@Data
public class RandomProblemVO
{

    @Schema(description = "题目id")
    private String problemId;
}