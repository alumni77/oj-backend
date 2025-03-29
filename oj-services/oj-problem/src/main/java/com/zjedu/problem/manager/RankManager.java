package com.zjedu.problem.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.pojo.vo.ACMRankVO;
import com.zjedu.pojo.vo.OIRankVO;
import com.zjedu.problem.feign.PassportFeignClient;
import com.zjedu.utils.Constants;
import com.zjedu.utils.RedisUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/29 16:09
 * @Version 1.0
 * @Description
 */

@Component
public class RankManager
{
    @Resource
    private RedisUtils redisUtils;

    @Resource
    private PassportFeignClient passportFeignClient;

    // 排行榜缓存时间 60s
    private static final long cacheRankSecond = 60;

    /**
     * 获取排行榜数据
     *
     * @param limit
     * @param currentPage
     * @param searchUser
     * @param type
     * @return
     * @throws StatusFailException
     */
    public IPage getRankList(Integer limit, Integer currentPage, String searchUser, Integer type) throws StatusFailException
    {

        // 页数，每页题数若为空，设置默认值
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 30;

        List<String> uidList = null;
        if (StringUtils.hasText(searchUser))
        {
            uidList = passportFeignClient.searchUserUidList(searchUser);
        }

        IPage rankList = null;
        // 根据type查询不同类型的排行榜
        if (type.intValue() == Constants.Contest.TYPE_ACM.getCode())
        {
            rankList = getACMRankList(limit, currentPage, uidList);
        } else if (type.intValue() == Constants.Contest.TYPE_OI.getCode())
        {
            rankList = getOIRankList(limit, currentPage, uidList);
        } else
        {
            throw new StatusFailException("排行榜类型代码不正确，请使用0(ACM),1(OI)！");
        }
        return rankList;
    }


    private IPage<ACMRankVO> getACMRankList(int limit, int currentPage, List<String> uidList)
    {

        IPage<ACMRankVO> data = null;
        if (uidList != null)
        {
            Page<ACMRankVO> page = new Page<>(currentPage, limit);
            if (uidList.size() > 0)
            {
                data = passportFeignClient.getACMRankList(page, uidList);
            } else
            {
                data = page;
            }
        } else
        {
            String key = Constants.Account.ACM_RANK_CACHE.getCode() + "_" + limit + "_" + currentPage;
            data = (IPage<ACMRankVO>) redisUtils.get(key);
            if (data == null)
            {
                Page<ACMRankVO> page = new Page<>(currentPage, limit);
                data = passportFeignClient.getACMRankList(page, null);
                redisUtils.set(key, data, cacheRankSecond);
            }
        }

        return data;
    }


    private IPage<OIRankVO> getOIRankList(int limit, int currentPage, List<String> uidList)
    {

        IPage<OIRankVO> data = null;
        if (uidList != null)
        {
            Page<OIRankVO> page = new Page<>(currentPage, limit);
            if (uidList.size() > 0)
            {
                data = passportFeignClient.getOIRankList(page, uidList);
            } else
            {
                data = page;
            }
        } else
        {
            String key = Constants.Account.OI_RANK_CACHE.getCode() + "_" + limit + "_" + currentPage;
            data = (IPage<OIRankVO>) redisUtils.get(key);
            if (data == null)
            {
                Page<OIRankVO> page = new Page<>(currentPage, limit);
                data = passportFeignClient.getOIRankList(page, null);
                redisUtils.set(key, data, cacheRankSecond);
            }
        }

        return data;
    }
}
