package org.dreamlife.hippocampus.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @auther 柳俊阳
 * @github https://github.com/johnliu1122/
 * @csdn https://blog.csdn.net/qq_35695616
 * @email johnliu1122@163.com
 * @birthday 11-22
 * @date 2020/3/18
 */
@Slf4j
@SpringBootApplication
public class RedisCacheServer {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RedisCacheServer.class);
    }
}
