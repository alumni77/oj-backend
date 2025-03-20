package com.zjedu.shiro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Zhong
 * @Create 2025/3/20 15:06
 * @Version 1.0
 * @Description 存在redis session的当前登录用户信息
 */

@Data
public class AccountProfile implements Serializable
{

    @Schema(description = "用户id")
    private String uid;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "真实姓名")
    private String realname;

    @Schema(description = "头衔名称")
    private String titleName;

    @Schema(description = "头衔背景颜色")
    private String titleColor;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "0可用，1不可用")
    private int status;

    //shiro登录用户实体默认主键获取方法要为getId
    public String getId() {
        return uid;
    }
}