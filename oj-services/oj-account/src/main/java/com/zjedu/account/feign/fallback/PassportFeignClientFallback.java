package com.zjedu.account.feign.fallback;

import com.zjedu.account.feign.PassportFeignClient;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.pojo.entity.user.Role;
import com.zjedu.pojo.vo.ACMRankVO;
import com.zjedu.pojo.vo.UserHomeVO;
import com.zjedu.pojo.vo.UserInfoVO;
import com.zjedu.pojo.vo.UserRolesVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/21 10:48
 * @Version 1.0
 * @Description
 */

@Slf4j
@Component
public class PassportFeignClientFallback implements PassportFeignClient
{
    @Override
    public UserHomeVO getUserHomeInfo(String uid, String username)
    {
        log.error("调用passport-getUserHomeInfo服务失败——兜底回调");
        return null;
    }

    @Override
    public boolean updateUserInfo(UserInfoVO userInfoVo, String userId) throws StatusFailException
    {
        log.error("调用passport-updateUserInfo服务失败——兜底回调");

        return false;
    }

    @Override
    public UserRolesVO getUserRoles(String uid, String username)
    {
        log.error("调用passport-getUserRoles服务失败——兜底回调");

        return null;
    }

    @Override
    public List<Role> getRolesByUid(String uid)
    {
        log.error("调用passport-getRolesByUid服务失败——兜底回调");

        return null;
    }

    @Override
    public List<ACMRankVO> getRecent7ACRank()
    {
        log.error("调用passport-getRecent7ACRank服务失败——兜底回调");

        return null;
    }
}
