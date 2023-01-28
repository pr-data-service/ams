-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: localhost    Database: ams-prod
-- ------------------------------------------------------
-- Server version	5.7.17-log

--
-- Table structure for table `apartment_details`
--

DROP TABLE IF EXISTS `apartment_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apartment_details` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  `ADDRESS1` varchar(200) DEFAULT NULL,
  `ADDRESS2` varchar(200) DEFAULT NULL,
  `PIN_CODE` varchar(45) NOT NULL,
  `STATE` varchar(45) NOT NULL,
  `IS_ACTIVE` tinyint(1) DEFAULT '1',
  `USER_ID` int(11) DEFAULT NULL,
  `CREATED_BY` varchar(45) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` varchar(45) DEFAULT NULL,
  `MODIFIED_DATE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;


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
  `APARTMENT_ID` int(11) DEFAULT NULL,
  `CREATED_BY` int(11) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` int(11) DEFAULT NULL,
  `MODIFIED_DATE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  KEY `KEY_FLAT_ID_idx` (`FLAT_ID`),
  KEY `APARTMENT_ID_idx` (`APARTMENT_ID`),
  CONSTRAINT `FK_link_apartment_flat.APARTMENT_ID` FOREIGN KEY (`APARTMENT_ID`) REFERENCES `apartment_details` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_link_apartment_flat.FLAT_ID` FOREIGN KEY (`FLAT_ID`) REFERENCES `flat_details` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;


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
  `USER_ADDRESS` varchar(300) DEFAULT NULL,
  `CONTACT_NO_1` varchar(45) DEFAULT NULL,
  `CONTACT_NO_2` varchar(45) DEFAULT NULL,
  `EMAIL_ID` varchar(45) DEFAULT NULL,
  `PASSWORD` varchar(45) DEFAULT NULL,
  `APARTMENT_ID` int(11) DEFAULT NULL,
  `IS_DELETED` tinyint(1) DEFAULT NULL,
  `IS_ACTIVE` tinyint(1) DEFAULT '0',
  `CREATED_BY` int(11) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` int(11) DEFAULT NULL,
  `MODIFIED_DATE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link_flat_details_user_details`
--

DROP TABLE IF EXISTS `link_flat_details_user_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `link_flat_details_user_details` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FLAT_ID` int(11) DEFAULT NULL,
  `USER_ID` int(11) DEFAULT NULL,
  `LINK_DATE` datetime DEFAULT NULL,
  `UNLINK_DATE` datetime DEFAULT NULL,
  `APARTMENT_ID` int(11) DEFAULT NULL,
  `IS_DELETED` tinyint(1) DEFAULT NULL,
  `IS_ACTIVE` tinyint(1) DEFAULT '0',
  `CREATED_BY` int(11) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` int(11) DEFAULT NULL,
  `MODIFIED_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `KY_FLAT_ID_idx` (`FLAT_ID`),
  KEY `KY_USER_ID_idx` (`USER_ID`),
  CONSTRAINT `FK_link_flat_details_user_details.FLAT_ID` FOREIGN KEY (`FLAT_ID`) REFERENCES `flat_details` (`ID`),
  CONSTRAINT `FK_link_flat_details_user_details.USER_ID` FOREIGN KEY (`USER_ID`) REFERENCES `user_details` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;



DROP TABLE IF EXISTS `session_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `session_details` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  `FROM_DATE` date NOT NULL,
  `TO_DATE` date NOT NULL,
  `IS_ACTIVE` tinyint(1) DEFAULT '1',
  `APARTMENT_ID` int(11) DEFAULT NULL,
  `CREATED_BY` varchar(45) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` varchar(45) DEFAULT NULL,
  `MODIFIED_DATE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

--
-- Table structure for table `maintenance`
--

DROP TABLE IF EXISTS `maintenance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `maintenance` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `AMOUNT` decimal(15,3) NOT NULL,
  `FLAT_ID` int(11) DEFAULT NULL,
  `SESSION_ID` int(11) DEFAULT NULL,
  `APARTMENT_ID` int(11) DEFAULT NULL,
  `LAST_ACTIVE_MAINT_ID` int(11) DEFAULT '0',
  `IS_ACTIVE` int(1) DEFAULT '1',
  `CREATED_BY` int(11) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` int(11) DEFAULT NULL,
  `MODIFIED_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `KY_FLAT_ID_idx` (`FLAT_ID`),
  KEY `KY_SESSION_ID_idx` (`SESSION_ID`),
  KEY `KY_APARTMENT_ID_idx` (`APARTMENT_ID`),
  CONSTRAINT `FK_maintenance.APARTMENT_ID` FOREIGN KEY (`APARTMENT_ID`) REFERENCES `apartment_details` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_maintenance.FLAT_ID` FOREIGN KEY (`FLAT_ID`) REFERENCES `flat_details` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_maintenance.SESSION_ID` FOREIGN KEY (`SESSION_ID`) REFERENCES `session_details` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `events`
--

DROP TABLE IF EXISTS `events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `events` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` varchar(200) DEFAULT NULL,
  `AMOUNT_PER_FLAT` decimal(15,2) DEFAULT NULL,
  `TARGET_AMOUNT` decimal(15,2) DEFAULT NULL,
  `IS_ACTIVE` int(11) DEFAULT NULL,
  `APARTMENT_ID` int(11) DEFAULT NULL,
  `CREATED_BY` varchar(45) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` varchar(45) DEFAULT NULL,
  `MODIFIED_DATE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `expense_head`
--

DROP TABLE IF EXISTS `expense_head`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expense_head` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `EVENT_ID` int(11) DEFAULT NULL,
  `TITLE` varchar(100) DEFAULT NULL,
  `DESCRIPTION` varchar(200) DEFAULT NULL,
  `SESSION_ID` int(11) DEFAULT NULL,
  `APARTMENT_ID` int(11) DEFAULT NULL,
  `CREATED_BY` int(11) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` int(11) DEFAULT NULL,
  `MODIFIED_DATE` varchar(45) DEFAULT NULL,
  `remarks` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `expenses`
--

DROP TABLE IF EXISTS `expenses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expenses` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TITLE` varchar(90) CHARACTER SET utf8 DEFAULT NULL,
  `ACCOUNT_NO` varchar(20) DEFAULT NULL,
  `AMOUNT` decimal(15,2) DEFAULT NULL,
  `DESCRIPTION` varchar(180) CHARACTER SET utf8 DEFAULT NULL,
  `EXPENSE_DATE` datetime DEFAULT NULL,
  `VOUCHER_NO` varchar(255) DEFAULT NULL,
  `PAYMENT_MODE` varchar(45) DEFAULT NULL,
  `EVENT_ID` int(11) DEFAULT NULL,
  `SESSION_ID` int(11) DEFAULT NULL,
  `APARTMENT_ID` int(11) DEFAULT NULL,
  `CREATED_BY` int(11) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` int(11) DEFAULT NULL,
  `MODIFIED_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `KY_EVENT_ID_idx` (`EVENT_ID`),
  CONSTRAINT `FK_expenses.EVENT_ID` FOREIGN KEY (`EVENT_ID`) REFERENCES `events` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `expense_items`
--

DROP TABLE IF EXISTS `expense_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expense_items` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `EXPENSE_ID` int(11) DEFAULT NULL,
  `ITEM_HEAD` varchar(90) CHARACTER SET utf8 DEFAULT NULL,
  `AMOUNT` decimal(15,2) DEFAULT NULL,
  `SESSION_ID` int(11) DEFAULT NULL,
  `APARTMENT_ID` int(11) DEFAULT NULL,
  `CREATED_BY` int(11) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` int(11) DEFAULT NULL,
  `MODIFIED_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `KY_EXPENSE_idx` (`EXPENSE_ID`),
  CONSTRAINT `FK_expense_items.EXPENSE_ID` FOREIGN KEY (`EXPENSE_ID`) REFERENCES `expenses` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notes`
--

DROP TABLE IF EXISTS `notes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notes` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NOTE_TYPE` varchar(45) NOT NULL DEFAULT 'NOTE',
  `TITLE` varchar(45) NOT NULL,
  `NOTE_TEXT` varchar(600) DEFAULT NULL,
  `PARENT_OBJECT` varchar(45) NOT NULL,
  `PARENT_RECORD_ID` int(11) NOT NULL,
  `SESSION_ID` int(11) DEFAULT NULL,
  `APARTMENT_ID` int(11) DEFAULT NULL,
  `CREATED_BY` varchar(45) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` varchar(45) DEFAULT NULL,
  `MODIFIED_DATE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FLAT_ID` int(11) DEFAULT NULL,
  `PAYMENT_MODE` varchar(45) DEFAULT NULL,
  `PAYMENT_MODE_REF` varchar(45) DEFAULT NULL,
  `AMOUNT` decimal(15,2) DEFAULT NULL,
  `PAYMENT_DATE` date DEFAULT NULL,
  `REMARKS` varchar(200) DEFAULT NULL,
  `IS_CANCELED` tinyint(1) DEFAULT '0',
  `CANCEL_REMARKS` varchar(180) DEFAULT NULL,
  `PAYMENT_BY` int(11) DEFAULT NULL,
  `BILL_NO` varchar(45) DEFAULT NULL,
  `SESSION_ID` int(11) DEFAULT NULL,
  `APARTMENT_ID` int(11) DEFAULT NULL,
  `CREATED_BY` int(11) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` int(11) DEFAULT NULL,
  `MODIFIED_DATE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_payment.FLAT_ID` FOREIGN KEY (`FLAT_ID`) REFERENCES `flat_details` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment_bill_no`
--

DROP TABLE IF EXISTS `payment_bill_no`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_bill_no` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BILL_NO` int(11) NOT NULL,
  `SESSION_ID` int(11) DEFAULT NULL,
  `APARTMENT_ID` int(11) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment_details`
--

DROP TABLE IF EXISTS `payment_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_details` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PAYMENT_ID` int(11) NOT NULL,
  `FLAT_ID` int(11) NOT NULL,
  `AMOUNT` decimal(15,2) DEFAULT NULL,
  `PAYMENT_DATE` date DEFAULT NULL,
  `PAYMENT_MONTH` int(11) DEFAULT NULL,
  `PAYMENT_YEAR` int(11) DEFAULT NULL,
  `PAYMENT_BY` int(11) DEFAULT NULL,
  `EVENT_ID` int(11) DEFAULT NULL,
  `PAYMENT_FOR_SESSION_ID` int(11) NOT NULL,
  `IS_CANCELED` tinyint(1) DEFAULT '0',
  `SESSION_ID` int(11) DEFAULT NULL,
  `APARTMENT_ID` int(11) DEFAULT NULL,
  `CREATED_BY` varchar(45) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` varchar(45) DEFAULT NULL,
  `MODIFIED_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `KY_PAYMENT_TYPE_idx` (`EVENT_ID`),
  CONSTRAINT `FK_payment_details.EVENT_ID` FOREIGN KEY (`EVENT_ID`) REFERENCES `events` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment_mode`
--

DROP TABLE IF EXISTS `payment_mode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_mode` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MODE` varchar(45) NOT NULL,
  `APARTMENT_ID` int(11) DEFAULT NULL,
  `IS_ACTIVE` int(11) DEFAULT NULL,
  `CREATED_BY` varchar(45) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` varchar(45) DEFAULT NULL,
  `MODIFIED_DATE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment_type_master`
--

DROP TABLE IF EXISTS `payment_type_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_type_master` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TYPE` varchar(45) NOT NULL,
  `DESCRIPTION` varchar(200) DEFAULT NULL,
  `APARTMENT_ID` int(11) DEFAULT NULL,
  `IS_ACTIVE` int(11) DEFAULT NULL,
  `CREATED_BY` varchar(45) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_BY` varchar(45) DEFAULT NULL,
  `MODIFIED_DATE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment_voucher_no`
--

DROP TABLE IF EXISTS `payment_voucher_no`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_voucher_no` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `VOUCHER_NO` int(11) NOT NULL,
  `SESSION_ID` int(11) DEFAULT NULL,
  `APARTMENT_ID` int(11) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `session_details`
--


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-01-28 16:27:17