package com.zjedu.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Author Zhong
 * @Create 2025/4/5 15:37
 * @Version 1.0
 * @Description
 */

@Data
public class TrainingProblemDTO
{

    @NotBlank(message = "题目id不能为空")
    private Long pid;

    @NotBlank(message = "训练id不能为空")
    private Long tid;

    @NotBlank(message = "题目在训练中的展示id不能为空")
    private String displayId;
}