package com.liuapi.incubator.repository.transaction;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.liuapi.incubator.repository.propagation.mapper.UserMapper;
import com.liuapi.incubator.repository.propagation.model.User;
import com.liuapi.incubator.repository.propagation.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 编程式事务管理
 */
@Service
public class UserService2 {
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Resource
    private UserMapper userMapper;
    @Autowired
    private LogService logService;

    public void addUser() {
        transactionTemplate.execute(
                status -> {
                    userMapper.addUser(new User()
                            .setId(null) // id is null then use mysql auto increment id
                            .setUsername("newUserM2")
                            .setAddress("浙江省湖州市德清县武康镇"));
                    return null;
                }
        );
    }
}
