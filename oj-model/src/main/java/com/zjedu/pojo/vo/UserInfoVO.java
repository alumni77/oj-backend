package com.zjedu.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/18 10:16
 * @Version 1.0
 * @Description UserInfoVO 视图对象
 */

@Data
public class UserInfoVO
{
    @Schema(description = "用户id")
    private String uid;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "头衔名称")
    private String titleName;

    @Schema(description = "头衔背景颜色")
    private String titleColor;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "学号")
    private String number;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "学校")
    private String school;

    @Schema(description = "专业")
    private String course;

    @Schema(description = "个性签名")
    private String signature;

    @Schema(description = "真实姓名")
    private String realname;

    @Schema(description = "github地址")
    private String github;

    @Schema(description = "博客地址")
    private String blog;

    @Schema(description = "cf的username")
    private String cfUsername;

    @Schema(description = "角色列表")
    private List<String> roleList;
}
