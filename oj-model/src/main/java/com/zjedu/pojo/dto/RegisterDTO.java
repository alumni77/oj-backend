package com.zjedu.pojo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * @Author Zhong
 * @Create 2025/3/20 10:27
 * @Version 1.0
 * @Description
 */

@Data
@Accessors(chain = true)
public class RegisterDTO implements Serializable
{

    @Nullable
    private String uuid;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    private String email;

    @NotBlank(message = "验证码不能为空")
    private String code;
}