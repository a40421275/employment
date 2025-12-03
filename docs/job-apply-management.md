# 岗位申请模块 (JobApplyController)

**基础路径**: `/api/job-applies`

## 统一响应格式

所有接口都使用统一的响应格式，包含以下字段：

### 成功响应格式
```json
{
  "code": 200,
  "message": "操作成功消息",
  "data": { ... },
  "timestamp": "2025-11-30T10:35:18"
}
```

### 错误响应格式
```json
{
  "code": 400,
  "message": "错误消息",
  "data": null,
  "timestamp": "2025-11-30T10:35:18"
}
```

### 响应字段说明
| 字段名 | 数据类型 | 说明 |
|--------|----------|------|
| code | Integer | 状态码：200-成功，400-客户端错误，500-服务器错误 |
| message | String | 响应消息，描述操作结果 |
| data | Object | 响应数据，具体内容根据接口不同而变化 |
| timestamp | String | 响应时间戳，ISO格式 |

## JobApplyDTO 对象结构

```json
{
  "id": 1,
  "userId": 1,
  "jobId": 1,
  "resumeId": 1,
  "status": 1,
  "matchScore": 85.50,
  "applyNotes": "申请备注信息",
  "interviewTime": "2025-11-15 14:00:00",
  "interviewLocation": "北京市朝阳区科技大厦10层",
  "feedback": "面试反馈信息",
  "createTime": "2025-11-08T10:00:00",
  "updateTime": "2025-11-08T10:00:00",
  "jobTitle": "Java开发工程师",
  "companyName": "某科技公司",
  "resumeName": "张三的简历",
  "userName": "zhangsan",
  "userEmail": "zhangsan@example.com"
}
```

### 字段说明

| 字段名 | 数据类型 | 说明 |
|--------|----------|------|
| id | Long | 申请ID，主键 |
| userId | Long | 用户ID |
| jobId | Long | 岗位ID |
| resumeId | Long | 简历ID |
| status | Integer | 申请状态：1-已投递，2-已查看，3-已通知面试，4-面试通过，5-已拒绝 |
| matchScore | BigDecimal | 匹配度分数，范围0-100 |
| applyNotes | String | 申请备注 |
| interviewTime | LocalDateTime | 面试时间，格式：yyyy-MM-dd HH:mm:ss |
| interviewLocation | String | 面试地点 |
| feedback | String | 反馈信息 |
| createTime | LocalDateTime | 创建时间 |
| updateTime | LocalDateTime | 更新时间 |
| jobTitle | String | 岗位标题 |
| companyName | String | 公司名称 |
| resumeName | String | 简历名称 |
| userName | String | 用户名 |
| userEmail | String | 用户邮箱 |

## 接口列表

### 申请岗位
- **URL**: `POST /`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 请求体 | Long | 是 | 用户ID |
  | jobId | 请求体 | Long | 是 | 岗位ID |
  | resumeId | 请求体 | Long | 是 | 简历ID |
  | status | 请求体 | Integer | 否 | 申请状态，默认1（已投递） |
  | applyNotes | 请求体 | String | 否 | 申请备注 |
  | feedback | 请求体 | String | 否 | 反馈信息 |
  | interviewTime | 请求体 | String | 否 | 面试时间，格式：yyyy-MM-dd HH:mm:ss |
  | interviewLocation | 请求体 | String | 否 | 面试地点 |

  请求示例：
  ```json
  {
    "userId": 1,
    "jobId": 1,
    "resumeId": 1,
    "applyNotes": "申请备注",
    "status": 1,
    "feedback": "反馈信息",
    "interviewTime": "2025-11-15 14:00:00",
    "interviewLocation": "面试地点"
  }
  ```
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "申请岗位成功",
    "data": { ... }, // JobApplyDTO 对象
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 创建岗位申请

### 更新岗位申请
- **URL**: `PUT /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 申请ID |
  | userId | 请求体 | Long | 否 | 用户ID |
  | jobId | 请求体 | Long | 否 | 岗位ID |
  | resumeId | 请求体 | Long | 否 | 简历ID |
  | status | 请求体 | Integer | 否 | 申请状态 |
  | applyNotes | 请求体 | String | 否 | 申请备注 |
  | feedback | 请求体 | String | 否 | 反馈信息 |
  | interviewTime | 请求体 | String | 否 | 面试时间，格式：yyyy-MM-dd HH:mm:ss |
  | interviewLocation | 请求体 | String | 否 | 面试地点 |

  请求示例：
  ```json
  {
    "userId": 1,
    "jobId": 1,
    "resumeId": 1,
    "applyNotes": "更新后的申请备注",
    "status": 2,
    "feedback": "更新后的反馈信息",
    "interviewTime": "2025-11-20 10:00:00",
    "interviewLocation": "新的面试地点"
  }
  ```
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "更新岗位申请成功",
    "data": { ... }, // 更新后的 JobApplyDTO 对象
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 更新岗位申请信息

### 删除岗位申请
- **URL**: `DELETE /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 申请ID |
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "删除岗位申请成功",
    "data": null,
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 删除岗位申请

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
    "message": "获取申请详情成功",
    "data": { ... }, // JobApplyDTO 实体对象
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 获取申请详情

### 分页查询申请列表
- **URL**: `GET /`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认10 |
  | sort | 查询 | String | 否 | 排序字段，如：id |
  | direction | 查询 | String | 否 | 排序方向：ASC/DESC，默认DESC |
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "查询申请列表成功",
    "data": {
      "content": [...], // JobApplyDTO 实体列表
      "totalElements": 100,
      "totalPages": 10,
      "size": 10,
      "number": 0
    },
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 分页查询申请列表

### 根据用户ID查询申请列表
- **URL**: `GET /user/{userId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "查询用户申请列表成功",
    "data": {
      "content": [...], // JobApplyDTO 实体列表
      "totalElements": 10,
      "totalPages": 1,
      "size": 10,
      "number": 0
    },
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 根据用户ID查询申请列表

### 根据用户ID分页查询申请列表
- **URL**: `GET /user/{userId}/page`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认10 |
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "查询用户申请列表成功",
    "data": {
      "content": [...], // JobApplyDTO 实体列表
      "totalElements": 100,
      "totalPages": 10,
      "size": 10,
      "number": 0
    },
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 根据用户ID分页查询申请列表

### 根据岗位ID查询申请列表
- **URL**: `GET /job/{jobId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 路径 | Long | 是 | 岗位ID |
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "查询岗位申请列表成功",
    "data": {
      "content": [...], // JobApplyDTO 实体列表
      "totalElements": 10,
      "totalPages": 1,
      "size": 10,
      "number": 0
    },
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 根据岗位ID查询申请列表

### 根据岗位ID分页查询申请列表
- **URL**: `GET /job/{jobId}/page`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 路径 | Long | 是 | 岗位ID |
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认10 |
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "查询岗位申请列表成功",
    "data": {
      "content": [...], // JobApplyDTO 实体列表
      "totalElements": 100,
      "totalPages": 10,
      "size": 10,
      "number": 0
    },
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 根据岗位ID分页查询申请列表

### 根据简历ID查询申请列表
- **URL**: `GET /resume/{resumeId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | resumeId | 路径 | Long | 是 | 简历ID |
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "查询简历申请列表成功",
    "data": {
      "content": [...], // JobApplyDTO 实体列表
      "totalElements": 10,
      "totalPages": 1,
      "size": 10,
      "number": 0
    },
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 根据简历ID查询申请列表

### 根据状态查询申请列表
- **URL**: `GET /status/{status}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | status | 路径 | Integer | 是 | 申请状态（1-5） |
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "查询状态申请列表成功",
    "data": {
      "content": [...], // JobApplyDTO 实体列表
      "totalElements": 10,
      "totalPages": 1,
      "size": 10,
      "number": 0
    },
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 根据状态查询申请列表

### 根据用户ID和状态查询申请列表
- **URL**: `GET /user/{userId}/status/{status}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
  | status | 路径 | Integer | 是 | 申请状态（1-5） |
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "查询用户状态申请列表成功",
    "data": {
      "content": [...], // JobApplyDTO 实体列表
      "totalElements": 10,
      "totalPages": 1,
      "size": 10,
      "number": 0
    },
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 根据用户ID和状态查询申请列表

### 根据岗位ID和状态查询申请列表
- **URL**: `GET /job/{jobId}/status/{status}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 路径 | Long | 是 | 岗位ID |
  | status | 路径 | Integer | 是 | 申请状态（1-5） |
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "查询岗位状态申请列表成功",
    "data": {
      "content": [...], // JobApplyDTO 实体列表
      "totalElements": 10,
      "totalPages": 1,
      "size": 10,
      "number": 0
    },
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 根据岗位ID和状态查询申请列表

### 更新申请状态
- **URL**: `PUT /{id}/status`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 申请ID |
  | status | 查询 | Integer | 是 | 申请状态（1-5） |
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "更新申请状态成功",
    "data": { ... }, // 更新后的 JobApplyDTO 实体对象
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 更新申请状态

### 设置面试时间
- **URL**: `PUT /{id}/interview`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 申请ID |
  | interviewTime | 查询 | String | 是 | 面试时间，格式：yyyy-MM-dd HH:mm:ss |
  | interviewLocation | 查询 | String | 是 | 面试地点 |
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "设置面试时间成功",
    "data": { ... }, // 更新后的 JobApplyDTO 实体对象
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 设置面试时间

### 添加申请反馈
- **URL**: `PUT /{id}/feedback`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 申请ID |
  | feedback | 查询 | String | 是 | 反馈内容 |
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "添加申请反馈成功",
    "data": { ... }, // 更新后的 JobApplyDTO 实体对象
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 添加申请反馈

### 面试通过
- **URL**: `POST /{id}/pass-interview`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 申请ID |
  | feedback | 查询 | String | 否 | 反馈内容（可选） |
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "面试通过操作成功",
    "data": { ... }, // 更新后的 JobApplyDTO 实体对象
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 标记面试通过

### 拒绝申请
- **URL**: `POST /{id}/reject`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 申请ID |
  | reason | 查询 | String | 是 | 拒绝原因 |
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "拒绝申请操作成功",
    "data": { ... }, // 更新后的 JobApplyDTO 实体对象
    "timestamp": "2025-11-30T10:35:18"
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
    "message": "取消申请操作成功",
    "data": { ... }, // 更新后的 JobApplyDTO 实体对象
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 取消申请

### 计算匹配度
- **URL**: `GET /match-score`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 查询 | Long | 是 | 岗位ID |
  | resumeId | 查询 | Long | 是 | 简历ID |
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "计算匹配度成功",
    "data": 85.50, // 匹配度分数（BigDecimal类型）
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 计算岗位与简历的匹配度

### 检查用户是否已申请该岗位
- **URL**: `GET /check-apply`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 查询 | Long | 是 | 用户ID |
  | jobId | 查询 | Long | 是 | 岗位ID |
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "检查申请状态成功",
    "data": true, // 布尔值（true-已申请，false-未申请）
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 检查用户是否已申请该岗位

### 检查是否已申请
- **URL**: `GET /check-applied`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 查询 | Long | 是 | 用户ID |
  | jobId | 查询 | Long | 是 | 岗位ID |
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "检查申请状态成功",
    "data": {
      "applied": true,
      "applyId": 1,
      "applyTime": "2025-11-08T12:00:00",
      "status": 1,
      "statusDesc": "已投递"
    },
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 检查申请状态并返回详细信息

### 统计各状态的申请数量
- **URL**: `GET /stats/status`
- **请求参数**: 无
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "统计申请状态成功",
    "data": {
      "1": 10,
      "2": 5,
      "3": 3,
      "4": 1,
      "5": 2
    },
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 统计各状态的申请数量

### 统计用户申请数量
- **URL**: `GET /user/{userId}/count`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "统计用户申请数量成功",
    "data": 21, // 申请数量（Long类型）
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 统计用户申请数量

### 统计岗位申请数量
- **URL**: `GET /job/{jobId}/count`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 路径 | Long | 是 | 岗位ID |
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "统计岗位申请数量成功",
    "data": 15, // 申请数量（Long类型）
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 统计岗位申请数量

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
    "message": "获取热门申请岗位成功",
    "data": [...], // 热门岗位列表（Object[]数组）
    "timestamp": "2025-11-30T10:35:18"
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
    "message": "获取最新申请成功",
    "data": {
      "content": [...], // JobApplyDTO 实体列表
      "totalElements": 10,
      "totalPages": 1,
      "size": 10,
      "number": 0
    },
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 获取最新申请

### 获取用户申请统计
- **URL**: `GET /user/{userId}/stats`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
- **响应参数**: 
  ```json
  {
    "code": 200,
    "message": "获取用户申请统计成功",
    "data": {
      "status_1": 10,
      "status_2": 5,
      "status_3": 3,
      "status_4": 1,
      "status_5": 2,
      "totalApplies": 21,
      "interviewInvites": 3,
      "interviewPassed": 1
    },
    "timestamp": "2025-11-30T10:35:18"
  }
  ```
- **功能**: 获取用户申请统计

## 申请状态说明

| 状态码 | 状态名称 | 说明 |
|--------|----------|------|
| 1 | 已投递 | 用户已提交申请 |
| 2 | 已查看 | 企业已查看申请 |
| 3 | 已通知面试 | 企业已发送面试通知 |
| 4 | 面试通过 | 面试已通过 |
| 5 | 已拒绝 | 申请被拒绝 |

## 使用示例

### 申请岗位示例
```bash
curl -X POST "http://localhost:8080/api/job-applies" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "userId": 1,
    "jobId": 1,
    "resumeId": 1,
    "applyNotes": "尊敬的HR，我对贵公司的Java开发岗位非常感兴趣...",
    "status": 1
  }'
```

### 获取用户申请列表示例
```bash
curl -X GET "http://localhost:8080/api/job-applies/user/1" \
  -H "Authorization: Bearer {token}"
```

### 设置面试时间示例
```bash
curl -X PUT "http://localhost:8080/api/job-applies/1/interview" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -H "Authorization: Bearer {token}" \
  -d "interviewTime=2025-11-15 14:00:00&interviewLocation=北京市朝阳区科技大厦10层"
```

### 面试通过示例
```bash
curl -X POST "http://localhost:8080/api/job-applies/1/pass-interview" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -H "Authorization: Bearer {token}" \
  -d "feedback=面试表现优秀，符合岗位要求"
```

### 检查是否已申请示例
```bash
curl -X GET "http://localhost:8080/api/job-applies/check-applied?userId=1&jobId=1" \
  -H "Authorization: Bearer {token}"
```

## 注意事项

- 所有岗位申请接口都需要有效的JWT Token进行认证
- 申请状态：1-已投递，2-已查看，3-已通知面试，4-面试通过，5-已拒绝
- 分页查询默认页码从0开始，每页大小默认10
- 一个用户对同一个岗位只能申请一次
- 申请记录包含完整的申请流程信息
- 面试通知、录用、拒绝等操作会触发相应的通知
- 所有接口都使用统一的响应格式，包含code、message、data、timestamp字段
- 异常时返回400状态码和具体的错误信息

[返回主文档](../docs/README.md)
