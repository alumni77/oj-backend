package com.zjedu.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author Zhongs
 * @Create 2025/3/16 21:03
 * @Version 1.0
 */

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayMainApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(GatewayMainApplication.class, args);
    }
}
