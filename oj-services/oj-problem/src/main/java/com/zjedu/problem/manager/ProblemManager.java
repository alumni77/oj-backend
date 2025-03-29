package com.zjedu.problem.manager;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.annotation.HOJAccessEnum;
import com.zjedu.common.exception.*;
import com.zjedu.pojo.dto.PidListDTO;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.entity.problem.*;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.*;
import com.zjedu.problem.dao.*;
import com.zjedu.problem.feign.JudgeFeignClient;
import com.zjedu.problem.feign.PassportFeignClient;
import com.zjedu.utils.Constants;
import com.zjedu.validator.AccessValidator;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

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
    private ProblemTagEntityService problemTagEntityService;

    @Resource
    private TagEntityService tagEntityService;

    @Resource
    private LanguageEntityService languageEntityService;

    @Resource
    private ProblemLanguageEntityService problemLanguageEntityService;

    @Resource
    private CodeTemplateEntityService codeTemplateEntityService;

    @Resource
    private AccessValidator accessValidator;

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

    /**
     * 获取指定题目的详情信息，标签，所支持语言，做题情况（只能查询公开题目 也就是auth为1）
     *
     * @param problemId
     * @return
     * @throws StatusNotFoundException
     * @throws StatusForbiddenException
     */
    public ProblemInfoVO getProblemInfo(String problemId) throws StatusNotFoundException, StatusForbiddenException
    {
        QueryWrapper<Problem> wrapper = new QueryWrapper<Problem>().eq("problem_id", problemId);
        //查询题目详情，题目标签，题目语言，题目做题情况
        Problem problem = problemEntityService.getOne(wrapper, false);
        if (problem == null)
        {
            throw new StatusNotFoundException("该题号对应的题目不存在");
        }
        if (problem.getAuth() != 1)
        {
            throw new StatusForbiddenException("该题号对应题目并非公开题目，不支持访问！");
        }

        QueryWrapper<ProblemTag> problemTagQueryWrapper = new QueryWrapper<>();
        problemTagQueryWrapper.eq("pid", problem.getId());

        // 获取该题号对应的标签id
        List<Long> tidList = new LinkedList<>();
        problemTagEntityService.list(problemTagQueryWrapper).forEach(problemTag ->
        {
            tidList.add(problemTag.getTid());
        });
        List<Tag> tags = new ArrayList<>();
        if (!tidList.isEmpty())
        {
            tags = tagEntityService.listByIds(tidList);
        }

        // 记录 languageId对应的name
        HashMap<Long, String> tmpMap = new HashMap<>();
        // 获取题目提交的代码支持的语言
        List<String> languagesStr = new LinkedList<>();
        QueryWrapper<ProblemLanguage> problemLanguageQueryWrapper = new QueryWrapper<>();
        problemLanguageQueryWrapper.eq("pid", problem.getId()).select("lid");
        List<Long> lidList = problemLanguageEntityService.list(problemLanguageQueryWrapper)
                .stream().map(ProblemLanguage::getLid).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(lidList))
        {
            Collection<Language> languages = languageEntityService.listByIds(lidList);
            languages = languages.stream().sorted(Comparator.comparing(Language::getSeq, Comparator.reverseOrder())
                            .thenComparing(Language::getId))
                    .toList();
            languages.forEach(language ->
            {
                languagesStr.add(language.getName());
                tmpMap.put(language.getId(), language.getName());
            });
        }
        // 获取题目的提交记录
        ProblemCountVO problemCount = judgeFeignClient.getProblemCountByPid(problem.getId());

        // 获取题目的代码模板
        QueryWrapper<CodeTemplate> codeTemplateQueryWrapper = new QueryWrapper<>();
        codeTemplateQueryWrapper.eq("pid", problem.getId()).eq("status", true);
        List<CodeTemplate> codeTemplates = codeTemplateEntityService.list(codeTemplateQueryWrapper);
        HashMap<String, String> LangNameAndCode = new HashMap<>();
        if (CollectionUtil.isNotEmpty(codeTemplates))
        {
            for (CodeTemplate codeTemplate : codeTemplates)
            {
                LangNameAndCode.put(tmpMap.get(codeTemplate.getLid()), codeTemplate.getCode());
            }
        }
        // 屏蔽一些题目参数
        problem.setJudgeExtraFile(null)
                .setSpjCode(null)
                .setSpjLanguage(null);

        // 将数据统一写入到一个Vo返回数据实体类中
        return new ProblemInfoVO(problem, tags, languagesStr, problemCount, LangNameAndCode);
    }

    public LastAcceptedCodeVO getUserLastAcceptedCode(Long pid)
    {
        // 需要获取一下该token对应用户的数据
        //从请求头获取用户ID
        String userId = request.getHeader("X-User-Id");
        UserInfo userRolesVo = passportFeignClient.getByUid(userId);

        List<Judge> judgeList = judgeFeignClient.queryJudgeListByWrapper(pid, userId, 0);
        LastAcceptedCodeVO lastAcceptedCodeVO = new LastAcceptedCodeVO();
        if (CollectionUtil.isNotEmpty(judgeList))
        {
            Judge judge = judgeList.get(0);
            lastAcceptedCodeVO.setSubmitId(judge.getSubmitId());
            lastAcceptedCodeVO.setLanguage(judge.getLanguage());
            lastAcceptedCodeVO.setCode(buildCode(judge, userRolesVo.getUuid()));
        } else
        {
            lastAcceptedCodeVO.setCode("");
        }
        return lastAcceptedCodeVO;
    }

    private String buildCode(Judge judge, String uid)
    {

        //如果不是超管或题目管理员，需要检查网站是否开启隐藏代码功能
        UserRolesVO userRolesVo = passportFeignClient.getUserRoles(uid);
        // 是否为超级管理员
        boolean isRoot = userRolesVo.getRoles().stream()
                .anyMatch(role -> "root".equals(role.getRole()));
        // 是否为题目管理员
        boolean isProblemAdmin = userRolesVo.getRoles().stream()
                .anyMatch((role -> "problem_admin".equals(role.getRole())));

        if (!isRoot && !isProblemAdmin)
        {
            try
            {
                accessValidator.validateAccess(HOJAccessEnum.HIDE_NON_CONTEST_SUBMISSION_CODE);
            } catch (AccessException e)
            {
                return "Because the super administrator has enabled " +
                        "the function of not viewing the submitted code outside the contest of master station, \n" +
                        "the code of this submission details has been hidden.";
            }
        }
        if (judge.getLanguage().toLowerCase().contains("py"))
        {
            return judge.getCode() + "\n\n" +
                    "'''\n" +
                    "    @runId: " + judge.getSubmitId() + "\n" +
                    "    @language: " + judge.getLanguage() + "\n" +
                    "    @author: " + judge.getUsername() + "\n" +
                    "    @submitTime: " + DateUtil.format(judge.getSubmitTime(), "yyyy-MM-dd HH:mm:ss") + "\n" +
                    "'''";
        } else if (judge.getLanguage().toLowerCase().contains("ruby"))
        {
            return judge.getCode() + "\n\n" +
                    "=begin\n" +
                    "* @runId: " + judge.getSubmitId() + "\n" +
                    "* @language: " + judge.getLanguage() + "\n" +
                    "* @author: " + judge.getUsername() + "\n" +
                    "* @submitTime: " + DateUtil.format(judge.getSubmitTime(), "yyyy-MM-dd HH:mm:ss") + "\n" +
                    "=end";
        } else
        {
            return judge.getCode() + "\n\n" +
                    "/**\n" +
                    "* @runId: " + judge.getSubmitId() + "\n" +
                    "* @language: " + judge.getLanguage() + "\n" +
                    "* @author: " + judge.getUsername() + "\n" +
                    "* @submitTime: " + DateUtil.format(judge.getSubmitTime(), "yyyy-MM-dd HH:mm:ss") + "\n" +
                    "*/";
        }
    }

    //TODO: 该方法需要用到trainingManager
    public List<ProblemFullScreenListVO> getFullScreenProblemList(Long tid) throws StatusFailException, StatusForbiddenException, StatusAccessDeniedException
    {
        if (tid != null)
        {
            return null;
//            return trainingManager.getProblemFullScreenList(tid);
        } else
        {
            throw new StatusFailException("请求参数错误：tid不能为空");
        }
    }
}
