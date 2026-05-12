package com.hspedu.hspliving.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient //启动Nacos服务发现
public class HsplivingGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(HsplivingGatewayApplication.class,args);
    }
}
