package com.zjedu.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.problem.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/3/31 11:06
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface TagMapper extends BaseMapper<Tag>
{

}