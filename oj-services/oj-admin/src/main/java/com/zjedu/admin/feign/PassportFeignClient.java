package com.zjedu.admin.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.admin.feign.fallback.PassportFeignClientFallback;
import com.zjedu.pojo.entity.user.Role;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.UserRolesVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/31 15:54
 * @Version 1.0
 * @Description
 */

@FeignClient(value = "oj-passport", path = "/api/passport", fallback = PassportFeignClientFallback.class)
public interface PassportFeignClient
{
    @GetMapping("/get-user-by-uid")
    UserInfo getByUid(@RequestParam("uid") String uid);

    @GetMapping("/get-user-role")
    UserRolesVO getUserRoles(@RequestParam("uid") String uid, @RequestParam("username") String username);

    @GetMapping("/get-roles-by-uid")
    List<Role> getRolesByUid(@RequestParam("uid") String uid);

    @GetMapping("/get-user-list")
    Page<UserRolesVO> getUserList(@RequestParam(value = "limit",required = false) int limit,
                                  @RequestParam(value = "currentPage",required = false) int currentPage,
                                  @RequestParam(value = "keyword", required = false) String keyword,
                                  @RequestParam(value = "onlyAdmin",defaultValue = "false") Boolean onlyAdmin);

    @DeleteMapping("/delete-cache")
    void deleteCache(@RequestParam("uid") String uid, @RequestParam("isRemoveSession") boolean isRemoveSession);


}
