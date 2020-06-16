package com.liuapi.incubator.repository.xa;

/**
 *
 * SELECT @@tx_isolation; 查看隔离级别
 *
 * SERIALIZABLE
 * 可串行化的隔离级别
 * 可串行化不是串行执行，没有锁冲突的事务是可以并发的, 而是有锁冲突的事务只能串行执行。
 * 1）串行的粒度是事务【单独的select语句不是一个事务，单独的insert/update/delete是一个事务】
 * 2）没有锁冲突的事务是可以并发的，单独的select是可以并发的
 * 3）出现锁冲突的事务只能串行执行
 * 4）开启事务来读的话，读会在读取的每一行上加锁
 *
 * SERIALIZABLE解决了重复读和幻读的问题
 * 如果正常读的话不是一个事务，那么读是不会阻塞的，每次直接获取行的最新提交值即可返回
 * 如果开启了事务来读，那么读会在读取的每一行数据上加锁，如果获取不到锁还可能会阻塞
 */
public class SerializableIsolationScene {

}
