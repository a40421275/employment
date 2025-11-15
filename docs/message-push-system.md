# 消息推送模块 (MessagePushController)

**基础路径**: `/api/messages`

## 接口列表

### 发送站内消息
- **URL**: `POST /internal`
- **请求参数**:
  ```json
  {
    "title": "消息标题",
    "content": "消息内容",
    "senderId": 1, // 发送者ID
    "receiverIds": [1, 2, 3], // 接收者ID列表
    "type": 1, // 1-系统消息，2-用户消息，3-岗位消息
    "priority": 1, // 1-低，2-中，3-高
    "expireTime": "2025-11-15T12:00:00" // 过期时间
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "发送成功",
    "data": {
      "messageId": 1,
      "sentCount": 3,
      "failedCount": 0
    }
  }
  ```
- **功能**: 发送站内消息

### 发送邮件
- **URL**: `POST /email`
- **请求参数**:
  ```json
  {
    "subject": "邮件主题",
    "content": "邮件内容",
    "toEmails": ["user1@example.com", "user2@example.com"],
    "ccEmails": ["cc@example.com"],
    "bccEmails": ["bcc@example.com"],
    "templateId": "welcome_email", // 邮件模板ID
    "templateData": {"name": "张三"}, // 模板数据
    "attachments": ["file1.pdf", "file2.jpg"] // 附件列表
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "发送成功",
    "data": {
      "emailId": 1,
      "sentCount": 2,
      "failedCount": 0,
      "failedEmails": []
    }
  }
  ```
- **功能**: 发送邮件

### 发送短信
- **URL**: `POST /sms`
- **请求参数**:
  ```json
  {
    "content": "短信内容",
    "phoneNumbers": ["13800138000", "13800138001"],
    "templateId": "verification_code", // 短信模板ID
    "templateData": {"code": "123456"}, // 模板数据
    "signature": "就业平台" // 短信签名
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "发送成功",
    "data": {
      "smsId": 1,
      "sentCount": 2,
      "failedCount": 0,
      "failedPhones": []
    }
  }
  ```
- **功能**: 发送短信

### 发送微信消息
- **URL**: `POST /wechat`
- **请求参数**:
  ```json
  {
    "openIds": ["openid1", "openid2"],
    "templateId": "job_notification", // 微信模板ID
    "templateData": {
      "first": {"value": "岗位通知"},
      "keyword1": {"value": "Java开发工程师"},
      "keyword2": {"value": "科技公司"},
      "remark": {"value": "请及时查看"}
    },
    "url": "https://example.com/job/1" // 跳转链接
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "发送成功",
    "data": {
      "wechatId": 1,
      "sentCount": 2,
      "failedCount": 0,
      "failedOpenIds": []
    }
  }
  ```
- **功能**: 发送微信消息

### 发送推送通知
- **URL**: `POST /push`
- **请求参数**:
  ```json
  {
    "title": "推送标题",
    "content": "推送内容",
    "userIds": [1, 2, 3],
    "deviceTypes": ["ios", "android"], // 设备类型
    "badge": 1, // 角标数字
    "sound": "default", // 提示音
    "data": {"jobId": 1}, // 自定义数据
    "expireTime": "2025-11-15T12:00:00"
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "发送成功",
    "data": {
      "pushId": 1,
      "sentCount": 3,
      "failedCount": 0,
      "failedUsers": []
    }
  }
  ```
- **功能**: 发送推送通知

### 获取消息详情
- **URL**: `GET /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 消息ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "id": 1,
      "title": "面试邀请通知",
      "content": "您已收到面试邀请，请及时查看详情",
      "senderId": 1,
      "senderName": "系统管理员",
      "receiverId": 2,
      "receiverName": "张三",
      "type": 3,
      "typeName": "岗位消息",
      "priority": 3,
      "priorityName": "高",
      "status": 0,
      "statusName": "未读",
      "expireTime": "2025-11-15T12:00:00",
      "createTime": "2025-11-08T12:00:00",
      "readTime": null,
      "attachments": []
    }
  }
  ```
- **功能**: 获取消息详情

### 分页查询消息列表
- **URL**: `GET /`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
  | type | 查询 | Integer | 否 | 消息类型 |
  | status | 查询 | Integer | 否 | 消息状态 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "content": [
        {
          "id": 1,
          "title": "面试邀请通知",
          "content": "您已收到面试邀请，请及时查看详情",
          "senderName": "系统管理员",
          "receiverName": "张三",
          "type": 3,
          "typeName": "岗位消息",
          "priority": 3,
          "priorityName": "高",
          "status": 0,
          "statusName": "未读",
          "createTime": "2025-11-08T12:00:00"
        }
      ],
      "totalElements": 100,
      "totalPages": 5,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 分页查询消息

### 根据用户ID查询消息
- **URL**: `GET /user/{userId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "title": "面试邀请通知",
        "content": "您已收到面试邀请，请及时查看详情",
        "senderName": "系统管理员",
        "type": 3,
        "typeName": "岗位消息",
        "priority": 3,
        "priorityName": "高",
        "status": 0,
        "statusName": "未读",
        "createTime": "2025-11-08T12:00:00"
      },
      {
        "id": 2,
        "title": "系统通知",
        "content": "系统维护通知",
        "senderName": "系统管理员",
        "type": 1,
        "typeName": "系统消息",
        "priority": 2,
        "priorityName": "中",
        "status": 1,
        "statusName": "已读",
        "createTime": "2025-11-07T10:00:00"
      }
    ]
  }
  ```
- **功能**: 根据用户ID查询消息

### 根据用户ID分页查询消息
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
    "message": "获取成功",
    "data": {
      "content": [
        {
          "id": 1,
          "title": "面试邀请通知",
          "content": "您已收到面试邀请，请及时查看详情",
          "senderName": "系统管理员",
          "type": 3,
          "typeName": "岗位消息",
          "priority": 3,
          "priorityName": "高",
          "status": 0,
          "statusName": "未读",
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
- **功能**: 根据用户ID分页查询消息

### 根据类型查询消息
- **URL**: `GET /type/{type}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | type | 路径 | Integer | 是 | 消息类型 |
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "content": [
        {
          "id": 1,
          "title": "面试邀请通知",
          "content": "您已收到面试邀请，请及时查看详情",
          "senderName": "系统管理员",
          "receiverName": "张三",
          "type": 3,
          "typeName": "岗位消息",
          "priority": 3,
          "priorityName": "高",
          "status": 0,
          "statusName": "未读",
          "createTime": "2025-11-08T12:00:00"
        }
      ],
      "totalElements": 30,
      "totalPages": 2,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 根据类型查询消息

### 根据状态查询消息
- **URL**: `GET /status/{status}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | status | 路径 | Integer | 是 | 消息状态 |
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "content": [
        {
          "id": 1,
          "title": "面试邀请通知",
          "content": "您已收到面试邀请，请及时查看详情",
          "senderName": "系统管理员",
          "receiverName": "张三",
          "type": 3,
          "typeName": "岗位消息",
          "priority": 3,
          "priorityName": "高",
          "status": 0,
          "statusName": "未读",
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
- **功能**: 根据状态查询消息

### 搜索消息
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
    "message": "获取成功",
    "data": {
      "content": [
        {
          "id": 1,
          "title": "面试邀请通知",
          "content": "您已收到面试邀请，请及时查看详情",
          "senderName": "系统管理员",
          "receiverName": "张三",
          "type": 3,
          "typeName": "岗位消息",
          "priority": 3,
          "priorityName": "高",
          "status": 0,
          "statusName": "未读",
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
- **功能**: 搜索消息

### 标记消息为已读
- **URL**: `POST /{id}/read`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 消息ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "标记成功",
    "data": {
      "id": 1,
      "title": "面试邀请通知",
      "status": 1,
      "statusName": "已读",
      "readTime": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 标记消息为已读

### 批量标记为已读
- **URL**: `POST /batch-read`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | ids | 请求体 | List<Long> | 是 | 消息ID列表 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "批量标记成功",
    "data": {
      "successCount": 5,
      "failedCount": 0,
      "failedIds": [],
      "readTime": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 批量标记消息为已读

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
    "message": "标记成功",
    "data": {
      "userId": 1,
      "markedCount": 15,
      "readTime": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 标记用户所有消息为已读

### 删除消息
- **URL**: `DELETE /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 消息ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "删除成功",
    "data": {
      "id": 1,
      "title": "面试邀请通知"
    }
  }
  ```
- **功能**: 删除消息

### 批量删除消息
- **URL**: `DELETE /batch`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | ids | 请求体 | List<Long> | 是 | 消息ID列表 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "批量删除成功",
    "data": {
      "successCount": 5,
      "failedCount": 0,
      "failedIds": []
    }
  }
  ```
- **功能**: 批量删除消息

### 清空用户消息
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
      "clearedCount": 20
    }
  }
  ```
- **功能**: 清空用户所有消息

### 获取未读消息数量
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
      "unreadCount": 5,
      "totalCount": 25
    }
  }
  ```
- **功能**: 获取用户未读消息数量

### 获取最新消息
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
        "title": "面试邀请通知",
        "content": "您已收到面试邀请，请及时查看详情",
        "senderName": "系统管理员",
        "type": 3,
        "typeName": "岗位消息",
        "priority": 3,
        "priorityName": "高",
        "status": 0,
        "statusName": "未读",
        "createTime": "2025-11-08T12:00:00"
      },
      {
        "id": 2,
        "title": "系统通知",
        "content": "系统维护通知",
        "senderName": "系统管理员",
        "type": 1,
        "typeName": "系统消息",
        "priority": 2,
        "priorityName": "中",
        "status": 1,
        "statusName": "已读",
        "createTime": "2025-11-07T10:00:00"
      }
    ]
  }
  ```
- **功能**: 获取用户最新消息

### 获取重要消息
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
        "title": "面试邀请通知",
        "content": "您已收到面试邀请，请及时查看详情",
        "senderName": "系统管理员",
        "type": 3,
        "typeName": "岗位消息",
        "priority": 3,
        "priorityName": "高",
        "status": 0,
        "statusName": "未读",
        "createTime": "2025-11-08T12:00:00"
      }
    ]
  }
  ```
- **功能**: 获取用户重要消息

### 统计消息数量
- **URL**: `GET /stats/count`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 查询 | Long | 否 | 用户ID |
  | type | 查询 | Integer | 否 | 消息类型 |
  | status | 查询 | Integer | 否 | 消息状态 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "userId": 1,
      "totalCount": 25,
      "unreadCount": 5,
      "readCount": 20,
      "deletedCount": 0,
      "typeCounts": {
        "system": 10,
        "user": 8,
        "job": 7
      },
      "priorityCounts": {
        "low": 5,
        "medium": 15,
        "high": 5
      }
    }
  }
  ```
- **功能**: 统计消息数量

### 获取消息统计
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
      "summary": {
        "totalMessages": 25,
        "unreadMessages": 5,
        "readMessages": 20,
        "importantMessages": 3,
        "todayMessages": 2,
        "weekMessages": 8,
        "monthMessages": 15
      },
      "typeDistribution": [
        {
          "type": "system",
          "count": 10,
          "percentage": 0.4
        },
        {
          "type": "user",
          "count": 8,
          "percentage": 0.32
        },
        {
          "type": "job",
          "count": 7,
          "percentage": 0.28
        }
      ],
      "priorityDistribution": [
        {
          "priority": "high",
          "count": 5,
          "percentage": 0.2
        },
        {
          "priority": "medium",
          "count": 15,
          "percentage": 0.6
        },
        {
          "priority": "low",
          "count": 5,
          "percentage": 0.2
        }
      ]
    }
  }
  ```
- **功能**: 获取消息统计摘要

### 获取发送状态
- **URL**: `GET /{id}/status`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 消息ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "id": 1,
      "title": "面试邀请通知",
      "status": "sent",
      "statusName": "已发送",
      "sentTime": "2025-11-08T12:00:00",
      "deliveredTime": "2025-11-08T12:01:00",
      "readTime": "2025-11-08T12:05:00",
      "deliveryRate": 1.0,
      "readRate": 0.8,
      "recipients": [
        {
          "userId": 1,
          "userName": "张三",
          "status": "read",
          "deliveredTime": "2025-11-08T12:01:00",
          "readTime": "2025-11-08T12:05:00"
        },
        {
          "userId": 2,
          "userName": "李四",
          "status": "delivered",
          "deliveredTime": "2025-11-08T12:01:00",
          "readTime": null
        }
      ]
    }
  }
  ```
- **功能**: 获取消息发送状态

### 重新发送消息
- **URL**: `POST /{id}/resend`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 消息ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "重新发送成功",
    "data": {
      "id": 1,
      "title": "面试邀请通知",
      "resendTime": "2025-11-08T22:00:00",
      "sentCount": 3,
      "failedCount": 0
    }
  }
  ```
- **功能**: 重新发送消息

### 取消发送消息
- **URL**: `POST /{id}/cancel`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 消息ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "取消成功",
    "data": {
      "id": 1,
      "title": "面试邀请通知",
      "status": "cancelled",
      "cancelTime": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 取消发送消息

### 获取消息模板
- **URL**: `GET /templates`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | type | 查询 | String | 否 | 模板类型：email/sms/wechat |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "name": "欢迎邮件模板",
        "type": "email",
        "subject": "欢迎加入就业平台",
        "content": "亲爱的{{name}}，欢迎您注册就业平台！",
        "variables": ["name"],
        "status": 1,
        "createTime": "2025-11-08T12:00:00"
      },
      {
        "id": 2,
        "name": "验证码短信模板",
        "type": "sms",
        "content": "您的验证码是{{code}}，有效期5分钟",
        "variables": ["code"],
        "status": 1,
        "createTime": "2025-11-08T12:00:00"
      }
    ]
  }
  ```
- **功能**: 获取消息模板

### 创建消息模板
- **URL**: `POST /templates`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | TemplateDTO | 请求体 | Object | 是 | 模板信息对象 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "创建成功",
    "data": {
      "id": 1,
      "name": "欢迎邮件模板",
      "type": "email",
      "subject": "欢迎加入就业平台",
      "content": "亲爱的{{name}}，欢迎您注册就业平台！",
      "variables": ["name"],
      "status": 1,
      "createTime": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 创建消息模板

### 更新消息模板
- **URL**: `PUT /templates/{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 模板ID |
  | TemplateDTO | 请求体 | Object | 是 | 模板信息对象 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新成功",
    "data": {
      "id": 1,
      "name": "欢迎邮件模板",
      "type": "email",
      "subject": "欢迎加入就业平台",
      "content": "亲爱的{{name}}，欢迎您注册就业平台！",
      "variables": ["name"],
      "status": 1,
      "updateTime": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 更新消息模板

### 删除消息模板
- **URL**: `DELETE /templates/{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 模板ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "删除成功",
    "data": {
      "id": 1,
      "name": "欢迎邮件模板"
    }
  }
  ```
- **功能**: 删除消息模板

### 获取推送配置
- **URL**: `GET /config`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "email": {
        "enabled": true,
        "host": "smtp.example.com",
        "port": 587,
        "username": "noreply@example.com",
        "ssl": true
      },
      "sms": {
        "enabled": true,
        "provider": "aliyun",
        "accessKey": "***",
        "secretKey": "***",
        "signature": "就业平台"
      },
      "wechat": {
        "enabled": true,
        "appId": "wx1234567890",
        "appSecret": "***",
        "templateIds": {
          "job_notification": "template123"
        }
      },
      "push": {
        "enabled": true,
        "ios": {
          "enabled": true,
          "certificate": "***",
          "password": "***"
        },
        "android": {
          "enabled": true,
          "apiKey": "***"
        }
      }
    }
  }
  ```
- **功能**: 获取推送配置

### 更新推送配置
- **URL**: `PUT /config`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | configData | 请求体 | Object | 是 | 配置数据 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新成功",
    "data": {
      "updatedSections": ["email", "sms"],
      "updateTime": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 更新推送配置

### 测试推送通道
- **URL**: `POST /test`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | channel | 请求体 | String | 是 | 推送通道：email/sms/wechat/push |
  | target | 请求体 | String | 是 | 测试目标 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "测试成功",
    "data": {
      "channel": "email",
      "target": "test@example.com",
      "status": "success",
      "responseTime": 1200,
      "testTime": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 测试推送通道

## 使用示例

### 发送站内消息示例
```bash
curl -X POST "http://localhost:8080/api/messages/internal" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "title": "面试邀请通知",
    "content": "您已收到面试邀请，请及时查看详情",
    "senderId": 1,
    "receiverIds": [1, 2, 3],
    "type": 3,
    "priority": 3,
    "expireTime": "2025-11-15T12:00:00"
  }'
```

### 发送邮件示例
```bash
curl -X POST "http://localhost:8080/api/messages/email" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "subject": "欢迎加入就业平台",
    "content": "感谢您注册就业平台，祝您找到心仪的工作！",
    "toEmails": ["user@example.com"],
    "templateId": "welcome_email",
    "templateData": {"name": "张三"}
  }'
```

### 获取用户消息列表示例
```bash
curl -X GET "http://localhost:8080/api/messages/user/1" \
  -H "Authorization: Bearer {token}"
```

### 标记消息为已读示例
```bash
curl -X POST "http://localhost:8080/api/messages/1/read" \
  -H "Authorization: Bearer {token}"
```

### 获取未读消息数量示例
```bash
curl -X GET "http://localhost:8080/api/messages/user/1/unread-count" \
  -H "Authorization: Bearer {token}"
```

## 注意事项

- 所有消息推送接口都需要有效的JWT Token进行认证
- 消息类型：1-系统消息，2-用户消息，3-岗位消息
- 消息状态：0-未读，1-已读，2-已删除
- 优先级：1-低，2-中，3-高
- 支持多种推送通道：站内消息、邮件、短信、微信、推送通知
- 消息支持模板化，提高发送效率
- 支持批量操作，提高处理效率
- 消息支持过期时间，自动清理过期消息
- 推送配置支持动态调整，适应不同环境

[返回主文档](../docs/README.md)
