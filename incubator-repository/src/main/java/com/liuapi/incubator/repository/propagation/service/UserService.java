package com.liuapi.incubator.repository.propagation.service;

import com.liuapi.incubator.repository.propagation.mapper.UserMapper;
import com.liuapi.incubator.repository.propagation.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
@Slf4j
@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    @Autowired
    private LogService logService;

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

    /**
     * require_new的传播级别下
     * 调用insertLogWithNewTransaction会获取一个新的连接，然后在新连接上开启事务
     *
     * require_new与not_support传播级别的异同点
     * 相同点：都会获取一个新的连接
     * 不同点：require_new会给新的连接开启一下事务，而not_support则不会开启事务
     *
     * 2020-05-11T15:59:54.738775Z	  332 Query	SET autocommit=0
     * 2020-05-11T15:59:54.746931Z	  332 Query	insert into user (id,username,address,created_at) values (null,'bug','浙江省湖州市德清县武康镇',now())
     * 2020-05-11T15:59:54.758261Z	  331 Query	SET autocommit=0
     * 2020-05-11T15:59:54.766241Z	  331 Query	insert into log (id,uid,`value`,created_at) values (null,5,'createUser',now())
     * 2020-05-11T15:59:54.769402Z	  331 Query	commit
     * 2020-05-11T15:59:54.773194Z	  331 Query	SET autocommit=1
     * 2020-05-11T15:59:54.775719Z	  332 Query	rollback
     * 2020-05-11T15:59:54.778077Z	  332 Query	SET autocommit=1
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addUserWithExceptionAndPropagationRequiredNew() throws Exception {
        User user = new User()
                .setId(null) // id is null then use mysql auto increment id
                .setUsername("bug")
                .setAddress("浙江省湖州市德清县武康镇");
        userMapper.addUser(user);
        // require_new 会获取一个新的数据库连接来创建一个独立的事务
        logService.insertLogWithNewTransaction(user.getId(),"createUser");
        throw new Exception("occur exception after add user bug");
    }

    /**
     * not_support的传播级别下
     * 调用insertLogWithoutTransaction会获取一个新的连接，然后新连接上直接执行sql并不开启事务
     * 2020-05-11T16:11:13.263292Z	  337 Query	SET autocommit=0
     * 2020-05-11T16:11:13.272977Z	  337 Query	insert into user (id,username,address,created_at) values (null,'bug','浙江省湖州市德清县武康镇',now())
     * 2020-05-11T16:11:13.285245Z	  336 Query	insert into log (id,uid,`value`,created_at) values (null,7,'createUser',now())
     * 2020-05-11T16:11:13.292104Z	  337 Query	rollback
     * 2020-05-11T16:11:13.293930Z	  337 Query	SET autocommit=1
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addUserWithExceptionAndPropagationNotSupport() throws Exception {
        User user = new User()
                .setId(null) // id is null then use mysql auto increment id
                .setUsername("bug")
                .setAddress("浙江省湖州市德清县武康镇");
        userMapper.addUser(user);
        logService.insertLogNotSupportTransaction(user.getId(),"createUser");
        throw new Exception("occur exception after add user bug");
    }

    /**
     * require的传播级别下
     * insertLogWithRequireTransaction会需要一个事务，如果当前数据库连接已开启了事务，就直接复用这个连接（相当于复用了当前的事务）
     *
     * require/support/mandatory/never传播级别的异同点
     * 当前连接开启事务就用者，当前连接没有开启事务就开启一下事务： require
     * 当前连接开启事务就用着，当前连接没有开启事务也就用着： support[support就表示怎么都行]
     * 当前连接开启事务就用者，当前连接没有开启事务就报错：mandatory
     * 当前连接开启事务就报错 never
     *
     * 不同点：require是当前数据连接没有开启类事务就开启事务，support是当前事务没有开始事务也不会开启事务
     * 2020-05-11T16:05:21.960981Z	  337 Query	SET autocommit=0
     * 2020-05-11T16:05:22.696062Z	  337 Query	insert into user (id,username,address,created_at) values (null,'bug','浙江省湖州市德清县武康镇',now())
     * 2020-05-11T16:05:22.831591Z	  337 Query	insert into log (id,uid,`value`,created_at) values (null,6,'createUser',now())
     * 2020-05-11T16:05:22.843474Z	  337 Query	rollback
     * 2020-05-11T16:05:22.849319Z	  337 Query	SET autocommit=1
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addUserWithExceptionAndPropagationRequired() throws Exception {
        User user = new User()
                .setId(null) // id is null then use mysql auto increment id
                .setUsername("bug")
                .setAddress("浙江省湖州市德清县武康镇");
        userMapper.addUser(user);
        logService.insertLogWithRequireTransaction(user.getId(),"createUser");
        throw new Exception("occur exception after add user bug");
    }

    /**
     * support的传播级别下
     * 调用insertLogSupportTransaction 不管当前数据库连接有没有开启事务，直接用这个数据库连接执行sql
     * 2020-05-11T16:34:12.121077Z	  343 Query	SET autocommit=0
     * 2020-05-11T16:34:12.953934Z	  343 Query	insert into user (id,username,address,created_at) values (null,'bug','浙江省湖州市德清县武康镇',now())
     * 2020-05-11T16:34:13.073910Z	  343 Query	insert into log (id,uid,`value`,created_at) values (null,9,'createUser',now())
     * 2020-05-11T16:34:13.084021Z	  343 Query	rollback
     * 2020-05-11T16:34:13.092094Z	  343 Query	SET autocommit=1
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addUserWithExceptionAndPropagationSupport() throws Exception {
        User user = new User()
                .setId(null) // id is null then use mysql auto increment id
                .setUsername("bug")
                .setAddress("浙江省湖州市德清县武康镇");
        userMapper.addUser(user);
        logService.insertLogSupportTransaction(user.getId(),"createUser");
        throw new Exception("occur exception after add user bug");
    }

    /**
     * mandatory传播级别下
     * 调用insertLogMustNeedExistingTransactionOrThrowException如果当前数据库连接没有开启事务则直接报错
     */
    public void addUserMustNeedExistingTransactionOrThrowException() {
        User user = new User()
                .setId(null) // id is null then use mysql auto increment id
                .setUsername("bug")
                .setAddress("浙江省湖州市德清县武康镇");
        userMapper.addUser(user);
        logService.insertLogMustNeedExistingTransactionOrThrowException(user.getId(),"createUser");
    }

    /**
     * never的传播级别下
     * 当调用insertLogNeverUseTransactionOrThrowException 如果当前数据库连接开启了事务，则直接报错
     * Existing transaction found for transaction marked with propagation 'never'
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addUserNeverUseTransactionOrThrowException() {
        User user = new User()
                .setId(null) // id is null then use mysql auto increment id
                .setUsername("bug")
                .setAddress("浙江省湖州市德清县武康镇");
        userMapper.addUser(user);
        logService.insertLogNeverUseTransactionOrThrowException(user.getId(),"createUser");
    }

    /**
     * 当前事务开启内嵌事务的原理是
     * 开启内嵌事务时，当前事务设置 savepoint;
     * 当内嵌事务报错时，当前事务回滚到 savepoint;
     *
     * general_log 截取如下：
     * Time                 Id Command    Argument
     * 2020-05-11T15:54:26.990293Z	  332 Query	SET autocommit=0
     * 2020-05-11T15:54:27.930105Z	  332 Query	insert into user (id,username,address,created_at) values (null,'newUser','浙江省湖州市德清县武康镇',now())
     * 2020-05-11T15:54:27.959550Z	  332 Query	SAVEPOINT `SAVEPOINT_1`
     * 2020-05-11T15:54:28.085198Z	  332 Query	insert into log (id,uid,`value`,created_at) values (null,3,'nestedExceptionLog',now())
     * 2020-05-11T15:54:28.095079Z	  332 Query	ROLLBACK TO SAVEPOINT `SAVEPOINT_1`
     * 2020-05-11T15:54:28.108048Z	  332 Query	insert into user (id,username,address,created_at) values (null,'newUser2','浙江省湖州市德清县武康镇',now())
     * 2020-05-11T15:54:28.119290Z	  332 Query	commit
     * 2020-05-11T15:54:28.121530Z	  332 Query	SET autocommit=1
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addUserWithNestedLog() {
        User user = new User()
                .setId(null) // id is null then use mysql auto increment id
                .setUsername("newUser")
                .setAddress("浙江省湖州市德清县武康镇");
        userMapper.addUser(user);

        try {
            logService.insertLogAfterSavepoint(user.getId(),"nestedExceptionLog");
        } catch (Exception e) {
            log.error("catch exception in the nested transaction and just ignore ");
        }

        userMapper.addUser(new User()
                .setId(null) // id is null then use mysql auto increment id
                .setUsername("newUser2")
                .setAddress("浙江省湖州市德清县武康镇"));
    }
}
