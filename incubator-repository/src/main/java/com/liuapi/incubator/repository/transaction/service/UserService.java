package com.liuapi.incubator.repository.transaction.service;

import com.liuapi.incubator.repository.transaction.mapper.UserMapper;
import com.liuapi.incubator.repository.transaction.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    @Autowired
    private UserOpLogService userOpLogService;

    @Transactional
    public void flushUser(){
        userMapper.deleteUserById(1L);
        userMapper.addUser(new User()
                .setId(1L)
                .setUsername("张三")
                .setAddress("浙江省湖州市德清县武康镇"));
        User user = userMapper.load(1L);
        System.out.println(user);
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void flushUserWithExceptionAndPropagationRequiredNew() throws Exception {
        User user = new User()
                .setId(null) // id is null then use mysql auto increment id
                .setUsername("bug")
                .setAddress("浙江省湖州市德清县武康镇");
        userMapper.addUser(user);
        userOpLogService.insertLogAndCreateNewTransaction(user.getId(),"createUser");
        throw new Exception("occur exception after add user bug");
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void flushUserWithExceptionAndPropagationRequired() throws Exception {
        User user = new User()
                .setId(null) // id is null then use mysql auto increment id
                .setUsername("bug")
                .setAddress("浙江省湖州市德清县武康镇");
        userMapper.addUser(user);
        userOpLogService.insertLogAndIncludedIntoExistingTransaction(user.getId(),"createUser");
        throw new Exception("occur exception after add user bug");
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void flushUserWithExceptionAndPropagationNotSupport() throws Exception {
        User user = new User()
                .setId(null) // id is null then use mysql auto increment id
                .setUsername("bug")
                .setAddress("浙江省湖州市德清县武康镇");
        userMapper.addUser(user);
        userOpLogService.insertLogWithoutTransaction(user.getId(),"createUser");
        throw new Exception("occur exception after add user bug");
    }
}
