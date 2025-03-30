package com.zjedu.passport.dao.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.passport.dao.user.UserInfoEntityService;
import com.zjedu.passport.mapper.UserInfoMapper;
import com.zjedu.pojo.dto.RegisterDTO;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.utils.Constants;
import com.zjedu.utils.RedisUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/19 22:16
 * @Version 1.0
 * @Description
 */
@Service
public class UserInfoEntityServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoEntityService
{
    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private RedisUtils redisUtils;


    @Override
    public Boolean addUser(RegisterDTO registerDto)
    {
        return userInfoMapper.addUser(registerDto) == 1;
    }

    @Override
    public List<String> getSuperAdminUidList()
    {

        String cacheKey = Constants.Account.SUPER_ADMIN_UID_LIST_CACHE.getCode();
        List<String> superAdminUidList = (List<String>) redisUtils.get(cacheKey);
        if (superAdminUidList == null)
        {
            superAdminUidList = userInfoMapper.getSuperAdminUidList();
            redisUtils.set(cacheKey, superAdminUidList, 12 * 3600);
        }
        return superAdminUidList;
    }
}
