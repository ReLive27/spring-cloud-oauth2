DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `nick_name` varchar(255) DEFAULT NULL COMMENT '昵称',
  `phone` varchar(255) DEFAULT NULL COMMENT '手机号',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `role_name` varchar(255) DEFAULT NULL COMMENT '角色名',
  `role_code` varchar(255) DEFAULT NULL COMMENT '角色code',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `permission_name` varchar(255) DEFAULT NULL COMMENT '权限名称',
  `permission_code` varchar(255) DEFAULT NULL COMMENT '权限code',
  `permission_type` varchar(255) DEFAULT NULL COMMENT '权限类型 API MENU',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `user_2_role`;
CREATE TABLE `user_2_role` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `role_2_permission`;
CREATE TABLE `role_2_permission` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  `permission_id` bigint(20) DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `user`(`id`, `username`, `password`, `nick_name`, `phone`, `email`) VALUES (1, 'admin', '$2a$10$ssWeYcRjVlB763hw5AkoZ.extoczZQShG/iaNrvtHeVTDASrOVyt.', '超级管理员', '13110002000', 'test@qwd.com');

INSERT INTO `role`(`id`, `role_name`, `role_code`) VALUES (1, '系统管理员', 'ROLE_ADMIN');

INSERT INTO `user_2_role`(`id`, `user_id`, `role_id`) VALUES (1, 1, 1);

INSERT INTO `permission`(`id`, `permission_name`, `permission_code`, `permission_type`) VALUES (1, '首页', 'home-page', 'API');

INSERT INTO `role_2_permission`(`id`, `role_id`, `permission_id`) VALUES (1, 1, 1);




