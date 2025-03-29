package com.zjedu.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Zhong
 * @Create 2025/3/29 16:12
 * @Version 1.0
 * @Description
 */

@Schema(name = "OI排行榜数据类OIRankVO", description = "")
@Data
public class OIRankVO implements Serializable
{
    @Schema(description = "用户id")
    private String uid;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "个性签名")
    private String signature;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "头衔、称号")
    private String titleName;

    @Schema(description = "头衔、称号的颜色")
    private String titleColor;

    @Schema(description = "OI得分列表")
    private Integer score;

    @Schema(description = "总提交数")
    private Integer total;

    @Schema(description = "总通过数")
    private Integer ac;

}