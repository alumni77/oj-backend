package com.zjedu.account.manager;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjedu.account.dao.commom.FileEntityService;
import com.zjedu.account.dao.judge.JudgeEntityService;
import com.zjedu.account.dao.problem.ProblemEntityService;
import com.zjedu.account.feign.PassportFeignClient;
import com.zjedu.pojo.entity.common.File;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.entity.user.Role;
import com.zjedu.pojo.vo.ACMRankVO;
import com.zjedu.pojo.vo.RecentUpdatedProblemVO;
import com.zjedu.pojo.vo.SubmissionStatisticsVO;
import com.zjedu.utils.Constants;
import com.zjedu.utils.RedisUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Zhong
 * @Create 2025/3/23 21:18
 * @Version 1.0
 * @Description
 */

@Component
public class HomeManager
{
    @Resource
    private FileEntityService fileEntityService;

    @Resource
    private ProblemEntityService problemEntityService;

    @Resource
    private JudgeEntityService judgeEntityService;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private HttpServletRequest request;

    @Resource
    private PassportFeignClient passportFeignClient;

    private final static String SUBMISSION_STATISTICS_KEY = "home_submission_statistics";


    public List<HashMap<String, Object>> getHomeCarousel()
    {
        List<File> fileList = fileEntityService.queryCarouselFileList();
        return fileList.stream().map(f ->
        {
            HashMap<String, Object> param = new HashMap<>(2);
            param.put("id", f.getId());
            param.put("url", Constants.File.IMG_API.getPath() + f.getName());
            return param;
        }).collect(Collectors.toList());
    }

    public List<ACMRankVO> getRecentSevenACRank()
    {
        return passportFeignClient.getRecent7ACRank();
    }

    /**
     * 获取最近前十更新的题目（不包括比赛题目、私有题目）
     *
     * @return
     */
    public List<RecentUpdatedProblemVO> getRecentUpdatedProblemList()
    {
        QueryWrapper<Problem> problemQueryWrapper = new QueryWrapper<>();
        problemQueryWrapper.select("id", "problem_id", "title", "type", "gmt_modified", "gmt_create");
        problemQueryWrapper.eq("auth", 1);
        problemQueryWrapper.orderByDesc("gmt_create");
        problemQueryWrapper.last("limit 10");
        List<Problem> problemList = problemEntityService.list(problemQueryWrapper);
        if (!CollectionUtils.isEmpty(problemList))
        {
            return problemList.stream()
                    .map(this::convertUpdatedProblemVO)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private RecentUpdatedProblemVO convertUpdatedProblemVO(Problem problem)
    {
        return RecentUpdatedProblemVO.builder()
                .problemId(problem.getProblemId())
                .id(problem.getId())
                .title(problem.getTitle())
                .gmtCreate(problem.getGmtCreate())
                .gmtModified(problem.getGmtModified())
                .type(problem.getType())
                .build();
    }

    /**
     * 获取网站最近一周的提交状态（ac总量、提交总量）
     *
     * @param forceRefresh
     * @return
     */
    public SubmissionStatisticsVO getLastWeekSubmissionStatistics(Boolean forceRefresh)
    {
        SubmissionStatisticsVO submissionStatisticsVO = (SubmissionStatisticsVO) redisUtils.get(SUBMISSION_STATISTICS_KEY);

        // 从请求头获取当前用户ID
        String userId = request.getHeader("X-User-Id");
        boolean isRoot = false;
        if (StringUtils.hasText(userId))
        {
            // 使用 PassportFeignClient 获取用户角色列表
            List<Role> roles = passportFeignClient.getRolesByUid(userId);
            isRoot = roles.stream().anyMatch(role -> "root".equals(role.getRole()));
        }

        forceRefresh = forceRefresh && isRoot;

        if (submissionStatisticsVO == null || forceRefresh)
        {
            DateTime dateTime = DateUtil.offsetDay(new Date(), -6);
            String strTime = DateFormatUtils.format(dateTime, "yyyy-MM-dd") + " 00:00:00";
            QueryWrapper<Judge> judgeQueryWrapper = new QueryWrapper<>();
            judgeQueryWrapper.select("submit_id", "status", "gmt_create");
            judgeQueryWrapper.apply("UNIX_TIMESTAMP(gmt_create) >= UNIX_TIMESTAMP('" + strTime + "')");
            List<Judge> judgeList = judgeEntityService.list(judgeQueryWrapper);
            submissionStatisticsVO = buildSubmissionStatisticsVo(judgeList);
            redisUtils.set(SUBMISSION_STATISTICS_KEY, submissionStatisticsVO, 60 * 30);
        }
        return submissionStatisticsVO;
    }

    private SubmissionStatisticsVO buildSubmissionStatisticsVo(List<Judge> judgeList)
    {
        long acTodayCount = 0;
        long acOneDayAgoCount = 0;
        long acTwoDaysAgoCount = 0;
        long acThreeDaysAgoCount = 0;
        long acFourDaysAgoCount = 0;
        long acFiveDaysAgoCount = 0;
        long acSixDaysAgoCount = 0;

        long totalTodayCount = 0;
        long totalOneDayAgoCount = 0;
        long totalTwoDaysAgoCount = 0;
        long totalThreeDaysAgoCount = 0;
        long totalFourDaysAgoCount = 0;
        long totalFiveDaysAgoCount = 0;
        long totalSixDaysAgoCount = 0;


        Date date = new Date();
        String todayStr = DateUtil.format(date, "MM-dd");
        String oneDayAgoStr = DateFormatUtils.format(DateUtil.offsetDay(date, -1), "MM-dd");
        String twoDaysAgoStr = DateFormatUtils.format(DateUtil.offsetDay(date, -2), "MM-dd");
        String threeDaysAgoStr = DateFormatUtils.format(DateUtil.offsetDay(date, -3), "MM-dd");
        String fourDaysAgoStr = DateFormatUtils.format(DateUtil.offsetDay(date, -4), "MM-dd");
        String fiveDaysAgoStr = DateFormatUtils.format(DateUtil.offsetDay(date, -5), "MM-dd");
        String sixDaysAgoStr = DateFormatUtils.format(DateUtil.offsetDay(date, -6), "MM-dd");

        if (!CollectionUtils.isEmpty(judgeList))
        {
            Map<String, List<Judge>> map = judgeList.stream()
                    .collect(
                            Collectors.groupingBy(
                                    o -> DateUtil.format(o.getGmtCreate(), "MM-dd"))
                    );
            for (Map.Entry<String, List<Judge>> entry : map.entrySet())
            {
                if (Objects.equals(entry.getKey(), todayStr))
                {
                    totalTodayCount += entry.getValue().size();
                    long count = entry.getValue()
                            .parallelStream()
                            .filter(judge -> Objects.equals(judge.getStatus(), Constants.Judge.STATUS_ACCEPTED.getStatus()))
                            .count();
                    acTodayCount += count;
                } else if (Objects.equals(entry.getKey(), oneDayAgoStr))
                {
                    totalOneDayAgoCount += entry.getValue().size();
                    long count = entry.getValue()
                            .parallelStream()
                            .filter(judge -> Objects.equals(judge.getStatus(), Constants.Judge.STATUS_ACCEPTED.getStatus()))
                            .count();
                    acOneDayAgoCount += count;
                } else if (Objects.equals(entry.getKey(), twoDaysAgoStr))
                {
                    totalTwoDaysAgoCount += entry.getValue().size();
                    long count = entry.getValue()
                            .parallelStream()
                            .filter(judge -> Objects.equals(judge.getStatus(), Constants.Judge.STATUS_ACCEPTED.getStatus()))
                            .count();
                    acTwoDaysAgoCount += count;
                } else if (Objects.equals(entry.getKey(), threeDaysAgoStr))
                {
                    totalThreeDaysAgoCount += entry.getValue().size();
                    long count = entry.getValue()
                            .parallelStream()
                            .filter(judge -> Objects.equals(judge.getStatus(), Constants.Judge.STATUS_ACCEPTED.getStatus()))
                            .count();
                    acThreeDaysAgoCount += count;
                } else if (Objects.equals(entry.getKey(), fourDaysAgoStr))
                {
                    totalFourDaysAgoCount += entry.getValue().size();
                    long count = entry.getValue()
                            .parallelStream()
                            .filter(judge -> Objects.equals(judge.getStatus(), Constants.Judge.STATUS_ACCEPTED.getStatus()))
                            .count();
                    acFourDaysAgoCount += count;
                } else if (Objects.equals(entry.getKey(), fiveDaysAgoStr))
                {
                    totalFiveDaysAgoCount += entry.getValue().size();
                    long count = entry.getValue()
                            .parallelStream()
                            .filter(judge -> Objects.equals(judge.getStatus(), Constants.Judge.STATUS_ACCEPTED.getStatus()))
                            .count();
                    acFiveDaysAgoCount += count;
                } else if (Objects.equals(entry.getKey(), sixDaysAgoStr))
                {
                    totalSixDaysAgoCount += entry.getValue().size();
                    long count = entry.getValue()
                            .parallelStream()
                            .filter(judge -> Objects.equals(judge.getStatus(), Constants.Judge.STATUS_ACCEPTED.getStatus()))
                            .count();
                    acSixDaysAgoCount += count;
                }
            }
        }

        SubmissionStatisticsVO submissionStatisticsVO = new SubmissionStatisticsVO();
        submissionStatisticsVO.setDateStrList(Arrays.asList(
                sixDaysAgoStr,
                fiveDaysAgoStr,
                fourDaysAgoStr,
                threeDaysAgoStr,
                twoDaysAgoStr,
                oneDayAgoStr,
                todayStr));

        submissionStatisticsVO.setAcCountList(Arrays.asList(
                acSixDaysAgoCount,
                acFiveDaysAgoCount,
                acFourDaysAgoCount,
                acThreeDaysAgoCount,
                acTwoDaysAgoCount,
                acOneDayAgoCount,
                acTodayCount));

        submissionStatisticsVO.setTotalCountList(Arrays.asList(
                totalSixDaysAgoCount,
                totalFiveDaysAgoCount,
                totalFourDaysAgoCount,
                totalThreeDaysAgoCount,
                totalTwoDaysAgoCount,
                totalOneDayAgoCount,
                totalTodayCount));

        return submissionStatisticsVO;
    }
}
