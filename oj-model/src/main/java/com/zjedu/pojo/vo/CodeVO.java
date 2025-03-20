package com.zjedu.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author Zhong
 * @Create 2025/3/19 21:24
 * @Version 1.0
 * @Description 注册验证码VO类
 */

@Data
public class CodeVO
{
    @Schema(description = "验证码")
    private String code;

    @Schema(description = "验证码有效时间,单位秒")
    private Integer expire;
}
