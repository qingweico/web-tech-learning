CREATE TABLE `t_bill` (
  `id` BIGINT ( 20 ) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` VARCHAR ( 32 ) NOT NULL COMMENT '订单编号',
  `phone` VARCHAR ( 20 ) NOT NULL COMMENT '手机号',
  `amount` DECIMAL ( 12, 2 ) NOT NULL COMMENT '订单金额',
  `service_fee` DECIMAL ( 12, 2 ) DEFAULT 0.00 COMMENT '手续费',
  `status` VARCHAR ( 20 ) NOT NULL COMMENT '账单状态（UNPAID/PAID/REFUNDED/CANCELLED）',
  `payment_method` VARCHAR ( 20 ) NOT NULL COMMENT '支付方式（ALIPAY/WECHAT/BANK）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `transaction_no` VARCHAR ( 20 ) DEFAULT NULL COMMENT '交易流水号',
  `remark` VARCHAR ( 200 ) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY ( `id` ),
  UNIQUE KEY `uk_order_no` ( `order_no` ),
  KEY `idx_phone` ( `phone` )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '账单表';
