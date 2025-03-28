package com.zjedu.pojo.vo;

import com.zjedu.pojo.entity.judge.JudgeCase;
import lombok.Data;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/28 16:34
 * @Version 1.0
 * @Description
 */

@Data
public class JudgeCaseVO
{

    /**
     * 当judgeCaseMode为default时
     */
    private List<JudgeCase> judgeCaseList;

    /**
     * 当judgeCaseMode为subtask_lowest,subtask_average时
     */
    private List<SubTaskJudgeCaseVO> subTaskJudgeCaseVoList;

    /**
     * default,subtask_lowest,subtask_average
     */
    private String judgeCaseMode;
}
