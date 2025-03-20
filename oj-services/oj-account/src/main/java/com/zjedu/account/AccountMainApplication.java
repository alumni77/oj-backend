package com.zjedu.account;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author Zhong
 * @Create 2025/3/20 15:57
 * @Version 1.0
 * @Description
 */

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"com.zjedu"})
@MapperScan(basePackages = {"com.zjedu.account.mapper"})
public class AccountMainApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(AccountMainApplication.class, args);
    }
}
