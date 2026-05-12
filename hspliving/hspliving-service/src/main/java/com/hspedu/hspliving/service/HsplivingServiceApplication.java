package com.hspedu.hspliving.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 主启动类
 */
@SpringBootApplication
@EnableDiscoveryClient //启用Nacos服务发现功能，可以注册和发现服务
public class HsplivingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(HsplivingServiceApplication.class,args);
    }
}
