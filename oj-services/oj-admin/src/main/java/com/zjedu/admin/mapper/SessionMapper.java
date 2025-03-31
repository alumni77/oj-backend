package com.zjedu.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.user.Session;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/3/31 15:51
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface SessionMapper extends BaseMapper<Session>
{

}
