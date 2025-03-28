package com.zjedu.judgeserve.dao.judge.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.judgeserve.dao.judge.JudgeCaseEntityService;
import com.zjedu.judgeserve.mapper.JudgeCaseMapper;
import com.zjedu.pojo.entity.judge.JudgeCase;
import org.springframework.stereotype.Service;

/**
 * @Author Zhong
 * @Create 2025/3/28 16:57
 * @Version 1.0
 * @Description 服务实现类
 */

@Service
public class JudgeCaseEntityServiceImpl extends ServiceImpl<JudgeCaseMapper, JudgeCase> implements JudgeCaseEntityService
{

}
