SET @STUDY_GROUP_NAME = 'Vitamin A Collection Details'; -- 'WAFSS BioData Details';  -- however there could be more than one of these perhaps?
SET @STUDYKEY = 22;
SET @STUDYNAME= 'Vitamin A';


select * from study.study;

/* still need to test this






SET @STUDY_GROUP_NAME = 'IRD';
SET @STUDYKEY = 18;
SET @STUDYNAME= 'IRD';





*/







/* *
*
this statement may be better off goiing straight into biocollection.hospital field 
*
INSERT INTO `study`.`custom_field`
(`NAME`,
`DESCRIPTION`,
`FIELD_TYPE_ID`,
`STUDY_ID`,
`ARK_FUNCTION_ID`,
`UNIT_TYPE_ID`,
`MIN_VALUE`,
`MAX_VALUE`,
`ENCODED_VALUES`,
`MISSING_VALUE`,
`HAS_DATA`,
`CUSTOM_FIELD_LABEL`)
SELECT 'HOSPITAL' AS NAME, 'NOTE: Encoded values mapped from sortorder in old data' as DESCRIPTION, 
(SELECT ID FROM study.field_type WHERE name = 'CHARACTER') AS FIELD_TYPE,
@STUDYKEY AS STUDY_ID, (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION') AS ARK_FUNCTION_ID, NULL AS UNITS_TYPE_ID, NULL MIN_VALUE, NULL MAX_VALUE,
(SELECT (CONCAT(GROUP_CONCAT(CONCAT(FLOOR(SORTORDER),'=',TRIM(VALUE)) ORDER BY SORTORDER SEPARATOR ';'),';'))) AS ENCODED_VALUES,
NULL AS MISSING_VALUE,
0 AS HAS_DATA,
'Hospital' AS CUSTOM_FIELD_LABEL
FROM wagerlab.IX_LISTOFVALUES 
WHERE TYPE ='HOSPITAL' AND DELETED =0 AND STUDYKEY IN (0, @STUDYKEY) AND VALUE IS NOT NULL;

-- this could go straight into biocollection.ref_doctor field that seems to be in lims.biocollectuion.  will perform better  - might loose capablility of dropdownish kind of things
-- Insert HOSPITAL and REF_DOCTOR as custom biocollection fields
INSERT INTO `study`.`custom_field`
(`NAME`,
`DESCRIPTION`,
`FIELD_TYPE_ID`,
`STUDY_ID`,
`ARK_FUNCTION_ID`,
`UNIT_TYPE_ID`,
`MIN_VALUE`,
`MAX_VALUE`,
`ENCODED_VALUES`,
`MISSING_VALUE`,
`HAS_DATA`,
`CUSTOM_FIELD_LABEL`)
SELECT 'SURGEON' AS NAME, 'NOTE: Encoded values mapped from sortorder in old data' as DESCRIPTION, 
(SELECT ID FROM study.field_type WHERE name = 'CHARACTER') AS FIELD_TYPE,
@STUDYKEY AS STUDY_ID, (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION') AS ARK_FUNCTION_ID, NULL AS UNITS_TYPE_ID, NULL MIN_VALUE, NULL MAX_VALUE,
(SELECT (CONCAT(GROUP_CONCAT(CONCAT(FLOOR(SORTORDER),'=',TRIM(VALUE)) ORDER BY SORTORDER SEPARATOR ';'),';'))) AS ENCODED_VALUES,
NULL AS MISSING_VALUE,
0 AS HAS_DATA,
'Surgeon' AS CUSTOM_FIELD_LABEL
FROM wagerlab.IX_LISTOFVALUES 
WHERE TYPE ='REF_DOCTOR' AND DELETED =0 AND STUDYKEY IN (0, @STUDYKEY) AND VALUE IS NOT NULL;

-- Insert Custom field display (BioCollection)
INSERT INTO `study`.`custom_field_display`
(`CUSTOM_FIELD_ID`,
`CUSTOM_FIELD_GROUP_ID`,
`SEQUENCE`,
`REQUIRED`,
`REQUIRED_MESSAGE`)
SELECT ID, NULL, ID, 0 AS REQUIRED, NULL
FROM study.custom_field
WHERE study_id = @STUDYKEY
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION')
AND name IN ('HOSPITAL', 'SURGEON');


-- HOSPITAL and SURGEON data from IX_ADMISSIONS
INSERT INTO `lims`.`biocollection_custom_field_data`
(`BIO_COLLECTION_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bc.id AS BIO_COLLECTION_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, SUBSTRING_INDEX(TRIM(TRAILING  SUBSTRING(cf.encoded_values, INSTR(cf.ENCODED_VALUES, concat('=', bc.HOSPITAL, ';'))) FROM cf.ENCODED_VALUES), ';', -1) AS TEXT_DATA_VALUE, NULL AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM study.custom_field cf, study.custom_field_display cfd, lims.biocollection bc
WHERE cf.id = cfd.custom_field_id
AND cf.NAME = 'HOSPITAL'
AND cf.study_id IN (SELECT id FROM study.study WHERE parent_id = @STUDYKEY)
AND bc.study_id IN (SELECT id FROM study.study WHERE parent_id = @STUDYKEY)
AND bc.HOSPITAL IS NOT NULL;



INSERT INTO `lims`.`biocollection_custom_field_data`
(`BIO_COLLECTION_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bc.id AS BIO_COLLECTION_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, SUBSTRING_INDEX(TRIM(TRAILING  SUBSTRING(cf.encoded_values, INSTR(cf.ENCODED_VALUES, concat('=', bc.REF_DOCTOR, ';'))) FROM cf.ENCODED_VALUES), ';', -1) AS TEXT_DATA_VALUE, NULL AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM study.custom_field cf, study.custom_field_display cfd, lims.biocollection bc
WHERE cf.id = cfd.custom_field_id
AND cf.NAME = 'SURGEON'
AND cf.study_id IN (SELECT id FROM study.study WHERE parent_id = @STUDYKEY)
AND bc.study_id IN (SELECT id FROM study.study WHERE parent_id = @STUDYKEY)
AND bc.REF_DOCTOR IS NOT NULL;

*/



-- Insert All BioCollection Custom fields
INSERT INTO `study`.`custom_field`
(`NAME`,
`DESCRIPTION`,
`FIELD_TYPE_ID`,
`STUDY_ID`,
`ARK_FUNCTION_ID`,
`UNIT_TYPE_ID`,
`MIN_VALUE`,
`MAX_VALUE`,
`ENCODED_VALUES`,
`MISSING_VALUE`,
`HAS_DATA`,
`CUSTOM_FIELD_LABEL`)
SELECT bf.COLUMNNAME AS NAME, IF(bf.LOVTYPE IS NOT NULL, 'NOTE: Encoded values mapped from sortorder in old data', null) as DESCRIPTION, 
(SELECT ID FROM study.field_type WHERE name = IF(bft.TYPENAME = 'string', 'CHARACTER', IF(bft.TYPENAME = 'number', 'NUMBER', 'DATE'))) AS FIELD_TYPE,
@STUDYKEY AS STUDY_ID, (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION') AS ARK_FUNCTION_ID, NULL AS UNITS_TYPE_ID, NULL MIN_VALUE, NULL MAX_VALUE,
(
SELECT CONCAT(GROUP_CONCAT(CONCAT(FLOOR(SORTORDER),'=',TRIM(VALUE)) ORDER BY SORTORDER SEPARATOR ';'),';') AS ENCODED_VALUES
FROM wagerlab.IX_LISTOFVALUES 
WHERE TYPE = bf.LOVTYPE
GROUP BY TYPE
) AS ENCODED_VALUES,
NULL AS MISSING_VALUE,
0 AS HAS_DATA,
bf.FIELDNAME AS CUSTOM_FIELD_LABEL
FROM wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, wagerlab.IX_BIODATA_GROUP bg
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME = @STUDY_GROUP_NAME 									-- like 'WARTN%'
AND bg.DOMAIN = 'ADMISSIONS'
ORDER BY bfg.POSITION;

-- Insert  All Custom field display (for BioCollection)
INSERT INTO `study`.`custom_field_display`
(`CUSTOM_FIELD_ID`,
`CUSTOM_FIELD_GROUP_ID`,
`SEQUENCE`,
`REQUIRED`,
`REQUIRED_MESSAGE`)
SELECT ID, NULL, ID, 0 AS REQUIRED, NULL
FROM study.custom_field
WHERE study_id = @STUDYKEY
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION');


select * from lims.biocollection_custom_field_data
where custom_Field_display_id in(

select id from study.custom_field_display where custom_field_id in 
(
select id from study.custom_field where study_id = 22
)
);



select * from study.custom_field_display where custom_field_id in 
(
select id from study.custom_field where study_id = 22
);


select * from study.custom_field where study_id = 22


-- inset actual biocollection custopm field data itself now
-- Normal text/character data
INSERT INTO `lims`.`biocollection_custom_field_data`
(`BIO_COLLECTION_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bc.id AS BIO_COLLECTION_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, bd.STRING_VALUE AS TEXT_DATA_VALUE, NULL AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM wagerlab.IX_BIODATA bd, wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, wagerlab.IX_BIODATA_GROUP bg, wagerlab.IX_ADMISSIONS adm,study.custom_field cf, study.custom_field_display cfd,
lims.biocollection bc
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME = @STUDY_GROUP_NAME -- like 'WARTN%'
-- AND bg.DOMAIN = 'ADMISSIONS'
AND bd.FIELDKEY = bf.FIELDKEY
AND bd.DOMAINKEY = adm.ADMISSIONKEY
AND adm.DELETED = 0
AND cf.study_id = @STUDYKEY
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION')
AND cf.id = cfd.custom_field_id
AND cf.NAME = bf.COLUMNNAME
AND bc.BIOCOLLECTION_UID = adm.ADMISSIONID
AND bc.STUDY_ID = adm.COLLECTIONGROUPKEY
AND bf.LOVTYPE IS NULL
AND STRING_VALUE IS NOT NULL;

select * from study.study where id = @STUDYKEY

select * from  wagerlab.IX_BIODATA bd, wagerlab.ix_biodata_group bg where group_name = @STUDY_GROUP_NAME

-- Drop-down data as migrated into encoded values
INSERT INTO `lims`.`biocollection_custom_field_data`
(`BIO_COLLECTION_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bc.id AS BIO_COLLECTION_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, SUBSTRING_INDEX(TRIM(TRAILING  SUBSTRING(cf.encoded_values, INSTR(cf.ENCODED_VALUES, concat('=', bd.STRING_VALUE, ';'))) FROM cf.ENCODED_VALUES), ';', -1) AS TEXT_DATA_VALUE, NULL AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM wagerlab.IX_BIODATA bd, wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, wagerlab.IX_BIODATA_GROUP bg, wagerlab.IX_ADMISSIONS adm,study.custom_field cf, study.custom_field_display cfd,
lims.biocollection bc
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME = @STUDY_GROUP_NAME -- like 'WARTN%'
AND bg.DOMAIN = 'ADMISSIONS'
AND bd.FIELDKEY = bf.FIELDKEY
AND bd.DOMAINKEY = adm.ADMISSIONKEY
AND adm.DELETED = 0
AND cf.study_id = @STUDYKEY
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION')
AND cf.id = cfd.custom_field_id
AND cf.NAME = bf.COLUMNNAME
AND bc.NAME = adm.ADMISSIONID
AND bc.STUDY_ID = adm.COLLECTIONGROUPKEY
AND bf.LOVTYPE IS NOT NULL
AND STRING_VALUE IS NOT NULL;
 
-- Dates
INSERT INTO `lims`.`biocollection_custom_field_data`
(`BIO_COLLECTION_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bc.id AS BIO_COLLECTION_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, NULL AS TEXT_DATA_VALUE, bd.DATE_VALUE AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM wagerlab.IX_BIODATA bd, wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, wagerlab.IX_BIODATA_GROUP bg, wagerlab.IX_ADMISSIONS adm,study.custom_field cf, study.custom_field_display cfd,
lims.biocollection bc
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME = @STUDY_GROUP_NAME -- like 'WARTN%'
AND bg.DOMAIN = 'ADMISSIONS'
AND bd.FIELDKEY = bf.FIELDKEY
AND bd.DOMAINKEY = adm.ADMISSIONKEY
AND adm.DELETED = 0
AND cf.study_id = @STUDYKEY
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'LIMS_COLLECTION')
AND cf.id = cfd.custom_field_id
AND cf.NAME = bf.COLUMNNAME
AND bc.NAME = adm.ADMISSIONID
AND bc.STUDY_ID = adm.COLLECTIONGROUPKEY
AND DATE_VALUE IS NOT NULL;


/************************************
BIOSPECIMEN CUSTOM
***************************************/

-- Insert Biospecimen Custom fields
INSERT INTO `study`.`custom_field`
(`NAME`,
`DESCRIPTION`,
`FIELD_TYPE_ID`,
`STUDY_ID`,
`ARK_FUNCTION_ID`,
`UNIT_TYPE_ID`,
`MIN_VALUE`,
`MAX_VALUE`,
`ENCODED_VALUES`,
`MISSING_VALUE`,
`HAS_DATA`,
`CUSTOM_FIELD_LABEL`)
SELECT bf.COLUMNNAME AS NAME, IF(bf.LOVTYPE IS NOT NULL, 'NOTE: Encoded values mapped from sortorder in old data', null) as DESCRIPTION, 
(SELECT ID FROM study.field_type WHERE name = IF(bft.TYPENAME = 'string', 'CHARACTER', IF(bft.TYPENAME = 'number', 'NUMBER', 'DATE'))) AS FIELD_TYPE,
@STUDYKEY AS STUDY_ID, (SELECT ID FROM study.ark_function WHERE name = 'BIOSPECIMEN') AS ARK_FUNCTION_ID, NULL AS UNIT_TYPE_ID, NULL MIN_VALUE, NULL MAX_VALUE,
(
SELECT CONCAT(GROUP_CONCAT(CONCAT(FLOOR(SORTORDER),'=',TRIM(VALUE)) ORDER BY SORTORDER SEPARATOR ';'),';') AS ENCODED_VALUES
FROM wagerlab.IX_LISTOFVALUES 
WHERE TYPE = bf.LOVTYPE
GROUP BY TYPE
) AS ENCODED_VALUES,
NULL AS MISSING_VALUE,
0 AS HAS_DATA,
bf.FIELDNAME AS CUSTOM_FIELD_LABEL
FROM wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, wagerlab.IX_BIODATA_GROUP bg
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME = @STUDY_GROUP_NAME -- like (@STUDYNAME || '%')
AND bg.DOMAIN = 'BIOSPECIMEN'
ORDER BY bfg.POSITION;

-- 

select * from lims.biospecimen_custom_field_data
where custom_Field_display_id in(

select id from study.custom_field_display where custom_field_id in 
(
select id from study.custom_field where study_id = 17
)
);

-- Insert Custom field display (Biospecimen)
INSERT INTO `study`.`custom_field_display`
(`CUSTOM_FIELD_ID`,
`CUSTOM_FIELD_GROUP_ID`,
`SEQUENCE`,
`REQUIRED`,
`REQUIRED_MESSAGE`)
SELECT ID, NULL, ID, 0 AS REQUIRED, NULL
FROM study.custom_field
WHERE study_id = @STUDYKEY
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'BIOSPECIMEN');

-- NOW THE ACTUAL BIOSPEC cUSTOM DATA
-- Characters
INSERT INTO `lims`.`biospecimen_custom_field_data`
(`BIOSPECIMEN_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bs.id AS BIOSPECIMEN_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, SUBSTRING_INDEX(TRIM(TRAILING  SUBSTRING(cf.encoded_values, INSTR(cf.ENCODED_VALUES, concat('=', bd.STRING_VALUE, ';'))) FROM cf.ENCODED_VALUES), ';', -1) AS TEXT_DATA_VALUE, NULL AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM 	wagerlab.IX_BIODATA bd, wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, 
		wagerlab.IX_BIODATA_GROUP bg, wagerlab.IX_BIOSPECIMEN bio,study.custom_field cf, study.custom_field_display cfd,
		lims.biospecimen bs
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME = @STUDY_GROUP_NAME -- like (@STUDYNAME || '%')
AND bg.DOMAIN = 'BIOSPECIMEN'
AND bd.FIELDKEY = bf.FIELDKEY
AND bd.DOMAINKEY = bio.BIOSPECIMENKEY
AND bio.DELETED = 0
AND bio.studykey =  @STUDYKEY
AND cf.study_id = @STUDYKEY
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'BIOSPECIMEN')
AND cf.id = cfd.custom_field_id
AND cf.NAME = bf.COLUMNNAME
AND STRING_VALUE IS NOT NULL
AND bf.LOVTYPE IS NOT NULL
AND bs.OLD_ID = bio.BIOSPECIMENKEY;  -- wafss 20,744  --- noooo 0
 
-- Dates
INSERT INTO `lims`.`biospecimen_custom_field_data`
(`BIOSPECIMEN_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bs.id AS BIOSPECIMEN_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, NULL AS TEXT_DATA_VALUE, bd.DATE_VALUE AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM 	wagerlab.IX_BIODATA bd, wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, 
		wagerlab.IX_BIODATA_GROUP bg, wagerlab.IX_BIOSPECIMEN bio,study.custom_field cf, study.custom_field_display cfd,
		lims.biospecimen bs
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME = @STUDY_GROUP_NAME -- like (@STUDYNAME || '%')
AND bg.DOMAIN = 'BIOSPECIMEN'
AND bd.FIELDKEY = bf.FIELDKEY
AND bd.DOMAINKEY = bio.BIOSPECIMENKEY
AND bio.studykey =  @STUDYKEY
AND bio.DELETED = 0
AND cf.study_id = @STUDYKEY
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'BIOSPECIMEN')
AND cf.id = cfd.custom_field_id
AND cf.NAME = bf.COLUMNNAME
AND DATE_VALUE IS NOT NULL
AND bs.OLD_ID = bio.BIOSPECIMENKEY; -- WAFSS 34,182 - in reality looks like we have =935*5+29*4 (ie 4791) ... ok now fixed by adding link to bio.studykey = @studykey


/** Work in progress  - To be completed  **/
-- Drop-down data as migrated into encoded values
INSERT INTO `lims`.`biospecimen_custom_field_data`
(`BIOSPECIMEN_ID`,
`CUSTOM_FIELD_DISPLAY_ID`,
`TEXT_DATA_VALUE`,
`DATE_DATA_VALUE`,
`NUMBER_DATA_VALUE`,
`ERROR_DATA_VALUE`)
SELECT bs.id AS BIOSPECIMEN_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, 
	SUBSTRING_INDEX(TRIM(TRAILING  SUBSTRING(cf.encoded_values, INSTR(cf.ENCODED_VALUES, concat('=', bd.STRING_VALUE, ';'))) FROM cf.ENCODED_VALUES), ';', -1) AS TEXT_DATA_VALUE, 
	NULL AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM wagerlab.IX_BIODATA bd, wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, 
	wagerlab.IX_BIODATA_GROUP bg, wagerlab.IX_BIOSPECIMEN bio, wagerlab.IX_BIOSPECIMEN ixb,study.custom_field cf, study.custom_field_display cfd, 
	lims.biospecimen bs
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME = @STUDY_GROUP_NAME -- 'WARTN%'
AND bg.DOMAIN = 'BIOSPECIMEN'
AND bd.DOMAINKEY = bio.BIOSPECIMENKEY
AND bio.studykey =  @STUDYKEY
AND bd.FIELDKEY = bf.FIELDKEY
AND bd.DOMAINKEY = ixb.biospecimenkey
AND ixb.DELETED = 0
AND cf.study_id = @STUDYKEY
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'BIOSPECIMEN')
AND cf.id = cfd.custom_field_id
AND cf.NAME = bf.COLUMNNAME
AND bs.biospecimen_uid = ixb.biospecimenid
AND bs.STUDY_ID = ixb.SUBSTUDYKEY
AND bf.LOVTYPE IS NOT NULL
AND STRING_VALUE IS NOT NULL; -- WAFSS 17359 - 72285
 

select * from lims.biocollection where name is not null
and biocollection_uid <> name;




