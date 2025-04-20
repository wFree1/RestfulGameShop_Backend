package org.csugameshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.csugameshop")
@MapperScan(basePackages = "org.csugameshop.mapper")
public class GameShopBackApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameShopBackApplication.class, args);
    }
}