package com.zjedu.pojo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/28 21:58
 * @Version 1.0
 * @Description 主要是获取前端题目列表页查询用户对应题目提交详情
 */

@Data
@Accessors(chain = true)
public class PidListDTO
{
    @NotEmpty(message = "查询的题目id列表不能为空")
    private List<Long> pidList;

    /**
     * 是否包含比赛结束后的提交统计
     */
    private Boolean containsEnd;

    private Long cid;

    private Long gid;
}