package com.zjedu.pojo.vo;

import lombok.Data;

/**
 * @Author Zhong
 * @Create 2025/3/29 16:03
 * @Version 1.0
 * @Description
 */

@Data
public class ProblemFullScreenListVO {

    private Long pid;

    private String problemId;

    private String title;

    private Integer status;

    private Integer score;
}
