-- MySQL dump 10.13  Distrib 8.0.21, for Win64 (x86_64)
--
-- Host: localhost    Database: bankaccount
-- ------------------------------------------------------
-- Server version	5.7.26-log

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
-- Table structure for table `user_basic_details`
--

DROP TABLE IF EXISTS `user_basic_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_basic_details` (
  `userID` int(11) NOT NULL AUTO_INCREMENT,
  `user_tag` varchar(45) CHARACTER SET latin1 DEFAULT NULL,
  `user_name` varchar(80) CHARACTER SET latin1 NOT NULL,
  `dob` date NOT NULL,
  `gender` varchar(1) CHARACTER SET latin1 NOT NULL,
  `mobile_no` varchar(10) CHARACTER SET latin1 NOT NULL,
  `email_id` varchar(45) CHARACTER SET latin1 NOT NULL,
  `unique_id` varchar(45) CHARACTER SET latin1 NOT NULL,
  `c_address` varchar(100) CHARACTER SET latin1 NOT NULL,
  `p_address` varchar(100) CHARACTER SET latin1 NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `reference_user_code` varchar(45) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE KEY `user_tag_UNIQUE` (`user_tag`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='To store bank account user basic details';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_basic_details`
--

LOCK TABLES `user_basic_details` WRITE;
/*!40000 ALTER TABLE `user_basic_details` DISABLE KEYS */;
INSERT INTO `user_basic_details` VALUES (1,'LT0001','Raj','1988-02-19','M','9987654356','raj@g.co','765489223561','HB02/221,Shankar Marg, New Delhi 110043','HB02/221,Shankar Marg, New Delhi 110043','2020-08-05 16:28:40',NULL),(2,'LT0002','Shubam','1972-07-22','M','8897865467','subham@g.co','876989223645','B-02,Vikas Marg,New Delhi-110046','B-02,Vikas Marg,New Delhi-110046','2020-08-05 16:30:16',NULL),(3,'LT0003','Shivani','1990-05-20','F','7896759809','shivani@g.co','987654223897','HNo-05,SMS Marg,New Delhi-110001','HNo-05,SMS Marg,New Delhi-110001','2020-08-05 16:31:39',NULL),(4,'LT0004','Sanjay','1989-10-06','M','9943578920','sanjay@g.co','987973452342','A105,Shani Vihar,New Delhi-110001','A105,Shani Vihar,New Delhi-110001','2020-08-05 16:33:32',NULL),(5,'LT0005','Priya','1987-12-09','F','8876578920','priya@g.co','887233452342','Block-115,Amritha Vihar,New Delhi-110053','Block-115,Amritha Vihar,New Delhi-110053','2020-08-05 16:34:48',NULL);
/*!40000 ALTER TABLE `user_basic_details` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-08-06 11:55:09
