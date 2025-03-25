package com.zjedu.pojo.entity.problem;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 * @Author Zhong
 * @Create 2025/3/25 14:12
 * @Version 1.0
 * @Description
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name="Case对象", description="题目测试样例")
public class ProblemCase {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "题目id")
    private Long pid;

    @Schema(description = "测试样例的输入")
    private String input;

    @Schema(description = "测试样例的输出")
    private String output;

    @Schema(description = "该测试样例的IO得分")
    private Integer score;

    @Schema(description = "subtask分组的编号")
    private Integer groupNum;

    @Schema(description = "0可用，1不可用")
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
}
