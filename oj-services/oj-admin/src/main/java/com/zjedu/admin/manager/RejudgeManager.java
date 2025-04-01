package com.zjedu.admin.manager;

import com.zjedu.admin.dao.JudgeEntityService;
import com.zjedu.admin.feign.JudgeServeFeignClient;
import com.zjedu.admin.feign.PassportFeignClient;
import com.zjedu.admin.feign.ProblemFeignClient;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.vo.UserRolesVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author Zhong
 * @Create 2025/4/1 20:48
 * @Version 1.0
 * @Description
 */

@Slf4j
@Component
public class RejudgeManager
{
    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private ProblemFeignClient problemFeignClient;

    @Resource
    private JudgeServeFeignClient judgeServeFeignClient;

    @Resource
    private JudgeEntityService judgeEntityService;

    @Resource
    private HttpServletRequest request;

    public Judge rejudge(Long submitId) throws StatusFailException
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

        if (!isRoot && !isAdmin && !isProblemAdmin)
        {
            throw new StatusFailException("没有权限重判");
        }

        Judge judge = judgeEntityService.getById(submitId);

        // 调用判题服务
        judgeServeFeignClient.sendTask(judge.getSubmitId(), judge.getPid(), false);
        return judge;
    }
}
