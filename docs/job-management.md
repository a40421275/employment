# 岗位管理模块 (JobController)

**基础路径**: `/api/jobs`

## 接口列表

### 创建岗位
- **URL**: `POST /`
- **请求参数** (JobDTO对象):
  | 字段名 | 类型 | 必填 | 说明 | 默认值 |
  |--------|------|------|------|--------|
  | title | String | 是 | 岗位标题 | - |
  | categoryId | Long | 是 | 岗位分类ID | - |
  | companyId | Long | 是 | 企业ID | - |
  | department | String | 否 | 部门 | - |
  | jobType | Integer | 否 | 岗位类型：1-全职，2-兼职，3-实习 | 1 |
  | salaryMin | BigDecimal | 否 | 最低薪资 | - |
  | salaryMax | BigDecimal | 否 | 最高薪资 | - |
  | salaryUnit | String | 否 | 薪资单位 | "月" |
  | workCity | String | 否 | 工作城市 | - |
  | workAddress | String | 否 | 工作地址 | - |
  | description | String | 否 | 岗位描述 | - |
  | requirements | String | 否 | 任职要求 | - |
  | benefits | String | 否 | 福利待遇 | - |
  | contactInfo | String | 否 | 联系方式 | - |
  | educationRequirement | String | 否 | 学历要求 | - |
  | experienceRequirement | String | 否 | 经验要求 | - |
  | recruitNumber | Integer | 否 | 招聘人数 | 1 |
  | urgentLevel | Integer | 否 | 紧急程度：0-普通，1-紧急，2-非常紧急 | 0 |
  | priorityLevel | Integer | 否 | 优先级：0-普通，1-重要，2-非常重要 | 0 |
  | isRecommended | Boolean | 否 | 是否推荐 | false |
  | recommendReason | String | 否 | 推荐理由 | - |
  | keywords | String | 否 | 关键词 | - |
  | status | Integer | 否 | 岗位状态：0-草稿，1-已发布，2-已下架，3-已归档 | 0 |
  | publishTime | LocalDateTime | 否 | 发布时间 | - |
  | expireTime | LocalDateTime | 否 | 过期时间 | - |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "创建成功",
    "data": {
      "id": 1,
      "title": "岗位标题",
      "categoryId": 1,
      "companyId": 1,
      "jobType": 1,
      "salaryMin": 8000,
      "salaryMax": 15000,
      "salaryUnit": "月",
      "workCity": "工作城市",
      "status": 0
    }
  }
  ```
- **功能**: 创建岗位

### 更新岗位
- **URL**: `PUT /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 岗位ID |
  | JobDTO | 请求体 | Object | 是 | 岗位信息对象，字段同创建岗位接口 |

- **JobDTO对象属性**:
  | 字段名 | 类型 | 必填 | 说明 | 默认值 |
  |--------|------|------|------|--------|
  | title | String | 是 | 岗位标题 | - |
  | categoryId | Long | 是 | 岗位分类ID | - |
  | companyId | Long | 是 | 企业ID | - |
  | department | String | 否 | 部门 | - |
  | jobType | Integer | 否 | 岗位类型：1-全职，2-兼职，3-实习 | 1 |
  | salaryMin | BigDecimal | 否 | 最低薪资 | - |
  | salaryMax | BigDecimal | 否 | 最高薪资 | - |
  | salaryUnit | String | 否 | 薪资单位 | "月" |
  | workCity | String | 否 | 工作城市 | - |
  | workAddress | String | 否 | 工作地址 | - |
  | description | String | 否 | 岗位描述 | - |
  | requirements | String | 否 | 任职要求 | - |
  | benefits | String | 否 | 福利待遇 | - |
  | contactInfo | String | 否 | 联系方式 | - |
  | educationRequirement | String | 否 | 学历要求 | - |
  | experienceRequirement | String | 否 | 经验要求 | - |
  | recruitNumber | Integer | 否 | 招聘人数 | 1 |
  | urgentLevel | Integer | 否 | 紧急程度：0-普通，1-紧急，2-非常紧急 | 0 |
  | priorityLevel | Integer | 否 | 优先级：0-普通，1-重要，2-非常重要 | 0 |
  | isRecommended | Boolean | 否 | 是否推荐 | false |
  | recommendReason | String | 否 | 推荐理由 | - |
  | keywords | String | 否 | 关键词 | - |
  | status | Integer | 否 | 岗位状态：0-草稿，1-已发布，2-已下架，3-已归档 | 0 |
  | publishTime | LocalDateTime | 否 | 发布时间 | - |
  | expireTime | LocalDateTime | 否 | 过期时间 | - |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新成功",
    "data": {
      "id": 1,
      "title": "岗位标题",
      "categoryId": 1,
      "companyId": 1,
      "jobType": 1,
      "salaryMin": 8000,
      "salaryMax": 15000,
      "salaryUnit": "月",
      "workCity": "工作城市",
      "status": 1
    }
  }
  ```
- **功能**: 更新岗位

### 删除岗位
- **URL**: `DELETE /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 岗位ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "删除成功",
    "data": null
  }
  ```
- **功能**: 删除岗位

### 获取岗位详情
- **URL**: `GET /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 岗位ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "id": 1,
      "title": "岗位标题",
      "categoryId": 1,
      "companyId": 1,
      "companyName": "公司名称",
      "department": "部门",
      "jobType": 1,
      "salaryMin": 8000,
      "salaryMax": 15000,
      "salaryUnit": "月",
      "workCity": "工作城市",
      "workAddress": "工作地址",
      "description": "岗位描述",
      "requirements": "任职要求",
      "benefits": "福利待遇",
      "contactInfo": "联系方式",
      "educationRequirement": "本科",
      "experienceRequirement": "1-3年",
      "recruitNumber": 2,
      "status": 1,
      "viewCount": 100,
      "applyCount": 10,
      "publishTime": "2025-11-08T12:00:00",
      "expireTime": "2025-12-08T12:00:00",
      "createTime": "2025-11-08T12:00:00",
      "updateTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 获取岗位详情

### 统一查询岗位列表
- **URL**: `POST /query`
- **请求参数** (JobQueryDTO对象):
  | 字段名 | 类型 | 必填 | 说明 | 默认值 |
  |--------|------|------|------|--------|
  | keyword | String | 否 | 搜索关键词 | - |
  | categoryId | Long | 否 | 岗位分类ID | - |
  | workCity | String | 否 | 工作城市 | - |
  | jobType | Integer | 否 | 岗位类型：1-全职，2-兼职，3-实习 | - |
  | minSalary | Double | 否 | 最低薪资 | - |
  | maxSalary | Double | 否 | 最高薪资 | - |
  | status | Integer | 否 | 岗位状态：0-草稿，1-已发布，2-已下架，3-已归档 | 1 |
  | page | Integer | 否 | 页码，从0开始 | 0 |
  | size | Integer | 否 | 每页大小 | 10 |
  | sort | String | 否 | 排序字段 | "id" |
  | direction | String | 否 | 排序方向：ASC/DESC | "DESC" |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": {
      "content": [
        {
          "id": 1,
          "title": "岗位标题",
          "categoryId": 1,
          "categoryName": "技术类",
          "companyId": 2,
          "companyName": "测试企业有限公司",
          "jobType": 1,
          "salaryMin": 8000,
          "salaryMax": 15000,
          "salaryUnit": "月",
          "workCity": "工作城市",
          "status": 1,
          "viewCount": 100,
          "applyCount": 10,
          "publishTime": "2025-11-08T12:00:00",
          "expireTime": "2025-12-08T12:00:00"
        }
      ],
      "totalElements": 100,
      "totalPages": 10,
      "size": 10,
      "number": 0
    }
  }
  ```
- **功能**: 统一查询岗位列表，支持多条件筛选、搜索、分页，替代原有的多个查询接口

### 发布岗位
- **URL**: `POST /{id}/publish`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 岗位ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "发布成功",
    "data": {
      "id": 1,
      "status": 1
    }
  }
  ```
- **功能**: 发布岗位

### 下架岗位
- **URL**: `POST /{id}/offline`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 岗位ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "下架成功",
    "data": {
      "id": 1,
      "status": 2
    }
  }
  ```
- **功能**: 下架岗位

### 归档岗位
- **URL**: `POST /{id}/archive`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 岗位ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "归档成功",
    "data": {
      "id": 1,
      "status": 3
    }
  }
  ```
- **功能**: 归档岗位

### 获取活跃岗位
- **URL**: `GET /active`
- **请求参数**: 无
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": [
      {
        "id": 1,
        "title": "岗位标题",
        "categoryId": 1,
        "companyId": 1,
        "companyName": "公司名称",
        "jobType": 1,
        "salaryMin": 8000,
        "salaryMax": 15000,
        "workCity": "工作城市",
        "status": 1,
        "viewCount": 100,
        "applyCount": 10,
        "createTime": "2025-11-08T12:00:00",
        "updateTime": "2025-11-08T12:00:00"
      }
    ]
  }
  ```
- **功能**: 获取活跃岗位（已发布且未过期）

### 获取即将过期岗位
- **URL**: `GET /expiring`
- **请求参数**: 无
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": [
      {
        "id": 1,
        "title": "岗位标题",
        "categoryId": 1,
        "companyId": 1,
        "companyName": "公司名称",
        "jobType": 1,
        "salaryMin": 8000,
        "salaryMax": 15000,
        "workCity": "工作城市",
        "status": 1,
        "viewCount": 100,
        "applyCount": 10,
        "expireTime": "2025-11-15T12:00:00",
        "createTime": "2025-11-08T12:00:00",
        "updateTime": "2025-11-08T12:00:00"
      }
    ]
  }
  ```
- **功能**: 获取即将过期岗位

### 统计岗位状态
- **URL**: `GET /stats/status`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "统计成功",
    "data": {
      "draft": 10,
      "published": 50,
      "offline": 5,
      "archived": 15
    }
  }
  ```
- **功能**: 统计各状态岗位数量

### 统计城市岗位
- **URL**: `GET /stats/city`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "统计成功",
    "data": {
      "北京": 25,
      "上海": 20,
      "深圳": 15,
      "广州": 10,
      "杭州": 8,
      "其他": 22
    }
  }
  ```
- **功能**: 统计各城市岗位数量

### 获取热门岗位
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
        "title": "热门岗位标题",
        "companyName": "公司名称",
        "jobType": 1,
        "salaryMin": 15000,
        "salaryMax": 25000,
        "workCity": "工作城市",
        "viewCount": 1000,
        "applyCount": 50
      }
    ]
  }
  ```
- **功能**: 获取热门岗位

### 获取最新岗位
- **URL**: `GET /latest`
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
        "title": "最新岗位标题",
        "companyName": "公司名称",
        "jobType": 1,
        "salaryMin": 12000,
        "salaryMax": 20000,
        "workCity": "工作城市",
        "createTime": "2025-11-08T12:00:00"
      }
    ]
  }
  ```
- **功能**: 获取最新岗位

## 使用示例

### 创建岗位示例
```bash
curl -X POST "http://localhost:8080/api/jobs" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "title": "Java开发工程师",
    "categoryId": 1,
    "companyId": 1,
    "companyName": "科技公司",
    "department": "技术部",
    "jobType": 1,
    "salaryMin": 15000,
    "salaryMax": 25000,
    "salaryUnit": "月",
    "workCity": "北京",
    "workAddress": "北京市朝阳区",
    "description": "负责Java后端开发",
    "requirements": "3年以上Java开发经验",
    "benefits": "五险一金，带薪年假",
    "contactInfo": "hr@company.com"
  }'
```

### 统一查询岗位示例
```bash
curl -X POST "http://localhost:8080/api/jobs/query" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "keyword": "Java",
    "workCity": "北京",
    "jobType": 1,
    "minSalary": 10000,
    "maxSalary": 20000,
    "status": 1,
    "page": 0,
    "size": 10,
    "sort": "publishTime",
    "direction": "DESC"
  }'
```

### 获取岗位详情示例
```bash
curl -X GET "http://localhost:8080/api/jobs/1" \
  -H "Authorization: Bearer {token}"
```

## 注意事项

- 所有岗位管理接口都需要有效的JWT Token进行认证
- 岗位状态：0-草稿，1-已发布，2-已下架，3-已归档
- 岗位类型：1-全职，2-兼职，3-实习
- 紧急程度：0-普通，1-紧急，2-非常紧急
- 优先级：0-普通，1-重要，2-非常重要
- 学历要求：不限、高中、大专、本科、硕士、博士
- 经验要求：不限、应届生、1年、1-3年、3-5年、5-10年、10年以上
- 分页查询默认页码从0开始，每页大小默认20
- 搜索和筛选功能支持多条件组合查询
- 关键词字段用于搜索优化，支持逗号分隔的多个关键词

[返回主文档](../docs/README.md)
