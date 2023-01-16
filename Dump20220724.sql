-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: localhost    Database: ams
-- ------------------------------------------------------
-- Server version	5.7.17-log

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
-- Table structure for table `apartment_details`
--

DROP TABLE IF EXISTS `apartment_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apartment_details` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `APARTMENT_NAME` varchar(45) NOT NULL,
  `APARTMENT_ADDRESS_1` varchar(45) DEFAULT NULL,
  `APARTMENT_ADDRESS_2` varchar(45) DEFAULT NULL,
  `PIN_CODE` int(11) DEFAULT NULL,
  `CREATED_BY` varchar(45) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` varchar(45) DEFAULT NULL,
  `MODIFIED_DATE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apartment_details`
--

LOCK TABLES `apartment_details` WRITE;
/*!40000 ALTER TABLE `apartment_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `apartment_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flat_details`
--

DROP TABLE IF EXISTS `flat_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flat_details` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FLAT_NO` varchar(45) NOT NULL,
  `BLOCK` varchar(45) DEFAULT NULL,
  `FLAT_SIZE_SQFT` int(11) DEFAULT NULL,
  `NO_OF_ROOMS` int(11) DEFAULT NULL,
  `FLOOR_NO` int(11) DEFAULT NULL,
  `FLAT_TYPE` varchar(45) DEFAULT NULL,
  `CREATED_BY` varchar(45) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` varchar(45) DEFAULT NULL,
  `MODIFIED_DATE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flat_details`
--

LOCK TABLES `flat_details` WRITE;
/*!40000 ALTER TABLE `flat_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `flat_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_apartment_flat`
--

DROP TABLE IF EXISTS `link_apartment_flat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `link_apartment_flat` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `APARTMENT_ID` int(11) DEFAULT NULL,
  `FLAT_ID` int(11) DEFAULT NULL,
  `CREATED_BY` varchar(45) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` varchar(45) DEFAULT NULL,
  `MODIFIED_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FLAT_ID_idx` (`FLAT_ID`),
  KEY `APARTMENT_ID_idx` (`APARTMENT_ID`),
  CONSTRAINT `APARTMENT_ID` FOREIGN KEY (`APARTMENT_ID`) REFERENCES `apartment_details` (`ID`),
  CONSTRAINT `FLAT_ID` FOREIGN KEY (`FLAT_ID`) REFERENCES `flat_details` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_apartment_flat`
--

LOCK TABLES `link_apartment_flat` WRITE;
/*!40000 ALTER TABLE `link_apartment_flat` DISABLE KEYS */;
/*!40000 ALTER TABLE `link_apartment_flat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_flat_user`
--

DROP TABLE IF EXISTS `link_flat_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `link_flat_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FLAT_ID` int(11) DEFAULT NULL,
  `USER_ID` int(11) DEFAULT NULL,
  `CREATED_BY` varchar(45) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` varchar(45) DEFAULT NULL,
  `MODIFIED_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FLAT_ID_idx` (`FLAT_ID`),
  KEY `USER_ID_idx` (`USER_ID`),
  CONSTRAINT `FLAT_ID_1` FOREIGN KEY (`FLAT_ID`) REFERENCES `flat_details` (`ID`),
  CONSTRAINT `USER_ID` FOREIGN KEY (`USER_ID`) REFERENCES `user_details` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_flat_user`
--

LOCK TABLES `link_flat_user` WRITE;
/*!40000 ALTER TABLE `link_flat_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `link_flat_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `maintenance`
--

DROP TABLE IF EXISTS `maintenance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `maintenance` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PAYMENT_MODE` int(11) DEFAULT NULL,
  `AMOUNT` decimal(15,2) DEFAULT NULL,
  `PAYMENT_DATE` datetime DEFAULT NULL,
  `REMARKS` varchar(45) DEFAULT NULL,
  `PAYMENT_BY` int(11) DEFAULT NULL,
  `CREATED_BY` varchar(45) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` varchar(45) DEFAULT NULL,
  `MODIFIED_DATE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  CONSTRAINT `MAINTENANCE_ID_FK_MD` FOREIGN KEY (`ID`) REFERENCES `maintenance_details` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `maintenance`
--

LOCK TABLES `maintenance` WRITE;
/*!40000 ALTER TABLE `maintenance` DISABLE KEYS */;
/*!40000 ALTER TABLE `maintenance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `maintenance_details`
--

DROP TABLE IF EXISTS `maintenance_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `maintenance_details` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MAINTENANCE_ID` int(11) NOT NULL,
  `FLAT_ID` int(11) NOT NULL,
  `AMOUNT` decimal(15,2) DEFAULT NULL,
  `PAYMENT_DATE` int(11) DEFAULT NULL,
  `PAYMENT_MONTH` int(11) DEFAULT NULL,
  `PAYMENT_YEAR` int(11) DEFAULT NULL,
  `PAYMENT_BY` int(11) DEFAULT NULL,
  `CREATED_BY` varchar(45) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` varchar(45) DEFAULT NULL,
  `MODIFIED_DATE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  CONSTRAINT `FLAT_ID_FK_MN` FOREIGN KEY (`ID`) REFERENCES `flat_details` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `maintenance_details`
--

LOCK TABLES `maintenance_details` WRITE;
/*!40000 ALTER TABLE `maintenance_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `maintenance_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `maintenance_master`
--

DROP TABLE IF EXISTS `maintenance_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `maintenance_master` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MAINTENANCE_AMOUNT` decimal(15,3) NOT NULL,
  `FROM_DATE` datetime DEFAULT NULL,
  `TO_DATE` datetime DEFAULT NULL,
  `IS_ACTIVE` int(11) DEFAULT NULL,
  `CREATED_BY` varchar(45) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` varchar(45) DEFAULT NULL,
  `MODIFIED_DATE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `maintenance_master`
--

LOCK TABLES `maintenance_master` WRITE;
/*!40000 ALTER TABLE `maintenance_master` DISABLE KEYS */;
/*!40000 ALTER TABLE `maintenance_master` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_master`
--

DROP TABLE IF EXISTS `payment_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_master` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PAYMENT_MODE` varchar(45) NOT NULL,
  `IS_ACTIVE` int(11) DEFAULT NULL,
  `CREATED_BY` varchar(45) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` varchar(45) DEFAULT NULL,
  `MODIFIED_DATE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_master`
--

LOCK TABLES `payment_master` WRITE;
/*!40000 ALTER TABLE `payment_master` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_master` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_details`
--

DROP TABLE IF EXISTS `user_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_details` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LOGIN_ID` varchar(45) DEFAULT NULL,
  `FIRST_NAME` varchar(45) DEFAULT NULL,
  `MIDDLE_NAME` varchar(45) DEFAULT NULL,
  `LAST_NAME` varchar(45) DEFAULT NULL,
  `ADHAR_CARD_NO` varchar(45) DEFAULT NULL,
  `USER_ADDRESS` varchar(45) DEFAULT NULL,
  `CONTACT_NO_1` varchar(45) DEFAULT NULL,
  `CONTACT_NO_2` varchar(45) DEFAULT NULL,
  `EMAIL_ID` varchar(45) DEFAULT NULL,
  `IS_DELETED` int(11) DEFAULT NULL,
  `IS_ACTIVE` tinyint(1) DEFAULT '0',
  `CREATED_BY` varchar(45) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` varchar(45) DEFAULT NULL,
  `MODIFIED_DATE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_details`
--

LOCK TABLES `user_details` WRITE;
/*!40000 ALTER TABLE `user_details` DISABLE KEYS */;
INSERT INTO `user_details` VALUES (71,NULL,'Ram',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(72,NULL,'Saurav',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(73,NULL,'Rahul',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(74,NULL,'Rahul 1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(75,NULL,'Rahul 3',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(76,NULL,'Pradyut',NULL,'Sarkar',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(77,NULL,'Pradyut',NULL,'Sarkar',NULL,NULL,'9681819652','9681819652','pradyut.bca15@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL),(78,'raj_s@ymail.com','Rajdeep',NULL,'MMMM',NULL,NULL,'5555555555','2222222222','raj@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL),(83,'abc@aaa.com','Rajdeep',NULL,'M',NULL,NULL,'4444444444','2222222222','abc@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `user_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_type_master`
--

DROP TABLE IF EXISTS `user_type_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_type_master` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ROLE` varchar(45) NOT NULL,
  `IS_ACTIVE` int(11) DEFAULT NULL,
  `CREATED_BY` varchar(45) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` varchar(45) DEFAULT NULL,
  `MODIFIED_DATE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_type_master`
--

LOCK TABLES `user_type_master` WRITE;
/*!40000 ALTER TABLE `user_type_master` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_type_master` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-07-24  3:17:26
