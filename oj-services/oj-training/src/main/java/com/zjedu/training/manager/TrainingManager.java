package com.zjedu.training.manager;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.common.exception.StatusAccessDeniedException;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.pojo.entity.training.Training;
import com.zjedu.pojo.entity.training.TrainingCategory;
import com.zjedu.pojo.entity.training.TrainingProblem;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.ProblemVO;
import com.zjedu.pojo.vo.TrainingRankVO;
import com.zjedu.pojo.vo.TrainingRecordVO;
import com.zjedu.pojo.vo.TrainingVO;
import com.zjedu.training.dao.TrainingCategoryEntityService;
import com.zjedu.training.dao.TrainingEntityService;
import com.zjedu.training.dao.TrainingProblemEntityService;
import com.zjedu.training.dao.TrainingRecordEntityService;
import com.zjedu.training.feign.PassportFeignClient;
import com.zjedu.training.validator.TrainingValidator;
import com.zjedu.utils.Constants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Zhong
 * @Create 2025/3/30 15:37
 * @Version 1.0
 * @Description
 */

@Component
public class TrainingManager
{
    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private HttpServletRequest request;

    @Resource
    private TrainingEntityService trainingEntityService;

    @Resource
    private TrainingCategoryEntityService trainingCategoryEntityService;

    @Resource
    private TrainingProblemEntityService trainingProblemEntityService;

    @Resource
    private TrainingValidator trainingValidator;

    @Resource
    private TrainingRecordEntityService trainingRecordEntityService;


    /**
     * 获取训练题单列表，可根据关键词、类别、权限、类型过滤
     *
     * @param limit
     * @param currentPage
     * @param keyword
     * @param categoryId
     * @param auth
     * @return
     */
    public IPage<TrainingVO> getTrainingList(Integer limit,
                                             Integer currentPage,
                                             String keyword,
                                             Long categoryId,
                                             String auth)
    {

        // 页数，每页题数若为空，设置默认值
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 20;

        // 获取当前登录的用户
        //从请求头获取用户ID
        String userId = request.getHeader("X-User-Id");
        UserInfo userRolesVo = passportFeignClient.getByUid(userId);

        String currentUid = null;
        if (userRolesVo != null)
        {
            currentUid = userRolesVo.getUuid();
        }

        return trainingEntityService.getTrainingList(limit, currentPage, categoryId, auth, keyword, currentUid);
    }

    /**
     * 根据tid获取指定训练详情
     *
     * @param tid
     * @return
     */
    public TrainingVO getTraining(Long tid) throws StatusFailException, StatusAccessDeniedException, StatusForbiddenException
    {
        // 获取当前登录的用户
        //从请求头获取用户ID
        String userId = request.getHeader("X-User-Id");
        UserInfo userRolesVo = passportFeignClient.getByUid(userId);

        Training training = trainingEntityService.getById(tid);
        if (training == null || !training.getStatus())
        {
            throw new StatusFailException("该训练不存在或不允许显示！");
        }

        //TODO:由于训练的数据库表中没有数据，以下代码还不知是否调通
        TrainingVO trainingVo = BeanUtil.copyProperties(training, TrainingVO.class);
        TrainingCategory trainingCategory = trainingCategoryEntityService.getTrainingCategoryByTrainingId(training.getId());
        trainingVo.setCategoryName(trainingCategory.getName());
        trainingVo.setCategoryColor(trainingCategory.getColor());
        List<Long> trainingProblemIdList = trainingProblemEntityService.getTrainingProblemIdList(training.getId());
        trainingVo.setProblemCount(trainingProblemIdList.size());

        if (userRolesVo != null && trainingValidator.isInTrainingOrAdmin(training, userRolesVo))
        {
            Integer count = trainingProblemEntityService.getUserTrainingACProblemCount(userRolesVo.getUuid(), trainingProblemIdList);
            trainingVo.setAcCount(count);
        } else
        {
            trainingVo.setAcCount(0);
        }

        return trainingVo;
    }

    /**
     * 根据tid获取指定训练的题单题目列表
     *
     * @param tid
     * @return
     */
    public List<ProblemVO> getTrainingProblemList(Long tid) throws StatusAccessDeniedException,
            StatusForbiddenException, StatusFailException
    {
        Training training = trainingEntityService.getById(tid);
        if (training == null || !training.getStatus())
        {
            throw new StatusFailException("该训练不存在或不允许显示！");
        }

        //TODO:由于训练的数据库表中没有数据，以下代码还不知是否调通
        trainingValidator.validateTrainingAuth(training);
        return trainingProblemEntityService.getTrainingProblemList(tid);
    }

    /**
     * 获取训练的排行榜分页
     *
     * @param tid
     * @param limit
     * @param currentPage
     * @param keyword     搜索关键词 可过滤用户名、真实姓名、学校
     * @return
     * @throws StatusAccessDeniedException
     * @throws StatusForbiddenException
     * @throws StatusFailException
     */
    public IPage<TrainingRankVO> getTrainingRank(Long tid, Integer limit, Integer currentPage, String keyword) throws
            StatusAccessDeniedException, StatusForbiddenException, StatusFailException
    {
        Training training = trainingEntityService.getById(tid);
        if (training == null || !training.getStatus())
        {
            throw new StatusFailException("该训练不存在或不允许显示！");
        }

        //TODO:由于训练的数据库表中没有数据，以下代码还不知是否调通
        trainingValidator.validateTrainingAuth(training);

        // 页数，每页数若为空，设置默认值
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 30;

        if (StrUtil.isNotBlank(keyword))
        {
            keyword = keyword.toLowerCase();
        }
        return getTrainingRank(tid, training.getAuthor(),
                currentPage,
                limit,
                keyword);
    }

    private IPage<TrainingRankVO> getTrainingRank(Long tid, String username, int currentPage, int limit, String keyword)
    {

        Map<Long, String> tpIdMapDisplayId = getTPIdMapDisplayId(tid);
        List<TrainingRecordVO> trainingRecordVOList = trainingRecordEntityService.getTrainingRecord(tid);

        List<String> superAdminUidList = passportFeignClient.getSuperAdminUidList();

        List<TrainingRankVO> result = new ArrayList<>();

        HashMap<String, Integer> uidMapIndex = new HashMap<>();
        int pos = 0;
        for (TrainingRecordVO trainingRecordVo : trainingRecordVOList)
        {
            // 超级管理员和训练创建者的提交不入排行榜
            if (username.equals(trainingRecordVo.getUsername())
                    || superAdminUidList.contains(trainingRecordVo.getUid()))
            {
                continue;
            }

            // 如果有搜索关键词则 需要符合模糊匹配 用户名、真实姓名、学校的用户可进行榜单记录
            if (StrUtil.isNotBlank(keyword))
            {
                boolean isMatchKeyword = matchKeywordIgnoreCase(keyword, trainingRecordVo.getUsername())
                        || matchKeywordIgnoreCase(keyword, trainingRecordVo.getRealname())
                        || matchKeywordIgnoreCase(keyword, trainingRecordVo.getSchool());
                if (!isMatchKeyword)
                {
                    continue;
                }
            }

            TrainingRankVO trainingRankVo;
            Integer index = uidMapIndex.get(trainingRecordVo.getUid());
            if (index == null)
            {
                trainingRankVo = new TrainingRankVO();
                trainingRankVo.setRealname(trainingRecordVo.getRealname())
                        .setAvatar(trainingRecordVo.getAvatar())
                        .setSchool(trainingRecordVo.getSchool())
                        .setGender(trainingRecordVo.getGender())
                        .setUid(trainingRecordVo.getUid())
                        .setUsername(trainingRecordVo.getUsername())
                        .setNickname(trainingRecordVo.getNickname())
                        .setAc(0)
                        .setTotalRunTime(0);
                HashMap<String, HashMap<String, Object>> submissionInfo = new HashMap<>();
                trainingRankVo.setSubmissionInfo(submissionInfo);

                result.add(trainingRankVo);
                uidMapIndex.put(trainingRecordVo.getUid(), pos);
                pos++;
            } else
            {
                trainingRankVo = result.get(index);
            }
            String displayId = tpIdMapDisplayId.get(trainingRecordVo.getTpid());
            HashMap<String, Object> problemSubmissionInfo = trainingRankVo
                    .getSubmissionInfo()
                    .getOrDefault(displayId, new HashMap<>());

            // 如果该题目已经AC过了，只比较运行时间取最小
            if ((Boolean) problemSubmissionInfo.getOrDefault("isAC", false))
            {
                if (trainingRecordVo.getStatus().intValue() == Constants.Judge.STATUS_ACCEPTED.getStatus())
                {
                    int runTime = (int) problemSubmissionInfo.getOrDefault("runTime", 0);
                    if (runTime > trainingRecordVo.getUseTime())
                    {
                        trainingRankVo.setTotalRunTime(trainingRankVo.getTotalRunTime() - runTime + trainingRecordVo.getUseTime());
                        problemSubmissionInfo.put("runTime", trainingRecordVo.getUseTime());
                    }
                }
                continue;
            }

            problemSubmissionInfo.put("status", trainingRecordVo.getStatus());
            problemSubmissionInfo.put("score", trainingRecordVo.getScore());

            // 通过的话
            if (trainingRecordVo.getStatus().intValue() == Constants.Judge.STATUS_ACCEPTED.getStatus())
            {
                // 总解决题目次数ac+1
                trainingRankVo.setAc(trainingRankVo.getAc() + 1);
                problemSubmissionInfo.put("isAC", true);
                problemSubmissionInfo.put("runTime", trainingRecordVo.getUseTime());
                trainingRankVo.setTotalRunTime(trainingRankVo.getTotalRunTime() + trainingRecordVo.getUseTime());
            }

            trainingRankVo.getSubmissionInfo().put(displayId, problemSubmissionInfo);
        }

        List<TrainingRankVO> orderResultList = result.stream().sorted(Comparator.comparing(TrainingRankVO::getAc, Comparator.reverseOrder()) // 先以总ac数降序
                .thenComparing(TrainingRankVO::getTotalRunTime) //再以总耗时升序
        ).toList();

        // 计算好排行榜，然后进行分页
        Page<TrainingRankVO> page = new Page<>(currentPage, limit);
        int count = orderResultList.size();
        List<TrainingRankVO> pageList = new ArrayList<>();
        //计算当前页第一条数据的下标
        int currId = currentPage > 1 ? (currentPage - 1) * limit : 0;
        for (int i = 0; i < limit && i < count - currId; i++)
        {
            pageList.add(orderResultList.get(currId + i));
        }
        page.setSize(limit);
        page.setCurrent(currentPage);
        page.setTotal(count);
        page.setRecords(pageList);
        return page;
    }

    private boolean matchKeywordIgnoreCase(String keyword, String content)
    {
        return content != null && content.toLowerCase().contains(keyword);
    }

    private Map<Long, String> getTPIdMapDisplayId(Long tid)
    {
        QueryWrapper<TrainingProblem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tid", tid);
        List<TrainingProblem> trainingProblemList = trainingProblemEntityService.list(queryWrapper);
        return trainingProblemList.stream().collect(Collectors.toMap(TrainingProblem::getId, TrainingProblem::getDisplayId));
    }


}
