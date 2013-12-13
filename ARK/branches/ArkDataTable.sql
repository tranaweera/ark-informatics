CREATE DATABASE  IF NOT EXISTS `audit` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `audit`;
-- MySQL dump 10.13  Distrib 5.5.32, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: audit
-- ------------------------------------------------------
-- Server version	5.5.32-0ubuntu0.12.04.1

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
-- Table structure for table `consent_history`
--

DROP TABLE IF EXISTS `consent_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent_history` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `STUDY_COMP_ID` int(11) DEFAULT NULL,
  `STUDY_COMP_STATUS_ID` int(11) NOT NULL,
  `CONSENT_STATUS_ID` int(11) NOT NULL,
  `CONSENT_TYPE_ID` int(11) NOT NULL,
  `CONSENT_DATE` date DEFAULT NULL,
  `CONSENTED_BY` varchar(100) DEFAULT NULL,
  `COMMENTS` varchar(500) DEFAULT NULL,
  `REQUESTED_DATE` date DEFAULT NULL,
  `RECEIVED_DATE` date DEFAULT NULL,
  `COMPLETED_DATE` date DEFAULT NULL,
  `CONSENT_DOWNLOADED_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=114 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lss_consent_history`
--

DROP TABLE IF EXISTS `lss_consent_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lss_consent_history` (
  `ID` int(1) NOT NULL AUTO_INCREMENT,
  `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `CONSENT_TO_ACTIVE_CONTACT_ID` int(11) DEFAULT NULL,
  `CONSENT_TO_PASSIVE_DATA_GATHERING_ID` int(11) DEFAULT NULL,
  `CONSENT_TO_USE_DATA_ID` int(11) DEFAULT NULL,
  `CONSENT_STATUS_ID` int(11) DEFAULT NULL,
  `CONSENT_TYPE_ID` int(11) DEFAULT NULL,
  `CONSENT_DATE` date DEFAULT NULL,
  `CONSENT_DOWNLOADED` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8569 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-12-12 11:03:20
CREATE DATABASE  IF NOT EXISTS `geno` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `geno`;
-- MySQL dump 10.13  Distrib 5.5.32, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: geno
-- ------------------------------------------------------
-- Server version	5.5.32-0ubuntu0.12.04.1

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
-- Table structure for table `command`
--

DROP TABLE IF EXISTS `command`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `command` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `LOCATION` varchar(255) DEFAULT NULL,
  `SERVER_URL` varchar(255) DEFAULT NULL,
  `INPUT_FILE_FORMAT` varchar(255) DEFAULT NULL,
  `OUTPUT_FILE_FORMAT` varchar(255) DEFAULT NULL COMMENT 'may need error output too i guess or alt output',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lss_pipeline`
--

DROP TABLE IF EXISTS `lss_pipeline`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lss_pipeline` (
  `ID` int(11) NOT NULL,
  `PIPELINE_ID` int(11) NOT NULL,
  `LSS_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_lss_pipeline_pipeline_idx` (`PIPELINE_ID`),
  KEY `fk_lss_pipeline_lss_idx` (`LSS_ID`),
  CONSTRAINT `fk_lss_pipeline_lss` FOREIGN KEY (`LSS_ID`) REFERENCES `study`.`link_subject_study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_lss_pipeline_pipeline` FOREIGN KEY (`PIPELINE_ID`) REFERENCES `pipeline` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pipeline`
--

DROP TABLE IF EXISTS `pipeline`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pipeline` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(4096) DEFAULT NULL,
  `STUDY_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pipeline_template`
--

DROP TABLE IF EXISTS `pipeline_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pipeline_template` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(4096) DEFAULT NULL,
  `IS_TEMPLATE` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `process`
--

DROP TABLE IF EXISTS `process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `process` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `PIPELINE_ID` int(11) NOT NULL,
  `DESCRIPTION` varchar(4096) DEFAULT NULL,
  `COMMAND_ID` int(11) DEFAULT NULL COMMENT 'The command is the task/program that will perform the process/transform',
  `START_TIME` datetime DEFAULT NULL,
  `END_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_process_command_idx` (`COMMAND_ID`),
  KEY `fk_process_pipeline_idx` (`PIPELINE_ID`),
  CONSTRAINT `fk_process_pipeline` FOREIGN KEY (`PIPELINE_ID`) REFERENCES `pipeline` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_process_command` FOREIGN KEY (`COMMAND_ID`) REFERENCES `command` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `process_input`
--

DROP TABLE IF EXISTS `process_input`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `process_input` (
  `ID` int(11) NOT NULL,
  `PROCESS_ID` int(11) NOT NULL,
  `INPUT_FILE_LOCATION` varchar(255) DEFAULT NULL,
  `INPUT_FILE_HASH` varchar(255) DEFAULT NULL,
  `INPUT_FILE_TYPE` varchar(255) DEFAULT NULL,
  `INPUT_KEPT` tinyint(1) DEFAULT NULL,
  `INPUT_SERVER` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_process_input_process_idx` (`PROCESS_ID`),
  CONSTRAINT `fk_process_input_process` FOREIGN KEY (`PROCESS_ID`) REFERENCES `process` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `process_output`
--

DROP TABLE IF EXISTS `process_output`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `process_output` (
  `ID` int(11) NOT NULL,
  `PROCESS_ID` int(11) NOT NULL,
  `OUTPUT_FILE_LOCATION` varchar(255) DEFAULT NULL,
  `OUTPUT_FILE_HASH` varchar(255) DEFAULT NULL,
  `OUTPUT_FILE_TYPE` varchar(255) DEFAULT NULL,
  `OUTPUT_KEPT` tinyint(1) DEFAULT NULL,
  `OUTPUT_SERVER` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_process_output_process_idx` (`PROCESS_ID`),
  CONSTRAINT `fk_process_output_process` FOREIGN KEY (`PROCESS_ID`) REFERENCES `process` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-12-12 11:03:20
CREATE DATABASE  IF NOT EXISTS `admin` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `admin`;
-- MySQL dump 10.13  Distrib 5.5.32, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: admin
-- ------------------------------------------------------
-- Server version	5.5.32-0ubuntu0.12.04.1

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
-- Table structure for table `work_request`
--

DROP TABLE IF EXISTS `work_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `work_request` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `STATUS_ID` int(11) DEFAULT NULL,
  `RESEARCHER_ID` int(11) DEFAULT NULL,
  `REQUESTED_DATE` date DEFAULT NULL,
  `COMMENCED_DATE` date DEFAULT NULL,
  `COMPLETED_DATE` date DEFAULT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `GST` double DEFAULT NULL,
  `GST_ALLOW` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_work_request_researcher_id` (`RESEARCHER_ID`),
  KEY `fk_work_request_status_id` (`STATUS_ID`),
  CONSTRAINT `fk_work_request_researcher_id` FOREIGN KEY (`RESEARCHER_ID`) REFERENCES `researcher` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_work_request_status_id` FOREIGN KEY (`STATUS_ID`) REFERENCES `work_request_status` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `researcher`
--

DROP TABLE IF EXISTS `researcher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `researcher` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FIRST_NAME` varchar(30) DEFAULT NULL,
  `LAST_NAME` varchar(45) DEFAULT NULL,
  `ORGANIZATION` varchar(50) DEFAULT NULL,
  `ADDRESS` varchar(255) DEFAULT NULL,
  `ROLE_ID` int(11) DEFAULT NULL,
  `STATUS_ID` int(11) DEFAULT NULL,
  `CREATED_DATE` date DEFAULT NULL,
  `OFFICE_PHONE` varchar(25) DEFAULT NULL,
  `MOBILE` varchar(25) DEFAULT NULL,
  `EMAIL` varchar(45) DEFAULT NULL,
  `FAX` varchar(25) DEFAULT NULL,
  `COMMENT` varchar(255) DEFAULT NULL,
  `BILLING_TYPE_ID` int(11) DEFAULT NULL,
  `ACCOUNT_NUMBER` varchar(30) DEFAULT NULL,
  `BSB` varchar(8) DEFAULT NULL,
  `BANK` varchar(50) DEFAULT NULL,
  `ACCOUNT_NAME` varchar(50) DEFAULT NULL,
  `STUDY_ID` int(11) DEFAULT NULL,
  `TITLE_TYPE_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_researcher_role_id` (`ROLE_ID`),
  KEY `fk_researcher_status_id` (`STATUS_ID`),
  KEY `fk_researcher_billing_type_id` (`BILLING_TYPE_ID`),
  CONSTRAINT `fk_researcher_billing_type_id` FOREIGN KEY (`BILLING_TYPE_ID`) REFERENCES `billing_type` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `fk_researcher_role_id` FOREIGN KEY (`ROLE_ID`) REFERENCES `researcher_role` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_researcher_status_id` FOREIGN KEY (`STATUS_ID`) REFERENCES `researcher_status` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bilable_item`
--

DROP TABLE IF EXISTS `bilable_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bilable_item` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `QUANTITY` double DEFAULT NULL,
  `COMMENCE_DATE` date DEFAULT NULL,
  `TYPE` varchar(10) NOT NULL,
  `STATUS_ID` int(11) DEFAULT NULL,
  `REQUEST_ID` int(11) DEFAULT NULL,
  `INVOICE` varchar(5) DEFAULT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `BILLABLE_TYPE` int(11) DEFAULT NULL,
  `ITEM_COST` double DEFAULT NULL,
  `TOTAL_COST` double DEFAULT NULL,
  `ATTACHMENT_FILENAME` varchar(45) DEFAULT NULL,
  `ATTACHMENT_PAYLOAD` longblob,
  `TOTAL_GST` double DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_bilable_item_billable_type` (`BILLABLE_TYPE`),
  KEY `fk_bilable_item_request_id` (`REQUEST_ID`),
  KEY `fk_bilable_item_status` (`STATUS_ID`),
  CONSTRAINT `fk_bilable_item_billable_type` FOREIGN KEY (`BILLABLE_TYPE`) REFERENCES `billable_item_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_bilable_item_request_id` FOREIGN KEY (`REQUEST_ID`) REFERENCES `work_request` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-12-12 11:03:20
CREATE DATABASE  IF NOT EXISTS `lims` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `lims`;
-- MySQL dump 10.13  Distrib 5.5.32, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: lims
-- ------------------------------------------------------
-- Server version	5.5.32-0ubuntu0.12.04.1

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
-- Table structure for table `barcode_label_template`
--

DROP TABLE IF EXISTS `barcode_label_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `barcode_label_template` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) DEFAULT NULL,
  `BARCODE_PRINTER_ID` int(11) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` text,
  `LABEL_PREFIX` text NOT NULL,
  `LABEL_SUFFIX` text NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_barcode_label_study` (`STUDY_ID`),
  KEY `fk_barcode_label_printer` (`BARCODE_PRINTER_ID`),
  KEY `fk_barcode_label_barcode_printer` (`BARCODE_PRINTER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `barcode_label`
--

DROP TABLE IF EXISTS `barcode_label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `barcode_label` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) DEFAULT NULL,
  `BARCODE_PRINTER_ID` int(11) DEFAULT NULL,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` text,
  `LABEL_PREFIX` text NOT NULL,
  `LABEL_SUFFIX` text NOT NULL,
  `VERSION` int(11) NOT NULL,
  `BARCODE_PRINTER_NAME` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_barcode_label_study` (`STUDY_ID`),
  KEY `fk_barcode_label_printer` (`BARCODE_PRINTER_ID`),
  KEY `fk_barcode_label_barcode_printer` (`BARCODE_PRINTER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `barcode_label_data`
--

DROP TABLE IF EXISTS `barcode_label_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `barcode_label_data` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BARCODE_LABEL_ID` int(11) NOT NULL,
  `COMMAND` varchar(50) NOT NULL,
  `X_COORD` int(11) NOT NULL,
  `Y_COORD` int(11) NOT NULL,
  `P1` varchar(50) DEFAULT NULL,
  `P2` varchar(50) DEFAULT NULL,
  `P3` varchar(50) DEFAULT NULL,
  `P4` varchar(50) DEFAULT NULL,
  `P5` varchar(50) DEFAULT NULL,
  `P6` varchar(50) DEFAULT NULL,
  `P7` varchar(50) DEFAULT NULL,
  `P8` varchar(50) DEFAULT NULL,
  `QUOTE_LEFT` varchar(5) DEFAULT NULL,
  `DATA` varchar(50) DEFAULT NULL,
  `QUOTE_RIGHT` varchar(5) DEFAULT NULL,
  `LINE_FEED` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_barcode_label_data_label` (`BARCODE_LABEL_ID`),
  KEY `fk_barcode_label_data_1` (`BARCODE_LABEL_ID`),
  CONSTRAINT `fk_barcode_label_data_1` FOREIGN KEY (`BARCODE_LABEL_ID`) REFERENCES `barcode_label` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=177 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_cell`
--

DROP TABLE IF EXISTS `inv_cell`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_cell` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BOX_ID` int(11) NOT NULL,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `ROWNO` int(11) DEFAULT NULL,
  `COLNO` int(11) DEFAULT NULL,
  `STATUS` varchar(50) DEFAULT NULL,
  `BIOSPECIMEN_ID` int(11) DEFAULT NULL,
  `BIOSPECIMENKEY` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_inv_cell_box_idx` (`BOX_ID`),
  KEY `fk_inv_cell_biospecimen_idx` (`BIOSPECIMEN_ID`),
  CONSTRAINT `fk_inv_cell_biospecimen` FOREIGN KEY (`BIOSPECIMEN_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE SET NULL ON UPDATE NO ACTION,
  CONSTRAINT `fk_inv_cell_box` FOREIGN KEY (`BOX_ID`) REFERENCES `inv_box` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=657660 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biospecimen`
--

DROP TABLE IF EXISTS `biospecimen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimen` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BIOSPECIMEN_UID` varchar(50) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `SAMPLETYPE_ID` int(11) NOT NULL,
  `BIOCOLLECTION_ID` int(11) NOT NULL,
  `SUBSTUDY_ID` int(11) DEFAULT NULL,
  `PARENT_ID` int(11) DEFAULT NULL,
  `PARENTID` varchar(50) DEFAULT NULL,
  `OLD_ID` int(11) DEFAULT NULL,
  `OLDPARENT_ID` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `OTHERID` varchar(50) DEFAULT NULL,
  `BIOSPECIMEN_STORAGE_ID` int(11) DEFAULT NULL,
  `SAMPLE_TIME` time DEFAULT NULL,
  `PROCESSED_DATE` datetime DEFAULT NULL,
  `SAMPLE_DATE` datetime DEFAULT NULL,
  `SAMPLETYPE` varchar(255) DEFAULT NULL,
  `SAMPLESUBTYPE` varchar(255) DEFAULT NULL,
  `PROCESSED_TIME` time DEFAULT NULL,
  `DEPTH` int(11) DEFAULT '1',
  `BIOSPECIMEN_GRADE_ID` int(11) DEFAULT NULL,
  `BIOSPECIMEN_SPECIES_ID` int(11) DEFAULT '1',
  `QTY_COLLECTED` double DEFAULT NULL,
  `QTY_REMOVED` double DEFAULT NULL,
  `COMMENTS` text,
  `QUANTITY` decimal(16,10) DEFAULT NULL,
  `UNIT_ID` int(11) DEFAULT '0',
  `TREATMENT_TYPE_ID` int(11) NOT NULL,
  `BARCODED` tinyint(1) NOT NULL DEFAULT '0',
  `BIOSPECIMEN_QUALITY_ID` int(11) DEFAULT NULL,
  `BIOSPECIMEN_ANTICOAGULANT_ID` int(11) DEFAULT NULL,
  `BIOSPECIMEN_STATUS_ID` int(11) DEFAULT NULL,
  `DELETED` int(11) DEFAULT NULL,
  `CONCENTRATION` float DEFAULT NULL,
  `BIOSPECIMEN_PROTOCOL_ID` int(11) DEFAULT NULL,
  `PURITY` float DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_biospecimen_study` (`STUDY_ID`),
  KEY `fk_biospecimen_treatment_type_id` (`TREATMENT_TYPE_ID`),
  KEY `fk_biospecimen_unit` (`UNIT_ID`),
  KEY `fk_biospecimen_quality` (`BIOSPECIMEN_QUALITY_ID`),
  KEY `fk_biospecimen_anticoagulant` (`BIOSPECIMEN_ANTICOAGULANT_ID`),
  KEY `fk_biospecimen_status` (`BIOSPECIMEN_STATUS_ID`),
  KEY `fk_biospecimen_storage` (`BIOSPECIMEN_STORAGE_ID`),
  KEY `fk_biospecimen_species` (`BIOSPECIMEN_SPECIES_ID`),
  KEY `fk_biospecimen_biocollection` (`BIOCOLLECTION_ID`),
  KEY `fk_biospecimen_biospecimen_idx` (`BIOSPECIMEN_UID`),
  KEY `fk_biospecimen_biospecimen` (`PARENT_ID`),
  KEY `fk_biospecimen_parent_id` (`PARENT_ID`),
  KEY `fk_biospecimen_old_id` (`OLD_ID`),
  KEY `fk_biospecimen_subject` (`LINK_SUBJECT_STUDY_ID`),
  KEY `fk_biospecimen_protocol` (`BIOSPECIMEN_PROTOCOL_ID`),
  CONSTRAINT `fk_biospecimen_anticoagulant` FOREIGN KEY (`BIOSPECIMEN_ANTICOAGULANT_ID`) REFERENCES `biospecimen_anticoagulant` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_biocollection` FOREIGN KEY (`BIOCOLLECTION_ID`) REFERENCES `biocollection` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_biospecimen` FOREIGN KEY (`PARENT_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_protocol` FOREIGN KEY (`BIOSPECIMEN_PROTOCOL_ID`) REFERENCES `biospecimen_protocol` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_quality` FOREIGN KEY (`BIOSPECIMEN_QUALITY_ID`) REFERENCES `biospecimen_quality` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_species` FOREIGN KEY (`BIOSPECIMEN_SPECIES_ID`) REFERENCES `biospecimen_species` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_status` FOREIGN KEY (`BIOSPECIMEN_STATUS_ID`) REFERENCES `biospecimen_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_storage` FOREIGN KEY (`BIOSPECIMEN_STORAGE_ID`) REFERENCES `biospecimen_storage` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_biospecimen_treatment_type_id` FOREIGN KEY (`TREATMENT_TYPE_ID`) REFERENCES `treatment_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_biospecimen_unit` FOREIGN KEY (`UNIT_ID`) REFERENCES `unit` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=328242 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `access_request`
--

DROP TABLE IF EXISTS `access_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `access_request` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `REQUEST_DATE` datetime NOT NULL,
  `REQUIRED_DATE` datetime DEFAULT NULL,
  `COMMENTS` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_rack`
--

DROP TABLE IF EXISTS `inv_rack`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_rack` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FREEZER_ID` int(11) NOT NULL,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `NAME` varchar(50) NOT NULL,
  `AVAILABLE` int(11) DEFAULT NULL,
  `DESCRIPTION` text,
  `CAPACITY` int(11) DEFAULT NULL,
  `OLD_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_inv_freezer_tray_idx` (`FREEZER_ID`),
  CONSTRAINT `FK_inv_rack_inv_freezer` FOREIGN KEY (`FREEZER_ID`) REFERENCES `inv_freezer` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=261 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biospecimen_custom_field_data`
--

DROP TABLE IF EXISTS `biospecimen_custom_field_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimen_custom_field_data` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BIOSPECIMEN_ID` int(11) NOT NULL,
  `CUSTOM_FIELD_DISPLAY_ID` int(11) NOT NULL,
  `TEXT_DATA_VALUE` text,
  `DATE_DATA_VALUE` datetime DEFAULT NULL,
  `NUMBER_DATA_VALUE` double DEFAULT NULL,
  `ERROR_DATA_VALUE` text,
  PRIMARY KEY (`ID`),
  KEY `FK_BIOSPECFDATA_CUSTOM_FIELD_DISPLAY_ID` (`CUSTOM_FIELD_DISPLAY_ID`),
  KEY `FK_BIOSPECFDATA_BIOSPECIMEN_ID` (`BIOSPECIMEN_ID`),
  CONSTRAINT `FK_BIOSPECFDATA_BIOSPECIMEN_ID` FOREIGN KEY (`BIOSPECIMEN_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_BIOSPECFDATA_CUSTOM_FIELD_DISPLAY_ID` FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`) REFERENCES `study`.`custom_field_display` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=509683 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biocollection_custom_field_data`
--

DROP TABLE IF EXISTS `biocollection_custom_field_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biocollection_custom_field_data` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BIO_COLLECTION_ID` int(11) NOT NULL,
  `CUSTOM_FIELD_DISPLAY_ID` int(11) NOT NULL,
  `TEXT_DATA_VALUE` text,
  `DATE_DATA_VALUE` datetime DEFAULT NULL,
  `NUMBER_DATA_VALUE` double DEFAULT NULL,
  `ERROR_DATA_VALUE` text,
  PRIMARY KEY (`ID`),
  KEY `FK_BIOCOLCFDATA_BIOCOLLECTION_ID` (`BIO_COLLECTION_ID`),
  KEY `FK_BIOCOLCFDATA_CUSTOM_FIELD_DISPLAY_ID` (`CUSTOM_FIELD_DISPLAY_ID`),
  CONSTRAINT `FK_BIOCOLCFDATA_BIOCOLLECTION_ID` FOREIGN KEY (`BIO_COLLECTION_ID`) REFERENCES `biocollection` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_BIOCOLCFDATA_CUSTOM_FIELD_DISPLAY_ID` FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`) REFERENCES `study`.`custom_field_display` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=25500 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_freezer`
--

DROP TABLE IF EXISTS `inv_freezer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_freezer` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `LOCATION` text,
  `STATUS` varchar(50) DEFAULT NULL,
  `SITE_ID` int(11) NOT NULL,
  `CAPACITY` int(11) DEFAULT NULL,
  `LASTSERVICENOTE` text,
  `NAME` varchar(50) NOT NULL,
  `AVAILABLE` int(11) DEFAULT NULL,
  `DECOMMISSIONDATE` datetime DEFAULT NULL,
  `COMMISSIONDATE` datetime DEFAULT NULL,
  `LASTSERVICEDATE` datetime DEFAULT NULL,
  `DESCRIPTION` text,
  PRIMARY KEY (`ID`),
  KEY `fk_inv_freezer_site` (`SITE_ID`),
  CONSTRAINT `fk_inv_freezer_site` FOREIGN KEY (`SITE_ID`) REFERENCES `inv_site` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_box`
--

DROP TABLE IF EXISTS `inv_box`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_box` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `NOOFCOL` int(11) NOT NULL,
  `CAPACITY` int(11) DEFAULT NULL,
  `RACK_ID` int(11) NOT NULL,
  `AVAILABLE` int(11) DEFAULT NULL,
  `NOOFROW` int(11) NOT NULL,
  `COLNOTYPE_ID` int(11) NOT NULL,
  `ROWNOTYPE_ID` int(11) NOT NULL,
  `TRANSFER_ID` int(11) DEFAULT NULL,
  `TYPE` int(11) DEFAULT NULL,
  `OLD_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_inv_box_rowtype_idx` (`ROWNOTYPE_ID`),
  KEY `fk_inv_box_coltype_idx` (`COLNOTYPE_ID`),
  KEY `fk_inv_box_rack_idx` (`RACK_ID`),
  CONSTRAINT `fk_inv_box_coltype` FOREIGN KEY (`COLNOTYPE_ID`) REFERENCES `inv_col_row_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inv_box_rack` FOREIGN KEY (`RACK_ID`) REFERENCES `inv_rack` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inv_box_rowtype` FOREIGN KEY (`ROWNOTYPE_ID`) REFERENCES `inv_col_row_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3105 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biocollectionuid_template`
--

DROP TABLE IF EXISTS `biocollectionuid_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biocollectionuid_template` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `BIOCOLLECTIONUID_PREFIX` varchar(45) DEFAULT NULL,
  `BIOCOLLECTIONUID_TOKEN_ID` int(11) DEFAULT NULL,
  `BIOCOLLECTIONUID_PADCHAR_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `STUDY_ID_UNIQUE` (`STUDY_ID`),
  KEY `fk_biocollectionuid_template_study` (`STUDY_ID`),
  CONSTRAINT `fk_biocollectionuid_template_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biospecimen_anticoagulant`
--

DROP TABLE IF EXISTS `biospecimen_anticoagulant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimen_anticoagulant` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biospecimenuid_template`
--

DROP TABLE IF EXISTS `biospecimenuid_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimenuid_template` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `BIOSPECIMENUID_PREFIX` varchar(45) DEFAULT NULL,
  `BIOSPECIMENUID_TOKEN_ID` int(11) DEFAULT NULL,
  `BIOSPECIMENUID_PADCHAR_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `fk_study_study` (`STUDY_ID`),
  KEY `fk_study_biospecimenuid_padchar` (`BIOSPECIMENUID_PADCHAR_ID`),
  KEY `fk_study_biospecimenuid_token` (`BIOSPECIMENUID_TOKEN_ID`),
  CONSTRAINT `fk_study_biospecimenuid_padchar` FOREIGN KEY (`BIOSPECIMENUID_PADCHAR_ID`) REFERENCES `biospecimenuid_padchar` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_biospecimenuid_token` FOREIGN KEY (`BIOSPECIMENUID_TOKEN_ID`) REFERENCES `biospecimenuid_token` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biocollectionuid_sequence`
--

DROP TABLE IF EXISTS `biocollectionuid_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biocollectionuid_sequence` (
  `STUDY_NAME_ID` varchar(150) NOT NULL,
  `UID_SEQUENCE` int(11) NOT NULL DEFAULT '0',
  `INSERT_LOCK` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`STUDY_NAME_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bio_transaction`
--

DROP TABLE IF EXISTS `bio_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bio_transaction` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BIOSPECIMEN_ID` int(11) NOT NULL,
  `TRANSACTION_DATE` datetime DEFAULT NULL,
  `QUANTITY` decimal(16,10) DEFAULT NULL,
  `RECORDER` varchar(255) DEFAULT NULL,
  `REASON` text,
  `STATUS_ID` int(11) DEFAULT NULL,
  `REQUEST_ID` int(11) DEFAULT NULL,
  `UNIT_ID` int(11) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `FK_BIOTRANSACTION_BIOSPECIMEN_ID` (`BIOSPECIMEN_ID`),
  KEY `FK_BIOTRANSACTION_STATUS_ID` (`STATUS_ID`),
  KEY `FK_BIOTRANSACTION_REQUEST_ID` (`REQUEST_ID`),
  CONSTRAINT `FK_BIOTRANSACTION_BIOSPECIMEN_ID` FOREIGN KEY (`BIOSPECIMEN_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_BIOTRANSACTION_REQUEST_ID` FOREIGN KEY (`REQUEST_ID`) REFERENCES `access_request` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_BIOTRANSACTION_STATUS_ID` FOREIGN KEY (`STATUS_ID`) REFERENCES `bio_transaction_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=262925 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_site`
--

DROP TABLE IF EXISTS `inv_site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_site` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `CONTACT` varchar(50) DEFAULT NULL,
  `ADDRESS` text,
  `NAME` varchar(50) NOT NULL,
  `PHONE` varchar(50) DEFAULT NULL,
  `LDAP_GROUP` varchar(50) DEFAULT NULL,
  `STUDY_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_INV_SITE_STUDY` (`STUDY_ID`),
  CONSTRAINT `FK_INV_SITE_STUDY` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biocollection`
--

DROP TABLE IF EXISTS `biocollection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biocollection` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BIOCOLLECTION_UID` varchar(50) NOT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `COLLECTIONDATE` datetime DEFAULT NULL,
  `DELETED` int(11) DEFAULT NULL,
  `TIMESTAMP` varchar(55) DEFAULT NULL,
  `COMMENTS` text,
  `HOSPITAL` varchar(50) DEFAULT NULL,
  `SURGERYDATE` datetime DEFAULT NULL,
  `DIAG_CATEGORY` text,
  `REF_DOCTOR` varchar(50) DEFAULT NULL,
  `PATIENTAGE` int(11) DEFAULT NULL,
  `DISCHARGEDATE` datetime DEFAULT NULL,
  `HOSPITAL_UR` varchar(50) DEFAULT NULL,
  `DIAG_DATE` datetime DEFAULT NULL,
  `COLLECTIONGROUP_ID` int(11) DEFAULT NULL,
  `EPISODE_NUM` varchar(50) DEFAULT NULL,
  `EPISODE_DESC` varchar(50) DEFAULT NULL,
  `COLLECTIONGROUP` varchar(50) DEFAULT NULL,
  `TISSUETYPE` varchar(50) DEFAULT NULL,
  `TISSUECLASS` varchar(50) DEFAULT NULL,
  `PATHLABNO` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_collection_name_idx` (`NAME`),
  KEY `fk_collection_link_subject_study` (`LINK_SUBJECT_STUDY_ID`),
  KEY `fk_collection_study` (`STUDY_ID`),
  KEY `fk_collection_biocollection_uid_idx` (`BIOCOLLECTION_UID`),
  CONSTRAINT `fk_collection_link_subject_study` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `study`.`link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_collection_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16551 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `study_inv_site`
--

DROP TABLE IF EXISTS `study_inv_site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study_inv_site` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `INV_SITE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_study_inv_site_study` (`STUDY_ID`),
  KEY `fk_study_inv_site_inv_site` (`INV_SITE_ID`),
  CONSTRAINT `fk_study_inv_site_inv_site` FOREIGN KEY (`INV_SITE_ID`) REFERENCES `inv_site` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_inv_site_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=160 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biospecimenuid_sequence`
--

DROP TABLE IF EXISTS `biospecimenuid_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimenuid_sequence` (
  `STUDY_NAME_ID` varchar(150) NOT NULL,
  `UID_SEQUENCE` int(11) NOT NULL DEFAULT '0',
  `INSERT_LOCK` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`STUDY_NAME_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-12-12 11:03:20
CREATE DATABASE  IF NOT EXISTS `study` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `study`;
-- MySQL dump 10.13  Distrib 5.5.32, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: study
-- ------------------------------------------------------
-- Server version	5.5.32-0ubuntu0.12.04.1

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
-- Table structure for table `link_subject_twin`
--

DROP TABLE IF EXISTS `link_subject_twin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_subject_twin` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FIRST_SUBJECT` int(11) NOT NULL,
  `SECOND_SUBJECT` int(11) NOT NULL,
  `TWIN_TYPE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `correspondence_mode_type`
--

DROP TABLE IF EXISTS `correspondence_mode_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondence_mode_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(4096) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `consent`
--

DROP TABLE IF EXISTS `consent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `STUDY_COMP_ID` int(11) NOT NULL,
  `STUDY_COMP_STATUS_ID` int(11) NOT NULL,
  `CONSENT_STATUS_ID` int(11) NOT NULL,
  `CONSENT_TYPE_ID` int(11) NOT NULL,
  `CONSENT_DATE` date DEFAULT NULL,
  `CONSENTED_BY` varchar(100) DEFAULT NULL,
  `COMMENTS` varchar(500) DEFAULT NULL,
  `REQUESTED_DATE` date DEFAULT NULL,
  `RECEIVED_DATE` date DEFAULT NULL,
  `COMPLETED_DATE` date DEFAULT NULL,
  `CONSENT_DOWNLOADED_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_study` (`STUDY_ID`),
  KEY `fk_study_component` (`STUDY_COMP_ID`),
  KEY `fk_study_comp_status` (`STUDY_COMP_STATUS_ID`),
  KEY `fk_consent_status` (`CONSENT_STATUS_ID`),
  KEY `fk_consent_type` (`CONSENT_TYPE_ID`),
  KEY `fk_consent_downloaded` (`CONSENT_DOWNLOADED_ID`),
  KEY `fk_subject` (`LINK_SUBJECT_STUDY_ID`),
  CONSTRAINT `fk_consent_downloaded` FOREIGN KEY (`CONSENT_DOWNLOADED_ID`) REFERENCES `yes_no` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_consent_status` FOREIGN KEY (`CONSENT_STATUS_ID`) REFERENCES `consent_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_consent_type` FOREIGN KEY (`CONSENT_TYPE_ID`) REFERENCES `consent_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_link_subject_study` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_study_component` FOREIGN KEY (`STUDY_COMP_ID`) REFERENCES `study_comp` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_comp_status` FOREIGN KEY (`STUDY_COMP_STATUS_ID`) REFERENCES `study_comp_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `custom_field_group`
--

DROP TABLE IF EXISTS `custom_field_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_field_group` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(1000) DEFAULT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `PUBLISHED` tinyint(1) DEFAULT NULL,
  `ARK_FUNCTION_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`,`STUDY_ID`,`ARK_FUNCTION_ID`),
  KEY `FK_CUSTOM_FIELD_GROUP_STUDY_ID` (`STUDY_ID`),
  KEY `FK_CUSTOM_FIELD_GROUP_ARK_FUNCTION_ID` (`ARK_FUNCTION_ID`),
  CONSTRAINT `FK_CUSTOM_FIELD_GROUP_ARK_FUNCTION_ID` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CUSTOM_FIELD_GROUP_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `correspondence_direction_type`
--

DROP TABLE IF EXISTS `correspondence_direction_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondence_direction_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(4096) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `correspondences`
--

DROP TABLE IF EXISTS `correspondences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondences` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PERSON_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `ARK_USER_ID` int(11) DEFAULT NULL,
  `DATE` date DEFAULT NULL,
  `TIME` varchar(255) DEFAULT NULL,
  `REASON` varchar(4096) DEFAULT NULL,
  `MODE_TYPE_ID` int(11) DEFAULT NULL,
  `DIRECTION_TYPE_ID` int(11) DEFAULT NULL,
  `OUTCOME_TYPE_ID` int(11) DEFAULT NULL,
  `DETAILS` varchar(4096) DEFAULT NULL,
  `COMMENTS` varchar(4096) DEFAULT NULL,
  `ATTACHMENT_FILENAME` varchar(255) DEFAULT NULL,
  `ATTACHMENT_PAYLOAD` longblob,
  `BILLABLE_ITEM_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `mode_type` (`MODE_TYPE_ID`),
  KEY `direction_type` (`DIRECTION_TYPE_ID`),
  KEY `outcome_type` (`OUTCOME_TYPE_ID`),
  KEY `correspondences_study_id` (`STUDY_ID`),
  KEY `correspondences_person_id` (`PERSON_ID`),
  KEY `fk_correspondences_ark_user` (`ARK_USER_ID`),
  CONSTRAINT `correspondences_direction_type_id` FOREIGN KEY (`DIRECTION_TYPE_ID`) REFERENCES `correspondence_direction_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_mode_type_id` FOREIGN KEY (`MODE_TYPE_ID`) REFERENCES `correspondence_mode_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_outcome_type_id` FOREIGN KEY (`OUTCOME_TYPE_ID`) REFERENCES `correspondence_outcome_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_person_id` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `correspondences_study_id` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_correspondences_ark_user` FOREIGN KEY (`ARK_USER_ID`) REFERENCES `ark_user` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `email_account`
--

DROP TABLE IF EXISTS `email_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `email_account` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `PRIMARY_ACCOUNT` int(11) DEFAULT NULL,
  `PERSON_ID` int(11) NOT NULL,
  `EMAIL_ACCOUNT_TYPE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `EMAIL_ACCOUNT_PER_FK1` (`PERSON_ID`),
  KEY `EMAIL_ACCOUNT_EMA_FK1` (`EMAIL_ACCOUNT_TYPE_ID`),
  CONSTRAINT `email_account_ibfk_1` FOREIGN KEY (`EMAIL_ACCOUNT_TYPE_ID`) REFERENCES `email_account_type` (`ID`),
  CONSTRAINT `email_account_ibfk_2` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`EMAIL_ACCOUNT_TYPE_ID`) REFER `study';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payload`
--

DROP TABLE IF EXISTS `payload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payload` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `payload` longblob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1316 DEFAULT CHARSET=latin1 COMMENT='This is a simple table for storing LObs and an id to represe';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subject_file`
--

DROP TABLE IF EXISTS `subject_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject_file` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `STUDY_COMP_ID` int(11) DEFAULT NULL,
  `FILENAME` text NOT NULL,
  `PAYLOAD` longblob NOT NULL,
  `CHECKSUM` varchar(50) NOT NULL,
  `USER_ID` varchar(100) NOT NULL,
  `COMMENTS` text,
  PRIMARY KEY (`ID`),
  KEY `fk_subject_file_subject` (`LINK_SUBJECT_STUDY_ID`),
  KEY `fk_subject_file_study_comp` (`STUDY_COMP_ID`),
  CONSTRAINT `fk_subject_file_study_comp` FOREIGN KEY (`STUDY_COMP_ID`) REFERENCES `study_comp` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_subject_file_subject` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `consent_section`
--

DROP TABLE IF EXISTS `consent_section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent_section` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(500) NOT NULL,
  `DESCRIPTION` varchar(1000) DEFAULT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `STUDY_COMP_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`(255)),
  KEY `fk_consent_section_1` (`STUDY_ID`),
  KEY `fk_consent_section_2` (`STUDY_COMP_ID`),
  CONSTRAINT `fk_consent_section_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_consent_section_2` FOREIGN KEY (`STUDY_COMP_ID`) REFERENCES `study_comp` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link_study_studycomp`
--

DROP TABLE IF EXISTS `link_study_studycomp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_study_studycomp` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_COMP_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `STUDY_COMP_STATUS_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `LSSC_STUDY_FK` (`STUDY_ID`),
  KEY `LSSC_STUDY_COMP_FK` (`STUDY_COMP_ID`),
  KEY `LSSC_STUDY_COMP_STATUS_FK` (`STUDY_COMP_STATUS_ID`),
  CONSTRAINT `link_study_studycomp_ibfk_1` FOREIGN KEY (`STUDY_COMP_ID`) REFERENCES `study_comp` (`ID`),
  CONSTRAINT `link_study_studycomp_ibfk_2` FOREIGN KEY (`STUDY_COMP_STATUS_ID`) REFERENCES `study_comp_status` (`ID`),
  CONSTRAINT `link_study_studycomp_ibfk_3` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STUDY_COMP_ID`) REFER `study/study_c';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `study_consent_question`
--

DROP TABLE IF EXISTS `study_consent_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study_consent_question` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `QUESTION` varchar(700) DEFAULT NULL,
  `STUDY_ID` int(11) DEFAULT NULL,
  `DATA_TYPE_ID` int(11) DEFAULT NULL,
  `DISCRETE_VALUES` varchar(45) DEFAULT NULL,
  `FIELD_POSITION` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_study_consent_question_1` (`STUDY_ID`),
  KEY `fk_study_consent_question_2` (`DATA_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `audit_history`
--

DROP TABLE IF EXISTS `audit_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_history` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_STATUS_ID` int(11) DEFAULT '0',
  `DATE_TIME` datetime DEFAULT NULL,
  `ACTION_TYPE` varchar(50) NOT NULL,
  `ARK_USER_ID` varchar(255) DEFAULT NULL,
  `COMMENT` varchar(255) DEFAULT NULL,
  `ENTITY_TYPE` varchar(50) NOT NULL,
  `ENTITY_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `AUDIT_HISTORY_STUDY_STATUS_FK` (`STUDY_STATUS_ID`),
  KEY `AUDIT_HISTORY_ENTITY_ID` (`ENTITY_ID`),
  KEY `AUDIT_HISTORY_ACTION_TYPE` (`ACTION_TYPE`),
  KEY `AUDIT_HISTORY_ENTITY_TYPE` (`ENTITY_TYPE`),
  CONSTRAINT `audit_history_ibfk_1` FOREIGN KEY (`STUDY_STATUS_ID`) REFERENCES `study_status` (`ID`),
  CONSTRAINT `audit_history_ibfk_3` FOREIGN KEY (`STUDY_STATUS_ID`) REFERENCES `study_status` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7412 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`ACTION_TYPE_ID`) REFER `study/action';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `phone`
--

DROP TABLE IF EXISTS `phone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phone` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `AREA_CODE` varchar(10) DEFAULT NULL,
  `PHONE_NUMBER` varchar(10) NOT NULL,
  `PERSON_ID` int(11) NOT NULL,
  `PHONE_TYPE_ID` int(11) NOT NULL,
  `PHONE_STATUS_ID` int(11) NOT NULL DEFAULT '0',
  `SOURCE` varchar(500) DEFAULT NULL,
  `DATE_RECEIVED` date DEFAULT NULL,
  `COMMENT` varchar(1000) DEFAULT NULL,
  `SILENT` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `AREA_CODE_2` (`AREA_CODE`,`PHONE_NUMBER`,`PHONE_TYPE_ID`,`PERSON_ID`),
  KEY `PHONE_PHONE_TYPE_FK` (`PHONE_TYPE_ID`),
  KEY `PHONE_PERSON_FK` (`PERSON_ID`),
  KEY `phone_ibfk_3` (`PHONE_STATUS_ID`),
  KEY `phone_ibfk_4` (`SILENT`),
  CONSTRAINT `phone_ibfk_1` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `phone_ibfk_2` FOREIGN KEY (`PHONE_TYPE_ID`) REFERENCES `phone_type` (`ID`),
  CONSTRAINT `phone_ibfk_3` FOREIGN KEY (`PHONE_STATUS_ID`) REFERENCES `phone_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `phone_ibfk_4` FOREIGN KEY (`SILENT`) REFERENCES `yes_no` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_ID`) REFER `study/person`(`ID';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link_subject_studycomp`
--

DROP TABLE IF EXISTS `link_subject_studycomp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_subject_studycomp` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PERSON_SUBJECT_ID` int(11) NOT NULL,
  `STUDY_COMP_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `STUDY_COMP_STATUS_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `LINK_SSC_PERSON_FK` (`PERSON_SUBJECT_ID`),
  KEY `LINK_SSC_STUDY_COMP_FK` (`STUDY_COMP_ID`),
  KEY `LINK_SUBJECT_STUDYCOM_FK3` (`STUDY_ID`),
  KEY `LINK_SSC_STUDY_COMP_STATUS_FK` (`STUDY_COMP_STATUS_ID`),
  CONSTRAINT `link_subject_studycomp_ibfk_1` FOREIGN KEY (`PERSON_SUBJECT_ID`) REFERENCES `person` (`ID`),
  CONSTRAINT `link_subject_studycomp_ibfk_2` FOREIGN KEY (`STUDY_COMP_ID`) REFERENCES `study_comp` (`ID`),
  CONSTRAINT `link_subject_studycomp_ibfk_3` FOREIGN KEY (`STUDY_COMP_STATUS_ID`) REFERENCES `study_comp_status` (`ID`),
  CONSTRAINT `link_subject_studycomp_ibfk_4` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_SUBJECT_ID`) REFER `study/per';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `person_lastname_history`
--

DROP TABLE IF EXISTS `person_lastname_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_lastname_history` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PERSON_ID` int(11) NOT NULL,
  `LASTNAME` varchar(50) NOT NULL,
  `date_inserted` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  KEY `SURNAME` (`LASTNAME`),
  KEY `PERSON_ID` (`PERSON_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20242 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `person_contact_method`
--

DROP TABLE IF EXISTS `person_contact_method`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_contact_method` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `NAME` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `study_user_role_permission_view`
--

DROP TABLE IF EXISTS `study_user_role_permission_view`;
/*!50001 DROP VIEW IF EXISTS `study_user_role_permission_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `study_user_role_permission_view` (
  `studyName` tinyint NOT NULL,
  `userName` tinyint NOT NULL,
  `roleName` tinyint NOT NULL,
  `moduleName` tinyint NOT NULL,
  `create` tinyint NOT NULL,
  `read` tinyint NOT NULL,
  `update` tinyint NOT NULL,
  `delete` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `subject_study_consent`
--

DROP TABLE IF EXISTS `subject_study_consent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject_study_consent` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUBJECT_ID` int(11) DEFAULT NULL,
  `STUDY_ID` int(11) DEFAULT NULL,
  `STUDY_CONSENT_QUESTION_ID` int(11) DEFAULT NULL,
  `STATUS` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_subject_study_consent_1` (`STUDY_ID`),
  KEY `fk_subject_study_consent_2` (`SUBJECT_ID`),
  KEY `fk_subject_study_consent_3` (`STUDY_CONSENT_QUESTION_ID`),
  CONSTRAINT `fk_subject_study_consent_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_subject_study_consent_2` FOREIGN KEY (`SUBJECT_ID`) REFERENCES `person` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_subject_study_consent_3` FOREIGN KEY (`STUDY_CONSENT_QUESTION_ID`) REFERENCES `study_consent_question` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `upload`
--

DROP TABLE IF EXISTS `upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `upload` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `FILE_FORMAT_ID` int(11) NOT NULL,
  `DELIMITER_TYPE_ID` int(11) NOT NULL,
  `FILENAME` text NOT NULL,
  `CHECKSUM` varchar(50) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `START_TIME` datetime NOT NULL,
  `FINISH_TIME` datetime DEFAULT NULL,
  `UPLOAD_REPORT` longblob,
  `ARK_FUNCTION_ID` int(11) NOT NULL,
  `UPLOAD_TYPE_ID` int(11) DEFAULT NULL,
  `PAYLOAD_ID` int(11) NOT NULL,
  `STATUS_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_upload_file_format` (`FILE_FORMAT_ID`),
  KEY `fk_upload_delimiter` (`DELIMITER_TYPE_ID`),
  KEY `ID` (`ID`),
  KEY `fk_upload_ark_function_id` (`ARK_FUNCTION_ID`),
  KEY `fk_upload_study` (`STUDY_ID`),
  KEY `fk_upload_1` (`UPLOAD_TYPE_ID`),
  KEY `fk_upload_payload` (`PAYLOAD_ID`),
  KEY `fk_upload_status` (`STATUS_ID`),
  CONSTRAINT `fk_upload_ark_function_id` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_delimiter_type` FOREIGN KEY (`DELIMITER_TYPE_ID`) REFERENCES `delimiter_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_file_format` FOREIGN KEY (`FILE_FORMAT_ID`) REFERENCES `file_format` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_payload` FOREIGN KEY (`PAYLOAD_ID`) REFERENCES `payload` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_status` FOREIGN KEY (`STATUS_ID`) REFERENCES `upload_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upload_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1268 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`DELIMITER_TYPE_ID`) REFER `study/del';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `correspondence_outcome_type`
--

DROP TABLE IF EXISTS `correspondence_outcome_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correspondence_outcome_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(496) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subjectuid_padchar`
--

DROP TABLE IF EXISTS `subjectuid_padchar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subjectuid_padchar` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `consent_file`
--

DROP TABLE IF EXISTS `consent_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent_file` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CONSENT_ID` int(11) NOT NULL,
  `FILENAME` text NOT NULL,
  `PAYLOAD` longblob NOT NULL,
  `CHECKSUM` varchar(50) NOT NULL,
  `USER_ID` varchar(50) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  `UPDATE_USER_ID` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_consent_file_consent` (`CONSENT_ID`),
  CONSTRAINT `fk_upload_consent` FOREIGN KEY (`CONSENT_ID`) REFERENCES `consent` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `demographic_field`
--

DROP TABLE IF EXISTS `demographic_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `demographic_field` (
  `ID` int(11) NOT NULL,
  `ENTITY` varchar(255) DEFAULT NULL,
  `FIELD_NAME` varchar(255) DEFAULT NULL,
  `PUBLIC_FIELD_NAME` varchar(255) DEFAULT NULL,
  `FIELD_TYPE_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_demographic_field_field_type` (`FIELD_TYPE_ID`),
  CONSTRAINT `fk_demographic_field_field_type` FOREIGN KEY (`FIELD_TYPE_ID`) REFERENCES `field_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `consent_option`
--

DROP TABLE IF EXISTS `consent_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent_option` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link_study_studysite`
--

DROP TABLE IF EXISTS `link_study_studysite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_study_studysite` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_SITE_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `LINK_STUDY_STUDYSITE_STUDY_SITE_FK` (`STUDY_SITE_ID`),
  KEY `LINK_STUDY_STUDYSITE_STUDY_FK` (`STUDY_ID`),
  CONSTRAINT `link_study_studysite_ibfk_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`),
  CONSTRAINT `link_study_studysite_ibfk_2` FOREIGN KEY (`STUDY_SITE_ID`) REFERENCES `study_site` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STUDY_ID`) REFER `study/study`(`ID`)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `role_policy`
--

DROP TABLE IF EXISTS `role_policy`;
/*!50001 DROP VIEW IF EXISTS `role_policy`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `role_policy` (
  `Role` tinyint NOT NULL,
  `Module` tinyint NOT NULL,
  `FunctionGroup` tinyint NOT NULL,
  `Permission` tinyint NOT NULL,
  `Function` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `study_comp_status`
--

DROP TABLE IF EXISTS `study_comp_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study_comp_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `csv_blob`
--

DROP TABLE IF EXISTS `csv_blob`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `csv_blob` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CSV_BLOB` longblob NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Table containing BLOB references of CSV files for import/upl';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link_subject_contact`
--

DROP TABLE IF EXISTS `link_subject_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_subject_contact` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PERSON_CONTACT_ID` int(11) DEFAULT NULL,
  `PERSON_SUBJECT_ID` int(11) DEFAULT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `RELATIONSHIP_ID` int(11) DEFAULT NULL,
  `FAMILY_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `LINK_SUBJECT_CONTACT_PERSON_FK` (`PERSON_CONTACT_ID`),
  KEY `LINK_SUBJECT_CONTACT_PERSON_SUBJECT_FK` (`PERSON_SUBJECT_ID`),
  KEY `LINK_SUBJECT_CONTACT_STUDY_FK` (`STUDY_ID`),
  KEY `LINK_SUBJECT_CONTACT_RELATIONSHIP_FK` (`RELATIONSHIP_ID`),
  CONSTRAINT `link_subject_contact_ibfk_1` FOREIGN KEY (`PERSON_CONTACT_ID`) REFERENCES `person` (`ID`),
  CONSTRAINT `link_subject_contact_ibfk_2` FOREIGN KEY (`PERSON_SUBJECT_ID`) REFERENCES `person` (`ID`),
  CONSTRAINT `link_subject_contact_ibfk_3` FOREIGN KEY (`RELATIONSHIP_ID`) REFERENCES `relationship` (`ID`),
  CONSTRAINT `link_subject_contact_ibfk_4` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_CONTACT_ID`) REFER `study/per';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `custom_field`
--

DROP TABLE IF EXISTS `custom_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_field` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` text,
  `FIELD_TYPE_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `ARK_FUNCTION_ID` int(11) NOT NULL,
  `UNIT_TYPE_ID` int(11) DEFAULT NULL,
  `MIN_VALUE` varchar(45) DEFAULT NULL,
  `MAX_VALUE` varchar(45) DEFAULT NULL,
  `ENCODED_VALUES` text,
  `MISSING_VALUE` varchar(45) DEFAULT NULL,
  `HAS_DATA` tinyint(4) NOT NULL DEFAULT '0',
  `CUSTOM_FIELD_LABEL` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`,`STUDY_ID`,`ARK_FUNCTION_ID`),
  KEY `FK_STUDY_ID` (`STUDY_ID`),
  KEY `FK_UNIT_TYPE_ID` (`UNIT_TYPE_ID`),
  KEY `FK_CUSTOMFIELD_ARK_FUNCTION_ID` (`ARK_FUNCTION_ID`),
  KEY `FK_CUSTOMFIELD_FIELD_TYPE_ID` (`FIELD_TYPE_ID`),
  CONSTRAINT `FK_CUSTOMFIELD_ARK_FUNCTION_ID` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CUSTOMFIELD_FIELD_TYPE_ID` FOREIGN KEY (`FIELD_TYPE_ID`) REFERENCES `field_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CUSTOM_FIELD_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CUSTOM_FIELD_UNIT_TYPE_ID` FOREIGN KEY (`UNIT_TYPE_ID`) REFERENCES `unit_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=748 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `study_comp`
--

DROP TABLE IF EXISTS `study_comp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study_comp` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `KEYWORD` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`,`STUDY_ID`),
  KEY `STUDY_COMP_STUDY_FK` (`STUDY_ID`),
  CONSTRAINT `study_comp_ibfk_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STUDY_ID`) REFER `study/study`(`ID`)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link_site_contact`
--

DROP TABLE IF EXISTS `link_site_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_site_contact` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PERSON_ID` int(11) NOT NULL,
  `STUDY_SITE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `LINK_SITE_CONTACT_FK1` (`PERSON_ID`),
  KEY `LINK_SITE_CONTACT_STUDY_SITE_FK` (`STUDY_SITE_ID`),
  CONSTRAINT `link_site_contact_ibfk_1` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`),
  CONSTRAINT `link_site_contact_ibfk_2` FOREIGN KEY (`STUDY_SITE_ID`) REFERENCES `study_site` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_ID`) REFER `study/person`(`ID';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `state`
--

DROP TABLE IF EXISTS `state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `state` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `COUNTRY_ID` int(11) NOT NULL,
  `TYPE` varchar(45) NOT NULL COMMENT 'what they call a ''state'', ''province'' , etc\ncan be multiple for a country\neg; au has state and territory',
  `NAME` varchar(255) NOT NULL,
  `CODE` varchar(45) NOT NULL,
  `SHORT_NAME` varchar(56) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_country_id` (`COUNTRY_ID`),
  CONSTRAINT `fk_country_id` FOREIGN KEY (`COUNTRY_ID`) REFERENCES `country` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=23126 DEFAULT CHARSET=latin1 COMMENT='A link table that associates a country and its respective st';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link_study_arkmodule`
--

DROP TABLE IF EXISTS `link_study_arkmodule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_study_arkmodule` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `ARK_MODULE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_LINK_STUDY_ARKMODULE_STUDY_ID` (`STUDY_ID`),
  KEY `FK_LINK_STUDY_ARKMODULE_ARK_MODULE_ID` (`ARK_MODULE_ID`),
  CONSTRAINT `FK_LINK_STUDY_ARKMODULE_ARK_MODULE_ID` FOREIGN KEY (`ARK_MODULE_ID`) REFERENCES `ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `FK_LINK_STUDY_ARKMODULE_STUDY_ID` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=475 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link_subject_study`
--

DROP TABLE IF EXISTS `link_subject_study`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_subject_study` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PERSON_ID` int(11) NOT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `SUBJECT_STATUS_ID` int(11) NOT NULL DEFAULT '0',
  `SUBJECT_UID` varchar(50) NOT NULL,
  `CONSENT_TO_ACTIVE_CONTACT_ID` int(11) DEFAULT NULL,
  `CONSENT_TO_PASSIVE_DATA_GATHERING_ID` int(11) DEFAULT NULL,
  `CONSENT_TO_USE_DATA_ID` int(11) DEFAULT NULL,
  `CONSENT_STATUS_ID` int(11) DEFAULT NULL,
  `CONSENT_TYPE_ID` int(11) DEFAULT NULL,
  `CONSENT_DATE` date DEFAULT NULL,
  `OTHER_STATE` varchar(255) DEFAULT NULL,
  `HEARD_ABOUT_STUDY` varchar(500) DEFAULT NULL,
  `COMMENTS` varchar(1000) DEFAULT NULL,
  `CONSENT_DOWNLOADED` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UQ_STUDY_ID_SUBJECT_UID` (`STUDY_ID`,`SUBJECT_UID`),
  KEY `FK_LINK_SUBJECT_STUDY_PERSON_FK` (`PERSON_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_SUBJECT_STATUS_FK` (`SUBJECT_STATUS_ID`),
  KEY `FK_LINK_SBJT_STUDY_CNS_ACT_CNCT` (`CONSENT_TO_ACTIVE_CONTACT_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_CNS_PASS_DATA` (`CONSENT_TO_PASSIVE_DATA_GATHERING_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_CNS_USE_DATA` (`CONSENT_TO_USE_DATA_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_SUBJECT_UID` (`SUBJECT_UID`),
  KEY `FK_LINK_SUBJECT_STUDY_STUDY_FK` (`STUDY_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_CONSENT_STATUS_ID` (`CONSENT_STATUS_ID`),
  KEY `FK_LINK_SUBJECT_STUDY_CONSENT_TYPE_ID` (`CONSENT_TYPE_ID`),
  KEY `FK_CONSENT_DOWNLOADED_YES_NO` (`CONSENT_DOWNLOADED`),
  CONSTRAINT `FK_CONSENT_DOWNLOADED_YES_NO` FOREIGN KEY (`CONSENT_DOWNLOADED`) REFERENCES `yes_no` (`ID`),
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_CNS_ACT_CTNT` FOREIGN KEY (`CONSENT_TO_ACTIVE_CONTACT_ID`) REFERENCES `consent_option` (`ID`),
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_CNS_PASS_DATA` FOREIGN KEY (`CONSENT_TO_PASSIVE_DATA_GATHERING_ID`) REFERENCES `consent_option` (`ID`),
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_CNS_USE_DATA` FOREIGN KEY (`CONSENT_TO_USE_DATA_ID`) REFERENCES `consent_option` (`ID`),
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_CONSENT_STATUS_ID` FOREIGN KEY (`CONSENT_STATUS_ID`) REFERENCES `consent_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_CONSENT_TYPE_ID` FOREIGN KEY (`CONSENT_TYPE_ID`) REFERENCES `consent_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_PERSON_FK` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`),
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_STUDY_FK` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_LINK_SUBJECT_STUDY_SUBJECT_STATUS_FK` FOREIGN KEY (`SUBJECT_STATUS_ID`) REFERENCES `subject_status` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=555792 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`PERSON_ID`) REFER `study/person`(`ID';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link_study_substudy`
--

DROP TABLE IF EXISTS `link_study_substudy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_study_substudy` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `SUB_STUDY_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `LINK_STUDY_SUBSTUDY_STUDY_FK` (`STUDY_ID`),
  KEY `LINK_STUDY_SUBSTUDY_SUB_STUDY_FK` (`SUB_STUDY_ID`),
  CONSTRAINT `link_study_substudy_ibfk_1` FOREIGN KEY (`STUDY_ID`) REFERENCES `study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `link_study_substudy_ibfk_2` FOREIGN KEY (`SUB_STUDY_ID`) REFERENCES `study` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STUDY_ID`) REFER `study/study`(`ID`)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `custom_field_upload`
--

DROP TABLE IF EXISTS `custom_field_upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_field_upload` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CUSTOM_FIELD_ID` int(11) NOT NULL,
  `UPLOAD_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_CFU_CUSTOM_FIELD_ID` (`CUSTOM_FIELD_ID`),
  KEY `FK_CFU_UPLOAD_ID` (`UPLOAD_ID`),
  CONSTRAINT `FK_CFU_CUSTOM_FIELD_ID` FOREIGN KEY (`CUSTOM_FIELD_ID`) REFERENCES `custom_field` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CFU_UPLOAD_ID` FOREIGN KEY (`UPLOAD_ID`) REFERENCES `upload` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=871 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link_subject_pedigree`
--

DROP TABLE IF EXISTS `link_subject_pedigree`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_subject_pedigree` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LINK_SUBJECT_STUDY_ID` int(11) DEFAULT NULL,
  `RELATIVE_ID` int(11) DEFAULT NULL,
  `RELATIONSHIP_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `custom_field_display`
--

DROP TABLE IF EXISTS `custom_field_display`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_field_display` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CUSTOM_FIELD_ID` int(11) NOT NULL,
  `CUSTOM_FIELD_GROUP_ID` int(11) DEFAULT NULL,
  `SEQUENCE` int(11) DEFAULT NULL,
  `REQUIRED` int(11) DEFAULT NULL,
  `REQUIRED_MESSAGE` varchar(45) DEFAULT NULL,
  `ALLOW_MULTIPLE_SELECTION` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `FK_CUSTOM_FIELD_GROUP_ID` (`CUSTOM_FIELD_GROUP_ID`),
  KEY `FK_CUSTOM_FIELD_GROUP_CUSTOM_FIELD_ID` (`CUSTOM_FIELD_ID`),
  CONSTRAINT `FK_CUSTOM_FIELD_GROUP_CUSTOM_FIELD_ID` FOREIGN KEY (`CUSTOM_FIELD_ID`) REFERENCES `custom_field` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CUSTOM_FIELD_GROUP_ID` FOREIGN KEY (`CUSTOM_FIELD_GROUP_ID`) REFERENCES `custom_field_group` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=806 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ADDRESS_LINE_1` varchar(255) DEFAULT NULL,
  `STREET_ADDRESS` varchar(255) DEFAULT NULL,
  `CITY` varchar(45) DEFAULT NULL,
  `STATE_ID` int(11) DEFAULT NULL,
  `POST_CODE` varchar(10) DEFAULT NULL,
  `COUNTRY_ID` int(11) DEFAULT NULL,
  `ADDRESS_STATUS_ID` int(11) DEFAULT NULL,
  `ADDRESS_TYPE_ID` int(11) NOT NULL,
  `OTHER_STATE` varchar(45) DEFAULT NULL,
  `PERSON_ID` int(11) NOT NULL,
  `DATE_RECEIVED` date DEFAULT NULL,
  `COMMENTS` text,
  `PREFERRED_MAILING_ADDRESS` int(11) NOT NULL,
  `SOURCE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_address_country` (`COUNTRY_ID`),
  KEY `fk_address_person` (`PERSON_ID`),
  KEY `fk_address_address_type` (`ADDRESS_TYPE_ID`),
  KEY `fk_address_status` (`ADDRESS_STATUS_ID`),
  KEY `fk_address_preferred_mailing_address_id` (`PREFERRED_MAILING_ADDRESS`),
  KEY `fk_address_state` (`STATE_ID`),
  CONSTRAINT `fk_address_address_type` FOREIGN KEY (`ADDRESS_TYPE_ID`) REFERENCES `address_type` (`ID`),
  CONSTRAINT `fk_address_country` FOREIGN KEY (`COUNTRY_ID`) REFERENCES `country` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_address_person` FOREIGN KEY (`PERSON_ID`) REFERENCES `person` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_address_state` FOREIGN KEY (`STATE_ID`) REFERENCES `state` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_address_status` FOREIGN KEY (`ADDRESS_STATUS_ID`) REFERENCES `address_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=256 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`ADDRESS_TYPE_ID`) REFER `study/addre';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subjectuid_sequence`
--

DROP TABLE IF EXISTS `subjectuid_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subjectuid_sequence` (
  `STUDY_NAME_ID` varchar(150) NOT NULL,
  `UID_SEQUENCE` int(11) NOT NULL DEFAULT '0',
  `INSERT_LOCK` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`STUDY_NAME_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'LEGACY ID, \nkeep table structures similar\n',
  `NAME` varchar(100) NOT NULL COMMENT 'Common / \nColloquial Name',
  `COUNTRY_CODE` varchar(2) NOT NULL COMMENT 'Official as used in local match ups, unique\n',
  `ALPHA_3_CODE` varchar(45) NOT NULL,
  `NUMERIC_CODE` varchar(45) NOT NULL,
  `OFFICIAL_NAME` varchar(45) NOT NULL COMMENT 'Correct Name, Probably not used often\n',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=253 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `study`
--

DROP TABLE IF EXISTS `study`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(150) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `DATE_OF_APPLICATION` date DEFAULT NULL,
  `ESTIMATED_YEAR_OF_COMPLETION` int(11) DEFAULT NULL,
  `CHIEF_INVESTIGATOR` varchar(50) DEFAULT NULL,
  `CO_INVESTIGATOR` varchar(50) DEFAULT NULL,
  `AUTO_GENERATE_SUBJECTUID` int(11) NOT NULL,
  `SUBJECTUID_START` int(11) DEFAULT NULL,
  `STUDY_STATUS_ID` int(11) NOT NULL,
  `SUBJECTUID_PREFIX` varchar(20) DEFAULT NULL,
  `CONTACT_PERSON` varchar(50) DEFAULT NULL,
  `CONTACT_PERSON_PHONE` varchar(20) DEFAULT NULL,
  `LDAP_GROUP_NAME` varchar(100) DEFAULT NULL,
  `AUTO_CONSENT` int(11) DEFAULT NULL,
  `SUB_STUDY_BIOSPECIMEN_PREFIX` varchar(20) DEFAULT NULL,
  `STUDY_LOGO` blob,
  `FILENAME` varchar(255) DEFAULT NULL,
  `SUBJECTUID_TOKEN_ID` int(11) DEFAULT NULL,
  `SUBJECTUID_PADCHAR_ID` int(11) DEFAULT NULL,
  `SUBJECT_KEY_PREFIX` varchar(45) DEFAULT NULL,
  `SUBJECT_KEY_START` varchar(45) DEFAULT NULL,
  `PARENT_ID` int(11) DEFAULT NULL,
  `AUTO_GENERATE_BIOSPECIMENUID` tinyint(4) NOT NULL DEFAULT '0',
  `AUTO_GENERATE_BIOCOLLECTIONUID` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `STUDY_STUDY_STATUS_FK1` (`STUDY_STATUS_ID`),
  KEY `ID` (`ID`),
  KEY `fk_study_subjectuid_padchar` (`SUBJECTUID_PADCHAR_ID`),
  KEY `fk_study_subjectuid_token` (`SUBJECTUID_TOKEN_ID`),
  KEY `fk_study_study` (`PARENT_ID`),
  CONSTRAINT `fk_study_study` FOREIGN KEY (`PARENT_ID`) REFERENCES `study` (`ID`) ON DELETE SET NULL ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_study_status` FOREIGN KEY (`STUDY_STATUS_ID`) REFERENCES `study_status` (`ID`),
  CONSTRAINT `fk_study_subjectuid_padchar` FOREIGN KEY (`SUBJECTUID_PADCHAR_ID`) REFERENCES `subjectuid_padchar` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_subjectuid_token` FOREIGN KEY (`SUBJECTUID_TOKEN_ID`) REFERENCES `subjectuid_token` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`STUDY_STATUS_ID`) REFER `study/study';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subject_custom_field_data`
--

DROP TABLE IF EXISTS `subject_custom_field_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject_custom_field_data` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `CUSTOM_FIELD_DISPLAY_ID` int(11) NOT NULL,
  `TEXT_DATA_VALUE` text,
  `DATE_DATA_VALUE` datetime DEFAULT NULL,
  `ERROR_DATA_VALUE` text,
  `NUMBER_DATA_VALUE` double DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_CFD_LINK_SUBJECT_STUDY_ID` (`LINK_SUBJECT_STUDY_ID`),
  KEY `FK_CFD_CUSTOM_FIELD_DISPLAY_ID` (`CUSTOM_FIELD_DISPLAY_ID`),
  CONSTRAINT `FK_CFD_CUSTOM_FIELD_DISPLAY_ID` FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`) REFERENCES `custom_field_display` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CFD_LINK_SUBJECT_STUDY_ID` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1600101037 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `consent_answer`
--

DROP TABLE IF EXISTS `consent_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consent_answer` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FIRST_NAME` varchar(50) DEFAULT NULL,
  `MIDDLE_NAME` varchar(50) DEFAULT NULL,
  `LAST_NAME` varchar(50) DEFAULT NULL,
  `PREFERRED_NAME` varchar(150) DEFAULT NULL,
  `GENDER_TYPE_ID` int(11) NOT NULL DEFAULT '0',
  `DATE_OF_BIRTH` date DEFAULT NULL,
  `DATE_OF_DEATH` date DEFAULT NULL,
  `REGISTRATION_DATE` date DEFAULT NULL,
  `CAUSE_OF_DEATH` varchar(255) DEFAULT NULL,
  `VITAL_STATUS_ID` int(11) NOT NULL DEFAULT '0',
  `TITLE_TYPE_ID` int(11) NOT NULL DEFAULT '0',
  `MARITAL_STATUS_ID` int(11) DEFAULT NULL,
  `PERSON_CONTACT_METHOD_ID` int(11) DEFAULT NULL,
  `PREFERRED_EMAIL` varchar(150) DEFAULT NULL,
  `OTHER_EMAIL` varchar(45) DEFAULT NULL,
  `DATE_LAST_KNOWN_ALIVE` date DEFAULT NULL,
  `OTHER_ID` int(11) DEFAULT NULL,
  `OTHER_EMAIL_STATUS` int(11) DEFAULT '0',
  `PREFERRED_EMAIL_STATUS` int(11) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `PERSON_GENDER_TYPE_FK` (`GENDER_TYPE_ID`),
  KEY `PERSON_VITAL_STATUS_FK` (`VITAL_STATUS_ID`),
  KEY `PERSON_TITLE_TYPE_FK` (`TITLE_TYPE_ID`),
  KEY `fk_person_person_contact_method` (`PERSON_CONTACT_METHOD_ID`),
  KEY `fk_person_marital_status` (`MARITAL_STATUS_ID`),
  KEY `fk_person_other_email_status` (`OTHER_EMAIL_STATUS`),
  KEY `fk_person_preferred_email_status` (`PREFERRED_EMAIL_STATUS`),
  CONSTRAINT `fk_person_gender_type` FOREIGN KEY (`GENDER_TYPE_ID`) REFERENCES `gender_type` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `fk_person_marital_status` FOREIGN KEY (`MARITAL_STATUS_ID`) REFERENCES `marital_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_person_other_email_status` FOREIGN KEY (`OTHER_EMAIL_STATUS`) REFERENCES `email_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_person_person_contact_method` FOREIGN KEY (`PERSON_CONTACT_METHOD_ID`) REFERENCES `person_contact_method` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_person_preferred_email_status` FOREIGN KEY (`PREFERRED_EMAIL_STATUS`) REFERENCES `email_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_person_title_type` FOREIGN KEY (`TITLE_TYPE_ID`) REFERENCES `title_type` (`ID`),
  CONSTRAINT `fk_person_vital_status` FOREIGN KEY (`VITAL_STATUS_ID`) REFERENCES `vital_status` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=367324 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`GENDER_TYPE_ID`) REFER `study/gender';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `study_site`
--

DROP TABLE IF EXISTS `study_site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `study_site` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `ADDRESS_ID` int(11) NOT NULL,
  `DOMAIN_TYPE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `STUDY_SITE_ADDRES_FK1` (`ADDRESS_ID`),
  KEY `STUDY_SITE_DOMAIN_TYPE_FK` (`DOMAIN_TYPE_ID`),
  CONSTRAINT `study_site_ibfk_1` FOREIGN KEY (`ADDRESS_ID`) REFERENCES `address` (`ID`),
  CONSTRAINT `study_site_ibfk_2` FOREIGN KEY (`DOMAIN_TYPE_ID`) REFERENCES `domain_type` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB; (`ADDRESS_ID`) REFER `study/address`(`';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Final view structure for view `study_user_role_permission_view`
--

/*!50001 DROP TABLE IF EXISTS `study_user_role_permission_view`*/;
/*!50001 DROP VIEW IF EXISTS `study_user_role_permission_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`arkadmin`@`130.95.56.%` SQL SECURITY DEFINER */
/*!50001 VIEW `study_user_role_permission_view` AS select distinct `study`.`NAME` AS `studyName`,`ark_user`.`LDAP_USER_NAME` AS `userName`,`ark_role`.`NAME` AS `roleName`,`ark_module`.`NAME` AS `moduleName`,max(if((`arpt`.`ARK_PERMISSION_ID` = 1),_utf8'Y',_utf8'N')) AS `create`,max(if((`arpt`.`ARK_PERMISSION_ID` = 2),_utf8'Y',_utf8'N')) AS `read`,max(if((`arpt`.`ARK_PERMISSION_ID` = 3),_utf8'Y',_utf8'N')) AS `update`,max(if((`arpt`.`ARK_PERMISSION_ID` = 4),_utf8'Y',_utf8'N')) AS `delete` from ((((((`ark_role_policy_template` `arpt` join `ark_role`) join `ark_user_role`) join `ark_user`) join `ark_module`) join `ark_permission` `ap`) join `study`) where ((`arpt`.`ARK_ROLE_ID` = `ark_role`.`ID`) and (`arpt`.`ARK_MODULE_ID` = `ark_module`.`ID`) and (`arpt`.`ARK_PERMISSION_ID` = `ap`.`ID`) and (`arpt`.`ARK_ROLE_ID` = `ark_user_role`.`ARK_ROLE_ID`) and (`arpt`.`ARK_MODULE_ID` = `ark_user_role`.`ARK_MODULE_ID`) and (`ark_user_role`.`ARK_ROLE_ID` = `ark_role`.`ID`) and (`ark_user_role`.`ARK_MODULE_ID` = `ark_module`.`ID`) and (`ark_user_role`.`ARK_USER_ID` = `ark_user`.`ID`) and (`ark_user_role`.`STUDY_ID` = `study`.`ID`)) group by `study`.`NAME`,`ark_user`.`LDAP_USER_NAME`,`ark_role`.`NAME`,`ark_module`.`NAME` order by `ark_user_role`.`STUDY_ID`,`ark_user`.`LDAP_USER_NAME`,`ark_role`.`ID` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `role_policy`
--

/*!50001 DROP TABLE IF EXISTS `role_policy`*/;
/*!50001 DROP VIEW IF EXISTS `role_policy`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`arkadmin`@`130.95.56.%` SQL SECURITY DEFINER */
/*!50001 VIEW `role_policy` AS select `ar`.`NAME` AS `Role`,`am`.`NAME` AS `Module`,`af`.`NAME` AS `FunctionGroup`,`ap`.`NAME` AS `Permission`,`af`.`DESCRIPTION` AS `Function` from ((((`ark_role_policy_template` `arpt` join `ark_role` `ar` on((`arpt`.`ARK_ROLE_ID` = `ar`.`ID`))) join `ark_permission` `ap` on((`arpt`.`ARK_PERMISSION_ID` = `ap`.`ID`))) left join `ark_module` `am` on((`arpt`.`ARK_MODULE_ID` = `am`.`ID`))) left join `ark_function` `af` on((`arpt`.`ARK_FUNCTION_ID` = `af`.`ID`))) order by `ar`.`ID`,`af`.`ID`,`ap`.`ID` */;
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

-- Dump completed on 2013-12-12 11:03:20
CREATE DATABASE  IF NOT EXISTS `reporting` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `reporting`;
-- MySQL dump 10.13  Distrib 5.5.32, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: reporting
-- ------------------------------------------------------
-- Server version	5.5.32-0ubuntu0.12.04.1

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
-- Table structure for table `biocollection_field`
--

DROP TABLE IF EXISTS `biocollection_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biocollection_field` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ENTITY` varchar(255) DEFAULT NULL,
  `FIELD_NAME` varchar(255) DEFAULT NULL,
  `PUBLIC_FIELD_NAME` varchar(255) DEFAULT NULL,
  `FIELD_TYPE_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_biocollection_field_field_type` (`FIELD_TYPE_ID`),
  CONSTRAINT `fk_biocollection_field_field_type` FOREIGN KEY (`FIELD_TYPE_ID`) REFERENCES `study`.`field_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biocollection_field_search`
--

DROP TABLE IF EXISTS `biocollection_field_search`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biocollection_field_search` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BIOCOLLECTION_FIELD_ID` int(11) DEFAULT NULL,
  `SEARCH_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uq_bcfs_bcf_s` (`BIOCOLLECTION_FIELD_ID`,`SEARCH_ID`),
  KEY `fk_dfs_biocollection_field` (`BIOCOLLECTION_FIELD_ID`),
  KEY `fk_dfs_search` (`SEARCH_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=latin1 COMMENT='many2many join biocollection_field and search';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biospecimen_field`
--

DROP TABLE IF EXISTS `biospecimen_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimen_field` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ENTITY` varchar(255) DEFAULT NULL,
  `FIELD_NAME` varchar(255) DEFAULT NULL,
  `PUBLIC_FIELD_NAME` varchar(255) DEFAULT NULL,
  `FIELD_TYPE_ID` int(11) DEFAULT NULL,
  `FILTERABLE` tinyint(4) DEFAULT '1',
  PRIMARY KEY (`ID`),
  KEY `fk_biospecimen_field_field_type` (`FIELD_TYPE_ID`),
  CONSTRAINT `fk_biospecimen_field_field_type` FOREIGN KEY (`FIELD_TYPE_ID`) REFERENCES `study`.`field_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biospecimen_field_search`
--

DROP TABLE IF EXISTS `biospecimen_field_search`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biospecimen_field_search` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BIOSPECIMEN_FIELD_ID` int(11) DEFAULT NULL,
  `SEARCH_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uq_bsfs_df_s` (`BIOSPECIMEN_FIELD_ID`,`SEARCH_ID`),
  KEY `fk_bsfs_biospecimen_field` (`BIOSPECIMEN_FIELD_ID`),
  KEY `fk_bsfs_search` (`SEARCH_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=211 DEFAULT CHARSET=latin1 COMMENT='many2many join biospecimen_field and search';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `custom_field_display_search`
--

DROP TABLE IF EXISTS `custom_field_display_search`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_field_display_search` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CUSTOM_FIELD_DISPLAY_ID` int(11) DEFAULT NULL,
  `SEARCH_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uq_cfds_cfd_search` (`CUSTOM_FIELD_DISPLAY_ID`,`SEARCH_ID`),
  KEY `fk_cfds_custom_field_display` (`CUSTOM_FIELD_DISPLAY_ID`),
  KEY `fk_cfds_search` (`SEARCH_ID`),
  CONSTRAINT `fk_cfds_custom_field_display` FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`) REFERENCES `study`.`custom_field_display` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_cfds_search` FOREIGN KEY (`SEARCH_ID`) REFERENCES `search` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=298 DEFAULT CHARSET=latin1 COMMENT='many2many join custom_field_display and search';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `demographic_field`
--

DROP TABLE IF EXISTS `demographic_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `demographic_field` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ENTITY` varchar(255) DEFAULT NULL,
  `FIELD_NAME` varchar(255) DEFAULT NULL,
  `PUBLIC_FIELD_NAME` varchar(255) DEFAULT NULL,
  `FIELD_TYPE_ID` int(11) DEFAULT NULL,
  `FILTERABLE` tinyint(4) DEFAULT '1',
  PRIMARY KEY (`ID`),
  KEY `fk_demographic_field_field_type` (`FIELD_TYPE_ID`),
  CONSTRAINT `fk_demographic_field_field_type` FOREIGN KEY (`FIELD_TYPE_ID`) REFERENCES `study`.`field_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `demographic_field_search`
--

DROP TABLE IF EXISTS `demographic_field_search`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `demographic_field_search` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DEMOGRAPHIC_FIELD_ID` int(11) DEFAULT NULL,
  `SEARCH_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uq_dfs_df_s` (`DEMOGRAPHIC_FIELD_ID`,`SEARCH_ID`),
  KEY `fk_dfs_demographic_field` (`DEMOGRAPHIC_FIELD_ID`),
  KEY `fk_dfs_search` (`SEARCH_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=435 DEFAULT CHARSET=latin1 COMMENT='many2many join demographic_field and search';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `query_filter`
--

DROP TABLE IF EXISTS `query_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `query_filter` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DEMOGRAPHIC_FIELD_ID` int(11) DEFAULT NULL,
  `CUSTOM_FIELD_DISPLAY_ID` int(11) DEFAULT NULL,
  `VALUE` varchar(512) DEFAULT NULL,
  `SECOND_VALUE` varchar(512) DEFAULT NULL,
  `OPERATOR` varchar(256) DEFAULT NULL,
  `PREFIX` varchar(56) DEFAULT NULL,
  `BIOSPECIMEN_FIELD_ID` int(11) DEFAULT NULL,
  `BIOCOLLECTION_FIELD_ID` int(11) DEFAULT NULL,
  `SEARCH_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_qf_df_idx` (`DEMOGRAPHIC_FIELD_ID`),
  KEY `fk_qf_cfd_idx` (`CUSTOM_FIELD_DISPLAY_ID`),
  KEY `fk_qf_bsf_idx` (`BIOSPECIMEN_FIELD_ID`),
  KEY `fk_qf_bcf_idx` (`BIOCOLLECTION_FIELD_ID`),
  KEY `fk_query_filter_1_idx` (`SEARCH_ID`),
  CONSTRAINT `fk_qf_bcf` FOREIGN KEY (`BIOCOLLECTION_FIELD_ID`) REFERENCES `biocollection_field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_qf_bsf` FOREIGN KEY (`BIOSPECIMEN_FIELD_ID`) REFERENCES `biospecimen_field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_qf_cfd` FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`) REFERENCES `study`.`custom_field_display` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_qf_df` FOREIGN KEY (`DEMOGRAPHIC_FIELD_ID`) REFERENCES `demographic_field` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_qf_search` FOREIGN KEY (`SEARCH_ID`) REFERENCES `search` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `query_filter_grouping`
--

DROP TABLE IF EXISTS `query_filter_grouping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `query_filter_grouping` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PARENT_GROUPING_ID` int(11) NOT NULL,
  `LEFT_FILTER_ID` int(11) NOT NULL,
  `JOIN_TO_NEXT_FILTER` varchar(56) DEFAULT NULL,
  `PRECEDENCE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_qfg_qg_idx` (`PARENT_GROUPING_ID`),
  KEY `fk_qfg_qf_idx` (`LEFT_FILTER_ID`),
  CONSTRAINT `fk_qfg_qf` FOREIGN KEY (`LEFT_FILTER_ID`) REFERENCES `query_filter` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_qfg_qg` FOREIGN KEY (`PARENT_GROUPING_ID`) REFERENCES `query_grouping` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `query_grouping`
--

DROP TABLE IF EXISTS `query_grouping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `query_grouping` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `query_grouping_grouping`
--

DROP TABLE IF EXISTS `query_grouping_grouping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `query_grouping_grouping` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PARENT_GROUPING_ID` int(11) NOT NULL,
  `LEFT_GROUPING_ID` int(11) NOT NULL,
  `JOIN_TO_NEXT_FILTER` varchar(56) DEFAULT NULL,
  `PRECEDENCE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_qgg_parent_grouping_idx` (`PARENT_GROUPING_ID`),
  KEY `fk_qgg_left_grouping_idx` (`LEFT_GROUPING_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_output_format`
--

DROP TABLE IF EXISTS `report_output_format`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_output_format` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_template`
--

DROP TABLE IF EXISTS `report_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_template` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(1024) DEFAULT NULL,
  `TEMPLATE_PATH` varchar(255) NOT NULL,
  `MODULE_ID` int(11) DEFAULT NULL,
  `FUNCTION_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`),
  KEY `FK_REPORTTEMPLATE_ARKMODULE` (`MODULE_ID`),
  KEY `FK_REPORTTEMPLATE_ARKFUNCTION` (`FUNCTION_ID`),
  CONSTRAINT `FK_REPORTTEMPLATE_ARKFUNCTION` FOREIGN KEY (`FUNCTION_ID`) REFERENCES `study`.`ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_REPORTTEMPLATE_ARKMODULE` FOREIGN KEY (`MODULE_ID`) REFERENCES `study`.`ark_module` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `search`
--

DROP TABLE IF EXISTS `search`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `search` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `TOP_LEVEL_GROUPING_ID` int(11) DEFAULT NULL,
  `STUDY_ID` int(11) DEFAULT NULL,
  `STATUS` varchar(45) DEFAULT NULL,
  `STARTTIME` datetime DEFAULT NULL,
  `FINISHTIME` datetime DEFAULT NULL,
  `INCLUDE_GENO` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `fk_search_query_grouping` (`TOP_LEVEL_GROUPING_ID`),
  KEY `fk_search_study` (`STUDY_ID`),
  CONSTRAINT `fk_search_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `search_payload`
--

DROP TABLE IF EXISTS `search_payload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `search_payload` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PAYLOAD` longblob NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3023 DEFAULT CHARSET=latin1 COMMENT='This is a simple table for storing LOBs';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `search_result`
--

DROP TABLE IF EXISTS `search_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `search_result` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SEARCH_ID` int(11) NOT NULL,
  `FILE_FORMAT_ID` int(11) NOT NULL,
  `DELIMITER_TYPE_ID` int(11) NOT NULL,
  `FILENAME` text NOT NULL,
  `CHECKSUM` varchar(50) DEFAULT NULL,
  `USER_ID` varchar(50) DEFAULT NULL,
  `START_TIME` datetime NOT NULL,
  `FINISH_TIME` datetime DEFAULT NULL,
  `SEARCH_PAYLOAD_ID` int(11) NOT NULL,
  `STATUS_ID` int(11) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `ID` (`ID`),
  KEY `fk_upload_payload` (`SEARCH_PAYLOAD_ID`),
  KEY `fk_search_result_search` (`SEARCH_ID`),
  CONSTRAINT `fk_search_result_search` FOREIGN KEY (`SEARCH_ID`) REFERENCES `search` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_upload_payload` FOREIGN KEY (`SEARCH_PAYLOAD_ID`) REFERENCES `search_payload` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3019 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `search_subject`
--

DROP TABLE IF EXISTS `search_subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `search_subject` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SEARCH_ID` int(11) NOT NULL,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_search_subject_1_idx` (`SEARCH_ID`),
  KEY `fk_search_subject_2` (`LINK_SUBJECT_STUDY_ID`),
  CONSTRAINT `fk_search_subject_1` FOREIGN KEY (`SEARCH_ID`) REFERENCES `search` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_search_subject_2` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `study`.`link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-12-12 11:03:20
CREATE DATABASE  IF NOT EXISTS `pheno` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `pheno`;
-- MySQL dump 10.13  Distrib 5.5.32, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: pheno
-- ------------------------------------------------------
-- Server version	5.5.32-0ubuntu0.12.04.1

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
-- Table structure for table `pheno_data`
--

DROP TABLE IF EXISTS `pheno_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pheno_data` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CUSTOM_FIELD_DISPLAY_ID` int(11) DEFAULT NULL,
  `PHENO_COLLECTION_ID` int(11) DEFAULT NULL,
  `DATE_DATA_VALUE` date DEFAULT NULL,
  `NUMBER_DATA_VALUE` double DEFAULT NULL,
  `TEXT_DATA_VALUE` text,
  `ERROR_DATA_VALUE` text,
  PRIMARY KEY (`ID`),
  KEY `FK_PHENO_DATA_CFD_ID` (`CUSTOM_FIELD_DISPLAY_ID`),
  KEY `FK_PHENO_DATA_PHENO_COLLECTION_ID` (`PHENO_COLLECTION_ID`),
  CONSTRAINT `FK_PHENO_DATA_CFD_ID` FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`) REFERENCES `study`.`custom_field_display` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_PHENO_DATA_PHENO_COLLECTION_ID` FOREIGN KEY (`PHENO_COLLECTION_ID`) REFERENCES `pheno_collection` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=846603 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pheno_collection`
--

DROP TABLE IF EXISTS `pheno_collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pheno_collection` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LINK_SUBJECT_STUDY_ID` int(11) NOT NULL,
  `QUESTIONNAIRE_STATUS_ID` int(11) NOT NULL,
  `RECORD_DATE` datetime NOT NULL,
  `CUSTOM_FIELD_GROUP_ID` int(11) NOT NULL,
  `REVIEWED_DATE` date DEFAULT NULL,
  `REVIEWED_BY_ID` int(11) DEFAULT NULL,
  `DESCRIPTION` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_PHENO_COLLECTION_LINK_SUBJECT_STUDY_ID` (`LINK_SUBJECT_STUDY_ID`),
  KEY `FK_PHENO_QUESTIONNAIRE_STATUS_ID` (`QUESTIONNAIRE_STATUS_ID`),
  KEY `FK_PHENO_CUSTOM_FIELD_GROUP_ID` (`CUSTOM_FIELD_GROUP_ID`),
  KEY `FK_REVIEWED_BY_ARK_USER_ID` (`REVIEWED_BY_ID`),
  CONSTRAINT `FK_PHENO_COLLECTION_LINK_SUBJECT_STUDY_ID` FOREIGN KEY (`LINK_SUBJECT_STUDY_ID`) REFERENCES `study`.`link_subject_study` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_PHENO_CUSTOM_FIELD_GROUP_ID` FOREIGN KEY (`CUSTOM_FIELD_GROUP_ID`) REFERENCES `study`.`custom_field_group` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_PHENO_QUESTIONNAIRE_STATUS_ID` FOREIGN KEY (`QUESTIONNAIRE_STATUS_ID`) REFERENCES `questionnaire_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_REVIEWED_BY_ARK_USER_ID` FOREIGN KEY (`REVIEWED_BY_ID`) REFERENCES `study`.`ark_user` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10153 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-12-12 11:03:20
