package com.zjedu.training.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.vo.ProblemVO;
import com.zjedu.pojo.vo.TrainingRankVO;
import com.zjedu.pojo.vo.TrainingVO;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/30 15:36
 * @Version 1.0
 * @Description
 */

public interface TrainingService
{
    CommonResult<IPage<TrainingVO>> getTrainingList(Integer limit, Integer currentPage, String keyword, Long categoryId, String auth);

    CommonResult<TrainingVO> getTraining(Long tid);

    CommonResult<List<ProblemVO>> getTrainingProblemList(Long tid);

    CommonResult<IPage<TrainingRankVO>> getTrainingRank(Long tid, Integer limit, Integer currentPage, String keyword);
}
