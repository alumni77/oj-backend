package com.zjedu.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

/**
 * @Author Zhongs
 * @Create 2025/3/16 21:03
 * @Version 1.0
 */

@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.zjedu"})
public class GatewayMainApplication
{
    public static void main(String[] args)
    {
        ConfigurableApplicationContext context = SpringApplication.run(GatewayMainApplication.class, args);
        String[] filterNames = context.getBeanNamesForType(GlobalFilter.class);
        System.out.println("注册的全局过滤器: " + Arrays.toString(filterNames));
    }
}
