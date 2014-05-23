CREATE TABLE STUDY.ACTION_TYPE 
(	
ID INT, 
NAME VARCHAR(20), 
DESCRIPTION VARCHAR(255)
);
ALTER TABLE STUDY.ACTION_TYPE ADD CONSTRAINT ACTION_TYPE_PK PRIMARY KEY (ID);

CREATE TABLE STUDY.ADDRESS 
(	
ID INT, 
STREET_ADDRESS VARCHAR(255), 
SUBURB VARCHAR(50), 
POST_CODE VARCHAR(10), 
CITY VARCHAR(30), 
STATE VARCHAR(20), 
COUNTRY VARCHAR(50), 
PERSON_ID INT, 
ADDRESS_STATUS INT, 
ADDRESS_TYPE_ID INT
);
ALTER TABLE STUDY.ADDRESS ADD CONSTRAINT ADDRESS_PK PRIMARY KEY (ID);
ALTER TABLE STUDY.ADDRESS ADD CONSTRAINT ADDRESS_PERSON_FK1 FOREIGN KEY (PERSON_ID) REFERENCES STUDY.PERSON (ID);
ALTER TABLE STUDY.ADDRESS ADD CONSTRAINT ADDRESS_ADDRESS_T_FK1 FOREIGN KEY (ADDRESS_TYPE_ID) REFERENCES STUDY.ADDRESS_TYPE (ID);

CREATE TABLE STUDY.ADDRESS_TYPE 
(	
ID INT, 
NAME VARCHAR(50), 
DESCRIPTION VARCHAR(255)
);
ALTER TABLE STUDY.ADDRESS_TYPE ADD CONSTRAINT ADDRESS_TYPE_PK PRIMARY KEY (ID);

CREATE TABLE STUDY.AUDIT_HISTORY 
(	
ID INT NOT NULL , 
STUDY_STATUS_ID INT, 
DATE_TIME DATE, 
ACTION_TYPE_ID INT, 
ARK_USER_ID VARCHAR(50), 
NOTE VARCHAR(255)
);
ALTER TABLE STUDY.AUDIT_HISTORY ADD CONSTRAINT AUDIT_HISTORY_PK PRIMARY KEY (ID);
ALTER TABLE STUDY.AUDIT_HISTORY ADD CONSTRAINT AUDIT_HISTORY_STU_FK1 FOREIGN KEY (STUDY_STATUS_ID) REFERENCES STUDY.STUDY_STATUS (ID);
ALTER TABLE STUDY.AUDIT_HISTORY ADD CONSTRAINT AUDIT_HISTORY_ACT_FK1 FOREIGN KEY (ACTION_TYPE_ID) REFERENCES STUDY.ACTION_TYPE (ID);

CREATE TABLE STUDY.CORRESPONDENCE 
(	
ID INT, 
DATE_OF_CORRESPONDENCE DATE, 
SUMMARY VARCHAR(255), 
CORRESPONDENCE_TYPE_ID INT
);
ALTER TABLE STUDY.CORRESPONDENCE ADD CONSTRAINT CORRESPONDENCE_PK PRIMARY KEY (ID);
ALTER TABLE STUDY.CORRESPONDENCE ADD CONSTRAINT CORRESPONDENCE_CO_FK1 FOREIGN KEY (CORRESPONDENCE_TYPE_ID) REFERENCES STUDY.CORRESPONDENCE_TYPE (ID);

CREATE TABLE STUDY.CORRESPONDENCE_TYPE 
(	
ID INT NOT NULL , 
NAME VARCHAR(50), 
TYPE_DESCRIPTION VARCHAR(255)
);
ALTER TABLE STUDY.CORRESPONDENCE_TYPE ADD CONSTRAINT CORRESPONDENCE_TYPE_PK PRIMARY KEY (ID);

CREATE TABLE STUDY.DATA_TYPE 
(	
ID INT NOT NULL , 
NAME VARCHAR(255), 
DESCRIPTION VARCHAR(255)
);
ALTER TABLE STUDY.DATA_TYPE ADD CONSTRAINT DATA_TYPE_PK PRIMARY KEY (ID);

CREATE TABLE STUDY.DOCUMENT 
(	
ID INT NOT NULL , 
NAME VARCHAR(100), 
DESCRIPTION VARCHAR(255), 
DOCUMENT_CONTENT BLOB, 
STUDY_COMP_ID INT, 
CORRESPONDENCE_ID INT
);
ALTER TABLE STUDY.DOCUMENT ADD CONSTRAINT DOCUMENT_PK PRIMARY KEY (ID);
ALTER TABLE STUDY.DOCUMENT ADD CONSTRAINT DOCUMENT_STUDY_CO_FK1 FOREIGN KEY (STUDY_COMP_ID) REFERENCES STUDY.STUDY_COMP (ID);
ALTER TABLE STUDY.DOCUMENT ADD CONSTRAINT DOCUMENT_CORRESPO_FK1 FOREIGN KEY (CORRESPONDENCE_ID) REFERENCES STUDY.CORRESPONDENCE (ID);

CREATE TABLE STUDY.DOMAIN 
(	
ID INT NOT NULL , 
NAME VARCHAR(100)
);
ALTER TABLE STUDY.DOMAIN ADD CONSTRAINT DOMAIN_PK PRIMARY KEY (ID);

CREATE TABLE STUDY.DOMAIN_TYPE 
(	
ID INT NOT NULL , 
NAME VARCHAR(20), 
DESCRIPTION VARCHAR(255)
);
ALTER TABLE STUDY.DOMAIN_TYPE ADD CONSTRAINT DOMAIN_TYPE_PK PRIMARY KEY (ID);

CREATE TABLE STUDY.EMAIL_ACCOUNT 
(	
ID INT NOT NULL , 
NAME VARCHAR(255), 
PRIMARY_ACCOUNT INT, 
PERSON_ID INT, 
EMAIL_ACCOUNT_TYPE_ID INT
);
ALTER TABLE STUDY.EMAIL_ACCOUNT ADD CONSTRAINT EMAIL_ACCOUNT_PK PRIMARY KEY (ID);
ALTER TABLE STUDY.EMAIL_ACCOUNT ADD CONSTRAINT EMAIL_ACCOUNT_PER_FK1 FOREIGN KEY (PERSON_ID) REFERENCES STUDY.PERSON (ID);
ALTER TABLE STUDY.EMAIL_ACCOUNT ADD CONSTRAINT EMAIL_ACCOUNT_EMA_FK1 FOREIGN KEY (EMAIL_ACCOUNT_TYPE_ID) REFERENCES STUDY.EMAIL_ACCOUNT_TYPE (ID);

CREATE TABLE STUDY.EMAIL_ACCOUNT_TYPE 
(	
ID INT NOT NULL , 
NAME VARCHAR(20), 
DESCRIPTION VARCHAR(50)
);
ALTER TABLE STUDY.EMAIL_ACCOUNT_TYPE ADD CONSTRAINT EMAIL_ACCOUNT_TYPE_PK PRIMARY KEY (ID);

CREATE TABLE STUDY.LINK_SITE_CONTACT 
(	
ID INT NOT NULL , 
PERSON_ID INT, 
STUDY_SITE_ID INT
);
ALTER TABLE STUDY.LINK_SITE_CONTACT ADD CONSTRAINT LINK_SITE_CONTACT_PK PRIMARY KEY (ID);
ALTER TABLE STUDY.LINK_SITE_CONTACT ADD CONSTRAINT LINK_SITE_CONTACT_FK1 FOREIGN KEY (PERSON_ID) REFERENCES STUDY.PERSON (ID);
ALTER TABLE STUDY.LINK_SITE_CONTACT ADD CONSTRAINT LINK_SITE_CONTACT_FK2 FOREIGN KEY (STUDY_SITE_ID) REFERENCES STUDY.STUDY_SITE (ID);

CREATE TABLE STUDY.LINK_STUDY_STUDYCOMP 
(	
ID INT NOT NULL , 
STUDY_COMP_ID INT, 
STUDY_ID INT, 
STUDY_COMP_STATUS_ID INT
);
ALTER TABLE STUDY.LINK_STUDY_STUDYCOMP ADD CONSTRAINT LINK_STUDY_STUDYCOMP_PK PRIMARY KEY (ID);
ALTER TABLE STUDY.LINK_STUDY_STUDYCOMP ADD CONSTRAINT LINK_STUDY_STUDYCOMP_FK1 FOREIGN KEY (STUDY_ID) REFERENCES STUDY.STUDY (ID);
ALTER TABLE STUDY.LINK_STUDY_STUDYCOMP ADD CONSTRAINT LINK_STUDY_STUDYCOMP_FK2 FOREIGN KEY (STUDY_COMP_ID) REFERENCES STUDY.STUDY_COMP (ID);
ALTER TABLE STUDY.LINK_STUDY_STUDYCOMP ADD CONSTRAINT LINK_STUDY_STUDYCOMP_FK3 FOREIGN KEY (STUDY_COMP_STATUS_ID) REFERENCES STUDY.STUDY_COMP_STATUS (ID);

CREATE TABLE STUDY.LINK_STUDY_STUDYSITE 
(	
ID INT NOT NULL , 
STUDY_SITE_ID INT, 
STUDY_ID INT
);
ALTER TABLE STUDY.LINK_STUDY_STUDYSITE ADD CONSTRAINT LINK_STUDY_STUDYSITE_PK PRIMARY KEY (ID);
ALTER TABLE STUDY.LINK_STUDY_STUDYSITE ADD CONSTRAINT LINK_STUDY_STUDYSITE_FK1 FOREIGN KEY (STUDY_SITE_ID) REFERENCES STUDY.STUDY_SITE (ID);
ALTER TABLE STUDY.LINK_STUDY_STUDYSITE ADD CONSTRAINT LINK_STUDY_STUDYSITE_FK2 FOREIGN KEY (STUDY_ID) REFERENCES STUDY.STUDY (ID);

CREATE TABLE STUDY.LINK_STUDY_SUBSTUDY 
(	
ID INT NOT NULL , 
STUDY_ID INT, 
SUB_STUDY_ID INT
);
ALTER TABLE STUDY.LINK_STUDY_SUBSTUDY ADD CONSTRAINT LINK_STUDY_SUBSTUDY_PK PRIMARY KEY (ID);
ALTER TABLE STUDY.LINK_STUDY_SUBSTUDY ADD CONSTRAINT LINK_STUDY_SUBSTUDY_S_FK1 FOREIGN KEY (STUDY_ID) REFERENCES STUDY.STUDY (ID);
ALTER TABLE STUDY.LINK_STUDY_SUBSTUDY ADD CONSTRAINT LINK_STUDY_SUBSTUDY_S_FK2 FOREIGN KEY (SUB_STUDY_ID) REFERENCES STUDY.STUDY (ID);

CREATE TABLE STUDY.LINK_SUBJECT_CONTACT 
(	
ID INT NOT NULL , 
PERSON_CONTACT_ID INT, 
PERSON_SUBJECT_ID INT, 
STUDY_ID INT, 
RELATIONSHIP_ID INT
);
ALTER TABLE STUDY.LINK_SUBJECT_CONTACT ADD CONSTRAINT LINK_SUBJECT_CONTACT_PK PRIMARY KEY (ID);
ALTER TABLE STUDY.LINK_SUBJECT_CONTACT ADD CONSTRAINT LINK_SUBJECT_CONTACT_FK1 FOREIGN KEY (PERSON_CONTACT_ID) REFERENCES STUDY.PERSON (ID);
ALTER TABLE STUDY.LINK_SUBJECT_CONTACT ADD CONSTRAINT LINK_SUBJECT_CONTACT_FK2 FOREIGN KEY (PERSON_SUBJECT_ID) REFERENCES STUDY.PERSON (ID);
ALTER TABLE STUDY.LINK_SUBJECT_CONTACT ADD CONSTRAINT LINK_SUBJECT_CONTACT_FK3 FOREIGN KEY (STUDY_ID) REFERENCES STUDY.STUDY (ID);
ALTER TABLE STUDY.LINK_SUBJECT_CONTACT ADD CONSTRAINT LINK_SUBJECT_CONTACT_FK4 FOREIGN KEY (RELATIONSHIP_ID) REFERENCES STUDY.RELATIONSHIP (ID);

CREATE TABLE STUDY.LINK_SUBJECT_STUDY 
(	
ID INT NOT NULL , 
PERSON_ID INT NOT NULL, 
STUDY_ID INT NOT NULL, 
SUBJECT_STATUS_ID INT,
SUBJECT_UID VARCHAR(50) NOT NULL
);
ALTER TABLE STUDY.LINK_SUBJECT_STUDY ADD CONSTRAINT LINK_SUBJECT_STUDY_PK PRIMARY KEY (ID);
ALTER TABLE STUDY.LINK_SUBJECT_STUDY ADD CONSTRAINT LINK_SUBJECT_STUDY_ST_FK1 FOREIGN KEY (PERSON_ID) REFERENCES STUDY.PERSON (ID);
ALTER TABLE STUDY.LINK_SUBJECT_STUDY ADD CONSTRAINT LINK_SUBJECT_STUDY_ST_FK2 FOREIGN KEY (STUDY_ID) REFERENCES STUDY.STUDY (ID);
ALTER TABLE STUDY.LINK_SUBJECT_STUDY ADD CONSTRAINT LINK_SUBJECT_STUDY_ST_FK3 FOREIGN KEY (SUBJECT_STATUS_ID) REFERENCES STUDY.STUDY_STATUS (ID);

CREATE TABLE STUDY.LINK_SUBJECT_STUDYCOMP 
(	
ID INT NOT NULL , 
PERSON_SUBJECT_ID INT, 
STUDY_COMP_ID INT, 
STUDY_ID INT, 
COMP_STATUS_ID INT
);
ALTER TABLE STUDY.LINK_SUBJECT_STUDYCOMP ADD CONSTRAINT LINK_SUBJECT_STUDYCOM_PK PRIMARY KEY (ID);
ALTER TABLE STUDY.LINK_SUBJECT_STUDYCOMP ADD CONSTRAINT LINK_SUBJECT_STUDYCOM_FK1 FOREIGN KEY (PERSON_SUBJECT_ID) REFERENCES STUDY.PERSON (ID);
ALTER TABLE STUDY.LINK_SUBJECT_STUDYCOMP ADD CONSTRAINT LINK_SUBJECT_STUDYCOM_FK2 FOREIGN KEY (STUDY_COMP_ID) REFERENCES STUDY.STUDY_COMP (ID);
ALTER TABLE STUDY.LINK_SUBJECT_STUDYCOMP ADD CONSTRAINT LINK_SUBJECT_STUDYCOM_FK3 FOREIGN KEY (STUDY_ID) REFERENCES STUDY.STUDY (ID);
ALTER TABLE STUDY.LINK_SUBJECT_STUDYCOMP ADD CONSTRAINT LINK_SUBJECT_STUDYCOM_FK4 FOREIGN KEY (COMP_STATUS_ID) REFERENCES STUDY.STUDY_COMP_STATUS (ID);

CREATE TABLE STUDY.MARITAL_STATUS 
(	
ID INT NOT NULL , 
MARTIAL_STATUS VARCHAR(50) NOT NULL , 
DESCRIPTION VARCHAR(255)
);
ALTER TABLE STUDY.MARITAL_STATUS ADD CONSTRAINT MARITAL_STATUS_PK PRIMARY KEY (ID);

CREATE TABLE STUDY.MENUS 
(	
ID INT, 
NAME VARCHAR(50), 
RESOURCEKEY VARCHAR(200)
);

CREATE TABLE STUDY.PERSON 
(	
ID INT, 
FIRST_NAME VARCHAR(50), 
MIDDLE_NAME VARCHAR(50), 
LAST_NAME VARCHAR(50), 
PREFERRED_NAME VARCHAR(150), 
GENDER_TYPE_ID INT, 
DATE_OF_BIRTH DATE, 
DATE_OF_DEATH DATE, 
REGISTRATION_DATE DATE, 
CAUSE_OF_DEATH VARCHAR(255), 
MARITAL_STATUS_ID INT, 
VITAL_STATUS_ID INT, 
TITLE_TYPE_ID INT, 
REGISTRATION_STATUS_ID INT
);
ALTER TABLE STUDY.PERSON ADD CONSTRAINT PERSON_PK PRIMARY KEY (ID);
ALTER TABLE STUDY.PERSON ADD CONSTRAINT PERSON_GENDER_TYPE_FK FOREIGN KEY (GENDER_TYPE_ID) REFERENCES STUDY.GENDER_TYPE (ID);
ALTER TABLE STUDY.PERSON ADD CONSTRAINT PERSON_MARITAL_STATUS_FK FOREIGN KEY (MARITAL_STATUS_ID) REFERENCES STUDY.MARITAL_STATUS (ID);  
ALTER TABLE STUDY.PERSON ADD CONSTRAINT PERSON_VITAL_STATUS_FK FOREIGN KEY (VITAL_STATUS_ID) REFERENCES STUDY.VITAL_STATUS (ID);
ALTER TABLE STUDY.PERSON ADD CONSTRAINT PERSON_TITLE_TYPE_FK FOREIGN KEY (TITLE_TYPE_ID) REFERENCES STUDY.TITLE_TYPE (ID);
ALTER TABLE STUDY.PERSON ADD CONSTRAINT PERSON_REGISTR_STATUS_FK FOREIGN KEY (REGISTRATION_STATUS_ID) REFERENCES STUDY.REGISTRATION_STATUS (ID);

CREATE TABLE STUDY.PHONE 
(	
ID INT NOT NULL , 
PHONE_INT INT, 
PERSON_ID INT, 
PHONE_TYPE_ID INT
);
ALTER TABLE STUDY.PHONE ADD CONSTRAINT PHONE_PK PRIMARY KEY (ID);
ALTER TABLE STUDY.PHONE ADD CONSTRAINT PHONE_PHONE_TYPE_FK1 FOREIGN KEY (PHONE_TYPE_ID) REFERENCES STUDY.PHONE_TYPE (ID);

CREATE TABLE STUDY.PHONE_TYPE 
(	
ID INT, 
NAME VARCHAR(20), 
DESCRIPTION VARCHAR(255)
);
ALTER TABLE STUDY.PHONE_TYPE ADD CONSTRAINT PHONE_TYPE_PK PRIMARY KEY (ID);

CREATE TABLE STUDY.REGISTRATION_STATUS 
(	
ID INT NOT NULL , 
REGISTRATION_STATUS VARCHAR(50), 
DESCRIPTION VARCHAR(255)
);
ALTER TABLE STUDY.REGISTRATION_STATUS ADD CONSTRAINT REGISTRATION_STATUS_PK PRIMARY KEY (ID);

CREATE TABLE STUDY.RELATIONSHIP 
(	
ID INT, 
NAME VARCHAR(20), 
DESCRIPTION VARCHAR(255)
);
ALTER TABLE STUDY.RELATIONSHIP ADD CONSTRAINT RELATIONSHIP_PK PRIMARY KEY (ID);

CREATE TABLE STUDY.STUDY 
(	
ID INT, 
NAME VARCHAR(150), 
DESCRIPTION VARCHAR(255), 
DATE_OF_APPLICATION DATE, 
ESTIMATED_YEAR_OF_COMPLETION INT, 
CHIEF_INVESTIGATOR VARCHAR(50), 
CO_INVESTIGATOR VARCHAR(50), 
AUTO_GENERATE_SUBJECT_KEY INT, 
SUBJECT_KEY_START INT, 
STUDY_STATUS_ID INT, 
SUBJECT_KEY_PREFIX VARCHAR(20), 
CONTACT_PERSON VARCHAR(50), 
CONTACT_PERSON_PHONE VARCHAR(20), 
LDAP_GROUP_NAME VARCHAR(100), 
AUTO_CONSENT INT, 
SUB_STUDY_BIOSPECIMEN_PREFIX VARCHAR(20)
);
ALTER TABLE STUDY.STUDY ADD CONSTRAINT STUDY_PK PRIMARY KEY (ID);
ALTER TABLE STUDY.STUDY ADD CONSTRAINT STUDY_STUDY_STATUS_FK1 FOREIGN KEY (STUDY_STATUS_ID) REFERENCES STUDY.STUDY_STATUS (ID);

CREATE TABLE STUDY.STUDY_COMP 
(	
ID INT, 
NAME VARCHAR(100), 
DESCRIPTION VARCHAR(255), 
STUDY_ID INT
);
ALTER TABLE STUDY.STUDY_COMP ADD CONSTRAINT STUDY_COMP_PK PRIMARY KEY (ID);
ALTER TABLE STUDY.STUDY_COMP ADD CONSTRAINT STUDY_COMP_STUDY_FK1 FOREIGN KEY (STUDY_ID) REFERENCES STUDY.STUDY (ID);

CREATE TABLE STUDY.STUDY_COMP_STATUS 
(	
ID INT, 
NAME VARCHAR(100), 
DESCRIPTION VARCHAR(255)
);
ALTER TABLE STUDY.STUDY_COMP_STATUS ADD CONSTRAINT STUDY_COMP_STATUS_PK PRIMARY KEY (ID);

CREATE TABLE STUDY.STUDY_SITE 
(	
ID INT, 
NAME VARCHAR(255), 
DESCRIPTION VARCHAR(255), 
ADDRESS_ID INT, 
DOMAIN_TYPE_ID INT
);
ALTER TABLE STUDY.STUDY_SITE ADD CONSTRAINT STUDY_SITE_PK PRIMARY KEY (ID);
ALTER TABLE STUDY.STUDY_SITE ADD CONSTRAINT STUDY_SITE_ADDRES_FK1 FOREIGN KEY (ADDRESS_ID) REFERENCES STUDY.ADDRESS (ID);
ALTER TABLE STUDY.STUDY_SITE ADD CONSTRAINT STUDY_SITE_DOMAIN_FK1 FOREIGN KEY (DOMAIN_TYPE_ID) REFERENCES STUDY.DOMAIN_TYPE (ID);

CREATE TABLE STUDY.STUDY_STATUS 
(	
ID INT NOT NULL , 
NAME VARCHAR(25), 
DESCRIPTION VARCHAR(255)
);
ALTER TABLE STUDY.STUDY_STATUS ADD CONSTRAINT STUDY_STATUS_PK PRIMARY KEY (ID);

CREATE TABLE STUDY.SUBJECT_CUST_FLD_DAT 
(	
ID INT NOT NULL , 
FIELD_DATA VARCHAR(4000), 
LINK_SUBJECT_STUDY_ID INT, 
SUBJECT_CUSTM_FLD_ID INT
);
ALTER TABLE STUDY.SUBJECT_CUST_FLD_DAT ADD CONSTRAINT SUBJECT_CUST_FLD_DAT_PK PRIMARY KEY (ID);
ALTER TABLE STUDY.SUBJECT_CUST_FLD_DAT ADD CONSTRAINT SUBJECT_CUST_FLD_DAT_FK1 FOREIGN KEY (LINK_SUBJECT_STUDY_ID) REFERENCES STUDY.LINK_SUBJECT_STUDY (ID);
ALTER TABLE STUDY.SUBJECT_CUST_FLD_DAT ADD CONSTRAINT SUBJECT_CUST_FLD_DAT_FK2 FOREIGN KEY (SUBJECT_CUSTM_FLD_ID) REFERENCES STUDY.SUBJECT_CUSTM_FLD (ID);

CREATE TABLE STUDY.SUBJECT_CUSTM_FLD 
(	
ID INT NOT NULL , 
NAME VARCHAR(200), 
DATA_TYPE_ID INT, 
DESCRIPTION VARCHAR(255), 
STUDY_ID INT
);
ALTER TABLE STUDY.SUBJECT_CUSTM_FLD ADD CONSTRAINT SUBJECT_CUSTM_FLD_PK PRIMARY KEY (ID);
ALTER TABLE STUDY.SUBJECT_CUSTM_FLD ADD CONSTRAINT SUBJECT_CUSTM_FLD_FK1 FOREIGN KEY (DATA_TYPE_ID) REFERENCES STUDY.DATA_TYPE (ID);
ALTER TABLE STUDY.SUBJECT_CUSTM_FLD ADD CONSTRAINT SUBJECT_CUSTM_FLD_FK2 FOREIGN KEY (STUDY_ID) REFERENCES STUDY.STUDY (ID);

CREATE TABLE STUDY.SUBJECT_STATUS 
(	
ID INT, 
NAME VARCHAR(20), 
DESCRIPTION VARCHAR(255)
);
ALTER TABLE STUDY.SUBJECT_STATUS ADD CONSTRAINT SUBJECT_STATUS_PK PRIMARY KEY (ID);

CREATE TABLE STUDY.GENDER_TYPE
(
ID          INT NOT NULL,
NAME        VARCHAR(20),
DESCRIPTION VARCHAR(255)
);
ALTER TABLE STUDY.GENDER_TYPE ADD CONSTRAINT GENDER_TYPE_PK PRIMARY KEY (ID);

CREATE TABLE STUDY.TITLE_TYPE
(
ID          INT NOT NULL,
NAME        VARCHAR(20),
DESCRIPTION VARCHAR(255)
);
ALTER TABLE STUDY.TITLE_TYPE ADD CONSTRAINT TITLE_TYPE_PK PRIMARY KEY(ID);

CREATE TABLE STUDY.VITAL_STATUS
(
ID          INT NOT NULL,
NAME VARCHAR(20),
DESCRIPTION VARCHAR(255)
);
ALTER TABLE STUDY.VITAL_STATUS ADD CONSTRAINT VITAL_STATUS_PK PRIMARY KEY(ID);
