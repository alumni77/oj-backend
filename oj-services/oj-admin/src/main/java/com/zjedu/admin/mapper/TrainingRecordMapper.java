package com.zjedu.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.training.TrainingRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/4/2 22:02
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface TrainingRecordMapper extends BaseMapper<TrainingRecord>
{
}
