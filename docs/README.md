# 就业平台 API 文档

## 项目概述
这是一个基于 Spring Boot 的就业平台后端系统，提供完整的招聘求职功能。

## 技术栈
- **后端框架**: Spring Boot 2.x
- **数据库**: MySQL
- **ORM**: Spring Data JPA
- **安全框架**: Spring Security
- **构建工具**: Maven

## 数据库配置
数据库连接配置在 `src/main/resources/application.properties` 中：
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employment_db
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

## 文档导航

### API 测试指南
- [API 测试指南索引](api-test-guide-index.md) - 完整的API测试指南和快速开始

### 前端设计方案
- [首页功能设计方案](homepage-design.md) - 首页功能优化和智能推荐设计方案

## 模块文档

### 核心模块
- [用户管理模块](user-management.md) - 用户注册、登录、资料管理等
- [岗位管理模块](job-management.md) - 岗位的增删改查和搜索
- [简历管理模块](resume-management.md) - 简历的创建和管理
- [岗位申请模块](job-apply-management.md) - 岗位申请流程管理

### 高级功能模块
- [通知管理模块](notification-management.md) - 系统通知和消息推送
- [岗位分类模块](job-category-management.md) - 岗位分类管理
- [智能推荐模块](recommendation-system.md) - 智能岗位推荐
- [数据分析模块](analytics-system.md) - 数据统计和分析

### 系统管理模块
- [权限管理模块](permission-management.md) - 用户权限和角色管理
- [消息推送模块](message-push-system.md) - 站内消息、邮件、短信推送
- [文件管理模块](file-management.md) - 文件上传下载管理

## 快速开始

### 1. 用户注册
```bash
curl -X POST "http://localhost:8080/api/users/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "phone": "13800138000",
    "email": "test@example.com",
    "password": "password123",
    "userType": 1
  }'
```

### 2. 用户登录（使用测试账号）
```bash
curl -X POST "http://localhost:8080/api/users/login" \
  -H "Content-Type: application/json" \
  -d '{
    "account": "wangcheng03",
    "password": "ABCabc@123"
  }'
```

### 3. 获取岗位列表
```bash
curl -X GET "http://localhost:8080/api/jobs?page=0&size=10" \
  -H "Authorization: Bearer {token}"
```

## 测试账号信息

| 用户名 | 密码 | 用户类型 | 说明 |
|--------|------|----------|------|
| wangcheng03 | ABCabc@123 | 求职者 | 主要测试账号 |
| testuser | password123 | 求职者 | 备用测试账号 |
| company1 | password123 | 企业用户 | 企业用户测试账号 |
| company2 | password123 | 企业用户 | 企业用户测试账号 |

**推荐使用测试账号：wangcheng03 / ABCabc@123**

## 鉴权说明

### 认证方式
系统使用基于JWT (JSON Web Token) 的Bearer Token认证方式。

#### 1. 获取Token
通过登录接口获取访问令牌。

#### 2. 使用Token
在后续请求的Header中携带Token：
```
Authorization: Bearer {token}
```

#### 3. 无需认证的接口
- 用户注册 (`POST /api/users/register`)
- 用户登录 (`POST /api/users/login`)
- 微信登录 (`POST /api/users/login/wx`)
- 手机号登录 (`POST /api/users/login/phone`)
- 发送验证码 (`POST /api/users/send-verification-code`)
- 重置密码 (`POST /api/users/reset-password`)

#### 4. 需要认证的接口
除上述登录和认证相关接口外，其他所有接口都需要有效的JWT Token进行鉴权验证。

## 接口使用说明

### 请求格式
- **Content-Type**: `application/json`
- **认证方式**: Bearer Token (JWT)
- **分页参数**: 
  - `page`: 页码 (从0开始)
  - `size`: 每页大小
  - `sort`: 排序字段
  - `direction`: 排序方向 (ASC/DESC)

### 响应格式
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": "2025-11-08T11:19:45"
}
```

### 常见状态码
- `200`: 成功
- `400`: 请求参数错误
- `401`: 未授权
- `403`: 权限不足
- `404`: 资源不存在
- `500`: 服务器内部错误

## 注意事项

- 所有API接口都需要进行身份验证（Bearer Token）
- 文件上传需要配置存储路径
- 推荐系统需要训练数据
- 数据分析需要足够的数据量
- 接口调用频率限制：100次/分钟
- 文件大小限制：单个文件不超过10MB
- 分页查询默认页码从0开始，每页大小默认20
- 所有时间参数使用ISO 8601格式：yyyy-MM-dd'T'HH:mm:ss

## 版本信息

- **当前版本**: v1.0.0
- **最后更新**: 2025-11-08
- **维护者**: 就业平台开发团队
