package com.zjedu.pojo.vo;

import lombok.Data;

/**
 * @Author Zhong
 * @Create 2025/3/21 19:07
 * @Version 1.0
 * @Description
 */

@Data
public class ChangeAccountVO
{

    private Integer code;

    private String msg;

    private UserInfoVO userInfo;
}