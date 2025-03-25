package com.zjedu.judge.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.judge.dao.ProblemCaseEntityService;
import com.zjedu.judge.mapper.ProblemCaseMapper;
import com.zjedu.pojo.entity.problem.ProblemCase;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/25 19:06
 * @Version 1.0
 * @Description
 */

@Service
public class ProblemCaseEntityServiceImpl extends ServiceImpl<ProblemCaseMapper, ProblemCase> implements ProblemCaseEntityService
{
}
