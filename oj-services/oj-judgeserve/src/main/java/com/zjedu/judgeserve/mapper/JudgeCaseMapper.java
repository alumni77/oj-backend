package com.zjedu.judgeserve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.judge.JudgeCase;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/3/28 16:58
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface JudgeCaseMapper extends BaseMapper<JudgeCase>
{

}
