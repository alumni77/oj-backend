package com.zjedu.admin.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjedu.admin.dao.*;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.entity.training.Training;
import com.zjedu.pojo.entity.training.TrainingProblem;
import com.zjedu.pojo.entity.training.TrainingRecord;
import com.zjedu.utils.Constants;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/4/2 21:57
 * @Version 1.0
 * @Description
 */

@Component
public class AdminTrainingRecordManager
{
    @Resource
    private TrainingRecordEntityService trainingRecordEntityService;

    @Resource
    private TrainingProblemEntityService trainingProblemEntityService;

    @Resource
    private JudgeEntityService judgeEntityService;

    @Resource
    private TrainingRegisterEntityService trainingRegisterEntityService;

    @Resource
    private TrainingEntityService trainingEntityService;

    @Async
    public void checkSyncRecord(Training training)
    {
        if (!Constants.Training.AUTH_PRIVATE.getValue().equals(training.getAuth()))
        {
            return;
        }
        QueryWrapper<TrainingRecord> trainingRecordQueryWrapper = new QueryWrapper<>();
        trainingRecordQueryWrapper.eq("tid", training.getId());
        int count = (int) trainingRecordEntityService.count(trainingRecordQueryWrapper);
        if (count <= 0)
        {
            syncAllUserProblemRecord(training.getId());
        }
    }

    private void syncAllUserProblemRecord(Long tid)
    {
        QueryWrapper<TrainingProblem> trainingProblemQueryWrapper = new QueryWrapper<>();
        trainingProblemQueryWrapper.eq("tid", tid);
        List<TrainingProblem> trainingProblemList = trainingProblemEntityService.list(trainingProblemQueryWrapper);
        if (trainingProblemList.isEmpty())
        {
            return;
        }
        List<Long> pidList = new ArrayList<>();
        HashMap<Long, Long> pidMapTPid = new HashMap<>();
        for (TrainingProblem trainingProblem : trainingProblemList)
        {
            pidList.add(trainingProblem.getPid());
            pidMapTPid.put(trainingProblem.getPid(), trainingProblem.getId());
        }

        List<String> uidList = trainingRegisterEntityService.getAlreadyRegisterUidList(tid);
        if (uidList.isEmpty())
        {
            return;
        }
        QueryWrapper<Judge> judgeQueryWrapper = new QueryWrapper<>();
        judgeQueryWrapper.in("pid", pidList)
                .eq("cid", 0)
                .eq("status", Constants.Judge.STATUS_ACCEPTED.getStatus()) // 只同步ac的提交
                .in("uid", uidList);
        List<Judge> judgeList = judgeEntityService.list(judgeQueryWrapper);
        saveBatchNewRecordByJudgeList(judgeList, tid, null, pidMapTPid);
    }

    private void saveBatchNewRecordByJudgeList(List<Judge> judgeList, Long tid, Long tpId, HashMap<Long, Long> pidMapTPid)
    {
        if (!CollectionUtils.isEmpty(judgeList))
        {
            List<TrainingRecord> trainingRecordList = new ArrayList<>();
            for (Judge judge : judgeList)
            {
                TrainingRecord trainingRecord = new TrainingRecord()
                        .setPid(judge.getPid())
                        .setSubmitId(judge.getSubmitId())
                        .setTid(tid)
                        .setUid(judge.getUid());
                if (pidMapTPid != null)
                {
                    trainingRecord.setTpid(pidMapTPid.get(judge.getPid()));
                }
                if (tpId != null)
                {
                    trainingRecord.setTpid(tpId);
                }
                trainingRecordList.add(trainingRecord);
            }
            trainingRecordEntityService.saveBatch(trainingRecordList);
        }
    }

    @Async
    public void syncAlreadyRegisterUserRecord(Long tid, Long pid, Long tpId)
    {
        Training training = trainingEntityService.getById(tid);
        if (!Constants.Training.AUTH_PRIVATE.getValue().equals(training.getAuth()))
        {
            return;
        }
        List<String> uidList = trainingRegisterEntityService.getAlreadyRegisterUidList(tid);
        syncNewProblemUserSubmissionToRecord(pid, tpId, tid, uidList);
    }

    private void syncNewProblemUserSubmissionToRecord(Long pid, Long tpId, Long tid, List<String> uidList)
    {
        if (!CollectionUtils.isEmpty(uidList))
        {
            QueryWrapper<Judge> judgeQueryWrapper = new QueryWrapper<>();
            judgeQueryWrapper.eq("pid", pid)
                    .eq("cid", 0)
                    .eq("status", Constants.Judge.STATUS_ACCEPTED.getStatus()) // 只同步ac的提交
                    .in("uid", uidList);
            List<Judge> judgeList = judgeEntityService.list(judgeQueryWrapper);
            saveBatchNewRecordByJudgeList(judgeList, tid, tpId, null);
        }
    }


}
