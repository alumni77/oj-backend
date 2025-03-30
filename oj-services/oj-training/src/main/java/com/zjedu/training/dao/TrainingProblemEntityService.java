package com.zjedu.training.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjedu.pojo.entity.training.TrainingProblem;
import com.zjedu.pojo.vo.ProblemVO;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/30 15:51
 * @Version 1.0
 * @Description
 */

public interface TrainingProblemEntityService extends IService<TrainingProblem>
{
    List<TrainingProblem> getTrainingListAcceptedCountByUid(List<Long> tidList, String uid);

    List<Long> getTrainingProblemIdList(Long tid);

    Integer getUserTrainingACProblemCount(String uuid, List<Long> trainingProblemIdList);

    List<ProblemVO> getTrainingProblemList(Long tid);
}
