package com.zjedu.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Author Zhong
 * @Create 2025/4/5 19:23
 * @Version 1.0
 * @Description
 */

@Data
public class AdminEditUserDTO
{

    @NotBlank(message = "username不能为空")
    private String username;

    @NotBlank(message = "uid不能为空")
    private String uid;

    private String realname;

    private String email;

    private String password;

    private Integer type;

    private Integer status;

    private Boolean setNewPwd;

    private String titleName;

    private String titleColor;
}