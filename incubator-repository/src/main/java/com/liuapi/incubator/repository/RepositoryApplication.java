package com.liuapi.incubator.repository;

import com.liuapi.incubator.repository.mapper.UserMapper;
import com.liuapi.incubator.repository.model.User;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@MapperScan(basePackages = "com.liuapi.incubator.repository.mapper")
@SpringBootApplication
public class RepositoryApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RepositoryApplication.class);
        UserMapper userMapper = context.getBean(UserMapper.class);
        userMapper.deleteUserById(1L);
        userMapper.addUser(new User()
                .setId(1L)
                .setUsername("张三")
                .setAddress("浙江省湖州市德清县武康镇"));
        User user = userMapper.load(1L);
        System.out.println(user);
    }
}
