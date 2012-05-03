
CREATE  TABLE `lims`.`biocollectionuid_template` (
  `ID` INT NOT NULL AUTO_INCREMENT ,
  `STUDY_ID` INT NOT NULL ,
  `BIOCOLLECTIONUID_PREFIX` VARCHAR(45) NULL ,
  `BIOCOLLECTIONUID_PADCHAR_ID` INT NULL ,
  `BIOCOLLECTIONUID_TOKEN_ID` INT NULL ,
  PRIMARY KEY (`ID`) ,
  UNIQUE INDEX `STUDY_ID_UNIQUE` (`STUDY_ID` ASC) )
ENGINE = InnoDB;

INSERT INTO `biocollectionuid_token` (`ID`, `NAME`) VALUES (1,'-'),(2,'@'),(3,'#'),(4,':'),(5,'*'),(6,'|'),(7,'_'),(8,'+');


