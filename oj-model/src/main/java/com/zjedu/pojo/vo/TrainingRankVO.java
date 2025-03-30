package com.zjedu.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;

/**
 * @Author Zhong
 * @Create 2025/3/30 19:17
 * @Version 1.0
 * @Description
 */

@Data
@Accessors(chain = true)
public class TrainingRankVO
{

    @Schema(description = "用户id")
    private String uid;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户真实姓名")
    private String realname;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "学校")
    private String school;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "ac题目数")
    private Integer ac;

    @Schema(description = "总运行时间ms")
    private Integer totalRunTime;

    @Schema(description = "有提交的题的提交详情")
    private HashMap<String, HashMap<String, Object>> submissionInfo;
}