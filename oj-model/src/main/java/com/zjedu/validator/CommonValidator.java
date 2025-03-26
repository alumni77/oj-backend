package com.zjedu.validator;

import cn.hutool.core.util.StrUtil;
import com.zjedu.common.exception.StatusFailException;
import org.springframework.stereotype.Component;

/**
 * @Author Zhong
 * @Create 2025/3/23 16:37
 * @Version 1.0
 * @Description
 */

@Component
public class CommonValidator {

    public void validateContent(String content, String item) throws StatusFailException
    {
        if (StrUtil.isBlank(content)) {
            throw new StatusFailException(item + "的内容不能为空！");
        }
        if (content.length() > 65535) {
            throw new StatusFailException(item + "的内容长度超过限制，请重新编辑！");
        }
    }

    public void validateContent(String content, String item, int length) throws StatusFailException {
        if (StrUtil.isBlank(content)) {
            throw new StatusFailException(item + "的内容不能为空！");
        }
        if (content.length() > length) {
            throw new StatusFailException(item + "的内容长度超过限制，请重新编辑！");
        }
    }

    public void validateContentLength(String content, String item, int length) throws StatusFailException {
        if (content != null && content.length() > length) {
            throw new StatusFailException(item + "的内容长度超过限制，请重新编辑！");
        }
    }



    public void validateNotEmpty(Object value, String item) throws StatusFailException {
        if (value == null) {
            throw new StatusFailException(item + "不能为空");
        }
        if (value instanceof String){
            if (StrUtil.isBlank((String)value)){
                throw new StatusFailException(item + "不能为空");
            }
        }
    }
}
