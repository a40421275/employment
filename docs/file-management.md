# 文件管理模块 (FileController)

**基础路径**: `/api/files`

## 接口列表

### 上传文件
- **URL**: `POST /upload`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | file | 表单 | MultipartFile | 是 | 上传的文件 |
  | userId | 表单 | Long | 是 | 用户ID |
  | fileType | 表单 | String | 是 | 文件类型：resume/avatar/id_card/other |
  | description | 表单 | String | 否 | 文件描述 |
  | tags | 表单 | String | 否 | 文件标签，逗号分隔 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "上传成功",
    "data": {
      "id": 1,
      "fileName": "example.pdf",
      "fileSize": 1024000,
      "fileType": "resume",
      "fileUrl": "/files/download/1",
      "uploadTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 上传文件

### 批量上传文件
- **URL**: `POST /batch-upload`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | files | 表单 | List<MultipartFile> | 是 | 上传的文件列表 |
  | userId | 表单 | Long | 是 | 用户ID |
  | fileType | 表单 | String | 是 | 文件类型：resume/avatar/id_card/other |
  | descriptions | 表单 | List<String> | 否 | 文件描述列表 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "批量上传成功",
    "data": {
      "successCount": 5,
      "failedCount": 0,
      "failedFiles": [],
      "uploadedFiles": [
        {
          "id": 1,
          "fileName": "file1.pdf",
          "fileSize": 1024000,
          "fileType": "resume",
          "fileUrl": "/files/download/1"
        }
      ]
    }
  }
  ```
- **功能**: 批量上传文件

### 下载文件
- **URL**: `GET /download/{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 文件ID |
- **响应参数**: 文件流
- **功能**: 下载文件

### 获取文件信息
- **URL**: `GET /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 文件ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "id": 1,
      "fileName": "example.pdf",
      "fileSize": 1024000,
      "fileType": "resume",
      "fileUrl": "/files/download/1",
      "userId": 1,
      "description": "个人简历",
      "tags": "简历,PDF",
      "uploadTime": "2025-11-08T12:00:00",
      "downloadCount": 5,
      "status": 1
    }
  }
  ```
- **功能**: 获取文件信息

### 删除文件
- **URL**: `DELETE /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 文件ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "删除成功",
    "data": null
  }
  ```
- **功能**: 删除文件

### 获取文件列表
- **URL**: `GET /`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
  | sort | 查询 | String | 否 | 排序字段，如：uploadTime |
  | direction | 查询 | String | 否 | 排序方向：ASC/DESC，默认DESC |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "content": [
        {
          "id": 1,
          "fileName": "example.pdf",
          "fileSize": 1024000,
          "fileType": "resume",
          "fileUrl": "/files/download/1",
          "userId": 1,
          "uploadTime": "2025-11-08T12:00:00",
          "downloadCount": 5
        }
      ],
      "totalElements": 100,
      "totalPages": 5,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 获取文件列表

### 根据用户获取文件
- **URL**: `GET /user/{userId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
  | fileType | 查询 | String | 否 | 文件类型：resume/avatar/id_card/other |
  | status | 查询 | Integer | 否 | 文件状态：1-正常，2-已删除 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "content": [
        {
          "id": 1,
          "fileName": "example.pdf",
          "fileSize": 1024000,
          "fileType": "resume",
          "fileUrl": "/files/download/1",
          "uploadTime": "2025-11-08T12:00:00",
          "downloadCount": 5
        }
      ],
      "totalElements": 50,
      "totalPages": 3,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 根据用户获取文件

### 根据类型获取文件
- **URL**: `GET /type/{fileType}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | fileType | 路径 | String | 是 | 文件类型：resume/avatar/id_card/other |
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
  | userId | 查询 | Long | 否 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "content": [
        {
          "id": 1,
          "fileName": "example.pdf",
          "fileSize": 1024000,
          "fileType": "resume",
          "fileUrl": "/files/download/1",
          "userId": 1,
          "uploadTime": "2025-11-08T12:00:00"
        }
      ],
      "totalElements": 30,
      "totalPages": 2,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 根据类型获取文件

### 搜索文件
- **URL**: `GET /search`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | keyword | 查询 | String | 是 | 搜索关键词 |
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
  | fileType | 查询 | String | 否 | 文件类型：resume/avatar/id_card/other |
  | userId | 查询 | Long | 否 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "搜索成功",
    "data": {
      "content": [
        {
          "id": 1,
          "fileName": "example.pdf",
          "fileSize": 1024000,
          "fileType": "resume",
          "fileUrl": "/files/download/1",
          "userId": 1,
          "uploadTime": "2025-11-08T12:00:00"
        }
      ],
      "totalElements": 25,
      "totalPages": 2,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 搜索文件

### 更新文件信息
- **URL**: `PUT /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 文件ID |
  | FileDTO | 请求体 | Object | 是 | 文件信息对象 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新成功",
    "data": {
      "id": 1,
      "fileName": "example.pdf",
      "description": "更新后的文件描述",
      "tags": "简历,PDF,更新",
      "status": 1
    }
  }
  ```
- **功能**: 更新文件信息

### 获取文件统计
- **URL**: `GET /stats`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "totalFiles": 1000,
      "totalSize": 1024000000,
      "fileTypes": {
        "resume": 500,
        "avatar": 200,
        "id_card": 100,
        "other": 200
      },
      "dailyUploadCount": 50,
      "weeklyUploadCount": 350,
      "monthlyUploadCount": 1000,
      "totalDownloadCount": 5000,
      "averageFileSize": 1024000
    }
  }
  ```
- **功能**: 获取文件统计

### 清理临时文件
- **URL**: `DELETE /cleanup-temp`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | days | 查询 | Integer | 否 | 清理多少天前的临时文件，默认7天 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "清理成功",
    "data": {
      "deletedCount": 50,
      "freedSpace": 51200000
    }
  }
  ```
- **功能**: 清理临时文件

### 获取文件预览
- **URL**: `GET /{id}/preview`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 文件ID |
  | width | 查询 | Integer | 否 | 预览宽度 |
  | height | 查询 | Integer | 否 | 预览高度 |
- **响应参数**: 预览图片流
- **功能**: 获取文件预览

### 验证文件归属
- **URL**: `GET /{id}/belongs-to/{userId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 文件ID |
  | userId | 路径 | Long | 是 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "验证成功",
    "data": {
      "fileId": 1,
      "fileName": "example.pdf",
      "userId": 1,
      "belongsToUser": true,
      "fileOwner": "张三"
    }
  }
  ```
- **功能**: 验证文件是否属于用户

### 复制文件
- **URL**: `POST /{id}/copy`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 文件ID |
  | targetUserId | 请求体 | Long | 是 | 目标用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "复制成功",
    "data": {
      "originalFileId": 1,
      "originalFileName": "example.pdf",
      "copiedFileId": 2,
      "copiedFileName": "example_copy.pdf",
      "targetUserId": 2,
      "targetUserName": "李四",
      "copyTime": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 复制文件

### 移动文件
- **URL**: `POST /{id}/move`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 文件ID |
  | targetUserId | 请求体 | Long | 是 | 目标用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "移动成功",
    "data": {
      "fileId": 1,
      "fileName": "example.pdf",
      "originalUserId": 1,
      "originalUserName": "张三",
      "targetUserId": 2,
      "targetUserName": "李四",
      "moveTime": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 移动文件

### 获取文件历史版本
- **URL**: `GET /{id}/versions`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 文件ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "versionId": 1,
        "fileId": 1,
        "fileName": "example_v1.pdf",
        "fileSize": 1024000,
        "versionNumber": 1,
        "createTime": "2025-11-08T12:00:00",
        "creator": "张三",
        "description": "初始版本"
      },
      {
        "versionId": 2,
        "fileId": 1,
        "fileName": "example_v2.pdf",
        "fileSize": 1025000,
        "versionNumber": 2,
        "createTime": "2025-11-08T14:00:00",
        "creator": "张三",
        "description": "更新简历内容"
      }
    ]
  }
  ```
- **功能**: 获取文件历史版本

### 恢复文件版本
- **URL**: `POST /{id}/restore/{versionId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 文件ID |
  | versionId | 路径 | Long | 是 | 版本ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "恢复成功",
    "data": {
      "fileId": 1,
      "fileName": "example.pdf",
      "restoredVersionId": 1,
      "restoredVersionNumber": 1,
      "restoreTime": "2025-11-08T22:00:00",
      "restoredBy": "张三"
    }
  }
  ```
- **功能**: 恢复文件版本

### 获取文件分享链接
- **URL**: `POST /{id}/share`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 文件ID |
  | expireTime | 请求体 | String | 否 | 过期时间 |
  | password | 请求体 | String | 否 | 访问密码 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "分享成功",
    "data": {
      "fileId": 1,
      "fileName": "example.pdf",
      "shareLink": "https://example.com/files/share/abc123",
      "shareCode": "abc123",
      "expireTime": "2025-11-15T12:00:00",
      "hasPassword": true,
      "createTime": "2025-11-08T22:00:00",
      "creator": "张三"
    }
  }
  ```
- **功能**: 获取文件分享链接

### 取消文件分享
- **URL**: `DELETE /{id}/share`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 文件ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "取消分享成功",
    "data": {
      "fileId": 1,
      "fileName": "example.pdf",
      "cancelTime": "2025-11-08T22:00:00",
      "cancelledBy": "张三"
    }
  }
  ```
- **功能**: 取消文件分享

## 使用示例

### 上传文件示例
```bash
curl -X POST "http://localhost:8080/api/files/upload" \
  -H "Authorization: Bearer {token}" \
  -F "file=@resume.pdf" \
  -F "userId=1" \
  -F "fileType=resume" \
  -F "description=个人简历" \
  -F "tags=简历,PDF"
```

### 下载文件示例
```bash
curl -X GET "http://localhost:8080/api/files/download/1" \
  -H "Authorization: Bearer {token}" \
  --output resume.pdf
```

### 获取文件信息示例
```bash
curl -X GET "http://localhost:8080/api/files/1" \
  -H "Authorization: Bearer {token}"
```

### 获取用户文件列表示例
```bash
curl -X GET "http://localhost:8080/api/files/user/1?fileType=resume&page=0&size=10" \
  -H "Authorization: Bearer {token}"
```

### 更新文件信息示例
```bash
curl -X PUT "http://localhost:8080/api/files/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "description": "更新后的简历描述",
    "tags": "简历,PDF,Java开发"
  }'
```

## 注意事项

- 所有文件管理接口都需要有效的JWT Token进行认证
- 文件类型：resume（简历）、avatar（头像）、id_card（身份证）、other（其他）
- 文件状态：1-正常，2-已删除
- 文件大小限制：单个文件不超过10MB
- 支持的文件格式：PDF、DOC、DOCX、JPG、PNG等
- 分页查询默认页码从0开始，每页大小默认20
- 文件支持搜索和筛选功能
- 文件支持版本管理和历史记录
- 文件支持分享功能，可设置过期时间和访问密码
- 临时文件自动清理，避免存储空间浪费

[返回主文档](../docs/README.md)
