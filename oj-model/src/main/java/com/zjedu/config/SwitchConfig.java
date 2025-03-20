package com.zjedu.config;

import cn.hutool.core.text.UnicodeUtil;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/19 21:06
 * @Version 1.0
 * @Description
 */

@Data
public class SwitchConfig
{
    /**
     * 是否开启公开评测
     */
    private Boolean openPublicJudge = true;

    /**
     * 是否隐藏提交详情的代码（超管不受限制）
     */
    private Boolean hideNonContestSubmissionCode = false;

    /**
     * 提交间隔秒数
     */
    private Integer defaultSubmitInterval = 8;


    private List<String> format2Unicode(List<String> strList)
    {
        if (CollectionUtils.isEmpty(strList))
        {
            return Collections.emptyList();
        }
        List<String> unicodeList = new ArrayList<>();
        for (String str : strList)
        {
            unicodeList.add(UnicodeUtil.toUnicode(str, true));
        }
        return unicodeList;
    }

    private List<String> convertUnicode2Str(List<String> unicodeList)
    {
        if (CollectionUtils.isEmpty(unicodeList))
        {
            return Collections.emptyList();
        }
        List<String> strList = new ArrayList<>();
        for (String str : unicodeList)
        {
            strList.add(UnicodeUtil.toString(str));
        }
        return strList;
    }
}
