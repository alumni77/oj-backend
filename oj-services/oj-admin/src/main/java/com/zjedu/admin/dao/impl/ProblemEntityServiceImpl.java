package com.zjedu.admin.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.admin.dao.ProblemEntityService;
import com.zjedu.admin.mapper.ProblemMapper;
import com.zjedu.pojo.entity.problem.Problem;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/1 21:48
 * @Version 1.0
 * @Description
 */

@Service
public class ProblemEntityServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements ProblemEntityService
{
}
