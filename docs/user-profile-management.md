# 用户资料管理模块 (UserProfileController)

**基础路径**: `/api/user-profiles`

## 接口列表

### 创建用户资料
- **URL**: `POST /{userId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
  | UserProfileDTO | 请求体 | Object | 是 | 用户资料信息对象 |

  **UserProfileCreateDTO对象结构**:
  ```json
  {
    "realName": "真实姓名",        // 字符串，必填，真实姓名
    "gender": 1,                  // 整数，可选，性别：0-未知，1-男，2-女
    "birthday": "1990-01-01",     // 字符串，可选，生日
    "avatar": "头像URL",          // 字符串，可选，头像URL
    "idCardFront": "身份证正面",   // 字符串，可选，身份证正面照片URL
    "idCardBack": "身份证背面",    // 字符串，可选，身份证背面照片URL
    "education": "本科",           // 字符串，可选，学历
    "workYears": 3,               // 整数，可选，工作年限
    "currentSalary": 15000.00,    // 数字，可选，当前薪资
    "expectedSalary": 20000.00,   // 数字，可选，期望薪资
    "city": "北京",                // 字符串，可选，所在城市
    "skills": ["Java", "Spring"], // 数组，可选，技能标签列表
    "selfIntro": "个人简介",       // 字符串，可选，个人介绍
    "preferredCities": ["北京", "上海"], // 数组，可选，期望城市列表
    "jobTypes": ["全职", "兼职"],  // 数组，可选，工作类型列表
    "industries": ["互联网", "金融"], // 数组，可选，行业偏好列表
    "workMode": "全职",           // 字符串，可选，工作模式：全职/兼职/实习
    "jobStatus": "积极求职"       // 字符串，可选，求职状态：积极求职/观望中/在职看机会
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "创建用户资料成功",
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
      "skills": "Java,Spring",
      "selfIntro": "个人简介",
      "createTime": "2025-11-08T12:00:00",
      "updateTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 创建用户资料

### 获取用户资料详情
- **URL**: `GET /{userId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取用户资料详情成功",
    "data": {
      // 用户基础信息
      "id": 1,
      "username": "用户名",
      "phone": "手机号",
      "email": "邮箱",
      "userType": 1,
      "status": 1,
      "authStatus": 0,
      
      // 用户资料信息
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
      "skills": ["Java", "Spring", "MySQL"], // 技能标签列表
      "selfIntro": "个人简介",
      
      // 求职偏好
      "preferredCities": ["北京", "上海"], // 期望城市列表
      "jobTypes": ["全职", "兼职"],       // 工作类型列表
      "industries": ["互联网", "金融"],    // 行业偏好列表
      "workMode": "全职",                // 工作模式：全职/兼职/实习
      "jobStatus": "积极求职",            // 求职状态：积极求职/观望中/在职看机会
      
      // 统计信息
      "totalResumes": 5,        // 简历总数
      "jobApplyCount": 20,      // 投递次数
      "interviewCount": 5,      // 面试次数
      "offerCount": 2,          // 收到offer数
      
      // 时间信息
      "createTime": "2025-11-08T12:00:00",
      "updateTime": "2025-11-08T12:00:00",
      "lastLoginTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 获取用户资料详情，包含用户基础信息、详细资料信息、求职偏好和统计信息

### 更新用户资料
- **URL**: `PUT /{userId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
  | UserProfileUpdateDTO | 请求体 | Object | 是 | 用户资料更新对象 |

  **UserProfileUpdateDTO对象结构**:
  ```json
  {
    "realName": "更新后的真实姓名", // 字符串，可选，真实姓名
    "gender": 1,                  // 整数，可选，性别：0-未知，1-男，2-女
    "birthday": "1990-01-01",     // 字符串，可选，生日
    "avatar": "更新后的头像URL",   // 字符串，可选，头像URL
    "idCardFront": "身份证正面",   // 字符串，可选，身份证正面照片URL
    "idCardBack": "身份证背面",    // 字符串，可选，身份证背面照片URL
    "education": "硕士",           // 字符串，可选，学历
    "workYears": 5,               // 整数，可选，工作年限
    "currentSalary": 20000.00,    // 数字，可选，当前薪资
    "expectedSalary": 25000.00,   // 数字，可选，期望薪资
    "city": "上海",                // 字符串，可选，所在城市
    "skills": ["Java", "Spring", "MySQL"], // 数组，可选，技能标签列表
    "selfIntro": "更新后的个人简介", // 字符串，可选，个人介绍
    "preferredCities": ["上海", "杭州"], // 数组，可选，期望城市列表
    "jobTypes": ["全职"],         // 数组，可选，工作类型列表
    "industries": ["互联网"],      // 数组，可选，行业偏好列表
    "workMode": "全职",           // 字符串，可选，工作模式：全职/兼职/实习
    "jobStatus": "在职看机会"     // 字符串，可选，求职状态：积极求职/观望中/在职看机会
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
      "realName": "更新后的真实姓名",
      "gender": 1,
      "birthday": "1990-01-01",
      "avatar": "更新后的头像URL",
      "education": "硕士",
      "workYears": 5,
      "currentSalary": 20000.00,
      "expectedSalary": 25000.00,
      "city": "上海",
      "skills": "Java,Spring,MySQL",
      "selfIntro": "更新后的个人简介",
      "createTime": "2025-11-08T12:00:00",
      "updateTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 更新用户资料

### 更新技能标签
- **URL**: `PUT /{userId}/skills`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
  | skills | 请求体 | List<String> | 是 | 技能标签列表 |
  
  **请求体示例**:
  ```json
  ["Java", "Spring Boot", "MySQL", "Redis"]
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新技能标签成功",
    "data": {
      "id": 1,
      "userId": 1,
      "skills": "[\"Java\",\"Spring Boot\",\"MySQL\",\"Redis\"]",
      "updateTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 更新用户技能标签

### 更新求职偏好
- **URL**: `PUT /{userId}/job-preferences`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
  | preferences | 请求体 | Map<String, Object> | 是 | 求职偏好对象 |
  
  **请求体示例**:
  ```json
  {
    "preferredCities": ["北京", "上海", "深圳"],
    "jobTypes": ["全职", "兼职"],
    "industries": ["互联网", "金融科技"],
    "workMode": "全职",
    "jobStatus": "积极求职"
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新求职偏好成功",
    "data": {
      "id": 1,
      "userId": 1,
      "preferredCities": "[\"北京\",\"上海\",\"深圳\"]",
      "jobTypes": "[\"全职\",\"兼职\"]",
      "industries": "[\"互联网\",\"金融科技\"]",
      "workMode": "全职",
      "jobStatus": "积极求职",
      "updateTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 更新用户求职偏好

### 分页查询用户资料列表
- **URL**: `GET /`
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
          "username": "用户名",
          "phone": "手机号",
          "email": "邮箱",
          "userType": 1,
          "status": 1,
          "authStatus": 0,
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
          "skills": ["Java", "Spring", "MySQL"],
          "selfIntro": "个人简介",
          "preferredCities": ["北京", "上海"],
          "jobTypes": ["全职", "兼职"],
          "industries": ["互联网", "金融"],
          "workMode": "全职",
          "jobStatus": "积极求职",
          "totalResumes": 5,
          "jobApplyCount": 20,
          "interviewCount": 5,
          "offerCount": 2,
          "createTime": "2025-11-08T12:00:00",
          "updateTime": "2025-11-08T12:00:00",
          "lastLoginTime": "2025-11-08T12:00:00"
        }
      ],
      "totalElements": 100,
      "totalPages": 5,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 分页查询用户资料列表，支持多条件筛选

### 根据技能搜索用户资料
- **URL**: `POST /search/skills`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | skills | 请求体 | List<String> | 是 | 技能列表 |
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
  
  **请求体示例**:
  ```json
  ["Java", "Spring Boot", "MySQL"]
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "根据技能搜索用户资料成功",
    "data": {
      "content": [...],
      "totalElements": 15,
      "totalPages": 1,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 根据技能搜索用户资料

### 根据城市搜索用户资料
- **URL**: `GET /search/city/{city}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | city | 路径 | String | 是 | 城市名称 |
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "根据城市搜索用户资料成功",
    "data": {
      "content": [...],
      "totalElements": 12,
      "totalPages": 1,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 根据城市搜索用户资料

### 根据工作年限搜索用户资料
- **URL**: `GET /search/work-years`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | minYears | 查询 | Integer | 否 | 最小工作年限 |
  | maxYears | 查询 | Integer | 否 | 最大工作年限 |
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "根据工作年限搜索用户资料成功",
    "data": {
      "content": [...],
      "totalElements": 10,
      "totalPages": 1,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 根据工作年限搜索用户资料

### 根据期望薪资搜索用户资料
- **URL**: `GET /search/expected-salary`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | minSalary | 查询 | Double | 否 | 最小期望薪资 |
  | maxSalary | 查询 | Double | 否 | 最大期望薪资 |
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "根据期望薪资搜索用户资料成功",
    "data": {
      "content": [...],
      "totalElements": 8,
      "totalPages": 1,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 根据期望薪资搜索用户资料

### 获取用户资料统计
- **URL**: `GET /{userId}/stats`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取用户资料统计成功",
    "data": {
      "userId": 1,
      "totalResumes": 5,
      "jobApplyCount": 20,
      "interviewCount": 5,
      "offerCount": 2,
      "profileCompleteness": 85,
      "lastUpdateTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 获取用户资料统计信息

### 获取热门技能标签
- **URL**: `GET /popular-skills`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | limit | 查询 | Integer | 否 | 限制数量，默认10 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取热门技能标签成功",
    "data": [
      {
        "skill": "Java",
        "count": 150
      },
      {
        "skill": "Spring Boot",
        "count": 120
      },
      {
        "skill": "MySQL",
        "count": 100
      }
    ]
  }
  ```
- **功能**: 获取热门技能标签

### 获取城市分布统计
- **URL**: `GET /city-distribution`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取城市分布统计成功",
    "data": [
      {
        "city": "北京",
        "count": 150
      },
      {
        "city": "上海",
        "count": 120
      },
      {
        "city": "深圳",
        "count": 100
      }
    ]
  }
  ```
- **功能**: 获取城市分布统计

### 获取工作年限分布统计
- **URL**: `GET /work-years-distribution`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取工作年限分布统计成功",
    "data": [
      {
        "workYears": 1,
        "count": 50
      },
      {
        "workYears": 3,
        "count": 80
      },
      {
        "workYears": 5,
        "count": 60
      }
    ]
  }
  ```
- **功能**: 获取工作年限分布统计

### 获取期望薪资分布统计
- **URL**: `GET /expected-salary-distribution`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取期望薪资分布统计成功",
    "data": [
      {
        "salaryRange": "10k-15k",
        "count": 80
      },
      {
        "salaryRange": "15k-20k",
        "count": 120
      },
      {
        "salaryRange": "20k-25k",
        "count": 60
      }
    ]
  }
  ```
- **功能**: 获取期望薪资分布统计

### 增加投递次数
- **URL**: `POST /{userId}/increment-apply`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "增加投递次数成功",
    "data": null
  }
  ```
- **功能**: 增加用户投递次数

### 增加面试次数
- **URL**: `POST /{userId}/increment-interview`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "增加面试次数成功",
    "data": null
  }
  ```
- **功能**: 增加用户面试次数

### 增加offer数
- **URL**: `POST /{userId}/increment-offer`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "增加offer数成功",
    "data": null
  }
  ```
- **功能**: 增加用户收到offer数

## 使用示例

### 创建用户资料示例
```bash
curl -X POST "http://localhost:8080/api/user-profiles/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "userId": 1,
    "realName": "张三",
    "gender": 1,
    "birthday": "1995-12-10",
    "avatar": "/avatars/user1.jpg",
    "education": "本科",
    "workYears": 3,
    "currentSalary": 15000.00,
    "expectedSalary": 20000.00,
    "city": "北京",
    "skills": "Java,Spring Boot,MySQL",
    "selfIntro": "热爱技术，善于学习，有良好的团队协作能力"
  }'
```

### 获取用户资料详情示例
```bash
curl -X GET "http://localhost:8080/api/user-profiles/1" \
  -H "Authorization: Bearer {token}"
```

### 更新技能标签示例
```bash
curl -X PUT "http://localhost:8080/api/user-profiles/1/skills" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '["Java", "Spring Boot", "MySQL", "Redis", "Docker"]'
```

### 分页查询用户资料列表示例
```bash
curl -X GET "http://localhost:8080/api/user-profiles?page=0&size=10&city=北京&education=本科&minWorkYears=1&maxWorkYears=5&skill=Java" \
  -H "Authorization: Bearer {token}"
```

## 注意事项

- 所有用户资料管理接口都需要有效的JWT Token进行认证
- 用户资料与用户账号信息关联，通过userId进行关联
- **DTO类型说明**：
  - `UserProfileCreateDTO` - 用户资料创建接口专用，包含所有可编辑字段
  - `UserProfileUpdateDTO` - 用户资料更新接口专用，与创建接口字段完全一致
  - `UserProfileDetailDTO` - 用户资料详情接口专用，包含完整信息（基础信息、资料信息、求职偏好、统计信息）
- **字段类型转换**：
  - 技能标签、期望城市、工作类型、行业偏好在DTO中为List<String>类型
  - 在实体中存储为JSON字符串格式
  - 服务层自动处理类型转换
- **统计字段**：
  - 统计信息（简历数量、投递次数、面试次数、offer数）只在详情接口中返回
  - 创建和更新接口不包含统计字段，统计信息由系统自动维护
- **求职偏好**：
  - 包含期望城市、工作类型、行业偏好、工作模式、求职状态
  - 支持在创建和更新接口中设置
- 支持多条件搜索和筛选，便于企业招聘人员查找合适的人才

[返回主文档](../docs/README.md)
