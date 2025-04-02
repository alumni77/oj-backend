package com.zjedu.admin.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.admin.dao.ProblemCaseEntityService;
import com.zjedu.admin.mapper.ProblemCaseMapper;
import com.zjedu.pojo.entity.problem.ProblemCase;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/2 18:57
 * @Version 1.0
 * @Description
 */

@Service
public class ProblemCaseEntityServiceImpl extends ServiceImpl<ProblemCaseMapper, ProblemCase> implements ProblemCaseEntityService
{
}
