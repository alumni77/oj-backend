package com.zjedu.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author Zhong
 * @Create 2025/4/6 15:56
 * @Version 1.0
 * @Description
 */

@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebConfigDTO
{

    /**
     * 基础 URL
     */
    private String baseUrl;

    /**
     * 网站名称
     */
    private String name;

    /**
     * 网站简称
     */
    private String shortName;

    /**
     * 网站简介
     */
    private String description;

    /**
     * 是否允许注册
     */
    private Boolean register;

    /**
     * 备案名
     */
    private String recordName;

    /**
     * 备案地址
     */
    private String recordUrl;

    /**
     * 项目名
     */
    private String projectName;

    /**
     * 项目地址
     */
    private String projectUrl;
}

