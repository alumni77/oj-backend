package com.zjedu.pojo.entity.judge;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Zhong
 * @Create 2025/3/25 14:18
 * @Version 1.0
 * @Description
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name="JudgeCase对象", description="")
public class JudgeCase implements Serializable
{

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "题目id")
    private Long pid;

    @Schema(description = "判题id")
    private Long submitId;

    @Schema(description = "用户id")
    private String uid;

    @Schema(description = "测试样例id")
    private Long caseId;

    @Schema(description = "测试该样例所用时间ms")
    private Integer time;

    @Schema(description = "测试该样例所用空间KB")
    private Integer memory;

    @Schema(description = "IO得分")
    private Integer score;

    @Schema(description = "测试该样例结果状态码")
    private Integer status;

    @Schema(description = "样例输入，输入文件名")
    private String inputData;

    @Schema(description = "样例输出，输出文件名")
    private String outputData;

    @Schema(description = "用户样例输出，暂不使用，当前用于记录对单个测试点的输出或信息提示")
    private String userOutput;

    @Schema(description = "subtask分组的组号")
    private Integer groupNum;

    @Schema(description = "排序")
    private Integer seq;

    @Schema(description = "default,subtask_lowest,subtask_average")
    private String mode;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

}