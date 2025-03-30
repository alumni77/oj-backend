package com.zjedu.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author Zhong
 * @Create 2025/3/30 13:33
 * @Version 1.0
 * @Description
 */

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages = {"com.zjedu"})
@MapperScan(basePackages = {"com.zjedu.common.mapper"})
public class CommonMainApplication
{
    public static void main(String[] args)
    {
        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
        SpringApplication.run(CommonMainApplication.class, args);
    }
}