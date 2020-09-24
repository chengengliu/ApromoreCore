SET FOREIGN_KEY_CHECKS=0;

USE `apromore`;

DROP VIEW IF EXISTS `apromore`.`keywords`;

DROP TABLE IF EXISTS `search_history`;
DROP TABLE IF EXISTS `native`;
DROP TABLE IF EXISTS `native_type`;
DROP TABLE IF EXISTS `process`;
DROP TABLE IF EXISTS `process_branch`;
DROP TABLE IF EXISTS `process_model_version`;
DROP TABLE IF EXISTS `folder`;
DROP TABLE IF EXISTS `membership`;
DROP TABLE IF EXISTS `permission`;
DROP TABLE IF EXISTS `group`;
DROP TABLE IF EXISTS `user_group`;
DROP TABLE IF EXISTS `group_folder`;
DROP TABLE IF EXISTS `group_process`;
DROP TABLE IF EXISTS `role`;
DROP TABLE IF EXISTS `role_permission`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `user_role`;
DROP TABLE IF EXISTS `workspace`;
DROP TABLE IF EXISTS `edit_session`;
DROP TABLE IF EXISTS `process_model_attribute`;
DROP TABLE IF EXISTS `metric`;

DROP TABLE IF EXISTS `history_event`;

DROP TABLE IF EXISTS `group_log`;
DROP TABLE IF EXISTS `log`;
DROP TABLE IF EXISTS `dashboard_layout`;

# DROP TABLE IF EXISTS `qrtz_fired_triggers`;
# DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
# DROP TABLE IF EXISTS `qrtz_scheduler_state`;
# DROP TABLE IF EXISTS `qrtz_locks`;
# DROP TABLE IF EXISTS `qrtz_simple_triggers`;
# DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
# DROP TABLE IF EXISTS `qrtz_cron_triggers`;
# DROP TABLE IF EXISTS `qrtz_blob_triggers`;
# DROP TABLE IF EXISTS `qrtz_triggers`;
# DROP TABLE IF EXISTS `qrtz_job_details`;
# DROP TABLE IF EXISTS `qrtz_calendars`;


-- Construct the DB
CREATE TABLE `history_event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` varchar(50) NOT NULL,
  `type` varchar(50) NOT NULL,
  `occurDate` datetime DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_history_user` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `search_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `search` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `un_search` (`userId` , `search`),
  CONSTRAINT `fk_search` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `native_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nat_type` varchar(20) NOT NULL DEFAULT '',
  `extension` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `process` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `domain` varchar(40) DEFAULT NULL,
  `owner` int(11) DEFAULT NULL,
  `nativeTypeId` int(11) DEFAULT NULL,
  `folderId` int(11) DEFAULT NULL,
  `ranking` varchar(10) DEFAULT NULL,
  `createDate` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_process2` (`nativeTypeId`),
  KEY `fk_users` (`owner`),
  KEY `fk_folder` (`folderId`),
  CONSTRAINT `fk_folder` FOREIGN KEY (`folderId`)
  REFERENCES `folder` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_process1` FOREIGN KEY (`owner`)
  REFERENCES `user` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_process2` FOREIGN KEY (`nativeTypeId`)
  REFERENCES `native_type` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `folderId` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `file_path` varchar(255) NOT NULL,
  `domain` varchar(255) DEFAULT NULL,
  `ranking` varchar(10) DEFAULT NULL,
  `createDate` varchar(40) DEFAULT NULL,
  `owner` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_users` (`owner`),
  KEY `fk_folder` (`folderId`),
  CONSTRAINT `fk_log_folder` FOREIGN KEY (`folderId`)
  REFERENCES `folder` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_log1` FOREIGN KEY (`owner`)
  REFERENCES `user` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `native` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` longtext,
  `lastUpdateDate` varchar(40) DEFAULT NULL,
  `nativeTypeId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_native_type` FOREIGN KEY (`nativeTypeId`)
  REFERENCES `native_type` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `process_branch` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `branch_name` varchar(255),
  `processId` int(11) DEFAULT NULL,
  `createDate` varchar(40) DEFAULT NULL,
  `lastUpdateDate` varchar(40) DEFAULT NULL,
  `sourceProcessModelVersion` int(11) NULL,
  `currentProcessModelVersion` int(11) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_process_branch` FOREIGN KEY (`processId`)
  REFERENCES `process` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_source_version` FOREIGN KEY (`sourceProcessModelVersion`)
  REFERENCES `process_model_version` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_current_version` FOREIGN KEY (`currentProcessModelVersion`)
  REFERENCES `process_model_version` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `process_model_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `branchId` int(11) DEFAULT NULL,
  `rootFragmentVersionId` int(11) DEFAULT NULL,
  `nativeId` int(11) DEFAULT NULL,
  `canonicalId` int(11) DEFAULT NULL,
  `nativeTypeId` int(11) DEFAULT NULL,
  `originalId` varchar(200),
  `version_number` varchar(15),
  `change_propagation` int,
  `lock_status` int,
  `num_nodes` int,
  `num_edges` int,
  `createDate` varchar(40) DEFAULT NULL,
  `lastUpdateDate` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `fk_process_branch_model_version` FOREIGN KEY (`branchId`)
  REFERENCES `process_branch` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_process_native` FOREIGN KEY (`nativeId`)
  REFERENCES `native` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_process_native_type` FOREIGN KEY (`nativeTypeId`)
  REFERENCES `native_type` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `folder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parentId` int(11) DEFAULT NULL,
  `workspaceId` int(11) NULL DEFAULT '1',
  `folder_name` varchar(100) DEFAULT NULL,
  `folder_description` varchar(1000) DEFAULT NULL,
  `creatorId` int(11) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `modifiedById` int(11) DEFAULT NULL,
  `date_modified` datetime DEFAULT NULL,
  `ged_matrix_computation` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `folder_creator` (`creatorId`),
  KEY `folder_modified_by` (`modifiedById`),
  KEY `folder_workspace` (`workspaceId`),
  KEY `folder_folder` (`parentId`),
  CONSTRAINT `folder_creator` FOREIGN KEY (`creatorId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `folder_folder` FOREIGN KEY (`parentId`) REFERENCES `folder` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `folder_modified_by` FOREIGN KEY (`modifiedById`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `folder_workspace` FOREIGN KEY (`workspaceId`) REFERENCES `workspace` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `membership` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `password` varchar(100) NOT NULL,
  `password_salt` varchar(100) NOT NULL,
  `mobile_pin` varchar(100) DEFAULT NULL,
  `email` varchar(200) NOT NULL,
  `password_question` varchar(50) NULL DEFAULT NULL,
  `password_answer` varchar(50) NULL DEFAULT NULL,
  `is_approved` boolean not null default 1,
  `is_locked` boolean not null default 1,
  `date_created` datetime NOT NULL,
  `failed_password_attempts` int(11) NOT NULL DEFAULT '0',
  `failed_answer_attempts` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY (`userId`) USING BTREE,
  UNIQUE KEY (`email`) USING BTREE,
  CONSTRAINT `FK_users` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `row_guid` varchar(256) NOT NULL,
  `permission_name` varchar(45) NOT NULL,
  `permission_description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `row_guid` varchar(255) NOT NULL,
  `name` varchar(45) NOT NULL,
  `type` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `group_row_guid_UNIQUE` (`row_guid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_group` (
  `userId` int(11) NOT NULL,
  `groupId` int(11) NOT NULL,
  PRIMARY KEY (`userId` , `groupId`),
  KEY `fk_user_group_user` (`userId`),
  KEY `fk_user_group_group` (`groupId`),
  CONSTRAINT `fk_user_group_user` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_group_group` FOREIGN KEY (`groupId`) REFERENCES `group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `group_folder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupId` int(11) NOT NULL,
  `folderId` int(11) NOT NULL,
  `has_read` tinyint(1) NOT NULL DEFAULT '0',
  `has_write` tinyint(1) NOT NULL DEFAULT '0',
  `has_ownership` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_group_folder_folder` (`folderId`),
  KEY `fk_group_folder_group` (`groupId`),
  CONSTRAINT `fk_group_folder_folder` FOREIGN KEY (`folderId`) REFERENCES `folder` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_group_folder_group` FOREIGN KEY (`groupId`) REFERENCES `group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `group_process` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupId` int(11) NOT NULL,
  `processId` int(11) NOT NULL,
  `has_read` tinyint(1) NOT NULL DEFAULT '0',
  `has_write` tinyint(1) NOT NULL DEFAULT '0',
  `has_ownership` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_group_process_process` (`processId`),
  KEY `fk_group_process_group` (`groupId`),
  CONSTRAINT `fk_group_process_process` FOREIGN KEY (`processId`) REFERENCES `process` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_group_process_group` FOREIGN KEY (`groupId`) REFERENCES `group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `group_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupId` int(11) NOT NULL,
  `logId` int(11) NOT NULL,
  `has_read` tinyint(1) NOT NULL DEFAULT '0',
  `has_write` tinyint(1) NOT NULL DEFAULT '0',
  `has_ownership` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_group_log_log` (`logId`),
  KEY `fk_group_log_group` (`groupId`),
  CONSTRAINT `fk_group_log_log` FOREIGN KEY (`logId`) REFERENCES `log` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_group_log_group` FOREIGN KEY (`groupId`) REFERENCES `group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `row_guid` varchar(256) NOT NULL,
  `role_name` varchar(45) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `role_permission` (
  `roleId` int(11) NOT NULL,
  `permissionId` int(11) NOT NULL,
  PRIMARY KEY (`roleId` , `permissionId`),
  KEY `FK_role_permission_role` (`roleId`),
  KEY `FK_role_permission_permission` (`permissionId`),
  CONSTRAINT `FK_role_permission_permission` FOREIGN KEY (`permissionId`) REFERENCES `permission` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_role_permission_role` FOREIGN KEY (`roleId`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
)  ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `row_guid` varchar(255) NOT NULL,
  `username` varchar(45) NOT NULL,
  `date_created` datetime NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `last_activity_date` datetime,  -- null indicates never logged in --
  `groupId` int(11) NOT NULL,
  `organization` VARCHAR(255),
  `role` VARCHAR(255),
  `country` VARCHAR(255),
  `phone` VARCHAR(255),
  `subscription` VARCHAR(255),
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_row_guid_UNIQUE` (`row_guid`),
  UNIQUE KEY `user_username_UNIQUE` (`username`),
  KEY (`groupId`),
  CONSTRAINT `fk_user_group` FOREIGN KEY (`groupId`) REFERENCES `group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
)  ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_role` (
  `roleId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  PRIMARY KEY (`roleId` , `userId`),
  KEY `FK_user_role_users` (`userId`),
  KEY `FK_user_role_role` (`roleId`),
  CONSTRAINT `FK_user_role_role` FOREIGN KEY (`roleId`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_user_role_users` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
)  ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `workspace` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `workspace_name` varchar(45) NOT NULL,
  `workspace_description` varchar(1000) DEFAULT NULL,
  `userId` int(11) NOT NULL,
  `date_created` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `workspace_user` (`userId`),
  CONSTRAINT `workspace_user` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
)  ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `process_model_attribute` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `processModelVersionId` int(11) DEFAULT NULL,
  `name` varchar(255),
  `value` longtext,
  `any` longtext NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_pmv_att_pmv` FOREIGN KEY (`processModelVersionId`) REFERENCES `process_model_version` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
)  ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `metric` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `processModelVersionId` int(11) NOT NULL,
  `name` varchar(40) NOT NULL,
  `value` double NOT NULL,
  `lastUpdateDate` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `un_metric` (`processModelVersionId` , `name`),
  CONSTRAINT `fk_metric1` FOREIGN KEY (`processModelVersionId`) REFERENCES `process_model_version` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `dashboard_layout` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `logId` int(11) NOT NULL,
  `layout` mediumtext,
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`),
  KEY `logId` (`logId`),
  CONSTRAINT `dashboard_layout_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `dashboard_layout_ibfk_2` FOREIGN KEY (`logId`) REFERENCES `log` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `usermetadata_type`;

CREATE TABLE `usermetadata_type` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`type` varchar(255) DEFAULT NULL COMMENT 'Metadata type',
`version` int(11) DEFAULT NULL COMMENT 'Metadata type',
`is_valid` tinyint(1) DEFAULT NULL COMMENT 'Indicate whether this record is valid',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `usermetadata`;

CREATE TABLE `usermetadata` (
`id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
`type_id` int(11) NOT NULL COMMENT 'FK User mtadata type id',
`created_by` varchar(255) DEFAULT NULL COMMENT 'The user create this metadata',
`created_time` varchar(40) DEFAULT NULL COMMENT 'Create time',
`updated_by` varchar(255) DEFAULT NULL COMMENT 'The user updated this metadata',
`updated_time` varchar(40) DEFAULT NULL COMMENT 'Last update time',
`content` mediumtext COMMENT 'Content of user metadata',
`revision` int(11) DEFAULT NULL COMMENT 'reserve for optimistic lock',
`is_valid` tinyint(1) NOT NULL COMMENT 'Indicate whether this record is valid',
PRIMARY KEY (`id`),
KEY `type_id` (`type_id`),
CONSTRAINT `usermetadata_ibfk_2` FOREIGN KEY (`type_id`) REFERENCES `usermetadata_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `usermetadata_type` WRITE;
/*!40000 ALTER TABLE `usermetadata_type` DISABLE KEYS */;

INSERT INTO `usermetadata_type` (`id`, `type`, `version`, `is_valid`)
VALUES
(1,'FILTER',1,1),
(2,'DASHBOARD',1,1),
(3,'CSV_IMPORTER',1,1),
(4,'LOG_ANIMATION',1,1),
(5,'DASH_TEMPLATE',1,1);

/*!40000 ALTER TABLE `usermetadata_type` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `usermetadata_log`;

CREATE TABLE `usermetadata_log` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`usermetadata_id` int(11) DEFAULT NULL COMMENT 'FK USERMETADATA ID',
`log_id` int(11) DEFAULT NULL COMMENT 'FK LOG ID',
PRIMARY KEY (`id`),
KEY `log_id` (`log_id`),
KEY `usermetadata_id` (`usermetadata_id`),
CONSTRAINT `usermetadata_log_ibfk_1` FOREIGN KEY (`log_id`) REFERENCES `log` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT `usermetadata_log_ibfk_2` FOREIGN KEY (`usermetadata_id`) REFERENCES `usermetadata` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `usermetadata_process`;

CREATE TABLE `usermetadata_process` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`usermetadata_id` int(11) DEFAULT NULL COMMENT 'FK USERMETADATA ID',
`process_id` int(11) DEFAULT NULL COMMENT 'FK PROCESS ID',
PRIMARY KEY (`id`),
KEY `usermetadata_id` (`usermetadata_id`),
KEY `process_id` (`process_id`),
CONSTRAINT `usermetadata_process_ibfk_1` FOREIGN KEY (`usermetadata_id`) REFERENCES `usermetadata` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT `usermetadata_process_ibfk_2` FOREIGN KEY (`process_id`) REFERENCES `process` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `group_usermetadata`;

CREATE TABLE `group_usermetadata` (
`id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
`group_id` int(11) NOT NULL COMMENT 'FK GROUP ID',
`usermetadata_id` int(11) NOT NULL COMMENT 'FK USER METADATA ID',
`has_read` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Has read permission',
`has_write` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Has write permission',
`has_ownership` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Has owner permission',
PRIMARY KEY (`id`),
KEY `group_id` (`group_id`),
KEY `user_metadata_id` (`usermetadata_id`),
CONSTRAINT `group_usermetadata_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT `group_usermetadata_ibfk_2` FOREIGN KEY (`usermetadata_id`) REFERENCES `usermetadata` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Scheduler tables

# CREATE TABLE `qrtz_job_details` (
#   SCHED_NAME VARCHAR(120) NOT NULL,
#   JOB_NAME  VARCHAR(200) NOT NULL,
#   JOB_GROUP VARCHAR(200) NOT NULL,
#   DESCRIPTION VARCHAR(250) NULL,
#   JOB_CLASS_NAME   VARCHAR(250) NOT NULL,
#   IS_DURABLE VARCHAR(1) NOT NULL,
#   IS_NONCONCURRENT VARCHAR(1) NOT NULL,
#   IS_UPDATE_DATA VARCHAR(1) NOT NULL,
#   REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
#   JOB_DATA BLOB NULL,
#   PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
#
# CREATE TABLE `qrtz_triggers` (
#   SCHED_NAME VARCHAR(120) NOT NULL,
#   TRIGGER_NAME VARCHAR(200) NOT NULL,
#   TRIGGER_GROUP VARCHAR(200) NOT NULL,
#   JOB_NAME  VARCHAR(200) NOT NULL,
#   JOB_GROUP VARCHAR(200) NOT NULL,
#   DESCRIPTION VARCHAR(250) NULL,
#   NEXT_FIRE_TIME BIGINT(13) NULL,
#   PREV_FIRE_TIME BIGINT(13) NULL,
#   PRIORITY INTEGER NULL,
#   TRIGGER_STATE VARCHAR(16) NOT NULL,
#   TRIGGER_TYPE VARCHAR(8) NOT NULL,
#   START_TIME BIGINT(13) NOT NULL,
#   END_TIME BIGINT(13) NULL,
#   CALENDAR_NAME VARCHAR(200) NULL,
#   MISFIRE_INSTR SMALLINT(2) NULL,
#   JOB_DATA BLOB NULL,
#   PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
#   FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
#   REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
#
# CREATE TABLE `qrtz_simple_triggers` (
#   SCHED_NAME VARCHAR(120) NOT NULL,
#   TRIGGER_NAME VARCHAR(200) NOT NULL,
#   TRIGGER_GROUP VARCHAR(200) NOT NULL,
#   REPEAT_COUNT BIGINT(7) NOT NULL,
#   REPEAT_INTERVAL BIGINT(12) NOT NULL,
#   TIMES_TRIGGERED BIGINT(10) NOT NULL,
#   PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
#   FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
#   REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
#
# CREATE TABLE `qrtz_cron_triggers` (
#   SCHED_NAME VARCHAR(120) NOT NULL,
#   TRIGGER_NAME VARCHAR(200) NOT NULL,
#   TRIGGER_GROUP VARCHAR(200) NOT NULL,
#   CRON_EXPRESSION VARCHAR(200) NOT NULL,
#   TIME_ZONE_ID VARCHAR(80),
#   PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
#   FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
#   REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
#
# CREATE TABLE `qrtz_simprop_triggers` (
#   SCHED_NAME VARCHAR(120) NOT NULL,
#   TRIGGER_NAME VARCHAR(200) NOT NULL,
#   TRIGGER_GROUP VARCHAR(200) NOT NULL,
#   STR_PROP_1 VARCHAR(512) NULL,
#   STR_PROP_2 VARCHAR(512) NULL,
#   STR_PROP_3 VARCHAR(512) NULL,
#   INT_PROP_1 INT NULL,
#   INT_PROP_2 INT NULL,
#   LONG_PROP_1 BIGINT NULL,
#   LONG_PROP_2 BIGINT NULL,
#   DEC_PROP_1 NUMERIC(13,4) NULL,
#   DEC_PROP_2 NUMERIC(13,4) NULL,
#   BOOL_PROP_1 VARCHAR(1) NULL,
#   BOOL_PROP_2 VARCHAR(1) NULL,
#   PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
#   FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
#   REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
#
# CREATE TABLE `qrtz_blob_triggers` (
#   SCHED_NAME VARCHAR(120) NOT NULL,
#   TRIGGER_NAME VARCHAR(200) NOT NULL,
#   TRIGGER_GROUP VARCHAR(200) NOT NULL,
#   BLOB_DATA BLOB NULL,
#   PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
#   FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
#   REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
#
# CREATE TABLE `qrtz_calendars` (
#   SCHED_NAME VARCHAR(120) NOT NULL,
#   CALENDAR_NAME  VARCHAR(200) NOT NULL,
#   CALENDAR BLOB NOT NULL,
#   PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
#
# CREATE TABLE `qrtz_paused_trigger_grps` (
#   SCHED_NAME VARCHAR(120) NOT NULL,
#   TRIGGER_GROUP  VARCHAR(200) NOT NULL,
#   PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
#
# CREATE TABLE `qrtz_fired_triggers` (
#   SCHED_NAME VARCHAR(120) NOT NULL,
#   ENTRY_ID VARCHAR(95) NOT NULL,
#   TRIGGER_NAME VARCHAR(200) NOT NULL,
#   TRIGGER_GROUP VARCHAR(200) NOT NULL,
#   INSTANCE_NAME VARCHAR(200) NOT NULL,
#   FIRED_TIME BIGINT(13) NOT NULL,
#   SCHED_TIME BIGINT(13) NOT NULL,
#   PRIORITY INTEGER NOT NULL,
#   STATE VARCHAR(16) NOT NULL,
#   JOB_NAME VARCHAR(200) NULL,
#   JOB_GROUP VARCHAR(200) NULL,
#   IS_NONCONCURRENT VARCHAR(1) NULL,
#   REQUESTS_RECOVERY VARCHAR(1) NULL,
#   PRIMARY KEY (SCHED_NAME,ENTRY_ID)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
#
# CREATE TABLE `qrtz_scheduler_state` (
#   SCHED_NAME VARCHAR(120) NOT NULL,
#   INSTANCE_NAME VARCHAR(200) NOT NULL,
#   LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
#   CHECKIN_INTERVAL BIGINT(13) NOT NULL,
#   PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
#
# CREATE TABLE `qrtz_locks` (
#   SCHED_NAME VARCHAR(120) NOT NULL,
#   LOCK_NAME  VARCHAR(40) NOT NULL,
#   PRIMARY KEY (SCHED_NAME,LOCK_NAME)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- Create indexes for the tables

CREATE INDEX `idx_search_history` ON `search_history` (`position`, `search`) USING BTREE;
CREATE INDEX `idx_native_type` ON `native_type` (`nat_type`, `extension`) USING BTREE;
CREATE INDEX `idx_user_username` ON `user` (`username`) USING BTREE;
CREATE INDEX `idx_process_name` ON `process` (`name`, `folderId`) USING BTREE;
CREATE INDEX `idx_branch_name` ON `process_branch` (`branch_name`) USING BTREE;
CREATE INDEX `idx_pmv_version` ON `process_model_version` (`version_number`) USING BTREE;
CREATE INDEX `idx_pmv_lock` ON `process_model_version` (`lock_status`) USING BTREE;


-- Add the basic data used by the system.

LOCK TABLES `native_type` WRITE;
/*!40000 ALTER TABLE `native_type` DISABLE KEYS */;
/*INSERT INTO `native_type` VALUES (1,'EPML 2.0','epml');*/
/*INSERT INTO `native_type` VALUES (2,'XPDL 2.2','xpdl');*/
/*INSERT INTO `native_type` VALUES (3,'PNML 1.3.2', 'pnml');*/
/*INSERT INTO `native_type` VALUES (4,'YAWL 2.2', 'yawl');*/
INSERT INTO `native_type` VALUES (5,'BPMN 2.0', 'bpmn');
/*INSERT INTO `native_type` VALUES (6,'AML fragment', 'aml');*/
/*!40000 ALTER TABLE `native_type` ENABLE KEYS */;
UNLOCK TABLES;


LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'80da507e-cdd7-40f4-a9f8-b2d2edb12856','ROLE_ADMIN','Ultimate power to administer other users');
INSERT INTO `role` VALUES (2,'0ecd70b4-a204-41cd-a246-e3fcef88f6fe','ROLE_USER','Allowed to log in');
INSERT INTO `role` VALUES (3,'72503ce0-d7cd-47b3-a33c-1b741d7599a1','ROLE_MANAGER','Allowed to administer folders, models and event logs');
INSERT INTO `role` VALUES (4,'e0ae8c02-bfe0-11ea-992a-17a70e2c7d0f','ROLE_ANALYST','Allowed access to all tools except predictive monitoring training');
INSERT INTO `role` VALUES (5,'43fbd3c8-bfe1-11ea-9694-abeaa8ed1c0f','ROLE_OBSERVER','Allowed access to all tools in read-only mode');
INSERT INTO `role` VALUES (6,'4ff46cbc-bfe1-11ea-abec-aba9c1ea1178','ROLE_DESIGNER','Allowed access to the BPMN editor');
INSERT INTO `role` VALUES (7,'596fcfd4-bfe1-11ea-8564-4f9db22c941e','ROLE_DATA_SCIENTIST','Allowed access to all tools including predictive monitoring training');
INSERT INTO `role` VALUES (8,'64e734a6-bfe1-11ea-b4cf-2bed596c2920','ROLE_OPERATIONS','Allowed access to the predictive monitoring dashboard (runtime)');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;


LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'75f4a46a-bd32-4fbb-ba7a-c50d06414fac','james',   '2012-05-23 11:52:48','Cameron', 'James',       NULL,1,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `user` VALUES (2,'aaf24d0d-58f2-43b1-8dcc-bf99717b708f','chathura','2012-05-23 11:59:51','Chathura','Ekanayake',   NULL,2,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `user` VALUES (3,'a393f9c2-e2ee-49ed-9b6a-a1269811764c','arthur',  '2012-05-23 12:07:24','Arthur',  'Ter Hofstede',NULL,3,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `user` VALUES (4,'b6701ee5-227b-493e-9b01-85aa33acd53b','Macri',   '2012-05-23 20:08:03','Marie',   'Fauvet',      NULL,4,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `user` VALUES (5,'c81da91f-facc-4eff-b648-bdc1a2a5ebbe','larosa',  '2012-05-23 20:24:37','Marcello','La Rosa',     NULL,5,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `user` VALUES (6,'c03eff4d-3672-4c91-bfea-36c67e2423f5','felix',   '2012-05-23 20:37:44','Felix',   'Mannhardt',   NULL,6,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `user` VALUES (7,'fbcd5a9a-a224-40cb-8ab9-b12436d92835','raboczi', '2012-05-23 20:40:26','Simon',   'Raboczi',     NULL,7,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `user` VALUES (8,'ad1f7b60-1143-4399-b331-b887585a0f30','admin',   '2012-05-28 16:51:05','Test',    'User',        NULL,8,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;


LOCK TABLES `membership` WRITE;
/*!40000 ALTER TABLE `membership` DISABLE KEYS */;
INSERT INTO `membership` VALUES (1,1,'5f4dcc3b5aa765d61d8327deb882cf99','username','','cam.james@gmail.com','Test question','test',1,0,'2012-05-23 20:37:44',0,0);
INSERT INTO `membership` VALUES (2,2,'5f4dcc3b5aa765d61d8327deb882cf99','username','','c.ekanayake@qut.edu.au','Test question','test',1,0,'2012-05-28 16:51:05',0,0);
INSERT INTO `membership` VALUES (3,3,'5f4dcc3b5aa765d61d8327deb882cf99','username','','arthur@yawlfoundation.org','Test question','test',1,0,'2012-06-16 11:43:16',0,0);
INSERT INTO `membership` VALUES (4,4,'5f4dcc3b5aa765d61d8327deb882cf99','username','','marie-christine.fauvet@qut.edu.au','Test question','test',1,0,'2012-06-16 11:56:00',0,0);
INSERT INTO `membership` VALUES (5,5,'5f4dcc3b5aa765d61d8327deb882cf99','username','','m.larosa@qut.edu.au','Test question','test',1,0,'2012-06-16 12:01:35',0,0);
INSERT INTO `membership` VALUES (6,6,'5f4dcc3b5aa765d61d8327deb882cf99','username','','felix.mannhardt@smail.wir.h-brs.de','Test question','test',1,0,'2012-06-16 12:08:50',0,0);
INSERT INTO `membership` VALUES (7,7,'5f4dcc3b5aa765d61d8327deb882cf99','username','','raboczi@gmail.com','Test question','test',1,0,'2012-06-16 12:35:25',0,0);
INSERT INTO `membership` VALUES (8,8,'5f4dcc3b5aa765d61d8327deb882cf99','username','','admin','Test question','test',1,0,'2012-06-16 14:10:14',0,0);
/*!40000 ALTER TABLE `membership` ENABLE KEYS */;
UNLOCK TABLES;


LOCK TABLES `group` WRITE;
/*!40000 ALTER TABLE `group` DISABLE KEYS */;
INSERT INTO `group` VALUES (1,'uuid1-james',   'james',   'USER');
INSERT INTO `group` VALUES (2,'uuid2-chathura','chathura','USER');
INSERT INTO `group` VALUES (3,'uuid3-arthur',  'arthur',  'USER');
INSERT INTO `group` VALUES (4,'uuid4-Macri',   'Macri',   'USER');
INSERT INTO `group` VALUES (5,'uuid5-larosa',  'larosa',  'USER');
INSERT INTO `group` VALUES (6,'uuid6-felix',   'felix',   'USER');
INSERT INTO `group` VALUES (7,'uuid7-raboczi', 'raboczi', 'USER');
INSERT INTO `group` VALUES (8,'uuid8-admin',   'admin',   'USER');
INSERT INTO `group` VALUES (9,'uuid9-public',  'public',  'PUBLIC');
/*!40000 ALTER TABLE `group` ENABLE KEYS */;
UNLOCK TABLES;


LOCK TABLES `user_group` WRITE;
/*!40000 ALTER TABLE `user_group` DISABLE KEYS */;
INSERT INTO `user_group` VALUES (1,1),(1,9);  # every user is explicitly a member of the public group
INSERT INTO `user_group` VALUES (2,2),(2,9);
INSERT INTO `user_group` VALUES (3,3),(3,9);
INSERT INTO `user_group` VALUES (4,4),(4,9);
INSERT INTO `user_group` VALUES (5,5),(5,9);
INSERT INTO `user_group` VALUES (6,6),(6,9);
INSERT INTO `user_group` VALUES (7,7),(7,9);
INSERT INTO `user_group` VALUES (8,8),(8,9);
/*!40000 ALTER TABLE `user_group` ENABLE KEYS */;
UNLOCK TABLES;


LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,8);
INSERT INTO `user_role` VALUES (2,1);
INSERT INTO `user_role` VALUES (2,2);
INSERT INTO `user_role` VALUES (2,3);
INSERT INTO `user_role` VALUES (2,4);
INSERT INTO `user_role` VALUES (2,5);
INSERT INTO `user_role` VALUES (2,6);
INSERT INTO `user_role` VALUES (2,7);
INSERT INTO `user_role` VALUES (2,8);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;


LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` VALUES (1,'dff60714-1d61-4544-8884-0d8b852ba41e','View users','View other user data');
INSERT INTO `permission` VALUES (2,'2e884153-feb2-4842-b291-769370c86e44','Edit users','Modify other user data');
INSERT INTO `permission` VALUES (3,'d9ade57c-14c7-4e43-87e5-6a9127380b1b','Edit groups','Create, populate and delete groups');
INSERT INTO `permission` VALUES (4,'ea31a607-212f-447e-8c45-78f1e59b1dde','Edit roles','Modify other user roles');
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;


LOCK TABLES `role_permission` WRITE;
/*!40000 ALTER TABLE `role_permission` DISABLE KEYS */;
INSERT INTO `role_permission` VALUES (1,1);
INSERT INTO `role_permission` VALUES (1,2);
INSERT INTO `role_permission` VALUES (1,3);
INSERT INTO `role_permission` VALUES (1,4);
INSERT INTO `role_permission` VALUES (2,1);
INSERT INTO `role_permission` VALUES (3,3);
/*!40000 ALTER TABLE `role_permission` ENABLE KEYS */;
UNLOCK TABLES;


LOCK TABLES `search_history` WRITE;
/*!40000 ALTER TABLE `search_history` DISABLE KEYS */;
INSERT INTO `search_history` VALUES (1,1,8,'airport');
INSERT INTO `search_history` VALUES (2,2,8,'gold coast');
INSERT INTO `search_history` VALUES (3,3,8,'goldcoast');
/*!40000 ALTER TABLE `search_history` ENABLE KEYS */;
UNLOCK TABLES;


CREATE VIEW apromore.keywords AS
  select process.id
    AS value, 'process' AS type, process.id AS processId, NULL AS logId, NULL AS folderId
    from process union
  select process.name
    AS value, 'process' AS type, process.id AS processId, NULL AS logId, NULL AS folderId
    from process union
  select process.domain
    AS value, 'process' AS type, process.id AS processId, NULL AS logId, NULL AS folderId
    from process union
  select native_type.nat_type
    AS value, 'process' AS type, process.id AS processId, NULL AS logId, NULL AS folderId
    from process join native_type ON (process.nativeTypeId = native_type.id) union
  select `user`.first_name
    AS value, 'process' AS type, process.id AS processId, NULL AS logId, NULL AS folderId
    from process join `user` ON (process.owner = `user`.username) union
  select `user`.last_name
    AS value, 'process' AS type, process.id AS processId, NULL AS logId, NULL AS folderId
    from process join `user` ON (process.owner = `user`.username) union
  select process_branch.branch_name
    AS value, 'process' AS type, process_branch.processId AS processId, NULL AS logId, NULL AS folderId
    from process_branch union
  select `log`.id
    AS value, 'log' AS type, NULL AS processId, `log`.id AS logId, NULL AS folderId
    from `log` union
  select `log`.name
    AS value, 'log' AS type, NULL AS processId, `log`.id AS logId, NULL AS folderId
    from `log` union
  select `log`.domain
    AS value, 'log' AS type, NULL AS processId, `log`.id AS logId, NULL AS folderId
    from `log` union
  select folder.folder_name
    AS value, 'folder' AS type, NULL AS processId, NULL AS logId, folder.id AS folderId
    from folder;

SET FOREIGN_KEY_CHECKS=1;
