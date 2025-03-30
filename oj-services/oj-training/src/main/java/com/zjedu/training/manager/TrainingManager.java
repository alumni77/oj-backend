package com.zjedu.training.manager;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.common.exception.StatusAccessDeniedException;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.pojo.entity.training.Training;
import com.zjedu.pojo.entity.training.TrainingCategory;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.TrainingVO;
import com.zjedu.training.dao.TrainingCategoryEntityService;
import com.zjedu.training.dao.TrainingEntityService;
import com.zjedu.training.dao.TrainingProblemEntityService;
import com.zjedu.training.feign.PassportFeignClient;
import com.zjedu.training.validator.TrainingValidator;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/30 15:37
 * @Version 1.0
 * @Description
 */

@Component
public class TrainingManager
{
    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private HttpServletRequest request;

    @Resource
    private TrainingEntityService trainingEntityService;

    @Resource
    private TrainingCategoryEntityService trainingCategoryEntityService;

    @Resource
    private TrainingProblemEntityService trainingProblemEntityService;

    @Resource
    private TrainingValidator trainingValidator;


    /**
     * 获取训练题单列表，可根据关键词、类别、权限、类型过滤
     *
     * @param limit
     * @param currentPage
     * @param keyword
     * @param categoryId
     * @param auth
     * @return
     */
    public IPage<TrainingVO> getTrainingList(Integer limit,
                                             Integer currentPage,
                                             String keyword,
                                             Long categoryId,
                                             String auth)
    {

        // 页数，每页题数若为空，设置默认值
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 20;

        // 获取当前登录的用户
        //从请求头获取用户ID
        String userId = request.getHeader("X-User-Id");
        UserInfo userRolesVo = passportFeignClient.getByUid(userId);

        String currentUid = null;
        if (userRolesVo != null)
        {
            currentUid = userRolesVo.getUuid();
        }

        return trainingEntityService.getTrainingList(limit, currentPage, categoryId, auth, keyword, currentUid);
    }

    /**
     * 根据tid获取指定训练详情
     *
     * @param tid
     * @return
     */
    public TrainingVO   getTraining(Long tid) throws StatusFailException, StatusAccessDeniedException, StatusForbiddenException
    {
        // 获取当前登录的用户
        //从请求头获取用户ID
        String userId = request.getHeader("X-User-Id");
        UserInfo userRolesVo = passportFeignClient.getByUid(userId);

        Training training = trainingEntityService.getById(tid);
        if (training == null || !training.getStatus())
        {
            throw new StatusFailException("该训练不存在或不允许显示！");
        }

        //TODO:由于训练的数据库表中没有数据，以下代码还不知是否调通
        TrainingVO trainingVo = BeanUtil.copyProperties(training, TrainingVO.class);
        TrainingCategory trainingCategory = trainingCategoryEntityService.getTrainingCategoryByTrainingId(training.getId());
        trainingVo.setCategoryName(trainingCategory.getName());
        trainingVo.setCategoryColor(trainingCategory.getColor());
        List<Long> trainingProblemIdList = trainingProblemEntityService.getTrainingProblemIdList(training.getId());
        trainingVo.setProblemCount(trainingProblemIdList.size());

        if (userRolesVo != null && trainingValidator.isInTrainingOrAdmin(training, userRolesVo))
        {
            Integer count = trainingProblemEntityService.getUserTrainingACProblemCount(userRolesVo.getUuid(), trainingProblemIdList);
            trainingVo.setAcCount(count);
        } else
        {
            trainingVo.setAcCount(0);
        }

        return trainingVo;
    }

}
