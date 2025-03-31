package com.zjedu.file.manager;

import com.alibaba.excel.EasyExcel;
import com.zjedu.pojo.vo.ExcelUserVO;
import com.zjedu.utils.RedisUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author Zhong
 * @Create 2025/3/31 14:35
 * @Version 1.0
 * @Description
 */

@Component
@Slf4j
public class UserFileManager
{
    @Resource
    private RedisUtils redisUtils;

    public void generateUserExcel(String key, HttpServletResponse response) throws IOException
    {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode(key, StandardCharsets.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        response.setHeader("Content-Type", "application/xlsx");
        EasyExcel.write(response.getOutputStream(), ExcelUserVO.class).sheet("用户数据").doWrite(getGenerateUsers(key));
    }

    private List<ExcelUserVO> getGenerateUsers(String key)
    {
        List<ExcelUserVO> result = new LinkedList<>();
        Map<Object, Object> userInfo = redisUtils.hmget(key);
        for (Object hashKey : userInfo.keySet())
        {
            String username = (String) hashKey;
            String password = (String) userInfo.get(hashKey);
            result.add(new ExcelUserVO().setUsername(username).setPassword(password));
        }
        return result;
    }
}
