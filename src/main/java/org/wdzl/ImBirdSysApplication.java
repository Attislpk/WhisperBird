package org.wdzl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement // 启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
@SpringBootApplication
@MapperScan(value = "org.wdzl.dao")
@ComponentScan(basePackages = {"org.wdzl","org.n3r.idworker"})
public class ImBirdSysApplication{

    @Bean
    public SpringUtil getSpingUtil() {
        return new SpringUtil();
    }

    public static void main(String[] args) {
        SpringApplication.run(ImBirdSysApplication.class, args);
    }
}
