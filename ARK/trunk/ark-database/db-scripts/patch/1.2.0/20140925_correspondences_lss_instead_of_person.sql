ALTER TABLE study.correspondences DROP FOREIGN KEY correspondences_person_id ;
	
ALTER TABLE study.correspondences
CHANGE COLUMN PERSON_ID PERSON_ID INT(11) NULL ,
	ADD CONSTRAINT correspondence_person_id FOREIGN KEY (PERSON_ID)
	REFERENCES study.person (ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION;


ALTER TABLE `study`.`correspondences` ADD COLUMN `LINK_SUBJECT_STUDY_ID` INT(11) NULL  AFTER `BILLABLE_ITEM_ID` ;


UPDATE study.correspondences a
SET LINK_SUBJECT_STUDY_ID = 
	(select id from study.LINK_SUBJECT_STUDY lss
	where lss.person_id = a.person_id
	and lss.study_id = a.study_id)
where study_id is not null
and person_id is not null;

/*after thorough testing remove person_id column*/

