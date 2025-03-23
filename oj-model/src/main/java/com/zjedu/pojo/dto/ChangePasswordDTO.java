package com.zjedu.pojo.dto;

import lombok.Data;

/**
 * @Author Zhong
 * @Create 2025/3/21 19:08
 * @Version 1.0
 * @Description
 */

@Data
public class ChangePasswordDTO
{

    private String oldPassword;

    private String newPassword;
}