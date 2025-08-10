CREATE TABLE `locker` (
                          `lock_name` varchar(100) NOT NULL COMMENT '锁名称',
                          `lock_key` varchar(100) NOT NULL COMMENT '锁密钥',
                          `lock_count` int(11) NOT NULL COMMENT '上锁次数',
                          `create_time` datetime(6) NOT NULL COMMENT '创建时间',
                          `limit_time` datetime(6) NOT NULL COMMENT '失效时间',
                          PRIMARY KEY (`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分布式锁';
