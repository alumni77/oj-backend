package com.zjedu.passport.dao.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjedu.pojo.dto.RegisterDTO;
import com.zjedu.pojo.entity.user.UserInfo;

/**
 * @Author Zhong
 * @Create 2025/3/19 21:37
 * @Version 1.0
 * @Description 服务类
 */

public interface UserInfoEntityService extends IService<UserInfo>
{
    public Boolean addUser(RegisterDTO registerDto);
}
