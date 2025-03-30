package com.zjedu.pojo.entity.training;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author Zhong
 * @Create 2025/3/30 15:46
 * @Version 1.0
 * @Description
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "Training对象", description = "训练题单实体")
public class Training implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "训练题单标题")
    private String title;

    @Schema(description = "训练题单简介")
    private String description;

    @Schema(description = "训练题单创建者用户名")
    private String author;

    @Schema(description = "训练题单权限类型：Public、Private")
    private String auth;

    @Schema(description = "训练题单权限为Private时的密码")
    private String privatePwd;

    @Schema(description = "是否可用")
    private Boolean status;

    @Schema(description = "编号，升序排序")
    @TableField("`rank`")
    private Integer rank;

    @Schema(description = "是否为团队内的训练")
    private Boolean isGroup;

//    @Schema(description = "团队ID")
//    private Long gid;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

}