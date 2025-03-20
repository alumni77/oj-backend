package com.zjedu.config;

import com.zjedu.utils.IpUtils;
import lombok.Data;

/**
 * @Author Zhong
 * @Create 2025/3/19 21:09
 * @Version 1.0
 * @Description
 */

@Data
public class WebConfig
{
    // 网站前端显示配置
    private String baseUrl = "http://" + IpUtils.getServiceIp();

    private String name = "Online Judge";

    private String shortName = "OJ";

    private String description;

    private Boolean register = true;

    private String recordName;

    private String recordUrl;

    private String projectName = "OJ";

    private String projectUrl = "https://gitee.com/himitzh0730/hoj";
}
