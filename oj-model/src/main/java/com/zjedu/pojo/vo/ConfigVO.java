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
    //TODO: 这里的配置项需要根据实际情况进行修改
    // 数据库配置
    @Value("${MYSQL_USERNAME:root}")
    private String mysqlUsername;

    @Value("${MYSQL_PASSWORD:123456}")
    private String mysqlPassword;

    @Value("${MYSQL_DATABASE:hoj}")
    private String mysqlDBName;

    @Value("${MYSQL_HOST:localhost}")
    private String mysqlHost;

    @Value("${hoj.db.public-host:172.20.0.3}")
    private String mysqlPublicHost;

    @Value("${MYSQL_PORT:3306}")
    private Integer mysqlPort;

    @Value("${MYSQL_PORT:3306}")
    private Integer mysqlPublicPort;

    // 判题服务token
    @Value("${hoj.judge.token:no_judge_token}")
    private String judgeToken;

    // 缓存配置
    @Value("${REDIS_HOST:localhost}")
    private String redisHost;

    @Value("${REDIS_PORT:6379}")
    private Integer redisPort;

    @Value("${REDIS_PASSWORD:123456}")
    private String redisPassword;

    // jwt配置
    @Value("${JWT_SECRET:SmMUX4cbYjmr5sMkTS7DaQ7NYhEBerWJtNPehhJ/1PxzEJOBEV6IbUYwV/PKb+uiooDtdgIBLxjzpV6Oq0OIyQ==\n}")
    private String tokenSecret;

    @Value("${hoj.jwt.expire:86500}")
    private String tokenExpire;

    @Value("${hoj.jwt.checkRefreshExpire:43200}")
    private String checkRefreshExpire;
}
