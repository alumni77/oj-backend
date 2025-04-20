package com.zjedu.validator;

import cn.hutool.core.util.StrUtil;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.pojo.entity.problem.Problem;
import com.zjedu.utils.Constants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @Author Zhong
 * @Create 2025/4/2 14:48
 * @Version 1.0
 * @Description
 */

@Component
public class ProblemValidator
{

    @Resource
    private CommonValidator commonValidator;

    public void validateProblem(Problem problem) throws StatusFailException
    {
        if (problem == null)
        {
            throw new StatusFailException("题目的配置项不能为空！");
        }

        if (StrUtil.isBlank(problem.getProblemId()))
        {
            throw new StatusFailException("题目的展示ID不能为空！");
        }

        defaultValidate(problem);

        if (problem.getTimeLimit() <= 0)
        {
            throw new StatusFailException("题目的时间限制不能小于等于0！");
        }
        if (problem.getMemoryLimit() <= 0)
        {
            throw new StatusFailException("题目的内存限制不能小于等于0！");
        }
        if (problem.getStackLimit() <= 0)
        {
            throw new StatusFailException("题目的栈限制不能小于等于0！");
        }
    }

    private void defaultValidate(Problem problem) throws StatusFailException
    {
        Constants.ProblemType type = Constants.ProblemType.getProblemType(problem.getType());
        if (type == null)
        {
            throw new StatusFailException("题目的类型必须为ACM(0), OI(1)！");
        }

        Constants.ProblemAuth auth = Constants.ProblemAuth.getProblemAuth(problem.getAuth());
        if (auth == null)
        {
            throw new StatusFailException("题目的权限必须为公开题目(1), 隐藏题目(2), 比赛题目(3)！");
        }

        Constants.JudgeMode judgeMode = Constants.JudgeMode.getJudgeMode(problem.getJudgeMode());
        if (judgeMode == null)
        {
            throw new StatusFailException("题目的判题模式必须为普通判题(default), 特殊判题(spj), 交互判题(interactive)！");
        }

        Constants.JudgeCaseMode judgeCaseMode = Constants.JudgeCaseMode.getJudgeCaseMode(problem.getJudgeCaseMode());
        if (judgeCaseMode == null)
        {
            throw new StatusFailException("题目的用例模式不正确！");
        }
        if (type == Constants.ProblemType.ACM)
        {
            if (judgeCaseMode != Constants.JudgeCaseMode.DEFAULT
                    && judgeCaseMode != Constants.JudgeCaseMode.ERGODIC_WITHOUT_ERROR)
            {
                throw new StatusFailException("题目的用例模式错误，ACM类型的题目只能为默认模式(default)、遇错止评(ergodic_without_error)！");
            }
        } else
        {
            if (judgeCaseMode != Constants.JudgeCaseMode.DEFAULT
                    && judgeCaseMode != Constants.JudgeCaseMode.SUBTASK_AVERAGE
                    && judgeCaseMode != Constants.JudgeCaseMode.SUBTASK_LOWEST)
            {
                throw new StatusFailException("题目的用例模式错误，OI类型的题目只能为默认模式(default)、" +
                        "子任务（最低得分）(subtask_lowest)、 子任务（平均得分）(subtask_average)！");
            }
        }
    }

    public void validateProblemUpdate(Problem problem) throws StatusFailException
    {
        if (problem == null)
        {
            throw new StatusFailException("题目的配置项不能为空！");
        }
        if (problem.getId() == null)
        {
            throw new StatusFailException("题目的id不能为空！");
        }
        validateProblem(problem);
    }
}