package com.zjedu.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @Author Zhong
 * @Create 2025/3/26 19:01
 * @Version 1.0
 * @Description
 */

@Data
@Schema(name = "返回的判题信息", description = "")
public class JudgeVO
{

    @Schema(description = "用户id")
    private String uid;

    @Schema(description = "提交id")
    @TableId(value = "submit_id", type = IdType.AUTO)
    private Long submitId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "题目id")
    private Long pid;

    @Schema(description = "题目展示id")
    private String displayPid;

    @Schema(description = "题目标题")
    private String title;

    @Schema(description = "比赛display_id")
    private String displayId;

    @Schema(description = "结果码具体参考文档")
    private Date submitTime;

    @Schema(description = "结果码具体参考文档")
    private Integer status;

    @Schema(description = "0为代码全部人可见，1为仅自己可见。")
    private Boolean share;

    @Schema(description = "运行时间(ms)")
    private Integer time;

    @Schema(description = "运行内存（b）")
    private Integer memory;

    @Schema(description = "题目得分，ACM题目默认为null")
    private Integer score;

    @Schema(description = "该题在OI排行榜的分数")
    private Integer oiRankScore;

    @Schema(description = "代码长度")
    private Integer length;

    @Schema(description = "代码语言")
    private String language;

    @Schema(description = "比赛id，非比赛题目默认为0")
    private Long cid;

    @Schema(description = "比赛中题目排序id，非比赛题目默认为0")
    private Long cpid;

    @Schema(description = "题目来源")
    private String source;

    @Schema(description = "判题机ip")
    private String judger;

    @Schema(description = "提交者所在ip")
    private String ip;

    @Schema(description = "是否人工评测")
    private Boolean isManual;
}