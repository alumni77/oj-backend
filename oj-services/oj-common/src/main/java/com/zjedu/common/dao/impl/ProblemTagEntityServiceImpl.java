package com.zjedu.common.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.common.dao.ProblemTagEntityService;
import com.zjedu.common.mapper.ProblemTagMapper;
import com.zjedu.pojo.entity.problem.ProblemTag;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/30 14:28
 * @Version 1.0
 * @Description
 */

@Service
public class ProblemTagEntityServiceImpl extends ServiceImpl<ProblemTagMapper, ProblemTag> implements ProblemTagEntityService
{
}