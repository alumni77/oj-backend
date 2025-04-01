package com.zjedu.admin.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.admin.dao.JudgeEntityService;
import com.zjedu.admin.mapper.JudgeMapper;
import com.zjedu.pojo.entity.judge.Judge;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/1 21:10
 * @Version 1.0
 * @Description
 */

@Service
public class JudgeEntityServiceImpl extends ServiceImpl<JudgeMapper, Judge> implements JudgeEntityService
{
}
