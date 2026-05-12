package com.hspedu.hspliving.commodity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 *
 */
@SpringBootApplication
@EnableDiscoveryClient //启用Nacos服务发现功能，可以注册和发现服务
public class HsplivingCommodityApplication {
    public static void main(String[] args) {
        SpringApplication.run(HsplivingCommodityApplication.class, args);
    }
}
