package com.zjedu.judgeserve.feign;

import com.zjedu.judgeserve.feign.fallback.PassportFeignClientFallback;
import com.zjedu.pojo.entity.user.Role;
import com.zjedu.pojo.vo.UserRolesVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/26 19:09
 * @Version 1.0
 * @Description
 */

@FeignClient(value = "oj-passport",path = "/api/passport",fallback = PassportFeignClientFallback.class)
public interface PassportFeignClient
{

    @GetMapping("/get-roles-by-uid")
    List<Role> getRolesByUid(@RequestParam("uid") String uid);

    @GetMapping("/get-user-role")
    UserRolesVO getUserRoles(@RequestParam("uid") String uid, @RequestParam("username") String username);
}
