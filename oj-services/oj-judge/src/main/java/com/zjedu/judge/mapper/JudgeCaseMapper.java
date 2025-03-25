package com.zjedu.judge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.judge.JudgeCase;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/3/25 14:20
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface JudgeCaseMapper extends BaseMapper<JudgeCase>
{

}
