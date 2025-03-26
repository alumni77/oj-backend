package com.zjedu.judge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.vo.JudgeVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/3/25 14:07
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface JudgeMapper extends BaseMapper<Judge>
{

    IPage<JudgeVO> getCommonJudgeList(Page<JudgeVO> page, String searchPid, Integer status, String username, String uid, Boolean completeProblemID, Long gid);

}
