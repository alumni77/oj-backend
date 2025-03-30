package com.zjedu.common.controller;

import com.zjedu.annotation.AnonApi;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.service.CommonService;
import com.zjedu.pojo.entity.problem.Tag;
import com.zjedu.pojo.vo.CaptchaVO;
import com.zjedu.pojo.vo.ProblemTagVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/30 13:41
 * @Version 1.0
 * @Description
 */

@RestController
public class CommonController
{

    @Resource
    private CommonService commonService;

    @GetMapping("/captcha")
    @AnonApi
    public CommonResult<CaptchaVO> getCaptcha()
    {
        return commonService.getCaptcha();
    }

    //TODO 需要训练模块相关服务
//    @GetMapping("/get-training-category")
//    @AnonApi
//    public CommonResult<List<TrainingCategory>> getTrainingCategory() {
//        return commonService.getTrainingCategory();
//    }

    @GetMapping("/get-all-problem-tags")
    @AnonApi
    public CommonResult<List<Tag>> getAllProblemTagsList(@RequestParam(value = "oj", defaultValue = "ME") String oj)
    {
        return commonService.getAllProblemTagsList(oj);
    }

    @GetMapping("/get-problem-tags-and-classification")
    @AnonApi
    public CommonResult<List<ProblemTagVO>> getProblemTagsAndClassification(@RequestParam(value = "oj", defaultValue = "ME") String oj)
    {
        return commonService.getProblemTagsAndClassification(oj);
    }

}
