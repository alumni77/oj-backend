package com.zjedu.pojo.entity.common;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author Zhong
 * @Create 2025/3/23 21:20
 * @Version 1.0
 * @Description
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "File对象", description = "")
@TableName("`file`")
public class File implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    private String uid;

//    @Schema(description = "团队id")
//    private Long gid;

    @Schema(description = "文件所属类型，例如avatar")
    @TableField("`type`")
    private String type;

    @Schema(description = "文件名")
    private String name;

    @Schema(description = "文件后缀格式")
    private String suffix;

    @Schema(description = "文件所在文件夹的路径")
    private String folderPath;

    @Schema(description = "文件绝对路径")
    private String filePath;

    @Schema(description = "是否删除")
    @TableField("`delete`")
    private Boolean delete;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @Schema(description = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

}