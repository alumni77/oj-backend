package com.zjedu.judge.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.judge.dao.JudgeEntityService;
import com.zjedu.judge.mapper.JudgeMapper;
import com.zjedu.judge.mapper.ProblemMapper;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.vo.JudgeVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Zhong
 * @Create 2025/3/25 14:06
 * @Version 1.0
 * @Description 服务实现类
 */

@Service
public class JudgeEntityServiceImpl extends ServiceImpl<JudgeMapper, Judge> implements JudgeEntityService
{
    @Resource
    private JudgeMapper judgeMapper;

    @Resource
    private ProblemMapper problemMapper;


    @Override
    public IPage<JudgeVO> getCommonJudgeList(Integer limit, Integer currentPage, String searchPid, Integer status, String username, String uid, Boolean completeProblemID, Long gid)
    {
        //新建分页
        Page<JudgeVO> page = new Page<>(currentPage, limit);

        IPage<JudgeVO> commonJudgeList = judgeMapper.getCommonJudgeList(page, searchPid, status, username, uid, completeProblemID, gid);
        List<JudgeVO> records = commonJudgeList.getRecords();
        if (!CollectionUtils.isEmpty(records))
        {
            List<Long> pidList = records.stream().map(JudgeVO::getPid).collect(Collectors.toList());
            QueryWrapper<Problem> problemQueryWrapper = new QueryWrapper<>();
            problemQueryWrapper.select("id", "title")
                    .in("id", pidList);
            List<Problem> problemList = problemMapper.selectList(problemQueryWrapper);
            HashMap<Long, String> storeMap = new HashMap<>(limit);
            for (JudgeVO judgeVo : records)
            {
                judgeVo.setTitle(getProblemTitleByPid(judgeVo.getPid(), problemList, storeMap));
            }
        }
        return commonJudgeList;
    }

    private String getProblemTitleByPid(Long pid, List<Problem> problemList, HashMap<Long, String> storeMap)
    {
        String title = storeMap.get(pid);
        if (title != null)
        {
            return title;
        }
        for (Problem problem : problemList)
        {
            if (problem.getId().equals(pid))
            {
                storeMap.put(pid, problem.getTitle());
                return problem.getTitle();
            }
        }
        return "";
    }
}
