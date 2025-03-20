package com.zjedu.pojo.dto;

import lombok.Data;

/**
 * @Author Zhong
 * @Create 2025/3/20 14:32
 * @Version 1.0
 * @Description
 */

@Data
public class ResetPasswordDTO
{

    private String username;

    private String password;

    private String code;
}