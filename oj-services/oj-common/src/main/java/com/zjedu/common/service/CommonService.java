package com.zjedu.common.service;

import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.entity.problem.CodeTemplate;
import com.zjedu.pojo.entity.problem.Language;
import com.zjedu.pojo.entity.problem.Tag;
import com.zjedu.pojo.entity.training.TrainingCategory;
import com.zjedu.pojo.vo.CaptchaVO;
import com.zjedu.pojo.vo.ProblemTagVO;

import java.util.Collection;
import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/30 13:41
 * @Version 1.0
 * @Description
 */

public interface CommonService
{
    CommonResult<CaptchaVO> getCaptcha();

    CommonResult<List<Tag>> getAllProblemTagsList(String oj);

    CommonResult<List<ProblemTagVO>> getProblemTagsAndClassification(String oj);

    CommonResult<Collection<Tag>> getProblemTags(Long pid);

    CommonResult<List<Language>> getLanguages(Long pid, Boolean all);

    CommonResult<Collection<Language>> getProblemLanguages(Long pid);

    CommonResult<List<CodeTemplate>> getProblemCodeTemplate(Long pid);

    CommonResult<List<TrainingCategory>> getTrainingCategory();
}
