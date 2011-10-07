USE study;

ALTER TABLE `study`.`ark_user_role` DROP FOREIGN KEY `FK_ARK_USECASE_ID` ;
ALTER TABLE `study`.`ark_user_role` DROP COLUMN `ARK_USECASE_ID` , ADD COLUMN `STUDY_ID` INT NULL  AFTER `ARK_MODULE_ID` , 
  ADD CONSTRAINT `FK_ARK_USER_ROLE_STUDY_ID`
  FOREIGN KEY (`STUDY_ID` )
  REFERENCES `study`.`study` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `FK_ARK_USER_ROLE_STUDY_ID` (`STUDY_ID` ASC) 
, DROP INDEX `FK_ARK_USECASE_ID` ;
