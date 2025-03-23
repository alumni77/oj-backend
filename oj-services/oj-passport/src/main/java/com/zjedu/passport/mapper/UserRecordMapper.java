package com.zjedu.passport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.user.UserRecord;
import com.zjedu.pojo.vo.ACMRankVO;
import com.zjedu.pojo.vo.UserHomeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


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
    UserHomeVO getUserHomeInfo(@Param("uid") String uid, @Param("username") String username);

    List<ACMRankVO> getRecent7ACRank();
}
