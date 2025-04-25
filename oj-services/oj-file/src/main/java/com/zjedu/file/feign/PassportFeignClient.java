package com.zjedu.file.feign;

import com.zjedu.file.feign.fallback.PassportFeignClientFallback;
import com.zjedu.pojo.vo.UserRolesVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @Author Zhong
 * @Create 2025/3/30 20:13
 * @Version 1.0
 * @Description
 */

@FeignClient(value = "oj-passport", url = "http://121.40.127.160:8000",path = "/api/passport", fallback = PassportFeignClientFallback.class)
public interface PassportFeignClient
{
    @GetMapping("/get-user-role")
    UserRolesVO getUserRoles(@RequestParam("uid") String uid, @RequestParam("username") String username);


}