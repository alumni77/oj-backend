package com.zjedu.training.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.training.TrainingCategory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/3/30 16:10
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface TrainingCategoryMapper extends BaseMapper<TrainingCategory>
{

    TrainingCategory getTrainingCategoryByTrainingId(@Param("tid") Long tid);
}