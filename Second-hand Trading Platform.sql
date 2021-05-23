/*
Navicat MySQL Data Transfer

Source Server         : hyh
Source Server Version : 50716
Source Host           : localhost:3307
Source Database       : shadouyou

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2021-05-18 15:52:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `administrator`
-- ----------------------------
DROP TABLE IF EXISTS `administrator`;
CREATE TABLE `administrator` (
  `administratorID` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `accessLevel` int(10) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`administratorID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of administrator
-- ----------------------------
INSERT INTO `administrator` VALUES ('1', '1999', '5ec829debe54b19a5f78d9a65b900a39', '0', '超管');

-- ----------------------------
-- Table structure for `appeal`
-- ----------------------------
DROP TABLE IF EXISTS `appeal`;
CREATE TABLE `appeal` (
  `appealID` int(11) NOT NULL AUTO_INCREMENT,
  `orderID` int(11) DEFAULT NULL,
  `userID` int(11) DEFAULT NULL,
  `appealTime` varchar(255) DEFAULT NULL,
  `appealReason` varchar(255) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`appealID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of appeal
-- ----------------------------

-- ----------------------------
-- Table structure for `comment`
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `commentID` int(11) NOT NULL AUTO_INCREMENT,
  `userID` int(11) DEFAULT NULL,
  `goodsID` int(11) DEFAULT NULL,
  `releaseTime` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`commentID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of comment
-- ----------------------------

-- ----------------------------
-- Table structure for `goods`
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `goodsID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `userID` int(11) DEFAULT NULL,
  `goodsPrice` double(11,2) DEFAULT '0.00',
  `sendPrice` double(11,2) NOT NULL DEFAULT '0.00',
  `exchangeBook` varchar(255) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `childTag` varchar(255) DEFAULT NULL,
  `realaseTime` varchar(255) DEFAULT NULL,
  `isExamine` int(11) DEFAULT NULL,
  `examineTime` varchar(255) DEFAULT NULL,
  `ascription` varchar(255) DEFAULT NULL,
  `nopassReason` varchar(255) DEFAULT NULL,
  `isSale` int(11) DEFAULT NULL,
  `imgfirst` varchar(255) NOT NULL,
  `imgsecond` varchar(255) DEFAULT NULL,
  `imgthird` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`goodsID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of goods
-- ----------------------------

-- ----------------------------
-- Table structure for `inform`
-- ----------------------------
DROP TABLE IF EXISTS `inform`;
CREATE TABLE `inform` (
  `informID` int(11) NOT NULL AUTO_INCREMENT,
  `objectID` int(11) DEFAULT NULL,
  `userID` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `state` int(6) DEFAULT NULL,
  `releaseTime` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`informID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of inform
-- ----------------------------

-- ----------------------------
-- Table structure for `message`
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `messageID` int(11) NOT NULL AUTO_INCREMENT,
  `receiverID` int(11) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `isRead` int(11) DEFAULT NULL,
  PRIMARY KEY (`messageID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of message
-- ----------------------------

-- ----------------------------
-- Table structure for `notice`
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `NoticeID` int(11) NOT NULL AUTO_INCREMENT,
  `administratorID` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `releaseTime` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`NoticeID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of notice
-- ----------------------------

-- ----------------------------
-- Table structure for `order_`
-- ----------------------------
DROP TABLE IF EXISTS `order_`;
CREATE TABLE `order_` (
  `orderID` int(11) NOT NULL AUTO_INCREMENT,
  `goodsID` int(11) DEFAULT NULL,
  `goodsPrice` double(11,2) DEFAULT '0.00',
  `goodsName` varchar(255) DEFAULT NULL,
  `userID` int(11) DEFAULT NULL,
  `tradingStatus` varchar(255) DEFAULT NULL,
  `releaseTime` varchar(255) DEFAULT NULL,
  `endtime` varchar(255) DEFAULT NULL,
  `isComment` int(6) DEFAULT NULL,
  `creditRating` double(6,1) DEFAULT '0.0',
  `sendway` int(6) DEFAULT NULL,
  `sendprice` double(11,2) DEFAULT '0.00',
  `imgfirst` varchar(255) DEFAULT NULL,
  `imgsecond` varchar(255) DEFAULT NULL,
  `imgthird` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`orderID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of order_
-- ----------------------------

-- ----------------------------
-- Table structure for `tag`
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `tagID` int(11) NOT NULL AUTO_INCREMENT,
  `ascription` varchar(255) DEFAULT NULL,
  `tagName` varchar(255) DEFAULT NULL,
  `hot` int(11) DEFAULT '0',
  PRIMARY KEY (`tagID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of tag
-- ----------------------------
INSERT INTO `tag` VALUES ('1', 'common', '代步工具', '0');
INSERT INTO `tag` VALUES ('2', 'common', '数码产品', '0');
INSERT INTO `tag` VALUES ('3', 'common', '书籍', '0');
INSERT INTO `tag` VALUES ('4', 'common', '生活用品', '0');
INSERT INTO `tag` VALUES ('5', 'common', '其它', '0');
INSERT INTO `tag` VALUES ('6', 'graduation', '代步工具', '0');
INSERT INTO `tag` VALUES ('7', 'graduation', '数码产品', '0');
INSERT INTO `tag` VALUES ('8', 'graduation', '书籍', '0');
INSERT INTO `tag` VALUES ('9', 'graduation', '生活用品', '0');
INSERT INTO `tag` VALUES ('10', 'graduation', '其它', '0');
INSERT INTO `tag` VALUES ('12', 'books', '人文', '0');
INSERT INTO `tag` VALUES ('13', 'books', '社会', '0');
INSERT INTO `tag` VALUES ('14', 'books', '科学', '0');
INSERT INTO `tag` VALUES ('15', 'books', '教材', '0');
INSERT INTO `tag` VALUES ('16', 'books', '考研', '0');
INSERT INTO `tag` VALUES ('17', 'books', '其它', '0');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `userID` int(11) NOT NULL AUTO_INCREMENT,
  `accountNumber` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `images` varchar(255) DEFAULT NULL,
  `introduction` varchar(255) DEFAULT NULL,
  `realname` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `QQnumber` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `creditRating` double(5,1) DEFAULT '0.0',
  `isUse` int(6) DEFAULT NULL,
  PRIMARY KEY (`userID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('2', '221701226', '906419fc15294b1fb063b36f0f6e3739', '黄于晗', '男', 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png', null, '黄于晗', null, '13235913279', null, '福建省福州市闽侯县上街镇福州大学', '3.0', '1');

-- ----------------------------
-- View structure for `books_goods`
-- ----------------------------
DROP VIEW IF EXISTS `books_goods`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `books_goods` AS (select `goods`.`goodsID` AS `goodsID`,`goods`.`name` AS `name`,`goods`.`title` AS `title`,`goods`.`text` AS `text`,`goods`.`userID` AS `userID`,`goods`.`goodsPrice` AS `goodsPrice`,`goods`.`sendPrice` AS `sendPrice`,`goods`.`exchangeBook` AS `exchangeBook`,`goods`.`tag` AS `tag`,`goods`.`childTag` AS `childTag`,`goods`.`realaseTime` AS `realaseTime`,`goods`.`isExamine` AS `isExamine`,`goods`.`examineTime` AS `examineTime`,`goods`.`ascription` AS `ascription`,`goods`.`nopassReason` AS `nopassReason`,`goods`.`isSale` AS `isSale`,`goods`.`imgfirst` AS `imgfirst`,`goods`.`imgsecond` AS `imgsecond`,`goods`.`imgthird` AS `imgthird` from `goods` where (`goods`.`ascription` = 'books')) ;

-- ----------------------------
-- View structure for `books_tag`
-- ----------------------------
DROP VIEW IF EXISTS `books_tag`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `books_tag` AS (select `tag`.`tagID` AS `tagID`,`tag`.`ascription` AS `ascription`,`tag`.`tagName` AS `tagName`,`tag`.`hot` AS `hot` from `tag` where (`tag`.`ascription` = 'books')) ;

-- ----------------------------
-- View structure for `common_goods`
-- ----------------------------
DROP VIEW IF EXISTS `common_goods`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `common_goods` AS (select `goods`.`goodsID` AS `goodsID`,`goods`.`name` AS `name`,`goods`.`title` AS `title`,`goods`.`text` AS `text`,`goods`.`userID` AS `userID`,`goods`.`goodsPrice` AS `goodsPrice`,`goods`.`sendPrice` AS `sendPrice`,`goods`.`exchangeBook` AS `exchangeBook`,`goods`.`tag` AS `tag`,`goods`.`childTag` AS `childTag`,`goods`.`realaseTime` AS `realaseTime`,`goods`.`isExamine` AS `isExamine`,`goods`.`examineTime` AS `examineTime`,`goods`.`ascription` AS `ascription`,`goods`.`nopassReason` AS `nopassReason`,`goods`.`isSale` AS `isSale`,`goods`.`imgfirst` AS `imgfirst`,`goods`.`imgsecond` AS `imgsecond`,`goods`.`imgthird` AS `imgthird` from `goods` where (`goods`.`ascription` = 'common')) ;

-- ----------------------------
-- View structure for `common_tag`
-- ----------------------------
DROP VIEW IF EXISTS `common_tag`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `common_tag` AS (select `tag`.`tagID` AS `tagID`,`tag`.`ascription` AS `ascription`,`tag`.`tagName` AS `tagName`,`tag`.`hot` AS `hot` from `tag` where (`tag`.`ascription` = 'common')) ;

-- ----------------------------
-- View structure for `graduation_goods`
-- ----------------------------
DROP VIEW IF EXISTS `graduation_goods`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `graduation_goods` AS (select `goods`.`goodsID` AS `goodsID`,`goods`.`name` AS `name`,`goods`.`title` AS `title`,`goods`.`text` AS `text`,`goods`.`userID` AS `userID`,`goods`.`goodsPrice` AS `goodsPrice`,`goods`.`sendPrice` AS `sendPrice`,`goods`.`exchangeBook` AS `exchangeBook`,`goods`.`tag` AS `tag`,`goods`.`childTag` AS `childTag`,`goods`.`realaseTime` AS `realaseTime`,`goods`.`isExamine` AS `isExamine`,`goods`.`examineTime` AS `examineTime`,`goods`.`ascription` AS `ascription`,`goods`.`nopassReason` AS `nopassReason`,`goods`.`isSale` AS `isSale`,`goods`.`imgfirst` AS `imgfirst`,`goods`.`imgsecond` AS `imgsecond`,`goods`.`imgthird` AS `imgthird` from `goods` where (`goods`.`ascription` = 'graduation')) ;

-- ----------------------------
-- View structure for `graduation_tag`
-- ----------------------------
DROP VIEW IF EXISTS `graduation_tag`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `graduation_tag` AS (select `tag`.`tagID` AS `tagID`,`tag`.`ascription` AS `ascription`,`tag`.`tagName` AS `tagName`,`tag`.`hot` AS `hot` from `tag` where (`tag`.`ascription` = 'graduation')) ;
