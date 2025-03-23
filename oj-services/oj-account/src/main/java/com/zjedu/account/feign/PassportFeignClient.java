package com.zjedu.account.feign;

import com.zjedu.account.feign.fallback.PassportFeignClientFallback;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.UserHomeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Zhong
 * @Create 2025/3/21 10:46
 * @Version 1.0
 * @Description
 */

@FeignClient(value = "oj-passport", path = "/api/passport", fallback = PassportFeignClientFallback.class)
public interface PassportFeignClient
{
    @GetMapping("/get-user")
    UserInfo getByUsername(@RequestParam("username") String username);

    @GetMapping("/get-user-home-info")
    UserHomeVO getUserHomeInfo(@RequestParam(value = "uid", required = false) String uid,
                               @RequestParam(value = "username", required = false) String username);

    @GetMapping("/get-user-by-uid")
    UserInfo getByUid(@RequestParam("uid") String uid);

    @PutMapping("/update-password")
    boolean updatePassword(@RequestParam("uid") String uid, @RequestParam("newPassword") String newPassword);
}