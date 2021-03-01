package org.wdzl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(value = "org.wdzl.dao")
@ComponentScan(basePackages = {"org.wdzl","org.n3r.idworker"})
public class ImBirdSysApplication{

    public static void main(String[] args) {
        SpringApplication.run(ImBirdSysApplication.class, args);
    }

}
