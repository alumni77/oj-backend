package com.zjedu.judgeserve.dao.training.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.judgeserve.dao.training.TrainingRegisterEntityService;
import com.zjedu.judgeserve.mapper.TrainingRegisterMapper;
import com.zjedu.pojo.entity.training.TrainingRegister;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/8 21:26
 * @Version 1.0
 * @Description
 */

@Service
public class TrainingRegisterEntityServiceImpl extends ServiceImpl<TrainingRegisterMapper, TrainingRegister> implements TrainingRegisterEntityService
{
}
