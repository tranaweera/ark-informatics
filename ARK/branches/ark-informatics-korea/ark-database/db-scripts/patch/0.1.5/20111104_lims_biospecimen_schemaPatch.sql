use lims;
/* Patch from using Navicat. */
ALTER TABLE `biospecimen` MODIFY COLUMN `PROCESSED_DATE`  datetime NULL DEFAULT NULL AFTER `SAMPLE_TIME`;
ALTER TABLE `biospecimen` MODIFY COLUMN `BIOSPECIMEN_GRADE_ID`  int(11) NULL DEFAULT NULL AFTER `DEPTH`;
ALTER TABLE `biospecimen` MODIFY COLUMN `SAMPLESUBTYPE`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `SAMPLETYPE`;


ALTER TABLE `lims`.`barcode_printer` CHANGE COLUMN `PORT` `PORT` INT NOT NULL  ;
