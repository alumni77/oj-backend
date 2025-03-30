package com.zjedu.file.feign.fallback;

import com.zjedu.file.feign.PassportFeignClient;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.UserRolesVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author Zhong
 * @Create 2025/3/30 20:14
 * @Version 1.0
 * @Description
 */

@Slf4j
@Component
public class PassportFeignClientFallback implements PassportFeignClient
{
    @Override
    public UserInfo getByUid(String uid)
    {
        log.error("调用passport-getByUid服务失败——兜底回调");
        return null;
    }

    @Override
    public UserRolesVO getUserRoles(String uid)
    {
        log.error("调用passport-getUserRoles服务失败——兜底回调");

        return null;
    }

    @Override
    public void updateUserAvatar(String avatar, String uid)
    {
        log.error("调用passport-updateUserAvatar服务失败——兜底回调");
    }
}
