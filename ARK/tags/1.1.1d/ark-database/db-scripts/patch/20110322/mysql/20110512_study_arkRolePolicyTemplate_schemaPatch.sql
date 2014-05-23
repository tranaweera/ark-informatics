USE study;

DROP TABLE IF EXISTS `ark_role_policy_template`;
CREATE  TABLE `study`.`ark_role_policy_template` (
  `ID` INT NOT NULL AUTO_INCREMENT ,
  `ARK_ROLE_ID` INT NOT NULL ,
  `ARK_MODULE_ID` INT NOT NULL ,
  `ARK_FUNCTION_ID` INT NOT NULL ,
  `ARK_PERMISSION_ID` INT NOT NULL ,
  PRIMARY KEY (`ID`) ,
  INDEX `FK_ROLE_TMPLT_ARK_ROLE_ID` (`ARK_ROLE_ID` ASC) ,
  INDEX `FK_ROLE_TMPLT_ARK_MODULE_ID` (`ARK_MODULE_ID` ASC) ,
  INDEX `FK_ROLE_TMPLT_ARK_FUNCTION_ID` (`ARK_FUNCTION_ID` ASC) ,
  INDEX `FK_ROLE_TMPLT_ARK_PRMSN_ID` (`ARK_PERMISSION_ID` ASC) ,
  CONSTRAINT `FK_ROLE_TMPLT_ARK_ROLE_ID`
    FOREIGN KEY (`ARK_ROLE_ID` )
    REFERENCES `study`.`ark_role` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ROLE_TMPLT_ARK_MODULE_ID`
    FOREIGN KEY (`ARK_MODULE_ID` )
    REFERENCES `study`.`ark_module` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ROLE_TMPLT_ARK_FUNCTION_ID`
    FOREIGN KEY (`ARK_FUNCTION_ID` )
    REFERENCES `study`.`ark_function` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ROLE_TMPLT_ARK_PRMSN_ID`
    FOREIGN KEY (`ARK_PERMISSION_ID` )
    REFERENCES `study`.`ark_permission` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
