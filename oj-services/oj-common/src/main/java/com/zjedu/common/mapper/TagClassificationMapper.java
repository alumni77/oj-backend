package com.zjedu.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.problem.TagClassification;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/3/30 14:21
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface TagClassificationMapper extends BaseMapper<TagClassification>
{
}
