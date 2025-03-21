package com.zjedu.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @Author Zhong
 * @Create 2025/3/21 13:36
 * @Version 1.0
 * @Description 用户主页的数据格式
 */

@Schema(name = "用户主页的数据格式类UserHomeVO", description = "")
@Data
public class UserHomeVO
{

    @Schema(description = "用户id")
    private String uid;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "学校")
    private String school;

    @Schema(description = "个性签名")
    private String signature;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "gender")
    private String gender;

    @Schema(description = "github地址")
    private String github;

    @Schema(description = "博客地址")
    private String blog;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "头衔、称号")
    private String titleName;

    @Schema(description = "头衔、称号的颜色")
    private String titleColor;

    @Schema(description = "总提交数")
    private Integer total;

    @Schema(description = "cf得分")
    private Integer rating;

    @Schema(description = "OI得分列表")
    private List<Integer> scoreList;

    @Schema(description = "已解决题目列表")
    private List<String> solvedList;

    @Schema(description = "难度=>[P1000,P1001]")
    private Map<Integer, List<UserHomeProblemVO>> solvedGroupByDifficulty;

    @Schema(description = "最近上线时间")
    private Date recentLoginTime;

}