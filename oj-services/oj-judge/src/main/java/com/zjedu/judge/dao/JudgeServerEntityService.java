package com.zjedu.judge.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjedu.pojo.entity.judge.JudgeServer;

import java.util.HashMap;

/**
 * @Author Zhong
 * @Create 2025/3/24 21:47
 * @Version 1.0
 * @Description
 */

public interface JudgeServerEntityService extends IService<JudgeServer>
{
    HashMap<String, Object> getJudgeServerInfo();
}
