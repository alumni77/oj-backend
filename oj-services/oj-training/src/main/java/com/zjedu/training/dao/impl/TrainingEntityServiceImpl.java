package com.zjedu.training.dao.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjedu.pojo.entity.training.Training;
import com.zjedu.pojo.entity.training.TrainingProblem;
import com.zjedu.pojo.vo.TrainingVO;
import com.zjedu.training.dao.TrainingEntityService;
import com.zjedu.training.dao.TrainingProblemEntityService;
import com.zjedu.training.mapper.TrainingMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Zhong
 * @Create 2025/3/30 15:45
 * @Version 1.0
 * @Description
 */

@Service
public class TrainingEntityServiceImpl extends ServiceImpl<TrainingMapper, Training> implements TrainingEntityService
{

    @Resource
    private TrainingMapper trainingMapper;

    @Resource
    private TrainingProblemEntityService trainingProblemEntityService;

    @Override
    public Page<TrainingVO> getTrainingList(int limit,
                                            int currentPage,
                                            Long categoryId,
                                            String auth,
                                            String keyword,
                                            String currentUid)
    {

        //新建分页
        Page<TrainingVO> page = new Page<>(currentPage, limit);

        List<TrainingVO> trainingList = trainingMapper.getTrainingList(page, categoryId, auth, keyword);

        // 当前用户有登录，且训练列表不为空，则查询用户对于每个训练的做题进度
        if (StringUtils.hasText(currentUid) && trainingList.size() > 0)
        {
            List<Long> tidList = trainingList.stream().map(TrainingVO::getId).collect(Collectors.toList());
            List<TrainingProblem> trainingProblemList = trainingProblemEntityService.getTrainingListAcceptedCountByUid(tidList, currentUid);

            HashMap<Long, Integer> tidMapCount = new HashMap<>(trainingList.size());
            for (TrainingProblem trainingProblem : trainingProblemList)
            {
                int count = tidMapCount.getOrDefault(trainingProblem.getTid(), 0);
                count++;
                tidMapCount.put(trainingProblem.getTid(), count);
            }

            for (TrainingVO trainingVo : trainingList)
            {
                Integer count = tidMapCount.getOrDefault(trainingVo.getId(), 0);
                trainingVo.setAcCount(count);
            }
        }

        page.setRecords(trainingList);
        return page;
    }
}
