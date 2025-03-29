package com.zjedu.problem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.problem.ProblemTag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/3/29 15:04
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface ProblemTagMapper extends BaseMapper<ProblemTag>
{
}
