package com.zjedu.admin.manager;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.admin.dao.JudgeEntityService;
import com.zjedu.admin.dao.ProblemCaseEntityService;
import com.zjedu.admin.dao.ProblemEntityService;
import com.zjedu.admin.feign.JudgeServeFeignClient;
import com.zjedu.admin.feign.PassportFeignClient;
import com.zjedu.admin.feign.ProblemFeignClient;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.CompileDTO;
import com.zjedu.pojo.dto.ProblemDTO;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.entity.problem.ProblemCase;
import com.zjedu.pojo.entity.user.Role;
import com.zjedu.pojo.vo.UserRolesVO;
import com.zjedu.utils.Constants;
import com.zjedu.validator.ProblemValidator;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author Zhong
 * @Create 2025/4/2 14:20
 * @Version 1.0
 * @Description
 */

@Slf4j
@Component
public class AdminProblemManager
{
    @Resource
    private ProblemEntityService problemEntityService;

    @Resource
    private HttpServletRequest request;

    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private ProblemFeignClient problemFeignClient;

    @Resource
    private JudgeServeFeignClient judgeServeFeignClient;

    @Resource
    private ProblemValidator problemValidator;

    @Resource
    private JudgeEntityService judgeEntityService;

    @Resource
    private ProblemCaseEntityService problemCaseEntityService;

    @Value("${oj.judge.token}")
    private String judgeToken;

    public IPage<Problem> getProblemList(Integer limit, Integer currentPage, String keyword, Integer auth, String oj) throws StatusForbiddenException, StatusFailException
    {

        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有权限操作！");
        }

        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;
        IPage<Problem> iPage = new Page<>(currentPage, limit);
        IPage<Problem> problemList;

        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");

        // 根据oj筛选过滤
        if (oj != null && !"All".equals(oj))
        {
            if (!Constants.RemoteOJ.isRemoteOJ(oj))
            {
                queryWrapper.eq("is_remote", false);
            } else
            {
                queryWrapper.eq("is_remote", true).likeRight("problem_id", oj);
            }
        }

        if (auth != null && auth != 0)
        {
            queryWrapper.eq("auth", auth);
        }

        if (StringUtils.hasText(keyword))
        {
            final String key = keyword.trim();
            queryWrapper.and(wrapper -> wrapper.like("title", key).or()
                    .like("author", key).or()
                    .like("problem_id", key));
        }
        problemList = problemEntityService.page(iPage, queryWrapper);
        return problemList;
    }

    public Problem getProblem(Long pid) throws StatusForbiddenException, StatusFailException
    {
        Problem problem = problemEntityService.getById(pid);

        if (problem != null)
        { // 查询成功
            // 获取当前登录的用户
            boolean b = checkAuthority();
            // 只有超级管理员和题目管理员、题目创建者才能操作
            if (!b)
            {
                throw new StatusForbiddenException("对不起，你无权限查看题目！");
            }
            return problem;
        } else
        {
            throw new StatusFailException("查询失败！");
        }
    }

    public void deleteProblem(Long pid) throws StatusFailException, StatusForbiddenException
    {
        // 获取当前登录的用户
        boolean b = checkAuthority();
        // 只有超级管理员和题目管理员、题目创建者才能操作
        if (!b)
        {
            throw new StatusForbiddenException("对不起，你无权限删除题目！");
        }

        boolean isOk = problemEntityService.removeById(pid);
        /*
        problem的id为其他表的外键的表中的对应数据都会被一起删除！
         */
        if (isOk)
        { // 删除成功
            FileUtil.del(Constants.File.TESTCASE_BASE_FOLDER.getPath() + File.separator + "problem_" + pid);
            String userId = request.getHeader("X-User-ID");
            UserRolesVO userRolesVo = passportFeignClient.getUserRoles(userId, "");
            log.info("[{}],[{}],pid:[{}],operatorUid:[{}],operatorUsername:[{}]",
                    "Admin_Problem", "Delete", pid, userRolesVo.getUid(), userRolesVo.getUsername());
        } else
        {
            throw new StatusFailException("删除失败！");
        }
    }

    public void addProblem(ProblemDTO problemDto) throws StatusFailException, StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有权限添加题目操作！");
        }

        problemValidator.validateProblem(problemDto.getProblem());
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("problem_id", problemDto.getProblem().getProblemId().toUpperCase());
        Problem problem = problemEntityService.getOne(queryWrapper);
        if (problem != null)
        {
            throw new StatusFailException("该题目的Problem ID已存在，请更换！");
        }

        String userId = request.getHeader("X-User-ID");
        UserRolesVO userRoles = passportFeignClient.getUserRoles(userId, "");
        List<Role> roles = userRoles.getRoles();
        problemDto.getProblem().setAuthor(roles.get(0).getRole());
        boolean isOk = problemFeignClient.adminAddProblem(problemDto);
        if (!isOk)
        {
            throw new StatusFailException("添加失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateProblem(ProblemDTO problemDto) throws StatusForbiddenException, StatusFailException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有权限进行更新题目操作！");
        }

        // 确保所有集合不为null
        if (problemDto.getSamples() == null) {
            problemDto.setSamples(new ArrayList<>());
        }

        problemValidator.validateProblemUpdate(problemDto.getProblem());

        // 获取当前登录的用户
        String userId = request.getHeader("X-User-ID");
        UserRolesVO userRolesVo = passportFeignClient.getUserRoles(userId, "");


        String problemId = problemDto.getProblem().getProblemId().toUpperCase();
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("problem_id", problemId);
        Problem problem = problemEntityService.getOne(queryWrapper);

        // 如果problem_id不是原来的且已存在该problem_id，则修改失败！
        if (problem != null && problem.getId().longValue() != problemDto.getProblem().getId())
        {
            throw new StatusFailException("当前的Problem ID 已被使用，请重新更换新的！");
        }

        // 记录修改题目的用户
        problemDto.getProblem().setModifiedUser(userRolesVo.getUsername());

        boolean result = problemFeignClient.adminUpdateProblem(problemDto);
        if (result)
        { // 更新成功
            if (problem == null)
            { // 说明改了problemId，同步一下judge表
                UpdateWrapper<Judge> judgeUpdateWrapper = new UpdateWrapper<>();
                judgeUpdateWrapper.eq("pid", problemDto.getProblem().getId())
                        .set("display_pid", problemId);
                judgeEntityService.update(judgeUpdateWrapper);
            }

        } else
        {
            throw new StatusFailException(" 修改失败");
        }
    }

    public List<ProblemCase> getProblemCases(Long pid, Boolean isUpload) throws StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有权限获取例题操作！");
        }

        QueryWrapper<ProblemCase> problemCaseQueryWrapper = new QueryWrapper<>();
        problemCaseQueryWrapper.eq("pid", pid).eq("status", 0);
        if (isUpload)
        {
            problemCaseQueryWrapper.last("order by length(input) asc,input asc");
        }
        return problemCaseEntityService.list(problemCaseQueryWrapper);
    }

    public CommonResult compileSpj(CompileDTO compileDTO) throws StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有权限操作！");
        }

        if (!StringUtils.hasText(compileDTO.getCode()) || !StringUtils.hasText(compileDTO.getLanguage()))
        {
            return CommonResult.errorResponse("参数不能为空！");
        }

        compileDTO.setToken(judgeToken);
        judgeServeFeignClient.compileSpj(compileDTO);
        return null;
    }

    public CommonResult compileInteractive(CompileDTO compileDTO) throws StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有权限操作！");
        }


        if (!StringUtils.hasText(compileDTO.getCode()) || !StringUtils.hasText(compileDTO.getLanguage()))
        {
            return CommonResult.errorResponse("参数不能为空！");
        }

        compileDTO.setToken(judgeToken);
        judgeServeFeignClient.compileInteractive(compileDTO);
        return null;
    }

    public void changeProblemAuth(Problem problem) throws StatusFailException, StatusForbiddenException
    {

        String userId = request.getHeader("X-User-ID");
        UserRolesVO userRolesVo = passportFeignClient.getUserRoles(userId, "");
        // 是否为超级管理员
        boolean isRoot = userRolesVo.getRoles().stream()
                .anyMatch(role -> "root".equals(role.getRole()));
        // 是否为problem_admin
        boolean isProblemAdmin = userRolesVo.getRoles().stream()
                .anyMatch(role -> "problem_admin".equals(role.getRole()));

        if (!isProblemAdmin && !isRoot && problem.getAuth() == 1)
        {
            throw new StatusForbiddenException("修改失败！你无权限公开题目！");
        }

        UpdateWrapper<Problem> problemUpdateWrapper = new UpdateWrapper<>();
        problemUpdateWrapper.eq("id", problem.getId())
                .set("auth", problem.getAuth())
                .set("modified_user", userRolesVo.getUsername());

        boolean isOk = problemEntityService.update(problemUpdateWrapper);
        if (!isOk)
        {
            throw new StatusFailException("修改失败");
        }
        log.info("[{}],[{}],value:[{}],pid:[{}],operatorUid:[{}],operatorUsername:[{}]",
                "Admin_Problem", "Change_Auth", problem.getAuth(), problem.getId(), userRolesVo.getUid(), userRolesVo.getUsername());
    }

    private boolean checkAuthority()
    {
        String userId = request.getHeader("X-User-ID");
        UserRolesVO userRoles = passportFeignClient.getUserRoles(userId, "");
        // 是否为超级管理员
        boolean isRoot = userRoles.getRoles().stream()
                .anyMatch(role -> "root".equals(role.getRole()));
        // 是否为admin
        boolean isAdmin = userRoles.getRoles().stream()
                .anyMatch(role -> "admin".equals(role.getRole()));
        // 是否为problem_admin
        boolean isProblemAdmin = userRoles.getRoles().stream()
                .anyMatch(role -> "problem_admin".equals(role.getRole()));

        return isRoot || isAdmin || isProblemAdmin;
    }
}
