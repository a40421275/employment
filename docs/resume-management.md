# 简历管理模块 (ResumeController)

**基础路径**: `/api/resumes`

## 模块概述

简历管理模块经过重构，统一了附件简历和结构化简历的管理，简化了接口设计，提高了系统性能。

### 主要改进
- 统一简历类型管理：附件简历（类型1）和结构化简历（类型2）
- 优化接口设计：提供15个核心接口满足各种业务需求
- 优化数据结构：新增简历类型、同步配置、模板样式等字段
- 增强查询能力：支持多条件组合查询、搜索和分页

## 接口列表

### 创建简历
- **URL**: `POST /`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | ResumeCreateDTO | 请求体 | Object | 是 | 简历创建信息对象 |

  **ResumeCreateDTO对象结构**:
  ```json
  {
    "userId": 1,               // 整数，必填，用户ID
    "title": "简历标题",         // 字符串，必填，简历标题
    "resumeType": 1,           // 整数，必填，简历类型：1-附件简历，2-结构化简历
    "fileUrl": "文件URL",        // 字符串，可选，文件URL（附件简历必填）
    "fileType": "pdf",          // 字符串，可选，文件类型：pdf/doc/docx（附件简历必填）
    "basicInfo": {              // 对象，可选，基础信息
      "realName": "张三",
      "gender": 1,
      "birthday": "1995-12-10",
      "avatar": "/avatars/user1.jpg",
      "phone": "13800138000",
      "email": "zhangsan@example.com",
      "city": "北京",
      "education": "本科",
      "workYears": 3,
      "currentSalary": 15000.0,
      "expectedSalary": 20000.0,
      "selfIntro": "个人简介",
      "preferredCities": ["北京", "上海", "深圳"], // 数组，期望城市列表
      "jobTypes": ["全职", "兼职"],               // 数组，工作类型列表
      "industries": ["互联网", "金融"],           // 数组，行业偏好列表
      "workMode": "全职",                        // 字符串，工作模式：全职/兼职/实习
      "jobStatus": "积极求职"                    // 字符串，求职状态：积极求职/观望中/在职看机会
    },
    "structuredData": {         // 对象，可选，结构化简历数据（结构化简历必填）
      "educations": [
        {
          "school": "清华大学",
          "major": "计算机科学与技术",
          "degree": "本科",
          "startDate": "2014-09-01",
          "endDate": "2018-06-30",
          "description": "教育经历描述"
        }
      ],
      "workExperiences": [
        {
          "company": "某科技公司",
          "position": "Java开发工程师",
          "startDate": "2018-07-01",
          "endDate": "2020-06-30",
          "description": "工作经历描述",
          "achievements": ["成就1", "成就2"]
        }
      ],
      "skills": [
        {
          "name": "Java",
          "level": "精通",
          "description": "熟练使用Java开发"
        }
      ],
      "projects": [
        {
          "name": "项目名称",
          "role": "开发工程师",
          "startDate": "2019-03-01",
          "endDate": "2020-02-28",
          "description": "项目描述",
          "technologies": ["Spring Boot", "MySQL"],
          "responsibilities": ["负责模块开发", "参与系统设计"]
        }
      ],
      "certificates": [
        {
          "name": "Java高级工程师认证",
          "issuingAuthority": "Oracle",
          "issueDate": "2020-03-20",
          "description": "Java高级工程师认证"
        }
      ]
    },
    "settings": {               // 对象，可选，简历设置
      "isDefault": false,       // 布尔值，是否默认简历，默认false
      "privacyLevel": 1,        // 整数，隐私级别：1-公开，2-仅投递企业，3-隐藏，默认1
      "templateStyle": "modern", // 字符串，模板样式，默认modern
      "showPhoto": true,        // 布尔值，是否显示照片，默认true
      "showSalary": false       // 布尔值，是否显示薪资，默认false
    }
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "创建简历成功",
    "data": {
      "id": 1,
      "userId": 1,
      "title": "简历标题",
      "resumeType": 1,
      "fileUrl": "文件URL",
      "fileType": "pdf",
      "isDefault": false,
      "privacyLevel": 1,
      "syncProfileData": false,
      "lastSyncTime": null,
      "viewCount": 0,
      "downloadCount": 0,
      "shareCount": 0,
      "lastViewTime": null,
      "structuredData": null,
      "templateStyle": "modern",
      "colorScheme": "blue",
      "fontFamily": "Arial",
      "showPhoto": true,
      "showSalary": false,
      "createTime": "2025-11-27T09:00:00",
      "updateTime": "2025-11-27T09:00:00"
    }
  }
  ```
- **功能**: 创建简历，支持附件简历和结构化简历

### 更新简历
- **URL**: `PUT /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 简历ID |
  | ResumeUpdateDTO | 请求体 | Object | 是 | 简历更新信息对象 |

  **ResumeUpdateDTO对象结构**:
  ```json
  {
    "title": "更新后的简历标题",   // 字符串，可选，简历标题
    "isDefault": true,          // 布尔值，可选，是否默认简历
    "privacyLevel": 2,          // 整数，可选，隐私级别
    "syncProfileData": true,    // 布尔值，可选，是否同步用户资料
    "basicInfo": {              // 对象，可选，基础信息
      "realName": "张三",
      "gender": 1,
      "birthday": "1995-12-10",
      "phone": "13800138000",
      "email": "zhangsan@example.com",
      "avatar": "/avatars/user1.jpg",
      "city": "北京",
      "education": "本科",
      "workYears": 3,
      "currentSalary": 15000.0,
      "expectedSalary": 20000.0,
      "selfIntro": "个人简介",
      "preferredCities": ["北京", "上海", "深圳"], // 数组，期望城市列表
      "jobTypes": ["全职", "兼职"],               // 数组，工作类型列表
      "industries": ["互联网", "金融"],           // 数组，行业偏好列表
      "workMode": "全职",                        // 字符串，工作模式：全职/兼职/实习
      "jobStatus": "积极求职"                    // 字符串，求职状态：积极求职/观望中/在职看机会
    },
    "structuredData": {         // 对象，可选，结构化简历数据
      "educations": [...],
      "workExperiences": [...],
      "skills": [...],
      "projects": [...],
      "certificates": [...]
    },
    "templateStyle": "classic",  // 字符串，可选，模板样式
    "colorScheme": "green",     // 字符串，可选，配色方案
    "fontFamily": "Microsoft YaHei", // 字符串，可选，字体
    "showPhoto": false,         // 布尔值，可选，是否显示照片
    "showSalary": true          // 布尔值，可选，是否显示薪资
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新简历成功",
    "data": {
      "id": 1,
      "userId": 1,
      "title": "更新后的简历标题",
      "resumeType": 2,
      "fileUrl": null,
      "fileType": null,
      "isDefault": true,
      "privacyLevel": 2,
      "syncProfileData": true,
      "lastSyncTime": "2025-11-27T09:30:00",
      "viewCount": 45,
      "downloadCount": 12,
      "shareCount": 3,
      "lastViewTime": "2025-11-27T09:25:00",
      "structuredData": {...},
      "templateStyle": "classic",
      "colorScheme": "green",
      "fontFamily": "Microsoft YaHei",
      "showPhoto": false,
      "showSalary": true,
      "createTime": "2025-11-27T09:00:00",
      "updateTime": "2025-11-27T09:30:00"
    }
  }
  ```
- **功能**: 更新简历信息

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
- **功能**: 删除简历，同时会自动清理该简历关联的所有附件信息
- **注意事项**: 
  - 删除简历时会自动清理该简历关联的所有附件文件
  - 附件清理包括删除附件记录和物理文件
  - 如果附件清理失败，简历删除操作仍会继续，但会记录错误日志

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
      "resumeType": 2,
      "fileUrl": null,
      "fileType": null,
      "isDefault": true,
      "privacyLevel": 1,
      "syncProfileData": true,
      "lastSyncTime": "2025-11-27T09:30:00",
      "viewCount": 45,
      "downloadCount": 12,
      "shareCount": 3,
      "lastViewTime": "2025-11-27T09:25:00",
      "basicInfo": {
        "realName": "张三",
        "gender": 1,
        "birthday": "1995-12-10",
        "phone": "13800138000",
        "email": "zhangsan@example.com",
        "avatar": "/avatars/user1.jpg",
        "city": "北京",
        "education": "本科",
        "workYears": 3,
        "selfIntro": "个人简介",
        "preferredCities": ["北京", "上海", "深圳"], // 数组，期望城市列表
        "jobTypes": ["全职", "兼职"],               // 数组，工作类型列表
        "industries": ["互联网", "金融"],           // 数组，行业偏好列表
        "workMode": "全职",                        // 字符串，工作模式：全职/兼职/实习
        "jobStatus": "积极求职"                    // 字符串，求职状态：积极求职/观望中/在职看机会
      },
      "structuredData": {
        "educations": [...],
        "workExperiences": [...],
        "skills": [...],
        "projects": [...],
        "certificates": [...]
      },
      "settings": {
        "templateStyle": "modern",
        "colorScheme": "blue",
        "fontFamily": "Arial",
        "showPhoto": true,
        "showSalary": false
      },
      "stats": {
        "viewCount": 45,
        "downloadCount": 12,
        "shareCount": 3,
        "isDefault": true,
        "privacyLevel": 1,
        "completeness": 85
      },
      "createTime": "2025-11-27T09:00:00",
      "updateTime": "2025-11-27T09:30:00"
    }
  }
  ```
- **功能**: 获取简历详情，包含完整的结构化数据

### 分页查询简历列表
- **URL**: `GET /`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
  | sort | 查询 | String | 否 | 排序字段，如：createTime |
  | direction | 查询 | String | 否 | 排序方向：ASC/DESC，默认DESC |
  | resumeType | 查询 | Integer | 否 | 简历类型：1-附件简历，2-结构化简历 |
  | privacyLevel | 查询 | Integer | 否 | 隐私级别：1-公开，2-仅投递企业，3-隐藏 |
  | keyword | 查询 | String | 否 | 搜索关键词 |
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
          "resumeType": 2,
          "fileType": null,
          "isDefault": true,
          "privacyLevel": 1,
          "viewCount": 45,
          "downloadCount": 12,
          "shareCount": 3,
          "lastViewTime": "2025-11-27T09:25:00",
          "templateStyle": "modern",
          "colorScheme": "blue",
          "fontFamily": "Arial",
          "showPhoto": true,
          "showSalary": false,
          "createTime": "2025-11-27T09:00:00",
          "updateTime": "2025-11-27T09:30:00"
        }
      ],
      "totalElements": 50,
      "totalPages": 3,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 分页查询简历，支持多条件组合查询

### 获取当前用户简历列表
- **URL**: `GET /my`
- **请求参数**:
  | 无
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
        "resumeType": 2,
        "fileType": null,
        "isDefault": true,
        "privacyLevel": 1,
        "viewCount": 45,
        "downloadCount": 12,
        "shareCount": 3,
        "lastViewTime": "2025-11-27T09:25:00",
        "templateStyle": "modern",
        "colorScheme": "blue",
        "fontFamily": "Arial",
        "showPhoto": true,
        "showSalary": false,
        "createTime": "2025-11-27T09:00:00",
        "updateTime": "2025-11-27T09:30:00"
      }
    ]
  }
  ```
- **功能**: 根据用户ID查询简历列表

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
      "resumeType": 2,
      "fileUrl": null,
      "fileType": null,
      "isDefault": true,
      "privacyLevel": 1,
      "syncProfileData": true,
      "lastSyncTime": "2025-11-27T09:30:00",
      "viewCount": 45,
      "downloadCount": 12,
      "shareCount": 3,
      "lastViewTime": "2025-11-27T09:25:00",
      "structuredData": {...},
      "templateStyle": "modern",
      "colorScheme": "blue",
      "fontFamily": "Arial",
      "showPhoto": true,
      "showSalary": false,
      "createTime": "2025-11-27T09:00:00",
      "updateTime": "2025-11-27T09:30:00"
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
    "message": "设置默认简历成功",
    "data": {
      "id": 1,
      "userId": 1,
      "title": "简历标题",
      "resumeType": 2,
      "fileUrl": null,
      "fileType": null,
      "isDefault": true,
      "privacyLevel": 1,
      "syncProfileData": true,
      "lastSyncTime": "2025-11-27T09:30:00",
      "viewCount": 45,
      "downloadCount": 12,
      "shareCount": 3,
      "lastViewTime": "2025-11-27T09:25:00",
      "structuredData": {...},
      "templateStyle": "modern",
      "colorScheme": "blue",
      "fontFamily": "Arial",
      "showPhoto": true,
      "showSalary": false,
      "createTime": "2025-11-27T09:00:00",
      "updateTime": "2025-11-27T09:30:00"
    }
  }
  ```
- **功能**: 设置默认简历

### 同步用户资料数据
- **URL**: `POST /{id}/sync-profile`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 简历ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "同步用户资料成功",
    "data": {
      "id": 1,
      "userId": 1,
      "title": "简历标题",
      "resumeType": 2,
      "fileUrl": null,
      "fileType": null,
      "isDefault": true,
      "privacyLevel": 1,
      "syncProfileData": true,
      "lastSyncTime": "2025-11-27T09:30:00",
      "viewCount": 45,
      "downloadCount": 12,
      "shareCount": 3,
      "lastViewTime": "2025-11-27T09:25:00",
      "structuredData": {...},
      "templateStyle": "modern",
      "colorScheme": "blue",
      "fontFamily": "Arial",
      "showPhoto": true,
      "showSalary": false,
      "createTime": "2025-11-27T09:00:00",
      "updateTime": "2025-11-27T09:30:00"
    }
  }
  ```
- **功能**: 同步用户资料数据到简历

### 更新简历隐私级别
- **URL**: `PUT /{id}/privacy`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 简历ID |
  | privacyLevel | 查询 | Integer | 是 | 隐私级别：1-公开，2-仅投递企业，3-隐藏 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新隐私级别成功",
    "data": {
      "id": 1,
      "userId": 1,
      "title": "简历标题",
      "resumeType": 2,
      "fileUrl": null,
      "fileType": null,
      "isDefault": true,
      "privacyLevel": 2,
      "syncProfileData": true,
      "lastSyncTime": "2025-11-27T09:30:00",
      "viewCount": 45,
      "downloadCount": 12,
      "shareCount": 3,
      "lastViewTime": "2025-11-27T09:25:00",
      "structuredData": {...},
      "templateStyle": "modern",
      "colorScheme": "blue",
      "fontFamily": "Arial",
      "showPhoto": true,
      "showSalary": false,
      "createTime": "2025-11-27T09:00:00",
      "updateTime": "2025-11-27T09:30:00"
    }
  }
  ```
- **功能**: 更新简历隐私级别

### 搜索简历
- **URL**: `GET /search`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | keyword | 查询 | String | 是 | 搜索关键词 |
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认10 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "搜索简历成功",
    "data": {
      "content": [
        {
          "id": 1,
          "userId": 1,
          "title": "Java开发工程师简历",
          "resumeType": 2,
          "fileType": null,
          "isDefault": true,
          "privacyLevel": 1,
          "viewCount": 45,
          "downloadCount": 12,
          "shareCount": 3,
          "lastViewTime": "2025-11-27T09:25:00",
          "templateStyle": "modern",
          "colorScheme": "blue",
          "fontFamily": "Arial",
          "showPhoto": true,
          "showSalary": false,
          "createTime": "2025-11-27T09:00:00",
          "updateTime": "2025-11-27T09:30:00"
        }
      ],
      "totalElements": 50,
      "totalPages": 5,
      "size": 10,
      "number": 0
    }
  }
  ```
- **功能**: 根据关键词搜索简历

### 获取热门简历
- **URL**: `GET /hot`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | limit | 查询 | Integer | 否 | 返回数量，默认10 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取热门简历成功",
    "data": [
      {
        "id": 1,
        "userId": 1,
        "title": "热门简历标题",
        "resumeType": 2,
        "fileType": null,
        "isDefault": true,
        "privacyLevel": 1,
        "viewCount": 150,
        "downloadCount": 45,
        "shareCount": 12,
        "lastViewTime": "2025-11-27T09:25:00",
        "templateStyle": "modern",
        "colorScheme": "blue",
        "fontFamily": "Arial",
        "showPhoto": true,
        "showSalary": false,
        "createTime": "2025-11-27T09:00:00",
        "updateTime": "2025-11-27T09:30:00"
      }
    ]
  }
  ```
- **功能**: 获取热门简历（按浏览量排序）

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
    "message": "统计简历数量成功",
    "data": {
      "count": 5
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
    "message": "验证简历归属成功",
    "data": {
      "belongs": true
    }
  }
  ```
- **功能**: 验证简历是否属于指定用户

## 使用示例

### 创建附件简历示例
```bash
curl -X POST "http://localhost:8080/api/resumes" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "userId": 1,
    "title": "Java开发工程师简历",
    "resumeType": 1,
    "fileUrl": "/uploads/resumes/1/java_developer.pdf",
    "fileType": "pdf",
    "settings": {
      "isDefault": true,
      "privacyLevel": 1,
      "templateStyle": "modern",
      "showPhoto": true,
      "showSalary": false
    }
  }'
```

### 创建结构化简历示例
```bash
curl -X POST "http://localhost:8080/api/resumes" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "userId": 1,
    "title": "Java开发工程师结构化简历",
    "resumeType": 2,
    "basicInfo": {
      "realName": "张三",
      "gender": 1,
      "birthday": "1995-12-10",
      "avatar": "/avatars/user1.jpg",
      "phone": "13800138000",
      "email": "zhangsan@example.com",
      "city": "北京",
      "education": "本科",
      "workYears": 3,
      "currentSalary": 15000.0,
      "expectedSalary": 20000.0,
      "selfIntro": "个人简介",
      "preferredCities": ["北京", "上海", "深圳"],
      "jobTypes": ["全职", "兼职"],
      "industries": ["互联网", "金融"],
      "workMode": "全职",
      "jobStatus": "积极求职"
    },
    "structuredData": {
      "educations": [
        {
          "school": "清华大学",
          "major": "计算机科学与技术",
          "degree": "本科",
          "startDate": "2014-09-01",
          "endDate": "2018-06-30",
          "description": "教育经历描述"
        }
      ],
      "workExperiences": [
        {
          "company": "某科技公司",
          "position": "Java开发工程师",
          "startDate": "2018-07-01",
          "endDate": "2020-06-30",
          "description": "工作经历描述",
          "achievements": ["成就1", "成就2"]
        }
      ],
      "skills": [
        {
          "name": "Java",
          "level": "精通",
          "description": "熟练使用Java开发"
        }
      ],
      "projects": [
        {
          "name": "项目名称",
          "role": "开发工程师",
          "startDate": "2019-03-01",
          "endDate": "2020-02-28",
          "description": "项目描述",
          "technologies": ["Spring Boot", "MySQL"],
          "responsibilities": ["负责模块开发", "参与系统设计"]
        }
      ],
      "certificates": [
        {
          "name": "Java高级工程师认证",
          "issuingAuthority": "Oracle",
          "issueDate": "2020-03-20",
          "description": "Java高级工程师认证"
        }
      ]
    },
    "settings": {
      "isDefault": true,
      "privacyLevel": 1,
      "templateStyle": "modern",
      "showPhoto": true,
      "showSalary": false
    }
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

### 搜索简历示例
```bash
curl -X GET "http://localhost:8080/api/resumes?keyword=Java&resumeType=2&page=0&size=10" \
  -H "Authorization: Bearer {token}"
```

## 注意事项

- 所有简历管理接口都需要有效的JWT Token进行认证
- 简历类型：1-附件简历，2-结构化简历
- 隐私级别：1-公开，2-仅投递企业，3-隐藏
- 文件类型支持：pdf/doc/docx（仅附件简历）
- 每个用户只能有一个默认简历
- 结构化简历数据存储在 `structured_data` 字段中
- 支持简历模板系统，可自定义样式和布局
- 分页查询默认页码从0开始，每页大小默认20

## 数据结构说明

### 简历实体字段
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 简历ID |
| userId | Long | 用户ID |
| title | String | 简历标题 |
| resumeType | Integer | 简历类型：1-附件简历，2-结构化简历 |
| fileUrl | String | 文件URL（附件简历） |
| fileType | String | 文件类型（附件简历） |
| isDefault | Boolean | 是否默认简历 |
| privacyLevel | Integer | 隐私级别 |
| syncProfileData | Boolean | 是否同步用户资料 |
| lastSyncTime | LocalDateTime | 最后同步时间 |
| viewCount | Integer | 查看次数 |
| downloadCount | Integer | 下载次数 |
| shareCount | Integer | 分享次数 |
| lastViewTime | LocalDateTime | 最后查看时间 |
| structuredData | String | 结构化简历数据（JSON格式） |
| templateStyle | String | 模板样式 |
| colorScheme | String | 配色方案 |
| fontFamily | String | 字体 |
| showPhoto | Boolean | 是否显示照片 |
| showSalary | Boolean | 是否显示薪资 |

### 结构化数据格式
结构化简历数据采用JSON格式存储，包含以下主要部分：

#### 1. 基本信息 (basicInfo)
```json
{
  "realName": "张三",           // 字符串，真实姓名
  "gender": 1,                 // 整数，性别：0-未知，1-男，2-女
  "birthday": "1995-12-10",    // 字符串，生日（YYYY-MM-DD格式）
  "phone": "13800138000",      // 字符串，手机号
  "email": "zhangsan@example.com", // 字符串，邮箱
  "avatar": "/avatars/user1.jpg", // 字符串，头像URL
  "city": "北京",              // 字符串，所在城市
  "education": "本科",         // 字符串，最高学历：高中/大专/本科/硕士/博士
  "workYears": 3,              // 整数，工作年限
  "currentSalary": 15000.0,    // 数字，当前薪资
  "expectedSalary": 20000.0,   // 数字，期望薪资
  "selfIntro": "个人简介",     // 字符串，自我介绍
  "preferredCities": ["北京", "上海", "深圳"], // 数组，期望城市列表
  "jobTypes": ["全职", "兼职"],               // 数组，工作类型列表
  "industries": ["互联网", "金融"],           // 数组，行业偏好列表
  "workMode": "全职",                        // 字符串，工作模式：全职/兼职/实习
  "jobStatus": "积极求职"                    // 字符串，求职状态：积极求职/观望中/在职看机会
}
```

#### 2. 教育经历 (educations)
```json
[
  {
    "school": "清华大学",       // 字符串，学校名称
    "major": "计算机科学与技术", // 字符串，专业
    "degree": "本科",          // 字符串，学历：高中/大专/本科/硕士/博士
    "startDate": "2014-09-01", // 字符串，开始时间（YYYY-MM-DD）
    "endDate": "2018-06-30",   // 字符串，结束时间（YYYY-MM-DD）
    "isCurrent": false,        // 布尔值，是否在读
    "description": "教育经历描述" // 字符串，教育经历描述
  }
]
```

#### 3. 工作经历 (workExperiences)
```json
[
  {
    "company": "某科技公司",    // 字符串，公司名称
    "position": "Java开发工程师", // 字符串，职位
    "industry": "互联网",       // 字符串，行业
    "startDate": "2018-07-01", // 字符串，开始时间（YYYY-MM-DD）
    "endDate": "2020-06-30",   // 字符串，结束时间（YYYY-MM-DD）
    "isCurrent": false,        // 布尔值，是否在职
    "department": "技术部",     // 字符串，部门
    "salary": 15000.0,        // 数字，薪资
    "description": "工作经历描述", // 字符串，工作描述
    "achievements": ["成就1", "成就2"] // 数组，工作成就列表
  }
]
```

#### 4. 专业技能 (skills)
```json
[
  {
    "category": "后端技术",     // 字符串，技能分类：后端技术/数据库/中间件/前端技术等
    "name": "Java",            // 字符串，技能名称
    "proficiency": 4,          // 整数，熟练度：1-了解，2-熟悉，3-熟练，4-精通
    "years": 3,                // 整数，使用年限
    "description": "熟练使用Java开发" // 字符串，技能描述
  }
]
```

#### 5. 项目经验 (projects)
```json
[
  {
    "name": "项目名称",         // 字符串，项目名称
    "role": "开发工程师",       // 字符串，担任角色
    "startDate": "2019-03-01", // 字符串，开始时间（YYYY-MM-DD）
    "endDate": "2020-02-28",   // 字符串，结束时间（YYYY-MM-DD）
    "description": "项目描述",  // 字符串，项目描述
    "technologies": ["Spring Boot", "MySQL"], // 数组，使用技术列表
    "responsibilities": ["负责模块开发", "参与系统设计"], // 数组，职责描述列表
    "achievements": ["项目成就1", "项目成就2"] // 数组，项目成果列表
  }
]
```

#### 6. 证书荣誉 (certificates)
```json
[
  {
    "name": "Java高级工程师认证", // 字符串，证书名称
    "issuingAuthority": "Oracle", // 字符串，颁发机构
    "issueDate": "2020-03-20",  // 字符串，颁发时间（YYYY-MM-DD）
    "expiryDate": "2023-03-20", // 字符串，过期时间（YYYY-MM-DD）
    "level": "高级",           // 字符串，证书级别
    "description": "Java高级工程师认证" // 字符串，证书描述
  }
]
```

#### 7. 简历设置 (settings)
```json
{
  "isDefault": false,          // 布尔值，是否默认简历
  "privacyLevel": 1,           // 整数，隐私级别：1-公开，2-仅投递企业，3-隐藏
  "templateStyle": "modern",   // 字符串，模板样式：modern/classic/professional
  "colorScheme": "blue",       // 字符串，配色方案：blue/green/purple/red
  "fontFamily": "Arial",       // 字符串，字体：Arial/Microsoft YaHei/SimSun
  "showPhoto": true,           // 布尔值，是否显示照片
  "showSalary": false          // 布尔值，是否显示薪资
}
```

### 字段验证规则

#### 必填字段
- **基本信息**: realName, phone, email
- **教育经历**: school, degree
- **工作经历**: company, position
- **专业技能**: name, proficiency
- **项目经验**: name, role

#### 格式验证
- **日期格式**: 必须为 YYYY-MM-DD 格式
- **邮箱格式**: 必须符合邮箱格式规范
- **手机号格式**: 必须为11位数字
- **薪资格式**: 必须为数字类型
- **熟练度范围**: 1-4（了解-精通）

#### 数据完整性检查
- 教育经历按时间倒序排列
- 工作经历按时间倒序排列
- 技能按熟练度降序排列
- 项目经验按时间倒序排列

## 重构总结

### 精简的接口
- 从原来的20+个接口精简到8个核心接口
- 移除了重复的查询接口（如根据隐私级别、文件类型等单独查询）
- 统一了附件简历和结构化简历的管理

### 优化的数据结构
- 新增 `resumeType` 字段区分简历类型
- 新增 `syncProfileData` 和 `lastSyncTime` 支持用户资料同步
- 新增模板样式相关字段支持个性化展示
- 优化统计字段（viewCount、downloadCount、shareCount）

### 增强的查询能力
- 支持多条件组合查询
- 支持分页和排序
- 支持关键词搜索
- 支持按简历类型和隐私级别过滤

### 性能提升
- 减少数据库查询次数
- 优化数据结构存储
- 统一接口逻辑，减少代码重复
- 支持批量操作和缓存机制

## 迁移指南

### 从旧版本迁移
1. 更新数据库表结构（添加新字段）
2. 迁移现有简历数据
3. 更新前端调用接口
4. 测试所有功能正常

### 数据迁移脚本
```sql
-- 添加新字段
ALTER TABLE resume ADD COLUMN resume_type INT DEFAULT 1;
ALTER TABLE resume ADD COLUMN sync_profile_data BOOLEAN DEFAULT FALSE;
ALTER TABLE resume ADD COLUMN last_sync_time DATETIME;
ALTER TABLE resume ADD COLUMN last_view_time DATETIME;
ALTER TABLE resume ADD COLUMN template_style VARCHAR(50);
ALTER TABLE resume ADD COLUMN color_scheme VARCHAR(50);
ALTER TABLE resume ADD COLUMN font_family VARCHAR(100);
ALTER TABLE resume ADD COLUMN show_photo BOOLEAN DEFAULT TRUE;
ALTER TABLE resume ADD COLUMN show_salary BOOLEAN DEFAULT FALSE;

-- 迁移结构化简历数据
UPDATE resume SET resume_type = 2 WHERE file_type = 'structured';
UPDATE resume SET structured_data = content WHERE file_type = 'structured';
```

### 兼容性说明
- 新版本向后兼容旧版本的简历数据
- 结构化简历自动迁移到新格式
- 附件简历保持不变
- 所有现有接口调用仍然有效

## 测试建议

### 功能测试
- 创建附件简历和结构化简历
- 更新简历信息
- 设置默认简历
- 分页查询和搜索
- 删除简历

### 性能测试
- 大数据量下的查询性能
- 并发创建和更新简历
- 简历导出和分享功能

### 安全测试
- 权限验证
- 数据隐私保护
