package com.relive;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author ReLive
 * @Date 2021/1/17-16:50
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = "com.relive.mapper")
public class ResourceServer {
    public static void main(String[] args) {
        SpringApplication.run(ResourceServer.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
