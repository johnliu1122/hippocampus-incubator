package com.liuapi.incubator.repository.datasource.druid;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 这里往Ioc容器中注入了两个DateSource，逻辑上是两个分片
 * 在配置上这两个都不是Primary的，而Mybatis框架只会使用一个Primary的DateSource
 * 这个项目中Primary的数据源是 com.liuapi.incubator.repository.datasource.route.RoutingDataSource
 * 这个RoutingDataSource可以认为是一个代理模式的变体，代理了这两个DateSource
 */
@Configuration
public class DruidConfiguration {
    /**
     * 如果只想用druid的数据源，不想用druid的控制台
     * 可以直接 return new DruidDataSource();
     * @return DruidDataSource
     */
    @Bean
    @ConfigurationProperties("spring.datasource.shard1.druid")
    public DataSource shard1() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.shard2.druid")
    public DataSource shard2() {
        return DruidDataSourceBuilder.create().build();
    }
}
