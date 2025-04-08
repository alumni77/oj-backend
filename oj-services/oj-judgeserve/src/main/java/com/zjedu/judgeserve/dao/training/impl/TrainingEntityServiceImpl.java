package com.zjedu.judgeserve.dao.training.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.judgeserve.dao.training.TrainingEntityService;
import com.zjedu.judgeserve.mapper.TrainingMapper;
import com.zjedu.pojo.entity.training.Training;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/8 21:22
 * @Version 1.0
 * @Description
 */

@Service
public class TrainingEntityServiceImpl extends ServiceImpl<TrainingMapper, Training> implements TrainingEntityService
{
}
