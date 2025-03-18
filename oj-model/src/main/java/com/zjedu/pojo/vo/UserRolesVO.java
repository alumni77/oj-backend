package com.zjedu.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.zjedu.pojo.entity.user.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/18 15:55
 * @Version 1.0
 * @Description
 */
@Data
@Schema(name="用户信息以及其对应的角色",description = "")
public class UserRolesVO implements Serializable
{
    private static final long serialVersionUID = 10000L;

    @TableId
    @Schema(description = "用户id")
    private String uid;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "学校")
    private String school;

    @Schema(description = "专业")
    private String course;

    @Schema(description = "学号")
    private String number;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "真实姓名")
    private String realname;

    @Schema(description = "cf的username")
    private String cfUsername;

    @Schema(description = "github地址")
    private String github;

    @Schema(description = "博客地址")
    private String blog;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "头衔名称")
    private String titleName;

    @Schema(description = "头衔背景颜色")
    private String titleColor;

    @Schema(description = "个性签名")
    private String signature;

    @Schema(description = "0可用，1不可用")
    private int status;

    @Schema(description = "创建时间")
    private Date gmtCreate;

    @Schema(description = "修改时间")
    private Date gmtModified;

    @Schema(description = "角色列表")
    private List<Role> roles;
}
