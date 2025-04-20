package com.zjedu.admin.controller;

import com.zjedu.admin.service.AdminTrainingCategoryService;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.entity.training.TrainingCategory;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/4/2 20:50
 * @Version 1.0
 * @Description
 */

@RestController
@RequestMapping("/training/category")
public class AdminTrainingCategoryController
{
    @Resource
    private AdminTrainingCategoryService adminTrainingCategoryService;

    @GetMapping
    private  CommonResult<List<TrainingCategory>> getTrainingCategoryList()
    {
        return adminTrainingCategoryService.getTrainingCategoryList();
    }

    @PostMapping
    public CommonResult<TrainingCategory> addTrainingCategory(@RequestBody TrainingCategory trainingCategory)
    {
        return adminTrainingCategoryService.addTrainingCategory(trainingCategory);
    }

    @PutMapping
    public CommonResult<Void> updateTrainingCategory(@RequestBody TrainingCategory trainingCategory)
    {
        return adminTrainingCategoryService.updateTrainingCategory(trainingCategory);
    }

    @DeleteMapping
    public CommonResult<Void> deleteTrainingCategory(@RequestParam("id") Long id)
    {
        return adminTrainingCategoryService.deleteTrainingCategory(id);
    }


}
