package com.zjedu.pojo.entity.user;

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
 * @Create 2025/3/18 16:03
 * @Version 1.0
 * @Description
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "Role对象",description = "")
public class Role implements Serializable
{

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "角色")
    private String role;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "默认0可用，1不可用")
    private Integer status;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @Schema(description = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}