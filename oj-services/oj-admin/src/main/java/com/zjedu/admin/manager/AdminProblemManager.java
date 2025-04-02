package com.zjedu.admin.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjedu.admin.dao.ProblemEntityService;
import com.zjedu.admin.feign.PassportFeignClient;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.vo.UserRolesVO;
import com.zjedu.utils.Constants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


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

    public IPage<Problem> getProblemList(Integer limit, Integer currentPage, String keyword, Integer auth, String oj) throws StatusFailException
    {

        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusFailException("没有权限操作！");
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
