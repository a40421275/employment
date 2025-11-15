# 就业平台 API 测试指南索引

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

## 模块 API 文档索引

### 1. 用户管理模块
- **基础路径**: `/api/users`
- **文档**: [用户管理模块 API 文档](./user-management.md)
- **主要功能**: 用户注册、登录、资料管理、认证等

### 2. 岗位管理模块
- **基础路径**: `/api/jobs`
- **文档**: [岗位管理模块 API 文档](./job-management.md)
- **主要功能**: 岗位创建、更新、搜索、筛选等

### 3. 简历管理模块
- **基础路径**: `/api/resumes`
- **文档**: [简历管理模块 API 文档](./resume-management.md)
- **主要功能**: 简历创建、管理、隐私设置等

### 4. 岗位申请模块
- **基础路径**: `/api/job-applies`
- **文档**: [岗位申请模块 API 文档](./job-apply-management.md)
- **主要功能**: 岗位申请、状态管理、面试邀请等

### 5. 通知管理模块
- **基础路径**: `/api/notifications`
- **文档**: [通知管理模块 API 文档](./notification-management.md)
- **主要功能**: 通知创建、发送、状态管理等

### 6. 岗位分类模块
- **基础路径**: `/api/job-categories`
- **文档**: [岗位分类模块 API 文档](./job-category-management.md)
- **主要功能**: 分类管理、树形结构、排序等

### 7. 智能推荐模块
- **基础路径**: `/api/recommendations`
- **文档**: [智能推荐模块 API 文档](./recommendation-system.md)
- **主要功能**: 岗位推荐、简历推荐、算法管理等

### 8. 数据分析模块
- **基础路径**: `/api/analytics`
- **文档**: [数据分析模块 API 文档](./analytics-system.md)
- **主要功能**: 数据统计、趋势分析、报表生成等

### 9. 权限管理模块
- **基础路径**: `/api/permissions`
- **文档**: [权限管理模块 API 文档](./permission-management.md)
- **主要功能**: 角色管理、权限分配、用户权限验证等

### 10. 消息推送模块
- **基础路径**: `/api/messages`
- **文档**: [消息推送模块 API 文档](./message-push-system.md)
- **主要功能**: 站内消息、邮件、短信、微信推送等

### 11. 文件管理模块
- **基础路径**: `/api/files`
- **文档**: [文件管理模块 API 文档](./file-management.md)
- **主要功能**: 文件上传、下载、管理、分享等

## 测试建议

### 1. 基础功能测试
- 用户注册、登录、资料管理
- 岗位的增删改查和搜索
- 简历的创建和管理
- 岗位申请流程测试

### 2. 高级功能测试
- 智能推荐系统测试
- 数据分析功能测试
- 权限管理系统测试
- 消息推送功能测试

### 3. 性能测试
- 并发用户测试
- 大数据量查询测试
- 文件上传下载性能测试

### 4. 安全测试
- 权限验证测试
- 数据安全测试
- 接口安全测试

## 部署说明

1. 配置数据库连接
2. 运行数据库初始化脚本
3. 启动Spring Boot应用
4. 访问API接口进行测试

## 鉴权说明

### 认证方式
系统使用基于JWT (JSON Web Token) 的Bearer Token认证方式。

#### 1. 获取Token
通过登录接口获取访问令牌：

```bash
# 用户登录获取Token
curl -X POST "http://localhost:8080/api/users/login" \
  -H "Content-Type: application/json" \
  -d '{
    "account": "testuser",
    "password": "password123"
  }'
```

响应示例：
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "username": "testuser",
      "phone": "13800138000",
      "email": "test@example.com",
      "userType": 1,
      "status": 1,
      "authStatus": 0
    }
  }
}
```

#### 2. 使用Token
在后续请求的Header中携带Token：

```bash
curl -X GET "http://localhost:8080/api/jobs" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### 3. Token过期
- Token默认有效期为24小时
- 过期后需要重新登录获取新的Token
- 可以通过刷新Token接口续期（如果支持）

#### 4. 权限控制
系统根据用户类型和角色进行权限控制：

| 用户类型 | 权限说明 |
|---------|----------|
| 1-求职者 | 可查看岗位、投递简历、管理个人资料 |
| 2-企业用户 | 可发布岗位、查看申请、管理企业信息 |
| 3-管理员 | 拥有所有权限，可管理用户、岗位、系统配置 |

### 无需认证的接口
以下接口无需认证即可访问：
- 用户注册 (`POST /api/users/register`)
- 用户登录 (`POST /api/users/login`)
- 微信登录 (`POST /api/users/login/wx`)
- 手机号登录 (`POST /api/users/login/phone`)
- 发送验证码 (`POST /api/users/send-verification-code`)
- 重置密码 (`POST /api/users/reset-password`)

### 需要认证的接口
除上述登录和认证相关接口外，其他所有接口都需要有效的JWT Token进行鉴权验证。

**重要提示**：所有模块的API接口（除用户注册、登录等认证相关接口外）都必须携带有效的JWT Token才能访问。Token验证失败将返回401状态码。

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

## 测试账号信息

为了方便测试，系统已预置以下测试账号：

### 基础功能测试账号
| 用户名 | 密码 | 用户类型 | 说明 |
|--------|------|----------|------|
| wangcheng03 | ABCabc@123 | 求职者 | 主要测试账号 |
| testuser | password123 | 求职者 | 备用测试账号 |
| company1 | password123 | 企业用户 | 企业用户测试账号 |
| company2 | password123 | 企业用户 | 企业用户测试账号 |

### 权限管理模块测试账号
| 用户名 | 密码 | 用户类型 | 角色 | 权限说明 |
|--------|------|----------|------|----------|
| admin | qwe123!@# | 管理员 | 超级管理员 | 拥有所有系统权限 |
| manager | qwe123!@# | 管理员 | 管理员 | 拥有大部分管理权限 |
| employer | qwe123!@# | 企业用户 | 企业用户 | 可以管理岗位和查看简历 |
| user | qwe123!@# | 求职者 | 普通用户 | 可以查看岗位和投递简历 |

**推荐使用测试账号：wangcheng03 / ABCabc@123**

**权限模块测试账号密码：所有权限模块测试账号的密码均为 `qwe123!@#`**

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

### 4. 创建岗位申请
```bash
curl -X POST "http://localhost:8080/api/job-applies" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "userId": 1,
    "jobId": 1,
    "resumeId": 1,
    "applyNotes": "对该岗位非常感兴趣",
    "status": 1
  }'
```

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

---

**更多详细信息请查看各模块的具体API文档**
