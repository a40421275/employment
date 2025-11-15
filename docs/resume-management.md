# 简历管理模块 (ResumeController)

**基础路径**: `/api/resumes`

## 接口列表

### 创建简历
- **URL**: `POST /`
- **请求参数**:
  ```json
  {
    "userId": 1,
    "title": "简历标题",
    "content": "JSON格式的简历内容",
    "fileUrl": "文件URL",
    "fileType": "pdf", // pdf/doc/docx
    "isDefault": false,
    "privacyLevel": 1 // 1-公开，2-仅投递企业，3-隐藏
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "创建成功",
    "data": {
      "id": 1,
      "userId": 1,
      "title": "简历标题",
      "fileType": "pdf",
      "isDefault": false,
      "privacyLevel": 1
    }
  }
  ```
- **功能**: 创建简历

### 更新简历
- **URL**: `PUT /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 简历ID |
  | ResumeDTO | 请求体 | Object | 是 | 简历信息对象 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新成功",
    "data": {
      "id": 1,
      "userId": 1,
      "title": "简历标题",
      "fileType": "pdf",
      "isDefault": true,
      "privacyLevel": 1,
      "updateTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 更新简历

### 删除简历
- **URL**: `DELETE /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 简历ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "删除成功",
    "data": null
  }
  ```
- **功能**: 删除简历

### 获取简历详情
- **URL**: `GET /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 简历ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "id": 1,
      "userId": 1,
      "title": "简历标题",
      "content": "JSON格式的简历内容",
      "fileUrl": "文件URL",
      "fileType": "pdf",
      "isDefault": true,
      "privacyLevel": 1,
      "createTime": "2025-11-08T12:00:00",
      "updateTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 获取简历详情

### 分页查询简历列表
- **URL**: `GET /`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
  | sort | 查询 | String | 否 | 排序字段，如：createTime |
  | direction | 查询 | String | 否 | 排序方向：ASC/DESC，默认DESC |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": {
      "content": [
        {
          "id": 1,
          "userId": 1,
          "title": "简历标题",
          "fileType": "pdf",
          "isDefault": true,
          "privacyLevel": 1,
          "createTime": "2025-11-08T12:00:00"
        }
      ],
      "totalElements": 50,
      "totalPages": 3,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 分页查询简历

### 根据用户ID查询简历
- **URL**: `GET /user/{userId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": [
      {
        "id": 1,
        "userId": 1,
        "title": "简历标题",
        "fileType": "pdf",
        "isDefault": true,
        "privacyLevel": 1,
        "createTime": "2025-11-08T12:00:00"
      }
    ]
  }
  ```
- **功能**: 根据用户ID查询简历

### 根据用户ID分页查询简历
- **URL**: `GET /user/{userId}/page`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": {
      "content": [
        {
          "id": 1,
          "userId": 1,
          "title": "简历标题",
          "fileType": "pdf",
          "isDefault": true,
          "privacyLevel": 1,
          "createTime": "2025-11-08T12:00:00"
        }
      ],
      "totalElements": 10,
      "totalPages": 1,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 根据用户ID分页查询简历

### 获取默认简历
- **URL**: `GET /user/{userId}/default`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "id": 1,
      "userId": 1,
      "title": "默认简历标题",
      "content": "JSON格式的简历内容",
      "fileUrl": "文件URL",
      "fileType": "pdf",
      "isDefault": true,
      "privacyLevel": 1,
      "createTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 获取用户默认简历

### 设置默认简历
- **URL**: `POST /{id}/set-default`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 简历ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "设置成功",
    "data": {
      "id": 1,
      "isDefault": true
    }
  }
  ```
- **功能**: 设置默认简历

### 根据隐私级别查询简历
- **URL**: `GET /privacy/{privacyLevel}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | privacyLevel | 路径 | Integer | 是 | 隐私级别：1-公开，2-仅投递企业，3-隐藏 |
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": {
      "content": [
        {
          "id": 1,
          "userId": 1,
          "title": "简历标题",
          "fileType": "pdf",
          "isDefault": true,
          "privacyLevel": 1,
          "createTime": "2025-11-08T12:00:00"
        }
      ],
      "totalElements": 15,
      "totalPages": 1,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 根据隐私级别查询简历

### 根据文件类型查询简历
- **URL**: `GET /file-type/{fileType}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | fileType | 路径 | String | 是 | 文件类型：pdf/doc/docx |
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": {
      "content": [
        {
          "id": 1,
          "userId": 1,
          "title": "简历标题",
          "fileType": "pdf",
          "isDefault": true,
          "privacyLevel": 1,
          "createTime": "2025-11-08T12:00:00"
        }
      ],
      "totalElements": 20,
      "totalPages": 1,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 根据文件类型查询简历

### 搜索简历
- **URL**: `GET /search`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | keyword | 查询 | String | 是 | 搜索关键词 |
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "搜索成功",
    "data": {
      "content": [
        {
          "id": 1,
          "userId": 1,
          "title": "简历标题",
          "fileType": "pdf",
          "isDefault": true,
          "privacyLevel": 1,
          "createTime": "2025-11-08T12:00:00"
        }
      ],
      "totalElements": 8,
      "totalPages": 1,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 搜索简历

### 更新隐私级别
- **URL**: `PUT /{id}/privacy`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 简历ID |
  | privacyLevel | 请求体 | Integer | 是 | 隐私级别：1-公开，2-仅投递企业，3-隐藏 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新成功",
    "data": {
      "id": 1,
      "privacyLevel": 2
    }
  }
  ```
- **功能**: 更新隐私级别

### 获取公开简历
- **URL**: `GET /public`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": {
      "content": [
        {
          "id": 1,
          "userId": 1,
          "title": "公开简历标题",
          "fileType": "pdf",
          "isDefault": true,
          "privacyLevel": 1,
          "createTime": "2025-11-08T12:00:00"
        }
      ],
      "totalElements": 25,
      "totalPages": 2,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 获取公开简历

### 获取热门简历
- **URL**: `GET /hot`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | limit | 查询 | Integer | 否 | 限制数量，默认10 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "userId": 1,
        "title": "热门简历标题",
        "fileType": "pdf",
        "isDefault": true,
        "privacyLevel": 1,
        "viewCount": 500,
        "downloadCount": 100,
        "createTime": "2025-11-08T12:00:00"
      }
    ]
  }
  ```
- **功能**: 获取热门简历

### 统计用户简历数量
- **URL**: `GET /user/{userId}/count`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "统计成功",
    "data": {
      "userId": 1,
      "totalCount": 5,
      "defaultCount": 1,
      "publicCount": 3,
      "privateCount": 2
    }
  }
  ```
- **功能**: 统计用户简历数量

### 验证简历归属
- **URL**: `GET /{id}/belongs-to/{userId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 简历ID |
  | userId | 路径 | Long | 是 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "验证成功",
    "data": {
      "belongs": true,
      "resumeId": 1,
      "userId": 1
    }
  }
  ```
- **功能**: 验证简历是否属于用户

## 使用示例

### 创建简历示例
```bash
curl -X POST "http://localhost:8080/api/resumes" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "userId": 1,
    "title": "Java开发工程师简历",
    "content": "{\"basicInfo\": {\"name\": \"张三\", \"age\": 25}, \"education\": [{\"school\": \"清华大学\", \"major\": \"计算机科学\"}]}",
    "fileUrl": "/files/resume1.pdf",
    "fileType": "pdf",
    "isDefault": true,
    "privacyLevel": 1
  }'
```

### 获取用户简历列表示例
```bash
curl -X GET "http://localhost:8080/api/resumes/user/1" \
  -H "Authorization: Bearer {token}"
```

### 设置默认简历示例
```bash
curl -X POST "http://localhost:8080/api/resumes/1/set-default" \
  -H "Authorization: Bearer {token}"
```

## 注意事项

- 所有简历管理接口都需要有效的JWT Token进行认证
- 隐私级别：1-公开，2-仅投递企业，3-隐藏
- 文件类型支持：pdf/doc/docx
- 每个用户只能有一个默认简历
- 简历内容为JSON格式，包含基本信息、教育经历、工作经历等
- 分页查询默认页码从0开始，每页大小默认20

[返回主文档](../docs/README.md)
