package com.zjedu.training.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.annotation.AnonApi;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.vo.TrainingVO;
import com.zjedu.training.service.TrainingService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Zhong
 * @Create 2025/3/30 15:36
 * @Version 1.0
 * @Description
 */


@RestController
public class TrainingController
{
    @Resource
    private TrainingService trainingService;

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
    @GetMapping("/get-training-list")
    @AnonApi
    public CommonResult<IPage<TrainingVO>> getTrainingList(@RequestParam(value = "limit", required = false) Integer limit,
                                                           @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                           @RequestParam(value = "keyword", required = false) String keyword,
                                                           @RequestParam(value = "categoryId", required = false) Long categoryId,
                                                           @RequestParam(value = "auth", required = false) String auth)
    {

        return trainingService.getTrainingList(limit, currentPage, keyword, categoryId, auth);
    }

}
