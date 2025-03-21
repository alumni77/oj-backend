package com.zjedu.account.feign.fallback;

import com.zjedu.account.feign.PassportFeignClient;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.UserHomeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
    public UserInfo getByUsername(String username)
    {
        log.error("调用passport服务失败——兜底回调");
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("未知用户");
        return userInfo;
    }

    @Override
    public UserHomeVO getUserHomeInfo(String uid, String username)
    {
        log.error("调用passport服务失败——兜底回调");
        return null;
    }
}
