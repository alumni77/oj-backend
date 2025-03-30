package com.zjedu.training.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjedu.pojo.entity.training.Training;
import com.zjedu.pojo.vo.TrainingVO;

/**
 * @Author Zhong
 * @Create 2025/3/30 15:44
 * @Version 1.0
 * @Description
 */

public interface TrainingEntityService extends IService<Training>
{
    Page<TrainingVO> getTrainingList(int limit,
                                     int currentPage,
                                     Long categoryId,
                                     String auth,
                                     String keyword,
                                     String currentUid);
}
