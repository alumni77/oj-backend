package com.zjedu.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author Zhong
 * @Create 2025/3/21 14:30
 * @Version 1.0
 * @Description
 */

@Data
@Accessors(chain = true)
public class ProblemCountVO implements Serializable
{

    private static final long serialVersionUID = 1L;

    private Long pid;

    private Integer total;

    private Integer ac;

    @Schema(description = "空间超限")
    private Integer mle;

    @Schema(description = "时间超限")
    private Integer tle;

    @Schema(description = "运行错误")
    private Integer re;

    @Schema(description = "格式错误")
    private Integer pe;

    @Schema(description = "编译错误")
    private Integer ce;

    @Schema(description = "答案错误")
    private Integer wa;

    @Schema(description = "系统错误")
    private Integer se;

    @Schema(description = "部分通过，OI题目")
    private Integer pa;

}
