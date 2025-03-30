package com.zjedu.training.feign.fallback;

import com.zjedu.pojo.entity.user.Role;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.training.feign.PassportFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/30 15:42
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
    public List<Role> getRolesByUid(String uid)
    {
        log.error("调用passport-getRolesByUid服务失败——兜底回调");
        return List.of();
    }
}
