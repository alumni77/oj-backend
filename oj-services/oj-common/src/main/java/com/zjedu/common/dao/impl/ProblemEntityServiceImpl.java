package com.zjedu.common.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.common.dao.ProblemEntityService;
import com.zjedu.common.mapper.ProblemMapper;
import com.zjedu.pojo.entity.problem.Problem;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/30 14:33
 * @Version 1.0
 * @Description
 */

@Service
public class ProblemEntityServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements ProblemEntityService
{

}