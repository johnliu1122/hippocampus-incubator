package com.liuapi.incubator.repository.datasource.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 花活，实际用途有待考证
 */
@Configuration
public class RoutingDataSourceConfiguration {
    /**
     * 配置 路由数据源
     *
     * 路由数据源可以根据线程内的局部变量来选择相应的数据源
     * @param shard1
     * @param shard2
     * @return
     */
    @Bean
    @Primary
    public RoutingDataSource dataSource(DataSource shard1, DataSource shard2) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("shard1", shard1);
        targetDataSources.put("shard2", shard2);
        return new RoutingDataSource(shard1, targetDataSources);
    }

}