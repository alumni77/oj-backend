package com.zjedu.training.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.entity.training.TrainingProblem;
import com.zjedu.pojo.vo.ProblemFullScreenListVO;
import com.zjedu.pojo.vo.ProblemVO;
import com.zjedu.training.dao.JudgeEntityService;
import com.zjedu.training.dao.TrainingProblemEntityService;
import com.zjedu.training.mapper.TrainingProblemMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Author Zhong
 * @Create 2025/3/30 15:52
 * @Version 1.0
 * @Description
 */

@Service
public class TrainingProblemEntityServiceImpl extends ServiceImpl<TrainingProblemMapper, TrainingProblem> implements TrainingProblemEntityService
{
    @Resource
    private TrainingProblemMapper trainingProblemMapper;

    @Resource
    private JudgeEntityService judgeEntityService;

    @Override
    public List<TrainingProblem> getTrainingListAcceptedCountByUid(List<Long> tidList, String uid)
    {
        return trainingProblemMapper.getTrainingListAcceptedCountByUid(tidList, uid);
    }

    @Override
    public List<Long> getTrainingProblemIdList(Long tid)
    {
        return trainingProblemMapper.getTrainingProblemCount(tid);
    }

    @Override
    public Integer getUserTrainingACProblemCount(String uid, List<Long> pidList)
    {
        if (CollectionUtils.isEmpty(pidList))
        {
            return 0;
        }
        QueryWrapper<Judge> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT pid")
                .eq("uid", uid)
                .eq("status", 0)
                .in("pid", pidList);
        return Math.toIntExact(judgeEntityService.count(queryWrapper));

    }

    @Override
    public List<ProblemVO> getTrainingProblemList(Long tid)
    {
        List<ProblemVO> trainingProblemList = trainingProblemMapper.getTrainingProblemList(tid);
        return trainingProblemList.stream().filter(distinctByKey(ProblemVO::getPid)).collect(Collectors.toList());
    }

    @Override
    public List<ProblemFullScreenListVO> getTrainingFullScreenProblemList(Long tid)
    {
        return trainingProblemMapper.getTrainingFullScreenProblemList(tid);
    }

    static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor)
    {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
