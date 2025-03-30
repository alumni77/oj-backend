package com.zjedu.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Zhong
 * @Create 2025/3/30 15:38
 * @Version 1.0
 * @Description
 */

@Schema(name = "训练题单查询对象TrainingVO", description = "")
@Data
public class TrainingVO implements Serializable
{

    @Schema(description = "训练id")
    private Long id;

    @Schema(description = "题目标题")
    private String title;

    @Schema(description = "训练描述")
    private String description;

    @Schema(description = "训练创建者用户名")
    private String author;

    @Schema(description = "训练题单权限类型：Public、Private")
    private String auth;

    @Schema(description = "训练题单的分类名称")
    private String categoryName;

    @Schema(description = "训练题单的分类背景颜色")
    private String categoryColor;

    @Schema(description = "训练题单的编号，升序排序")
    private Integer rank;

    @Schema(description = "该训练的总题数")
    private Integer problemCount;

    @Schema(description = "当前用户已完成训练的题数")
    private Integer acCount;

//    @Schema(description = "团队ID")
//    private Long gid;

    @Schema(description = "训练更新时间")
    private Date gmtModified;


}