package com.zjedu.training;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author Zhong
 * @Create 2025/3/30 15:30
 * @Version 1.0
 * @Description
 */

@EnableDiscoveryClient
//@EnableFeignClients
@SpringBootApplication
@MapperScan(basePackages = {"com.zjedu.training.mapper"})
@ComponentScan(basePackages = "com.zjedu")
public class TrainingMainApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(TrainingMainApplication.class, args);
    }
}