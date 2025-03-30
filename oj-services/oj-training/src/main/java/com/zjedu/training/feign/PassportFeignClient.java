package com.zjedu.training.feign;

import com.zjedu.pojo.entity.user.Role;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.training.feign.fallback.PassportFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/30 15:42
 * @Version 1.0
 * @Description
 */

@FeignClient(value = "oj-passport", path = "/api/passport", fallback = PassportFeignClientFallback.class)
public interface PassportFeignClient
{
    @GetMapping("/get-user-by-uid")
    UserInfo getByUid(@RequestParam("uid") String uid);

    @GetMapping("/get-roles-by-uid")
    List<Role> getRolesByUid(@RequestParam("uid") String uid);

    @GetMapping("/get-user-uid-list")
    List<String> getSuperAdminUidList();
}
