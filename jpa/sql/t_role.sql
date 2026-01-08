DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

BEGIN;
INSERT INTO `t_role` (`id`, `name`, `code`, `description`) VALUES (1, '系统管理员', 'ADMIN', '系统管理员');
INSERT INTO `t_role` (`id`, `name`, `code`, `description`) VALUES (2, '普通用户角色', 'USER', '普通用户角色');
COMMIT;

