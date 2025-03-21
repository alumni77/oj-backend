package com.zjedu.pojo.vo;

import com.zjedu.pojo.entity.problem.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/21 14:29
 * @Version 1.0
 * @Description
 */

@Schema(name = "题目列表查询对象ProblemVO", description = "")
@Data
public class ProblemVO implements Serializable
{

    @Schema(description = "题目id")
    private Long pid;

    @Schema(description = "题目展示id")
    private String problemId;

    @Schema(description = "题目标题")
    private String title;

    @Schema(description = "题目难度")
    private Integer difficulty;

    @Schema(description = "题目类型")
    private Integer type;

    @Schema(description = "题目标签")
    private List<Tag> tags;

    // 以下为题目做题情况

    @Schema(description = "该题总提交数")
    private Integer total = 0;

    @Schema(description = "通过提交数")
    private Integer ac = 0;

    @Schema(description = "空间超限提交数")
    private Integer mle = 0;

    @Schema(description = "时间超限提交数")
    private Integer tle = 0;

    @Schema(description = "运行错误提交数")
    private Integer re = 0;

    @Schema(description = "格式错误提交数")
    private Integer pe = 0;

    @Schema(description = "编译错误提交数")
    private Integer ce = 0;

    @Schema(description = "答案错误提交数")
    private Integer wa = 0;

    @Schema(description = "系统错误提交数")
    private Integer se = 0;

    @Schema(description = "该IO题目分数总和")
    private Integer pa = 0;

    @Schema(description = "IO题目总分数")
    private Integer score;

    public void setProblemCountVo(ProblemCountVO problemCountVo)
    {
        this.total = problemCountVo.getTotal() == null ? 0 : problemCountVo.getTotal();
        this.ac = problemCountVo.getAc() == null ? 0 : problemCountVo.getAc();
        this.mle = problemCountVo.getMle() == null ? 0 : problemCountVo.getMle();
        this.tle = problemCountVo.getTle() == null ? 0 : problemCountVo.getTle();
        this.re = problemCountVo.getRe() == null ? 0 : problemCountVo.getRe();
        this.pe = problemCountVo.getPe() == null ? 0 : problemCountVo.getPe();
        this.ce = problemCountVo.getCe() == null ? 0 : problemCountVo.getCe();
        this.wa = problemCountVo.getWa() == null ? 0 : problemCountVo.getWa();
        this.se = problemCountVo.getSe() == null ? 0 : problemCountVo.getSe();
        this.pa = problemCountVo.getPa() == null ? 0 : problemCountVo.getPa();
    }

}