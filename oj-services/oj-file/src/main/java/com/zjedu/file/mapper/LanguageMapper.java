package com.zjedu.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.problem.Language;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/3/31 11:03
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface LanguageMapper extends BaseMapper<Language>
{
}
