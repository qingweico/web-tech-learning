CREATE TABLE `t_user` (
                          `id` varchar(32) NOT NULL COMMENT '主键',
                          `username` varchar(100) NOT NULL COMMENT '用户名',
                          `face_id` varchar(255) DEFAULT NULL COMMENT '人脸入库图片信息 ,保存到MongoDB的GridFS中,为空表示未开启人脸登录',
                          `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `uk_username` (`username`),
                          KEY `idx_face_id` (`face_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';
