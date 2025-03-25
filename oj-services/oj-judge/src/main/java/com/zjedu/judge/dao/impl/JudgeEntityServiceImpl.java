package com.zjedu.judge.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.judge.dao.JudgeEntityService;
import com.zjedu.judge.mapper.JudgeMapper;
import com.zjedu.pojo.entity.judge.Judge;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/25 14:06
 * @Version 1.0
 * @Description 服务实现类
 */

@Service
public class JudgeEntityServiceImpl extends ServiceImpl<JudgeMapper, Judge> implements JudgeEntityService
{

}
