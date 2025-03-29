package com.zjedu.problem.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.ACMRankVO;
import com.zjedu.pojo.vo.OIRankVO;
import com.zjedu.pojo.vo.UserRolesVO;
import com.zjedu.problem.feign.fallback.PassportFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/28 22:01
 * @Version 1.0
 * @Description
 */

@FeignClient(value = "oj-passport", path = "/api/passport", fallback = PassportFeignClientFallback.class)
public interface PassportFeignClient
{
    @GetMapping("/get-user-by-uid")
    UserInfo getByUid(@RequestParam("uid") String uid);

    @GetMapping("/get-user-role")
    UserRolesVO getUserRoles(@RequestParam("uid") String uid);

    @PostMapping("get-oi-rank-list")
    Page<OIRankVO> getOIRankList(@RequestBody Page<OIRankVO> page, @RequestParam(required = false) List<String> uidList);

    @PostMapping("/get-acm-rank-list")
    Page<ACMRankVO> getACMRankList(@RequestBody Page<ACMRankVO> page, @RequestParam(required = false) List<String> uidList);

    @GetMapping("/search-user-uid-list")
    List<String> searchUserUidList(@RequestParam String keyword);
}