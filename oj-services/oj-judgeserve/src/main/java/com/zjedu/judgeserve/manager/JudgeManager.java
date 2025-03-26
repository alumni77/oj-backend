package com.zjedu.judgeserve.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.annotation.HOJAccessEnum;
import com.zjedu.common.exception.AccessException;
import com.zjedu.common.exception.StatusAccessDeniedException;
import com.zjedu.common.exception.StatusNotFoundException;
import com.zjedu.judgeserve.feign.JudgeFeignClient;
import com.zjedu.judgeserve.feign.PassportFeignClient;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.JudgeVO;
import com.zjedu.pojo.vo.SubmissionInfoVO;
import com.zjedu.pojo.vo.UserRolesVO;
import com.zjedu.utils.Constants;
import com.zjedu.validator.AccessValidator;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

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
    private PassportFeignClient passportFeignClient;

    @Resource
    private JudgeFeignClient judgeFeignClient;

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
     * @param gid
     * @return
     * @throws StatusAccessDeniedException
     */
    public IPage<JudgeVO> getJudgeList(Integer limit, Integer currentPage, Boolean onlyMine, String searchPid, Integer searchStatus, String searchUsername, Boolean completeProblemID, Long gid) throws StatusAccessDeniedException
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
                completeProblemID,
                gid);

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
}
