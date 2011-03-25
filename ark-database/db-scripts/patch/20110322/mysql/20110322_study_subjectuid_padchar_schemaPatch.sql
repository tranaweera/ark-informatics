USE study;

-- Cellis added new table [subjectuid_padchar]
CREATE TABLE `subjectuid_padchar` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(25) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1

-- Cellis modified study table to connect to new table [subjectuid_padchar] and neaten it up
ALTER TABLE `study` DROP FOREIGN KEY `study_ibfk_1`;
ALTER TABLE `study` DROP COLUMN `AUTO_GENERATE_SUBJECT_UID`;
ALTER TABLE `study` DROP COLUMN `SUBJECT_UID_START`;

ALTER TABLE `study` ADD COLUMN `AUTO_GENERATE_SUBJECTUID`  int(11) NOT NULL AFTER `CO_INVESTIGATOR`;
ALTER TABLE `study` ADD COLUMN `SUBJECTUID_START`  int(11) NULL DEFAULT NULL AFTER `AUTO_GENERATE_SUBJECTUID`;
ALTER TABLE `study` MODIFY COLUMN `STUDY_STATUS_ID`  int(11) NOT NULL ;
ALTER TABLE `study` ADD COLUMN `SUBJECTUID_PREFIX`  varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `STUDY_STATUS_ID`;
ALTER TABLE `study` MODIFY COLUMN `CONTACT_PERSON`  varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ;
ALTER TABLE `study` ADD COLUMN `SUBJECTUID_TOKEN`  varchar(1) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `FILENAME`;
ALTER TABLE `study` ADD COLUMN `SUBJECTUID_PADCHAR_ID`  int(11) NULL DEFAULT NULL AFTER `SUBJECTUID_TOKEN`;
ALTER TABLE `study` MODIFY COLUMN `SUBJECT_KEY_PREFIX`  varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ;
ALTER TABLE `study` ADD COLUMN `SUBJECT_KEY_START`  varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `SUBJECT_KEY_PREFIX`;
ALTER TABLE `study` ADD CONSTRAINT `fk_study_study_status` FOREIGN KEY (`STUDY_STATUS_ID`) REFERENCES `study_status` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `study` ADD CONSTRAINT `fk_study_subjectuid_padchar` FOREIGN KEY (`SUBJECTUID_PADCHAR_ID`) REFERENCES `subjectuid_padchar` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `study` MODIFY COLUMN `CONTACT_PERSON`  varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `SUBJECTUID_PREFIX`;
ALTER TABLE `study` MODIFY COLUMN `SUBJECT_KEY_PREFIX`  varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `SUBJECTUID_PADCHAR_ID`;
