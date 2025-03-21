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
 * @Create 2025/3/21 14:27
 * @Version 1.0
 * @Description
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "Problem对象", description = "")
public class Problem implements Serializable
{

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "题目的自定义ID 例如（HOJ-1000）")
    private String problemId;

    @Schema(description = "题目")
    private String title;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "0为ACM,1为OI")
    private Integer type;

    @Schema(description = "default,spj,interactive")
    private String judgeMode;

    @Schema(description = "default,subtask_lowest,subtask_average")
    private String judgeCaseMode;

    @Schema(description = "单位ms")
    private Integer timeLimit;

    @Schema(description = "单位mb")
    private Integer memoryLimit;

    @Schema(description = "单位mb")
    private Integer stackLimit;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "输入描述")
    private String input;

    @Schema(description = "输出描述")
    private String output;

    @Schema(description = "题面样例")
    private String examples;

    @Schema(description = "是否为vj判题")
    private Boolean isRemote;

    @Schema(description = "题目来源（vj判题时例如HDU-1000的链接）")
    private String source;

    @Schema(description = "题目难度")
    private Integer difficulty;

    @Schema(description = "备注,提醒")
    private String hint;

    @Schema(description = "默认为1公开，2为私有，3为比赛中")
    private Integer auth;

    @Schema(description = "当该题目为oi题目时的分数")
    private Integer ioScore;

    @Schema(description = "该题目对应的相关提交代码，用户是否可用分享")
    private Boolean codeShare;

    @Schema(description = "特判程序或交互程序的代码")
    @TableField(value = "spj_code", updateStrategy = FieldStrategy.IGNORED)
    private String spjCode;

    @Schema(description = "特判程序或交互程序的语言")
    @TableField(value = "spj_language", updateStrategy = FieldStrategy.IGNORED)
    private String spjLanguage;

    @Schema(description = "特判程序或交互程序的额外文件 json key:name description:content")
    @TableField(value = "user_extra_file", updateStrategy = FieldStrategy.IGNORED)
    private String userExtraFile;

    @Schema(description = "特判程序或交互程序的额外文件 json key:name description:content")
    @TableField(value = "judge_extra_file", updateStrategy = FieldStrategy.IGNORED)
    private String judgeExtraFile;

    @Schema(description = "是否默认去除用户代码的每行末尾空白符")
    private Boolean isRemoveEndBlank;

    @Schema(description = "是否默认开启该题目的测试样例结果查看")
    private Boolean openCaseResult;

    @Schema(description = "题目测试数据是否是上传的")
    private Boolean isUploadCase;

    @Schema(description = "题目测试数据的版本号")
    private String caseVersion;

    @Schema(description = "修改题目的管理员用户名")
    private String modifiedUser;

    @Schema(description = "是否为团队内的题目")
    private Boolean isGroup;

    @Schema(description = "团队ID")
    private Long gid;

    @Schema(description = "申请公开的进度：null为未申请，1为申请中，2为申请通过，3为申请拒绝")
    private Integer applyPublicProgress;

    @Schema(description = "是否是file io自定义输入输出文件模式")
    @TableField(value = "is_file_io")
    private Boolean isFileIO;

    @Schema(description = "题目指定的file io输入文件的名称")
    @TableField(value = "io_read_file_name")
    private String ioReadFileName;

    @Schema(description = "题目指定的file io输出文件的名称")
    @TableField(value = "io_write_file_name")
    private String ioWriteFileName;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
