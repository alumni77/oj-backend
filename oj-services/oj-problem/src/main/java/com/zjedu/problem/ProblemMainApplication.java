package com.zjedu.problem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author Zhong
 * @Create 2025/3/28 20:28
 * @Version 1.0
 * @Description
 */

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages = {"com.zjedu"})
@MapperScan(basePackages = {"com.zjedu.problem.mapper"})
public class ProblemMainApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ProblemMainApplication.class, args);
    }
}
