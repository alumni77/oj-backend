package com.zjedu.file.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.file.dao.ProblemEntityService;
import com.zjedu.file.mapper.ProblemMapper;
import com.zjedu.pojo.entity.problem.Problem;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/4/8 19:51
 * @Version 1.0
 * @Description
 */

@Service
public class ProblemEntityServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements ProblemEntityService
{
}
