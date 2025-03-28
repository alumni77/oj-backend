package com.zjedu.problem.dao.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.vo.ProblemCountVO;
import com.zjedu.pojo.vo.ProblemVO;
import com.zjedu.problem.dao.ProblemEntityService;
import com.zjedu.problem.feign.JudgeFeignClient;
import com.zjedu.problem.mapper.ProblemMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Zhong
 * @Create 2025/3/28 20:48
 * @Version 1.0
 * @Description
 */

@Service
public class ProblemEntityServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements ProblemEntityService
{
    @Resource
    private JudgeFeignClient judgeFeignClient;

    @Resource
    private ProblemMapper problemMapper;

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
            List<ProblemCountVO> problemListCount = judgeFeignClient.getProblemListByPids(pidList);
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
