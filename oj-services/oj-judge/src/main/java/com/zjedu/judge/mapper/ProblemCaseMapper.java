package com.zjedu.judge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.problem.ProblemCase;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/3/25 19:06
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface ProblemCaseMapper extends BaseMapper<ProblemCase>
{
}