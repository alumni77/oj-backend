package com.zjedu.passport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.user.UserRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Zhong
 * @Create 2025/3/20 12:00
 * @Version 1.0
 * @Description
 */
@Mapper
@Repository
public interface UserRecordMapper extends BaseMapper<UserRecord>
{
}
