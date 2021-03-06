USE study;
/** Add work main tab **/
INSERT INTO `study`.`ark_module`
(`NAME`,
`DESCRIPTION`)
VALUES
(
'Work',
'Work Tracking Module'
);


/** Add Researcher, Billable Item Type, Work Request, Billable Item Sub tabs **/
INSERT INTO `study`.`ark_function`
(`NAME`,
`DESCRIPTION`,
`ARK_FUNCTION_TYPE_ID`,
`RESOURCE_KEY`)
VALUES
(
'RESEARCHER',
'Researcher tab',
(select ID from study.ark_function_type where LOWER(name)=LOWER('NON-REPORT')),
'tab.module.work.researcher'
),
(
'BILLABLE_ITEM_TYPE',
'Billable item type tab',
(select ID from study.ark_function_type where LOWER(name)=LOWER('NON-REPORT')),
'tab.module.work.billableitemtype'
),
(
'WORK_REQUEST',
'Work Request tab',
(select ID from study.ark_function_type where LOWER(name)=LOWER('NON-REPORT')),
'tab.module.work.workrequest'
),
(
'BILLABLE_ITEM',
'Billable Item Tab',
(select ID from study.ark_function_type where LOWER(name)=LOWER('NON-REPORT')),
'tab.module.work.billableitem'
);

/** Join Researcher, Billable Item Type, Work Request, Billable Item Sub tabs to the Work main tab **/

INSERT INTO `study`.`ark_module_function`
(`ARK_MODULE_ID`,
`ARK_FUNCTION_ID`,
`FUNCTION_SEQUENCE`)
VALUES
(
(select ID from study.ark_module where LOWER(Name) = LOWER('Work')),
(select ID from study.ark_function where LOWER(RESOURCE_KEY) = LOWER('tab.module.work.researcher')),
1
),
(
(select ID from study.ark_module where LOWER(Name) = LOWER('Work')),
(select ID from study.ark_function where LOWER(RESOURCE_KEY) = LOWER('tab.module.work.billableitemtype')),
2
),
(
(select ID from study.ark_module where LOWER(Name) = LOWER('Work')),
(select ID from study.ark_function where LOWER(RESOURCE_KEY) = LOWER('tab.module.work.workrequest')),
3
),
(
(select ID from study.ark_module where LOWER(Name) = LOWER('Work')),
(select ID from study.ark_function where LOWER(RESOURCE_KEY) = LOWER('tab.module.work.billableitem')),
4
);


/** Give permission to the user roles to access the work tab **/
INSERT INTO `study`.`ark_module_role`
(
`ARK_MODULE_ID`,
`ARK_ROLE_ID`)
VALUES
(
(select ID from study.ark_module where LOWER(Name) = LOWER('Work')),
(select ID from study.ark_role where LOWER(Name) = LOWER('Study Administrator'))
);


/* Insert CRUD permissions */
INSERT INTO `study`.`ark_role_policy_template`
(`ARK_ROLE_ID`,
`ARK_MODULE_ID`,
`ARK_FUNCTION_ID`,
`ARK_PERMISSION_ID`)
VALUES
(
(SELECT ID FROM study.ark_role WHERE name = 'Study Administrator'),
(SELECT ID FROM study.ark_module WHERE LOWER(Name) = LOWER('Work')),
(select ID from study.ark_function where LOWER(RESOURCE_KEY) = LOWER('tab.module.work.researcher')),
1
),
(
(SELECT ID FROM study.ark_role WHERE name = 'Study Administrator'),
(SELECT ID FROM study.ark_module WHERE LOWER(Name) = LOWER('Work')),
(select ID from study.ark_function where LOWER(RESOURCE_KEY) = LOWER('tab.module.work.researcher')),
2
),
(
(SELECT ID FROM study.ark_role WHERE name = 'Study Administrator'),
(SELECT ID FROM study.ark_module WHERE LOWER(Name) = LOWER('Work')),
(select ID from study.ark_function where LOWER(RESOURCE_KEY) = LOWER('tab.module.work.researcher')),
3
),
(
(SELECT ID FROM study.ark_role WHERE name = 'Study Administrator'),
(SELECT ID FROM study.ark_module WHERE LOWER(Name) = LOWER('Work')),
(select ID from study.ark_function where LOWER(RESOURCE_KEY) = LOWER('tab.module.work.researcher')),
4
);

INSERT INTO `study`.`ark_role_policy_template`
(`ARK_ROLE_ID`,
`ARK_MODULE_ID`,
`ARK_FUNCTION_ID`,
`ARK_PERMISSION_ID`)
VALUES
(
(SELECT ID FROM study.ark_role WHERE name = 'Study Administrator'),
(SELECT ID FROM study.ark_module WHERE LOWER(Name) = LOWER('Work')),
(select ID from study.ark_function where LOWER(RESOURCE_KEY) = LOWER('tab.module.work.billableitemtype')),
1
),
(
(SELECT ID FROM study.ark_role WHERE name = 'Study Administrator'),
(SELECT ID FROM study.ark_module WHERE LOWER(Name) = LOWER('Work')),
(select ID from study.ark_function where LOWER(RESOURCE_KEY) = LOWER('tab.module.work.billableitemtype')),
2
),
(
(SELECT ID FROM study.ark_role WHERE name = 'Study Administrator'),
(SELECT ID FROM study.ark_module WHERE LOWER(Name) = LOWER('Work')),
(select ID from study.ark_function where LOWER(RESOURCE_KEY) = LOWER('tab.module.work.billableitemtype')),
3
),
(
(SELECT ID FROM study.ark_role WHERE name = 'Study Administrator'),
(SELECT ID FROM study.ark_module WHERE LOWER(Name) = LOWER('Work')),
(select ID from study.ark_function where LOWER(RESOURCE_KEY) = LOWER('tab.module.work.billableitemtype')),
4
);

INSERT INTO `study`.`ark_role_policy_template`
(`ARK_ROLE_ID`,
`ARK_MODULE_ID`,
`ARK_FUNCTION_ID`,
`ARK_PERMISSION_ID`)
VALUES
(
(SELECT ID FROM study.ark_role WHERE name = 'Study Administrator'),
(SELECT ID FROM study.ark_module WHERE LOWER(Name) = LOWER('Work')),
(select ID from study.ark_function where LOWER(RESOURCE_KEY) = LOWER('tab.module.work.workrequest')),
1
),
(
(SELECT ID FROM study.ark_role WHERE name = 'Study Administrator'),
(SELECT ID FROM study.ark_module WHERE LOWER(Name) = LOWER('Work')),
(select ID from study.ark_function where LOWER(RESOURCE_KEY) = LOWER('tab.module.work.workrequest')),
2
),
(
(SELECT ID FROM study.ark_role WHERE name = 'Study Administrator'),
(SELECT ID FROM study.ark_module WHERE LOWER(Name) = LOWER('Work')),
(select ID from study.ark_function where LOWER(RESOURCE_KEY) = LOWER('tab.module.work.workrequest')),
3
),
(
(SELECT ID FROM study.ark_role WHERE name = 'Study Administrator'),
(SELECT ID FROM study.ark_module WHERE LOWER(Name) = LOWER('Work')),
(select ID from study.ark_function where LOWER(RESOURCE_KEY) = LOWER('tab.module.work.workrequest')),
4
);

INSERT INTO `study`.`ark_role_policy_template`
(`ARK_ROLE_ID`,
`ARK_MODULE_ID`,
`ARK_FUNCTION_ID`,
`ARK_PERMISSION_ID`)
VALUES
(
(SELECT ID FROM study.ark_role WHERE name = 'Study Administrator'),
(SELECT ID FROM study.ark_module WHERE LOWER(Name) = LOWER('Work')),
(select ID from study.ark_function where LOWER(RESOURCE_KEY) = LOWER('tab.module.work.billableitem')),
1
),
(
(SELECT ID FROM study.ark_role WHERE name = 'Study Administrator'),
(SELECT ID FROM study.ark_module WHERE LOWER(Name) = LOWER('Work')),
(select ID from study.ark_function where LOWER(RESOURCE_KEY) = LOWER('tab.module.work.billableitem')),
2
),
(
(SELECT ID FROM study.ark_role WHERE name = 'Study Administrator'),
(SELECT ID FROM study.ark_module WHERE LOWER(Name) = LOWER('Work')),
(select ID from study.ark_function where LOWER(RESOURCE_KEY) = LOWER('tab.module.work.billableitem')),
3
),
(
(SELECT ID FROM study.ark_role WHERE name = 'Study Administrator'),
(SELECT ID FROM study.ark_module WHERE LOWER(Name) = LOWER('Work')),
(select ID from study.ark_function where LOWER(RESOURCE_KEY) = LOWER('tab.module.work.billableitem')),
4
);



