package com.zjedu.training.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.pojo.entity.training.TrainingRegister;
import com.zjedu.training.dao.TrainingRegisterEntityService;
import com.zjedu.training.mapper.TrainingRegisterMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Zhong
 * @Create 2025/3/30 16:18
 * @Version 1.0
 * @Description
 */

@Service
public class TrainingRegisterEntityServiceImpl extends ServiceImpl<TrainingRegisterMapper, TrainingRegister> implements TrainingRegisterEntityService
{

    @Resource
    private TrainingRegisterMapper trainingRegisterMapper;

    @Override
    public List<String> getAlreadyRegisterUidList(Long tid)
    {
        QueryWrapper<TrainingRegister> trainingRegisterQueryWrapper = new QueryWrapper<>();
        trainingRegisterQueryWrapper.eq("tid", tid);
        return trainingRegisterMapper.selectList(trainingRegisterQueryWrapper).stream().map(TrainingRegister::getUid).collect(Collectors.toList());
    }

}