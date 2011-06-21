use study;
CREATE  TABLE `study`.`ark_module_function` (
  `ID` INT NOT NULL AUTO_INCREMENT ,
  `ARK_MODULE_ID` INT NOT NULL ,
  `ARK_FUNCTION_ID` INT NOT NULL ,
  PRIMARY KEY (`ID`) ,
  INDEX `FK_ARK_MODULE_FUNCTION_ARK_MODULE_ID` (`ARK_MODULE_ID` ASC) ,
  INDEX `FK_ARK_MODULE_FUNCTION_ARK_FUNCTION_ID` (`ARK_FUNCTION_ID` ASC) ,
  CONSTRAINT `FK_ARK_MODULE_FUNCTION_ARK_MODULE_ID`
    FOREIGN KEY (`ARK_MODULE_ID` )
    REFERENCES `study`.`ark_module` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ARK_MODULE_FUNCTION_ARK_FUNCTION_ID`
    FOREIGN KEY (`ARK_FUNCTION_ID` )
    REFERENCES `study`.`ark_function` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

/* Maps Study Module to Study,User Management, Study Component and My Detail Ark Functions */
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`) VALUES (1, 1, 1);
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`) VALUES (2, 1, 2);
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`) VALUES (3, 1, 3);
INSERT INTO `study`.`ark_module_function` (`ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`) VALUES (4, 1, 4);
