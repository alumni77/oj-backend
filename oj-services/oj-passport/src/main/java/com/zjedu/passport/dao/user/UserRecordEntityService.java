package com.zjedu.passport.dao.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjedu.pojo.entity.user.UserRecord;
import com.zjedu.pojo.vo.ACMRankVO;
import com.zjedu.pojo.vo.UserHomeVO;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/20 11:57
 * @Version 1.0
 * @Description 服务类
 */

public interface UserRecordEntityService extends IService<UserRecord>
{
    UserHomeVO getUserHomeInfo(String uid, String username);

    List<ACMRankVO> getRecent7ACRank();

}
