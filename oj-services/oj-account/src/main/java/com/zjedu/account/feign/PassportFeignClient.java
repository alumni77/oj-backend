package com.zjedu.account.feign;

import com.zjedu.account.feign.fallback.PassportFeignClientFallback;
import com.zjedu.pojo.entity.user.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author Zhong
 * @Create 2025/3/21 10:46
 * @Version 1.0
 * @Description
 */

@FeignClient(value = "oj-passport", path = "/api/passport", fallback = PassportFeignClientFallback.class)
public interface PassportFeignClient
{
    @GetMapping("/{username}")
    UserInfo getByUsername(@PathVariable String username);
}
