package com.zjedu.common.service.impl;

import com.zjedu.common.manager.CommonManager;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.service.CommonService;
import com.zjedu.pojo.entity.problem.Tag;
import com.zjedu.pojo.vo.CaptchaVO;
import com.zjedu.pojo.vo.ProblemTagVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/30 13:41
 * @Version 1.0
 * @Description
 */

@Service
public class CommonServiceImpl implements CommonService
{
    @Resource
    private CommonManager commonManager;

    @Override
    public CommonResult<CaptchaVO> getCaptcha()
    {
        return CommonResult.successResponse(commonManager.getCaptcha());
    }

    @Override
    public CommonResult<List<Tag>> getAllProblemTagsList(String oj)
    {
        return CommonResult.successResponse(commonManager.getAllProblemTagsList(oj));
    }

    @Override
    public CommonResult<List<ProblemTagVO>> getProblemTagsAndClassification(String oj)
    {
        return CommonResult.successResponse(commonManager.getProblemTagsAndClassification(oj));
    }
}
