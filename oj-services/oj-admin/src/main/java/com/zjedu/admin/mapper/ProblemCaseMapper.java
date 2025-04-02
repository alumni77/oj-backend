package com.zjedu.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.problem.ProblemCase;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/4/2 18:55
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface ProblemCaseMapper extends BaseMapper<ProblemCase>
{
}