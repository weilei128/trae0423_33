package com.jewelry;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jewelry.mapper")
public class JewelrySystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(JewelrySystemApplication.class, args);
    }
}
