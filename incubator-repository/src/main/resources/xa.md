
xa begin "d1";

update usercenter_shard1.user set balance = balance+100 where id = 18;

xa end "d1";



xa prepare "d1"; // 开启xa事务

xa commit "d1"; / xa rollback "d1"   // 提交或回滚xa事务

