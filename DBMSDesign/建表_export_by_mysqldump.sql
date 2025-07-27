-- MySQL dump 10.13  Distrib 8.4.6, for Win64 (x86_64)
--
-- Host: localhost    Database: dbms
-- ------------------------------------------------------
-- Server version	8.4.6

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `engineering`
--

DROP TABLE IF EXISTS `engineering`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `engineering` (
  `EID` char(30) NOT NULL,
  `ENAME` char(30) DEFAULT NULL,
  `ETYPE` char(30) DEFAULT NULL,
  PRIMARY KEY (`EID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `engineering`
--

LOCK TABLES `engineering` WRITE;
/*!40000 ALTER TABLE `engineering` DISABLE KEYS */;
INSERT INTO `engineering` VALUES ('106700','','河道观测'),('106900','','河道观测'),('107000','','河道观测'),('107100','','河道观测'),('107160','','河道观测'),('107200','','降水观测'),('206800','','降水观测'),('206900','','降水观测'),('207000','','降水观测'),('207100','','降水观测'),('207110','','降水观测'),('207270','','降水观测'),('307130','','库湖观测'),('307165','','库湖观测'),('307181','','库湖观测'),('404310','','水质观测'),('404390','','水质观测'),('407120','','水质观测'),('407180','','水质观测'),('500100','','土壤墒情'),('501950','','土壤墒情');
/*!40000 ALTER TABLE `engineering` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manage_way`
--

DROP TABLE IF EXISTS `manage_way`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `manage_way` (
  `MANWAY` int NOT NULL,
  PRIMARY KEY (`MANWAY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manage_way`
--

LOCK TABLES `manage_way` WRITE;
/*!40000 ALTER TABLE `manage_way` DISABLE KEYS */;
INSERT INTO `manage_way` VALUES (1),(2),(3);
/*!40000 ALTER TABLE `manage_way` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `management_unit`
--

DROP TABLE IF EXISTS `management_unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `management_unit` (
  `MAGID` int NOT NULL,
  `MAGNAME` char(50) NOT NULL,
  `MAGLOC` char(50) DEFAULT NULL,
  PRIMARY KEY (`MAGID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `management_unit`
--

LOCK TABLES `management_unit` WRITE;
/*!40000 ALTER TABLE `management_unit` DISABLE KEYS */;
INSERT INTO `management_unit` VALUES (1,'长江水利委员会水文局',''),(2,'长江委水文局',''),(3,'三峡分中心','');
/*!40000 ALTER TABLE `management_unit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `precipitation`
--

DROP TABLE IF EXISTS `precipitation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `precipitation` (
  `EID` char(30) NOT NULL,
  `APRECIP` decimal(8,2) DEFAULT NULL,
  `HPRECIP` decimal(8,2) DEFAULT NULL,
  `WAY` char(20) DEFAULT NULL,
  PRIMARY KEY (`EID`),
  CONSTRAINT `precipitation_ibfk_1` FOREIGN KEY (`EID`) REFERENCES `engineering` (`EID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `precipitation`
--

LOCK TABLES `precipitation` WRITE;
/*!40000 ALTER TABLE `precipitation` DISABLE KEYS */;
INSERT INTO `precipitation` VALUES ('206800',1233.20,1754.40,''),('206900',1155.70,1779.90,''),('207000',1273.00,1895.30,''),('207100',998.70,1600.80,''),('207110',1254.30,1698.50,''),('207270',1134.40,1745.40,'');
/*!40000 ALTER TABLE `precipitation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservoir`
--

DROP TABLE IF EXISTS `reservoir`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservoir` (
  `EID` char(30) NOT NULL,
  `AWATERLINE` decimal(8,2) DEFAULT NULL,
  `HWATERLINE` decimal(8,2) DEFAULT NULL,
  `HEIGHT` decimal(8,2) DEFAULT NULL,
  `SIZE` decimal(8,2) DEFAULT NULL,
  PRIMARY KEY (`EID`),
  CONSTRAINT `reservoir_ibfk_1` FOREIGN KEY (`EID`) REFERENCES `engineering` (`EID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservoir`
--

LOCK TABLES `reservoir` WRITE;
/*!40000 ALTER TABLE `reservoir` DISABLE KEYS */;
/*!40000 ALTER TABLE `reservoir` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `river_information`
--

DROP TABLE IF EXISTS `river_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `river_information` (
  `RID` int NOT NULL,
  `RNAME` char(50) NOT NULL,
  `RREGION` char(50) NOT NULL,
  PRIMARY KEY (`RID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `river_information`
--

LOCK TABLES `river_information` WRITE;
/*!40000 ALTER TABLE `river_information` DISABLE KEYS */;
INSERT INTO `river_information` VALUES (1,'长江','长江上游下端'),(2,'长江','长江中游干流'),(3,'长江','长江干流'),(4,'黄柏河','黄柏河'),(5,'梅溪河','长江上游下端');
/*!40000 ALTER TABLE `river_information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `soil_moisture`
--

DROP TABLE IF EXISTS `soil_moisture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `soil_moisture` (
  `EID` char(30) NOT NULL,
  `SMOISTURE` char(20) DEFAULT NULL,
  `WAY` char(20) DEFAULT NULL,
  PRIMARY KEY (`EID`),
  CONSTRAINT `soil_moisture_ibfk_1` FOREIGN KEY (`EID`) REFERENCES `engineering` (`EID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `soil_moisture`
--

LOCK TABLES `soil_moisture` WRITE;
/*!40000 ALTER TABLE `soil_moisture` DISABLE KEYS */;
/*!40000 ALTER TABLE `soil_moisture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `station_encoding`
--

DROP TABLE IF EXISTS `station_encoding`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `station_encoding` (
  `IID` char(30) NOT NULL,
  `STTYPE` char(30) NOT NULL,
  `INAME` char(20) NOT NULL,
  `PYK` char(9) DEFAULT NULL,
  `STID` char(30) NOT NULL,
  `NT` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`IID`,`STTYPE`),
  KEY `IID_index` (`IID`),
  KEY `STTYPE_index` (`STTYPE`),
  CONSTRAINT `iid_constraint` CHECK (regexp_like(`IID`,_utf8mb4'[0-9]{3}.[0-9]{3,4}')),
  CONSTRAINT `sttpe_constraint` CHECK ((`STTYPE` in (_utf8mb4'基础水文',_utf8mb4'降水蒸发',_utf8mb4'实时雨水情',_utf8mb4'水质')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `station_encoding`
--

LOCK TABLES `station_encoding` WRITE;
/*!40000 ALTER TABLE `station_encoding` DISABLE KEYS */;
INSERT INTO `station_encoding` VALUES ('101.001','基础水文','111','222','333','爱说大话华盛顿'),('104.0001','基础水文','艾家镇','ajz','60107320',''),('104.0002','基础水文','徐州','bd','60106720',' '),('104.0002','实时雨水情','巴东','bd','60106700',' '),('104.0002','水质','巴东','bd','60106700',' '),('104.0002','降水蒸发','巴东','bd','60538700',' '),('104.0003','基础水文','坝河口','bhk','60107140',' '),('104.0003','实时雨水情','坝河口','bhk','60107140',' '),('104.0003','降水蒸发','坝河口','bhk','60540210',' '),('104.0004','基础水文','宝塔河','bth','60107310',' '),('104.0005','基础水文','风箱沱','fxt','60107191',' '),('104.002','基础水文','茅坪','mp','60107000',' '),('104.002','实时雨水情','茅坪','mp','60107000',' '),('104.0021','基础水文','梅溪河口','mxhk','60513500',' '),('104.0023','基础水文','庙咀','mj','60107270',' '),('104.0023','实时雨水情','庙咀','mj','60107270',' '),('104.0028','基础水文','三斗坪','sdp','60107100',' '),('104.0028','实时雨水情','三斗坪','sdp','60107100',' '),('104.0028','降水蒸发','三斗坪','sdp','60540200',' '),('104.0032','实时雨水情','太平溪','tpx','60106900',' '),('104.0033','基础水文','覃家沱','qjt','60107110',' '),('104.0033','实时雨水情','覃家沱','qjt','60107110',' '),('104.0037','水质','晓溪塔','xxt','60980100',' '),('104.0047','基础水文','中隔岛','zgd','60107070',' '),('111.1111','基础水文','管理员端测试-编辑','glydcs','66666666','测试'),('123.123','实时雨水情','123','123','123','123'),('342424.123','基础水文','123123','123213','123123','');
/*!40000 ALTER TABLE `station_encoding` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `station_engineering`
--

DROP TABLE IF EXISTS `station_engineering`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `station_engineering` (
  `IID` char(30) NOT NULL,
  `EID` char(30) NOT NULL,
  `NT` char(50) DEFAULT NULL,
  `STTYPE` char(30) NOT NULL,
  PRIMARY KEY (`IID`,`EID`),
  KEY `EID` (`EID`),
  KEY `station_engineering_ibfk_1` (`IID`,`STTYPE`),
  CONSTRAINT `station_engineering_ibfk_1` FOREIGN KEY (`IID`, `STTYPE`) REFERENCES `station_encoding` (`IID`, `STTYPE`),
  CONSTRAINT `station_engineering_ibfk_2` FOREIGN KEY (`EID`) REFERENCES `engineering` (`EID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `station_engineering`
--

LOCK TABLES `station_engineering` WRITE;
/*!40000 ALTER TABLE `station_engineering` DISABLE KEYS */;
/*!40000 ALTER TABLE `station_engineering` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `station_information`
--

DROP TABLE IF EXISTS `station_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `station_information` (
  `IID` char(30) NOT NULL,
  `STTYPE` char(30) NOT NULL,
  `STLEVEL` int DEFAULT NULL,
  `MAGWAY` int DEFAULT NULL,
  `NT` char(50) DEFAULT NULL,
  PRIMARY KEY (`IID`,`STTYPE`),
  KEY `STTYPE` (`STTYPE`),
  KEY `STLEVEL` (`STLEVEL`),
  KEY `MAGWAY` (`MAGWAY`),
  CONSTRAINT `station_information_ibfk_1` FOREIGN KEY (`IID`, `STTYPE`) REFERENCES `station_encoding` (`IID`, `STTYPE`),
  CONSTRAINT `station_information_ibfk_2` FOREIGN KEY (`STLEVEL`) REFERENCES `station_level` (`STLEVEL`),
  CONSTRAINT `station_information_ibfk_3` FOREIGN KEY (`MAGWAY`) REFERENCES `manage_way` (`MANWAY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `station_information`
--

LOCK TABLES `station_information` WRITE;
/*!40000 ALTER TABLE `station_information` DISABLE KEYS */;
INSERT INTO `station_information` VALUES ('104.0001','基础水文',1,2,''),('104.0002','基础水文',1,1,''),('104.0002','实时雨水情',1,2,''),('104.0002','水质',1,2,''),('104.0002','降水蒸发',1,2,''),('104.0003','基础水文',3,3,''),('104.0003','实时雨水情',3,3,''),('104.0003','降水蒸发',3,3,''),('104.0004','基础水文',4,1,''),('104.0005','基础水文',3,2,''),('104.002','基础水文',5,1,''),('104.002','实时雨水情',3,1,''),('104.0021','基础水文',5,2,''),('104.0023','基础水文',4,3,''),('104.0023','实时雨水情',1,2,''),('104.0028','基础水文',5,2,''),('104.0028','实时雨水情',1,1,''),('104.0028','降水蒸发',1,3,''),('104.0032','实时雨水情',4,2,''),('104.0033','基础水文',3,3,''),('104.0033','实时雨水情',5,1,''),('104.0037','水质',4,1,''),('104.0047','基础水文',4,2,'');
/*!40000 ALTER TABLE `station_information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `station_level`
--

DROP TABLE IF EXISTS `station_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `station_level` (
  `STLEVEL` int NOT NULL,
  PRIMARY KEY (`STLEVEL`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `station_level`
--

LOCK TABLES `station_level` WRITE;
/*!40000 ALTER TABLE `station_level` DISABLE KEYS */;
INSERT INTO `station_level` VALUES (1),(2),(3),(4),(5);
/*!40000 ALTER TABLE `station_level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `station_location`
--

DROP TABLE IF EXISTS `station_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `station_location` (
  `IID` char(30) NOT NULL,
  `STTYPE` char(30) NOT NULL,
  `RID` int NOT NULL,
  `LONGITUDE` decimal(12,5) DEFAULT NULL,
  `LATITUDE` decimal(12,5) DEFAULT NULL,
  `STLOC` char(50) DEFAULT NULL,
  `NT` char(50) DEFAULT NULL,
  PRIMARY KEY (`IID`,`STTYPE`),
  KEY `STTYPE` (`STTYPE`),
  KEY `RID` (`RID`),
  CONSTRAINT `station_location_ibfk_1` FOREIGN KEY (`IID`, `STTYPE`) REFERENCES `station_encoding` (`IID`, `STTYPE`),
  CONSTRAINT `station_location_ibfk_2` FOREIGN KEY (`RID`) REFERENCES `river_information` (`RID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `station_location`
--

LOCK TABLES `station_location` WRITE;
/*!40000 ALTER TABLE `station_location` DISABLE KEYS */;
INSERT INTO `station_location` VALUES ('104.0001','基础水文',2,111.34764,30.62350,'',''),('104.0002','基础水文',1,110.37453,31.04822,'',''),('104.0002','实时雨水情',1,110.37453,31.04822,'',''),('104.0002','水质',1,110.37453,31.04822,'',''),('104.0002','降水蒸发',1,110.37453,31.04822,'',''),('104.0003','基础水文',1,111.05128,30.83603,'',''),('104.0003','实时雨水情',1,111.05128,30.83603,'',''),('104.0003','降水蒸发',1,111.05128,30.83603,'',''),('104.0004','基础水文',2,111.31550,30.66936,'',''),('104.0005','基础水文',1,111.18860,30.77614,'',''),('104.002','基础水文',3,110.98339,30.83122,'',''),('104.002','实时雨水情',3,110.98339,30.83122,'',''),('104.0021','基础水文',5,109.52422,31.05036,'',''),('104.0023','基础水文',3,111.26864,30.70875,'',''),('104.0023','实时雨水情',3,111.26864,30.70875,'',''),('104.0028','基础水文',3,111.01936,30.81567,'',''),('104.0028','实时雨水情',3,111.01936,30.81567,'',''),('104.0028','降水蒸发',3,111.01936,30.81567,'',''),('104.0032','实时雨水情',1,110.95958,30.87728,'',''),('104.0033','基础水文',1,111.01444,30.82311,'',''),('104.0033','实时雨水情',1,111.01444,30.82311,'',''),('104.0037','水质',4,111.31111,30.76944,'',''),('104.0047','基础水文',1,111.03608,30.82897,'','');
/*!40000 ALTER TABLE `station_location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `station_management`
--

DROP TABLE IF EXISTS `station_management`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `station_management` (
  `IID` char(30) NOT NULL,
  `STTYPE` char(30) NOT NULL,
  `REGION` int DEFAULT NULL,
  `MAGID` int DEFAULT NULL,
  PRIMARY KEY (`IID`,`STTYPE`),
  KEY `STTYPE` (`STTYPE`),
  KEY `MAGID` (`MAGID`),
  CONSTRAINT `station_management_ibfk_1` FOREIGN KEY (`IID`, `STTYPE`) REFERENCES `station_encoding` (`IID`, `STTYPE`),
  CONSTRAINT `station_management_ibfk_2` FOREIGN KEY (`MAGID`) REFERENCES `management_unit` (`MAGID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `station_management`
--

LOCK TABLES `station_management` WRITE;
/*!40000 ALTER TABLE `station_management` DISABLE KEYS */;
INSERT INTO `station_management` VALUES ('104.0001','基础水文',0,1),('104.0002','基础水文',0,2),('104.0002','实时雨水情',0,2),('104.0002','水质',0,2),('104.0002','降水蒸发',0,2),('104.0003','基础水文',0,1),('104.0003','实时雨水情',0,1),('104.0003','降水蒸发',0,1),('104.0004','基础水文',0,1),('104.0005','基础水文',0,1),('104.002','基础水文',0,3),('104.002','实时雨水情',0,3),('104.0021','基础水文',0,1),('104.0023','基础水文',0,3),('104.0023','实时雨水情',0,3),('104.0028','基础水文',0,3),('104.0028','实时雨水情',0,3),('104.0028','降水蒸发',0,3),('104.0032','实时雨水情',0,1),('104.0033','基础水文',0,1),('104.0033','实时雨水情',0,1),('104.0037','水质',0,2),('104.0047','基础水文',0,1);
/*!40000 ALTER TABLE `station_management` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `station_view1`
--

DROP TABLE IF EXISTS `station_view1`;
/*!50001 DROP VIEW IF EXISTS `station_view1`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `station_view1` AS SELECT 
 1 AS `编码`,
 1 AS `站名`,
 1 AS `管理机构`,
 1 AS `东经`,
 1 AS `北纬`,
 1 AS `流域水系`,
 1 AS `河流名称`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `station_view2`
--

DROP TABLE IF EXISTS `station_view2`;
/*!50001 DROP VIEW IF EXISTS `station_view2`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `station_view2` AS SELECT 
 1 AS `编码`,
 1 AS `站名`,
 1 AS `基础水文`,
 1 AS `降水蒸发`,
 1 AS `实时雨水情`,
 1 AS `水质`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `station_view_all`
--

DROP TABLE IF EXISTS `station_view_all`;
/*!50001 DROP VIEW IF EXISTS `station_view_all`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `station_view_all` AS SELECT 
 1 AS `编码`,
 1 AS `站名`,
 1 AS `管理机构`,
 1 AS `东经`,
 1 AS `北纬`,
 1 AS `流域水系`,
 1 AS `河流名称`,
 1 AS `基础水文`,
 1 AS `降水蒸发`,
 1 AS `实时雨水情`,
 1 AS `水质`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `station_viewx`
--

DROP TABLE IF EXISTS `station_viewx`;
/*!50001 DROP VIEW IF EXISTS `station_viewx`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `station_viewx` AS SELECT 
 1 AS `编码`,
 1 AS `站名`,
 1 AS `基础水文`,
 1 AS `降水蒸发`,
 1 AS `实时雨水情`,
 1 AS `水质`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usr` varchar(50) NOT NULL,
  `pwd` varchar(50) NOT NULL,
  `is_admin` tinyint(1) NOT NULL,
  `is_login` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (26,'bella','1234',1,0),(34,'diana','1234',1,0),(35,'1234','1234',0,0),(36,'5678','5678',0,0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `water_quality`
--

DROP TABLE IF EXISTS `water_quality`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `water_quality` (
  `EID` char(30) NOT NULL,
  `WQUALITY` char(20) DEFAULT NULL,
  `WAY` char(20) DEFAULT NULL,
  PRIMARY KEY (`EID`),
  CONSTRAINT `water_quality_ibfk_1` FOREIGN KEY (`EID`) REFERENCES `engineering` (`EID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `water_quality`
--

LOCK TABLES `water_quality` WRITE;
/*!40000 ALTER TABLE `water_quality` DISABLE KEYS */;
/*!40000 ALTER TABLE `water_quality` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `watercourse`
--

DROP TABLE IF EXISTS `watercourse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `watercourse` (
  `EID` char(30) NOT NULL,
  `HWATERLINE` decimal(8,2) DEFAULT NULL,
  `LWATERLINE` decimal(8,2) DEFAULT NULL,
  PRIMARY KEY (`EID`),
  CONSTRAINT `watercourse_ibfk_1` FOREIGN KEY (`EID`) REFERENCES `engineering` (`EID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `watercourse`
--

LOCK TABLES `watercourse` WRITE;
/*!40000 ALTER TABLE `watercourse` DISABLE KEYS */;
INSERT INTO `watercourse` VALUES ('106700',175.04,54.70),('106900',175.06,42.10),('107000',175.05,40.10),('107160',73.35,63.40),('107200',67.03,59.10);
/*!40000 ALTER TABLE `watercourse` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `station_view1`
--

/*!50001 DROP VIEW IF EXISTS `station_view1`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `station_view1` (`编码`,`站名`,`管理机构`,`东经`,`北纬`,`流域水系`,`河流名称`) AS select distinct `station_encoding`.`IID` AS `IID`,`station_encoding`.`INAME` AS `INAME`,`management_unit`.`MAGNAME` AS `MAGNAME`,`station_location`.`LONGITUDE` AS `LONGITUDE`,`station_location`.`LATITUDE` AS `LATITUDE`,`river_information`.`RREGION` AS `RREGION`,`river_information`.`RNAME` AS `RNAME` from ((((`station_encoding` join `station_location`) join `station_management`) join `management_unit`) join `river_information`) where ((`station_encoding`.`IID` = `station_location`.`IID`) and (`station_encoding`.`STTYPE` = `station_location`.`STTYPE`) and (`station_location`.`IID` = `station_management`.`IID`) and (`station_location`.`STTYPE` = `station_management`.`STTYPE`) and (`station_management`.`MAGID` = `management_unit`.`MAGID`) and (`station_location`.`RID` = `river_information`.`RID`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `station_view2`
--

/*!50001 DROP VIEW IF EXISTS `station_view2`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `station_view2` (`编码`,`站名`,`基础水文`,`降水蒸发`,`实时雨水情`,`水质`) AS select distinct `x`.`IID` AS `IID`,`x`.`INAME` AS `INAME`,`x1`.`STID` AS `STID`,`y`.`STID` AS `STID`,`z`.`STID` AS `STID`,`w`.`STID` AS `STID` from ((((`station_encoding` `x` left join `station_encoding` `x1` on(((`x`.`IID` = `x1`.`IID`) and (`x1`.`STTYPE` = '基础水文')))) left join `station_encoding` `y` on(((`x`.`IID` = `y`.`IID`) and (`y`.`STTYPE` = '降水蒸发')))) left join `station_encoding` `z` on(((`x`.`IID` = `z`.`IID`) and (`z`.`STTYPE` = '实时雨水情')))) left join `station_encoding` `w` on(((`x`.`IID` = `w`.`IID`) and (`w`.`STTYPE` = '水质')))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `station_view_all`
--

/*!50001 DROP VIEW IF EXISTS `station_view_all`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `station_view_all` (`编码`,`站名`,`管理机构`,`东经`,`北纬`,`流域水系`,`河流名称`,`基础水文`,`降水蒸发`,`实时雨水情`,`水质`) AS select distinct `station_view1`.`编码` AS `编码`,`station_view1`.`站名` AS `站名`,`station_view1`.`管理机构` AS `管理机构`,`station_view1`.`东经` AS `东经`,`station_view1`.`北纬` AS `北纬`,`station_view1`.`流域水系` AS `流域水系`,`station_view1`.`河流名称` AS `河流名称`,`station_view2`.`基础水文` AS `基础水文`,`station_view2`.`降水蒸发` AS `降水蒸发`,`station_view2`.`实时雨水情` AS `实时雨水情`,`station_view2`.`水质` AS `水质` from (`station_view1` join `station_view2`) where (`station_view1`.`编码` = `station_view2`.`编码`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `station_viewx`
--

/*!50001 DROP VIEW IF EXISTS `station_viewx`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `station_viewx` (`编码`,`站名`,`基础水文`,`降水蒸发`,`实时雨水情`,`水质`) AS select distinct `x`.`IID` AS `IID`,`x`.`INAME` AS `INAME`,`x`.`STID` AS `STID`,`y`.`STID` AS `STID`,`z`.`STID` AS `STID`,`w`.`STID` AS `STID` from (((`station_encoding` `x` join `station_encoding` `y`) join `station_encoding` `z`) join `station_encoding` `w`) where ((`x`.`IID` = `y`.`IID`) and (`x`.`IID` = `z`.`IID`) and (`x`.`IID` = `w`.`IID`) and ((`x`.`STTYPE` = '基础水文') or (`y`.`STTYPE` = '降水蒸发') or (`z`.`STTYPE` = '实时雨水情') or (`w`.`STTYPE` = '水质'))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-27 20:38:19
