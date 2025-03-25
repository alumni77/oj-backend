package com.zjedu.judge.service;

import com.zjedu.judge.common.exception.SystemError;
import com.zjedu.pojo.dto.TestJudgeReq;
import com.zjedu.pojo.dto.TestJudgeRes;
import com.zjedu.pojo.entity.judge.Judge;

import java.util.HashMap;

/**
 * @Author Zhong
 * @Create 2025/3/24 21:45
 * @Version 1.0
 * @Description
 */

public interface JudgeService
{
    void judge(Judge judge);

    TestJudgeRes testJudge(TestJudgeReq testJudgeReq);

    Boolean compileSpj(String code, Long pid, String spjLanguage, HashMap<String, String> extraFiles) throws SystemError;
}
