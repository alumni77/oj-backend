package com.zjedu.admin.service;

import com.zjedu.common.result.CommonResult;
import com.zjedu.pojo.entity.judge.Judge;

/**
 * @Author Zhong
 * @Create 2025/4/1 20:47
 * @Version 1.0
 * @Description
 */

public interface RejudgeService
{
    CommonResult<Judge> rejudge(Long submitId);
}
