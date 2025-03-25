package com.zjedu.pojo.entity.judge;

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
 * @Create 2025/3/21 14:42
 * @Version 1.0
 * @Description
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "Judge对象", description = "")
public class Judge implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "submit_id", type = IdType.AUTO)
    private Long submitId;

    @Schema(description = "题目id")
    private Long pid;

    @Schema(description = "题目展示id")
    private String displayPid;

    @Schema(description = "用户id")
    private String uid;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "提交的时间")
    private Date submitTime;

    @Schema(description = "结果码具体参考文档")
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    private Integer status;

    @Schema(description = "0为仅自己可见，1为全部人可见。")
    private Boolean share;

    @Schema(description = "错误提醒（编译错误，或者vj提醒）")
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    private String errorMessage;

    @Schema(description = "运行时间(ms)")
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    private Integer time;

    @Schema(description = "运行内存(kb)")
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    private Integer memory;

    @Schema(description = "IO判题不为空")
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    private Integer score;

    @Schema(description = "代码长度")
    private Integer length;

    @Schema(description = "代码")
    private String code;

    @Schema(description = "代码语言")
    private String language;

    @Schema(description = "比赛id，非比赛提交默认为0")
    private Long cid;

    @Schema(description = "比赛中题目排序id，非比赛提交默认为0")
    private Long cpid;

    @Schema(description = "团队id，非团队内题目提交为null")
    private Long gid;

    @Schema(description = "判题机名称")
    private String judger;

    @Schema(description = "提交者所在ip")
    private String ip;

    @Schema(description = "废弃")
    private Integer version;

    @Schema(description = "该题在OI排行榜的分数")
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    private Integer oiRankScore;

    @Schema(description = "vjudge判题在其它oj的提交id")
    private Long vjudgeSubmitId;

    @Schema(description = "vjudge判题在其它oj的提交用户名")
    private String vjudgeUsername;

    @Schema(description = "vjudge判题在其它oj的提交账号密码")
    private String vjudgePassword;

    @Schema(description = "是否人工评测")
    private Boolean isManual;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
