# 消息管理模块 (MessageController)

**基础路径**: `/api/messages`

## 接口列表

### 发送消息
- **URL**: `POST /`
- **请求参数**:
  ```json
  {
    "title": "消息标题",           // 字符串，必填，消息标题
    "content": "消息内容",         // 字符串，必填，消息内容
    "senderId": 1,                // 长整型，必填，发送者ID
    "receiverIds": [2, 3],        // 长整型数组，必填，接收者ID列表
    "type": 1,                    // 整数，必填，消息类型：1-系统消息，2-用户消息，3-岗位消息
    "priority": 1,                // 整数，可选，默认1，优先级：1-低，2-中，3-高
    "expireTime": "2025-12-31T23:59:59",  // 字符串，可选，过期时间
    "businessId": 1001,           // 长整型，可选，关联业务ID
    "businessType": "job_apply",  // 字符串，可选，业务类型
    "templateCode": "user_welcome_email",  // 字符串，可选，模板编码，用于自动选择模板
    "channelType": "email",       // 字符串，必填，渠道类型：email-邮件，sms-短信，wechat-微信，push-推送
    "attachments": "[{\"name\":\"简历.pdf\",\"url\":\"/files/123.pdf\"}]"  // 字符串，可选，附件信息JSON
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,                  // 整数，状态码
    "message": "消息发送成功",     // 字符串，响应消息
    "data": null,
    "timestamp": "2025-11-22T16:51:00"  // 字符串，响应时间戳
  }
  ```
- **功能**: 发送消息给一个或多个接收者，支持多种渠道类型和模板自动选择。消息将通过异步方式发送，支持邮件、短信、微信、推送等多种渠道。

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
      "id": 1,                    // 长整型，消息ID
      "title": "消息标题",         // 字符串，消息标题
      "content": "消息内容",       // 字符串，消息内容
      "senderId": 1,              // 长整型，发送者ID
      "senderName": "发送者姓名",  // 字符串，发送者姓名
      "receiverId": 2,            // 长整型，接收者ID
      "receiverName": "接收者姓名",// 字符串，接收者姓名
      "type": 1,                  // 整数，消息类型：1-系统消息，2-用户消息，3-岗位消息
      "typeName": "系统消息",      // 字符串，消息类型名称
      "priority": 1,              // 整数，优先级：1-低，2-中，3-高
      "priorityName": "低",       // 字符串，优先级名称
      "status": 0,                // 整数，消息状态：0-未读，1-已读，2-删除
      "statusName": "未读",       // 字符串，状态名称
      "createTime": "2025-11-22T16:30:00",  // 字符串，创建时间
      "readTime": null,           // 字符串，阅读时间
      "expireTime": "2025-12-31T23:59:59",  // 字符串，过期时间
      "businessId": 1001,         // 长整型，关联业务ID
      "businessType": "job_apply",// 字符串，业务类型
      "attachments": "[{\"name\":\"简历.pdf\",\"url\":\"/files/123.pdf\"}]"  // 字符串，附件信息JSON
    },
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 获取指定消息的详细信息

### 分页查询消息
- **URL**: `GET /`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 查询 | Long | 是 | 用户ID |
  | type | 查询 | Integer | 否 | 消息类型：1-系统消息，2-用户消息，3-岗位消息 |
  | status | 查询 | Integer | 否 | 消息状态：0-未读，1-已读，2-删除 |
  | keyword | 查询 | String | 否 | 搜索关键词（标题/内容） |
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
          "title": "消息标题",
          "content": "消息内容",
          "senderId": 1,
          "senderName": "发送者姓名",
          "receiverId": 2,
          "receiverName": "接收者姓名",
          "type": 1,
          "typeName": "系统消息",
          "priority": 1,
          "priorityName": "低",
          "status": 0,
          "statusName": "未读",
          "createTime": "2025-11-22T16:30:00",
          "readTime": null,
          "expireTime": "2025-12-31T23:59:59",
          "businessId": 1001,
          "businessType": "job_apply",
          "attachments": "[{\"name\":\"简历.pdf\",\"url\":\"/files/123.pdf\"}]"
        }
      ],
      "totalElements": 100,
      "totalPages": 5,
      "size": 20,
      "number": 0
    },
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 分页查询消息列表，支持多条件筛选

### 获取用户消息列表
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
        "title": "消息标题",
        "content": "消息内容",
        "senderId": 1,
        "senderName": "发送者姓名",
        "receiverId": 2,
        "receiverName": "接收者姓名",
        "type": 1,
        "typeName": "系统消息",
        "priority": 1,
        "priorityName": "低",
        "status": 0,
        "statusName": "未读",
        "createTime": "2025-11-22T16:30:00",
        "readTime": null,
        "expireTime": "2025-12-31T23:59:59",
        "businessId": 1001,
        "businessType": "job_apply",
        "attachments": "[{\"name\":\"简历.pdf\",\"url\":\"/files/123.pdf\"}]"
      }
    ],
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 获取指定用户的所有消息列表

### 获取用户最新消息
- **URL**: `GET /user/{userId}/latest`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
  | limit | 查询 | Integer | 否 | 返回数量，默认10 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "title": "消息标题",
        "content": "消息内容",
        "senderId": 1,
        "senderName": "发送者姓名",
        "receiverId": 2,
        "receiverName": "接收者姓名",
        "type": 1,
        "typeName": "系统消息",
        "priority": 1,
        "priorityName": "低",
        "status": 0,
        "statusName": "未读",
        "createTime": "2025-11-22T16:30:00",
        "readTime": null,
        "expireTime": "2025-12-31T23:59:59",
        "businessId": 1001,
        "businessType": "job_apply",
        "attachments": "[{\"name\":\"简历.pdf\",\"url\":\"/files/123.pdf\"}]"
      }
    ],
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 获取用户最新的消息列表

### 获取用户重要消息
- **URL**: `GET /user/{userId}/important`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
  | limit | 查询 | Integer | 否 | 返回数量，默认10 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "title": "重要消息标题",
        "content": "重要消息内容",
        "senderId": 1,
        "senderName": "发送者姓名",
        "receiverId": 2,
        "receiverName": "接收者姓名",
        "type": 1,
        "typeName": "系统消息",
        "priority": 3,
        "priorityName": "高",
        "status": 0,
        "statusName": "未读",
        "createTime": "2025-11-22T16:30:00",
        "readTime": null,
        "expireTime": "2025-12-31T23:59:59",
        "businessId": 1001,
        "businessType": "job_apply",
        "attachments": "[{\"name\":\"简历.pdf\",\"url\":\"/files/123.pdf\"}]"
      }
    ],
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 获取用户的重要消息列表（高优先级消息）

### 标记消息为已读
- **URL**: `PUT /{id}/read`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 消息ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "消息已标记为已读",
    "data": null,
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 标记单个消息为已读状态

### 批量标记为已读
- **URL**: `PUT /batch-read`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | ids | 请求体 | List<Long> | 是 | 消息ID列表 |
  
  **请求体示例**:
  ```json
  [1, 2, 3]
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "消息已批量标记为已读",
    "data": null,
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 批量标记多个消息为已读状态

### 标记用户所有消息为已读
- **URL**: `PUT /user/{userId}/read-all`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "所有消息已标记为已读",
    "data": null,
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 标记指定用户的所有消息为已读状态

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
    "message": "消息删除成功",
    "data": null,
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 删除单个消息

### 批量删除消息
- **URL**: `DELETE /batch`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | ids | 请求体 | List<Long> | 是 | 消息ID列表 |
  
  **请求体示例**:
  ```json
  [1, 2, 3]
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "消息批量删除成功",
    "data": null,
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 批量删除多个消息

### 清空用户消息
- **URL**: `DELETE /user/{userId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "用户消息清空成功",
    "data": null,
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 清空指定用户的所有消息

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
    "data": 5,                    // 整数，未读消息数量
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 获取指定用户的未读消息数量

### 获取消息统计
- **URL**: `GET /user/{userId}/stats`
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
      "total": 10,                // 整数，总消息数
      "unread": 3,                // 整数，未读消息数
      "read": 7                   // 整数，已读消息数
    },
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 获取指定用户的消息统计信息

### 发送邮件
- **URL**: `POST /email`
- **请求参数**:
  ```json
  {
    "templateCode": "user_welcome",      // 字符串，必填，模板编码
    "toEmails": ["user@example.com"],    // 字符串数组，必填，收件人邮箱列表
    "subject": "欢迎加入就业平台",        // 字符串，可选，邮件主题（如使用模板则忽略）
    "content": "邮件内容",               // 字符串，可选，邮件内容（如使用模板则忽略）
    "variables": {                       // 对象，可选，模板变量
      "username": "张三",
      "company": "测试企业"
    },
    "attachments": [                     // 数组，可选，附件列表
      {
        "name": "简历.pdf",
        "url": "/files/123.pdf"
      }
    ]
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "邮件发送成功",
    "data": {
      "successCount": 1,                // 整数，成功发送数量
      "failedCount": 0,                 // 整数，发送失败数量
      "failedEmails": [],               // 字符串数组，发送失败的邮箱列表
      "taskId": "email_task_123456"     // 字符串，邮件发送任务ID
    },
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 发送邮件给一个或多个收件人，支持模板和变量替换

### 获取邮件模板列表
- **URL**: `GET /email/templates`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
  | type | 查询 | String | 否 | 模板类型：email-邮件 |
  | status | 查询 | Integer | 否 | 模板状态：0-禁用，1-启用 |
  | keyword | 查询 | String | 否 | 搜索关键词（模板名称/描述） |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "content": [
        {
          "id": 1,
          "name": "用户欢迎邮件模板",
          "type": "email",
          "subject": "欢迎加入就业平台",
          "content": "尊敬的{{username}}，欢迎您加入就业平台！",
          "variables": "[\"username\", \"company\"]",
          "status": 1,
          "description": "新用户注册时发送的欢迎邮件",
          "createTime": "2025-11-22T16:30:00",
          "updateTime": "2025-11-22T16:30:00"
        }
      ],
      "totalElements": 10,
      "totalPages": 1,
      "size": 20,
      "number": 0
    },
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 分页查询邮件模板列表

### 获取邮件模板详情
- **URL**: `GET /email/templates/{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 模板ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "id": 1,
      "name": "用户欢迎邮件模板",
      "type": "email",
      "subject": "欢迎加入就业平台",
      "content": "尊敬的{{username}}，欢迎您加入就业平台！",
      "variables": "[\"username\", \"company\"]",
      "status": 1,
      "description": "新用户注册时发送的欢迎邮件",
      "createTime": "2025-11-22T16:30:00",
      "updateTime": "2025-11-22T16:30:00"
    },
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 获取指定邮件模板的详细信息

### 创建邮件模板
- **URL**: `POST /email/templates`
- **请求参数**:
  ```json
  {
    "name": "用户欢迎邮件模板",          // 字符串，必填，模板名称
    "type": "email",                   // 字符串，必填，模板类型：email-邮件
    "subject": "欢迎加入就业平台",       // 字符串，必填，邮件主题
    "content": "尊敬的{{username}}，欢迎您加入就业平台！",  // 字符串，必填，模板内容
    "variables": "[\"username\", \"company\"]",  // 字符串，可选，模板变量JSON数组
    "status": 1,                       // 整数，可选，模板状态：0-禁用，1-启用，默认1
    "description": "新用户注册时发送的欢迎邮件"  // 字符串，可选，模板描述
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "模板创建成功",
    "data": {
      "id": 1,
      "name": "用户欢迎邮件模板",
      "type": "email",
      "subject": "欢迎加入就业平台",
      "content": "尊敬的{{username}}，欢迎您加入就业平台！",
      "variables": "[\"username\", \"company\"]",
      "status": 1,
      "description": "新用户注册时发送的欢迎邮件",
      "createTime": "2025-11-22T16:30:00",
      "updateTime": "2025-11-22T16:30:00"
    },
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 创建新的邮件模板

### 更新邮件模板
- **URL**: `PUT /email/templates/{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 模板ID |
  | MessageTemplateDTO | 请求体 | Object | 是 | 模板信息对象 |
  
  **MessageTemplateDTO对象结构**:
  ```json
  {
    "name": "用户欢迎邮件模板",          // 字符串，可选，模板名称
    "subject": "欢迎加入就业平台",       // 字符串，可选，邮件主题
    "content": "尊敬的{{username}}，欢迎您加入就业平台！",  // 字符串，可选，模板内容
    "variables": "[\"username\", \"company\"]",  // 字符串，可选，模板变量JSON数组
    "status": 1,                       // 整数，可选，模板状态：0-禁用，1-启用
    "description": "新用户注册时发送的欢迎邮件"  // 字符串，可选，模板描述
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "模板更新成功",
    "data": {
      "id": 1,
      "name": "用户欢迎邮件模板",
      "type": "email",
      "subject": "欢迎加入就业平台",
      "content": "尊敬的{{username}}，欢迎您加入就业平台！",
      "variables": "[\"username\", \"company\"]",
      "status": 1,
      "description": "新用户注册时发送的欢迎邮件",
      "createTime": "2025-11-22T16:30:00",
      "updateTime": "2025-11-22T16:30:00"
    },
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 更新邮件模板信息

### 删除邮件模板
- **URL**: `DELETE /email/templates/{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 模板ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "模板删除成功",
    "data": null,
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 删除邮件模板

### 获取邮件发送状态
- **URL**: `GET /email/status/{taskId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | taskId | 路径 | String | 是 | 邮件发送任务ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "taskId": "email_task_123456",    // 字符串，邮件发送任务ID
      "status": "completed",            // 字符串，发送状态：pending-待发送，sending-发送中，completed-已完成，failed-失败
      "successCount": 1,                // 整数，成功发送数量
      "failedCount": 0,                 // 整数，发送失败数量
      "failedEmails": [],               // 字符串数组，发送失败的邮箱列表
      "startTime": "2025-11-22T16:30:00",  // 字符串，开始时间
      "endTime": "2025-11-22T16:31:00",    // 字符串，结束时间
      "errorMessage": null              // 字符串，错误信息
    },
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 获取邮件发送任务的状态信息


## 使用示例

### 发送消息示例
```bash
curl -X POST "http://localhost:8080/api/messages" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "title": "系统维护通知",
    "content": "系统将于今晚进行维护，请提前保存数据",
    "senderId": 1,
    "receiverIds": [2, 3, 4],
    "type": 1,
    "priority": 2,
    "expireTime": "2025-12-31T23:59:59"
  }'
```

### 查询消息示例
```bash
curl -X GET "http://localhost:8080/api/messages?userId=1&type=1&status=0&page=0&size=20" \
  -H "Authorization: Bearer {token}"
```

### 标记消息为已读示例
```bash
curl -X PUT "http://localhost:8080/api/messages/1/read" \
  -H "Authorization: Bearer {token}"
```

### 发送邮件示例
```bash
curl -X POST "http://localhost:8080/api/messages/email" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "templateCode": "user_welcome",
    "toEmails": ["user@example.com", "admin@example.com"],
    "variables": {
      "username": "张三",
      "company": "测试企业有限公司"
    },
    "attachments": [
      {
        "name": "用户指南.pdf",
        "url": "/files/user_guide.pdf"
      }
    ]
  }'
```

### 创建邮件模板示例
```bash
curl -X POST "http://localhost:8080/api/messages/email/templates" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "name": "面试邀请邮件模板",
    "type": "email",
    "subject": "面试邀请通知",
    "content": "尊敬的{{username}}，您好！{{company}}邀请您参加{{position}}岗位的面试。面试时间：{{interviewTime}}，面试地点：{{interviewLocation}}。请准时参加！",
    "variables": "[\"username\", \"company\", \"position\", \"interviewTime\", \"interviewLocation\"]",
    "status": 1,
    "description": "企业向求职者发送面试邀请的邮件模板"
  }'
```

### 查询邮件发送状态示例
```bash
curl -X GET "http://localhost:8080/api/messages/email/status/email_task_123456" \
  -H "Authorization: Bearer {token}"
```

## 数据库表结构

### message表
存储用户消息记录

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(200) | 消息标题 |
| content | TEXT | 消息内容 |
| sender_id | BIGINT | 发送者ID |
| receiver_id | BIGINT | 接收者ID |
| type | TINYINT | 消息类型：1-系统消息，2-用户消息，3-岗位消息 |
| priority | TINYINT | 优先级：1-低，2-中，3-高 |
| status | TINYINT | 消息状态：0-未读，1-已读，2-删除 |
| read_time | DATETIME | 阅读时间 |
| expire_time | DATETIME | 过期时间 |
| business_id | BIGINT | 关联业务ID |
| business_type | VARCHAR(50) | 业务类型 |
| attachments | TEXT | 附件信息JSON |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

### message_template表
存储消息模板

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(100) | 模板名称 |
| type | VARCHAR(20) | 模板类型 |
| subject | VARCHAR(200) | 邮件主题 |
| content | TEXT | 模板内容 |
| variables | TEXT | 模板变量 |
| status | TINYINT | 模板状态 |
| description | VARCHAR(500) | 模板描述 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

### message_channel表
存储消息通道配置

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(100) | 通道名称 |
| type | VARCHAR(20) | 通道类型 |
| config | TEXT | 通道配置 |
| status | TINYINT | 通道状态 |
| description | VARCHAR(500) | 通道描述 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

## 模板变量用途说明

### 模板变量概述

模板变量是消息模板系统中的动态内容占位符，用于在发送消息时动态替换模板中的特定内容。通过使用模板变量，可以实现消息内容的个性化定制，提高消息的灵活性和复用性。

### 模板变量存储格式

在 `message_template` 表中，`variables` 字段以 JSON 数组格式存储模板变量定义：

```json
["username", "company", "position", "interviewTime", "interviewLocation"]
```

### 模板变量使用方式

#### 1. 模板定义
在模板内容中使用 `{{变量名}}` 格式定义占位符：

```html
尊敬的{{username}}，您好！
{{company}}邀请您参加{{position}}岗位的面试。
面试时间：{{interviewTime}}
面试地点：{{interviewLocation}}
请准时参加！
```

#### 2. 发送时传入变量值
在发送邮件时，通过 `variables` 参数传入具体的变量值：

```json
{
  "templateCode": "interview_invitation",
  "toEmails": ["user@example.com"],
  "variables": {
    "username": "张三",
    "company": "测试企业有限公司",
    "position": "Java开发工程师",
    "interviewTime": "2025-11-25 14:00",
    "interviewLocation": "北京市朝阳区xxx大厦10层"
  }
}
```

#### 3. 变量替换结果
系统会自动将模板中的占位符替换为实际值：

```
尊敬的张三，您好！
测试企业有限公司邀请您参加Java开发工程师岗位的面试。
面试时间：2025-11-25 14:00
面试地点：北京市朝阳区xxx大厦10层
请准时参加！
```

### 常见模板变量示例

#### 用户欢迎邮件模板
```json
{
  "templateCode": "user_welcome",
  "variables": {
    "username": "李四",
    "company": "就业平台",
    "registerTime": "2025-11-22"
  }
}
```

#### 密码重置邮件模板
```json
{
  "templateCode": "password_reset",
  "variables": {
    "username": "王五",
    "resetLink": "https://example.com/reset-password?token=abc123",
    "expireTime": "30分钟"
  }
}
```

#### 面试结果通知模板
```json
{
  "templateCode": "interview_result",
  "variables": {
    "username": "赵六",
    "company": "科技公司",
    "position": "前端工程师",
    "result": "通过",
    "nextStep": "请等待HR联系您安排入职事宜"
  }
}
```

### 模板变量优势

1. **内容个性化**：根据不同用户动态生成个性化消息内容
2. **模板复用**：同一模板可适用于不同场景和用户
3. **维护简便**：修改模板内容无需重新部署代码
4. **业务灵活**：支持各种业务场景的动态内容需求

## 枚举定义

### 消息类型 (Message.Type)
- **SYSTEM** (1): 系统消息
- **USER** (2): 用户消息  
- **JOB** (3): 岗位消息

### 优先级 (Message.Priority)
- **LOW** (1): 低
- **MEDIUM** (2): 中
- **HIGH** (3): 高

### 消息状态 (Message.Status)
- **UNREAD** (0): 未读
- **READ** (1): 已读
- **DELETED** (2): 删除

## 验证码控制器 (VerificationCodeController)

**基础路径**: `/api/verification`

**说明**: 该控制器专门用于注册、登录等无需认证的场景，所有接口都无需JWT Token认证。

### 接口列表

#### 发送验证码
- **URL**: `POST /register`
- **请求参数**:
  ```json
  {
    "email": "user@example.com",        // 字符串，必填，接收者邮箱
    "codeType": "reset_password",             // 字符串，可选，验证码类型，默认"reset_password"
    "businessScene": "重置密码",         // 字符串，可选，业务场景描述，默认"用户注册"
    "expireMinutes": 10,                // 整数，可选，过期时间（分钟），默认10
    "codeLength": 6                     // 整数，可选，验证码长度，默认6
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "验证码发送成功",
    "data": {
      "success": true,                  // 布尔值，是否发送成功
      "message": "邮件验证码发送成功",   // 字符串，发送结果消息
      "verificationCode": "123456",     // 字符串，验证码（仅测试环境返回）
      "taskId": "email_task_123456",    // 字符串，邮件发送任务ID
      "expireMinutes": 10,              // 整数，过期时间（分钟）
      "sendTime": "2025-11-22T16:51:00" // 字符串，发送时间
    },
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 发送注册场景的邮件验证码，无需鉴权


#### 验证注册验证码
- **URL**: `POST /register/verify`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | email | 查询 | String | 是 | 邮箱地址 |
  | code | 查询 | String | 是 | 验证码 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "验证码验证成功",
    "data": null,
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
  **验证失败响应**:
  ```json
  {
    "code": 400,
    "message": "验证码验证失败",
    "data": null,
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 验证注册场景的验证码，无需鉴权

#### 检查验证码是否有效
- **URL**: `GET /valid`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | email | 查询 | String | 是 | 邮箱地址 |
  | codeType | 查询 | String | 是 | 验证码类型 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": true,                       // 布尔值，验证码是否有效
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 检查指定邮箱和验证码类型的验证码是否有效（存在且未过期），无需鉴权

#### 获取验证码剩余时间
- **URL**: `GET /remaining-time`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | email | 查询 | String | 是 | 邮箱地址 |
  | codeType | 查询 | String | 是 | 验证码类型 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": 1200,                       // 整数，剩余时间（秒）
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 获取验证码的剩余有效时间（秒），无需鉴权

#### 清除验证码
- **URL**: `DELETE /`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | email | 查询 | String | 是 | 邮箱地址 |
  | codeType | 查询 | String | 是 | 验证码类型 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "验证码已清除",
    "data": null,
    "timestamp": "2025-11-22T16:51:00"
  }
  ```
- **功能**: 清除指定邮箱和验证码类型的验证码，无需鉴权

`

#### 检查验证码有效性示例
```bash
curl -X GET "http://localhost:8080/api/verification/valid?email=user@example.com&codeType=register"
```

#### 获取验证码剩余时间示例
```bash
curl -X GET "http://localhost:8080/api/verification/remaining-time?email=user@example.com&codeType=register"
```

#### 清除验证码示例
```bash
curl -X DELETE "http://localhost:8080/api/verification?email=user@example.com&codeType=register"
```

## 注意事项

- 所有消息接口都需要有效的JWT Token进行认证
- 用户只能操作自己的消息，不能操作其他用户的消息
- 消息发送支持批量发送给多个接收者
- 消息状态变更支持单个和批量操作
- 消息支持关联业务ID和业务类型，便于业务集成
- 附件信息以JSON格式存储，包含文件名和URL信息
- 邮件验证码发送有频率限制，每日最多发送10次
- 邮件验证码在测试环境会返回验证码，生产环境不返回
- 验证码验证成功后会自动删除，确保一次性使用
