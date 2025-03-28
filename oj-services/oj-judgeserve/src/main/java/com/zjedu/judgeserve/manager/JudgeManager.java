package com.zjedu.judgeserve.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.annotation.HOJAccessEnum;
import com.zjedu.common.exception.*;
import com.zjedu.config.NacosSwitchConfig;
import com.zjedu.config.SwitchConfig;
import com.zjedu.judgeserve.feign.JudgeFeignClient;
import com.zjedu.judgeserve.feign.PassportFeignClient;
import com.zjedu.judgeserve.judge.self.JudgeDispatcher;
import com.zjedu.pojo.dto.SubmitJudgeDTO;
import com.zjedu.pojo.dto.TestJudgeDTO;
import com.zjedu.pojo.dto.TestJudgeReq;
import com.zjedu.pojo.dto.TestJudgeRes;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.JudgeVO;
import com.zjedu.pojo.vo.SubmissionInfoVO;
import com.zjedu.pojo.vo.TestJudgeVO;
import com.zjedu.pojo.vo.UserRolesVO;
import com.zjedu.utils.Constants;
import com.zjedu.utils.IpUtils;
import com.zjedu.utils.RedisUtils;
import com.zjedu.validator.AccessValidator;
import com.zjedu.validator.JudgeValidator;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Zhong
 * @Create 2025/3/26 19:05
 * @Version 1.0
 * @Description
 */

@Component
public class JudgeManager
{

    @Resource
    private HttpServletRequest request;

    @Resource
    private AccessValidator accessValidator;

    @Resource
    private JudgeValidator judgeValidator;

    @Resource
    private NacosSwitchConfig nacosSwitchConfig;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private JudgeFeignClient judgeFeignClient;

    @Resource
    private BeforeDispatchInitManager beforeDispatchInitManager;

    @Resource
    private JudgeDispatcher judgeDispatcher;

    /**
     * 通用查询判题记录列表
     *
     * @param limit
     * @param currentPage
     * @param onlyMine
     * @param searchPid
     * @param searchStatus
     * @param searchUsername
     * @param completeProblemID
     * @return
     * @throws StatusAccessDeniedException
     */
    public IPage<JudgeVO> getJudgeList(Integer limit, Integer currentPage, Boolean onlyMine, String searchPid, Integer searchStatus, String searchUsername, Boolean completeProblemID) throws StatusAccessDeniedException
    {
        // 页数，每页题数若为空，设置默认值
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 30;

        String uid = null;
        // 只查看当前用户的提交
        if (Boolean.TRUE.equals(onlyMine))
        {
            // 需要获取一下该token对应用户的数据（有token便能获取到）
            //从请求头获取用户ID
            String userId = request.getHeader("X-User-Id");
            UserInfo userRolesVo = passportFeignClient.getByUid(userId);

            if (userRolesVo == null)
            {
                throw new StatusAccessDeniedException("当前用户数据为空，请您重新登陆！");
            }
            uid = userRolesVo.getUuid();
        }
        if (searchPid != null)
        {
            searchPid = searchPid.trim();
        }
        if (searchUsername != null)
        {
            searchUsername = searchUsername.trim();
        }
        return judgeFeignClient.getCommonJudgeList(limit,
                currentPage,
                searchPid,
                searchStatus,
                searchUsername,
                uid,
                completeProblemID);

    }

    /**
     * 获取单个提交记录的详情
     *
     * @param submitId
     * @return
     */
    public SubmissionInfoVO getSubmission(Long submitId) throws StatusNotFoundException, StatusAccessDeniedException
    {
        Judge judge = judgeFeignClient.getJudgeById(submitId);
        if (judge == null)
        {
            throw new StatusNotFoundException("此提交数据不存在！");
        }

        //TODO:剩下的代码未测试调通
        String userId = request.getHeader("X-User-Id");
        UserRolesVO userRolesVo = null;
        boolean isRoot = false;
        boolean isProblemAdmin = false;
        // 是否为超级管理员和题目管理员
        if (userId != null)
        {
            userRolesVo = passportFeignClient.getUserRoles(userId);
            if (userRolesVo != null && userRolesVo.getRoles() != null)
            {
                isRoot = userRolesVo.getRoles().stream()
                        .anyMatch(role -> "root".equals(role.getRole()));
                isProblemAdmin = userRolesVo.getRoles().stream()
                        .anyMatch((role -> "problem_admin".equals(role.getRole())));
            }
        }

        // 清空不需要的信息
        judge.setVjudgeUsername(null);
        judge.setVjudgeSubmitId(null);
        judge.setVjudgePassword(null);

        SubmissionInfoVO submissionInfoVo = new SubmissionInfoVO();

        // 处理代码查看权限
        if (!judge.getShare() && !isRoot && !isProblemAdmin)
        {
            if (userRolesVo != null)
            { // 当前是登陆状态
                // 需要判断是否为当前登陆用户自己的提交代码
                if (!judge.getUid().equals(userRolesVo.getUid()))
                {
                    judge.setCode(null);
                }
            } else
            { // 不是登陆状态，就直接无权限查看代码
                judge.setCode(null);
            }
        }

        // 如果不是超管或题目管理员，需要检查网站是否开启隐藏代码功能
        if (!isRoot && !isProblemAdmin && judge.getCode() != null)
        {
            try
            {
                accessValidator.validateAccess(HOJAccessEnum.HIDE_NON_CONTEST_SUBMISSION_CODE);
            } catch (AccessException e)
            {
                judge.setCode("Because the super administrator has enabled " +
                        "the function of not viewing the submitted code outside the contest of master station, \n" +
                        "the code of this submission details has been hidden.");
            }
        }

        Problem problem = judgeFeignClient.getProblemById(judge.getPid());

        // 只允许查看ce错误、sf错误、se错误信息提示
        if (judge.getStatus().intValue() != Constants.Judge.STATUS_COMPILE_ERROR.getStatus() &&
                judge.getStatus().intValue() != Constants.Judge.STATUS_SYSTEM_ERROR.getStatus() &&
                judge.getStatus().intValue() != Constants.Judge.STATUS_SUBMITTED_FAILED.getStatus())
        {
            judge.setErrorMessage("The error message does not support viewing.");
        }

        submissionInfoVo.setSubmission(judge);
        submissionInfoVo.setCodeShare(problem.getCodeShare());

        return submissionInfoVo;
    }

    public Judge submitProblemJudge(SubmitJudgeDTO judgeDto) throws StatusForbiddenException, StatusFailException, StatusNotFoundException, StatusAccessDeniedException, AccessException
    {
        judgeValidator.validateSubmissionInfo(judgeDto);

        // 需要获取一下该token对应用户的数据
        //从请求头获取用户ID
        String userId = request.getHeader("X-User-Id");
        UserInfo userRolesVo = passportFeignClient.getByUid(userId);

        boolean isTrainingSubmission = judgeDto.getTid() != null && judgeDto.getTid() != 0;
        boolean isContestSubmission = false;

        SwitchConfig switchConfig = nacosSwitchConfig.getSwitchConfig();

        // 提交频率限制
        String lockKey = Constants.Account.SUBMIT_NON_CONTEST_LOCK.getCode() + userRolesVo.getUuid();
        if (switchConfig.getDefaultSubmitInterval() > 0 && !redisUtils.isWithinRateLimit(lockKey, switchConfig.getDefaultSubmitInterval()))
        {
            throw new StatusForbiddenException("对不起，您的提交频率过快，请稍后再尝试！");
        }

        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        // 将提交先写入数据库，准备调用判题服务器
        Judge judge = new Judge();
        judge.setShare(false) // 默认设置代码为单独自己可见
                .setCode(judgeDto.getCode())
//                .setCid(judgeDto.getCid())
//                .setGid(judgeDto.getGid())
                .setLanguage(judgeDto.getLanguage())
                .setLength(judgeDto.getCode().length())
                .setUid(userRolesVo.getUuid())
                .setUsername(userRolesVo.getUsername())
                .setStatus(Constants.Judge.STATUS_PENDING.getStatus()) // 开始进入判题队列
                .setSubmitTime(new Date())
                .setVersion(0)
                .setIp(IpUtils.getUserIpAddr(request));

        // 根据提交类型初始化
        if (isTrainingSubmission)
        {
//            beforeDispatchInitManager.initTrainingSubmission(judgeDto.getTid(), judgeDto.getPid(), userRolesVo, judge);
        } else
        {
            judge = beforeDispatchInitManager.initCommonSubmission(judgeDto.getPid(), judge);
        }

        // 将提交加入任务队列
        judgeDispatcher.sendTask(judge.getSubmitId(),
                judge.getPid(),
                isContestSubmission);

        return judge;
    }

    public String submitProblemTestJudge(TestJudgeDTO testJudgeDto) throws AccessException,
            StatusFailException, StatusForbiddenException, StatusSystemErrorException
    {
        judgeValidator.validateTestJudgeInfo(testJudgeDto);
        // 需要获取一下该token对应用户的数据
        //从请求头获取用户ID
        String userId = request.getHeader("X-User-Id");
        UserInfo userRolesVo = passportFeignClient.getByUid(userId);

        String lockKey = Constants.Account.TEST_JUDGE_LOCK.getCode() + userRolesVo.getUuid();
        SwitchConfig switchConfig = nacosSwitchConfig.getSwitchConfig();
        if (switchConfig.getDefaultSubmitInterval() > 0)
        {
            if (!redisUtils.isWithinRateLimit(lockKey, switchConfig.getDefaultSubmitInterval()))
            {
                throw new StatusForbiddenException("对不起，您使用在线调试过于频繁，请稍后再尝试！");
            }
        }

        Problem problem = judgeFeignClient.getProblemById(testJudgeDto.getPid());
        if (problem == null)
        {
            throw new StatusFailException("当前题目不存在！不支持在线调试！");
        }

        String uniqueKey = "TEST_JUDGE_" + IdUtil.simpleUUID();
        TestJudgeReq testJudgeReq = new TestJudgeReq();
        testJudgeReq.setMemoryLimit(problem.getMemoryLimit())
                .setTimeLimit(problem.getTimeLimit())
                .setStackLimit(problem.getStackLimit())
                .setCode(testJudgeDto.getCode())
                .setLanguage(testJudgeDto.getLanguage())
                .setUniqueKey(uniqueKey)
                .setExpectedOutput(testJudgeDto.getExpectedOutput())
                .setTestCaseInput(testJudgeDto.getUserInput())
                .setProblemJudgeMode(problem.getJudgeMode())
                .setIsRemoveEndBlank(problem.getIsRemoveEndBlank() || problem.getIsRemote())
                .setIsFileIO(problem.getIsFileIO())
                .setIoReadFileName(problem.getIoReadFileName())
                .setIoWriteFileName(problem.getIoWriteFileName());
        String userExtraFile = problem.getUserExtraFile();
        if (StringUtils.hasText(userExtraFile))
        {
            testJudgeReq.setExtraFile((HashMap<String, String>) JSONUtil.toBean(userExtraFile, Map.class));
        }
        judgeDispatcher.sendTestJudgeTask(testJudgeReq);
        redisUtils.set(uniqueKey, TestJudgeRes.builder()
                .status(Constants.Judge.STATUS_PENDING.getStatus())
                .build(), 10 * 60);
        return uniqueKey;
    }

    public TestJudgeVO getTestJudgeResult(String testJudgeKey) throws StatusFailException
    {
        TestJudgeRes testJudgeRes = (TestJudgeRes) redisUtils.get(testJudgeKey);
        if (testJudgeRes == null)
        {
            throw new StatusFailException("查询错误！当前在线调试任务不存在！");
        }
        TestJudgeVO testJudgeVo = new TestJudgeVO();
        testJudgeVo.setStatus(testJudgeRes.getStatus());
        if (Constants.Judge.STATUS_PENDING.getStatus().equals(testJudgeRes.getStatus()))
        {
            return testJudgeVo;
        }
        testJudgeVo.setUserInput(testJudgeRes.getInput());
        testJudgeVo.setUserOutput(testJudgeRes.getStdout());
        testJudgeVo.setExpectedOutput(testJudgeRes.getExpectedOutput());
        testJudgeVo.setMemory(testJudgeRes.getMemory());
        testJudgeVo.setTime(testJudgeRes.getTime());
        testJudgeVo.setStderr(testJudgeRes.getStderr());
        testJudgeVo.setProblemJudgeMode(testJudgeRes.getProblemJudgeMode());
        redisUtils.del(testJudgeKey);
        return testJudgeVo;
    }
}
