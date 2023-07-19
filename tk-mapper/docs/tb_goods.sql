-- ----------------------------
-- Table structure for tb_goods
-- ----------------------------
DROP TABLE IF EXISTS `tb_goods`;
CREATE TABLE `tb_goods`
(
    `id`         int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '物品名称',
    `no`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '物品编号',
    `unit_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '物品单价',
    `inventory`  decimal(10, 2) NULL DEFAULT NULL COMMENT '现有库存量',
    `remark`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
    `available`  char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '可用状态 0不可用 1可用',
    `version`    int NULL DEFAULT NULL COMMENT '乐观锁版本号',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_goods
-- ----------------------------
INSERT INTO `tb_goods`
VALUES (1, 'Product 1', 'P001', 10.50, 100.00, 'Test remark 1', '1', NULL);
INSERT INTO `tb_goods`
VALUES (2, 'Product 2', 'P002', 15.75, 50.00, 'Test remark 2', '1', NULL);
INSERT INTO `tb_goods`
VALUES (3, 'Product 3', 'P003', 20.00, 200.00, 'Test remark 3', '1', NULL);
INSERT INTO `tb_goods`
VALUES (4, 'Product 4', 'P004', 8.99, 80.00, 'Test remark 4', '0', NULL);
INSERT INTO `tb_goods`
VALUES (5, 'Product 5', 'P005', 12.50, 150.00, 'Test remark 5', '0', NULL);
INSERT INTO `tb_goods`
VALUES (6, 'Product 6', 'P006', 17.25, 30.00, 'Test remark 6', '0', NULL);
INSERT INTO `tb_goods`
VALUES (7, 'Product 7', 'P007', 9.99, 120.00, 'Test remark 7', '0', NULL);
INSERT INTO `tb_goods`
VALUES (8, 'Product 8', 'P008', 14.75, 90.00, 'Test remark 8', '0', NULL);
INSERT INTO `tb_goods`
VALUES (9, 'Product 9', 'P009', 19.50, 60.00, 'Test remark 9', '0', NULL);
INSERT INTO `tb_goods`
VALUES (10, 'Product 10', 'P010', 11.25, 180.00, 'Test remark 10', '1', NULL);

-- mybatis-plus使用同一个表 --
