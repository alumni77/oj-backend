package com.zjedu.account.dao.problem.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.account.dao.judge.JudgeEntityService;
import com.zjedu.account.dao.problem.ProblemEntityService;
import com.zjedu.account.mapper.ProblemMapper;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.vo.ProblemCountVO;
import com.zjedu.pojo.vo.ProblemVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Zhong
 * @Create 2025/3/21 14:34
 * @Version 1.0
 * @Description
 */

@Service
public class ProblemEntityServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements ProblemEntityService
{

    @Resource
    private ProblemMapper problemMapper;

    @Resource
    private JudgeEntityService judgeEntityService;

    @Override
    public Page<ProblemVO> getProblemList(int limit, int currentPage, Long pid, String title, Integer difficulty,
                                          List<Long> tid, String oj)
    {
        //新建分页
        Page<ProblemVO> page = new Page<>(currentPage, limit);
        Integer tagListSize = null;
        if (tid != null)
        {
            tid = tid.stream().distinct().collect(Collectors.toList());
            tagListSize = tid.size();
        }

        List<ProblemVO> problemList = problemMapper.getProblemList(page, pid, title, difficulty, tid, tagListSize, oj);

        if (!problemList.isEmpty())
        {
            List<Long> pidList = problemList.stream().map(ProblemVO::getPid).collect(Collectors.toList());
            List<ProblemCountVO> problemListCount = judgeEntityService.getProblemListCount(pidList);
            for (ProblemVO problemVo : problemList)
            {
                for (ProblemCountVO problemCountVo : problemListCount)
                {
                    if (problemVo.getPid().equals(problemCountVo.getPid()))
                    {
                        problemVo.setProblemCountVo(problemCountVo);
                        break;
                    }
                }
            }
        }
        return page.setRecords(problemList);
    }
}
