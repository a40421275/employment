-- 权限管理模块初始化数据

-- 设置字符集（确保中文正确插入）
-- 在Spring Boot执行SQL脚本时，需要显式设置字符集
SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;
SET CHARACTER SET utf8mb4;
SET character_set_client = utf8mb4;
SET character_set_results = utf8mb4;
SET character_set_connection = utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

-- 设置会话字符集
SET SESSION character_set_client = utf8mb4;
SET SESSION character_set_results = utf8mb4;
SET SESSION character_set_connection = utf8mb4;
SET SESSION collation_connection = utf8mb4_unicode_ci;

-- 先删除已有数据（避免重复插入）
-- 注意删除顺序：先删除有外键约束的子表，再删除父表
DELETE FROM user_role;
DELETE FROM role_permission;
DELETE FROM user_profile;
DELETE FROM user;
DELETE FROM role;
DELETE FROM permission;
DELETE FROM job;
DELETE FROM job_tag_relation;
DELETE FROM job_tag;
-- 对于有自引用外键的job_category表，先删除子分类再删除父分类
DELETE FROM job_category WHERE parent_id IS NOT NULL; -- 先删除子分类
DELETE FROM job_category WHERE parent_id IS NULL; -- 再删除父分类
DELETE FROM company;
DELETE FROM system_config;
DELETE FROM attachment;
DELETE FROM file;
DELETE FROM job_apply;

-- 插入基础权限数据
INSERT INTO permission (id, name, code, description, type, parent_id, url, method, sort_order, icon, module, status, create_time, update_time) VALUES
-- 系统管理菜单 (type=1)
(1, '系统管理', 'system:manage', '系统管理菜单', 1, NULL, '/system', NULL, 1, 'setting', 'system', 1, NOW(), NOW()),
(2, '用户管理', 'user:manage', '用户管理菜单', 1, 1, '/system/user', NULL, 1, 'user', 'system', 1, NOW(), NOW()),
(3, '角色管理', 'role:manage', '角色管理菜单', 1, 1, '/system/role', NULL, 2, 'team', 'system', 1, NOW(), NOW()),
(4, '权限管理', 'permission:manage', '权限管理菜单', 1, 1, '/system/permission', NULL, 3, 'safety', 'system', 1, NOW(), NOW()),

-- 岗位管理菜单 (type=1)
(5, '岗位管理', 'job:manage', '岗位管理菜单', 1, NULL, '/job', NULL, 2, 'suitcase', 'job', 1, NOW(), NOW()),
(6, '岗位列表', 'job:list', '岗位列表菜单', 1, 5, '/job/list', NULL, 1, 'unordered-list', 'job', 1, NOW(), NOW()),
(7, '岗位分类', 'job:category', '岗位分类菜单', 1, 5, '/job/category', NULL, 2, 'appstore', 'job', 1, NOW(), NOW()),

-- 简历管理菜单 (type=1)
(8, '简历管理', 'resume:manage', '简历管理菜单', 1, NULL, '/resume', NULL, 3, 'file-text', 'resume', 1, NOW(), NOW()),
(9, '简历列表', 'resume:list', '简历列表菜单', 1, 8, '/resume/list', NULL, 1, 'unordered-list', 'resume', 1, NOW(), NOW()),

-- 用户按钮权限 (type=2)
(10, '新增用户', 'user:add', '新增用户按钮', 2, 2, NULL, NULL, 1, 'plus', 'user', 1, NOW(), NOW()),
(11, '编辑用户', 'user:edit', '编辑用户按钮', 2, 2, NULL, NULL, 2, 'edit', 'user', 1, NOW(), NOW()),
(12, '删除用户', 'user:delete', '删除用户按钮', 2, 2, NULL, NULL, 3, 'delete', 'user', 1, NOW(), NOW()),
(13, '重置密码', 'user:reset-password', '重置密码按钮', 2, 2, NULL, NULL, 4, 'key', 'user', 1, NOW(), NOW()),

-- 角色按钮权限 (type=2)
(14, '新增角色', 'role:add', '新增角色按钮', 2, 3, NULL, NULL, 1, 'plus', 'role', 1, NOW(), NOW()),
(15, '编辑角色', 'role:edit', '编辑角色按钮', 2, 3, NULL, NULL, 2, 'edit', 'role', 1, NOW(), NOW()),
(16, '删除角色', 'role:delete', '删除角色按钮', 2, 3, NULL, NULL, 3, 'delete', 'role', 1, NOW(), NOW()),
(17, '分配权限', 'role:assign-permission', '分配权限按钮', 2, 3, NULL, NULL, 4, 'safety-certificate', 'role', 1, NOW(), NOW()),

-- 权限按钮权限 (type=2)
(18, '新增权限', 'permission:add', '新增权限按钮', 2, 4, NULL, NULL, 1, 'plus', 'permission', 1, NOW(), NOW()),
(19, '编辑权限', 'permission:edit', '编辑权限按钮', 2, 4, NULL, NULL, 2, 'edit', 'permission', 1, NOW(), NOW()),
(20, '删除权限', 'permission:delete', '删除权限按钮', 2, 4, NULL, NULL, 3, 'delete', 'permission', 1, NOW(), NOW()),

-- 岗位按钮权限 (type=2)
(21, '新增岗位', 'job:add', '新增岗位按钮', 2, 6, NULL, NULL, 1, 'plus', 'job', 1, NOW(), NOW()),
(22, '编辑岗位', 'job:edit', '编辑岗位按钮', 2, 6, NULL, NULL, 2, 'edit', 'job', 1, NOW(), NOW()),
(23, '删除岗位', 'job:delete', '删除岗位按钮', 2, 6, NULL, NULL, 3, 'delete', 'job', 1, NOW(), NOW()),
(24, '发布岗位', 'job:publish', '发布岗位按钮', 2, 6, NULL, NULL, 4, 'rocket', 'job', 1, NOW(), NOW()),

-- 简历按钮权限 (type=2)
(25, '查看简历', 'resume:view', '查看简历按钮', 2, 9, NULL, NULL, 1, 'eye', 'resume', 1, NOW(), NOW()),
(26, '下载简历', 'resume:download', '下载简历按钮', 2, 9, NULL, NULL, 2, 'download', 'resume', 1, NOW(), NOW()),
(27, '推荐岗位', 'resume:recommend', '推荐岗位按钮', 2, 9, NULL, NULL, 3, 'like', 'resume', 1, NOW(), NOW()),

-- 接口权限 (type=3)
(28, '用户读取接口', 'user:read', '用户读取接口权限', 3, NULL, '/api/users/**', 'GET', 1, NULL, 'user', 1, NOW(), NOW()),
(29, '用户写入接口', 'user:write', '用户写入接口权限', 3, NULL, '/api/users/**', 'POST,PUT,DELETE', 2, NULL, 'user', 1, NOW(), NOW()),
(30, '角色读取接口', 'role:read', '角色读取接口权限', 3, NULL, '/api/permissions/roles/**', 'GET', 3, NULL, 'role', 1, NOW(), NOW()),
(31, '角色写入接口', 'role:write', '角色写入接口权限', 3, NULL, '/api/permissions/roles/**', 'POST,PUT,DELETE', 4, NULL, 'role', 1, NOW(), NOW()),
(32, '权限读取接口', 'permission:read', '权限读取接口权限', 3, NULL, '/api/permissions/**', 'GET', 5, NULL, 'permission', 1, NOW(), NOW()),
(33, '权限写入接口', 'permission:write', '权限写入接口权限', 3, NULL, '/api/permissions/**', 'POST,PUT,DELETE', 6, NULL, 'permission', 1, NOW(), NOW()),
(34, '岗位读取接口', 'job:read', '岗位读取接口权限', 3, NULL, '/api/jobs/**', 'GET', 7, NULL, 'job', 1, NOW(), NOW()),
(35, '岗位写入接口', 'job:write', '岗位写入接口权限', 3, NULL, '/api/jobs/**', 'POST,PUT,DELETE', 8, NULL, 'job', 1, NOW(), NOW()),
(36, '简历读取接口', 'resume:read', '简历读取接口权限', 3, NULL, '/api/resumes/**', 'GET', 9, NULL, 'resume', 1, NOW(), NOW()),
(37, '简历写入接口', 'resume:write', '简历写入接口权限', 3, NULL, '/api/resumes/**', 'POST,PUT,DELETE', 10, NULL, 'resume', 1, NOW(), NOW()),

-- 文件管理模块权限
(38, '文件管理', 'file:manage', '文件管理菜单', 1, NULL, '/file', NULL, 4, 'file', 'file', 1, NOW(), NOW()),
(39, '文件列表', 'file:list', '文件列表菜单', 1, 38, '/file/list', NULL, 1, 'unordered-list', 'file', 1, NOW(), NOW()),
(40, '文件上传', 'file:upload', '文件上传按钮', 2, 39, NULL, NULL, 1, 'upload', 'file', 1, NOW(), NOW()),
(41, '文件下载', 'file:download', '文件下载按钮', 2, 39, NULL, NULL, 2, 'download', 'file', 1, NOW(), NOW()),
(42, '文件删除', 'file:delete', '文件删除按钮', 2, 39, NULL, NULL, 3, 'delete', 'file', 1, NOW(), NOW()),
(43, '文件读取接口', 'file:read', '文件读取接口权限', 3, NULL, '/api/files/**', 'GET', 11, NULL, 'file', 1, NOW(), NOW()),
(44, '文件写入接口', 'file:write', '文件写入接口权限', 3, NULL, '/api/files/**', 'POST,PUT,DELETE', 12, NULL, 'file', 1, NOW(), NOW()),

-- 企业管理模块权限
(45, '企业管理', 'company:manage', '企业管理菜单', 1, NULL, '/company', NULL, 5, 'shop', 'company', 1, NOW(), NOW()),
(46, '企业列表', 'company:list', '企业列表菜单', 1, 45, '/company/list', NULL, 1, 'unordered-list', 'company', 1, NOW(), NOW()),
(47, '企业详情', 'company:detail', '企业详情菜单', 1, 45, '/company/detail', NULL, 2, 'profile', 'company', 1, NOW(), NOW()),
(48, '新增企业', 'company:add', '新增企业按钮', 2, 46, NULL, NULL, 1, 'plus', 'company', 1, NOW(), NOW()),
(49, '编辑企业', 'company:edit', '编辑企业按钮', 2, 46, NULL, NULL, 2, 'edit', 'company', 1, NOW(), NOW()),
(50, '删除企业', 'company:delete', '删除企业按钮', 2, 46, NULL, NULL, 3, 'delete', 'company', 1, NOW(), NOW()),
(51, '企业认证', 'company:auth', '企业认证按钮', 2, 46, NULL, NULL, 4, 'safety-certificate', 'company', 1, NOW(), NOW()),
(52, '企业读取接口', 'company:read', '企业读取接口权限', 3, NULL, '/api/companies/**', 'GET', 13, NULL, 'company', 1, NOW(), NOW()),
(53, '企业写入接口', 'company:write', '企业写入接口权限', 3, NULL, '/api/companies/**', 'POST,PUT,DELETE', 14, NULL, 'company', 1, NOW(), NOW()),

-- 系统配置模块权限
(54, '系统配置', 'system:config', '系统配置菜单', 1, 1, '/system/config', NULL, 4, 'setting', 'system', 1, NOW(), NOW()),
(55, '配置列表', 'config:list', '配置列表菜单', 1, 54, '/system/config/list', NULL, 1, 'unordered-list', 'system', 1, NOW(), NOW()),
(56, '新增配置', 'config:add', '新增配置按钮', 2, 55, NULL, NULL, 1, 'plus', 'system', 1, NOW(), NOW()),
(57, '编辑配置', 'config:edit', '编辑配置按钮', 2, 55, NULL, NULL, 2, 'edit', 'system', 1, NOW(), NOW()),
(58, '删除配置', 'config:delete', '删除配置按钮', 2, 55, NULL, NULL, 3, 'delete', 'system', 1, NOW(), NOW()),
(59, '配置读取接口', 'config:read', '配置读取接口权限', 3, NULL, '/api/system-configs/**', 'GET', 15, NULL, 'system', 1, NOW(), NOW()),
(60, '配置写入接口', 'config:write', '配置写入接口权限', 3, NULL, '/api/system-configs/**', 'POST,PUT,DELETE', 16, NULL, 'system', 1, NOW(), NOW()),

-- 岗位申请模块权限
(61, '岗位申请管理', 'job-apply:manage', '岗位申请管理菜单', 1, NULL, '/job-apply', NULL, 6, 'solution', 'job-apply', 1, NOW(), NOW()),
(62, '申请列表', 'job-apply:list', '申请列表菜单', 1, 61, '/job-apply/list', NULL, 1, 'unordered-list', 'job-apply', 1, NOW(), NOW()),
(63, '申请详情', 'job-apply:detail', '申请详情菜单', 1, 61, '/job-apply/detail', NULL, 2, 'profile', 'job-apply', 1, NOW(), NOW()),
(64, '处理申请', 'job-apply:process', '处理申请按钮', 2, 62, NULL, NULL, 1, 'check', 'job-apply', 1, NOW(), NOW()),
(65, '申请读取接口', 'job-apply:read', '申请读取接口权限', 3, NULL, '/api/job-applies/**', 'GET', 17, NULL, 'job-apply', 1, NOW(), NOW()),
(66, '申请写入接口', 'job-apply:write', '申请写入接口权限', 3, NULL, '/api/job-applies/**', 'POST,PUT,DELETE', 18, NULL, 'job-apply', 1, NOW(), NOW()),

-- 通知管理模块权限
(67, '通知管理', 'notification:manage', '通知管理菜单', 1, NULL, '/notification', NULL, 7, 'bell', 'notification', 1, NOW(), NOW()),
(68, '通知列表', 'notification:list', '通知列表菜单', 1, 67, '/notification/list', NULL, 1, 'unordered-list', 'notification', 1, NOW(), NOW()),
(69, '发送通知', 'notification:send', '发送通知按钮', 2, 68, NULL, NULL, 1, 'message', 'notification', 1, NOW(), NOW()),
(70, '通知读取接口', 'notification:read', '通知读取接口权限', 3, NULL, '/api/notifications/**', 'GET', 19, NULL, 'notification', 1, NOW(), NOW()),
(71, '通知写入接口', 'notification:write', '通知写入接口权限', 3, NULL, '/api/notifications/**', 'POST,PUT,DELETE', 20, NULL, 'notification', 1, NOW(), NOW()),

-- 分析统计模块权限
(72, '分析统计', 'analytics:manage', '分析统计菜单', 1, NULL, '/analytics', NULL, 8, 'bar-chart', 'analytics', 1, NOW(), NOW()),
(73, '数据统计', 'analytics:statistics', '数据统计菜单', 1, 72, '/analytics/statistics', NULL, 1, 'line-chart', 'analytics', 1, NOW(), NOW()),
(74, '报表导出', 'analytics:export', '报表导出按钮', 2, 73, NULL, NULL, 1, 'export', 'analytics', 1, NOW(), NOW()),
(75, '分析读取接口', 'analytics:read', '分析读取接口权限', 3, NULL, '/api/analytics/**', 'GET', 21, NULL, 'analytics', 1, NOW(), NOW()),
(76, '分析写入接口', 'analytics:write', '分析写入接口权限', 3, NULL, '/api/analytics/**', 'POST,PUT,DELETE', 22, NULL, 'analytics', 1, NOW(), NOW());

-- 插入基础角色数据
INSERT INTO role (id, name, code, description, type, status, create_time, update_time) VALUES
(1, '超级管理员', 'ROLE_SUPER_ADMIN', '系统超级管理员，拥有所有权限', 1, 1, NOW(), NOW()),
(2, '管理员', 'ROLE_ADMIN', '系统管理员，拥有大部分管理权限', 1, 1, NOW(), NOW()),
(3, '企业用户', 'ROLE_EMPLOYER', '企业用户，可以管理岗位和查看简历', 2, 1, NOW(), NOW()),
(4, '普通用户', 'ROLE_USER', '普通用户，可以查看岗位和投递简历', 3, 1, NOW(), NOW());

-- 为超级管理员角色分配所有权限
INSERT INTO role_permission (role_id, permission_id, create_time) 
SELECT 1, id, NOW() FROM permission;

-- 为管理员角色分配管理权限（排除超级管理员权限）
INSERT INTO role_permission (role_id, permission_id, create_time) 
SELECT 2, id, NOW() FROM permission 
WHERE code NOT LIKE '%super%' AND code NOT IN ('system:manage');

-- 为企业用户角色分配岗位和简历相关权限
INSERT INTO role_permission (role_id, permission_id, create_time) 
SELECT 3, id, NOW() FROM permission 
WHERE module IN ('job', 'resume', 'company', 'file') OR code IN ('job:read', 'job:write', 'resume:read', 'company:read', 'company:write', 'file:read', 'file:write');

-- 为普通用户角色分配基本权限
INSERT INTO role_permission (role_id, permission_id, create_time) 
SELECT 4, id, NOW() FROM permission 
WHERE code IN ('job:read', 'resume:write', 'user:read', 'file:read', 'file:write', 'company:read');

-- 插入测试企业数据
INSERT INTO company (id, company_name, company_short_name, company_type, industry, scale, legal_person, registered_capital, establish_date, business_license_no, province, city, address, contact_person, contact_phone, contact_email, introduction, status, auth_status, job_count, view_count, favorite_count, apply_count, create_time, update_time) VALUES
(1, '示例科技有限公司', '示例科技', 1, '互联网', 3, '张三', 1000.00, '2020-01-01', '123456789012345', '北京市', '北京市', '中关村大街1号', '李四', '13800138000', 'contact@example.com', '专注于互联网技术开发的公司', 1, 2, 0, 0, 0, 0, NOW(), NOW()),
(2, '测试企业有限公司', '测试企业', 1, '软件', 2, '王五', 500.00, '2018-05-15', '987654321098765', '上海市', '上海市', '浦东新区张江高科技园区', '赵六', '13900139000', 'hr@test.com', '专业的软件开发和咨询服务', 1, 2, 0, 0, 0, 0, NOW(), NOW());

-- 插入管理员用户（密码为qwe123!@#的BCrypt加密）
INSERT INTO user (id, username, password, email, phone, user_type, status, auth_status, company_id, create_time, update_time) VALUES
(1, 'admin', '$2a$10$kOsvt708pMY6iUuU/eigc.w.Nbu.gD8Sxb2bCwy2nq8wkXVk7/TO2', 'admin@shera.com', '13800138000', 3, 1, 2, NULL, NOW(), NOW()),
(2, 'manager', '$2a$10$kOsvt708pMY6iUuU/eigc.w.Nbu.gD8Sxb2bCwy2nq8wkXVk7/TO2', 'manager@shera.com', '13800138001', 3, 1, 2, NULL, NOW(), NOW()),
(3, 'employer', '$2a$10$kOsvt708pMY6iUuU/eigc.w.Nbu.gD8Sxb2bCwy2nq8wkXVk7/TO2', 'employer@shera.com', '13800138002', 2, 1, 2, 1, NOW(), NOW()),
(4, 'user', '$2a$10$kOsvt708pMY6iUuU/eigc.w.Nbu.gD8Sxb2bCwy2nq8wkXVk7/TO2', 'user@shera.com', '13800138003', 1, 1, 2, NULL, NOW(), NOW());

-- 为用户分配角色
INSERT INTO user_role (user_id, role_id, create_time) VALUES
(1, 1, NOW()), -- admin -> 超级管理员
(2, 2, NOW()), -- manager -> 管理员
(3, 3, NOW()), -- employer -> 企业用户
(4, 4, NOW()); -- user -> 普通用户

-- 插入用户详细信息
INSERT INTO user_profile (user_id, real_name, gender, birthday, education, work_years, skills, create_time, update_time) VALUES
(1, '系统管理员', 1, '1990-01-01', '本科', 5, '系统管理,权限管理', NOW(), NOW()),
(2, '业务经理', 1, '1985-05-15', '硕士', 8, '项目管理,团队管理', NOW(), NOW()),
(3, '企业招聘专员', 2, '1992-08-20', '本科', 3, '招聘,人力资源管理', NOW(), NOW()),
(4, '求职用户', 1, '1995-12-10', '本科', 2, 'Java,Spring Boot', NOW(), NOW());

-- 插入系统预定义标签（独立标签）
INSERT IGNORE INTO `job_tag` (`tag_name`, `tag_type`, `tag_color`, `description`) VALUES
('急招', 0, '#ff6b6b', '急需招聘的岗位'),
('高薪', 0, '#ff9e2c', '薪资待遇优厚'),
('弹性工作', 0, '#4ecdc4', '工作时间灵活'),
('远程办公', 0, '#45b7d1', '支持远程工作'),
('实习', 0, '#96ceb4', '实习岗位'),
('兼职', 0, '#feca57', '兼职岗位'),
('全职', 0, '#ff9ff3', '全职岗位'),
('创业公司', 0, '#54a0ff', '创业型公司'),
('大厂', 0, '#5f27cd', '大型知名企业'),
('技术驱动', 0, '#00d2d3', '技术导向型公司'),
('成长空间大', 0, '#ff9f43', '职业发展空间广阔'),
('福利待遇好', 0, '#10ac84', '福利待遇优厚'),
('培训体系完善', 0, '#0abde3', '完善的培训体系'),
('团队氛围好', 0, '#ee5a24', '良好的团队氛围'),
('领导好', 0, '#a3cb38', '优秀的领导团队'),
('加班少', 0, '#1289a7', '加班较少'),
('年终奖', 0, '#d980fa', '提供年终奖金'),
('股票期权', 0, '#f368e0', '提供股票期权'),
('五险一金', 0, '#ff9f43', '完善的社会保障'),
('餐补', 0, '#ee5a24', '提供餐费补贴'),
('交通补贴', 0, '#a3cb38', '提供交通补贴'),
('住房补贴', 0, '#1289a7', '提供住房补贴');

-- 插入初始数据 - 先插入父级分类
INSERT IGNORE INTO `job_category` (`id`, `name`, `parent_id`, `level`, `sort_order`, `status`) VALUES
(1, '技术', NULL, 1, 1, 1),
(2, '产品', NULL, 1, 2, 1),
(3, '设计', NULL, 1, 3, 1),
(4, '运营', NULL, 1, 4, 1),
(5, '市场', NULL, 1, 5, 1),
(6, '销售', NULL, 1, 6, 1),
(7, '职能', NULL, 1, 7, 1);

-- 插入子级分类
INSERT IGNORE INTO `job_category` (`id`, `name`, `parent_id`, `level`, `sort_order`, `status`) VALUES
(8, '后端开发', 1, 2, 1, 1),
(9, '前端开发', 1, 2, 2, 1),
(10, '移动开发', 1, 2, 3, 1),
(11, '测试', 1, 2, 4, 1),
(12, '运维', 1, 2, 5, 1),
(13, '数据', 1, 2, 6, 1);

INSERT IGNORE INTO `admin_user` (`id`, `username`, `password`, `real_name`, `role`) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTV5UiC', '超级管理员', 'super_admin');

INSERT IGNORE INTO `system_config` (`config_key`, `config_value`, `description`, `config_group`, `value_type`, `editable`, `system_config`, `sort_order`, `validation_rule`, `default_value`, `options`) VALUES
-- 网站配置
('site_name', '就业服务平台', '网站名称', 'site', 'STRING', 1, 0, 1, '', '就业服务平台', ''),
('site_description', '专业的就业服务平台', '网站描述', 'site', 'STRING', 1, 0, 2, '', '专业的就业服务平台', ''),
('site_keywords', '就业,招聘,求职,岗位', '网站关键词', 'site', 'STRING', 1, 0, 3, '', '就业,招聘,求职,岗位', ''),
('site_logo', '/images/logo.png', '网站Logo', 'site', 'STRING', 1, 0, 4, '', '/images/logo.png', ''),
('site_favicon', '/images/favicon.ico', '网站图标', 'site', 'STRING', 1, 0, 5, '', '/images/favicon.ico', ''),
('site_copyright', '© 2025 就业服务平台', '版权信息', 'site', 'STRING', 1, 0, 6, '', '© 2025 就业服务平台', ''),
('site_icp', '京ICP备12345678号', 'ICP备案号', 'site', 'STRING', 1, 0, 7, '', '京ICP备12345678号', ''),
('site_contact', 'contact@example.com', '联系邮箱', 'site', 'STRING', 1, 0, 8, '', 'contact@example.com', ''),
('site_address', '北京市朝阳区', '联系地址', 'site', 'STRING', 1, 0, 9, '', '北京市朝阳区', ''),

-- 安全配置
('security_login_attempts', '5', '最大登录尝试次数', 'security', 'INTEGER', 1, 0, 1, 'min:1|max:10', '5', ''),
('security_lockout_time', '30', '账户锁定时间（分钟）', 'security', 'INTEGER', 1, 0, 2, 'min:1|max:1440', '30', ''),
('security_password_min_length', '8', '密码最小长度', 'security', 'INTEGER', 1, 0, 3, 'min:6|max:20', '8', ''),
('security_password_complexity', 'medium', '密码复杂度要求', 'security', 'STRING', 1, 0, 4, '', 'medium', '["low","medium","high"]'),

-- 邮件配置
('email_enabled', 'false', '邮件服务是否启用', 'email', 'BOOLEAN', 1, 0, 1, '', 'false', ''),
('email_host', 'smtp.example.com', '邮件服务器地址', 'email', 'STRING', 1, 0, 2, '', 'smtp.example.com', ''),
('email_port', '587', '邮件服务器端口', 'email', 'INTEGER', 1, 0, 3, 'min:1|max:65535', '587', ''),
('email_username', 'noreply@example.com', '邮件用户名', 'email', 'STRING', 1, 0, 4, '', 'noreply@example.com', ''),
('email_password', '', '邮件密码', 'email', 'STRING', 1, 0, 5, '', '', ''),
('email_from', 'noreply@example.com', '发件人邮箱', 'email', 'STRING', 1, 0, 6, '', 'noreply@example.com', ''),

-- 短信配置
('sms_enabled', 'false', '短信服务是否启用', 'sms', 'BOOLEAN', 1, 0, 1, '', 'false', ''),
('sms_provider', 'aliyun', '短信服务提供商', 'sms', 'STRING', 1, 0, 2, '', 'aliyun', '["aliyun","tencent","huawei"]'),
('sms_access_key', '', '短信Access Key', 'sms', 'STRING', 1, 0, 3, '', '', ''),
('sms_secret_key', '', '短信Secret Key', 'sms', 'STRING', 1, 0, 4, '', '', ''),
('sms_sign_name', '就业平台', '短信签名', 'sms', 'STRING', 1, 0, 5, '', '就业平台', ''),


-- 通知配置
('notification_enabled', 'true', '通知服务是否启用', 'notification', 'BOOLEAN', 1, 0, 1, '', 'true', ''),
('notification_types', 'email,sms,web', '通知类型', 'notification', 'STRING', 1, 0, 2, '', 'email,sms,web', '["email","sms","web"]'),
('notification_push_interval', '60', '通知推送间隔（秒）', 'notification', 'INTEGER', 1, 0, 3, 'min:1|max:3600', '60', ''),

-- 分析配置
('analytics_enabled', 'false', '分析服务是否启用', 'analytics', 'BOOLEAN', 1, 0, 1, '', 'false', ''),
('analytics_provider', 'google', '分析服务提供商', 'analytics', 'STRING', 1, 0, 2, '', 'google', '["google","baidu","cnzz"]'),
('analytics_tracking_id', '', '分析跟踪ID', 'analytics', 'STRING', 1, 0, 3, '', '', ''),

-- 缓存配置
('cache_enabled', 'true', '缓存服务是否启用', 'cache', 'BOOLEAN', 1, 0, 1, '', 'true', ''),
('cache_ttl', '3600', '缓存过期时间（秒）', 'cache', 'INTEGER', 1, 0, 2, 'min:1|max:86400', '3600', ''),
('cache_max_size', '1000', '缓存最大条目数', 'cache', 'INTEGER', 1, 0, 3, 'min:10|max:10000', '1000', ''),

-- 备份配置
('backup_enabled', 'false', '备份服务是否启用', 'backup', 'BOOLEAN', 1, 0, 1, '', 'false', ''),
('backup_interval', '86400', '备份间隔（秒）', 'backup', 'INTEGER', 1, 0, 2, 'min:3600|max:2592000', '86400', ''),
('backup_retention', '30', '备份保留天数', 'backup', 'INTEGER', 1, 0, 3, 'min:1|max:365', '30', ''),

-- 业务配置
('resume_privacy_default', '1', '简历默认隐私级别', 'business', 'INTEGER', 1, 0, 1, 'min:0|max:2', '1', '["0:公开","1:仅企业可见","2:仅自己可见"]'),
('job_expire_days', '30', '岗位默认过期天数', 'business', 'INTEGER', 1, 0, 2, 'min:1|max:365', '30', ''),
('match_threshold', '0.7', '简历岗位匹配阈值', 'business', 'DOUBLE', 1, 0, 3, 'min:0.1|max:1.0', '0.7', ''),
('max_resume_count', '3', '用户最大简历数量', 'business', 'INTEGER', 1, 0, 4, 'min:1|max:10', '3', ''),
('max_job_apply_count', '10', '用户最大申请数量', 'business', 'INTEGER', 1, 0, 5, 'min:1|max:50', '10', '');

-- 插入岗位信息初始化数据
INSERT IGNORE INTO `job` (
    `id`, `title`, `category_id`, `company_id`, `department`, `job_type`, 
    `salary_min`, `salary_max`, `salary_unit`, `work_city`, `work_address`, 
    `work_district`, `work_latitude`, `work_longitude`, `description`, `requirements`, 
    `benefits`, `contact_info`, `education_requirement`, `experience_requirement`, 
    `recruit_number`, `urgent_level`, `priority_level`, `is_recommended`, `recommend_reason`, 
    `keywords`, `status`, `view_count`, `apply_count`, `favorite_count`, 
    `publish_time`, `expire_time`, `create_time`, `update_time`
) VALUES
-- 示例科技公司的岗位
(1, 'Java后端开发工程师', 8, 1, '技术部', 1, 
 15000.00, 25000.00, '月', '北京市', '中关村大街1号', 
 '海淀区', 39.983424, 116.322987, 
 '负责公司核心业务系统的后端开发工作，参与系统架构设计和优化。',
 '1. 3年以上Java开发经验，熟悉Spring Boot、Spring Cloud等框架\n2. 熟悉MySQL、Redis等数据库技术\n3. 熟悉分布式系统设计，有微服务架构经验者优先\n4. 具备良好的编码习惯和团队协作能力',
 '1. 五险一金、补充医疗保险\n2. 年终奖、项目奖金\n3. 弹性工作制、带薪年假\n4. 技术培训、职业发展通道',
 '{"contactPerson": "张经理", "phone": "13800138000", "email": "hr@example.com"}',
 '本科', '3-5年', 3, 1, 1, TRUE, '技术实力强，发展空间大',
 'Java Spring Boot 微服务 后端开发', 1, 156, 23, 12,
 '2024-11-01 09:00:00', '2024-12-31 23:59:59', NOW(), NOW()),

(2, '前端开发工程师', 9, 1, '技术部', 1,
 12000.00, 20000.00, '月', '北京市', '中关村大街1号',
 '海淀区', 39.983424, 116.322987,
 '负责公司产品的前端开发工作，与设计师和后端工程师紧密合作。',
 '1. 2年以上前端开发经验，熟练掌握Vue.js或React\n2. 熟悉HTML5、CSS3、JavaScript等前端技术\n3. 有移动端开发经验者优先\n4. 具备良好的沟通能力和团队协作精神',
 '1. 五险一金、补充医疗保险\n2. 年终奖、项目奖金\n3. 弹性工作制、带薪年假\n4. 技术分享、学习资源支持',
 '{"contactPerson": "李主管", "phone": "13800138001", "email": "hr@example.com"}',
 '本科', '2-4年', 2, 0, 1, FALSE, NULL,
 '前端 Vue.js React JavaScript', 1, 89, 15, 8,
 '2024-11-05 10:00:00', '2024-12-31 23:59:59', NOW(), NOW()),

(3, 'Android开发工程师', 10, 1, '移动开发部', 1,
 13000.00, 22000.00, '月', '北京市', '中关村大街1号',
 '海淀区', 39.983424, 116.322987,
 '负责公司Android客户端的设计、开发和维护工作。',
 '1. 2年以上Android开发经验，熟悉Java/Kotlin\n2. 熟悉Android SDK和常用框架\n3. 有性能优化经验者优先\n4. 具备独立开发能力，有上架应用经验',
 '1. 五险一金、补充医疗保险\n2. 年终奖、项目奖金\n3. 弹性工作制、带薪年假\n4. 技术培训、职业发展',
 '{"contactPerson": "王经理", "phone": "13800138002", "email": "hr@example.com"}',
 '本科', '2-4年', 2, 0, 1, TRUE, '技术氛围好，成长空间大',
 'Android Java Kotlin 移动开发', 1, 67, 11, 5,
 '2024-11-10 14:00:00', '2024-12-31 23:59:59', NOW(), NOW()),

-- 测试企业的岗位
(4, '软件测试工程师', 11, 2, '质量保障部', 1,
 10000.00, 18000.00, '月', '上海市', '浦东新区张江高科技园区',
 '浦东新区', 31.204737, 121.601091,
 '负责软件产品的测试工作，确保产品质量。',
 '1. 2年以上软件测试经验\n2. 熟悉测试流程和方法，掌握测试用例设计\n3. 熟悉自动化测试工具者优先\n4. 具备良好的沟通能力和责任心',
 '1. 五险一金、补充医疗保险\n2. 年终奖、项目奖金\n3. 弹性工作制、带薪年假\n4. 培训学习、晋升机会',
 '{"contactPerson": "赵主管", "phone": "13900139000", "email": "hr@test.com"}',
 '大专', '2-4年', 3, 1, 1, FALSE, NULL,
 '软件测试 自动化测试 质量保障', 1, 45, 8, 3,
 '2024-11-08 09:30:00', '2024-12-31 23:59:59', NOW(), NOW()),

(5, '运维工程师', 12, 2, '运维部', 1,
 12000.00, 20000.00, '月', '上海市', '浦东新区张江高科技园区',
 '浦东新区', 31.204737, 121.601091,
 '负责公司服务器和网络设备的运维管理工作。',
 '1. 2年以上运维经验，熟悉Linux系统\n2. 熟悉Docker、Kubernetes等容器技术\n3. 熟悉网络配置和故障排查\n4. 具备良好的问题解决能力',
 '1. 五险一金、补充医疗保险\n2. 年终奖、项目奖金\n3. 弹性工作制、带薪年假\n4. 技术培训、职业发展',
 '{"contactPerson": "钱经理", "phone": "13900139001", "email": "hr@test.com"}',
 '本科', '2-4年', 2, 0, 1, TRUE, '技术氛围浓厚，发展前景好',
 '运维 Linux Docker Kubernetes', 1, 78, 12, 6,
 '2024-11-12 11:00:00', '2024-12-31 23:59:59', NOW(), NOW()),

-- 不同状态的岗位用于测试
(6, '产品经理（草稿）', 2, 1, '产品部', 1,
 15000.00, 25000.00, '月', '北京市', '中关村大街1号',
 '海淀区', 39.983424, 116.322987,
 '负责产品规划、设计和迭代工作。',
 '1. 3年以上产品经理经验\n2. 熟悉产品设计流程和工具\n3. 具备良好的沟通和协调能力\n4. 有互联网产品经验者优先',
 '1. 五险一金、补充医疗保险\n2. 年终奖、项目奖金\n3. 弹性工作制、带薪年假\n4. 产品培训、职业发展',
 '{"contactPerson": "孙总监", "phone": "13800138003", "email": "hr@example.com"}',
 '本科', '3-5年', 1, 0, 1, FALSE, NULL,
 '产品经理 产品设计 需求分析', 0, 0, 0, 0,
 NULL, '2024-12-31 23:59:59', NOW(), NOW()),

(7, 'UI设计师（已下架）', 3, 2, '设计部', 1,
 10000.00, 18000.00, '月', '上海市', '浦东新区张江高科技园区',
 '浦东新区', 31.204737, 121.601091,
 '负责产品的界面设计和用户体验优化。',
 '1. 2年以上UI设计经验\n2. 熟练使用Sketch、Figma等设计工具\n3. 具备良好的审美和创意能力\n4. 有移动端设计经验者优先',
 '1. 五险一金、补充医疗保险\n2. 年终奖、项目奖金\n3. 弹性工作制、带薪年假\n4. 设计培训、职业发展',
 '{"contactPerson": "周主管", "phone": "13900139002", "email": "hr@test.com"}',
 '本科', '2-4年', 1, 0, 1, FALSE, NULL,
 'UI设计 用户体验 界面设计', 2, 34, 5, 2,
 '2024-10-15 10:00:00', '2024-11-15 23:59:59', NOW(), NOW()),

-- 更多测试岗位
(8, '数据分析师', 13, 1, '数据部', 1,
 14000.00, 23000.00, '月', '北京市', '中关村大街1号',
 '海淀区', 39.983424, 116.322987,
 '负责业务数据的分析和挖掘工作。',
 '1. 2年以上数据分析经验\n2. 熟悉SQL、Python等数据分析工具\n3. 熟悉数据可视化工具\n4. 具备良好的业务理解能力',
 '1. 五险一金、补充医疗保险\n2. 年终奖、项目奖金\n3. 弹性工作制、带薪年假\n4. 数据培训、职业发展',
 '{"contactPerson": "吴经理", "phone": "13800138004", "email": "hr@example.com"}',
 '本科', '2-4年', 2, 0, 1, TRUE, '数据驱动，发展前景广阔',
 '数据分析 SQL Python 数据挖掘', 1, 92, 18, 9,
 '2024-11-15 13:00:00', '2024-12-31 23:59:59', NOW(), NOW()),

(9, '销售专员', 6, 2, '销售部', 1,
 8000.00, 15000.00, '月', '上海市', '浦东新区张江高科技园区',
 '浦东新区', 31.204737, 121.601091,
 '负责公司产品的销售和客户维护工作。',
 '1. 1年以上销售经验\n2. 具备良好的沟通和谈判能力\n3. 有软件销售经验者优先\n4. 积极主动，有团队合作精神',
 '1. 五险一金、补充医疗保险\n2. 绩效奖金、提成\n3. 带薪培训、晋升机会\n4. 团队活动、节日福利',
 '{"contactPerson": "郑主管", "phone": "13900139003", "email": "hr@test.com"}',
 '大专', '1-3年', 5, 1, 1, FALSE, NULL,
 '销售 客户维护 商务拓展', 1, 56, 9, 4,
 '2024-11-18 09:00:00', '2024-12-31 23:59:59', NOW(), NOW()),

(10, '运营专员', 4, 1, '运营部', 1,
 9000.00, 16000.00, '月', '北京市', '中关村大街1号',
 '海淀区', 39.983424, 116.322987,
 '负责产品运营和用户增长工作。',
 '1. 1年以上运营经验\n2. 熟悉运营工具和方法\n3. 具备数据分析和文案能力\n4. 有互联网运营经验者优先',
 '1. 五险一金、补充医疗保险\n2. 绩效奖金、项目奖金\n3. 弹性工作制、带薪年假\n4. 运营培训、职业发展',
 '{"contactPerson": "冯经理", "phone": "13800138005", "email": "hr@example.com"}',
 '本科', '1-3年', 3, 0, 1, TRUE, '运营体系完善，成长空间大',
 '运营 用户增长 数据分析', 1, 73, 14, 7,
 '2024-11-20 14:30:00', '2024-12-31 23:59:59', NOW(), NOW());

-- 插入岗位标签关联关系（多对多关系）
-- 注意：这里假设job_tag表中已经插入了系统标签，我们需要先获取标签ID
-- 为岗位1（Java后端开发工程师）添加标签
INSERT IGNORE INTO `job_tag_relation` (`job_id`, `tag_id`, `create_time`) 
SELECT 1, id, NOW() FROM `job_tag` WHERE `tag_name` IN ('高薪', '全职', '大厂', '技术驱动', '成长空间大', '福利待遇好');

-- 为岗位2（前端开发工程师）添加标签
INSERT IGNORE INTO `job_tag_relation` (`job_id`, `tag_id`, `create_time`) 
SELECT 2, id, NOW() FROM `job_tag` WHERE `tag_name` IN ('弹性工作', '全职', '技术驱动', '成长空间大', '团队氛围好');

-- 为岗位3（Android开发工程师）添加标签
INSERT IGNORE INTO `job_tag_relation` (`job_id`, `tag_id`, `create_time`) 
SELECT 3, id, NOW() FROM `job_tag` WHERE `tag_name` IN ('高薪', '全职', '大厂', '技术驱动', '年终奖', '五险一金');

-- 为岗位4（软件测试工程师）添加标签
INSERT IGNORE INTO `job_tag_relation` (`job_id`, `tag_id`, `create_time`) 
SELECT 4, id, NOW() FROM `job_tag` WHERE `tag_name` IN ('急招', '全职', '培训体系完善', '团队氛围好', '加班少');

-- 为岗位5（运维工程师）添加标签
INSERT IGNORE INTO `job_tag_relation` (`job_id`, `tag_id`, `create_time`) 
SELECT 5, id, NOW() FROM `job_tag` WHERE `tag_name` IN ('高薪', '全职', '技术驱动', '成长空间大', '年终奖', '五险一金');

-- 为岗位8（数据分析师）添加标签
INSERT IGNORE INTO `job_tag_relation` (`job_id`, `tag_id`, `create_time`) 
SELECT 8, id, NOW() FROM `job_tag` WHERE `tag_name` IN ('高薪', '全职', '大厂', '技术驱动', '成长空间大', '年终奖');

-- 为岗位9（销售专员）添加标签
INSERT IGNORE INTO `job_tag_relation` (`job_id`, `tag_id`, `create_time`) 
SELECT 9, id, NOW() FROM `job_tag` WHERE `tag_name` IN ('急招', '全职', '绩效奖金', '团队氛围好', '晋升机会');

-- 为岗位10（运营专员）添加标签
INSERT IGNORE INTO `job_tag_relation` (`job_id`, `tag_id`, `create_time`) 
SELECT 10, id, NOW() FROM `job_tag` WHERE `tag_name` IN ('全职', '成长空间大', '团队氛围好', '培训体系完善', '五险一金');

-- 更新标签使用次数（基于关联关系统计）
UPDATE `job_tag` SET `use_count` = (
    SELECT COUNT(*) FROM `job_tag_relation` WHERE `job_tag_relation`.`tag_id` = `job_tag`.`id`
) WHERE `id` IN (SELECT `tag_id` FROM `job_tag_relation`);

-- 文件管理模块初始化数据

-- 插入文件管理相关的系统配置
INSERT IGNORE INTO `system_config` (`config_key`, `config_value`, `description`, `config_group`, `value_type`, `editable`, `system_config`, `sort_order`, `validation_rule`, `default_value`, `options`) VALUES
-- 文件上传配置
('file.upload.max_size', '104857600', '文件上传最大大小（字节）', 'file', 'INTEGER', 1, 0, 1, 'min:1024|max:1073741824', '104857600', ''),
('file.upload.max_request_size', '104857600', '请求最大大小（字节）', 'file', 'INTEGER', 1, 0, 2, 'min:1024|max:1073741824', '104857600', ''),
('file.upload.allowed_types', 'jpg,jpeg,png,gif,pdf,doc,docx,txt', '允许上传的文件类型', 'file', 'STRING', 1, 0, 3, '', 'jpg,jpeg,png,gif,pdf,doc,docx,txt', ''),
('file.upload.storage_type', 'local', '文件存储类型', 'file', 'STRING', 1, 0, 4, '', 'local', '["local","oss","cos"]'),
('file.upload.storage_path', 'E:/data/', '文件存储路径', 'file', 'STRING', 1, 0, 5, '', 'uploads/', ''),
('file_download_signer.expiry_seconds', '3600', '文件下载过期时间（秒）', 'file', 'INTEGER', 1, 0, 5, 'min:1', '3600', ''),
('file.upload.cleanup_days', '30', '临时文件清理天数', 'file', 'INTEGER', 1, 0, 6, 'min:1|max:365', '30', '');

-- 插入文件表测试数据
INSERT IGNORE INTO `file` (
    `id`, `file_name`, `original_name`, `file_path`, `file_size`, `file_type`, 
    `mime_type`, `file_extension`, `file_hash`, `creator_user_id`, `reference_count`, 
    `status`, `last_access_time`, `create_time`, `update_time`
) VALUES
-- 用户头像文件
(1, '1_avatar_1700000000000_123.jpg', 'avatar.jpg', 'uploads/1/1_avatar_1700000000000_123.jpg', 102400, 'avatar', 
 'image/jpeg', 'jpg', 'e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855', 1, 1, 
 1, NOW(), NOW(), NOW()),
(2, '2_avatar_1700000001000_456.jpg', 'profile.jpg', 'uploads/2/2_avatar_1700000001000_456.jpg', 153600, 'avatar', 
 'image/jpeg', 'jpg', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 2, 1, 
 1, NOW(), NOW(), NOW()),
(3, '3_avatar_1700000002000_789.jpg', 'photo.jpg', 'uploads/3/3_avatar_1700000002000_789.jpg', 204800, 'avatar', 
 'image/jpeg', 'jpg', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 3, 1, 
 1, NOW(), NOW(), NOW()),
(4, '4_avatar_1700000003000_012.jpg', 'user_photo.jpg', 'uploads/4/4_avatar_1700000003000_012.jpg', 128000, 'avatar', 
 'image/jpeg', 'jpg', '6cf615d5bcaac778352a8f1f3360d23f02f34ec182e259897fd6ce485d7870d4', 4, 1, 
 1, NOW(), NOW(), NOW()),

-- 简历文件
(5, '1_resume_1700000004000_345.pdf', '张三_简历.pdf', 'uploads/1/1_resume_1700000004000_345.pdf', 204800, 'resume', 
 'application/pdf', 'pdf', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 1, 1, 
 1, NOW(), NOW(), NOW()),
(6, '2_resume_1700000005000_678.pdf', '李四_简历.pdf', 'uploads/2/2_resume_1700000005000_678.pdf', 256000, 'resume', 
 'application/pdf', 'pdf', '598d4c200461b81522a3328565c25f7c4d71d4c3b7b8b9aadc7e8e9a8b7c6d5e', 2, 1, 
 1, NOW(), NOW(), NOW()),
(7, '3_resume_1700000006000_901.pdf', '王五_简历.pdf', 'uploads/3/3_resume_1700000006000_901.pdf', 307200, 'resume', 
 'application/pdf', 'pdf', '7c4a8d09ca3762af61e59520943dc26494f8941b76a6c8e7b8a8e9a8b7c6d5e', 3, 1, 
 1, NOW(), NOW(), NOW()),
(8, '4_resume_1700000007000_234.pdf', '赵六_简历.pdf', 'uploads/4/4_resume_1700000007000_234.pdf', 179200, 'resume', 
 'application/pdf', 'pdf', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 4, 1, 
 1, NOW(), NOW(), NOW()),

-- 企业Logo文件
(9, '1_company_logo_1700000008000_567.png', 'example_logo.png', 'uploads/1/1_company_logo_1700000008000_567.png', 51200, 'company_logo', 
 'image/png', 'png', '15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225', 1, 1, 
 1, NOW(), NOW(), NOW()),
(10, '2_company_logo_1700000009000_890.png', 'test_logo.png', 'uploads/2/2_company_logo_1700000009000_890.png', 76800, 'company_logo', 
 'image/png', 'png', '25d55ad283aa400af464c76d713c07adf2f0f674f6f8e7b8a8e9a8b7c6d5e', 2, 1, 
 1, NOW(), NOW(), NOW()),

-- 岗位附件文件
(11, '1_job_attachment_1700000010000_123.docx', '岗位要求.docx', 'uploads/1/1_job_attachment_1700000010000_123.docx', 102400, 'job_attachment', 
 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 'docx', '35a9e381b1a27567549b5f8a6f783c167ebf809f1c4d6a9e3761a8e917e5d4e4', 1, 1, 
 1, NOW(), NOW(), NOW()),
(12, '2_job_attachment_1700000011000_456.pdf', '公司介绍.pdf', 'uploads/2/2_job_attachment_1700000011000_456.pdf', 153600, 'job_attachment', 
 'application/pdf', 'pdf', '45a9e381b1a27567549b5f8a6f783c167ebf809f1c4d6a9e3761a8e917e5d4e4', 2, 1, 
 1, NOW(), NOW(), NOW()),

-- 身份证文件
(13, '1_id_card_1700000012000_789.jpg', '身份证正面.jpg', 'uploads/1/1_id_card_1700000012000_789.jpg', 102400, 'id_card', 
 'image/jpeg', 'jpg', '55a9e381b1a27567549b5f8a6f783c167ebf809f1c4d6a9e3761a8e917e5d4e4', 1, 1, 
 1, NOW(), NOW(), NOW()),
(14, '1_id_card_1700000013000_012.jpg', '身份证反面.jpg', 'uploads/1/1_id_card_1700000013000_012.jpg', 102400, 'id_card', 
 'image/jpeg', 'jpg', '65a9e381b1a27567549b5f8a6f783c167ebf809f1c4d6a9e3761a8e917e5d4e4', 1, 1, 
 1, NOW(), NOW(), NOW()),

-- 其他文件
(15, '1_other_1700000014000_345.txt', '说明文档.txt', 'uploads/1/1_other_1700000014000_345.txt', 5120, 'other', 
 'text/plain', 'txt', '75a9e381b1a27567549b5f8a6f783c167ebf809f1c4d6a9e3761a8e917e5d4e4', 1, 1, 
 1, NOW(), NOW(), NOW());

-- 插入附件表测试数据
INSERT IGNORE INTO `attachment` (
    `id`, `file_id`, `user_id`, `business_type`, `business_id`, `description`, 
    `tags`, `is_public`, `is_temporary`, `expire_time`, `status`, 
    `download_count`, `view_count`, `create_time`, `update_time`
) VALUES
-- 用户头像附件
(1, 1, 1, 'user', 1, '管理员头像', 
 '头像,个人', FALSE, FALSE, NULL, 1, 
 0, 15, NOW(), NOW()),
(2, 2, 2, 'user', 2, '经理头像', 
 '头像,个人', FALSE, FALSE, NULL, 1, 
 0, 8, NOW(), NOW()),
(3, 3, 3, 'user', 3, '企业用户头像', 
 '头像,个人', FALSE, FALSE, NULL, 1, 
 0, 12, NOW(), NOW()),
(4, 4, 4, 'user', 4, '普通用户头像', 
 '头像,个人', FALSE, FALSE, NULL, 1, 
 0, 6, NOW(), NOW()),

-- 简历附件
(5, 5, 1, 'resume', 1, '张三的个人简历', 
 '简历,Java,Spring Boot', FALSE, FALSE, NULL, 1, 
 5, 23, NOW(), NOW()),
(6, 6, 2, 'resume', 2, '李四的个人简历', 
 '简历,项目管理,团队管理', FALSE, FALSE, NULL, 1, 
 3, 15, NOW(), NOW()),
(7, 7, 3, 'resume', 3, '王五的个人简历', 
 '简历,招聘,人力资源管理', FALSE, FALSE, NULL, 1, 
 2, 8, NOW(), NOW()),
(8, 8, 4, 'resume', 4, '赵六的个人简历', 
 '简历,Java,Spring Boot,前端', FALSE, FALSE, NULL, 1, 
 7, 18, NOW(), NOW()),

-- 企业Logo附件
(9, 9, 1, 'company', 1, '示例科技公司Logo', 
 'Logo,企业', TRUE, FALSE, NULL, 1, 
 12, 45, NOW(), NOW()),
(10, 10, 2, 'company', 2, '测试企业Logo', 
 'Logo,企业', TRUE, FALSE, NULL, 1, 
 8, 32, NOW(), NOW()),

-- 岗位附件
(11, 11, 1, 'job', 1, 'Java开发岗位详细要求', 
 '岗位要求,技术文档', TRUE, FALSE, NULL, 1, 
 15, 67, NOW(), NOW()),
(12, 12, 2, 'job', 4, '软件测试岗位公司介绍', 
 '公司介绍,岗位说明', TRUE, FALSE, NULL, 1, 
 8, 34, NOW(), NOW()),

-- 身份证附件
(13, 13, 1, 'user', 1, '身份证正面照片', 
 '身份证,认证', FALSE, FALSE, NULL, 1, 
 0, 3, NOW(), NOW()),
(14, 14, 1, 'user', 1, '身份证反面照片', 
 '身份证,认证', FALSE, FALSE, NULL, 1, 
 0, 2, NOW(), NOW()),

-- 其他文件附件
(15, 15, 1, 'system', 1, '系统使用说明文档', 
 '说明文档,帮助', TRUE, FALSE, NULL, 1, 
 3, 12, NOW(), NOW()),

-- 临时文件附件（用于测试临时文件功能）
(16, 5, 1, 'resume', NULL, '临时上传的简历文件', 
 '临时文件', FALSE, TRUE, DATE_ADD(NOW(), INTERVAL 7 DAY), 1, 
 0, 0, NOW(), NOW()),
(17, 6, 2, 'resume', NULL, '临时上传的项目文档', 
 '临时文件', FALSE, TRUE, DATE_ADD(NOW(), INTERVAL 7 DAY), 1, 
 0, 0, NOW(), NOW()),

-- 已删除文件附件（用于测试文件状态）
(18, 7, 3, 'resume', 3, '已删除的简历文件', 
 '简历,已删除', FALSE, FALSE, NULL, 2, 
 2, 5, NOW(), NOW()),
(19, 8, 4, 'resume', 4, '已过期的简历文件', 
 '简历,过期', FALSE, FALSE, DATE_SUB(NOW(), INTERVAL 1 DAY), 3, 
 1, 3, NOW(), NOW());

-- 更新文件引用计数（基于附件关系统计）
UPDATE `file` SET `reference_count` = (
    SELECT COUNT(*) FROM `attachment` WHERE `attachment`.`file_id` = `file`.`id` AND `attachment`.`status` = 1
) WHERE `id` IN (SELECT `file_id` FROM `attachment` WHERE `status` = 1);

-- 插入岗位申请测试数据
INSERT IGNORE INTO `job_apply` (
    `id`, `job_id`, `user_id`, `resume_id`, `status`, `match_score`, 
    `apply_notes`, `interview_time`, `interview_location`, `feedback`, 
    `create_time`, `update_time`
) VALUES
(1, 1, 4, 8, 1, 85.5, 
 '对Java开发岗位很感兴趣，希望有机会加入贵公司', NULL, 
 NULL, NULL, NOW(), NOW()),
(2, 2, 4, 8, 2, 78.2, 
 '前端开发经验丰富，熟悉Vue.js和React技术栈', NULL, 
 NULL, '简历符合要求，等待进一步沟通', NOW(), NOW()),
(3, 3, 4, 8, 3, 82.7, 
 '有Android开发经验，熟悉Java和Kotlin', '2024-11-20 14:00:00', 
 '北京市中关村大街1号示例科技公司', '技术能力符合要求，安排技术面试', NOW(), NOW()),
(4, 4, 4, 8, 4, 76.8, 
 '软件测试经验丰富，熟悉自动化测试工具', '2024-11-18 10:00:00', 
 '上海市浦东新区张江高科技园区测试企业', '面试表现优秀，符合岗位要求', NOW(), NOW()),
(5, 5, 4, 8, 5, 88.3, 
 '运维经验丰富，熟悉Linux和Docker技术', '2024-11-17 09:00:00', 
 '上海市浦东新区张江高科技园区测试企业', '恭喜您被录用，请准备入职材料', NOW(), NOW()),
(6, 8, 4, 8, 6, 65.4, 
 '数据分析经验较少，希望学习成长', NULL, 
 NULL, '岗位要求与简历匹配度不高，建议关注其他更适合的岗位', NOW(), NOW());
