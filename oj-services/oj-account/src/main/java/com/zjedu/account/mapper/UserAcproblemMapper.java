package com.zjedu.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.user.UserAcproblem;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/3/21 14:23
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface UserAcproblemMapper extends BaseMapper<UserAcproblem>
{
}
