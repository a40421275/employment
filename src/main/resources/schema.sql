-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    `phone` VARCHAR(20) UNIQUE COMMENT '手机号',
    `email` VARCHAR(100) UNIQUE COMMENT '邮箱',
    `password` VARCHAR(255) COMMENT '密码',
    `wx_openid` VARCHAR(100) UNIQUE COMMENT '微信openid',
    `wx_unionid` VARCHAR(100) UNIQUE COMMENT '微信unionid',
    `user_type` TINYINT NOT NULL DEFAULT 1 COMMENT '用户类型：1-求职者，2-企业用户，3-管理员',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常，2-黑名单',
    `auth_status` TINYINT NOT NULL DEFAULT 0 COMMENT '认证状态：0-未认证，1-审核中，2-已认证',
    `last_login_time` DATETIME COMMENT '最后登录时间',
    `company_id` BIGINT COMMENT '所属企业ID，仅企业用户使用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_phone` (`phone`),
    INDEX `idx_wx_openid` (`wx_openid`),
    INDEX `idx_status` (`status`),
    INDEX `idx_company_id` (`company_id`),
    FOREIGN KEY (`company_id`) REFERENCES `company`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 用户资料表
CREATE TABLE IF NOT EXISTS `user_profile` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `real_name` VARCHAR(50) COMMENT '真实姓名',
    `gender` TINYINT COMMENT '性别：0-未知，1-男，2-女',
    `birthday` DATE COMMENT '生日',
    `avatar` VARCHAR(500) COMMENT '头像URL',
    `id_card_front` VARCHAR(500) COMMENT '身份证正面',
    `id_card_back` VARCHAR(500) COMMENT '身份证反面',
    `education` VARCHAR(50) COMMENT '最高学历',
    `work_years` INT COMMENT '工作年限',
    `current_salary` DECIMAL(10,2) COMMENT '当前薪资',
    `expected_salary` DECIMAL(10,2) COMMENT '期望薪资',
    `city` VARCHAR(100) COMMENT '所在城市',
    `skills` TEXT COMMENT '技能标签，逗号分隔',
    `self_intro` TEXT COMMENT '自我介绍',
    `preferred_cities` TEXT COMMENT '期望城市，逗号分隔',
    `job_types` TEXT COMMENT '工作类型，逗号分隔',
    `industries` TEXT COMMENT '期望行业，逗号分隔',
    `work_mode` VARCHAR(50) COMMENT '工作模式：全职/兼职/实习/远程',
    `job_status` VARCHAR(50) COMMENT '求职状态：在职-看机会/离职-随时到岗/在校-实习',
    `job_apply_count` INT NOT NULL DEFAULT 0 COMMENT '投递次数',
    `interview_count` INT NOT NULL DEFAULT 0 COMMENT '面试次数',
    `offer_count` INT NOT NULL DEFAULT 0 COMMENT '录用次数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    INDEX `idx_city` (`city`),
    INDEX `idx_education` (`education`),
    INDEX `idx_work_years` (`work_years`),
    INDEX `idx_expected_salary` (`expected_salary`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户资料表';

-- 简历表（重构后）
CREATE TABLE IF NOT EXISTS `resume` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `title` VARCHAR(100) NOT NULL COMMENT '简历标题',
    `resume_type` TINYINT NOT NULL DEFAULT 1 COMMENT '简历类型：1-附件简历，2-结构化简历',
    `file_url` VARCHAR(500) COMMENT '简历文件URL',
    `file_type` VARCHAR(20) COMMENT '文件类型：pdf/doc/docx',
    `is_default` TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认简历：0-否，1-是',
    `privacy_level` TINYINT NOT NULL DEFAULT 1 COMMENT '隐私级别：1-公开，2-仅投递企业，3-隐藏',
    `sync_profile_data` TINYINT NOT NULL DEFAULT 0 COMMENT '是否同步用户资料：0-否，1-是',
    `last_sync_time` DATETIME COMMENT '最后同步时间',
    `view_count` INT NOT NULL DEFAULT 0 COMMENT '被查看次数',
    `download_count` INT NOT NULL DEFAULT 0 COMMENT '下载次数',
    `share_count` INT NOT NULL DEFAULT 0 COMMENT '分享次数',
    `last_view_time` DATETIME COMMENT '最后查看时间',
    `structured_data` TEXT COMMENT '结构化简历数据（JSON格式）',
    `basic_info` TEXT COMMENT '基础信息（JSON格式）',
    `template_style` VARCHAR(50) COMMENT '模板样式',
    `color_scheme` VARCHAR(50) COMMENT '配色方案',
    `font_family` VARCHAR(50) COMMENT '字体',
    `show_photo` TINYINT NOT NULL DEFAULT 1 COMMENT '是否显示照片：0-否，1-是',
    `show_salary` TINYINT NOT NULL DEFAULT 0 COMMENT '是否显示薪资：0-否，1-是',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_resume_type` (`resume_type`),
    INDEX `idx_is_default` (`is_default`),
    INDEX `idx_privacy_level` (`privacy_level`),
    INDEX `idx_sync_profile_data` (`sync_profile_data`),
    INDEX `idx_last_view_time` (`last_view_time`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='简历表（重构后）';

-- 实名认证记录表
CREATE TABLE IF NOT EXISTS `auth_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `auth_type` TINYINT NOT NULL COMMENT '认证类型：1-实名认证，2-企业认证',
    `status` TINYINT NOT NULL COMMENT '状态：0-失败，1-成功，2-审核中',
    `auth_data` TEXT COMMENT '认证数据（JSON格式）',
    `fail_reason` VARCHAR(500) COMMENT '失败原因',
    `operator_id` BIGINT COMMENT '操作员ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_status` (`status`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实名认证记录表';

-- 岗位分类表
CREATE TABLE IF NOT EXISTS `job_category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL COMMENT '分类名称',
    `description` VARCHAR(500) COMMENT '分类描述',
    `parent_id` BIGINT COMMENT '父级ID',
    `level` TINYINT NOT NULL DEFAULT 1 COMMENT '层级',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `icon` VARCHAR(200) COMMENT '分类图标',
    `color` VARCHAR(50) COMMENT '分类颜色',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_parent_id` (`parent_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位分类表';

-- 岗位表
CREATE TABLE IF NOT EXISTS `job` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(200) NOT NULL COMMENT '岗位标题',
    `category_id` BIGINT NOT NULL COMMENT '分类ID',
    `company_id` BIGINT COMMENT '公司ID',
    `company_name` VARCHAR(200) NOT NULL COMMENT '公司名称',
    `department` VARCHAR(100) COMMENT '部门',
    `job_type` TINYINT NOT NULL DEFAULT 1 COMMENT '岗位类型：1-全职，2-兼职，3-实习',
    `salary_min` DECIMAL(10,2) COMMENT '最低薪资',
    `salary_max` DECIMAL(10,2) COMMENT '最高薪资',
    `salary_unit` VARCHAR(20) DEFAULT '月' COMMENT '薪资单位：月/年/小时',
    `work_city` VARCHAR(100) NOT NULL COMMENT '工作城市',
    `work_address` VARCHAR(500) COMMENT '工作地址',
    `description` TEXT COMMENT '岗位描述',
    `requirements` TEXT COMMENT '任职要求',
    `benefits` TEXT COMMENT '福利待遇',
    `contact_info` TEXT COMMENT '联系信息（JSON格式）',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-草稿，1-已发布，2-已下架，3-已归档',
    `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览数',
    `apply_count` INT NOT NULL DEFAULT 0 COMMENT '申请数',
    `favorite_count` INT NOT NULL DEFAULT 0 COMMENT '收藏数',
    `publish_time` DATETIME COMMENT '发布时间',
    `expire_time` DATETIME COMMENT '过期时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_category_id` (`category_id`),
    INDEX `idx_company_id` (`company_id`),
    INDEX `idx_work_city` (`work_city`),
    INDEX `idx_status` (`status`),
    INDEX `idx_publish_time` (`publish_time`),
    FULLTEXT INDEX `idx_title_desc` (`title`, `description`),
    FOREIGN KEY (`category_id`) REFERENCES `job_category`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位表';

-- 岗位标签表（多对多关系设计）
CREATE TABLE IF NOT EXISTS `job_tag` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `tag_name` VARCHAR(50) NOT NULL COMMENT '标签名称',
    `tag_type` TINYINT NOT NULL DEFAULT 1 COMMENT '标签类型：0-系统标签，1-自定义标签',
    `tag_color` VARCHAR(20) COMMENT '标签颜色',
    `description` VARCHAR(200) COMMENT '标签描述',
    `use_count` INT NOT NULL DEFAULT 0 COMMENT '使用次数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tag_name` (`tag_name`),
    INDEX `idx_tag_name` (`tag_name`),
    INDEX `idx_tag_type` (`tag_type`),
    INDEX `idx_use_count` (`use_count`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位标签表';

-- 岗位标签关联表（多对多关系）
CREATE TABLE IF NOT EXISTS `job_tag_relation` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `job_id` BIGINT NOT NULL COMMENT '岗位ID',
    `tag_id` BIGINT NOT NULL COMMENT '标签ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_job_tag` (`job_id`, `tag_id`),
    INDEX `idx_job_id` (`job_id`),
    INDEX `idx_tag_id` (`tag_id`),
    FOREIGN KEY (`job_id`) REFERENCES `job`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`tag_id`) REFERENCES `job_tag`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位标签关联表';

-- 岗位申请表
CREATE TABLE IF NOT EXISTS `job_apply` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `job_id` BIGINT NOT NULL COMMENT '岗位ID',
    `resume_id` BIGINT NOT NULL COMMENT '简历ID',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-已投递，2-已查看，3-已通知面试，4-面试通过，5-已拒绝',
    `match_score` DECIMAL(5,2) COMMENT '匹配分数',
    `apply_notes` TEXT COMMENT '申请备注',
    `interview_time` DATETIME COMMENT '面试时间',
    `interview_location` VARCHAR(500) COMMENT '面试地点',
    `feedback` TEXT COMMENT '企业反馈',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_job` (`user_id`, `job_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_job_id` (`job_id`),
    INDEX `idx_status` (`status`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`job_id`) REFERENCES `job`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`resume_id`) REFERENCES `resume`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位申请表';

-- 岗位收藏表
CREATE TABLE IF NOT EXISTS `job_favorite` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `job_id` BIGINT NOT NULL COMMENT '岗位ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_job` (`user_id`, `job_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_job_id` (`job_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`job_id`) REFERENCES `job`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位收藏表';

-- 匹配记录表
CREATE TABLE IF NOT EXISTS `match_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `job_id` BIGINT NOT NULL COMMENT '岗位ID',
    `match_score` DECIMAL(5,2) NOT NULL COMMENT '匹配分数',
    `match_reason` TEXT COMMENT '匹配原因',
    `is_viewed` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已查看：0-否，1-是',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_job_id` (`job_id`),
    INDEX `idx_match_score` (`match_score`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`job_id`) REFERENCES `job`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='匹配记录表';

-- 消息通知表
CREATE TABLE IF NOT EXISTS `notification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `content` TEXT NOT NULL COMMENT '内容',
    `type` TINYINT NOT NULL COMMENT '类型：1-系统消息，2-申请结果，3-岗位推荐，4-面试通知',
    `is_read` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已读：0-否，1-是',
    `send_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `read_time` DATETIME COMMENT '阅读时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_type` (`type`),
    INDEX `idx_is_read` (`is_read`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息通知表';

-- 管理员表
CREATE TABLE IF NOT EXISTS `admin_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
    `role` VARCHAR(50) NOT NULL COMMENT '角色：super_admin, auditor, operator',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `last_login_time` DATETIME COMMENT '最后登录时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 操作日志表
CREATE TABLE IF NOT EXISTS `operation_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `admin_id` BIGINT COMMENT '管理员ID',
    `operation` VARCHAR(100) NOT NULL COMMENT '操作类型',
    `module` VARCHAR(50) NOT NULL COMMENT '模块名称',
    `description` TEXT COMMENT '操作描述',
    `ip_address` VARCHAR(50) COMMENT 'IP地址',
    `user_agent` TEXT COMMENT '用户代理',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_admin_id` (`admin_id`),
    INDEX `idx_module` (`module`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 权限表
CREATE TABLE IF NOT EXISTS `permission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL COMMENT '权限名称',
    `code` VARCHAR(100) UNIQUE NOT NULL COMMENT '权限代码',
    `description` VARCHAR(500) COMMENT '权限描述',
    `type` TINYINT NOT NULL DEFAULT 1 COMMENT '权限类型：1-菜单，2-按钮，3-接口',
    `parent_id` BIGINT COMMENT '父权限ID',
    `url` VARCHAR(200) COMMENT '权限URL',
    `method` VARCHAR(50) COMMENT '请求方法：GET, POST, PUT, DELETE等',
    `sort_order` INT NOT NULL DEFAULT 1 COMMENT '排序序号',
    `icon` VARCHAR(50) COMMENT '图标',
    `module` VARCHAR(50) COMMENT '所属模块',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_code` (`code`),
    INDEX `idx_module` (`module`),
    INDEX `idx_parent_id` (`parent_id`),
    INDEX `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 角色表
CREATE TABLE IF NOT EXISTS `role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL COMMENT '角色名称',
    `code` VARCHAR(100) UNIQUE NOT NULL COMMENT '角色代码',
    `description` VARCHAR(500) COMMENT '角色描述',
    `type` TINYINT NOT NULL DEFAULT 3 COMMENT '角色类型：1-系统角色，2-业务角色，3-自定义角色',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_code` (`code`),
    INDEX `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS `role_permission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT NOT NULL COMMENT '权限ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
    INDEX `idx_role_id` (`role_id`),
    INDEX `idx_permission_id` (`permission_id`),
    FOREIGN KEY (`role_id`) REFERENCES `role`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`permission_id`) REFERENCES `permission`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `user_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_role_id` (`role_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`role_id`) REFERENCES `role`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 企业表
CREATE TABLE IF NOT EXISTS `company` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `company_name` VARCHAR(200) NOT NULL COMMENT '企业名称',
    `company_short_name` VARCHAR(100) COMMENT '企业简称',
    `company_logo` VARCHAR(500) COMMENT '企业Logo',
    `company_type` TINYINT NOT NULL DEFAULT 1 COMMENT '企业类型：1-有限责任公司，2-股份有限公司，3-个人独资企业，4-合伙企业，5-外资企业，6-其他',
    `industry` VARCHAR(100) NOT NULL COMMENT '所属行业',
    `scale` TINYINT NOT NULL DEFAULT 1 COMMENT '企业规模：1-1-50人，2-51-100人，3-101-500人，4-501-1000人，5-1000人以上',
    `legal_person` VARCHAR(50) NOT NULL COMMENT '法人代表',
    `registered_capital` DECIMAL(15,2) COMMENT '注册资本（万元）',
    `establish_date` DATE COMMENT '成立日期',
    `business_license_no` VARCHAR(50) COMMENT '营业执照号',
    `business_license_image` VARCHAR(500) COMMENT '营业执照图片',
    `business_scope` TEXT COMMENT '经营范围',
    `province` VARCHAR(50) NOT NULL COMMENT '省份',
    `city` VARCHAR(50) NOT NULL COMMENT '城市',
    `district` VARCHAR(50) COMMENT '区县',
    `address` VARCHAR(500) NOT NULL COMMENT '详细地址',
    `latitude` DOUBLE COMMENT '纬度',
    `longitude` DOUBLE COMMENT '经度',
    `contact_person` VARCHAR(50) NOT NULL COMMENT '联系人',
    `contact_phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
    `contact_email` VARCHAR(100) COMMENT '联系邮箱',
    `website` VARCHAR(200) COMMENT '公司网站',
    `introduction` TEXT COMMENT '公司介绍',
    `culture` TEXT COMMENT '企业文化',
    `welfare` TEXT COMMENT '公司福利',
    `development` TEXT COMMENT '发展历程',
    `honors` TEXT COMMENT '荣誉资质',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常，2-审核中，3-审核失败',
    `auth_status` TINYINT NOT NULL DEFAULT 0 COMMENT '认证状态：0-未认证，1-审核中，2-已认证',
    `auth_time` DATETIME COMMENT '认证时间',
    `auth_operator_id` BIGINT COMMENT '认证操作员ID',
    `auth_fail_reason` VARCHAR(500) COMMENT '认证失败原因',
    `job_count` INT NOT NULL DEFAULT 0 COMMENT '发布岗位数量',
    `view_count` INT NOT NULL DEFAULT 0 COMMENT '被查看次数',
    `favorite_count` INT NOT NULL DEFAULT 0 COMMENT '被收藏次数',
    `apply_count` INT NOT NULL DEFAULT 0 COMMENT '收到申请数量',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_company_name` (`company_name`),
    INDEX `idx_company_type` (`company_type`),
    INDEX `idx_industry` (`industry`),
    INDEX `idx_scale` (`scale`),
    INDEX `idx_city` (`city`),
    INDEX `idx_status` (`status`),
    INDEX `idx_auth_status` (`auth_status`),
    INDEX `idx_create_time` (`create_time`),
    FULLTEXT INDEX `idx_company_name_intro` (`company_name`, `introduction`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业表';

-- 系统配置表
CREATE TABLE IF NOT EXISTS `system_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `config_key` VARCHAR(100) UNIQUE NOT NULL COMMENT '配置键',
    `config_value` TEXT NOT NULL COMMENT '配置值',
    `description` VARCHAR(500) COMMENT '配置描述',
    `config_group` VARCHAR(50) COMMENT '配置分组',
    `value_type` VARCHAR(20) COMMENT '配置值类型',
    `editable` TINYINT NOT NULL DEFAULT 1 COMMENT '是否可编辑：0-否，1-是',
    `system_config` TINYINT NOT NULL DEFAULT 0 COMMENT '是否为系统配置：0-否，1-是',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序顺序',
    `validation_rule` VARCHAR(200) COMMENT '验证规则',
    `default_value` TEXT COMMENT '默认值',
    `options` TEXT COMMENT '选项值（JSON格式）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_config_key` (`config_key`),
    INDEX `idx_config_group` (`config_group`),
    INDEX `idx_value_type` (`value_type`),
    INDEX `idx_editable` (`editable`),
    INDEX `idx_system_config` (`system_config`),
    INDEX `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';


-- 文件表（重构后）- 只保存物理文件信息
CREATE TABLE IF NOT EXISTS `file` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `file_name` VARCHAR(255) NOT NULL COMMENT '存储文件名',
    `original_name` VARCHAR(255) NOT NULL COMMENT '原始文件名',
    `file_path` VARCHAR(500) NOT NULL COMMENT '文件存储路径',
    `file_size` BIGINT NOT NULL COMMENT '文件大小（字节）',
    `file_type` VARCHAR(50) NOT NULL COMMENT '文件类型',
    `mime_type` VARCHAR(100) NOT NULL COMMENT 'MIME类型',
    `file_extension` VARCHAR(20) COMMENT '文件扩展名',
    `file_hash` VARCHAR(64) NOT NULL COMMENT '文件哈希值（SHA-256），用于重复文件检测',
    `creator_user_id` BIGINT NOT NULL COMMENT '创建者用户ID',
    `reference_count` INT NOT NULL DEFAULT 0 COMMENT '引用计数，记录有多少个附件在使用这个文件',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常，2-已删除',
    `last_access_time` DATETIME COMMENT '最后访问时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_file_hash` (`file_hash`),
    INDEX `idx_creator_user_id` (`creator_user_id`),
    INDEX `idx_file_type` (`file_type`),
    INDEX `idx_status` (`status`),
    INDEX `idx_reference_count` (`reference_count`),
    INDEX `idx_last_access_time` (`last_access_time`),
    FOREIGN KEY (`creator_user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件表（重构后）- 只保存物理文件信息';

-- 附件表 - 保存文件的业务属性
CREATE TABLE IF NOT EXISTS `attachment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `file_id` BIGINT NOT NULL COMMENT '文件ID（关联文件表）',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `business_type` VARCHAR(50) NOT NULL COMMENT '业务类型：resume/avatar/id_card/company_logo/job_attachment/other',
    `business_id` BIGINT COMMENT '业务ID',
    `description` VARCHAR(500) COMMENT '文件描述',
    `tags` VARCHAR(500) COMMENT '文件标签，逗号分隔',
    `is_public` TINYINT NOT NULL DEFAULT 0 COMMENT '是否公开：0-私有，1-公开',
    `is_temporary` TINYINT NOT NULL DEFAULT 0 COMMENT '是否临时文件：0-否，1-是',
    `expire_time` DATETIME COMMENT '过期时间',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常，2-已删除，3-已过期',
    `download_count` INT NOT NULL DEFAULT 0 COMMENT '下载次数',
    `view_count` INT NOT NULL DEFAULT 0 COMMENT '查看次数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_file_id` (`file_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_business_type` (`business_type`),
    INDEX `idx_business_id` (`business_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_is_temporary` (`is_temporary`),
    INDEX `idx_expire_time` (`expire_time`),
    INDEX `idx_create_time` (`create_time`),
    FOREIGN KEY (`file_id`) REFERENCES `file`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='附件表 - 保存文件的业务属性';

-- 消息表
CREATE TABLE IF NOT EXISTS `message` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(200) NOT NULL COMMENT '消息标题',
    `content` TEXT NOT NULL COMMENT '消息内容',
    `sender_id` BIGINT NOT NULL COMMENT '发送者ID',
    `receiver_id` BIGINT NOT NULL COMMENT '接收者ID',
    `type` TINYINT NOT NULL COMMENT '消息类型：1-系统消息，2-用户消息，3-岗位消息',
    `priority` TINYINT NOT NULL DEFAULT 1 COMMENT '优先级：1-低，2-中，3-高',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '消息状态：0-未读，1-已读，2-删除',
    `read_time` DATETIME COMMENT '阅读时间',
    `expire_time` DATETIME COMMENT '过期时间',
    `business_id` BIGINT COMMENT '关联的业务ID（如岗位ID、简历ID等）',
    `business_type` VARCHAR(50) COMMENT '业务类型',
    `template_id` BIGINT COMMENT '消息模板ID',
    `channel_type` VARCHAR(20) COMMENT '发送渠道类型：email-邮件，sms-短信，wechat-微信，push-推送',
    `send_result` TEXT COMMENT '发送结果（JSON格式，包含发送状态、错误信息等）',
    `attachments` TEXT COMMENT '附件信息（JSON格式）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_sender_id` (`sender_id`),
    INDEX `idx_receiver_id` (`receiver_id`),
    INDEX `idx_type` (`type`),
    INDEX `idx_status` (`status`),
    INDEX `idx_priority` (`priority`),
    INDEX `idx_expire_time` (`expire_time`),
    INDEX `idx_business` (`business_id`, `business_type`),
    INDEX `idx_template_id` (`template_id`),
    INDEX `idx_channel_type` (`channel_type`),
    INDEX `idx_create_time` (`create_time`),
    FOREIGN KEY (`sender_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`receiver_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`template_id`) REFERENCES `message_template`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';

-- 消息模板表
CREATE TABLE IF NOT EXISTS `message_template` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL COMMENT '模板名称',
    `channel_type` VARCHAR(20) NOT NULL COMMENT '模板类型：email-邮件，sms-短信，wechat-微信，push-推送',
    `subject` VARCHAR(200) COMMENT '邮件主题（仅邮件模板使用）',
    `content` TEXT NOT NULL COMMENT '模板内容',
    `variables` TEXT COMMENT '模板变量（JSON格式）',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '模板状态：0-禁用，1-启用',
    `description` VARCHAR(500) COMMENT '模板描述',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name_type` (`name`, `type`),
    INDEX `idx_type` (`type`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息模板表';


-- 设置数据库字符集（确保中文支持）
ALTER DATABASE employment_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
