package com.zjedu.judgeserve.validator;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjedu.common.exception.StatusAccessDeniedException;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.judgeserve.dao.training.TrainingRegisterEntityService;
import com.zjedu.judgeserve.dao.user.UserInfoEntityService;
import com.zjedu.judgeserve.feign.PassportFeignClient;
import com.zjedu.pojo.entity.training.Training;
import com.zjedu.pojo.entity.training.TrainingRegister;
import com.zjedu.pojo.entity.user.Role;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.utils.Constants;
import com.zjedu.validator.CommonValidator;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @Author Zhong
 * @Create 2025/4/8 21:24
 * @Version 1.0
 * @Description
 */

@Component
public class TrainingValidator
{

    @Resource
    private TrainingRegisterEntityService trainingRegisterEntityService;

    @Resource
    private CommonValidator commonValidator;

    @Resource
    private HttpServletRequest request;

    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private UserInfoEntityService userInfoEntityService;

    /**
     * 验证训练数据合法性
     *
     * @param training
     * @throws StatusFailException
     */
    public void validateTraining(Training training) throws StatusFailException
    {
        commonValidator.validateContent(training.getTitle(), "训练标题", 500);
        commonValidator.validateContentLength(training.getDescription(), "训练描述", 65535);
        if (!Objects.equals(training.getAuth(), "Public")
                && !Objects.equals(training.getAuth(), "Private"))
        {
            throw new StatusFailException("训练的权限类型必须为公开训练(Public)、私有训练(Private)！");
        }
    }

    /**
     * 验证用户训练的权限
     *
     * @param training
     * @throws StatusAccessDeniedException
     * @throws StatusForbiddenException
     */
    public void validateTrainingAuth(Training training) throws StatusAccessDeniedException, StatusForbiddenException
    {
        // 获取当前登录的用户
        //从请求头获取用户ID
        String userId = request.getHeader("X-User-Id");
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", userId);
        UserInfo userRolesVo = userInfoEntityService.getOne(queryWrapper, false);
        validateTrainingAuth(training, userRolesVo);
    }


    /**
     * 根据指定用户验证训练权限
     *
     * @param training
     * @param userRolesVo
     * @throws StatusAccessDeniedException
     * @throws StatusForbiddenException
     */
    public void validateTrainingAuth(Training training, UserInfo userRolesVo) throws StatusAccessDeniedException, StatusForbiddenException
    {
        // 使用 PassportFeignClient 获取用户角色列表
        List<Role> roles = passportFeignClient.getRolesByUid(userRolesVo.getUuid());
        boolean isRoot = roles.stream().anyMatch(role -> "root".equals(role.getRole()));

        if (Constants.Training.AUTH_PRIVATE.getValue().equals(training.getAuth()))
        {
            if (userRolesVo == null)
            {
                throw new StatusAccessDeniedException("该训练属于私有题单，请先登录以校验权限！");
            }
            boolean isAuthor = training.getAuthor().equals(userRolesVo.getUsername()); // 是否为该私有训练的创建者

            if (isRoot || isAuthor)
            {
                return;
            }

            // 如果不是管理员或作者，需要做注册权限校验
            checkTrainingRegister(training.getId(), userRolesVo.getUuid());
        }
    }

    /**
     * 检查用户是否已注册该训练
     *
     * @param tid
     * @param uid
     * @throws StatusAccessDeniedException
     * @throws StatusForbiddenException
     */
    private void checkTrainingRegister(Long tid, String uid) throws StatusAccessDeniedException, StatusForbiddenException
    {
        QueryWrapper<TrainingRegister> trainingRegisterQueryWrapper = new QueryWrapper<>();
        trainingRegisterQueryWrapper.eq("tid", tid);
        trainingRegisterQueryWrapper.eq("uid", uid);
        TrainingRegister trainingRegister = trainingRegisterEntityService.getOne(trainingRegisterQueryWrapper, false);

        if (trainingRegister == null)
        {
            throw new StatusAccessDeniedException("该训练属于私有，请先使用专属密码注册！");
        }

        if (!trainingRegister.getStatus())
        {
            throw new StatusForbiddenException("错误：你已被禁止参加该训练！");
        }
    }

    /**
     * 检查用户是否在训练中或是管理员
     *
     * @param training
     * @param userRolesVo
     * @return
     * @throws StatusAccessDeniedException
     */
    public boolean isInTrainingOrAdmin(Training training, UserInfo userRolesVo) throws StatusAccessDeniedException
    {
        if (Constants.Training.AUTH_PRIVATE.getValue().equals(training.getAuth()))
        {
            if (userRolesVo == null)
            {
                throw new StatusAccessDeniedException("该训练属于私有题单，请先登录以校验权限！");
            }

            // 使用 PassportFeignClient 获取用户角色列表
            List<Role> roles = passportFeignClient.getRolesByUid(userRolesVo.getUuid());
            boolean isRoot = roles.stream().anyMatch(role -> "root".equals(role.getRole())); // 是否为超级管理员
            boolean isAuthor = training.getAuthor().equals(userRolesVo.getUsername()); // 是否为该私有训练的创建者

            if (isRoot || isAuthor)
            {
                return true;
            }

            // 如果不是管理员或作者，需要做注册权限校验
            QueryWrapper<TrainingRegister> trainingRegisterQueryWrapper = new QueryWrapper<>();
            trainingRegisterQueryWrapper.eq("tid", training.getId());
            trainingRegisterQueryWrapper.eq("uid", userRolesVo.getUuid());
            TrainingRegister trainingRegister = trainingRegisterEntityService.getOne(trainingRegisterQueryWrapper, false);
            return trainingRegister != null && trainingRegister.getStatus();
        }
        return true;
    }
}
