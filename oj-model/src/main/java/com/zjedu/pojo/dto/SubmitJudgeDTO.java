package com.zjedu.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author Zhong
 * @Create 2025/3/26 20:52
 * @Version 1.0
 * @Description
 */

@Data
@Accessors(chain = true)
public class SubmitJudgeDTO
{
    @NotBlank(message = "题目id不能为空")
    private String pid;

    @NotBlank(message = "代码语言选择不能为空")
    private String language;

    @NotBlank(message = "提交的代码不能为空")
    private String code;

    @NotBlank(message = "提交的比赛id所属不能为空，若并非比赛提交，请设置为0")
    private Long cid;

    private Long tid;

    private Long gid;

    private Boolean isRemote;

}