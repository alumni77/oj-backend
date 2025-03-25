package com.zjedu.judgeserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
* @Author Zhong
* @Create 2025/3/25 22:10
* @Version 1.0
* @Description 
*/

@EnableDiscoveryClient
//@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages = {"com.zjedu"})
//@MapperScan(basePackages = {"com.zjedu.account.mapper"})
public class JudgeServeMainApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(JudgeServeMainApplication.class, args);
    }
}
