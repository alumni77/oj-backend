package com.zjedu.judgeserve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.judge.Judge;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/4/8 19:56
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface JudgeMapper extends BaseMapper<Judge>
{
}
