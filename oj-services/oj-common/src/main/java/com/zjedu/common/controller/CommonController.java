package com.zjedu.common.controller;

import com.zjedu.annotation.AnonApi;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.service.CommonService;
import com.zjedu.pojo.entity.problem.CodeTemplate;
import com.zjedu.pojo.entity.problem.Language;
import com.zjedu.pojo.entity.problem.Tag;
import com.zjedu.pojo.entity.training.TrainingCategory;
import com.zjedu.pojo.vo.CaptchaVO;
import com.zjedu.pojo.vo.ProblemTagVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
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

    @GetMapping("/get-training-category")
    @AnonApi
    public CommonResult<List<TrainingCategory>> getTrainingCategory()
    {
        return commonService.getTrainingCategory();
    }

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

    @GetMapping("/get-problem-tags")
    @AnonApi
    public CommonResult<Collection<Tag>> getProblemTags(Long pid)
    {
        return commonService.getProblemTags(pid);
    }

    @GetMapping("/languages")
    @AnonApi
    public CommonResult<List<Language>> getLanguages(@RequestParam(value = "pid", required = false) Long pid,
                                                     @RequestParam(value = "all", required = false) Boolean all)
    {
        return commonService.getLanguages(pid, all);
    }

    @GetMapping("/get-problem-languages")
    @AnonApi
    public CommonResult<Collection<Language>> getProblemLanguages(@RequestParam("pid") Long pid)
    {
        return commonService.getProblemLanguages(pid);
    }

    @GetMapping("/get-problem-code-template")
    @AnonApi
    public CommonResult<List<CodeTemplate>> getProblemCodeTemplate(@RequestParam("pid") Long pid)
    {
        return commonService.getProblemCodeTemplate(pid);
    }

}
