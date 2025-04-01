package com.zjedu.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author Zhong
 * @Create 2025/3/31 15:41
 * @Version 1.0
 * @Description
 */

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages = "com.zjedu")
@MapperScan(basePackages = {"com.zjedu.admin.mapper"})
public class AdminMainApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(AdminMainApplication.class, args);
    }
}