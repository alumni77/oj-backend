package com.zjedu.training.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.training.TrainingRecord;
import com.zjedu.pojo.vo.TrainingRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/30 19:24
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface TrainingRecordMapper extends BaseMapper<TrainingRecord>
{

    public List<TrainingRecordVO> getTrainingRecord(@Param("tid") Long tid);
}