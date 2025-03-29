package com.zjedu.pojo.entity.problem;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author Zhong
 * @Create 2025/3/29 15:10
 * @Version 1.0
 * @Description
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "Language对象", description = "")
@AllArgsConstructor
@NoArgsConstructor
public class Language implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "语言类型")
    private String contentType;

    @Schema(description = "语言描述")
    private String description;

    @Schema(description = "语言名字")
    private String name;

    @Schema(description = "编译指令")
    private String compileCommand;

    @Schema(description = "A+B模板")
    private String template;

    @Schema(description = "语言默认代码模板")
    private String codeTemplate;

    @Schema(description = "是否可作为特殊判题的一种语言")
    private Boolean isSpj;

    @Schema(description = "该语言属于哪个oj，自身oj用ME")
    private String oj;

    @Schema(description = "语言顺序")
    private Integer seq;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
}