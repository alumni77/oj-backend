package com.zjedu.judgeserve.dao.problem.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.judgeserve.dao.problem.ProblemEntityService;
import com.zjedu.judgeserve.mapper.ProblemMapper;
import com.zjedu.pojo.entity.problem.Problem;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/8 20:41
 * @Version 1.0
 * @Description
 */

@Service
public class ProblemEntityServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements ProblemEntityService
{
}
