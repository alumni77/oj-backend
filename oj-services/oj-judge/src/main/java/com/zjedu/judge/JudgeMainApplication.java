package com.zjedu.judge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author Zhong
 * @Create 2025/3/24 18:54
 * @Version 1.0
 * @Description
 */
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages = {"com.zjedu"})
@MapperScan(basePackages = {"com.zjedu.judge.mapper"})
public class JudgeMainApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(JudgeMainApplication.class, args);
    }
}
