ALTER TABLE `study`.`link_subject_study` DROP FOREIGN KEY `FK_LINK_SUBJECT_STUDY_STATE` , DROP FOREIGN KEY `FK_LINK_SUBJECT_STUDY_COUNTRY` ;
ALTER TABLE `study`.`link_subject_study` DROP COLUMN `OTHER_STATE` , DROP COLUMN `STATE_ID` , DROP COLUMN `COUNTRY_ID` , DROP COLUMN `SITE_POST` , DROP COLUMN `SITE_CITY` , DROP COLUMN `SITE_STREET_ADDRESS` , DROP COLUMN `TOTAL_MAMOGRAMS` , DROP COLUMN `YEAR_OF_RECENT_MAMOGRAM` , DROP COLUMN `YEAR_OF_FIRST_MAMOGRAM` , DROP COLUMN `SEND_NEWS_LETTER` , DROP COLUMN `STUDY_APPROACH_DATE` , DROP COLUMN `AMDRF_ID` 
, DROP INDEX `FK_LINK_SUBJECT_STUDY_COUNTRY` 
, DROP INDEX `FK_LINK_SUBJECT_STUDY_STATE` ;

