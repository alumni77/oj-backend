package com.zjedu.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author Zhong
 * @Create 2025/3/30 19:20
 * @Version 1.0
 * @Description
 */

@Data
@Schema(name = "用户在训练的记录", description = "")
public class TrainingRecordVO
{

    private Long id;

    @Schema(description = "训练id")
    private Long tid;

    @Schema(description = "训练题目id")
    private Long tpid;

    @Schema(description = "用户id")
    private String uid;

    @Schema(description = "题目id")
    private Long pid;

    @Schema(description = "提交id")
    private Long submitId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "学校")
    private String school;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "真实姓名")
    private String realname;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "提交结果状态码")
    private Integer status;

    @Schema(description = "OI得分")
    private Integer score;

    @Schema(description = "提交耗时")
    private Integer useTime;
}