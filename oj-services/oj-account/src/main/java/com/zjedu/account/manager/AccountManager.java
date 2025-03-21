package com.zjedu.account.manager;

import com.zjedu.account.feign.PassportFeignClient;
import com.zjedu.pojo.dto.CheckUsernameDTO;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.CheckUsernameVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


/**
 * @Author Zhong
 * @Create 2025/3/20 18:31
 * @Version 1.0
 * @Description
 */

@Component
public class AccountManager
{
    @Resource
    private PassportFeignClient passportFeignClient;


    public CheckUsernameVO checkUsername(CheckUsernameDTO checkUsernameDTO)
    {
        String username = checkUsernameDTO.getUsername();
        boolean rightUsername = false;

        if (StringUtils.hasText(username))
        {
            username = username.trim();
            UserInfo user = passportFeignClient.getByUsername(username);
            rightUsername = user != null;
        }

        CheckUsernameVO checkUsernameVO = new CheckUsernameVO();
        checkUsernameVO.setUsername(rightUsername);
        return checkUsernameVO;
    }
}
