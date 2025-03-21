package com.zjedu.pojo.entity.problem;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Zhong
 * @Create 2025/3/21 14:32
 * @Version 1.0
 * @Description
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name="Tag对象", description="")
public class Tag implements Serializable
{

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "标签名字")
    private String name;

    @Schema(description = "标签颜色")
    private String color;

    @Schema(description = "标签所属oj")
    private String oj;

    @Schema(description = "团队ID")
    private Long gid;

    @Schema(description = "标签分类ID")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long tcid;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
