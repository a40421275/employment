# 岗位申请模块 (JobApplyController)

**基础路径**: `/api/job-applies`

## 接口列表

### 申请岗位
- **URL**: `POST /`
- **请求参数**:
  ```json
  {
    "jobId": 1,
    "userId": 1,
    "resumeId": 1,
    "coverLetter": "求职信内容",
    "expectedSalary": 15000,
    "status": 0 // 0-已申请，1-已查看，2-已邀请面试，3-已录用，4-已拒绝
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "申请成功",
    "data": {
      "id": 1,
      "jobId": 1,
      "userId": 1,
      "resumeId": 1,
      "status": 0,
      "applyTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 申请岗位

### 更新申请状态
- **URL**: `PUT /{id}/status`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 申请ID |
  | status | 请求体 | Integer | 是 | 申请状态：0-已申请，1-已查看，2-已邀请面试，3-已录用，4-已拒绝 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新成功",
    "data": {
      "id": 1,
      "status": 1,
      "updateTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 更新申请状态

### 获取申请详情
- **URL**: `GET /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 申请ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "id": 1,
      "jobId": 1,
      "userId": 1,
      "resumeId": 1,
      "coverLetter": "求职信内容",
      "expectedSalary": 15000,
      "status": 0,
      "applyTime": "2025-11-08T12:00:00",
      "updateTime": "2025-11-08T12:00:00",
      "jobTitle": "Java开发工程师",
      "companyName": "科技公司",
      "userName": "张三"
    }
  }
  ```
- **功能**: 获取申请详情

### 分页查询申请列表
- **URL**: `GET /`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
  | sort | 查询 | String | 否 | 排序字段，如：applyTime |
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
          "jobId": 1,
          "userId": 1,
          "resumeId": 1,
          "status": 0,
          "applyTime": "2025-11-08T12:00:00",
          "jobTitle": "Java开发工程师",
          "companyName": "科技公司",
          "userName": "张三"
        }
      ],
      "totalElements": 100,
      "totalPages": 5,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 分页查询申请

### 根据用户ID查询申请
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
        "jobId": 1,
        "userId": 1,
        "resumeId": 1,
        "status": 0,
        "applyTime": "2025-11-08T12:00:00",
        "jobTitle": "Java开发工程师",
        "companyName": "科技公司"
      }
    ]
  }
  ```
- **功能**: 根据用户ID查询申请

### 根据用户ID分页查询申请
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
          "jobId": 1,
          "userId": 1,
          "resumeId": 1,
          "status": 0,
          "applyTime": "2025-11-08T12:00:00",
          "jobTitle": "Java开发工程师",
          "companyName": "科技公司"
        }
      ],
      "totalElements": 15,
      "totalPages": 1,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 根据用户ID分页查询申请

### 根据岗位ID查询申请
- **URL**: `GET /job/{jobId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 路径 | Long | 是 | 岗位ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": [
      {
        "id": 1,
        "jobId": 1,
        "userId": 1,
        "resumeId": 1,
        "status": 0,
        "applyTime": "2025-11-08T12:00:00",
        "userName": "张三",
        "userEmail": "zhangsan@example.com"
      }
    ]
  }
  ```
- **功能**: 根据岗位ID查询申请

### 根据岗位ID分页查询申请
- **URL**: `GET /job/{jobId}/page`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 路径 | Long | 是 | 岗位ID |
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
          "jobId": 1,
          "userId": 1,
          "resumeId": 1,
          "status": 0,
          "applyTime": "2025-11-08T12:00:00",
          "userName": "张三",
          "userEmail": "zhangsan@example.com"
        }
      ],
      "totalElements": 20,
      "totalPages": 1,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 根据岗位ID分页查询申请

### 根据状态查询申请
- **URL**: `GET /status/{status}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | status | 路径 | Integer | 是 | 申请状态：0-已申请，1-已查看，2-已邀请面试，3-已录用，4-已拒绝 |
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
          "jobId": 1,
          "userId": 1,
          "resumeId": 1,
          "status": 0,
          "applyTime": "2025-11-08T12:00:00",
          "jobTitle": "Java开发工程师",
          "companyName": "科技公司",
          "userName": "张三"
        }
      ],
      "totalElements": 30,
      "totalPages": 2,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 根据状态查询申请

### 根据用户ID和状态查询申请
- **URL**: `GET /user/{userId}/status/{status}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
  | status | 路径 | Integer | 是 | 申请状态 |
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
          "jobId": 1,
          "userId": 1,
          "resumeId": 1,
          "status": 0,
          "applyTime": "2025-11-08T12:00:00",
          "jobTitle": "Java开发工程师",
          "companyName": "科技公司"
        }
      ],
      "totalElements": 10,
      "totalPages": 1,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 根据用户ID和状态查询申请

### 根据岗位ID和状态查询申请
- **URL**: `GET /job/{jobId}/status/{status}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 路径 | Long | 是 | 岗位ID |
  | status | 路径 | Integer | 是 | 申请状态 |
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
          "jobId": 1,
          "userId": 1,
          "resumeId": 1,
          "status": 0,
          "applyTime": "2025-11-08T12:00:00",
          "userName": "张三",
          "userEmail": "zhangsan@example.com"
        }
      ],
      "totalElements": 8,
      "totalPages": 1,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 根据岗位ID和状态查询申请

### 搜索申请
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
          "jobId": 1,
          "userId": 1,
          "resumeId": 1,
          "status": 0,
          "applyTime": "2025-11-08T12:00:00",
          "jobTitle": "Java开发工程师",
          "companyName": "科技公司",
          "userName": "张三"
        }
      ],
      "totalElements": 5,
      "totalPages": 1,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 搜索申请

### 检查是否已申请
- **URL**: `GET /check-applied`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 查询 | Long | 是 | 岗位ID |
  | userId | 查询 | Long | 是 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "检查成功",
    "data": {
      "applied": true,
      "applyId": 1,
      "applyTime": "2025-11-08T12:00:00",
      "status": 0
    }
  }
  ```
- **功能**: 检查用户是否已申请该岗位

### 统计申请数量
- **URL**: `GET /stats/count`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 查询 | Long | 否 | 用户ID |
  | jobId | 查询 | Long | 否 | 岗位ID |
  | status | 查询 | Integer | 否 | 申请状态 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "统计成功",
    "data": {
      "totalCount": 50,
      "appliedCount": 30,
      "viewedCount": 10,
      "interviewCount": 5,
      "hiredCount": 3,
      "rejectedCount": 2
    }
  }
  ```
- **功能**: 统计申请数量

### 获取热门申请岗位
- **URL**: `GET /hot-jobs`
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
        "jobId": 1,
        "jobTitle": "Java开发工程师",
        "companyName": "科技公司",
        "applyCount": 50,
        "viewCount": 200
      }
    ]
  }
  ```
- **功能**: 获取热门申请岗位

### 获取最新申请
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
        "jobId": 1,
        "userId": 1,
        "resumeId": 1,
        "status": 0,
        "applyTime": "2025-11-08T12:00:00",
        "jobTitle": "Java开发工程师",
        "companyName": "科技公司",
        "userName": "张三"
      }
    ]
  }
  ```
- **功能**: 获取最新申请

### 邀请面试
- **URL**: `POST /{id}/invite-interview`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 申请ID |
  | interviewTime | 请求体 | String | 是 | 面试时间 |
  | interviewLocation | 请求体 | String | 是 | 面试地点 |
  | interviewer | 请求体 | String | 是 | 面试官 |
  | notes | 请求体 | String | 否 | 备注 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "邀请成功",
    "data": {
      "id": 1,
      "status": 2,
      "interviewTime": "2025-11-15T14:00:00",
      "interviewLocation": "北京市朝阳区科技大厦10层",
      "interviewer": "张经理"
    }
  }
  ```
- **功能**: 邀请面试

### 录用申请
- **URL**: `POST /{id}/hire`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 申请ID |
  | offerSalary | 请求体 | Integer | 是 | 录用薪资 |
  | startDate | 请求体 | String | 是 | 入职日期 |
  | notes | 请求体 | String | 否 | 备注 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "录用成功",
    "data": {
      "id": 1,
      "status": 3,
      "offerSalary": 15000,
      "startDate": "2025-12-01",
      "updateTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 录用申请

### 拒绝申请
- **URL**: `POST /{id}/reject`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 申请ID |
  | reason | 请求体 | String | 是 | 拒绝原因 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "拒绝成功",
    "data": {
      "id": 1,
      "status": 4,
      "reason": "不符合岗位要求",
      "updateTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 拒绝申请

### 取消申请
- **URL**: `POST /{id}/cancel`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 申请ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "取消成功",
    "data": {
      "id": 1,
      "status": 4,
      "updateTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 取消申请

### 获取申请统计
- **URL**: `GET /stats/summary`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "totalApplications": 100,
      "todayApplications": 5,
      "pendingApplications": 30,
      "interviewApplications": 10,
      "hiredApplications": 5,
      "rejectedApplications": 15
    }
  }
  ```
- **功能**: 获取申请统计摘要

### 获取申请趋势
- **URL**: `GET /stats/trend`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | days | 查询 | Integer | 否 | 天数，默认7 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "trend": [
        {
          "date": "2025-11-01",
          "count": 10
        },
        {
          "date": "2025-11-02",
          "count": 15
        },
        {
          "date": "2025-11-03",
          "count": 12
        }
      ],
      "total": 37,
      "average": 12.3
    }
  }
  ```
- **功能**: 获取申请趋势

## 使用示例

### 申请岗位示例
```bash
curl -X POST "http://localhost:8080/api/job-applies" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "jobId": 1,
    "userId": 1,
    "resumeId": 1,
    "coverLetter": "尊敬的HR，我对贵公司的Java开发岗位非常感兴趣...",
    "expectedSalary": 15000,
    "status": 0
  }'
```

### 获取用户申请列表示例
```bash
curl -X GET "http://localhost:8080/api/job-applies/user/1" \
  -H "Authorization: Bearer {token}"
```

### 邀请面试示例
```bash
curl -X POST "http://localhost:8080/api/job-applies/1/invite-interview" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "interviewTime": "2025-11-15T14:00:00",
    "interviewLocation": "北京市朝阳区科技大厦10层",
    "interviewer": "张经理",
    "notes": "请携带身份证和学历证明"
  }'
```

## 注意事项

- 所有岗位申请接口都需要有效的JWT Token进行认证
- 申请状态：0-已申请，1-已查看，2-已邀请面试，3-已录用，4-已拒绝
- 分页查询默认页码从0开始，每页大小默认20
- 一个用户对同一个岗位只能申请一次
- 申请记录包含完整的申请流程信息
- 面试邀请、录用、拒绝等操作会触发相应的通知

[返回主文档](../docs/README.md)
