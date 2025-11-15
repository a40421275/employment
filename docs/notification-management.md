# 通知管理模块 (NotificationController)

**基础路径**: `/api/notifications`

## 接口列表

### 创建通知
- **URL**: `POST /`
- **请求参数**:
  ```json
  {
    "userId": 1,
    "title": "通知标题",
    "content": "通知内容",
    "type": 1, // 1-系统通知，2-岗位通知，3-申请通知，4-消息通知
    "priority": 1, // 1-低，2-中，3-高
    "relatedId": 1, // 关联ID，如岗位ID、申请ID等
    "relatedType": "job" // 关联类型：job/apply/user等
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
      "title": "通知标题",
      "type": 1,
      "priority": 1,
      "status": 0
    }
  }
  ```
- **功能**: 创建通知

### 获取通知详情
- **URL**: `GET /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 通知ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "id": 1,
      "userId": 1,
      "title": "通知标题",
      "content": "通知内容",
      "type": 1,
      "priority": 1,
      "status": 0,
      "relatedId": 1,
      "relatedType": "job",
      "createTime": "2025-11-08T12:00:00",
      "updateTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 获取通知详情

### 分页查询通知列表
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
          "title": "通知标题",
          "type": 1,
          "priority": 1,
          "status": 0,
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
- **功能**: 分页查询通知

### 根据用户ID查询通知
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
        "title": "通知标题",
        "type": 1,
        "priority": 1,
        "status": 0,
        "createTime": "2025-11-08T12:00:00"
      }
    ]
  }
  ```
- **功能**: 根据用户ID查询通知

### 根据用户ID分页查询通知
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
          "title": "通知标题",
          "type": 1,
          "priority": 1,
          "status": 0,
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
- **功能**: 根据用户ID分页查询通知

### 根据类型查询通知
- **URL**: `GET /type/{type}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | type | 路径 | Integer | 是 | 通知类型：1-系统通知，2-岗位通知，3-申请通知，4-消息通知 |
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
          "title": "通知标题",
          "type": 1,
          "priority": 1,
          "status": 0,
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
- **功能**: 根据类型查询通知

### 根据状态查询通知
- **URL**: `GET /status/{status}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | status | 路径 | Integer | 是 | 通知状态：0-未读，1-已读 |
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
          "title": "通知标题",
          "type": 1,
          "priority": 1,
          "status": 0,
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
- **功能**: 根据状态查询通知

### 根据优先级查询通知
- **URL**: `GET /priority/{priority}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | priority | 路径 | Integer | 是 | 优先级：1-低，2-中，3-高 |
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
          "title": "通知标题",
          "type": 1,
          "priority": 1,
          "status": 0,
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
- **功能**: 根据优先级查询通知

### 根据用户ID和类型查询通知
- **URL**: `GET /user/{userId}/type/{type}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
  | type | 路径 | Integer | 是 | 通知类型 |
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
          "title": "通知标题",
          "type": 1,
          "priority": 1,
          "status": 0,
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
- **功能**: 根据用户ID和类型查询通知

### 根据用户ID和状态查询通知
- **URL**: `GET /user/{userId}/status/{status}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
  | status | 路径 | Integer | 是 | 通知状态 |
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
          "title": "通知标题",
          "type": 1,
          "priority": 1,
          "status": 0,
          "createTime": "2025-11-08T12:00:00"
        }
      ],
      "totalElements": 12,
      "totalPages": 1,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 根据用户ID和状态查询通知

### 搜索通知
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
          "title": "通知标题",
          "type": 1,
          "priority": 1,
          "status": 0,
          "createTime": "2025-11-08T12:00:00"
        }
      ],
      "totalElements": 5,
      "totalPages": 1,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 搜索通知

### 标记为已读
- **URL**: `POST /{id}/read`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 通知ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "标记成功",
    "data": {
      "id": 1,
      "status": 1,
      "readTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 标记通知为已读

### 批量标记为已读
- **URL**: `POST /batch-read`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | ids | 请求体 | List<Long> | 是 | 通知ID列表 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "批量标记成功",
    "data": {
      "successCount": 5,
      "failedCount": 0,
      "totalCount": 5
    }
  }
  ```
- **功能**: 批量标记通知为已读

### 标记所有为已读
- **URL**: `POST /user/{userId}/read-all`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "全部标记成功",
    "data": {
      "userId": 1,
      "markedCount": 15,
      "readTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 标记用户所有通知为已读

### 删除通知
- **URL**: `DELETE /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 通知ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "删除成功",
    "data": null
  }
  ```
- **功能**: 删除通知

### 批量删除通知
- **URL**: `DELETE /batch`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | ids | 请求体 | List<Long> | 是 | 通知ID列表 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "批量删除成功",
    "data": {
      "successCount": 8,
      "failedCount": 0,
      "totalCount": 8
    }
  }
  ```
- **功能**: 批量删除通知

### 清空用户通知
- **URL**: `DELETE /user/{userId}/clear`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "清空成功",
    "data": {
      "userId": 1,
      "deletedCount": 25
    }
  }
  ```
- **功能**: 清空用户所有通知

### 获取未读通知数量
- **URL**: `GET /user/{userId}/unread-count`
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
      "userId": 1,
      "unreadCount": 5
    }
  }
  ```
- **功能**: 获取用户未读通知数量

### 获取最新通知
- **URL**: `GET /user/{userId}/latest`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
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
        "title": "最新通知标题",
        "type": 1,
        "priority": 1,
        "status": 0,
        "createTime": "2025-11-08T12:00:00"
      }
    ]
  }
  ```
- **功能**: 获取用户最新通知

### 获取重要通知
- **URL**: `GET /user/{userId}/important`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
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
        "title": "重要通知标题",
        "type": 1,
        "priority": 3,
        "status": 0,
        "createTime": "2025-11-08T12:00:00"
      }
    ]
  }
  ```
- **功能**: 获取用户重要通知

### 统计通知数量
- **URL**: `GET /stats/count`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 查询 | Long | 否 | 用户ID |
  | type | 查询 | Integer | 否 | 通知类型 |
  | status | 查询 | Integer | 否 | 通知状态 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "统计成功",
    "data": {
      "totalCount": 50,
      "unreadCount": 15,
      "readCount": 35,
      "systemCount": 10,
      "jobCount": 20,
      "applyCount": 15,
      "messageCount": 5
    }
  }
  ```
- **功能**: 统计通知数量

### 获取通知统计
- **URL**: `GET /stats/summary`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 查询 | Long | 否 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "userId": 1,
      "totalNotifications": 50,
      "unreadNotifications": 15,
      "todayNotifications": 5,
      "weekNotifications": 20,
      "monthNotifications": 35
    }
  }
  ```
- **功能**: 获取通知统计摘要

### 发送系统通知
- **URL**: `POST /system`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | title | 请求体 | String | 是 | 通知标题 |
  | content | 请求体 | String | 是 | 通知内容 |
  | priority | 请求体 | Integer | 否 | 优先级，默认2 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "发送成功",
    "data": {
      "sentCount": 100,
      "title": "系统通知标题",
      "priority": 2
    }
  }
  ```
- **功能**: 发送系统通知给所有用户

### 发送岗位通知
- **URL**: `POST /job`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 请求体 | Long | 是 | 岗位ID |
  | title | 请求体 | String | 是 | 通知标题 |
  | content | 请求体 | String | 是 | 通知内容 |
  | userIds | 请求体 | List<Long> | 否 | 用户ID列表，为空则发送给所有用户 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "发送成功",
    "data": {
      "sentCount": 50,
      "jobId": 1,
      "title": "岗位通知标题",
      "userCount": 50
    }
  }
  ```
- **功能**: 发送岗位相关通知

## 使用示例

### 创建通知示例
```bash
curl -X POST "http://localhost:8080/api/notifications" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "userId": 1,
    "title": "面试邀请通知",
    "content": "您已收到面试邀请，请及时查看详情",
    "type": 3,
    "priority": 3,
    "relatedId": 1,
    "relatedType": "apply"
  }'
```

### 获取用户通知列表示例
```bash
curl -X GET "http://localhost:8080/api/notifications/user/1" \
  -H "Authorization: Bearer {token}"
```

### 标记通知为已读示例
```bash
curl -X POST "http://localhost:8080/api/notifications/1/read" \
  -H "Authorization: Bearer {token}"
```

### 获取未读通知数量示例
```bash
curl -X GET "http://localhost:8080/api/notifications/user/1/unread-count" \
  -H "Authorization: Bearer {token}"
```

## 注意事项

- 所有通知管理接口都需要有效的JWT Token进行认证
- 通知类型：1-系统通知，2-岗位通知，3-申请通知，4-消息通知
- 通知状态：0-未读，1-已读
- 优先级：1-低，2-中，3-高
- 分页查询默认页码从0开始，每页大小默认20
- 通知支持关联对象，如岗位、申请等
- 系统通知会自动发送给所有用户
- 通知支持批量操作，提高效率

[返回主文档](../docs/README.md)
