package com.zjedu.validator;

import com.zjedu.annotation.HOJAccessEnum;
import com.zjedu.common.exception.AccessException;
import com.zjedu.config.NacosSwitchConfig;
import com.zjedu.config.SwitchConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author Zhong
 * @Create 2025/3/26 20:11
 * @Version 1.0
 * @Description
 */
@Component
public class AccessValidator
{

    @Autowired
    private NacosSwitchConfig nacosSwitchConfig;

    public void validateAccess(HOJAccessEnum hojAccessEnum) throws AccessException
    {
        SwitchConfig switchConfig = nacosSwitchConfig.getSwitchConfig();
        switch (hojAccessEnum)
        {
            case PUBLIC_JUDGE:
                if (!switchConfig.getOpenPublicJudge())
                {
                    throw new AccessException("网站当前未开启题目评测的功能，禁止提交或调试！");
                }
                break;
            case HIDE_NON_CONTEST_SUBMISSION_CODE:
                if (switchConfig.getHideNonContestSubmissionCode())
                {
                    throw new AccessException("网站当前开启了隐藏非比赛提交代码不显示的功能！");
                }
        }
    }
}
