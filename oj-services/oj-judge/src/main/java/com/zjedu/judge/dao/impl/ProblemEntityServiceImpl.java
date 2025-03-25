package com.zjedu.judge.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.judge.dao.ProblemEntityService;
import com.zjedu.judge.mapper.ProblemMapper;
import com.zjedu.pojo.entity.problem.Problem;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/25 18:58
 * @Version 1.0
 * @Description 服务实现类
 */

@Service
public class ProblemEntityServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements ProblemEntityService
{

}
