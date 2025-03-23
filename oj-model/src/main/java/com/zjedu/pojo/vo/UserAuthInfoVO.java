package com.zjedu.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/23 16:34
 * @Version 1.0
 * @Description
 */

@Data
public class UserAuthInfoVO {

    @Schema(description = "角色列表")
    private List<String> roles;

    @Schema(description = "权限列表")
    private List<String> permissions;
}

