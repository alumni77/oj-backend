package com.zjedu.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.admin.service.AdminTrainingProblemService;
import com.zjedu.admin.service.AdminTrainingService;
import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.dto.TrainingDTO;
import com.zjedu.pojo.entity.training.Training;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Zhong
 * @Create 2025/4/2 21:13
 * @Version 1.0
 * @Description
 */

@RestController
@RequestMapping("/training")
public class AdminTrainingController
{
    @Resource
    private AdminTrainingService adminTrainingService;

    @Resource
    private AdminTrainingProblemService adminTrainingProblemService;

    @GetMapping("/get-training-list")
    public CommonResult<IPage<Training>> getTrainingList(@RequestParam(value = "limit", required = false) Integer limit,
                                                         @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                         @RequestParam(value = "keyword", required = false) String keyword)
    {
        return adminTrainingService.getTrainingList(limit, currentPage, keyword);
    }

    @GetMapping
    public CommonResult<TrainingDTO> getTraining(@RequestParam("tid") Long tid)
    {
        return adminTrainingService.getTraining(tid);
    }

    @DeleteMapping
    public CommonResult<Void> deleteTraining(@RequestParam("id") Long id)
    {
        return adminTrainingService.deleteTraining(id);
    }

    @PostMapping
    public CommonResult<Void> addTraining(@RequestBody TrainingDTO trainingDto)
    {
        return adminTrainingService.addTraining(trainingDto);
    }

    @PutMapping
    public CommonResult<Void> updateTraining(@RequestBody TrainingDTO trainingDto)
    {
        return adminTrainingService.updateTraining(trainingDto);
    }

    @PutMapping("/change-training-status")
    public CommonResult<Void> changeTrainingStatus(@RequestParam(value = "tid", required = true) Long tid,
                                                   @RequestParam(value = "author", required = true) String author,
                                                   @RequestParam(value = "status", required = true) Boolean status)
    {
        return adminTrainingService.changeTrainingStatus(tid, author, status);
    }

}
