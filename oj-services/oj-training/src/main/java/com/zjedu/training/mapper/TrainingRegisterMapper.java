package com.zjedu.training.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.training.TrainingRegister;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/3/30 16:18
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface TrainingRegisterMapper extends BaseMapper<TrainingRegister>
{
}
