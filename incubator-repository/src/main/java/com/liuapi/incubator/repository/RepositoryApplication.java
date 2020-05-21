package com.liuapi.incubator.repository;

import com.liuapi.incubator.repository.annocation.StrategyBeanScan;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@StrategyBeanScan(basePackages = {"com.liuapi.incubator.repository"})
@MapperScan(basePackages = {"com.liuapi.incubator.repository.transaction.mapper",
        "com.liuapi.incubator.repository.scene"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Slf4j
public class RepositoryApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RepositoryApplication.class);
    }
}
