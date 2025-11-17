# 用户管理模块 (UserController)

**基础路径**: `/api/users`

## 接口列表

### 用户注册
- **URL**: `POST /register`
- **请求参数**:
  ```json
  {
    "username": "用户名",        // 字符串，必填，长度2-50字符
    "phone": "手机号",          // 字符串，必填，11位手机号格式
    "email": "邮箱",            // 字符串，必填，邮箱格式
    "password": "密码",         // 字符串，必填，长度6-255字符
    "userType": 1,             // 整数，可选，默认1，用户类型：1-求职者，2-企业用户，3-管理员
    "companyId": 1             // 长整型，可选，所属企业ID，仅企业用户使用
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,                // 整数，状态码
    "message": "注册成功",       // 字符串，响应消息
    "data": {
      "id": 1,                  // 长整型，用户ID
      "username": "用户名",      // 字符串，用户名
      "phone": "手机号",        // 字符串，手机号
      "email": "邮箱",          // 字符串，邮箱
      "userType": 1,            // 整数，用户类型：1-求职者，2-企业用户，3-管理员
      "status": 1,              // 整数，用户状态：0-禁用，1-正常，2-黑名单
      "authStatus": 0,          // 整数，认证状态：0-未认证，1-审核中，2-已认证
      "createTime": "2025-11-10T22:00:00",  // 字符串，创建时间
      "updateTime": "2025-11-10T22:00:00"   // 字符串，更新时间
    },
    "timestamp": "2025-11-10T22:00:00"      // 字符串，响应时间戳
  }
  ```
- **功能**: 用户注册

### 用户登录
- **URL**: `POST /login`
- **请求参数**:
  ```json
  {
    "account": "admin",
    "password": "qwe123!@#"
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,                    // 整数，状态码
    "message": "登录成功",           // 字符串，响应消息
    "data": {
      "token": "JWT令牌",           // 字符串，JWT认证令牌
      "user": {
        "id": 1,                    // 长整型，用户ID
        "username": "用户名",        // 字符串，用户名
        "phone": "手机号",          // 字符串，手机号
        "email": "邮箱",            // 字符串，邮箱
        "userType": 1,              // 整数，用户类型：1-求职者，2-企业用户，3-管理员
        "status": 1,                // 整数，用户状态：0-禁用，1-正常，2-黑名单
        "authStatus": 0             // 整数，认证状态：0-未认证，1-审核中，2-已认证
      }
    },
    "timestamp": "2025-11-10T22:00:00"  // 字符串，响应时间戳
  }
  ```
- **功能**: 用户登录

### 微信登录
- **URL**: `POST /login/wx`
- **请求参数**:
  ```json
  {
    "openid": "微信openid",        // 字符串，必填，微信openid
    "unionid": "微信unionid"       // 字符串，可选，微信unionid
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "微信登录成功",
    "data": {
      "id": 1,
      "username": "用户名",
      "phone": "手机号",
      "email": "邮箱",
      "userType": 1,
      "status": 1,
      "authStatus": 0,
      "createTime": "2025-11-10T22:00:00",
      "updateTime": "2025-11-10T22:00:00"
    },
    "timestamp": "2025-11-10T22:00:00"
  }
  ```
- **功能**: 微信登录

### 手机号登录
- **URL**: `POST /login/phone`
- **请求参数**:
  ```json
  {
    "phone": "手机号",              // 字符串，必填，手机号
    "verificationCode": "验证码"    // 字符串，必填，验证码
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "手机登录成功",
    "data": {
      "id": 1,
      "username": "用户名",
      "phone": "手机号",
      "email": "邮箱",
      "userType": 1,
      "status": 1,
      "authStatus": 0,
      "createTime": "2025-11-10T22:00:00",
      "updateTime": "2025-11-10T22:00:00"
    },
    "timestamp": "2025-11-10T22:00:00"
  }
  ```
- **功能**: 手机号登录

### 获取用户信息
- **URL**: `GET /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取用户信息成功",
    "data": {
      "id": 1,
      "username": "用户名",
      "phone": "手机号",
      "email": "邮箱",
      "userType": 1,
      "status": 1,
      "authStatus": 0,
      "companyId": 2,
      "companyName": "测试企业有限公司",
      "lastLoginTime": "2025-11-10 22:00:00",
      "createTime": "2025-11-10T22:00:00",
      "updateTime": "2025-11-10T22:00:00"
    },
    "timestamp": "2025-11-10T22:00:00"
  }
  ```
- **功能**: 获取指定用户信息，包含企业ID和企业名称

### 更新用户信息
- **URL**: `PUT /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 用户ID |
  | UserDTO | 请求体 | Object | 是 | 用户信息对象 |
  
  **UserDTO对象结构**:
  ```json
  {
    "phone": "手机号",          // 字符串，可选，11位手机号格式
    "email": "邮箱",            // 字符串，可选，邮箱格式
    "wxOpenid": "微信openid",   // 字符串，可选，微信openid
    "wxUnionid": "微信unionid", // 字符串，可选，微信unionid
    "userType": 1,              // 整数，可选，用户类型：1-求职者，2-企业用户，3-管理员
    "status": 1,                // 整数，可选，用户状态：0-禁用，1-正常，2-黑名单
    "authStatus": 0,            // 整数，可选，认证状态：0-未认证，1-审核中，2-已认证
    "companyId": 1              // 长整型，可选，所属企业ID，仅企业用户使用
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新用户信息成功",
    "data": {
      "id": 1,
      "username": "用户名",
      "phone": "手机号",
      "email": "邮箱",
      "userType": 1,
      "status": 1,
      "authStatus": 0,
      "lastLoginTime": "2025-11-10 22:00:00",
      "createTime": "2025-11-10T22:00:00",
      "updateTime": "2025-11-10T22:00:00"
    },
    "timestamp": "2025-11-10T22:00:00"
  }
  ```
- **功能**: 更新用户信息

### 获取用户资料
- **URL**: `GET /{id}/profile`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取用户资料成功",
    "data": {
      "id": 1,
      "userId": 1,
      "username": "用户名",            // 字符串，用户账号
      "phone": "手机号",              // 字符串，手机号
      "email": "邮箱",                // 字符串，邮箱
      "userType": 1,                  // 整数，用户类型：1-求职者，2-企业用户，3-管理员
      "status": 1,                    // 整数，用户状态：0-禁用，1-正常，2-黑名单
      "authStatus": 0,                // 整数，认证状态：0-未认证，1-审核中，2-已认证
      "realName": "真实姓名",
      "gender": 1,                    // 整数，性别：0-未知，1-男，2-女
      "birthday": "1990-01-01",       // 字符串，生日
      "avatar": "头像URL",
      "education": "本科",             // 字符串，学历
      "workYears": 3,                 // 整数，工作年限
      "currentSalary": 15000.00,      // 数字，当前薪资
      "expectedSalary": 20000.00,     // 数字，期望薪资
      "city": "北京",                  // 字符串，所在城市
      "skills": "Java,Spring,MySQL",  // 字符串，技能标签（JSON格式）
      "selfIntro": "个人简介",         // 字符串，个人介绍
      "createTime": "2025-11-10T22:00:00",
      "updateTime": "2025-11-10T22:00:00"
    },
    "timestamp": "2025-11-10T22:00:00"
  }
  ```
- **功能**: 获取用户资料，包含用户账号信息和详细资料信息

### 更新用户资料
- **URL**: `PUT /{id}/profile`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 用户ID |
  | UserProfileDTO | 请求体 | Object | 是 | 用户资料对象 |
  
  **UserProfileDTO对象结构**:
  ```json
  {
    "realName": "真实姓名",        // 字符串，可选，真实姓名
    "gender": 1,                  // 整数，可选，性别：0-未知，1-男，2-女
    "birthday": "1990-01-01",     // 字符串，可选，生日
    "avatar": "头像URL",          // 字符串，可选，头像URL
    "education": "本科",           // 字符串，可选，学历
    "workYears": 3,               // 整数，可选，工作年限
    "currentSalary": 15000.00,    // 数字，可选，当前薪资
    "expectedSalary": 20000.00,   // 数字，可选，期望薪资
    "city": "北京",                // 字符串，可选，所在城市
    "skills": "Java,Spring,MySQL",// 字符串，可选，技能标签（JSON格式）
    "selfIntro": "个人简介"        // 字符串，可选，个人介绍
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新用户资料成功",
    "data": {
      "id": 1,
      "userId": 1,
      "realName": "真实姓名",
      "gender": 1,
      "birthday": "1990-01-01",
      "avatar": "头像URL",
      "education": "本科",
      "workYears": 3,
      "currentSalary": 15000.00,
      "expectedSalary": 20000.00,
      "city": "北京",
      "skills": "Java,Spring,MySQL",
      "selfIntro": "个人简介",
      "createTime": "2025-11-10T22:00:00",
      "updateTime": "2025-11-10T22:00:00"
    },
    "timestamp": "2025-11-10T22:00:00"
  }
  ```
- **功能**: 更新用户资料

### 分页查询用户资料列表
- **URL**: `GET /profiles`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
  | city | 查询 | String | 否 | 所在城市 |
  | education | 查询 | String | 否 | 学历 |
  | minWorkYears | 查询 | Integer | 否 | 最小工作年限 |
  | maxWorkYears | 查询 | Integer | 否 | 最大工作年限 |
  | minExpectedSalary | 查询 | Double | 否 | 最小期望薪资 |
  | maxExpectedSalary | 查询 | Double | 否 | 最大期望薪资 |
  | skill | 查询 | String | 否 | 技能关键词 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询用户资料列表成功",
    "data": {
      "content": [
        {
          "id": 1,
          "userId": 1,
          "realName": "真实姓名",
          "gender": 1,
          "birthday": "1990-01-01",
          "avatar": "头像URL",
          "education": "本科",
          "workYears": 3,
          "currentSalary": 15000.00,
          "expectedSalary": 20000.00,
          "city": "北京",
          "skills": "Java,Spring,MySQL",
          "selfIntro": "个人简介",
          "createTime": "2025-11-10T22:00:00",
          "updateTime": "2025-11-10T22:00:00"
        }
      ],
      "totalElements": 100,
      "totalPages": 5,
      "size": 20,
      "number": 0,
      "filters": {
        "city": "北京",
        "education": "本科",
        "minWorkYears": 1,
        "maxWorkYears": 5,
        "minExpectedSalary": 10000.00,
        "maxExpectedSalary": 30000.00,
        "skill": "Java"
      }
    },
    "timestamp": "2025-11-10T22:00:00"
  }
  ```
- **功能**: 分页查询用户资料列表，支持多条件筛选，**只返回用户类型为求职者的用户资料**

### 实名认证
- **URL**: `POST /{id}/realname-auth`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 用户ID |
  | realName | 请求体 | String | 是 | 真实姓名 |
  | idCard | 请求体 | String | 是 | 身份证号 |
  | idCardFront | 请求体 | String | 是 | 身份证正面照片URL |
  | idCardBack | 请求体 | String | 是 | 身份证背面照片URL |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "实名认证成功",
    "data": {
      "id": 1,
      "userId": 1,
      "realName": "真实姓名",
      "idCard": "身份证号",
      "authStatus": 1,
      "authTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 实名认证

### 修改密码
- **URL**: `POST /{id}/change-password`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 用户ID |
  | oldPassword | 请求体 | String | 是 | 旧密码 |
  | newPassword | 请求体 | String | 是 | 新密码 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "密码修改成功",
    "data": null
  }
  ```
- **功能**: 修改密码

### 重置密码
- **URL**: `POST /reset-password`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | phone | 请求体 | String | 是 | 手机号 |
  | verificationCode | 请求体 | String | 是 | 验证码 |
  | newPassword | 请求体 | String | 是 | 新密码 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "密码重置成功",
    "data": null
  }
  ```
- **功能**: 重置密码

### 管理员重置用户密码
- **URL**: `POST /{id}/admin-reset-password`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 用户ID |
  | newPassword | 请求体 | String | 是 | 新密码 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "管理员重置密码成功",
    "data": null
  }
  ```
- **功能**: 管理员重置用户密码（无需旧密码验证）

### 发送验证码
- **URL**: `POST /send-verification-code`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | phone | 请求体 | String | 是 | 手机号 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "验证码发送成功",
    "data": {
      "phone": "13800138000",
      "expireTime": "2025-11-08T12:05:00"
    }
  }
  ```
- **功能**: 发送验证码

### 分页查询用户列表
- **URL**: `GET /`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
  | userType | 查询 | Integer | 否 | 用户类型：1-求职者，2-企业用户，3-管理员 |
  | status | 查询 | Integer | 否 | 用户状态：0-禁用，1-正常，2-黑名单 |
  | authStatus | 查询 | Integer | 否 | 认证状态：0-未认证，1-审核中，2-已认证 |
  | keyword | 查询 | String | 否 | 搜索关键词（用户名/手机号/邮箱） |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": {
      "content": [
        {
          "id": 1,
          "username": "用户名",
          "phone": "手机号",
          "email": "邮箱",
          "userType": 1,
          "status": 1,
          "authStatus": 0,
          "companyId": 2,
          "companyName": "测试企业有限公司",
          "lastLoginTime": "2025-11-10 22:00:00",
          "createTime": "2025-11-10T22:00:00",
          "updateTime": "2025-11-10T22:00:00"
        }
      ],
      "totalElements": 100,
      "totalPages": 5,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 分页查询用户列表，返回用户信息包含企业ID和企业名称，不返回整个企业对象

### 搜索用户
- **URL**: `GET /search`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | keyword | 查询 | String | 是 | 搜索关键词（用户名/手机号/邮箱/真实姓名） |
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
  | userType | 查询 | Integer | 否 | 用户类型：1-求职者，2-企业用户，3-管理员 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "搜索成功",
    "data": {
      "content": [
        {
          "id": 1,
          "username": "用户名",
          "phone": "手机号",
          "email": "邮箱",
          "userType": 1,
          "status": 1,
          "authStatus": 0,
          "companyId": 2,
          "companyName": "测试企业有限公司",
          "lastLoginTime": "2025-11-10 22:00:00",
          "createTime": "2025-11-10T22:00:00",
          "updateTime": "2025-11-10T22:00:00"
        }
      ],
      "totalElements": 25,
      "totalPages": 2,
      "size": 20,
      "number": 0,
      "keyword": "搜索关键词"
    }
  }
  ```
- **功能**: 搜索用户，返回用户信息包含企业ID和企业名称，使用投影查询优化性能

### 获取用户统计
- **URL**: `GET /stats`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "totalUsers": 1000,
      "todayNewUsers": 50,
      "weekNewUsers": 350,
      "monthNewUsers": 1000,
      "userTypeDistribution": {
        "jobSeekers": 600,
        "enterpriseUsers": 300,
        "admins": 100
      },
      "statusDistribution": {
        "active": 850,
        "inactive": 150
      },
      "authDistribution": {
        "verified": 450,
        "unverified": 550
      },
      "dailyActiveUsers": 500,
      "weeklyActiveUsers": 800,
      "monthlyActiveUsers": 950,
      "averageLoginFrequency": 3.5,
      "userRetentionRate": 0.85
    }
  }
  ```
- **功能**: 获取用户统计

### 批量更新用户状态
- **URL**: `PUT /batch-status`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userIds | 请求体 | List<Long> | 是 | 用户ID列表 |
  | status | 请求体 | Integer | 是 | 用户状态：1-正常，2-禁用 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "批量更新成功",
    "data": {
      "successCount": 5,
      "failedCount": 0,
      "failedUserIds": [],
      "updatedStatus": 1,
      "updateTime": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 批量更新用户状态

### 批量删除用户
- **URL**: `DELETE /batch`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userIds | 请求体 | List<Long> | 是 | 用户ID列表 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "批量删除成功",
    "data": {
      "successCount": 5,
      "failedCount": 0,
      "failedUserIds": [],
      "deletedCount": 5,
      "deleteTime": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 批量删除用户

### 获取用户活跃度
- **URL**: `GET /{id}/activity`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 用户ID |
  | days | 查询 | Integer | 否 | 统计天数，默认30天 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "userId": 1,
      "username": "用户名",
      "totalLoginCount": 150,
      "lastLoginTime": "2025-11-08T22:00:00",
      "loginDays": 25,
      "averageDailyLogin": 6,
      "activityScore": 85,
      "activityLevel": "高活跃",
      "dailyActivity": [
        {
          "date": "2025-11-01",
          "loginCount": 5,
          "jobViews": 20,
          "resumeUpdates": 1,
          "jobApplies": 3
        },
        {
          "date": "2025-11-02",
          "loginCount": 6,
          "jobViews": 25,
          "resumeUpdates": 0,
          "jobApplies": 2
        }
      ],
      "weeklyActivity": {
        "loginCount": 42,
        "jobViews": 150,
        "resumeUpdates": 3,
        "jobApplies": 15
      },
      "monthlyActivity": {
        "loginCount": 150,
        "jobViews": 600,
        "resumeUpdates": 12,
        "jobApplies": 60
      }
    }
  }
  ```
- **功能**: 获取用户活跃度

### 获取用户行为统计
- **URL**: `GET /{id}/behavior-stats`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 用户ID |
  | startDate | 查询 | String | 否 | 开始日期 |
  | endDate | 查询 | String | 否 | 结束日期 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "userId": 1,
      "username": "用户名",
      "period": {
        "startDate": "2025-11-01",
        "endDate": "2025-11-08"
      },
      "behaviorStats": {
        "totalLogins": 42,
        "totalJobViews": 150,
        "totalJobApplies": 15,
        "totalResumeUpdates": 3,
        "totalMessages": 25,
        "totalNotifications": 10,
        "averageSessionDuration": 1800,
        "favoriteJobs": 8,
        "followedCompanies": 5
      },
      "dailyBehavior": [
        {
          "date": "2025-11-01",
          "logins": 5,
          "jobViews": 20,
          "jobApplies": 3,
          "resumeUpdates": 1,
          "messages": 4,
          "notifications": 2
        },
        {
          "date": "2025-11-02",
          "logins": 6,
          "jobViews": 25,
          "jobApplies": 2,
          "resumeUpdates": 0,
          "messages": 3,
          "notifications": 1
        }
      ],
      "behaviorTrend": {
        "loginTrend": "上升",
        "jobViewTrend": "稳定",
        "jobApplyTrend": "上升",
        "activityLevel": "高活跃"
      }
    }
  }
  ```
- **功能**: 获取用户行为统计

### 更新用户状态
- **URL**: `PUT /{id}/status`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 用户ID |
  | status | 请求体 | Integer | 是 | 用户状态：1-正常，2-禁用 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "状态更新成功",
    "data": {
      "id": 1,
      "status": 1
    }
  }
  ```
- **功能**: 更新用户状态

### 更新认证状态
- **URL**: `PUT /{id}/auth-status`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 用户ID |
  | authStatus | 请求体 | Integer | 是 | 认证状态：0-未认证，1-已认证 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "认证状态更新成功",
    "data": {
      "id": 1,
      "authStatus": 1
    }
  }
  ```
- **功能**: 更新认证状态

### 删除用户
- **URL**: `DELETE /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "删除成功",
    "data": null
  }
  ```
- **功能**: 删除用户

## 使用示例

### 用户注册示例
```bash
curl -X POST "http://localhost:8080/api/users/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "phone": "13800138000",
    "email": "test@example.com",
    "password": "password123",
    "userType": 1
  }'
```

### 用户登录示例
```bash
curl -X POST "http://localhost:8080/api/users/login" \
  -H "Content-Type: application/json" \
  -d '{
    "account": "wangcheng03",
    "password": "ABCabc@123"
  }'
```

### 获取用户信息示例
```bash
curl -X GET "http://localhost:8080/api/users/1" \
  -H "Authorization: Bearer {token}"
```

## 性能优化说明

### 投影查询优化

为了提升查询性能和避免Hibernate懒加载序列化问题，系统采用了投影查询技术来优化企业信息查询：

#### 优化内容
1. **创建UserWithCompanyDTO投影类**
   - 包含用户基本信息和企业关键信息（企业ID、企业名称）
   - 避免返回整个企业对象，减少数据传输量

2. **优化查询接口**
   - `GET /{id}` - 获取用户信息（包含企业信息）
   - `GET /` - 分页查询用户列表（包含企业信息）
   - `GET /search` - 搜索用户（包含企业信息）

3. **投影查询方法**
   - `findUserWithCompanyById(Long userId)` - 根据ID查询用户与企业信息
   - `listUsersWithCompany(Pageable pageable)` - 分页查询用户列表
   - `listUsersWithCompanyByUserType(Integer userType, Pageable pageable)` - 按用户类型查询
   - `listUsersWithCompanyByConditions(Integer userType, Integer status, Integer authStatus, Pageable pageable)` - 多条件查询
   - `searchUsersWithCompany(String keyword, Integer userType, Pageable pageable)` - 关键词搜索

#### 优化效果
- **性能提升**：减少数据库查询次数，避免N+1查询问题
- **数据传输优化**：只返回必要的字段，减少网络传输量
- **序列化安全**：避免Hibernate懒加载导致的序列化异常
- **响应格式统一**：确保所有查询接口返回一致的响应格式

#### 响应示例
```json
{
  "id": 1,
  "username": "testuser",
  "phone": "13800138000",
  "email": "test@example.com",
  "userType": 2,
  "status": 1,
  "authStatus": 1,
  "companyId": 2,
  "companyName": "测试企业有限公司",
  "lastLoginTime": "2025-11-10T22:00:00",
  "createTime": "2025-11-10T22:00:00",
  "updateTime": "2025-11-10T22:00:00"
}
```

## 注意事项

- 用户注册、登录、发送验证码、重置密码接口无需认证
- 其他所有接口都需要有效的JWT Token进行认证
- 用户状态：0-禁用，1-正常，2-黑名单
- 认证状态：0-未认证，1-审核中，2-已认证
- 用户类型：1-求职者，2-企业用户，3-管理员
- 性别：0-未知，1-男，2-女
- 学历：高中、大专、本科、硕士、博士、其他

[返回主文档](../docs/README.md)
