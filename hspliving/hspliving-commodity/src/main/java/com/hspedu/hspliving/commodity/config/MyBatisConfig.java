package com.hspedu.hspliving.commodity.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 通过MyBatis-plus插件实现分页显示的功能
 */
@Configuration //这是一个配置类
@EnableTransactionManagement //开启声明式事务管理功能
@MapperScan("com.hspedu.hspliving.commodity.dao") //扫描并注册指定的Mapper接口/DAO层
public class MyBatisConfig {
    //该方法完成分页功能
    @Bean //注入到spring容器
    public PaginationInterceptor paginationInterceptor() {
        //引入分页插件
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        //设置分页规则
        paginationInterceptor.setOverflow(true);  //假设数据到第10页结束，如果为true那10页之后的数据不会是空白而是第10页的数据
        paginationInterceptor.setLimit(100); //单页最多显示100条数据，小于0时不受限制

        return paginationInterceptor;
    };


}
