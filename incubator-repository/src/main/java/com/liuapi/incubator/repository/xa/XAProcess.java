package com.liuapi.incubator.repository.xa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;

/**
 * 可串行化的隔离级别下， XA事务是能够避免脏读的。
 *
 * 因为可串行化的隔离级别下，会串行执行出现锁冲突的事务。
 *
 * 如何避免脏读？
 *
 * 什么是脏读？一个事务读到其他事务未提交的内容
 * XA 事务修改了节点 A 上的数据 a --> a', 节点 B 上的数据  b --> b'，
 * 在提交阶段，可能 a' 被其他事务读到，这时候只要保证读到 a' 的事务也同时读到 b' ，就是一致的。
 * 反过来，如果其他事务读到的是 a', b 或者 a, b', 就违反了一致性，产生了脏读
 *
 * xa prepare后，各个资源位的xa事务都开启了，如果有其他
 *
 */
public class XAProcess {
    /**
     * 查看innodb所有进行中的事务
     * select * from information_schema.innodb_trx;
     *
     * 可以同时查看xa事务与普通事务
     */

    /**
     * xa begin "d1";
     *
     * update usercenter_shard1.user set balance = balance+100 where id = 18;
     *
     * xa end "d1";
     *
     * xa prepare "d1";
     *
     * xa commit "d1"; / xa rollback "d1"
     */
    public void process(){

    }

    /**
     * xa recover; 显示所有进行中的事务
     * +----------+--------------+--------------+------+
     * | formatID | gtrid_length | bqual_length | data |
     * +----------+--------------+--------------+------+
     * |        1 |            2 |            0 | d1   |
     * +----------+--------------+--------------+------+
     * xa commit "d1"; / xa rollback "d1";
     */
    public void recover() throws FileNotFoundException {



    }

}
