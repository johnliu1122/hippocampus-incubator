### 配置技巧
1. 如何显示sql日志？

logging:
  level:
    com.liuapi.incubator.repository: debug
最简便的方法就是将项目的日志级别定为debug即可

2. 如何在idea中快捷得跳转xml与dao

安装idea插件[ Free Mybatis Plugin ]

3. 如何查看binlog

show variables like 'log_bin';

SHOW BINLOG events;

show master status;

4. 查看general_log

show variables like "general_log%";

set global general_log_file="/general5.log";

set global general_log=on;




