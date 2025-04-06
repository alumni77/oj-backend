package com.zjedu.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author Zhong
 * @Create 2025/4/6 19:46
 * @Version 1.0
 * @Description
 */

@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SwitchConfigDTO
{
    /**
     * 是否开启公开评测
     */
    private Boolean openPublicJudge;

    /**
     * 是否隐藏非比赛提交详情的代码(超管不受限制)
     */
    private Boolean hideNonContestSubmissionCode;

    /**
     * 非比赛的提交间隔秒数
     */
    private Integer defaultSubmitInterval;

}
