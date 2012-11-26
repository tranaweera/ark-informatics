USE `study`;

DELETE FROM `upload_type`; 

INSERT INTO `upload_type` (`NAME`,`DESCRIPTION`,`ARK_MODULE_ID`) VALUES 
('Biospecimen Custom Data','Custom Data to be associated with a biospecimen',(select id from ark_module where name = 'LIMS')),
('Subject Demographic Data',NULL,(select id from ark_module where name = 'Subject')),
('Study-specific (custom) Data',NULL,(select id from ark_module where name = 'Subject')),
('Custom Data Sets','Custom Data Sets formerly known as Phenotypic Custom Data',(select id from ark_module where name = 'Datasets')),
('Biocollection Custom Data','Custom Data to be associated with a biospecimen',(select id from ark_module where name = 'LIMS')),
('Subject Consent Data',NULL,(select id from ark_module where name = 'Subject'));
