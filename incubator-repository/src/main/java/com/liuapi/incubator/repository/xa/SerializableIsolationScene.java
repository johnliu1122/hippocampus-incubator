package com.liuapi.incubator.repository.xa;

/**
 *
 * SELECT @@tx_isolation; 查看隔离级别
 *
 * serializable 可串行化的隔离级别介绍
 *
 * 可串行化不是串行执行，没有锁冲突的事务是可以并发的, 而是有锁冲突的事务只能串行执行。
 * SERIALIZABLE解决了重复读和幻读的问题
 *
 * SERIALIZABLE会在读取的每一行数据上都加行锁, 同时添加间隙锁来防止幻读
 *
 *
 */
public class SerializableIsolationScene {

}
