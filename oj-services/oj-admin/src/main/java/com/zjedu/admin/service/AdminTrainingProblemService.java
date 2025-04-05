package com.zjedu.admin.service;

import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.TrainingProblemDTO;
import com.zjedu.pojo.entity.training.TrainingProblem;

import java.util.HashMap;

/**
 * @Author Zhong
 * @Create 2025/4/2 21:14
 * @Version 1.0
 * @Description
 */

public interface AdminTrainingProblemService
{
    CommonResult<HashMap<String, Object>> getProblemList(Integer limit, Integer currentPage, String keyword, Boolean queryExisted, Long tid);

    CommonResult<Void> updateProblem(TrainingProblem trainingProblem);

    CommonResult<Void> deleteProblem(Long pid, Long tid);

    CommonResult<Void> addProblemFromPublic(TrainingProblemDTO trainingProblemDto);

}
