-- 权限管理模块初始化数据

-- 设置字符集
SET NAMES utf8mb4;

-- 先删除已有数据（避免重复插入）
-- 注意删除顺序：先删除有外键约束的子表，再删除父表
DELETE FROM user_role;
DELETE FROM role_permission;
DELETE FROM user_profile;
DELETE FROM user;
DELETE FROM role;
DELETE FROM permission;
DELETE FROM job;
DELETE FROM job_tag;
-- 对于有自引用外键的job_category表，先删除子分类再删除父分类
DELETE FROM job_category WHERE parent_id != 0; -- 先删除子分类
DELETE FROM job_category WHERE parent_id = 0; -- 再删除父分类
DELETE FROM company;
DELETE FROM system_config;

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
(37, '简历写入接口', 'resume:write', '简历写入接口权限', 3, NULL, '/api/resumes/**', 'POST,PUT,DELETE', 10, NULL, 'resume', 1, NOW(), NOW());

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
WHERE module IN ('job', 'resume') OR code IN ('job:read', 'job:write', 'resume:read');

-- 为普通用户角色分配基本权限
INSERT INTO role_permission (role_id, permission_id, create_time) 
SELECT 4, id, NOW() FROM permission 
WHERE code IN ('job:read', 'resume:write', 'user:read');

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

INSERT IGNORE INTO `system_config` (`config_key`, `config_value`, `description`) VALUES
('site_name', '就业平台', '网站名称'),
('resume_privacy_default', '2', '简历默认隐私级别'),
('job_expire_days', '30', '岗位默认过期天数'),
('match_threshold', '60', '匹配阈值（百分比）'),
('sms_enabled', 'false', '短信服务是否启用'),
('email_enabled', 'false', '邮件服务是否启用');

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
 'Java,Spring Boot,微服务,后端开发', 1, 156, 23, 12,
 '2024-11-01 09:00:00', '2024-12-31 23:59:59', NOW(), NOW()),

(2, '前端开发工程师', 9, 1, '技术部', 1,
 12000.00, 20000.00, '月', '北京市', '中关村大街1号',
 '海淀区', 39.983424, 116.322987,
 '负责公司产品的前端开发工作，与设计师和后端工程师紧密合作。',
 '1. 2年以上前端开发经验，熟练掌握Vue.js或React\n2. 熟悉HTML5、CSS3、JavaScript等前端技术\n3. 有移动端开发经验者优先\n4. 具备良好的沟通能力和团队协作精神',
 '1. 五险一金、补充医疗保险\n2. 年终奖、项目奖金\n3. 弹性工作制、带薪年假\n4. 技术分享、学习资源支持',
 '{"contactPerson": "李主管", "phone": "13800138001", "email": "hr@example.com"}',
 '本科', '2-4年', 2, 0, 1, FALSE, NULL,
 '前端,Vue.js,React,JavaScript', 1, 89, 15, 8,
 '2024-11-05 10:00:00', '2024-12-31 23:59:59', NOW(), NOW()),

(3, 'Android开发工程师', 10, 1, '移动开发部', 1,
 13000.00, 22000.00, '月', '北京市', '中关村大街1号',
 '海淀区', 39.983424, 116.322987,
 '负责公司Android客户端的设计、开发和维护工作。',
 '1. 2年以上Android开发经验，熟悉Java/Kotlin\n2. 熟悉Android SDK和常用框架\n3. 有性能优化经验者优先\n4. 具备独立开发能力，有上架应用经验',
 '1. 五险一金、补充医疗保险\n2. 年终奖、项目奖金\n3. 弹性工作制、带薪年假\n4. 技术培训、职业发展',
 '{"contactPerson": "王经理", "phone": "13800138002", "email": "hr@example.com"}',
 '本科', '2-4年', 2, 0, 1, TRUE, '技术氛围好，成长空间大',
 'Android,Java,Kotlin,移动开发', 1, 67, 11, 5,
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
 '软件测试,自动化测试,质量保障', 1, 45, 8, 3,
 '2024-11-08 09:30:00', '2024-12-31 23:59:59', NOW(), NOW()),

(5, '运维工程师', 12, 2, '运维部', 1,
 12000.00, 20000.00, '月', '上海市', '浦东新区张江高科技园区',
 '浦东新区', 31.204737, 121.601091,
 '负责公司服务器和网络设备的运维管理工作。',
 '1. 2年以上运维经验，熟悉Linux系统\n2. 熟悉Docker、Kubernetes等容器技术\n3. 熟悉网络配置和故障排查\n4. 具备良好的问题解决能力',
 '1. 五险一金、补充医疗保险\n2. 年终奖、项目奖金\n3. 弹性工作制、带薪年假\n4. 技术培训、职业发展',
 '{"contactPerson": "钱经理", "phone": "13900139001", "email": "hr@test.com"}',
 '本科', '2-4年', 2, 0, 1, TRUE, '技术氛围浓厚，发展前景好',
 '运维,Linux,Docker,Kubernetes', 1, 78, 12, 6,
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
 '产品经理,产品设计,需求分析', 0, 0, 0, 0,
 NULL, '2024-12-31 23:59:59', NOW(), NOW()),

(7, 'UI设计师（已下架）', 3, 2, '设计部', 1,
 10000.00, 18000.00, '月', '上海市', '浦东新区张江高科技园区',
 '浦东新区', 31.204737, 121.601091,
 '负责产品的界面设计和用户体验优化。',
 '1. 2年以上UI设计经验\n2. 熟练使用Sketch、Figma等设计工具\n3. 具备良好的审美和创意能力\n4. 有移动端设计经验者优先',
 '1. 五险一金、补充医疗保险\n2. 年终奖、项目奖金\n3. 弹性工作制、带薪年假\n4. 设计培训、职业发展',
 '{"contactPerson": "周主管", "phone": "13900139002", "email": "hr@test.com"}',
 '本科', '2-4年', 1, 0, 1, FALSE, NULL,
 'UI设计,用户体验,界面设计', 2, 34, 5, 2,
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
 '数据分析,SQL,Python,数据挖掘', 1, 92, 18, 9,
 '2024-11-15 13:00:00', '2024-12-31 23:59:59', NOW(), NOW()),

(9, '销售专员', 6, 2, '销售部', 1,
 8000.00, 15000.00, '月', '上海市', '浦东新区张江高科技园区',
 '浦东新区', 31.204737, 121.601091,
 '负责公司产品的销售和客户维护工作。',
 '1. 1年以上销售经验\n2. 具备良好的沟通和谈判能力\n3. 有软件销售经验者优先\n4. 积极主动，有团队合作精神',
 '1. 五险一金、补充医疗保险\n2. 绩效奖金、提成\n3. 带薪培训、晋升机会\n4. 团队活动、节日福利',
 '{"contactPerson": "郑主管", "phone": "13900139003", "email": "hr@test.com"}',
 '大专', '1-3年', 5, 1, 1, FALSE, NULL,
 '销售,客户维护,商务拓展', 1, 56, 9, 4,
 '2024-11-18 09:00:00', '2024-12-31 23:59:59', NOW(), NOW()),

(10, '运营专员', 4, 1, '运营部', 1,
 9000.00, 16000.00, '月', '北京市', '中关村大街1号',
 '海淀区', 39.983424, 116.322987,
 '负责产品运营和用户增长工作。',
 '1. 1年以上运营经验\n2. 熟悉运营工具和方法\n3. 具备数据分析和文案能力\n4. 有互联网运营经验者优先',
 '1. 五险一金、补充医疗保险\n2. 绩效奖金、项目奖金\n3. 弹性工作制、带薪年假\n4. 运营培训、职业发展',
 '{"contactPerson": "冯经理", "phone": "13800138005", "email": "hr@example.com"}',
 '本科', '1-3年', 3, 0, 1, TRUE, '运营体系完善，成长空间大',
 '运营,用户增长,数据分析', 1, 73, 14, 7,
 '2024-11-20 14:30:00', '2024-12-31 23:59:59', NOW(), NOW());
