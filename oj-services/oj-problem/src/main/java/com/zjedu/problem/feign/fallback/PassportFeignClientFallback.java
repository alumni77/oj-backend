package com.zjedu.problem.feign.fallback;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.ACMRankVO;
import com.zjedu.pojo.vo.OIRankVO;
import com.zjedu.pojo.vo.UserRolesVO;
import com.zjedu.problem.feign.PassportFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/28 22:02
 * @Version 1.0
 * @Description
 */

@Slf4j
@Component
public class PassportFeignClientFallback implements PassportFeignClient
{
    @Override
    public UserInfo getByUid(String uid)
    {
        log.error("调用passport-getByUid服务失败——兜底回调");
        return null;
    }

    @Override
    public UserRolesVO getUserRoles(String uid)
    {
        log.error("调用passport-getUserRoles服务失败——兜底回调");
        return null;
    }

    @Override
    public Page<OIRankVO> getOIRankList(Page<OIRankVO> page, List<String> uidList)
    {
        log.error("调用passport-getOIRankList服务失败——兜底回调");

        return null;
    }

    @Override
    public Page<ACMRankVO> getACMRankList(Page<ACMRankVO> page, List<String> uidList)
    {
        log.error("调用passport-getACMRankList服务失败——兜底回调");

        return null;
    }

    @Override
    public List<String> searchUserUidList(String keyword)
    {
        log.error("调用passport-searchUserUidList服务失败——兜底回调");

        return List.of();
    }
}
