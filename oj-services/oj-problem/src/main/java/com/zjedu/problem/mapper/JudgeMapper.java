package com.zjedu.problem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.judge.Judge;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/4/8 20:26
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface JudgeMapper extends BaseMapper<Judge>
{
}
