package com.zjedu.passport.dao.user.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.passport.dao.user.UserRecordEntityService;
import com.zjedu.passport.mapper.UserRecordMapper;
import com.zjedu.pojo.entity.user.UserRecord;
import com.zjedu.pojo.vo.ACMRankVO;
import com.zjedu.pojo.vo.OIRankVO;
import com.zjedu.pojo.vo.UserHomeVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/20 11:59
 * @Version 1.0
 * @Description 服务实现类
 */

@Service
public class UserRecordEntityServiceImpl extends ServiceImpl<UserRecordMapper, UserRecord> implements UserRecordEntityService
{
    @Resource
    private UserRecordMapper userRecordMapper;


    @Override
    public UserHomeVO getUserHomeInfo(String uid, String username)
    {
        return userRecordMapper.getUserHomeInfo(uid, username);
    }

    @Override
    public List<ACMRankVO> getRecent7ACRank()
    {
        return userRecordMapper.getRecent7ACRank();

    }

    @Override
    public IPage<OIRankVO> getOIRankList(Page<OIRankVO> page, List<String> uidList)
    {
        return userRecordMapper.getOIRankList(page, uidList);
    }

    @Override
    public IPage<ACMRankVO> getACMRankList(Page<ACMRankVO> page, List<String> uidList)
    {
        return userRecordMapper.getACMRankList(page, uidList);
    }
}
