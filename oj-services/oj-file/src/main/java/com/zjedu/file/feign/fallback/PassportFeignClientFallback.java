package com.zjedu.file.feign.fallback;

import com.zjedu.file.feign.PassportFeignClient;
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
    public UserRolesVO getUserRoles(String uid, String username)
    {
        log.error("调用passport-getUserRoles服务失败——兜底回调");

        return null;
    }
}
