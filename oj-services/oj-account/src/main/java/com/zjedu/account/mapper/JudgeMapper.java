package com.zjedu.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.vo.ProblemCountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/21 14:45
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface JudgeMapper extends BaseMapper<Judge>
{
    List<ProblemCountVO> getProblemListCount(@Param("pidList") List<Long> pidList);

    List<Judge> getLastYearUserJudgeList(@Param("uid") String uid, @Param("username") String username);

}