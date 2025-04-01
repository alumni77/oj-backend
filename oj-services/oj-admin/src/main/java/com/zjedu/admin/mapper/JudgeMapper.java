package com.zjedu.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.judge.Judge;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/4/1 21:10
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface JudgeMapper extends BaseMapper<Judge>
{
}
