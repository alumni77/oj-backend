package com.zjedu.account.dao.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjedu.pojo.entity.user.Session;

/**
 * @Author Zhong
 * @Create 2025/3/21 14:54
 * @Version 1.0
 * @Description
 */

public interface SessionEntityService extends IService<Session>
{

    void checkRemoteLogin(String uid);

}

