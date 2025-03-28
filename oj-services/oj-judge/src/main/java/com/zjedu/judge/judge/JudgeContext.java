package com.zjedu.judge.judge;

import com.zjedu.judge.common.exception.SystemError;
import com.zjedu.judge.dao.UserAcproblemEntityService;
import com.zjedu.judge.judge.entity.LanguageConfig;
import com.zjedu.pojo.dto.TestJudgeReq;
import com.zjedu.pojo.dto.TestJudgeRes;
import com.zjedu.pojo.entity.judge.Judge;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.pojo.entity.user.UserAcproblem;
import com.zjedu.utils.Constants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @Author Zhong
 * @Create 2025/3/25 14:09
 * @Version 1.0
 * @Description
 */

@Component
public class JudgeContext
{

    @Resource
    private JudgeStrategy judgeStrategy;

    @Resource
    private UserAcproblemEntityService userAcproblemEntityService;

    @Resource
    private LanguageConfigLoader languageConfigLoader;

    public Judge Judge(Problem problem, Judge judge)
    {

        // c和c++为一倍时间和空间，其它语言为2倍时间和空间
        LanguageConfig languageConfig = languageConfigLoader.getLanguageConfigByName(judge.getLanguage());
        if (languageConfig.getSrcName() == null
                || (!languageConfig.getSrcName().endsWith(".c")
                && !languageConfig.getSrcName().endsWith(".cpp")))
        {
            problem.setTimeLimit(problem.getTimeLimit() * 2);
            problem.setMemoryLimit(problem.getMemoryLimit() * 2);
        }

        HashMap<String, Object> judgeResult = judgeStrategy.judge(problem, judge);

        Judge finalJudgeRes = new Judge();
        finalJudgeRes.setSubmitId(judge.getSubmitId());
        // 如果是编译失败、提交错误或者系统错误就有错误提醒
        if (judgeResult.get("code") == Constants.Judge.STATUS_COMPILE_ERROR.getStatus() ||
                judgeResult.get("code") == Constants.Judge.STATUS_SYSTEM_ERROR.getStatus() ||
                judgeResult.get("code") == Constants.Judge.STATUS_RUNTIME_ERROR.getStatus() ||
                judgeResult.get("code") == Constants.Judge.STATUS_SUBMITTED_FAILED.getStatus())
        {
            finalJudgeRes.setErrorMessage((String) judgeResult.getOrDefault("errMsg", ""));
        }
        // 设置最终结果状态码
        finalJudgeRes.setStatus((Integer) judgeResult.get("code"));
        // 设置最大时间和最大空间不超过题目限制时间和空间
        // kb
        Integer memory = (Integer) judgeResult.get("memory");
        finalJudgeRes.setMemory(Math.min(memory, problem.getMemoryLimit() * 1024));
        // ms
        Integer time = (Integer) judgeResult.get("time");
        finalJudgeRes.setTime(Math.min(time, problem.getTimeLimit()));
        // score
        finalJudgeRes.setScore((Integer) judgeResult.getOrDefault("score", null));
        // oi_rank_score
        finalJudgeRes.setOiRankScore((Integer) judgeResult.getOrDefault("oiRankScore", null));

        return finalJudgeRes;
    }

    public TestJudgeRes testJudge(TestJudgeReq testJudgeReq)
    {
        // c和c++为一倍时间和空间，其它语言为2倍时间和空间
        LanguageConfig languageConfig = languageConfigLoader.getLanguageConfigByName(testJudgeReq.getLanguage());
        if (languageConfig.getSrcName() == null
                || (!languageConfig.getSrcName().endsWith(".c")
                && !languageConfig.getSrcName().endsWith(".cpp")))
        {
            testJudgeReq.setTimeLimit(testJudgeReq.getTimeLimit() * 2);
            testJudgeReq.setMemoryLimit(testJudgeReq.getMemoryLimit() * 2);
        }
        return judgeStrategy.testJudge(testJudgeReq);
    }

    public Boolean compileSpj(String code, Long pid, String spjLanguage, HashMap<String, String> extraFiles) throws SystemError
    {
        return Compiler.compileSpj(code, pid, spjLanguage, extraFiles);
    }

    public Boolean compileInteractive(String code, Long pid, String interactiveLanguage, HashMap<String, String> extraFiles) throws SystemError
    {
        return Compiler.compileInteractive(code, pid, interactiveLanguage, extraFiles);
    }


    public void updateOtherTable(Long submitId,
                                 Integer status,
                                 String uid,
                                 Long pid,
                                 Integer score,
                                 Integer useTime)
    {
        // 如果是AC,就更新user_acproblem表,
        if (status.intValue() == Constants.Judge.STATUS_ACCEPTED.getStatus())
        {
            userAcproblemEntityService.saveOrUpdate(new UserAcproblem()
                    .setPid(pid)
                    .setUid(uid)
                    .setSubmitId(submitId)
            );
        }
    }
}