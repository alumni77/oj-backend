package com.zjedu.admin.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjedu.pojo.entity.training.TrainingRegister;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/4/2 21:46
 * @Version 1.0
 * @Description
 */

public interface TrainingRegisterEntityService extends IService<TrainingRegister>
{
    List<String> getAlreadyRegisterUidList(Long tid);
}
