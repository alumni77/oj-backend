package com.zjedu.problem.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.pojo.entity.problem.ProblemTag;
import com.zjedu.problem.dao.ProblemTagEntityService;
import com.zjedu.problem.mapper.ProblemTagMapper;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/29 15:03
 * @Version 1.0
 * @Description
 */

@Service
public class ProblemTagEntityServiceImpl extends ServiceImpl<ProblemTagMapper, ProblemTag> implements ProblemTagEntityService
{
}