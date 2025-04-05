package com.zjedu.admin.manager;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.admin.dao.ProblemEntityService;
import com.zjedu.admin.dao.TrainingEntityService;
import com.zjedu.admin.dao.TrainingProblemEntityService;
import com.zjedu.admin.feign.PassportFeignClient;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.pojo.dto.TrainingProblemDTO;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.entity.training.Training;
import com.zjedu.pojo.entity.training.TrainingProblem;
import com.zjedu.pojo.vo.UserRolesVO;
import com.zjedu.utils.Constants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Zhong
 * @Create 2025/4/2 21:15
 * @Version 1.0
 * @Description
 */

@Slf4j
@Component
public class AdminTrainingProblemManager
{
    @Resource
    private HttpServletRequest request;

    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private TrainingProblemEntityService trainingProblemEntityService;

    @Resource
    private ProblemEntityService problemEntityService;

    @Resource
    private TrainingEntityService trainingEntityService;

    @Resource
    private AdminTrainingRecordManager adminTrainingRecordManager;

    public HashMap<String, Object> getProblemList(Integer limit, Integer currentPage, String keyword, Boolean queryExisted, Long tid) throws StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有操作权限!");
        }

        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;

        IPage<Problem> iPage = new Page<>(currentPage, limit);
        // 根据tid在TrainingProblem表中查询到对应pid集合
        QueryWrapper<TrainingProblem> trainingProblemQueryWrapper = new QueryWrapper<>();
        trainingProblemQueryWrapper.eq("tid", tid).orderByAsc("display_id");
        List<Long> pidList = new LinkedList<>();
        List<TrainingProblem> trainingProblemList = trainingProblemEntityService.list(trainingProblemQueryWrapper);
        HashMap<Long, TrainingProblem> trainingProblemMap = new HashMap<>();
        trainingProblemList.forEach(trainingProblem ->
        {
            if (!trainingProblemMap.containsKey(trainingProblem.getPid()))
            {
                trainingProblemMap.put(trainingProblem.getPid(), trainingProblem);
            }
            pidList.add(trainingProblem.getPid());
        });

        HashMap<String, Object> trainingProblem = new HashMap<>();

        QueryWrapper<Problem> problemQueryWrapper = new QueryWrapper<>();

        // 逻辑判断，如果是查询已有的就应该是in，如果是查询不要重复的，使用not in
        if (queryExisted)
        {
            problemQueryWrapper.in(pidList.size() > 0, "id", pidList);
        } else
        {
            // 权限需要是公开的（隐藏的，比赛中不可加入！）
            problemQueryWrapper.eq("auth", 1);
            problemQueryWrapper.notIn(pidList.size() > 0, "id", pidList);
        }

        if (StringUtils.hasText(keyword))
        {
            problemQueryWrapper.and(wrapper -> wrapper.like("title", keyword).or()
                    .like("problem_id", keyword).or()
                    .like("author", keyword));
        }

        if (pidList.isEmpty() && queryExisted)
        {
            problemQueryWrapper = new QueryWrapper<>();
            problemQueryWrapper.eq("id", null);
        }

        IPage<Problem> problemListPage = problemEntityService.page(iPage, problemQueryWrapper);

        if (queryExisted && pidList.size() > 0)
        {
            List<Problem> problemListPageRecords = problemListPage.getRecords();
            List<Problem> sortProblemList = problemListPageRecords
                    .stream()
                    .sorted(Comparator.comparingInt(problem -> trainingProblemMap.get(problem.getId()).getRank()))
                    .collect(Collectors.toList());
            problemListPage.setRecords(sortProblemList);
        }

        trainingProblem.put("problemList", problemListPage);
        trainingProblem.put("trainingProblemMap", trainingProblemMap);

        return trainingProblem;
    }

    public void updateProblem(TrainingProblem trainingProblem) throws StatusFailException, StatusForbiddenException
    {
        String userId = request.getHeader("X-User-ID");
        UserRolesVO userRoles = passportFeignClient.getUserRoles(userId, "");
        // 是否为超级管理员
        boolean isRoot = userRoles.getRoles().stream()
                .anyMatch(role -> "root".equals(role.getRole()));
        // 是否为problem_admin
        boolean isProblemAdmin = userRoles.getRoles().stream()
                .anyMatch(role -> "problem_admin".equals(role.getRole()));

        if (!isRoot && !isProblemAdmin)
        {
            throw new StatusForbiddenException("没有操作权限!");
        }

        boolean isOk = trainingProblemEntityService.saveOrUpdate(trainingProblem);

        if (!isOk)
        {
            throw new StatusFailException("修改失败！");
        }
    }

    public void deleteProblem(Long pid, Long tid) throws StatusFailException, StatusForbiddenException
    {
        String userId = request.getHeader("X-User-ID");
        UserRolesVO userRolesVo = passportFeignClient.getUserRoles(userId, "");
        // 是否为超级管理员
        boolean isRoot = userRolesVo.getRoles().stream()
                .anyMatch(role -> "root".equals(role.getRole()));
        // 是否为problem_admin
        boolean isProblemAdmin = userRolesVo.getRoles().stream()
                .anyMatch(role -> "problem_admin".equals(role.getRole()));

        if (!isRoot && !isProblemAdmin)
        {
            throw new StatusForbiddenException("没有操作权限!");
        }

        boolean isOk = false;
        //  训练id不为null，表示就是从比赛列表移除而已
        if (tid != null)
        {
            QueryWrapper<TrainingProblem> trainingProblemQueryWrapper = new QueryWrapper<>();
            trainingProblemQueryWrapper.eq("tid", tid).eq("pid", pid);
            isOk = trainingProblemEntityService.remove(trainingProblemQueryWrapper);
        } else
        {
             /*
                problem的id为其他表的外键的表中的对应数据都会被一起删除！
              */
            isOk = problemEntityService.removeById(pid);
        }

        if (isOk)
        {
            // 删除成功
            if (tid == null)
            {
                FileUtil.del(Constants.File.TESTCASE_BASE_FOLDER.getPath() + File.separator + "problem_" + pid);
                log.info("[{}],[{}],tid:[{}],pid:[{}],operatorUid:[{}],operatorUsername:[{}]",
                        "Admin_Training", "Delete_Problem", tid, pid, userRolesVo.getUid(), userRolesVo.getUsername());
            } else
            {
                log.info("[{}],[{}],tid:[{}],pid:[{}],operatorUid:[{}],operatorUsername:[{}]",
                        "Admin_Training", "Remove_Problem", tid, pid, userRolesVo.getUid(), userRolesVo.getUsername());
            }
            // 更新训练最近更新时间
            UpdateWrapper<Training> trainingUpdateWrapper = new UpdateWrapper<>();
            trainingUpdateWrapper.set("gmt_modified", new Date())
                    .eq("id", tid);
            trainingEntityService.update(trainingUpdateWrapper);
        } else
        {
            String msg = "删除失败！";
            if (tid != null)
            {
                msg = "移除失败！";
            }
            throw new StatusFailException(msg);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void addProblemFromPublic(TrainingProblemDTO trainingProblemDto) throws StatusFailException, StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有操作权限!");
        }

        Long pid = trainingProblemDto.getPid();
        Long tid = trainingProblemDto.getTid();
        String displayId = trainingProblemDto.getDisplayId();

        QueryWrapper<TrainingProblem> trainingProblemQueryWrapper = new QueryWrapper<>();
        trainingProblemQueryWrapper.eq("tid", tid)
                .and(wrapper -> wrapper.eq("pid", pid)
                        .or()
                        .eq("display_id", displayId));
        TrainingProblem trainingProblem = trainingProblemEntityService.getOne(trainingProblemQueryWrapper, false);
        if (trainingProblem != null)
        {
            throw new StatusFailException("添加失败，该题目已添加或者题目的训练展示ID已存在！");
        }

        TrainingProblem newTProblem = new TrainingProblem();
        boolean isOk = trainingProblemEntityService.saveOrUpdate(newTProblem
                .setTid(tid).setPid(pid).setDisplayId(displayId));
        if (isOk)
        { // 添加成功

            // 更新训练最近更新时间
            UpdateWrapper<Training> trainingUpdateWrapper = new UpdateWrapper<>();
            trainingUpdateWrapper.set("gmt_modified", new Date())
                    .eq("id", tid);
            trainingEntityService.update(trainingUpdateWrapper);

            // 获取当前登录的用户
            String userId = request.getHeader("X-User-ID");
            UserRolesVO userRolesVo = passportFeignClient.getUserRoles(userId, "");
            log.info("[{}],[{}],tid:[{}],pid:[{}],operatorUid:[{}],operatorUsername:[{}]",
                    "Admin_Training", "Add_Public_Problem", tid, pid, userRolesVo.getUid(), userRolesVo.getUsername());

            // 异步地同步用户对该题目的提交数据
            adminTrainingRecordManager.syncAlreadyRegisterUserRecord(tid, pid, newTProblem.getId());
        } else
        {
            throw new StatusFailException("添加失败！");
        }
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
