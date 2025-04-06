package com.zjedu.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.common.File;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/4/6 16:03
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface FileMapper extends BaseMapper<File>
{
}
