/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : 127.0.0.1
 Source Database       : zeus

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : utf-8

 Date: 04/18/2020 15:51:15 PM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `config`
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'key',
  `value` varchar(10240) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'value',
  `desc` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '描述',
  `enable` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否启用 true: 启用 false:禁用',
  `version` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '版本号',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key` (`key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='配置表';

-- ----------------------------
--  Table structure for `ecpm`
-- ----------------------------
DROP TABLE IF EXISTS `ecpm`;
CREATE TABLE `ecpm` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `trigger_day` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '触发日期 日期格式 yyyy-MM-dd',
  `ecpm` decimal(10,2) unsigned NOT NULL COMMENT 'ecpm值',
  `version` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '版本号',
  `del` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除 1:删除 0:未删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_trigger_day` (`trigger_day`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='ecpm配置表';

-- ----------------------------
--  Table structure for `ip_address`
-- ----------------------------
DROP TABLE IF EXISTS `ip_address`;
CREATE TABLE `ip_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip_start` varchar(15) COLLATE utf8mb4_bin NOT NULL,
  `ip_end` varchar(15) COLLATE utf8mb4_bin NOT NULL,
  `area` varchar(45) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '区域',
  `operator` varchar(6) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '运营商',
  `ip_start_num` bigint(10) NOT NULL,
  `ip_end_num` bigint(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ip_address_ip_end_num_index` (`ip_end_num`),
  KEY `ip_address_ip_start_num_index` (`ip_start_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IP地址';

-- ----------------------------
--  Table structure for `lottery_activity`
-- ----------------------------
DROP TABLE IF EXISTS `lottery_activity`;
CREATE TABLE `lottery_activity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(128) NOT NULL DEFAULT '' COMMENT '奖品名称',
  `state` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '抽奖活动状态{0:''待开启'',1:''待开奖'',2:已结束''}',
  `num` int(10) unsigned NOT NULL COMMENT '奖品数量',
  `cost_price` decimal(10,2) DEFAULT NULL COMMENT '单个奖品成本',
  `main_image_url` varchar(128) NOT NULL COMMENT '奖品主图地址',
  `detail_image_url` varchar(128) NOT NULL COMMENT '奖品详情图地址',
  `type` tinyint(2) NOT NULL COMMENT '奖品类型{0:''红包'',1:''实物''}',
  `product_state` tinyint(2) unsigned NOT NULL COMMENT '奖品状态{0:''下架'',1:''上架''}',
  `open_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开奖时间',
  `up_time` timestamp NULL DEFAULT NULL COMMENT '上架时间',
  `version` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '版本号',
  `del` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除 1:删除 0:未删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_state` (`state`,`product_state`) USING BTREE,
  KEY `idx_open_time` (`open_time`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='抽奖活动';

-- ----------------------------
--  Table structure for `sys_department`
-- ----------------------------
DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '部门名称',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父id',
  `level` int(11) DEFAULT NULL COMMENT '部门层级',
  `state` int(11) NOT NULL DEFAULT '1' COMMENT '状态，0：禁用，1：启用',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `remark` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_department_name_uindex` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=206 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='部门';

-- ----------------------------
--  Records of `sys_department`
-- ----------------------------
BEGIN;
INSERT INTO `sys_department` VALUES ('1', '技术部', null, '1', '1', '359544077', 'fe8c9cbac0c54395ac411335a31f4888', '15', '2019-10-25 09:46:49', '2019-11-13 19:56:07'), ('2', '研发部', null, '1', '1', '0', null, '0', '2019-11-01 20:45:43', null), ('20', '前端开发部', '2', '2', '1', '0', null, '0', '2019-11-01 20:48:38', null), ('21', '后台开发部', '2', '2', '1', '0', null, '0', '2019-11-01 20:48:38', null), ('22', '测试部', '2', '2', '1', '0', null, '0', '2019-11-01 20:48:38', null), ('201', '前端一组', '20', '3', '1', '0', null, '0', '2019-11-01 20:48:38', null), ('202', '前端二组', '20', '3', '1', '0', null, '0', '2019-11-01 20:48:38', null), ('203', '后台一组', '21', '3', '1', '0', null, '0', '2019-11-01 20:48:38', null), ('204', '后台二组', '21', '3', '1', '0', null, '0', '2019-11-01 20:48:38', null), ('205', '测试一组', '22', '3', '1', '0', null, '0', '2019-11-01 20:48:38', null);
COMMIT;

-- ----------------------------
--  Table structure for `sys_login_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `request_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '请求ID',
  `username` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户名称',
  `ip` varchar(15) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'IP',
  `area` varchar(45) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '区域',
  `operator` varchar(6) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '运营商',
  `token` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'tokenMd5值',
  `type` int(11) DEFAULT NULL COMMENT '1:登录，2：登出',
  `success` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否成功 true:成功/false:失败',
  `code` int(11) DEFAULT NULL COMMENT '响应码',
  `exception_message` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '失败消息记录',
  `user_agent` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '浏览器名称',
  `browser_name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '浏览器名称',
  `browser_version` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '浏览器版本',
  `engine_name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '浏览器引擎名称',
  `engine_version` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '浏览器引擎版本',
  `os_name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '系统名称',
  `platform_name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '平台名称',
  `mobile` tinyint(1) DEFAULT NULL COMMENT '是否是手机,0:否,1:是',
  `device_name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '移动端设备名称',
  `device_model` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '移动端设备型号',
  `remark` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='系统登录日志';

-- ----------------------------
--  Records of `sys_login_log`
-- ----------------------------
BEGIN;
INSERT INTO `sys_login_log` VALUES ('1', '1242813712335691777', 'admin', '127.0.0.1', '本机地址', null, 'c87aaffa35dadafb066cf18679eab36e', '1', '1', '200', null, 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36', 'Chrome', '80.0.3987.149', 'Webkit', '537.36', 'OSX', 'Mac', '0', null, null, null, '2020-03-25 22:01:11', null), ('2', '1242813887884091393', 'admin', '127.0.0.1', '本机地址', null, 'c87aaffa35dadafb066cf18679eab36e', '2', '1', '200', null, 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36', 'Chrome', '80.0.3987.149', 'Webkit', '537.36', 'OSX', 'Mac', '0', null, null, null, '2020-03-25 22:01:48', null), ('3', '1242814069371625474', 'admin', '127.0.0.1', '本机地址', null, null, '1', '0', null, '用户名或密码错误', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36', 'Chrome', '80.0.3987.149', 'Webkit', '537.36', 'OSX', 'Mac', '0', null, null, null, '2020-03-25 22:02:25', null), ('4', '1242814192096960513', null, '127.0.0.1', '本机地址', null, null, '2', '0', null, 'token不能为空', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36', 'Chrome', '80.0.3987.149', 'Webkit', '537.36', 'OSX', 'Mac', '0', null, null, null, '2020-03-25 22:02:54', null);
COMMIT;

-- ----------------------------
--  Table structure for `sys_operation_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `request_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '请求ID',
  `user_id` bigint(18) DEFAULT NULL COMMENT '用户ID',
  `user_name` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户名称',
  `name` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '日志名称',
  `ip` varchar(15) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'IP',
  `area` varchar(45) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '区域',
  `operator` varchar(6) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '运营商',
  `path` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '全路径',
  `module` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '模块名称',
  `class_name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '类名',
  `method_name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '方法名称',
  `request_method` varchar(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '请求方式，GET/POST',
  `content_type` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '内容类型',
  `request_body` tinyint(1) DEFAULT NULL COMMENT '是否是JSON请求映射参数',
  `param` text COLLATE utf8mb4_bin COMMENT '请求参数',
  `token` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'tokenMd5值',
  `type` int(11) DEFAULT NULL COMMENT '0:其它,1:新增,2:修改,3:删除,4:详情查询,5:所有列表,6:分页列表,7:其它查询,8:上传文件',
  `success` tinyint(1) DEFAULT NULL COMMENT '0:失败,1:成功',
  `code` int(11) DEFAULT NULL COMMENT '响应结果状态码',
  `message` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '响应结果消息',
  `exception_name` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '异常类名称',
  `exception_message` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '异常信息',
  `browser_name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '浏览器名称',
  `browser_version` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '浏览器版本',
  `engine_name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '浏览器引擎名称',
  `engine_version` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '浏览器引擎版本',
  `os_name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '系统名称',
  `platform_name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '平台名称',
  `mobile` tinyint(1) DEFAULT NULL COMMENT '是否是手机,0:否,1:是',
  `device_name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '移动端设备名称',
  `device_model` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '移动端设备型号',
  `remark` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='系统操作日志';

-- ----------------------------
--  Records of `sys_operation_log`
-- ----------------------------
BEGIN;
INSERT INTO `sys_operation_log` VALUES ('1', '1242805276474634241', null, null, 'helloWorld', '127.0.0.1', '本机地址', null, '/api/hello/world', null, 'HelloWorldController', 'helloWorld', 'GET', null, '0', null, null, '0', '1', '200', '操作成功', null, null, 'Chrome', '80.0.3987.149', 'Webkit', '537.36', 'OSX', 'Mac', '0', null, null, '', '2020-03-25 21:27:22', null), ('2', '1242820418688049153', null, null, 'FooBar分页列表', '127.0.0.1', '本机地址', null, '/api/fooBar/getPageList', 'foobar', 'com.example.foobar.controller.FooBarController', 'getFooBarPageList', 'POST', 'application/json', '1', '{\"pageIndex\":1,\"pageSize\":10}', null, '7', '1', '200', '操作成功', null, null, 'Chrome', '80.0.3987.149', 'Webkit', '537.36', 'OSX', 'Mac', '0', null, null, '', '2020-03-25 22:27:33', null);
COMMIT;

-- ----------------------------
--  Table structure for `sys_permission`
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '权限名称',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父id',
  `url` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '路径',
  `code` varchar(100) COLLATE utf8mb4_bin NOT NULL COMMENT '唯一编码',
  `icon` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图标',
  `type` int(11) NOT NULL COMMENT '类型，1：菜单，2：按钮',
  `level` int(11) NOT NULL COMMENT '层级，1：第一级，2：第二级，N：第N级',
  `state` int(11) NOT NULL DEFAULT '1' COMMENT '状态，0：禁用，1：启用',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `remark` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_permission_code_uindex` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4007 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='系统权限';

-- ----------------------------
--  Records of `sys_permission`
-- ----------------------------
BEGIN;
INSERT INTO `sys_permission` VALUES ('1', '系统管理', null, '/system/sys-user-list', 'system:management', 'el-icon-s-unfold', '1', '1', '1', '0', '1权限备注，可以查看所有用户信息', '0', '2019-10-26 11:12:40', null), ('100', '用户管理', '1', '/system/sys-user-list', 'sys:user:management', 'el-icon-s-unfold', '1', '2', '1', '0', '100权限备注，可以查看所有用户信息', '0', '2019-10-26 11:15:48', null), ('200', '角色管理', '1', '/system/sys-user-list', 'sys:role:management', 'el-icon-s-unfold', '1', '2', '1', '0', '200权限备注，可以查看所有用户信息', '0', '2019-10-26 11:15:48', null), ('300', '权限管理', '1', '/system/sys-user-list', 'sys:permission:management', 'el-icon-s-unfold', '1', '2', '1', '0', '300权限备注，可以查看所有用户信息', '0', '2019-10-26 11:15:48', null), ('400', '部门管理', '1', '/system/sys-user-list', 'sys:department:management', 'el-icon-s-unfold', '1', '2', '1', '0', '400权限备注，可以查看所有用户信息', '0', '2019-10-26 11:15:48', null), ('500', '日志管理', '1', '', 'sys:log:manager', 'el-icon-s-custom', '1', '2', '1', '0', '4005权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('1000', '用户新增', '100', '/system/sys-user-list', 'sys:user:add', 'el-icon-s-custom', '2', '3', '1', '0', '1000权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('1001', '用户修改', '100', '/system/sys-user-list', 'sys:user:update', 'el-icon-s-custom', '2', '3', '1', '0', '1001权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('1002', '用户删除', '100', '/system/sys-user-list', 'sys:user:delete', 'el-icon-s-custom', '2', '3', '1', '0', '1002权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('1003', '用户详情', '100', '/system/sys-user-list', 'sys:user:info', 'el-icon-s-custom', '2', '3', '1', '0', '1003权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('1004', '用户分页列表', '100', '/system/sys-user-list', 'sys:user:page', 'el-icon-s-custom', '2', '3', '1', '0', '1004权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('1005', '用户修改密码', '100', '/system/sys-user-list', 'sys:user:update:password', 'el-icon-s-custom', '2', '3', '1', '0', '1005权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('1006', '用户修改头像', '100', '/system/sys-user-list', 'sys:user:update:head', 'el-icon-s-custom', '2', '3', '1', '0', '1006权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('2000', '角色新增', '200', '/system/sys-user-list', 'sys:role:add', 'el-icon-s-custom', '2', '3', '1', '0', '2000权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('2001', '角色修改', '200', '/system/sys-user-list', 'sys:role:update', 'el-icon-s-custom', '2', '3', '1', '0', '2001权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('2002', '角色删除', '200', '/system/sys-user-list', 'sys:role:delete', 'el-icon-s-custom', '2', '3', '1', '0', '2002权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('2003', '角色详情', '200', '/system/sys-user-list', 'sys:role:info', 'el-icon-s-custom', '2', '3', '1', '0', '2003权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('2004', '角色分页列表', '200', '/system/sys-user-list', 'sys:role:page', 'el-icon-s-custom', '2', '3', '1', '0', '2004权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('2005', '角色列表', '200', '/system/sys-user-list', 'sys:role:list', 'el-icon-s-custom', '2', '3', '1', '0', '2005权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('2006', '角色权限ID列表', '200', null, 'sys:permission:three-ids-by-role-id', 'el-icon-s-custom', '2', '3', '1', '0', '2006权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('3000', '权限新增', '300', '/system/sys-user-list', 'sys:permission:add', 'el-icon-s-custom', '2', '3', '1', '0', '3000权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('3001', '权限修改', '300', '/system/sys-user-list', 'sys:permission:update', 'el-icon-s-custom', '2', '3', '1', '0', '3001权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('3002', '权限删除', '300', '/system/sys-user-list', 'sys:permission:delete', 'el-icon-s-custom', '2', '3', '1', '0', '3002权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('3003', '权限详情', '300', '/system/sys-user-list', 'sys:permission:info', 'el-icon-s-custom', '2', '3', '1', '0', '3003权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('3004', '权限分页列表', '300', '/system/sys-user-list', 'sys:permission:page', 'el-icon-s-custom', '2', '3', '1', '0', '3004权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('3005', '权限所有列表', '300', '/system/sys-user-list', 'sys:permission:all:menu:list', 'el-icon-s-custom', '2', '3', '1', '0', '3005权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('3006', '权限所有树形列表', '300', '/system/sys-user-list', 'sys:permission:all:menu:tree', 'el-icon-s-custom', '2', '3', '1', '0', '3006权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('3007', '权限用户列表', '300', '/system/sys-user-list', 'sys:permission:menu:list', 'el-icon-s-custom', '2', '3', '1', '0', '3007权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('3008', '权限用户树形列表', '300', '/system/sys-user-list', 'sys:permission:menu:tree', 'el-icon-s-custom', '2', '3', '1', '0', '3008权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('3009', '权限用户代码列表', '300', '/system/sys-user-list', 'sys:permission:codes', 'el-icon-s-custom', '2', '3', '1', '0', '3009权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('4000', '部门新增', '400', '/system/sys-user-list', 'sys:department:add', 'el-icon-s-custom', '2', '3', '1', '0', '4000权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('4001', '部门修改', '400', '/system/sys-user-list', 'sys:department:update', 'el-icon-s-custom', '2', '3', '1', '0', '4001权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('4002', '部门删除', '400', '/system/sys-user-list', 'sys:department:delete', 'el-icon-s-custom', '2', '3', '1', '0', '4002权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('4003', '部门详情', '400', '/system/sys-user-list', 'sys:department:info', 'el-icon-s-custom', '2', '3', '1', '0', '4003权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('4004', '部门分页列表', '400', '/system/sys-user-list', 'sys:department:page', 'el-icon-s-custom', '2', '3', '1', '0', '4004权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null), ('4005', '部门列表', '400', '/system/sys-user-list', 'sys:department:list', 'el-icon-s-custom', '2', '3', '1', '0', '4005权限备注，可以查看所有用户信息', '1', '2019-10-26 11:18:40', '2020-03-09 00:50:13'), ('4006', '部门树形列表', '400', null, 'sys:department:all:tree', 'el-icon-s-custom', '2', '3', '1', '0', '4006权限备注，可以查看所有用户信息', '0', '2019-10-26 11:18:40', null);
COMMIT;

-- ----------------------------
--  Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '角色名称',
  `code` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色唯一编码',
  `type` int(11) DEFAULT NULL COMMENT '角色类型',
  `state` int(11) NOT NULL DEFAULT '1' COMMENT '角色状态，0：禁用，1：启用',
  `remark` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_role_name_uindex` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='系统角色';

-- ----------------------------
--  Records of `sys_role`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES ('1', '管理员', 'admin', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null), ('2', 'test', 'test', null, '1', '测试人员拥有部分权限', '0', '2019-10-25 09:48:02', null), ('3', '管理员1', 'admin1', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null), ('4', '管理员2', 'admin2', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null), ('5', '管理员3', 'admin3', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null), ('6', '管理员4', 'admin4', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null), ('7', '管理员5', 'admin5', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null), ('8', '管理员6', 'admin6', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null), ('9', '管理员7', 'admin7', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null), ('10', '管理员8', 'admin8', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null), ('11', '管理员9', 'admin9', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null), ('12', '管理员10', 'admin10', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null), ('13', '管理员11', 'admin11', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null), ('14', '管理员12', 'admin12', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null), ('15', '管理员13', 'admin13', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null), ('16', '管理员14', 'admin14', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null), ('17', '管理员15', 'admin15', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null), ('18', '管理员16', 'admin16', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null), ('19', '管理员17', 'admin17', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null), ('20', '管理员18', 'admin18', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null), ('21', '管理员19', 'admin19', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null), ('22', '管理员20', 'admin20', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null), ('23', '管理员21', 'admin21', null, '1', '管理员拥有所有权限', '0', '2019-10-25 09:47:21', null);
COMMIT;

-- ----------------------------
--  Table structure for `sys_role_permission`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `permission_id` bigint(20) NOT NULL COMMENT '权限id',
  `state` int(11) NOT NULL DEFAULT '1' COMMENT '状态，0：禁用，1：启用',
  `remark` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `permission_id` (`permission_id`),
  KEY `role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=270 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='角色权限关系';

-- ----------------------------
--  Records of `sys_role_permission`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_permission` VALUES ('233', '1', '3008', '1', null, '0', '2020-03-03 14:19:18', null), ('234', '1', '1', '1', null, '0', '2020-03-03 14:19:18', null), ('235', '1', '3009', '1', null, '0', '2020-03-03 14:19:18', null), ('236', '1', '200', '1', null, '0', '2020-03-03 14:19:18', null), ('237', '1', '2000', '1', null, '0', '2020-03-03 14:19:18', null), ('238', '1', '400', '1', null, '0', '2020-03-03 14:19:18', null), ('239', '1', '2001', '1', null, '0', '2020-03-03 14:19:18', null), ('240', '1', '2002', '1', null, '0', '2020-03-03 14:19:18', null), ('241', '1', '2003', '1', null, '0', '2020-03-03 14:19:18', null), ('242', '1', '2004', '1', null, '0', '2020-03-03 14:19:18', null), ('243', '1', '2005', '1', null, '0', '2020-03-03 14:19:18', null), ('244', '1', '4000', '1', null, '0', '2020-03-03 14:19:18', null), ('245', '1', '4001', '1', null, '0', '2020-03-03 14:19:18', null), ('246', '1', '4002', '1', null, '0', '2020-03-03 14:19:18', null), ('247', '1', '4003', '1', null, '0', '2020-03-03 14:19:18', null), ('248', '1', '100', '1', null, '0', '2020-03-03 14:19:18', null), ('249', '1', '4004', '1', null, '0', '2020-03-03 14:19:18', null), ('250', '1', '4005', '1', null, '0', '2020-03-03 14:19:18', null), ('251', '1', '1000', '1', null, '0', '2020-03-03 14:19:18', null), ('252', '1', '1001', '1', null, '0', '2020-03-03 14:19:18', null), ('253', '1', '1002', '1', null, '0', '2020-03-03 14:19:18', null), ('254', '1', '1003', '1', null, '0', '2020-03-03 14:19:18', null), ('255', '1', '1004', '1', null, '0', '2020-03-03 14:19:18', null), ('256', '1', '300', '1', null, '0', '2020-03-03 14:19:18', null), ('257', '1', '1005', '1', null, '0', '2020-03-03 14:19:18', null), ('258', '1', '1006', '1', null, '0', '2020-03-03 14:19:18', null), ('259', '1', '3000', '1', null, '0', '2020-03-03 14:19:18', null), ('260', '1', '3001', '1', null, '0', '2020-03-03 14:19:18', null), ('261', '1', '3002', '1', null, '0', '2020-03-03 14:19:18', null), ('262', '1', '3003', '1', null, '0', '2020-03-03 14:19:18', null), ('263', '1', '3004', '1', null, '0', '2020-03-03 14:19:18', null), ('264', '1', '3005', '1', null, '0', '2020-03-03 14:19:18', null), ('265', '1', '3006', '1', null, '0', '2020-03-03 14:19:18', null), ('266', '1', '3007', '1', null, '0', '2020-03-03 14:19:18', null), ('267', '1', '500', '1', null, '0', '2020-03-03 14:19:18', null), ('268', '1', '2006', '1', null, '0', '2020-03-03 14:19:18', null), ('269', '1', '4006', '1', null, '0', '2020-03-03 14:19:18', null);
COMMIT;

-- ----------------------------
--  Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(20) COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `nickname` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '昵称',
  `password` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '密码',
  `salt` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '盐值',
  `phone` varchar(20) COLLATE utf8mb4_bin NOT NULL COMMENT '手机号码',
  `gender` int(11) NOT NULL DEFAULT '1' COMMENT '性别，0：女，1：男，默认1',
  `head` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像',
  `remark` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `state` int(11) NOT NULL DEFAULT '1' COMMENT '状态，0：禁用，1：启用，2：锁定',
  `department_id` bigint(20) NOT NULL COMMENT '部门id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `deleted` int(11) NOT NULL DEFAULT '0' COMMENT '逻辑删除，0：未删除，1：已删除',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_user_username_uindex` (`username`),
  KEY `department_id` (`department_id`),
  KEY `role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='系统用户';

-- ----------------------------
--  Records of `sys_user`
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES ('1', 'admin', '管理员', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889900', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '1', '1', '1', '0', '1', '2020-02-26 00:00:00', '2019-10-27 23:32:29'), ('2', 'test', '测试人员1', '34783fb724b259beb71a1279f7cd93bdcfd92a273d566f926419a37825c500df', '087c2e9857f35f1e243367f3b89b81c1', '15888889901', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Tester Account', '1', '1', '2', '0', '1', '2020-02-26 00:00:01', '2020-02-15 19:31:50'), ('3', 'admin1', '管理员1', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889902', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '1', '1', '1', '0', '1', '2020-02-26 00:09:09', '2019-10-27 23:32:29'), ('4', 'admin2', '管理员2', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889903', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '1', '1', '1', '0', '1', '2020-02-26 16:10:06', '2019-10-27 23:32:29'), ('5', 'admin3', '管理员3', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889904', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '1', '1', '1', '0', '1', '2020-02-26 16:10:06', '2019-10-27 23:32:29'), ('6', 'admin4', '管理员4', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889905', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '1', '1', '1', '0', '1', '2020-02-26 16:10:06', '2019-10-27 23:32:29'), ('7', 'admin5', '管理员5', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889906', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '1', '1', '1', '0', '1', '2020-02-26 23:59:59', '2019-10-27 23:32:29'), ('8', 'admin6', '管理员6', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '1', '1', '1', '0', '1', '2020-02-26 23:59:59', '2019-10-27 23:32:29'), ('9', 'admin7', '管理员7', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '1', '1', '1', '0', '1', '2020-02-20 23:59:59', '2019-10-27 23:32:29'), ('10', 'admin8', '管理员8', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '1', '1', '1', '0', '1', '2020-01-30 22:56:55', '2019-10-27 23:32:29'), ('11', 'admin9', '管理员9', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '1', '1', '1', '0', '1', '2019-12-30 00:52:01', '2019-10-27 23:32:29'), ('12', 'admin10', '管理员10', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '1', '1', '1', '0', '1', '2019-08-26 00:52:01', '2019-10-27 23:32:29'), ('13', 'admin11', '管理员11', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '1', '1', '1', '0', '1', '2019-08-26 00:52:01', '2019-10-27 23:32:29'), ('14', 'admin12', '管理员12', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '2', '1', '1', '0', '2', '2019-08-26 00:52:01', '2020-02-27 14:05:40'), ('15', 'admin13', '管理员13uuu', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '2', '1', '1', '0', '3', '2019-08-26 00:52:01', '2020-02-27 14:05:18'), ('16', 'admin14', '管理员14', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '0', '1', '1', '0', '1', '2019-08-26 00:52:01', '2019-10-27 23:32:29'), ('17', 'admin15', '管理员15', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '0', '1', '1', '0', '1', '2019-08-26 00:52:01', '2019-10-27 23:32:29'), ('18', 'admin16', '管理员16', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '0', '1', '1', '0', '1', '2019-08-26 00:52:01', '2019-10-27 23:32:29'), ('19', 'admin17', '管理员17', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '2', '1', '1', '0', '1', '2019-08-26 00:52:01', '2019-10-27 23:32:29'), ('20', 'admin18', '管理员18', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '2', '1', '1', '0', '1', '2019-08-26 00:52:01', '2019-10-27 23:32:29'), ('21', 'admin19', '管理员19', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '2', '1', '1', '0', '1', '2019-08-26 00:52:01', '2019-10-27 23:32:29'), ('22', 'admin20', '管理员20', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '2', '1', '1', '0', '1', '2019-08-26 00:52:01', '2019-10-27 23:32:29'), ('23', 'admin21', '管理员21', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '0', '1', '1', '0', '1', '2019-08-26 00:52:01', '2019-10-27 23:32:29'), ('24', 'admin22', '管理员22', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '0', '1', '1', '0', '1', '2019-08-26 00:52:01', '2019-10-27 23:32:29'), ('25', 'admin23', '管理员23', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '666', '15888889999', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'Administrator Account', '2', '1', '1', '0', '3', '2019-08-26 00:52:01', '2020-02-27 14:42:28'), ('100', 'dddd', 'ddddd', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', 'aa98a65fa53d198f38d8e3a63f3f5a65', 'ddddddddddd', '1', 'http://localhost:8888/api/resource/201908201013068.png', 'dddddddd', '1', '1', '1', '0', '1', '2020-02-26 14:06:53', '2020-02-27 14:06:52'), ('101', 'adminx', '111111', '11a254dab80d52bc4a347e030e54d861a9d2cdb2af2185a9ca4a7318e830d04d', '1faf81180b4a4a78c48d7c31479a0622', '11111111111', '1', 'http://localhost:8888/api/resource/201908201013068.png', '1111111111', '1', '1', '1', '1', '6', '2020-02-26 14:19:57', '2020-03-02 17:33:48');
COMMIT;

-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `mobile` varchar(64) DEFAULT NULL COMMENT '手机号',
  `del` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除 1:删除 0:未删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_mobile` (`mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
--  Records of `user`
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES ('8', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('9', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('10', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('11', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('12', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('13', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('14', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('15', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('16', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('17', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('18', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('19', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('20', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('21', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('22', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('23', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('24', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('25', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('26', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('27', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('28', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('29', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('30', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('31', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('32', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('33', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('34', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('35', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('36', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('37', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('38', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('39', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('40', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('41', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('42', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('43', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('44', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('45', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('46', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('47', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('48', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('49', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('50', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('51', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('52', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('53', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('54', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('55', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('56', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49'), ('57', null, '0', '2020-03-22 23:04:49', '2020-03-22 23:04:49');
COMMIT;

-- ----------------------------
--  Table structure for `user_account`
-- ----------------------------
DROP TABLE IF EXISTS `user_account`;
CREATE TABLE `user_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `amount` decimal(11,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '用户余额',
  `version` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '版本号',
  `del` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除 1:删除 0:未删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='用户账户';

-- ----------------------------
--  Table structure for `user_account_draw_approve`
-- ----------------------------
DROP TABLE IF EXISTS `user_account_draw_approve`;
CREATE TABLE `user_account_draw_approve` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `user_account_record_id` bigint(20) NOT NULL COMMENT '用户账户记录id',
  `state` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态 0:待审核 1:审核通过 2:审核不通过',
  `approve_time` timestamp NULL DEFAULT NULL COMMENT '审核时间',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `version` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '版本号',
  `del` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除 1:删除 0:未删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_account_record_id` (`user_account_record_id`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE,
  KEY `idx_user_id_state` (`user_id`,`state`) USING BTREE,
  KEY `idx_state_approve_time` (`state`,`approve_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户提现审核表';

-- ----------------------------
--  Table structure for `user_account_record`
-- ----------------------------
DROP TABLE IF EXISTS `user_account_record`;
CREATE TABLE `user_account_record` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `type` tinyint(2) unsigned NOT NULL COMMENT '支入支出类型{0:''支出'',1:''支入''}',
  `amount` decimal(11,2) unsigned NOT NULL COMMENT '支入支出金额 单位元',
  `use_type` tinyint(2) unsigned NOT NULL COMMENT '使用类型{0:''现金红包'',1:''提现''}',
  `state` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态{0:''初始化'',10:''进行中（需等待）'',20:''进行中（可重试）'',30;''成功'',40:''最终失败''} 现金红包类型默认成功10 提现默认初始化0',
  `err_msg` varchar(512) DEFAULT NULL COMMENT '错误信息',
  `pay_channel` tinyint(2) NOT NULL DEFAULT '0' COMMENT '提现渠道{0:''微信红包支付''}',
  `third_unique_id` varchar(64) DEFAULT NULL COMMENT '三方单号',
  `del` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除 1:删除 0:未删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_pay_channel_third_unique_id` (`pay_channel`,`third_unique_id`) USING BTREE,
  KEY `idx_user_id_type` (`user_id`,`type`) USING BTREE,
  KEY `idx_create_time_state` (`create_time`,`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账户明细';

-- ----------------------------
--  Table structure for `user_address`
-- ----------------------------
DROP TABLE IF EXISTS `user_address`;
CREATE TABLE `user_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `consignee_name` varchar(32) NOT NULL DEFAULT '' COMMENT '收货人姓名',
  `postal_code` varchar(32) DEFAULT '' COMMENT '邮编',
  `province_name` varchar(32) NOT NULL DEFAULT '' COMMENT '国标收货地址第一级地址',
  `city_name` varchar(32) NOT NULL DEFAULT '' COMMENT '国标收货地址第二级地址',
  `county_name` varchar(32) NOT NULL DEFAULT '' COMMENT '国标收货地址第三级地址',
  `detail_info` varchar(32) NOT NULL DEFAULT '' COMMENT '详细收货地址信息',
  `national_code` varchar(32) DEFAULT '' COMMENT '收货地址国家码',
  `tel_number` varchar(64) NOT NULL DEFAULT '' COMMENT '收货人手机号码',
  `del` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除 1:删除 0:未删除',
  `version` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '版本号',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_addr` (`tel_number`,`consignee_name`,`province_name`,`city_name`,`county_name`,`detail_info`,`user_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户地址表';

-- ----------------------------
--  Table structure for `user_lottery_record`
-- ----------------------------
DROP TABLE IF EXISTS `user_lottery_record`;
CREATE TABLE `user_lottery_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `lottery_activity_id` bigint(20) NOT NULL COMMENT '抽奖活动id',
  `state` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '状态{0:''参与抽奖(初始化)'',1:''已中奖'',2:未中奖''''}',
  `get_flag` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否领取 0:未领取 1:已领取',
  `win_amount` decimal(11,2) unsigned DEFAULT NULL COMMENT '中奖金额 当活动是红包类型才有效 单位元',
  `user_account_record_id` bigint(20) unsigned DEFAULT NULL COMMENT '用户账户记录id 当活动是红包类型才有效',
  `win_type` tinyint(2) unsigned DEFAULT NULL COMMENT '中奖类型{0:''红包'',1:''''实物}',
  `robot_flag` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否机器人参与抽奖{0:''非'',1:''是''}',
  `user_address_id` bigint(20) unsigned DEFAULT NULL COMMENT '用户收货地址id 实物奖品有该值',
  `win_time` timestamp NULL DEFAULT NULL COMMENT '中奖时间',
  `version` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '版本号',
  `del` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除 1:删除 0:未删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key` (`user_id`,`lottery_activity_id`) USING BTREE,
  KEY `idx_lottery_activity_id_robot_flag` (`lottery_activity_id`,`robot_flag`) USING BTREE,
  KEY `idx_lottery_activity_id_state` (`lottery_activity_id`,`state`)
) ENGINE=InnoDB AUTO_INCREMENT=171 DEFAULT CHARSET=utf8 COMMENT='用户抽奖记录表';

-- ----------------------------
--  Table structure for `user_share_record`
-- ----------------------------
DROP TABLE IF EXISTS `user_share_record`;
CREATE TABLE `user_share_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `share_day` varchar(16) NOT NULL COMMENT '分享日 格式yyyy-mm-dd',
  `state` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '状态{0:''可领取'',1:''已领取''}',
  `share_amount` decimal(11,2) unsigned NOT NULL COMMENT '分享获得金额 单位元',
  `user_account_record_id` bigint(20) DEFAULT NULL COMMENT '用户账户记录id ',
  `version` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '版本号',
  `del` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除 1:删除 0:未删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id_share_day` (`user_id`,`share_day`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COMMENT='用户分享记录表';

-- ----------------------------
--  Table structure for `user_third_info`
-- ----------------------------
DROP TABLE IF EXISTS `user_third_info`;
CREATE TABLE `user_third_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `open_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'openId',
  `app_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'appId',
  `union_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'unionId',
  `nick_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '昵称',
  `gender` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '性别',
  `language` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '语言',
  `city` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '市',
  `province` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '省',
  `country` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '国家信息',
  `avatar_url` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '头像',
  `del` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除 1:删除 0:未删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_id_app_id` (`user_id`,`app_id`) USING BTREE,
  UNIQUE KEY `unique_open_id_app_id` (`open_id`,`app_id`) USING BTREE,
  UNIQUE KEY `idx_union_id_app_id` (`union_id`,`app_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户三方信息';

-- ----------------------------
--  Records of `user_third_info`
-- ----------------------------
BEGIN;
INSERT INTO `user_third_info` VALUES ('62', '-1', '-1', 'wxd3ab7ab86cac8c02', null, '你瞒我瞒', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/01091532_0.jpeg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('63', '-2', '-2', 'wxd3ab7ab86cac8c02', null, '罗茂义', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/03155442_16.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('64', '-3', '-3', 'wxd3ab7ab86cac8c02', null, '墨染殇雪', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/06125815_9.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('65', '-4', '-4', 'wxd3ab7ab86cac8c02', null, '红', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/2020022910455033320.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('66', '-5', '-5', 'wxd3ab7ab86cac8c02', null, '陌颜幽梦', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D1076084373%2C2367880498%26fm%3D11%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('67', '-6', '-6', 'wxd3ab7ab86cac8c02', null, '自如', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D1134959888%2C3554053773%26fm%3D26%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('68', '-7', '-7', 'wxd3ab7ab86cac8c02', null, '灬一抹丶苍白', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D1209741383%2C1783837557%26fm%3D26%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('69', '-8', '-8', 'wxd3ab7ab86cac8c02', null, '(╯°□°）╯︵ ┻━┻', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D1338283018%2C3163536824%26fm%3D26%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('70', '-9', '-9', 'wxd3ab7ab86cac8c02', null, '乃纳', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D1453019879%2C3782226525%26fm%3D26%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('71', '-10', '-10', 'wxd3ab7ab86cac8c02', null, '鑫的开始', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D15363675%2C1280818686%26fm%3D15%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('72', '-11', '-11', 'wxd3ab7ab86cac8c02', null, '心已麻木i', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D1590059729%2C1015437717%26fm%3D15%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('73', '-12', '-12', 'wxd3ab7ab86cac8c02', null, 'Ennui', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D166148015%2C2439640714%26fm%3D26%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('74', '-13', '-13', 'wxd3ab7ab86cac8c02', null, '子供の時間', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D168957154%2C1253713076%26fm%3D15%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('75', '-14', '-14', 'wxd3ab7ab86cac8c02', null, '慑人的傲气', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D1693640730%2C2864833834%26fm%3D26%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('76', '-15', '-15', 'wxd3ab7ab86cac8c02', null, '青衫故人', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D1870796683%2C3322238076%26fm%3D11%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('77', '-16', '-16', 'wxd3ab7ab86cac8c02', null, '致原', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D1963649580%2C634218918%26fm%3D26%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('78', '-17', '-17', 'wxd3ab7ab86cac8c02', null, 'chino', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D208949338%2C2977580303%26fm%3D11%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('79', '-18', '-18', 'wxd3ab7ab86cac8c02', null, '念初', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D2189093955%2C2501060679%26fm%3D26%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('80', '-19', '-19', 'wxd3ab7ab86cac8c02', null, '纸盒', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D2211360062%2C2060660898%26fm%3D11%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('81', '-20', '-20', 'wxd3ab7ab86cac8c02', null, 'honey', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D2276691568%2C946994716%26fm%3D26%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('82', '-21', '-21', 'wxd3ab7ab86cac8c02', null, '▼遗忘那段似水年华', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D2280093061%2C1077713526%26fm%3D15%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('83', '-22', '-22', 'wxd3ab7ab86cac8c02', null, '吼姆啦', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D2378102508%2C1641074868%26fm%3D15%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('84', '-23', '-23', 'wxd3ab7ab86cac8c02', null, '梅梅', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D2440425167%2C3272311068%26fm%3D15%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('85', '-24', '-24', 'wxd3ab7ab86cac8c02', null, '晤姆', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D2489127333%2C3369301913%26fm%3D26%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('86', '-25', '-25', 'wxd3ab7ab86cac8c02', null, 'tina', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D2769502697%2C3458232849%26fm%3D26%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('87', '-26', '-26', 'wxd3ab7ab86cac8c02', null, '迈尔密', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D2843999829%2C337485673%26fm%3D26%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('88', '-27', '-27', 'wxd3ab7ab86cac8c02', null, 'wow', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D2876631740%2C2051406996%26fm%3D15%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('89', '-28', '-28', 'wxd3ab7ab86cac8c02', null, '枫无痕', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D2885889490%2C32352315%26fm%3D26%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('90', '-29', '-29', 'wxd3ab7ab86cac8c02', null, '有些人,只适合好奇~', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D2894140598%2C3632576954%26fm%3D26%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('91', '-30', '-30', 'wxd3ab7ab86cac8c02', null, 'hilbert', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D2951654164%2C2758055177%26fm%3D15%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('92', '-31', '-31', 'wxd3ab7ab86cac8c02', null, '素婉纤尘', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D3074859650%2C3580218271%26fm%3D15%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('93', '-32', '-32', 'wxd3ab7ab86cac8c02', null, '秘密', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D3184007781%2C563018498%26fm%3D26%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('94', '-33', '-33', 'wxd3ab7ab86cac8c02', null, '仅有的余温', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D3218398817%2C646770108%26fm%3D11%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('95', '-34', '-34', 'wxd3ab7ab86cac8c02', null, '浙江吴彦祖', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D3349064358%2C3247182518%26fm%3D15%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('96', '-35', '-35', 'wxd3ab7ab86cac8c02', null, '张磊', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D812403322%2C2823840884%26fm%3D11%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('97', '-36', '-36', 'wxd3ab7ab86cac8c02', null, '陆小逗', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D3411823610%2C2815038639%26fm%3D11%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('98', '-37', '-37', 'wxd3ab7ab86cac8c02', null, '可乐大王', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D352449196%2C3612915747%26fm%3D11%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('99', '-38', '-38', 'wxd3ab7ab86cac8c02', null, '储继伟', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D3527628389%2C3937276372%26fm%3D26%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('100', '-39', '-39', 'wxd3ab7ab86cac8c02', null, 'rocket.lee', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D3619880976%2C824954693%26fm%3D11%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('101', '-40', '-40', 'wxd3ab7ab86cac8c02', null, '逾期不候', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D3660652581%2C1410574306%26fm%3D26%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('102', '-41', '-41', 'wxd3ab7ab86cac8c02', null, 'Rocky', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D3675930337%2C1817483182%26fm%3D15%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('103', '-42', '-42', 'wxd3ab7ab86cac8c02', null, '林金松', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D3725245648%2C992496690%26fm%3D15%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('104', '-43', '-43', 'wxd3ab7ab86cac8c02', null, '留我一人', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D378663327%2C3360351790%26fm%3D15%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('105', '-44', '-44', 'wxd3ab7ab86cac8c02', null, '煮酒', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D38286842%2C2383028983%26fm%3D26%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('106', '-45', '-45', 'wxd3ab7ab86cac8c02', null, '咖啡即吾命', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D3834888080%2C3690645327%26fm%3D26%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('107', '-46', '-46', 'wxd3ab7ab86cac8c02', null, '笑叹★尘世美', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D4057208949%2C1810939333%26fm%3D15%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('108', '-47', '-47', 'wxd3ab7ab86cac8c02', null, '每天晒白牙', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D495932705%2C540515453%26fm%3D11%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('109', '-48', '-48', 'wxd3ab7ab86cac8c02', null, '于小彤', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D52932256%2C3498103417%26fm%3D11%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('110', '-49', '-49', 'wxd3ab7ab86cac8c02', null, '陌潇潇', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D735219341%2C1155707817%26fm%3D11%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44'), ('111', '-50', '-50', 'wxd3ab7ab86cac8c02', null, '浮世繁华', null, null, null, null, null, 'https://weixinggg.oss-cn-beijing.aliyuncs.com/user/avatar/u%3D799664887%2C1936941254%26fm%3D26%26gp%3D0.jpg', '0', '2020-03-15 21:28:29', '2020-03-28 18:19:44');
COMMIT;

-- ----------------------------
--  Table structure for `user_third_notice`
-- ----------------------------
DROP TABLE IF EXISTS `user_third_notice`;
CREATE TABLE `user_third_notice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `open_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'openId',
  `app_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'appId',
  `template_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '模板id',
  `enable_send_num` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '可发送次数',
  `version` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '版本号',
  `del` tinyint(1) unsigned DEFAULT '0' COMMENT '是否删除 1:删除 0:未删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_template_id_app_id_user_id` (`template_id`,`app_id`,`user_id`) USING BTREE,
  KEY `idx_user_id_app_id_enable_send_num` (`user_id`,`app_id`,`enable_send_num`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户三方订阅消息记录';

SET FOREIGN_KEY_CHECKS = 1;
