package com.zjedu.common.service;

import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.entity.problem.Tag;
import com.zjedu.pojo.vo.CaptchaVO;
import com.zjedu.pojo.vo.ProblemTagVO;

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
}
