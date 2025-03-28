package com.zjedu.problem.feign;

import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.UserRolesVO;
import com.zjedu.problem.feign.fallback.PassportFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Zhong
 * @Create 2025/3/28 22:01
 * @Version 1.0
 * @Description
 */

@FeignClient(value = "oj-passport",path = "/api/passport",fallback = PassportFeignClientFallback.class)
public interface PassportFeignClient
{
    @GetMapping("/get-user-by-uid")
    UserInfo getByUid(@RequestParam("uid") String uid);

    @GetMapping("/get-user-role")
    UserRolesVO getUserRoles(@RequestParam("uid") String uid);
}