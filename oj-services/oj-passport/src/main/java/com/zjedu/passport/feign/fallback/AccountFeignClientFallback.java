package com.zjedu.passport.feign.fallback;

import com.zjedu.passport.feign.AccountFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author Zhong
 * @Create 2025/3/21 16:21
 * @Version 1.0
 * @Description
 */

@Slf4j
@Component
public class AccountFeignClientFallback implements AccountFeignClient
{
}
