package com.zjedu.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/4/5 19:29
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface UserInfoMapper extends BaseMapper<UserInfo>
{
}
