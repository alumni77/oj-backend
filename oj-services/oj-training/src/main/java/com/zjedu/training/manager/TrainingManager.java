package com.zjedu.training.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.TrainingVO;
import com.zjedu.training.dao.TrainingEntityService;
import com.zjedu.training.feign.PassportFeignClient;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

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
}
