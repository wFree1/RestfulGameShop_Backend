package com.example.gameshoprestful;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("org.csu.gameshop.entity")
@EnableJpaRepositories(
        basePackages = "com.example.gameshoprestful.persistence",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = ".*Mapper$" // 排除MyBatis组件
        )
)
@MapperScan(
        value = "com.example.gameshoprestful.mapper"// MyBatis专用包
)
public class GameShopRestfulApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameShopRestfulApplication.class, args);
    }

}
