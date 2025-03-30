package com.zjedu.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.problem.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/3/30 14:08
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface TagMapper extends BaseMapper<Tag>
{

}