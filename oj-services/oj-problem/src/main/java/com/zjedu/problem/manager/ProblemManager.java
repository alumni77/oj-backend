package com.zjedu.problem.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusNotFoundException;
import com.zjedu.pojo.dto.PidListDTO;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.ProblemVO;
import com.zjedu.pojo.vo.RandomProblemVO;
import com.zjedu.problem.dao.ProblemEntityService;
import com.zjedu.problem.feign.JudgeFeignClient;
import com.zjedu.problem.feign.PassportFeignClient;
import com.zjedu.utils.Constants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @Author Zhong
 * @Create 2025/3/28 20:46
 * @Version 1.0
 * @Description
 */

@Component
public class ProblemManager
{
    @Resource
    private ProblemEntityService problemEntityService;

    @Resource
    private HttpServletRequest request;

    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private JudgeFeignClient judgeFeignClient;

    public Page<ProblemVO> getProblemList(Integer limit, Integer currentPage,
                                          String keyword, List<Long> tagId, Integer difficulty, String oj)
    {
        // 页数，每页题数若为空，设置默认值
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;

        // 关键词查询不为空
        if (StringUtils.hasText(keyword))
        {
            keyword = keyword.trim();
        }
        if (oj != null && !Constants.RemoteOJ.isRemoteOJ(oj))
        {
            oj = "Mine";
        }
        return problemEntityService.getProblemList(limit, currentPage, null, keyword,
                difficulty, tagId, oj);
    }

    public RandomProblemVO getRandomProblem() throws StatusFailException
    {
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        // 必须是公开题目
        queryWrapper.select("problem_id").eq("auth", 1);
        List<Problem> list = problemEntityService.list(queryWrapper);
        if (list.isEmpty())
        {
            throw new StatusFailException("获取随机题目失败，题库暂无公开题目！");
        }
        Random random = new Random();
        int index = random.nextInt(list.size());
        RandomProblemVO randomProblemVo = new RandomProblemVO();
        randomProblemVo.setProblemId(list.get(index).getProblemId());
        return randomProblemVo;
    }

    /**
     * 获取用户对应该题目列表中各个题目的做题情况
     *
     * @param pidListDto
     * @return
     */
    public HashMap<Long, Object> getUserProblemStatus(PidListDTO pidListDto) throws StatusNotFoundException
    {

        // 需要获取一下该token对应用户的数据
        //从请求头获取用户ID
        String userId = request.getHeader("X-User-Id");
        UserInfo userRolesVo = passportFeignClient.getByUid(userId);

        HashMap<Long, Object> result = new HashMap<>();

        // 先查询判断该用户对于这些题是否已经通过，若已通过，则无论后续再提交结果如何，该题都标记为通过
        List<Judge> judges = judgeFeignClient.getJudgeListByPids(pidListDto.getPidList(), userRolesVo.getUuid());

        for (Judge judge : judges)
        {
            HashMap<String, Object> temp = new HashMap<>();
            // 不是比赛题目
            if (judge.getStatus().intValue() == Constants.Judge.STATUS_ACCEPTED.getStatus())
            {
                // 如果该题目已通过，则强制写为通过（0）
                temp.put("status", Constants.Judge.STATUS_ACCEPTED.getStatus());
                result.put(judge.getPid(), temp);
            } else if (!result.containsKey(judge.getPid()))
            {
                // 还未写入，则使用最新一次提交的结果
                temp.put("status", judge.getStatus());
                result.put(judge.getPid(), temp);
            }
        }

        // 再次检查，应该可能从未提交过该题，则状态写为-10
        for (Long pid : pidListDto.getPidList())
        {
            if (!result.containsKey(pid))
            {
                HashMap<String, Object> temp = new HashMap<>();
                temp.put("status", Constants.Judge.STATUS_NOT_SUBMITTED.getStatus());
                result.put(pid, temp);
            }
        }
        return result;

    }

}
