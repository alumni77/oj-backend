package com.zjedu.file;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author Zhong
 * @Create 2025/3/30 19:49
 * @Version 1.0
 * @Description
 */

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@MapperScan(basePackages = {"com.zjedu.file.mapper"})
public class FileMainApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(FileMainApplication.class, args);
    }
}