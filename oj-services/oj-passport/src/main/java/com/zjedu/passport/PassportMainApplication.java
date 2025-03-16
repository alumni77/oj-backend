package com.zjedu.passport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author Zhong
 * @Create 2025/3/16 21:16
 * @Version 1.0
 */

@EnableDiscoveryClient
@SpringBootApplication
public class PassportMainApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(PassportMainApplication.class, args);
    }
}
