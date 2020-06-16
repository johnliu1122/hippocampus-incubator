-- 创建分片1的ddl
create database usercenter_shard1 charset utf8mb4;
use usercenter_shard1;
CREATE TABLE `log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '事务id',
  `uid` bigint(20) DEFAULT NULL COMMENT '用户id',
  `value` varchar(50) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `id_card` char(18) DEFAULT NULL,
  `balance` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_id_card` (`id_card`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `privilege` (
  `id` bigint(20) unsigned NOT NULL,
  `code` varchar(20) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建分片2的ddl
create database usercenter_shard2 charset utf8mb4;
create table usercenter_shard2.log select * from usercenter_shard1.log limit 0;
create table usercenter_shard2.user select * from usercenter_shard1.user limit 0;
create table usercenter_shard2.privilege select * from usercenter_shard1.privilege limit 0;
