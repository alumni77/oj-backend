package com.zjedu.admin.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjedu.admin.dao.TrainingCategoryEntityService;
import com.zjedu.admin.feign.PassportFeignClient;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.pojo.entity.training.TrainingCategory;
import com.zjedu.pojo.vo.UserRolesVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/4/2 20:52
 * @Version 1.0
 * @Description
 */

@Slf4j
@Component
public class AdminTrainingCategoryManager
{
    @Resource
    private HttpServletRequest request;

    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private TrainingCategoryEntityService trainingCategoryEntityService;

    public TrainingCategory addTrainingCategory(TrainingCategory trainingCategory) throws StatusFailException, StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有权限添加分类");
        }

        QueryWrapper<TrainingCategory> trainingCategoryQueryWrapper = new QueryWrapper<>();
        trainingCategoryQueryWrapper.eq("name", trainingCategory.getName());
        TrainingCategory existedTrainingCategory = trainingCategoryEntityService.getOne(trainingCategoryQueryWrapper, false);

        if (existedTrainingCategory != null)
        {
            throw new StatusFailException("该分类名称已存在！请勿重复添加！");
        }

        boolean isOk = trainingCategoryEntityService.save(trainingCategory);
        if (!isOk)
        {
            throw new StatusFailException("添加失败");
        }
        return trainingCategory;
    }

    public void updateTrainingCategory(TrainingCategory trainingCategory) throws StatusForbiddenException, StatusFailException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有权限修改分类");
        }
        if (trainingCategory.getId() == null)
        {
            throw new StatusFailException("分类ID不能为空！");
        }

        boolean isOk = trainingCategoryEntityService.updateById(trainingCategory);
        if (!isOk)
        {
            throw new StatusFailException("更新失败！");
        }
    }

    public void deleteTrainingCategory(Long id) throws StatusFailException, StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有权限删除分类");
        }
        boolean isOk = trainingCategoryEntityService.removeById(id);
        if (!isOk)
        {
            throw new StatusFailException("删除失败！");
        }
        // 获取当前登录的用户
        String userId = request.getHeader("X-User-ID");
        UserRolesVO userRolesVo = passportFeignClient.getUserRoles(userId, "");
        log.info("[{}],[{}],categoryId:[{}],operatorUid:[{}],operatorUsername:[{}]",
                "Admin_Training", "Delete_Category", id, userRolesVo.getUid(), userRolesVo.getUsername());
    }

    public List<TrainingCategory> getTrainingCategoryList() throws StatusForbiddenException, StatusFailException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("没有权限查看分类");
        }
        List<TrainingCategory> trainingCategories = trainingCategoryEntityService.getBaseMapper().selectList(null);
        if (trainingCategories == null)
        {
            throw new StatusFailException("获取分类列表失败！");
        }
        return trainingCategories;
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
