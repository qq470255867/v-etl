/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : vetu_quartz

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 23/01/2020 11:22:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_conf
-- ----------------------------
DROP TABLE IF EXISTS `sys_conf`;
CREATE TABLE `sys_conf`  (
  `plat_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `jar_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `src_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `log_interval` bigint(20) NOT NULL,
  `data_statistical` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`plat_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_conf
-- ----------------------------
INSERT INTO `sys_conf` VALUES ('数据接入管理平台', 'D:\\\\Download\\\\', 'D:\\\\Download\\\\src', 10, 'dataVolume');

SET FOREIGN_KEY_CHECKS = 1;
