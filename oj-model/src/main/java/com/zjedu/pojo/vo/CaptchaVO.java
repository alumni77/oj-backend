package com.zjedu.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author Zhong
 * @Create 2025/3/30 13:43
 * @Version 1.0
 * @Description
 */

@Data
public class CaptchaVO
{

    @Schema(description = "验证码图片的base64")
    private String img;

    @Schema(description = "验证码key")
    private String captchaKey;
}