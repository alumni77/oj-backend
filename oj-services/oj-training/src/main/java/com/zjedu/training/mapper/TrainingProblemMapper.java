package com.zjedu.training.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.training.TrainingProblem;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/30 15:52
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface TrainingProblemMapper extends BaseMapper<TrainingProblem>
{
    List<TrainingProblem> getTrainingListAcceptedCountByUid(List<Long> tidList, String uid);
}
