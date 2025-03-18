package com.zjedu.passport;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author Zhong
 * @Create 2025/3/16 21:16
 * @Version 1.0
 */

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"com.zjedu"})
@MapperScan({"com.zjedu.passport.mapper"}) // Scan only mapper package
public class PassportMainApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(PassportMainApplication.class, args);
    }
}