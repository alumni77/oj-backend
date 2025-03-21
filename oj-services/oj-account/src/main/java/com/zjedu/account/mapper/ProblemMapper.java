package com.zjedu.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.vo.ProblemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/21 14:35
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface ProblemMapper extends BaseMapper<Problem>
{
    List<ProblemVO> getProblemList(IPage page,
                                   @Param("pid") Long pid,
                                   @Param("keyword") String keyword,
                                   @Param("difficulty") Integer difficulty,
                                   @Param("tid") List<Long> tid,
                                   @Param("tagListSize") Integer tagListSize,
                                   @Param("oj") String oj);
}