package com.zjedu.pojo.vo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Author Zhong
 * @Create 2025/4/6 16:16
 * @Version 1.0
 * @Description
 */

@RefreshScope
@Data
@Component
public class ConfigVO
{
    // 数据库配置
    @Value("${MYSQL_USERNAME}")
    private String mysqlUsername;

    @Value("${MYSQL_PASSWORD}")
    private String mysqlPassword;

    @Value("${MYSQL_DATABASE}")
    private String mysqlDBName;

    @Value("${MYSQL_HOST}")
    private String mysqlHost;

    @Value("${hoj.db.public-host:172.20.0.3}")
    private String mysqlPublicHost;

    @Value("${MYSQL_PORT}")
    private Integer mysqlPort;

    @Value("${MYSQL_PORT}")
    private Integer mysqlPublicPort;

    // 判题服务token
    @Value("${hoj.judge.token:judge_token}")
    private String judgeToken;

    // 缓存配置
    @Value("${REDIS_HOST}")
    private String redisHost;

    @Value("${REDIS_PORT}")
    private Integer redisPort;

    @Value("${REDIS_PASSWORD}")
    private String redisPassword;

    // jwt配置
    @Value("${JWT_SECRET")
    private String tokenSecret;

    @Value("${hoj.jwt.expire:86500}")
    private String tokenExpire;

    @Value("${hoj.jwt.checkRefreshExpire:43200}")
    private String checkRefreshExpire;
}
