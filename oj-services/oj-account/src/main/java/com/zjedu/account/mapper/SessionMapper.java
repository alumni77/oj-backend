package com.zjedu.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.user.Session;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/3/21 15:03
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface SessionMapper extends BaseMapper<Session>
{

}