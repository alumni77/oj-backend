package com.zjedu.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Zhong
 * @Create 2025/3/18 10:19
 * @Version 1.0
 * @Description LoginDTO 登录数据实体类
 */

@Data
public class LoginDTO implements Serializable
{
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
