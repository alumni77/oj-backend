package com.zjedu.common.manager;

import cn.hutool.core.util.IdUtil;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.base.Captcha;
import com.zjedu.pojo.vo.CaptchaVO;
import com.zjedu.utils.RedisUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @Author Zhong
 * @Create 2025/3/30 13:42
 * @Version 1.0
 * @Description
 */

@Component
public class CommonManager
{
    @Resource
    private RedisUtils redisUtil;

    public CaptchaVO getCaptcha()
    {
        ArithmeticCaptcha specCaptcha = new ArithmeticCaptcha(90, 30, 4);
        specCaptcha.setCharType(Captcha.TYPE_DEFAULT);
        // 2位数运算
        specCaptcha.setLen(2);
        String verCode = specCaptcha.text().toLowerCase();
        String key = IdUtil.simpleUUID();
        // 存入redis并设置过期时间为30分钟
        redisUtil.set(key, verCode, 1800);
        // 将key和base64返回给前端
        CaptchaVO captchaVo = new CaptchaVO();
        captchaVo.setImg(specCaptcha.toBase64());
        captchaVo.setCaptchaKey(key);
        return captchaVo;
    }
}
