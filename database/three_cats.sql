-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: three_cats
-- ------------------------------------------------------
-- Server version	5.7.9-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `user_name` varchar(200) NOT NULL,
  `password` varchar(200) NOT NULL,
  `type` tinyint(1) NOT NULL,
  `lock` tinyint(1) NOT NULL,
  PRIMARY KEY (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_information`
--

DROP TABLE IF EXISTS `account_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_information` (
  `user_name` varchar(200) NOT NULL,
  `name` varchar(200) NOT NULL,
  `phone_number` varchar(50) NOT NULL,
  `email` varchar(200) NOT NULL,
  `city` varchar(50) NOT NULL,
  `county` varchar(200) NOT NULL,
  `address` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`user_name`),
  CONSTRAINT `key2_account_information` FOREIGN KEY (`user_name`) REFERENCES `account` (`user_name`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_information`
--

LOCK TABLES `account_information` WRITE;
/*!40000 ALTER TABLE `account_information` DISABLE KEYS */;
/*!40000 ALTER TABLE `account_information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_shop`
--

DROP TABLE IF EXISTS `chat_shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chat_shop` (
  `user_name_sender` varchar(200) NOT NULL,
  `user_name_receiver` varchar(200) NOT NULL,
  `time` datetime NOT NULL,
  `status` tinyint(1) NOT NULL,
  `content` varchar(1000) NOT NULL,
  KEY `key2_chat_shop_idx` (`user_name_sender`),
  KEY `key3_chat_shop_idx` (`user_name_receiver`),
  CONSTRAINT `key2_chat_shop` FOREIGN KEY (`user_name_sender`) REFERENCES `account` (`user_name`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `key3_chat_shop` FOREIGN KEY (`user_name_receiver`) REFERENCES `account` (`user_name`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_shop`
--

LOCK TABLES `chat_shop` WRITE;
/*!40000 ALTER TABLE `chat_shop` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat_shop` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client_status`
--

DROP TABLE IF EXISTS `client_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client_status` (
  `user_name` varchar(200) NOT NULL,
  `status` tinyint(1) NOT NULL,
  PRIMARY KEY (`user_name`),
  CONSTRAINT `key2_client_status` FOREIGN KEY (`user_name`) REFERENCES `account` (`user_name`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client_status`
--

LOCK TABLES `client_status` WRITE;
/*!40000 ALTER TABLE `client_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `client_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment_product_notification`
--

DROP TABLE IF EXISTS `comment_product_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment_product_notification` (
  `id_product` varchar(200) NOT NULL,
  `user_name` varchar(200) NOT NULL,
  `status` tinyint(1) NOT NULL,
  PRIMARY KEY (`id_product`,`user_name`),
  KEY `key3_comment_product_notification_idx` (`user_name`),
  CONSTRAINT `key2_comment_product_notification` FOREIGN KEY (`id_product`) REFERENCES `product_const_properties` (`id_product`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `key3_comment_product_notification` FOREIGN KEY (`user_name`) REFERENCES `account` (`user_name`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_product_notification`
--

LOCK TABLES `comment_product_notification` WRITE;
/*!40000 ALTER TABLE `comment_product_notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment_product_notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location` (
  `id_shop` varchar(200) NOT NULL,
  `longitude` float NOT NULL,
  `latitude` float NOT NULL,
  PRIMARY KEY (`id_shop`),
  CONSTRAINT `key2_location` FOREIGN KEY (`id_shop`) REFERENCES `shop` (`id_shop`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location`
--

LOCK TABLES `location` WRITE;
/*!40000 ALTER TABLE `location` DISABLE KEYS */;
/*!40000 ALTER TABLE `location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `old_product`
--

DROP TABLE IF EXISTS `old_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `old_product` (
  `id_old_product` varchar(200) NOT NULL,
  `user_name` varchar(200) NOT NULL,
  `product_name` varchar(200) NOT NULL,
  `product_catalog` varchar(200) NOT NULL,
  `status` tinyint(1) NOT NULL,
  `transaction_place` varchar(200) DEFAULT NULL,
  `time` datetime NOT NULL,
  `description` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id_old_product`),
  KEY `key2_old_product_idx` (`user_name`),
  CONSTRAINT `key2_old_product` FOREIGN KEY (`user_name`) REFERENCES `account` (`user_name`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `old_product`
--

LOCK TABLES `old_product` WRITE;
/*!40000 ALTER TABLE `old_product` DISABLE KEYS */;
/*!40000 ALTER TABLE `old_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `old_product_images`
--

DROP TABLE IF EXISTS `old_product_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `old_product_images` (
  `id_image` varchar(200) NOT NULL,
  `id_old_product` varchar(200) NOT NULL,
  `data` blob NOT NULL,
  PRIMARY KEY (`id_image`,`id_old_product`),
  KEY `key2_old_product_image_idx` (`id_old_product`),
  CONSTRAINT `key2_old_product_image` FOREIGN KEY (`id_old_product`) REFERENCES `old_product` (`id_old_product`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `old_product_images`
--

LOCK TABLES `old_product_images` WRITE;
/*!40000 ALTER TABLE `old_product_images` DISABLE KEYS */;
/*!40000 ALTER TABLE `old_product_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `old_product_saler_info`
--

DROP TABLE IF EXISTS `old_product_saler_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `old_product_saler_info` (
  `id_old_product` varchar(200) NOT NULL,
  `saler_name` varchar(200) NOT NULL,
  `phone_number` varchar(50) NOT NULL,
  `email` varchar(200) NOT NULL,
  `city` varchar(50) NOT NULL,
  `county` varchar(200) NOT NULL,
  `address` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id_old_product`),
  CONSTRAINT `key2_old_product_saler_info` FOREIGN KEY (`id_old_product`) REFERENCES `old_product` (`id_old_product`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `old_product_saler_info`
--

LOCK TABLES `old_product_saler_info` WRITE;
/*!40000 ALTER TABLE `old_product_saler_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `old_product_saler_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_color`
--

DROP TABLE IF EXISTS `product_color`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_color` (
  `id_product` varchar(200) NOT NULL,
  `color` varchar(45) NOT NULL,
  PRIMARY KEY (`id_product`,`color`),
  CONSTRAINT `key2_product_color` FOREIGN KEY (`id_product`) REFERENCES `product_const_properties` (`id_product`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_color`
--

LOCK TABLES `product_color` WRITE;
/*!40000 ALTER TABLE `product_color` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_color` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_comments`
--

DROP TABLE IF EXISTS `product_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_comments` (
  `id_product` varchar(200) NOT NULL,
  `user_name` varchar(200) NOT NULL,
  `time` datetime NOT NULL,
  `comment` varchar(2000) NOT NULL,
  KEY `key2_product_comment_idx` (`id_product`),
  KEY `key3_product_comment_idx` (`user_name`),
  CONSTRAINT `key2_product_comment` FOREIGN KEY (`id_product`) REFERENCES `product_const_properties` (`id_product`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `key3_product_comment` FOREIGN KEY (`user_name`) REFERENCES `account` (`user_name`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_comments`
--

LOCK TABLES `product_comments` WRITE;
/*!40000 ALTER TABLE `product_comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_const_properties`
--

DROP TABLE IF EXISTS `product_const_properties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_const_properties` (
  `id_product` varchar(200) NOT NULL,
  `id_shop` varchar(200) NOT NULL,
  `product_name` varchar(200) NOT NULL,
  `product_catalog` varchar(200) NOT NULL,
  `trade_mark` varchar(200) DEFAULT NULL,
  `city` varchar(200) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id_product`),
  KEY `key2_product_const_properties_idx` (`id_shop`),
  CONSTRAINT `key2_product_const_properties` FOREIGN KEY (`id_shop`) REFERENCES `shop` (`id_shop`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_const_properties`
--

LOCK TABLES `product_const_properties` WRITE;
/*!40000 ALTER TABLE `product_const_properties` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_const_properties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_dynamic_properties`
--

DROP TABLE IF EXISTS `product_dynamic_properties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_dynamic_properties` (
  `id_product` varchar(200) NOT NULL,
  `price_product` float NOT NULL,
  `promotion_percent` float NOT NULL,
  `status` tinyint(1) NOT NULL,
  `num_of_like` int(11) NOT NULL,
  `num_of_comment` int(11) NOT NULL,
  `num_of_view` int(11) NOT NULL,
  PRIMARY KEY (`id_product`),
  CONSTRAINT `key2_product_dynamic_properties` FOREIGN KEY (`id_product`) REFERENCES `product_const_properties` (`id_product`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_dynamic_properties`
--

LOCK TABLES `product_dynamic_properties` WRITE;
/*!40000 ALTER TABLE `product_dynamic_properties` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_dynamic_properties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_images`
--

DROP TABLE IF EXISTS `product_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_images` (
  `id_image` varchar(200) NOT NULL,
  `id_product` varchar(200) NOT NULL,
  `data` blob NOT NULL,
  PRIMARY KEY (`id_image`,`id_product`),
  KEY `key2_product_images_idx` (`id_product`),
  CONSTRAINT `key2_product_images` FOREIGN KEY (`id_product`) REFERENCES `product_const_properties` (`id_product`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_images`
--

LOCK TABLES `product_images` WRITE;
/*!40000 ALTER TABLE `product_images` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_size`
--

DROP TABLE IF EXISTS `product_size`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_size` (
  `id_product` varchar(200) NOT NULL,
  `size` varchar(100) NOT NULL,
  PRIMARY KEY (`id_product`,`size`),
  CONSTRAINT `key2_product_size` FOREIGN KEY (`id_product`) REFERENCES `product_const_properties` (`id_product`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_size`
--

LOCK TABLES `product_size` WRITE;
/*!40000 ALTER TABLE `product_size` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_size` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shop`
--

DROP TABLE IF EXISTS `shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shop` (
  `id_shop` varchar(200) NOT NULL,
  `user_name` varchar(200) NOT NULL,
  `shop_name` varchar(200) NOT NULL,
  `city` varchar(200) NOT NULL,
  `address` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id_shop`),
  KEY `key2_shop_idx` (`user_name`),
  CONSTRAINT `key2_shop` FOREIGN KEY (`user_name`) REFERENCES `account` (`user_name`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop`
--

LOCK TABLES `shop` WRITE;
/*!40000 ALTER TABLE `shop` DISABLE KEYS */;
/*!40000 ALTER TABLE `shop` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shop_catalog`
--

DROP TABLE IF EXISTS `shop_catalog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shop_catalog` (
  `id_shop` varchar(200) NOT NULL,
  `catalog` varchar(200) NOT NULL,
  PRIMARY KEY (`id_shop`,`catalog`),
  CONSTRAINT `key2_shop_catalog` FOREIGN KEY (`id_shop`) REFERENCES `shop` (`id_shop`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop_catalog`
--

LOCK TABLES `shop_catalog` WRITE;
/*!40000 ALTER TABLE `shop_catalog` DISABLE KEYS */;
/*!40000 ALTER TABLE `shop_catalog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shop_dynamic_properties`
--

DROP TABLE IF EXISTS `shop_dynamic_properties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shop_dynamic_properties` (
  `id_shop` varchar(200) NOT NULL,
  `num_of_like` int(11) NOT NULL,
  `num_of_view` int(11) NOT NULL,
  PRIMARY KEY (`id_shop`),
  CONSTRAINT `key2_shop_dynamic_properties` FOREIGN KEY (`id_shop`) REFERENCES `shop` (`id_shop`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop_dynamic_properties`
--

LOCK TABLES `shop_dynamic_properties` WRITE;
/*!40000 ALTER TABLE `shop_dynamic_properties` DISABLE KEYS */;
/*!40000 ALTER TABLE `shop_dynamic_properties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shop_images`
--

DROP TABLE IF EXISTS `shop_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shop_images` (
  `id_image` varchar(200) NOT NULL,
  `id_shop` varchar(200) NOT NULL,
  `data` blob NOT NULL,
  PRIMARY KEY (`id_image`,`id_shop`),
  KEY `key2_shop_images_idx` (`id_shop`),
  CONSTRAINT `key2_shop_images` FOREIGN KEY (`id_shop`) REFERENCES `shop` (`id_shop`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop_images`
--

LOCK TABLES `shop_images` WRITE;
/*!40000 ALTER TABLE `shop_images` DISABLE KEYS */;
/*!40000 ALTER TABLE `shop_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_like_product`
--

DROP TABLE IF EXISTS `user_like_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_like_product` (
  `id_product` varchar(200) NOT NULL,
  `user_name` varchar(200) NOT NULL,
  PRIMARY KEY (`id_product`,`user_name`),
  KEY `key3_user_like_product_idx` (`user_name`),
  CONSTRAINT `key2_user_like_product` FOREIGN KEY (`id_product`) REFERENCES `product_const_properties` (`id_product`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `key3_user_like_product` FOREIGN KEY (`user_name`) REFERENCES `account` (`user_name`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_like_product`
--

LOCK TABLES `user_like_product` WRITE;
/*!40000 ALTER TABLE `user_like_product` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_like_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_like_shop`
--

DROP TABLE IF EXISTS `user_like_shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_like_shop` (
  `id_shop` varchar(200) NOT NULL,
  `user_name` varchar(200) NOT NULL,
  PRIMARY KEY (`id_shop`,`user_name`),
  KEY `key3_user_like_shop_idx` (`user_name`),
  CONSTRAINT `key2_user_like_shop` FOREIGN KEY (`id_shop`) REFERENCES `shop` (`id_shop`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `key3_user_like_shop` FOREIGN KEY (`user_name`) REFERENCES `account` (`user_name`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_like_shop`
--

LOCK TABLES `user_like_shop` WRITE;
/*!40000 ALTER TABLE `user_like_shop` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_like_shop` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_view_old_product_catalog`
--

DROP TABLE IF EXISTS `user_view_old_product_catalog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_view_old_product_catalog` (
  `user_name` varchar(200) NOT NULL,
  `product_catalog` varchar(200) NOT NULL,
  `num_of_view` int(11) NOT NULL,
  PRIMARY KEY (`user_name`,`product_catalog`),
  CONSTRAINT `key2_user_view_old_product_catalog` FOREIGN KEY (`user_name`) REFERENCES `account` (`user_name`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_view_old_product_catalog`
--

LOCK TABLES `user_view_old_product_catalog` WRITE;
/*!40000 ALTER TABLE `user_view_old_product_catalog` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_view_old_product_catalog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_view_product_catalog`
--

DROP TABLE IF EXISTS `user_view_product_catalog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_view_product_catalog` (
  `user_name` varchar(200) NOT NULL,
  `product_catalog` varchar(200) NOT NULL,
  `num_of_view` int(11) NOT NULL,
  PRIMARY KEY (`user_name`,`product_catalog`),
  CONSTRAINT `key2_user_view_product_catalog` FOREIGN KEY (`user_name`) REFERENCES `account` (`user_name`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_view_product_catalog`
--

LOCK TABLES `user_view_product_catalog` WRITE;
/*!40000 ALTER TABLE `user_view_product_catalog` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_view_product_catalog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_view_shop`
--

DROP TABLE IF EXISTS `user_view_shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_view_shop` (
  `id_shop` varchar(200) NOT NULL,
  `user_name` varchar(45) NOT NULL,
  `num_of_view` int(11) NOT NULL,
  PRIMARY KEY (`id_shop`,`user_name`),
  KEY `key2_user_view_shop_idx` (`user_name`),
  CONSTRAINT `key2_user_view_shop` FOREIGN KEY (`user_name`) REFERENCES `account` (`user_name`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `key3_user_view_shop` FOREIGN KEY (`id_shop`) REFERENCES `shop` (`id_shop`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_view_shop`
--

LOCK TABLES `user_view_shop` WRITE;
/*!40000 ALTER TABLE `user_view_shop` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_view_shop` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-04-03 22:53:30
