# 文件管理模块

## 概述

文件管理模块为就业服务平台提供统一的文件上传、下载、存储和管理功能。支持多种文件类型，包括简历、头像、企业Logo、岗位附件等，并提供文件版本管理、权限控制、重复文件检测等高级功能。

## 功能特性

### 核心功能
- **两步式文件上传**: 支持文件池上传和附件创建分离
- **文件下载**: 支持文件下载
- **文件管理**: 支持文件删除和查询
- **临时文件管理**: 支持临时文件上传和绑定业务
- **文件验证**: 支持文件类型和大小验证
- **文件池管理**: 支持文件哈希查找和存储统计
- **签名URL访问**: 支持无需认证的签名URL文件访问

### 高级功能
- **重复文件检测**: 基于文件哈希值避免重复上传
- **过期文件清理**: 自动清理临时和过期文件
- **文件池管理**: 支持文件引用计数和清理
- **签名URL安全**: 支持带时效性的签名URL访问

## 数据库设计

### 文件表 (file)

| 字段名 | 类型 | 描述 | 约束 |
|--------|------|------|------|
| id | BIGINT | 文件ID | 主键，自增 |
| file_name | VARCHAR(255) | 存储文件名 | 非空 |
| original_name | VARCHAR(255) | 原始文件名 | 非空 |
| file_path | VARCHAR(500) | 文件存储路径 | 非空 |
| file_size | BIGINT | 文件大小(字节) | 非空 |
| file_type | VARCHAR(50) | 文件类型 | 非空 |
| mime_type | VARCHAR(100) | MIME类型 | 非空 |
| file_extension | VARCHAR(20) | 文件扩展名 | 非空 |
| file_hash | VARCHAR(64) | 文件哈希值 | 唯一索引 |
| creator_user_id | BIGINT | 创建用户ID | 外键 |
| reference_count | INT | 引用次数 | 默认0 |
| status | TINYINT | 文件状态 | 默认1 |
| last_access_time | DATETIME | 最后访问时间 | 可为空 |
| create_time | DATETIME | 创建时间 | 非空 |
| update_time | DATETIME | 更新时间 | 非空 |

### 文件状态定义
- `0`: 草稿
- `1`: 正常
- `2`: 已删除
- `3`: 已过期

### 文件类型定义
- `image`: 图片文件（如头像、Logo、身份证照片等）
- `document`: 文档文件（如简历、岗位附件、PDF文档等）
- `other`: 其他文件类型

### 业务类型定义 (businessType)
业务类型由各业务模块自定义，用于标识附件所属的业务模块。通过**业务类型 + 业务ID + 文件类型**三个字段唯一定位文件归属。

**设计原则**
- 业务类型由各业务模块自行定义和保障唯一性
- 系统不预定义业务类型枚举，支持动态扩展
- 各业务模块负责维护自己使用的业务类型标识

**使用示例**
- 业务类型=`user_avatar`, 业务ID=`123`, 文件类型=`image` → 用户123的头像
- 业务类型=`user_id_card`, 业务ID=`123`, 文件类型=`image` → 用户123的身份证
- 业务类型=`company_logo`, 业务ID=`456`, 文件类型=`image` → 企业456的Logo
- 业务类型=`job_attachment`, 业务ID=`789`, 文件类型=`document` → 岗位789的附件

**建议规范**
- 业务类型格式：`{模块名}_{用途名}`
- 各模块应在自己的文档中明确使用的业务类型
- 业务类型应具有唯一性和可读性

## API接口

### 两步式上传绑定方案

#### 第一步：上传文件到文件池
将文件上传到文件池，返回文件ID。支持自定义文件名，不依赖上传文件的实际名称。

```http
POST /api/files/upload/pool
Content-Type: multipart/form-data

参数:
- file: 文件 (必填)
- userId: 用户ID (必填)
- fileType: 文件类型 (必填)
- fileName: 自定义文件名 (可选，如不提供则使用原始文件名)
```

响应:
```json
{
  "code": 200,
  "message": "文件上传到文件池成功",
  "data": {
    "fileId": 1
  },
  "timestamp": "2025-11-17T00:54:23"
}
```

**文件名管理说明：**
- `fileName` 字段保存用户自定义的文件名，用于显示和下载
- `originalName` 字段保存上传文件的原始文件名，用于记录
- 物理文件存储路径使用UUID，确保文件系统安全
- 支持用户自定义文件名，不依赖上传文件的实际名称

#### 第二步：创建附件记录
将文件池中的文件绑定到具体业务，创建附件记录。

```http
POST /api/files/create/attachment
Content-Type: application/json

参数:
{
  "fileId": 1,
  "userId": 1,
  "businessType": "resume",
  "businessId": 123,
  "description": "求职简历",
  "tags": "简历,求职,Java",
  "isPublic": false
}
```

响应:
```json
{
  "code": 200,
  "message": "附件创建成功",
  "data": {
    "attachmentId": 1
  },
  "timestamp": "2025-11-17T00:54:23"
}
```

#### 便捷方法：上传并创建附件
一步完成文件上传和附件创建。

```http
POST /api/files/upload/attachment
Content-Type: multipart/form-data

参数:
- file: 文件 (必填)
- userId: 用户ID (必填)
- fileType: 文件类型 (必填)
- businessType: 业务类型 (必填)
- businessId: 业务ID (必填)
- description: 文件描述 (可选)
- tags: 文件标签 (可选)
- isPublic: 是否公开 (可选，默认false)
```

响应:
```json
{
  "code": 200,
  "message": "文件上传和附件创建成功",
  "data": {
    "attachmentId": 1
  },
  "timestamp": "2025-11-17T00:54:23"
}
```

### 签名URL方案

签名URL方案用于解决前端图片无法添加认证参数的问题，通过生成带有时效性和签名的URL，允许前端无需认证即可访问图片文件。

#### 生成签名文件URL（用于前端图片访问）
```http
GET /api/files/signed/generate/{attachmentId}
```

响应:
```json
{
  "code": 200,
  "message": "生成签名URL成功",
  "data": {
    "signedUrl": "/api/files/signed/?attachmentId=123&expires=1700000000&signature=abc123def456...",
    "attachmentId": 123
  },
  "timestamp": "2025-11-17T00:54:23"
}
```

#### 通过文件ID生成签名文件URL（用于前端图片访问）
```http
GET /api/files/signed/generate/by-file-id/{fileId}
```

响应:
```json
{
  "code": 200,
  "message": "通过文件ID生成签名URL成功",
  "data": {
    "signedUrl": "/api/files/signed/?fileId=456&expires=1700000000&signature=xyz789abc123...",
    "fileId": 456
  },
  "timestamp": "2025-11-17T00:54:23"
}
```

### 临时文件管理

#### 上传临时文件（7天过期）
```http
POST /api/files/upload/temporary
Content-Type: multipart/form-data

参数:
- file: 文件 (必填)
- userId: 用户ID (必填)
- fileType: 文件类型 (必填)
```

响应:
```json
{
  "code": 200,
  "message": "临时文件上传成功",
  "data": {
    "attachmentId": 1,
    "isTemporary": true,
    "expireDays": 7
  },
  "timestamp": "2025-11-17T00:54:23"
}
```

#### 绑定临时文件到业务
将临时文件绑定到具体业务，使其成为正式文件。

```http
POST /api/files/bind/temporary
Content-Type: application/json

参数:
{
  "attachmentId": 1,
  "businessType": "resume",
  "businessId": 123,
  "description": "求职简历",
  "tags": "简历,求职,Java",
  "isPublic": false
}
```

响应:
```json
{
  "code": 200,
  "message": "临时文件绑定业务成功",
  "data": {
    "success": true
  },
  "timestamp": "2025-11-17T00:54:23"
}
```

### 核心文件操作

#### 下载文件
```http
GET /api/files/download/{attachmentId}
```

响应: 文件二进制流

#### 通过文件ID下载文件
```http
GET /api/files/download/by-file-id/{fileId}
```

响应: 文件二进制流

#### 获取文件信息
```http
GET /api/files/{attachmentId}
```

响应:
```json
{
  "code": 200,
  "message": "获取文件信息成功",
  "data": {
    "attachmentId": 1,
    "fileName": "1_avatar_1700000000000_123.jpg",
    "originalName": "avatar.jpg",
    "fileSize": 102400,
    "fileType": "avatar",
    "mimeType": "image/jpeg",
    "fileExtension": "jpg",
    "userId": 1,
    "businessType": "avatar",
    "createTime": "2024-01-01T00:00:00",
    "updateTime": "2024-01-01T00:00:00",
    "fileUrl": "/api/files/download/1"
  },
  "timestamp": "2025-11-17T00:54:23"
}
```

#### 删除文件
```http
DELETE /api/files/{attachmentId}
```

响应:
```json
{
  "code": 200,
  "message": "文件删除成功",
  "data": {
    "success": true
  },
  "timestamp": "2025-11-17T00:54:23"
}
```

#### 获取用户文件列表
```http
GET /api/files/user/{userId}
参数:
- businessType: 业务类型 (可选)
```

响应:
```json
{
  "code": 200,
  "message": "获取用户文件列表成功",
  "data": [
    {
      "attachmentId": 1,
      "fileName": "1_avatar_1700000000000_123.jpg",
      "originalName": "avatar.jpg",
      "fileSize": 102400,
      "fileType": "avatar",
      "mimeType": "image/jpeg",
      "userId": 1,
      "businessType": "avatar",
      "createTime": "2024-01-01T00:00:00",
      "updateTime": "2024-01-01T00:00:00",
      "fileUrl": "/api/files/download/1"
    }
  ],
  "timestamp": "2025-11-17T00:54:23"
}
```

#### 根据业务类型和业务ID获取附件列表
```http
GET /api/files/business
参数:
- businessType: 业务类型 (必填)
- businessId: 业务ID (必填)
- fileType: 文件类型 (可选)
```

响应:
```json
{
  "code": 200,
  "message": "获取业务附件列表成功",
  "data": [
    {
      "attachmentId": 1,
      "fileName": "1_resume_1700000000000_123.pdf",
      "originalName": "张三_简历.pdf",
      "fileSize": 204800,
      "fileType": "resume",
      "mimeType": "application/pdf",
      "userId": 1,
      "businessType": "resume",
      "businessId": 123,
      "description": "求职简历",
      "tags": "简历,求职,Java",
      "downloadCount": 5,
      "viewCount": 15,
      "isPublic": false,
      "isTemporary": false,
      "createTime": "2024-01-01T00:00:00",
      "updateTime": "2024-01-01T00:00:00",
      "fileUrl": "/api/files/download/1",
      "downloadable": true,
      "expired": false
    }
  ],
  "timestamp": "2025-11-17T00:54:23"
}
```

#### 检查文件是否存在
```http
GET /api/files/exists/{attachmentId}
```

响应:
```json
{
  "code": 200,
  "message": "检查文件存在成功",
  "data": {
    "exists": true
  },
  "timestamp": "2025-11-17T00:54:23"
}
```

#### 获取文件URL
```http
GET /api/files/url/{attachmentId}
```

响应:
```json
{
  "code": 200,
  "message": "获取文件URL成功",
  "data": {
    "url": "/api/files/download/1"
  },
  "timestamp": "2025-11-17T00:54:23"
}
```

#### 统一查询附件列表（支持分页和复杂条件查询）
```http
POST /api/files/query
Content-Type: application/json

参数:
{
  "page": 0,
  "size": 20,
  "userId": 1,
  "businessType": "resume",
  "businessId": 123,
  "fileType": "pdf",
  "fileExtension": "pdf",
  "keyword": "简历",
  "status": 1,
  "isPublic": false,
  "isTemporary": false,
  "tags": "简历,求职",
  "startTime": "2024-01-01",
  "endTime": "2024-12-31",
  "minFileSize": 1024,
  "maxFileSize": 10485760,
  "sortField": "createTime",
  "sortDirection": "DESC"
}
```

参数说明:
- `page`: 页码，从0开始（可选，默认0）
- `size`: 每页大小（可选，默认20）
- `userId`: 用户ID（可选）
- `businessType`: 业务类型（可选，支持：resume、avatar、id_card、company_logo、job_attachment、other）
- `businessId`: 业务ID（可选）
- `fileType`: 文件类型（可选）
- `fileExtension`: 文件扩展名（可选）
- `keyword`: 搜索关键词，支持文件名、描述、标签搜索（可选）
- `status`: 状态（可选，1-正常，2-已删除，3-已过期）
- `isPublic`: 是否公开（可选，true-公开，false-私有）
- `isTemporary`: 是否临时文件（可选，true-临时文件，false-正式文件）
- `tags`: 标签，逗号分隔（可选）
- `startTime`: 开始时间，格式：yyyy-MM-dd（可选）
- `endTime`: 结束时间，格式：yyyy-MM-dd（可选）
- `minFileSize`: 文件大小最小值（字节，可选）
- `maxFileSize`: 文件大小最大值（字节，可选）
- `sortField`: 排序字段（可选，支持：fileName、fileSize、downloadCount、viewCount、createTime、updateTime，默认createTime）
- `sortDirection`: 排序方向（可选，ASC-升序，DESC-降序，默认DESC）

响应:
```json
{
  "code": 200,
  "message": "查询附件列表成功",
  "data": {
    "content": [
      {
        "attachmentId": 1,
        "userId": 1,
        "businessType": "resume",
        "businessId": 123,
        "description": "求职简历",
        "tags": "简历,求职,Java",
        "isPublic": false,
        "isTemporary": false,
        "expireTime": null,
        "status": 1,
        "downloadCount": 5,
        "viewCount": 15,
        "createTime": "2024-01-01T00:00:00",
        "updateTime": "2024-01-01T00:00:00",
        "fileId": 1,
        "fileName": "1_resume_1700000000000_123.pdf",
        "originalName": "张三_简历.pdf",
        "filePath": "/uploads/1/1_resume_1700000000000_123.pdf",
        "fileSize": 204800,
        "fileType": "resume",
        "mimeType": "application/pdf",
        "fileExtension": "pdf",
        "fileHash": "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
        "referenceCount": 1,
        "downloadable": true,
        "expired": false
      }
    ],
    "totalElements": 100,
    "totalPages": 10,
    "size": 20,
    "number": 0,
    "first": true,
    "last": false,
    "numberOfElements": 20
  },
  "timestamp": "2025-11-17T00:54:23"
}
```

**优化说明：**
统一查询接口现在直接返回包含附件和文件信息的完整数据（AttachmentWithFileDTO），无需客户端额外请求文件信息，提高了查询效率和用户体验。

### 文件验证

#### 验证文件类型
```http
POST /api/files/validate/type
Content-Type: multipart/form-data

参数:
- file: 文件 (必填)
```

响应:
```json
{
  "code": 200,
  "message": "文件类型验证完成",
  "data": {
    "isValid": true,
    "allowedTypes": "jpg,jpeg,png,gif,pdf,doc,docx,txt"
  },
  "timestamp": "2025-11-17T00:54:23"
}
```

#### 验证文件大小
```http
POST /api/files/validate/size
Content-Type: multipart/form-data

参数:
- file: 文件 (必填)
```

响应:
```json
{
  "code": 200,
  "message": "文件大小验证完成",
  "data": {
    "isValid": true,
    "maxSize": 10485760
  },
  "timestamp": "2025-11-17T00:54:23"
}
```

### 文件池管理

#### 根据文件哈希查找文件
```http
GET /api/files/find/by-hash
参数:
- fileHash: 文件哈希值 (必填)
```

响应:
```json
{
  "code": 200,
  "message": "查找文件完成",
  "data": {
    "fileId": 1,
    "exists": true
  },
  "timestamp": "2025-11-17T00:54:23"
}
```

### 附件删除功能

#### 只删除附件记录，不删除物理文件
适用于需要保留文件但删除业务关联的场景，如业务数据清理但保留文件用于其他用途。

```http
DELETE /api/files/attachment/{attachmentId}/only
```

响应:
```json
{
  "code": 200,
  "message": "附件记录删除成功（保留物理文件）",
  "data": {
    "success": true
  },
  "timestamp": "2025-11-17T00:54:23"
}
```

#### 批量只删除附件记录，不删除物理文件
```http
DELETE /api/files/attachment/batch-delete-only
Content-Type: application/json

参数:
{
  "attachmentIds": [1, 2, 3]
}
```

响应:
```json
{
  "code": 200,
  "message": "批量附件记录删除成功（保留物理文件）",
  "data": {
    "success": true
  },
  "timestamp": "2025-11-17T00:54:23"
}
```

### 附件编辑功能

附件编辑功能支持后期编辑更改文件关联，包括更新附件元数据、更换文件关联、更新业务信息等操作。

#### 更新附件信息（支持完整更新）
```http
PUT /api/files/attachment/update
Content-Type: application/json

参数:
{
  "attachmentId": 1,
  "userId": 1,
  "newFileId": 2,
  "businessType": "resume",
  "businessId": 123,
  "description": "更新后的简历描述",
  "tags": "简历,求职,Java,Spring",
  "isPublic": true,
  "isTemporary": false,
  "expireTime": "2025-12-31T23:59:59",
  "status": 1
}
```

响应:
```json
{
  "code": 200,
  "message": "附件更新成功",
  "data": {
    "success": true
  },
  "timestamp": "2025-11-17T00:54:23"
}
```

#### 更换附件关联的文件
```http
PUT /api/files/attachment/change-file
Content-Type: application/json

参数:
{
  "attachmentId": 1,
  "userId": 1,
  "newFileId": 2
}
```

响应:
```json
{
  "code": 200,
  "message": "附件文件更换成功",
  "data": {
    "success": true
  },
  "timestamp": "2025-11-17T00:54:23"
}
```

#### 更新附件业务信息
```http
PUT /api/files/attachment/update-business
Content-Type: application/json

参数:
{
  "attachmentId": 1,
  "userId": 1,
  "businessType": "resume",
  "businessId": 456
}
```

响应:
```json
{
  "code": 200,
  "message": "附件业务信息更新成功",
  "data": {
    "success": true
  },
  "timestamp": "2025-11-17T00:54:23"
}
```

#### 批量更新附件状态
```http
PUT /api/files/attachment/batch-update-status
Content-Type: application/json

参数:
{
  "attachmentIds": [1, 2, 3],
  "userId": 1,
  "status": 2
}
```

响应:
```json
{
  "code": 200,
  "message": "批量更新附件状态成功",
  "data": {
    "success": true
  },
  "timestamp": "2025-11-17T00:54:23"
}
```

#### 获取附件编辑历史
```http
GET /api/files/attachment/{attachmentId}/history
```

响应:
```json
{
  "code": 200,
  "message": "获取附件编辑历史成功",
  "data": [
    {
      "operation": "UPDATE",
      "field": "description",
      "oldValue": "旧描述",
      "newValue": "新描述",
      "operator": "system",
      "operationTime": "2025-11-17T14:00:00"
    },
    {
      "operation": "CHANGE_FILE",
      "field": "fileId",
      "oldValue": "123",
      "newValue": "456",
      "operator": "system",
      "operationTime": "2025-11-16T10:30:00"
    }
  ],
  "timestamp": "2025-11-17T00:54:23"
}
```

#### 获取文件存储统计
```http
GET /api/files/statistics/storage
```

响应:
```json
{
  "code": 200,
  "message": "获取存储统计成功",
  "data": {
    "totalFiles": 100,
    "totalSize": 104857600,
    "totalUsers": 50,
    "avgFileSize": 1048576,
    "lastUpdated": "2024-01-01T00:00:00"
  },
  "timestamp": "2025-11-17T00:54:23"
}
