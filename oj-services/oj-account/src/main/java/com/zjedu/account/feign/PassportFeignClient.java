package com.zjedu.account.feign;

import com.zjedu.account.feign.fallback.PassportFeignClientFallback;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.pojo.entity.user.Role;
import com.zjedu.pojo.vo.ACMRankVO;
import com.zjedu.pojo.vo.UserHomeVO;
import com.zjedu.pojo.vo.UserInfoVO;
import com.zjedu.pojo.vo.UserRolesVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/21 10:46
 * @Version 1.0
 * @Description
 */

@FeignClient(value = "oj-passport", path = "/api/passport", fallback = PassportFeignClientFallback.class)
public interface PassportFeignClient
{

    @GetMapping("/get-user-home-info")
    UserHomeVO getUserHomeInfo(@RequestParam(value = "uid", required = false) String uid,
                               @RequestParam(value = "username", required = false) String username);

    @PostMapping("/change-user-info")
    boolean updateUserInfo(@RequestBody UserInfoVO userInfoVo, @RequestParam("userId") String userId) throws StatusFailException;

    @GetMapping("/get-user-role")
    UserRolesVO getUserRoles(@RequestParam("uid") String uid, @RequestParam("username") String username);

    @GetMapping("/get-roles-by-uid")
    List<Role> getRolesByUid(@RequestParam("uid") String uid);

    @GetMapping("/get-recent7-ac-rank")
    List<ACMRankVO> getRecent7ACRank();
}