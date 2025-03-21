package com.zjedu.account.dao.judge.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.account.dao.judge.JudgeEntityService;
import com.zjedu.account.mapper.JudgeMapper;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.vo.ProblemCountVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/21 14:44
 * @Version 1.0
 * @Description 服务实现类
 */

@Service
public class JudgeEntityServiceImpl extends ServiceImpl<JudgeMapper, Judge> implements JudgeEntityService
{

    @Resource
    private JudgeMapper judgeMapper;

    @Override
    public List<ProblemCountVO> getProblemListCount(List<Long> pidList) {
        return judgeMapper.getProblemListCount(pidList);
    }
}
