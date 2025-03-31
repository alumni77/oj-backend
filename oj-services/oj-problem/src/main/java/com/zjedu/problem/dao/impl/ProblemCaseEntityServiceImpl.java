package com.zjedu.problem.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.pojo.entity.problem.ProblemCase;
import com.zjedu.problem.dao.ProblemCaseEntityService;
import com.zjedu.problem.mapper.ProblemCaseMapper;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/31 11:11
 * @Version 1.0
 * @Description
 */

@Service
public class ProblemCaseEntityServiceImpl extends ServiceImpl<ProblemCaseMapper, ProblemCase> implements ProblemCaseEntityService
{
}
