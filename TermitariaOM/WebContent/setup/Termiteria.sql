CREATE DATABASE  IF NOT EXISTS `audit` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `audit`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: audit
-- ------------------------------------------------------
-- Server version	5.5.10

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
-- Table structure for table `auditom`
--

DROP TABLE IF EXISTS `auditom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auditom` (
  `AUDITOMID` int(11) NOT NULL AUTO_INCREMENT,
  `EVENT` varchar(100) NOT NULL,
  `DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `PERSONID` int(11) DEFAULT NULL,
  `MESSAGE_RO` varchar(1000) NOT NULL,
  `ORGANISATIONID` int(11) NOT NULL,
  `MESSAGE_EN` varchar(1000) DEFAULT NULL,
  `FIRSTNAME` varchar(1000) NOT NULL,
  `LASTNAME` varchar(1000) NOT NULL,
  PRIMARY KEY (`AUDITOMID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `auditdm`
--

DROP TABLE IF EXISTS `auditdm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auditdm` (
  `AUDITDMID` int(11) NOT NULL AUTO_INCREMENT,
  `EVENT` varchar(100) NOT NULL,
  `DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `PERSONID` int(11) NOT NULL,
  `MESSAGE_RO` varchar(1000) DEFAULT NULL,
  `MESSAGE_EN` varchar(1000) DEFAULT NULL,
  `ORGANISATIONID` int(11) NOT NULL,
  `FIRNSTNAME` varchar(100) NOT NULL,
  `LASTNAME` varchar(100) NOT NULL,
  PRIMARY KEY (`AUDITDMID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `auditts`
--

DROP TABLE IF EXISTS `auditts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auditts` (
  `AUDITTSID` int(11) NOT NULL AUTO_INCREMENT,
  `EVENT` varchar(100) NOT NULL,
  `DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `PERSONID` int(11) NOT NULL,
  `MESSAGE_RO` varchar(1000) DEFAULT NULL,
  `ORGANISATIONID` int(11) NOT NULL,
  `MESSAGE_EN` varchar(1000) DEFAULT NULL,
  `FIRSTNAME` varchar(1000) NOT NULL,
  `LASTNAME` varchar(1000) NOT NULL,
  PRIMARY KEY (`AUDITTSID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `auditcm`
--

DROP TABLE IF EXISTS `auditcm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auditcm` (
  `AUDITCMID` int(11) NOT NULL AUTO_INCREMENT,
  `EVENT` varchar(100) NOT NULL,
  `DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `PERSONID` int(11) NOT NULL,
  `MESSAGE_RO` varchar(1000) DEFAULT NULL,
  `ORGANISATIONID` int(11) NOT NULL,
  `MESSAGE_EN` varchar(1000) DEFAULT NULL,
  `FIRSTNAME` varchar(1000) NOT NULL,
  `LASTNAME` varchar(1000) NOT NULL,
  PRIMARY KEY (`AUDITCMID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'audit'
--

--
-- Dumping routines for database 'audit'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-01-22 16:13:31
CREATE DATABASE  IF NOT EXISTS `cm` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `cm`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: cm
-- ------------------------------------------------------
-- Server version	5.5.10

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
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `PROJECTID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(1000) NOT NULL,
  `CLIENTID` int(11) DEFAULT NULL,
  `ORGANIZATIONID` int(11) NOT NULL,
  `OBSERVATION` varchar(2000) DEFAULT NULL,
  `DESCRIPTION` varchar(2000) DEFAULT NULL,
  `STATUS` smallint(6) NOT NULL,
  `MANAGERID` int(11) NOT NULL,
  PRIMARY KEY (`PROJECTID`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client` (
  `CLIENTID` int(11) NOT NULL AUTO_INCREMENT,
  `ORGANIZATIONID` int(11) NOT NULL,
  `TYPE` smallint(6) NOT NULL,
  `ADDRESS` varchar(1000) DEFAULT NULL,
  `PHONE` varchar(30) DEFAULT NULL,
  `EMAIL` varchar(50) DEFAULT NULL,
  `FAX` varchar(30) DEFAULT NULL,
  `OBSERVATION` varchar(2000) DEFAULT NULL,
  `STATUS` smallint(6) NOT NULL,
  `DESCRIPTION` varchar(2000) DEFAULT NULL,
  `P_FIRSTNAME` varchar(100) DEFAULT NULL,
  `P_LASTNAME` varchar(100) DEFAULT NULL,
  `P_SEX` char(1) DEFAULT NULL,
  `P_BIRTHDATE` date DEFAULT NULL,
  `C_NAME` varchar(100) DEFAULT NULL,
  `C_CUI` varchar(70) DEFAULT NULL,
  `C_IBAN` varchar(70) DEFAULT NULL,
  `C_CAPITAL` varchar(30) DEFAULT NULL,
  `C_LOCATION` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`CLIENTID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `projectteam`
--

DROP TABLE IF EXISTS `projectteam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectteam` (
  `PROJECTTEAMID` int(11) NOT NULL AUTO_INCREMENT,
  `PROJECTID` int(11) NOT NULL,
  `OBSERVATION` varchar(2000) DEFAULT NULL,
  `DESCRIPTION` varchar(2000) DEFAULT NULL,
  `STATUS` smallint(6) NOT NULL,
  PRIMARY KEY (`PROJECTTEAMID`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teammember`
--

DROP TABLE IF EXISTS `teammember`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teammember` (
  `MEMBERID` int(11) NOT NULL AUTO_INCREMENT,
  `PROJECTTEAMID` int(11) NOT NULL,
  `PERSONID` int(11) DEFAULT NULL,
  `FIRSTNAME` varchar(100) DEFAULT NULL,
  `LASTNAME` varchar(100) DEFAULT NULL,
  `EMAIL` varchar(50) DEFAULT NULL,
  `ADDRESS` varchar(1000) DEFAULT NULL,
  `PHONE` varchar(30) DEFAULT NULL,
  `OBSERVATION` varchar(2000) DEFAULT NULL,
  `DESCRIPTION` varchar(2000) DEFAULT NULL,
  `STATUS` smallint(6) NOT NULL,
  PRIMARY KEY (`MEMBERID`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'cm'
--

--
-- Dumping routines for database 'cm'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-01-22 16:13:31
CREATE DATABASE  IF NOT EXISTS `om` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `om`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: om
-- ------------------------------------------------------
-- Server version	5.5.10

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
-- Table structure for table `picture`
--

DROP TABLE IF EXISTS `picture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `picture` (
  `PICTUREID` int(11) NOT NULL AUTO_INCREMENT,
  `PICTURE` blob NOT NULL,
  `DATE_CREATED` timestamp NULL DEFAULT NULL,
  `DATE_MODIFIED` timestamp NULL DEFAULT NULL,
  `WIDTH` int(11) DEFAULT NULL,
  `HEIGHT` int(11) DEFAULT NULL,
  `EXTENSION` varchar(45) DEFAULT NULL,
  `NAME` varchar(45) NOT NULL,
  `PERSONID` int(11) NOT NULL,
  `PICTUREcol` varchar(45) NOT NULL,
  PRIMARY KEY (`PICTUREID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `personxdepartment`
--

DROP TABLE IF EXISTS `personxdepartment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `personxdepartment` (
  `PERSONID` int(11) NOT NULL,
  `DEPARTMENTID` int(11) NOT NULL,
  `JOBID` int(11) NOT NULL,
  PRIMARY KEY (`PERSONID`,`DEPARTMENTID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `localization`
--

DROP TABLE IF EXISTS `localization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `localization` (
  `LOCALIZATIONID` int(11) NOT NULL AUTO_INCREMENT,
  `RO` varchar(2000) NOT NULL,
  `EN` varchar(2000) NOT NULL,
  PRIMARY KEY (`LOCALIZATIONID`)
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `module`
--

DROP TABLE IF EXISTS `module`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `module` (
  `MODULEID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`MODULEID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orgxmodule`
--

DROP TABLE IF EXISTS `orgxmodule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orgxmodule` (
  `MODULEID` smallint(6) NOT NULL AUTO_INCREMENT,
  `ORGANISATIONID` int(11) NOT NULL,
  PRIMARY KEY (`MODULEID`,`ORGANISATIONID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `job`
--

DROP TABLE IF EXISTS `job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `job` (
  `JOBID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `JOBLEVEL` smallint(6) NOT NULL DEFAULT '-1',
  `DESCRIPTION` varchar(1000) DEFAULT NULL,
  `OBSERVATION` varchar(2000) DEFAULT NULL,
  `STATUS` smallint(6) NOT NULL DEFAULT '0',
  `ORGANISATIONID` int(11) NOT NULL,
  PRIMARY KEY (`JOBID`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `freeday`
--

DROP TABLE IF EXISTS `freeday`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `freeday` (
  `FREEDAYID` int(11) NOT NULL AUTO_INCREMENT,
  `CALENDARID` int(11) NOT NULL,
  `DAY` date NOT NULL,
  `OBSERVATION` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`FREEDAYID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usergroup`
--

DROP TABLE IF EXISTS `usergroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usergroup` (
  `USERGROUPID` int(11) NOT NULL AUTO_INCREMENT,
  `ORGANISATIONID` int(11) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(2000) DEFAULT NULL,
  `STATUS` smallint(6) NOT NULL,
  PRIMARY KEY (`USERGROUPID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `logo`
--

DROP TABLE IF EXISTS `logo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logo` (
  `LOGOID` int(11) NOT NULL AUTO_INCREMENT,
  `PICTURE` blob NOT NULL,
  `DATE_CREATED` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `DATE_MODIFIED` timestamp NULL DEFAULT NULL,
  `ORGANISATIONID` int(11) NOT NULL,
  `EXTENSION` varchar(45) NOT NULL,
  PRIMARY KEY (`LOGOID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `calendar`
--

DROP TABLE IF EXISTS `calendar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `calendar` (
  `CALENDARID` int(11) NOT NULL AUTO_INCREMENT,
  `STARTWORK` varchar(10) NOT NULL,
  `ENDWORK` varchar(10) NOT NULL,
  `OBSERVATION` varchar(2000) DEFAULT NULL,
  `ORGANISATIONID` int(11) NOT NULL,
  PRIMARY KEY (`CALENDARID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rolexpermission`
--

DROP TABLE IF EXISTS `rolexpermission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rolexpermission` (
  `ROLEID` int(11) NOT NULL,
  `PERMISSIONID` int(11) NOT NULL,
  PRIMARY KEY (`ROLEID`,`PERMISSIONID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission` (
  `PERMISSIONID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` int(11) NOT NULL,
  `MODULEID` smallint(6) NOT NULL,
  PRIMARY KEY (`PERMISSIONID`)
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `personxusergroup`
--

DROP TABLE IF EXISTS `personxusergroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `personxusergroup` (
  `USERGROUPID` int(11) NOT NULL,
  `PERSONID` int(11) NOT NULL,
  PRIMARY KEY (`USERGROUPID`,`PERSONID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `outofoffice`
--

DROP TABLE IF EXISTS `outofoffice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `outofoffice` (
  `OUTOFOFFICEID` int(11) NOT NULL AUTO_INCREMENT,
  `STARTPERIOD` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ENDPERIOD` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `REPLACEMENTID` int(11) NOT NULL DEFAULT '-1',
  `OBSERVATION` varchar(2000) DEFAULT NULL,
  `PERSONID` int(11) NOT NULL,
  `OUTOFOFFICEcol` varchar(45) NOT NULL,
  PRIMARY KEY (`OUTOFOFFICEID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person` (
  `PERSONID` int(11) NOT NULL AUTO_INCREMENT,
  `FIRSTNAME` varchar(100) NOT NULL,
  `LASTNAME` varchar(100) NOT NULL,
  `SEX` char(1) DEFAULT NULL,
  `BIRTHDATE` date DEFAULT NULL,
  `ADDRESS` varchar(100) DEFAULT NULL,
  `PHONE` varchar(30) DEFAULT NULL,
  `EMAIL` varchar(50) DEFAULT NULL,
  `OBSERVATION` varchar(2000) DEFAULT NULL,
  `PASSWORD` varchar(100) NOT NULL,
  `USERNAME` varchar(50) NOT NULL,
  `STATUS` smallint(6) NOT NULL DEFAULT '1',
  `ACCOUNT_NON_EXPIRED` smallint(6) NOT NULL DEFAULT '1',
  `ACCOUNT_NON_LOCKED` smallint(6) NOT NULL DEFAULT '1',
  `CREDENTIALS_NON_EXPIRED` smallint(6) NOT NULL DEFAULT '1',
  `ENABLED` smallint(6) NOT NULL DEFAULT '1',
  PRIMARY KEY (`PERSONID`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `theme`
--

DROP TABLE IF EXISTS `theme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `theme` (
  `THEMEID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  `CODE` varchar(45) NOT NULL,
  PRIMARY KEY (`THEMEID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `setting`
--

DROP TABLE IF EXISTS `setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `setting` (
  `SETTINGID` int(11) NOT NULL AUTO_INCREMENT,
  `ORGANISATIONID` int(11) NOT NULL,
  `PARAMETER` varchar(50) NOT NULL,
  `VALUE` varchar(30) DEFAULT NULL,
  `LOCALIZATIONID` int(11) NOT NULL,
  `STATUS` smallint(6) NOT NULL DEFAULT '1',
  PRIMARY KEY (`SETTINGID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `ROLEID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` int(11) NOT NULL DEFAULT '0',
  `OBSERVATION` varchar(2000) DEFAULT NULL,
  `MODULEID` int(11) NOT NULL,
  `ORGANISATIONID` int(11) NOT NULL DEFAULT '-999',
  `STATUS` smallint(6) NOT NULL DEFAULT '2',
  PRIMARY KEY (`ROLEID`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `personxrole`
--

DROP TABLE IF EXISTS `personxrole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `personxrole` (
  `ROLEID` int(11) NOT NULL,
  `PERSONID` int(11) NOT NULL,
  PRIMARY KEY (`ROLEID`,`PERSONID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `department` (
  `DEPARTMENTID` int(11) NOT NULL AUTO_INCREMENT,
  `ORGANISATIONID` int(11) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `MANAGERID` int(11) DEFAULT '-1',
  `PARENTDEPID` int(11) NOT NULL DEFAULT '-1',
  `OBSERVATION` varchar(2000) DEFAULT NULL,
  `STATUS` smallint(6) NOT NULL DEFAULT '1',
  PRIMARY KEY (`DEPARTMENTID`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `organisation`
--

DROP TABLE IF EXISTS `organisation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organisation` (
  `ORGANISATIONID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `ADDRESS` varchar(1000) NOT NULL,
  `PHONE` varchar(30) NOT NULL,
  `FAX` varchar(30) DEFAULT NULL,
  `EMAIL` varchar(50) NOT NULL,
  `OBSERVATION` varchar(2000) DEFAULT NULL,
  `PARENTID` int(11) NOT NULL DEFAULT '-1',
  `TYPE` smallint(6) NOT NULL DEFAULT '1',
  `STATUS` smallint(6) NOT NULL DEFAULT '1',
  `J` varchar(70) NOT NULL,
  `CUI` varchar(70) NOT NULL,
  `IBAN` varchar(70) NOT NULL,
  `CAPITAL` varchar(30) DEFAULT NULL,
  `LOCATION` varchar(1000) NOT NULL,
  PRIMARY KEY (`ORGANISATIONID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'om'
--

--
-- Dumping routines for database 'om'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-01-22 16:13:32
CREATE DATABASE  IF NOT EXISTS `ts` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `ts`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: ts
-- ------------------------------------------------------
-- Server version	5.5.10

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
-- Table structure for table `currency`
--

DROP TABLE IF EXISTS `currency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `currency` (
  `CURRENCYID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) DEFAULT NULL,
  `ABBREVIATION` varchar(20) DEFAULT NULL,
  `ORGANIZATIONID` int(11) DEFAULT NULL,
  `STATUS` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`CURRENCYID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `persondetail`
--

DROP TABLE IF EXISTS `persondetail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `persondetail` (
  `PERSONDETAILID` int(11) NOT NULL AUTO_INCREMENT,
  `PERSONID` int(11) NOT NULL,
  `COSTPRICE` float DEFAULT NULL,
  `COSTPRICECURRENCY` int(11) DEFAULT NULL,
  `COSTTIMEUNIT` smallint(6) DEFAULT NULL,
  `OBSERVATION` varchar(2000) DEFAULT NULL,
  `STATUS` smallint(6) DEFAULT NULL,
  `OVERTIMECOSTPRICE` float DEFAULT NULL,
  `OVERTIMECOSTCURRENCY` int(11) DEFAULT NULL,
  `OVERTIMECOSTTIMEUNIT` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`PERSONDETAILID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notificationsettings`
--

DROP TABLE IF EXISTS `notificationsettings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notificationsettings` (
  `NOTIFICATIONSETTINGSID` int(11) NOT NULL AUTO_INCREMENT,
  `PROJECTDETAILID` int(11) DEFAULT NULL,
  `USERID` int(11) NOT NULL,
  `ORGANIZATIONID` int(11) NOT NULL,
  `SETTING` smallint(6) NOT NULL,
  `STATUS` smallint(6) NOT NULL DEFAULT '0',
  PRIMARY KEY (`NOTIFICATIONSETTINGSID`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `widgetsession`
--

DROP TABLE IF EXISTS `widgetsession`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `widgetsession` (
  `WIDGETSESSIONID` int(11) NOT NULL AUTO_INCREMENT,
  `SESSIONID` varchar(8) NOT NULL,
  `USERID` int(11) NOT NULL,
  `CREATIONDATE` timestamp NULL DEFAULT NULL,
  `EXPIREDATE` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`WIDGETSESSIONID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `exchange`
--

DROP TABLE IF EXISTS `exchange`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `exchange` (
  `EXCHANGEID` int(11) NOT NULL AUTO_INCREMENT,
  `PROJECTDETAILID` int(11) DEFAULT NULL,
  `FIRSTCURRENCYID` int(11) NOT NULL,
  `SECONDCURRENCYID` int(11) NOT NULL,
  `RATE` float NOT NULL,
  `STATUS` smallint(6) DEFAULT NULL,
  `ORGANIZATIONID` int(11) NOT NULL,
  PRIMARY KEY (`EXCHANGEID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification` (
  `NOTIFICATIONID` int(11) NOT NULL AUTO_INCREMENT,
  `ISSUEDDATE` date NOT NULL,
  `RECEIVERID` int(11) NOT NULL,
  `MESSAGE` varchar(2000) NOT NULL,
  `SUBJECT` varchar(200) NOT NULL,
  PRIMARY KEY (`NOTIFICATIONID`)
) ENGINE=InnoDB AUTO_INCREMENT=693 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `recordsession`
--

DROP TABLE IF EXISTS `recordsession`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recordsession` (
  `RECORDSESSIONID` int(11) NOT NULL AUTO_INCREMENT,
  `SESSIONID` varchar(8) NOT NULL,
  `RECORDID` int(11) NOT NULL,
  PRIMARY KEY (`RECORDSESSIONID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `projectdetail`
--

DROP TABLE IF EXISTS `projectdetail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectdetail` (
  `PROJECTDETAILID` int(11) NOT NULL AUTO_INCREMENT,
  `PROJECTID` int(11) NOT NULL,
  `BUDGET` float DEFAULT NULL,
  `NOTIFICATIONPERCENTAGE` smallint(6) DEFAULT NULL,
  `COMPLETENESSPERCENTAGE` smallint(6) DEFAULT NULL,
  `OBSERVATION` varchar(2000) DEFAULT NULL,
  `STATUS` smallint(6) DEFAULT NULL,
  `BUDGETCURRENCY` int(11) DEFAULT NULL,
  `NOTIFICATIONSTATUS` smallint(6) DEFAULT '0',
  PRIMARY KEY (`PROJECTDETAILID`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teammemberdetail`
--

DROP TABLE IF EXISTS `teammemberdetail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teammemberdetail` (
  `TEAMMEMBERDETAILID` int(11) NOT NULL AUTO_INCREMENT,
  `TEAMMEMBERID` int(11) NOT NULL,
  `COSTPRICE` float DEFAULT NULL,
  `COSTPRICECURRENCY` int(11) DEFAULT NULL,
  `BILLINGPRICE` float DEFAULT NULL,
  `BILLINGPRICECURRENCY` int(11) DEFAULT NULL,
  `COSTTIMEUNIT` smallint(6) DEFAULT NULL,
  `BILLINGTIMEUNIT` smallint(6) DEFAULT NULL,
  `OBSERVATION` varchar(2000) DEFAULT NULL,
  `STATUS` smallint(6) DEFAULT NULL,
  `OVERTIMECOSTPRICE` float DEFAULT NULL,
  `OVERTIMECOSTCURRENCY` int(11) DEFAULT NULL,
  `OVERTIMEBILLINGPRICE` float DEFAULT NULL,
  `OVERTIMEBILLINGCURRENCY` int(11) DEFAULT NULL,
  `OVERTIMEBILLINGTIMEUNIT` smallint(6) DEFAULT NULL,
  `OVERTIMECOSTTIMEUNIT` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`TEAMMEMBERDETAILID`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity` (
  `ACTIVITYID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(500) NOT NULL,
  `PROJECTDETAILID` int(11) DEFAULT NULL,
  `BILLABLE` char(1) NOT NULL,
  `COSTPRICE` float DEFAULT NULL,
  `COSTPRICECURRENCY` int(11) DEFAULT NULL,
  `BILLINGPRICE` float DEFAULT NULL,
  `BILLINGPRICECURRENCY` int(11) DEFAULT NULL,
  `COSTTIMEUNIT` smallint(6) DEFAULT NULL,
  `BILLINGTIMEUNIT` smallint(6) DEFAULT NULL,
  `STATUS` smallint(6) DEFAULT NULL,
  `ORGANIZATIONID` int(11) NOT NULL,
  `DESCRIPTION` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`ACTIVITYID`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `expense`
--

DROP TABLE IF EXISTS `expense`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expense` (
  `EXPENSEID` int(11) NOT NULL AUTO_INCREMENT,
  `TEAMMEMBERDETAILID` int(11) DEFAULT NULL,
  `PROJECTDETAILDID` int(11) DEFAULT NULL,
  `ACTIVITYNAME` varchar(500) NOT NULL,
  `DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `COSTPRICE` float DEFAULT NULL,
  `COSTPRICECURRENCY` int(11) DEFAULT NULL,
  `BILLINGPRICE` float DEFAULT NULL,
  `BILLINGPRICECURRENCY` int(11) DEFAULT NULL,
  `BILLABLE` char(1) DEFAULT NULL,
  `OBSERVATION` varchar(2000) DEFAULT NULL,
  `DESCRIPTION` varchar(2000) DEFAULT NULL,
  `PERSONDETAILID` int(11) DEFAULT NULL,
  `ORGANIZATIONID` int(11) NOT NULL,
  `STATUS` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`EXPENSEID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `record`
--

DROP TABLE IF EXISTS `record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `record` (
  `RECORDID` int(11) NOT NULL AUTO_INCREMENT,
  `TEAMMEMBERDETAILID` int(11) DEFAULT NULL,
  `PROJECTDETAILID` int(11) DEFAULT NULL,
  `ACTIVITYID` int(11) NOT NULL,
  `STARTTIME` timestamp NULL DEFAULT NULL,
  `ENDTIME` timestamp NULL DEFAULT NULL,
  `TIME` varchar(7) DEFAULT NULL,
  `BILLABLE` char(1) DEFAULT NULL,
  `OBSERVATION` varchar(2000) DEFAULT NULL,
  `DESCRIPTION` varchar(2000) DEFAULT NULL,
  `OVERTIMESTARTTIME` timestamp NULL DEFAULT NULL,
  `OVERTIMEENDTIME` timestamp NULL DEFAULT NULL,
  `OVERTIMETIME` varchar(7) DEFAULT NULL,
  `OVERTIMEBILLABLE` char(1) DEFAULT NULL,
  `STATUS` smallint(6) DEFAULT NULL,
  `PERSONDETAILID` int(11) DEFAULT NULL,
  `ORGANIZATIONID` int(11) NOT NULL,
  `TITLE` varchar(1000) NOT NULL,
  PRIMARY KEY (`RECORDID`)
) ENGINE=InnoDB AUTO_INCREMENT=508 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'ts'
--

--
-- Dumping routines for database 'ts'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-01-22 16:13:32

-- Persons table
USE `om`;

INSERT INTO `person` (`PERSONID`,`FIRSTNAME`,`LASTNAME`,`SEX`,`BIRTHDATE`,`ADDRESS`,`PHONE`,`EMAIL`,`OBSERVATION`,`PASSWORD`,`USERNAME`,`STATUS`,`ACCOUNT_NON_EXPIRED`,`ACCOUNT_NON_LOCKED`,`CREDENTIALS_NON_EXPIRED`,`ENABLED`) VALUES (1,'AdminIT','LastName','M','2012-01-01','','','admin.it@termiteria.ro','','5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8','adminIT',1,1,1,1,1);

-- Roles

INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (1,'ADMIN_IT',1,'Super Admin. Is the SaaS Admin.',-1,-1,1);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (2,'OM_USER',2,NULL,1,-1,2);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (3,'OM_ADMIN',3,NULL,1,-1,2);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (6,'CM_USER',96,NULL,5,-1,2);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (7,'CM_USER_ALL',97,NULL,5,-1,2);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (8,'TS_USER',98,NULL,6,-1,2);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (9,'TS_USER_ALL',99,NULL,6,-1,2);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (10,'AUDIT_USER_READ',102,NULL,3,-1,2);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (11,'AUDIT_USER_ALL',103,NULL,3,-1,2);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (12,'OM_USER',104,NULL,1,3,3);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (13,'OM_ADMIN',105,NULL,1,3,3);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (16,'AUDIT_USER_READ',108,NULL,3,3,3);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (17,'AUDIT_USER_ALL',109,NULL,3,3,3);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (18,'CM_USER',110,NULL,5,3,3);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (19,'CM_USER_ALL',111,NULL,5,3,3);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (20,'TS_USER',112,NULL,6,3,3);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (21,'TS_USER_ALL',113,NULL,6,3,3);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (33,'OM_USER',125,NULL,1,5,3);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (34,'OM_ADMIN',126,NULL,1,5,3);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (35,'CM_USER',127,NULL,5,5,3);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (36,'CM_USER_ALL',128,NULL,5,5,3);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (37,'TS_USER',129,NULL,6,5,3);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (38,'TS_USER_ALL',130,NULL,6,5,3);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (39,'OM_USER',131,NULL,1,6,3);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (40,'OM_ADMIN',132,NULL,1,6,3);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (41,'TS_USER',133,NULL,6,6,3);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (42,'TS_USER_ALL',134,NULL,6,6,3);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (43,'CM_USER',135,NULL,5,6,3);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (44,'CM_USER_ALL',136,NULL,5,6,3);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (45,'AUDIT_USER_READ',137,NULL,3,6,3);
INSERT INTO `role` (`ROLEID`,`NAME`,`DESCRIPTION`,`OBSERVATION`,`MODULEID`,`ORGANISATIONID`,`STATUS`) VALUES (46,'AUDIT_USER_ALL',138,NULL,3,6,3);

-- Person X Role

INSERT INTO `personxrole` (`ROLEID`,`PERSONID`) VALUES (1,1);

-- Permissions

INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (1,'Super',4,-1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (2,'OM_Basic',5,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (4,'OM_OutOfOfficeAddToAll',53,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (5,'OM_PersonDelete',11,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (6,'OM_PersonAdd',9,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (7,'OM_OrgAddChild',12,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (8,'OM_OrgCreateAdmin',13,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (9,'OM_ChangeSettings',14,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (10,'OM_ViewTreeComplete',15,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (11,'OM_RoleDelete',16,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (12,'OM_ViewPersonRoleDepartmentDetails',17,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (13,'OM_EditUsername',18,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (14,'OM_CalendarUpdate',19,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (15,'OM_OrgUpdate',20,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (16,'OM_DeptAdd',21,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (17,'OM_OrgAddCeo',22,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (18,'OM_DeptUpdate',23,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (19,'OM_RoleAdd',24,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (20,'OM_RoleUpdate',25,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (21,'OM_ShowBranchSelect',26,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (22,'OM_PermissionView',27,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (23,'OM_ChangeLogo',28,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (24,'OM_ChangePersons',29,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (25,'OM_PersonUpdate',30,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (26,'OM_OutOfOfficeSearch',33,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (27,'OM_DeptDelete',34,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (28,'OM_ResetPassword',36,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (29,'OM_RoleSearch',35,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (30,'OM_OrgChildView',38,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (31,'OM_OrgChildDelete',39,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (32,'OM_OrgChildChangeStatus',40,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (33,'OM_PersonChangeStatus',41,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (34,'OM_AddUserGroup',42,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (35,'OM_DeleteUserGroup',43,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (36,'OM_UpdateUserGroup',44,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (37,'OM_FreedayAdd',45,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (38,'OM_FreedayUpdate',46,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (39,'OM_FreedayDelete',47,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (40,'OM_JobAdd',48,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (41,'OM_JobDelete',49,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (42,'OM_JobUpdate',50,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (43,'OM_JobChangeStatus',51,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (44,'OM_JobSearch',52,1);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (50,'CM_Basic',59,5);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (51,'TS_Basic',60,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (52,'CM_ClientAdd',61,5);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (53,'CM_ClientUpdate',62,5);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (54,'CM_ClientDelete',63,5);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (55,'CM_ProjectAdvancedAdd',64,5);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (56,'CM_ProjectAdvancedSearch',65,5);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (57,'CM_ProjectAdvancedDelete',66,5);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (58,'TS_RecordSearchAll',67,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (59,'TS_ProjectAdvancedSearch',68,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (60,'TS_ProjectAdvancedView',69,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (61,'TS_ProjectAddUpdateProjectDetails',70,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (62,'TS_ProjectAddUpdateTeamMemberDetails',71,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (63,'TS_ActivityAdvancedSearch',72,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (64,'TS_ActivityAdvancedAddUpdate',73,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (65,'TS_ActivityDelete',74,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (66,'TS_RecordAddAll',75,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (67,'TS_RecordDeleteAll',76,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (68,'TS_RecordUpdateAll',77,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (69,'TS_PersonAdvancedSearch',78,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (70,'TS_CostSheetAddAll',79,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (71,'TS_CostSheetSearchAll',80,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (72,'TS_CostSheetDeleteAll',81,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (73,'TS_CostSheetUpdateAll',82,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (74,'TS_CurrencySearch',83,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (75,'TS_CurrencyAdd',84,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (76,'TS_CurrencyUpdate',85,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (77,'TS_CurrencyDelete',86,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (78,'TS_PersonDetailAddUpdate',87,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (79,'TS_ExchangeSearchAll',88,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (80,'TS_ExchangeAddAll',89,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (81,'TS_ExchangeUpdateAll',90,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (82,'TS_ExchangeDeleteAll',91,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (83,'TS_PersonAdvancedView',92,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (84,'TS_TeamMemberView',93,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (85,'TS_ReportsView',94,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (86,'TS_RecordViewCosts',95,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (87,'TS_NotificationReceive',8,6);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (88,'AUDIT_Basic',100,3);
INSERT INTO `permission` (`PERMISSIONID`,`NAME`,`DESCRIPTION`,`MODULEID`) VALUES (89,'AUDIT_Delete',101,3);

-- Role X Permissions

INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,1);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,2);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,4);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,5);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,6);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,7);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,8);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,9);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,10);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,11);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,12);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,13);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,14);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,15);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,16);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,17);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,18);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,19);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,20);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,21);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,22);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,23);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,24);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,25);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,26);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,27);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,28);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,29);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,30);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,31);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,32);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,33);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,34);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,35);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,36);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,37);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,38);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,39);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,40);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,41);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,42);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,43);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (1,44);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (2,2);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,2);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,4);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,5);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,6);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,7);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,8);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,9);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,10);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,11);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,12);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,13);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,14);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,15);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,16);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,17);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,18);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,19);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,20);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,21);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,22);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,23);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,24);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,25);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,26);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,27);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,28);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,29);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,30);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,31);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,32);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,33);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,34);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,35);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,36);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,37);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,38);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,39);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,40);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,41);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,42);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,43);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (3,44);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (6,50);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (7,50);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (7,52);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (7,53);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (7,54);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (7,55);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (7,56);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (7,57);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (8,51);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,51);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,58);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,59);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,60);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,61);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,62);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,63);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,64);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,65);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,66);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,67);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,68);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,69);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,70);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,71);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,72);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,73);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,74);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,75);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,76);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,77);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,78);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,79);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,80);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,81);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,82);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,83);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,84);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,85);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,86);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (9,87);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (10,88);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (11,88);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (11,89);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (12,2);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,2);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,4);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,5);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,6);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,7);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,8);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,9);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,10);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,11);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,12);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,13);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,14);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,15);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,16);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,17);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,18);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,19);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,20);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,21);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,22);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,23);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,24);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,25);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,26);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,27);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,28);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,29);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,30);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,31);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,32);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,33);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,34);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,35);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,36);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,37);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,38);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,39);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,40);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,41);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,42);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,43);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (13,44);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (16,88);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (17,88);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (17,89);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (18,50);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (19,50);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (19,52);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (19,53);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (19,54);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (19,55);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (19,56);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (19,57);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (20,51);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,51);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,58);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,59);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,60);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,61);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,62);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,63);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,64);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,65);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,66);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,67);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,68);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,69);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,70);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,71);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,72);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,73);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,74);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,75);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,76);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,77);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,78);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,79);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,80);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,81);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,82);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,83);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,84);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,85);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,86);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (21,87);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (33,2);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,2);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,4);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,5);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,6);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,7);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,8);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,9);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,10);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,11);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,12);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,13);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,14);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,15);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,16);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,17);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,18);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,19);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,20);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,21);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,22);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,23);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,24);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,25);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,26);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,27);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,28);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,29);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,30);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,31);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,32);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,33);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,34);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,35);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,36);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,37);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,38);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,39);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,40);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,41);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,42);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,43);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (34,44);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (35,50);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (36,50);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (36,52);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (36,53);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (36,54);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (36,55);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (36,56);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (36,57);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (37,51);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,51);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,58);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,59);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,60);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,61);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,62);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,63);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,64);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,65);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,66);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,67);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,68);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,69);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,70);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,71);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,72);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,73);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,74);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,75);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,76);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,77);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,78);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,79);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,80);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,81);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,82);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,83);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,84);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,85);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,86);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (38,87);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (39,2);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,2);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,4);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,5);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,6);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,7);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,8);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,9);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,10);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,11);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,12);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,13);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,14);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,15);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,16);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,17);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,18);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,19);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,20);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,21);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,22);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,23);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,24);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,25);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,26);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,27);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,28);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,29);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,30);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,31);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,32);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,33);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,34);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,35);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,36);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,37);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,38);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,39);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,40);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,41);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,42);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,43);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (40,44);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (41,51);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,51);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,58);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,59);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,60);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,61);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,62);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,63);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,64);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,65);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,66);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,67);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,68);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,69);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,70);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,71);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,72);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,73);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,74);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,75);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,76);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,77);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,78);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,79);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,80);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,81);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,82);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,83);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,84);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,85);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,86);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (42,87);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (43,50);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (44,50);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (44,52);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (44,53);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (44,54);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (44,55);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (44,56);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (44,57);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (45,88);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (46,88);
INSERT INTO `rolexpermission` (`ROLEID`,`PERMISSIONID`) VALUES (46,89);

-- Setting table

INSERT INTO `setting` (`SETTINGID`,`ORGANISATIONID`,`PARAMETER`,`VALUE`,`LOCALIZATIONID`,`STATUS`) VALUES (1,-1,'theme','standard',37,1);

-- Theme table

INSERT INTO `theme` (`THEMEID`,`NAME`,`CODE`) VALUES (1,'Standard Theme','standard');
INSERT INTO `theme` (`THEMEID`,`NAME`,`CODE`) VALUES (2,'Dark Theme','dark');
