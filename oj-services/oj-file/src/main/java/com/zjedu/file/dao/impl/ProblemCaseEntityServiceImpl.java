package com.zjedu.file.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.file.dao.ProblemCaseEntityService;
import com.zjedu.file.mapper.ProblemCaseMapper;
import com.zjedu.pojo.entity.problem.ProblemCase;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/31 13:39
 * @Version 1.0
 * @Description
 */

@Service
public class ProblemCaseEntityServiceImpl extends ServiceImpl<ProblemCaseMapper, ProblemCase> implements ProblemCaseEntityService
{
}
