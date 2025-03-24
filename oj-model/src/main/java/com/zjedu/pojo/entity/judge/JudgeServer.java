package com.zjedu.pojo.entity.judge;

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
 * @Create 2025/3/24 21:48
 * @Version 1.0
 * @Description
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "JudgeServer对象", description = "判题服务器配置")
public class JudgeServer
{
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "判题服务名字")
    private String name;

    @Schema(description = "判题机ip")
    private String ip;

    @Schema(description = "判题机端口号")
    private Integer port;

    @Schema(description = "ip:port")
    private String url;

    @Schema(description = "判题机所在服务器cpu核心数")
    private Integer cpuCore;

    @Schema(description = "当前判题数")
    private Integer taskNumber;

    @Schema(description = "判题并发最大数")
    private Integer maxTaskNumber;

    @Schema(description = "0可用，1不可用")
    private Integer status;

    @Schema(description = "是否为远程判题vj")
    private Boolean isRemote;

    @Schema(description = "是否可提交CF")
    private Boolean cfSubmittable;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

}