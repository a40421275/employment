# 企业管理模块 (CompanyController)

**基础路径**: `/api/companies`

## 接口列表

### 创建企业
- **URL**: `POST /`
- **请求参数**:
  ```json
  {
    "companyName": "企业名称",          // 字符串，必填，长度1-200字符
    "companyShortName": "企业简称",     // 字符串，可选，长度1-100字符
    "companyLogo": "企业Logo",         // 字符串，可选，Logo图片URL
    "companyType": 1,                  // 整数，必填，企业类型：1-有限责任公司，2-股份有限公司，3-个人独资企业，4-合伙企业，5-外资企业，6-其他
    "industry": "所属行业",             // 字符串，必填，长度1-100字符
    "scale": 3,                        // 整数，必填，企业规模：1-1-50人，2-51-100人，3-101-500人，4-501-1000人，5-1000人以上
    "legalPerson": "法人代表",          // 字符串，必填，长度1-50字符
    "registeredCapital": 1000.00,      // 数字，可选，注册资本（万元）
    "establishDate": "2020-01-01",     // 字符串，可选，成立日期（格式：yyyy-MM-dd）
    "businessLicenseNo": "营业执照号",  // 字符串，可选，营业执照号
    "businessLicenseImage": "营业执照图片", // 字符串，可选，营业执照图片URL
    "businessScope": "经营范围",        // 字符串，可选，经营范围
    "province": "省份",                // 字符串，必填，省份
    "city": "城市",                    // 字符串，必填，城市
    "district": "区县",                // 字符串，可选，区县
    "address": "详细地址",              // 字符串，必填，详细地址
    "latitude": 39.9042,               // 数字，可选，纬度
    "longitude": 116.4074,             // 数字，可选，经度
    "contactPerson": "联系人",          // 字符串，必填，联系人
    "contactPhone": "联系电话",         // 字符串，必填，联系电话
    "contactEmail": "联系邮箱",         // 字符串，可选，联系邮箱
    "website": "公司网站",              // 字符串，可选，公司网站
    "introduction": "公司介绍",         // 字符串，可选，公司介绍
    "culture": "企业文化",              // 字符串，可选，企业文化
    "welfare": "公司福利",              // 字符串，可选，公司福利
    "development": "发展历程",          // 字符串，可选，发展历程
    "honors": "荣誉资质"                // 字符串，可选，荣誉资质
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,                        // 整数，状态码
    "message": "创建企业成功",           // 字符串，响应消息
    "data": {
      "id": 1,                          // 长整型，企业ID
      "companyName": "企业名称",         // 字符串，企业名称
      "companyShortName": "企业简称",    // 字符串，企业简称
      "companyType": 1,                 // 整数，企业类型
      "companyTypeDesc": "有限责任公司", // 字符串，企业类型描述
      "industry": "所属行业",            // 字符串，所属行业
      "scale": 3,                       // 整数，企业规模
      "scaleDesc": "101-500人",         // 字符串，企业规模描述
      "legalPerson": "法人代表",         // 字符串，法人代表
      "registeredCapital": 1000.00,     // 数字，注册资本
      "establishDate": "2020-01-01",    // 字符串，成立日期
      "businessLicenseNo": "营业执照号", // 字符串，营业执照号
      "province": "省份",               // 字符串，省份
      "city": "城市",                   // 字符串，城市
      "district": "区县",               // 字符串，区县
      "address": "详细地址",             // 字符串，详细地址
      "contactPerson": "联系人",         // 字符串，联系人
      "contactPhone": "联系电话",        // 字符串，联系电话
      "contactEmail": "联系邮箱",        // 字符串，联系邮箱
      "website": "公司网站",             // 字符串，公司网站
      "status": 1,                      // 整数，状态：0-禁用，1-正常，2-审核中，3-审核失败
      "statusDesc": "正常",             // 字符串，状态描述
      "authStatus": 0,                  // 整数，认证状态：0-未认证，1-审核中，2-已认证
      "authStatusDesc": "未认证",       // 字符串，认证状态描述
      "jobCount": 0,                    // 整数，岗位数量
      "viewCount": 0,                   // 整数，浏览数量
      "favoriteCount": 0,               // 整数，收藏数量
      "applyCount": 0,                  // 整数，申请数量
      "createTime": "2025-11-11T10:00:00",  // 字符串，创建时间
      "updateTime": "2025-11-11T10:00:00"   // 字符串，更新时间
    },
    "timestamp": "2025-11-11T10:00:00"      // 字符串，响应时间戳
  }
  ```
- **功能**: 创建新的企业信息

### 更新企业信息
- **URL**: `PUT /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 企业ID |
  | CompanyDTO | 请求体 | Object | 是 | 企业信息对象 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新企业信息成功",
    "data": {
      "id": 1,
      "companyName": "企业名称",
      "companyShortName": "企业简称",
      "companyType": 1,
      "companyTypeDesc": "有限责任公司",
      "industry": "所属行业",
      "scale": 3,
      "scaleDesc": "101-500人",
      "legalPerson": "法人代表",
      "registeredCapital": 1000.00,
      "establishDate": "2020-01-01",
      "businessLicenseNo": "营业执照号",
      "province": "省份",
      "city": "城市",
      "district": "区县",
      "address": "详细地址",
      "contactPerson": "联系人",
      "contactPhone": "联系电话",
      "contactEmail": "联系邮箱",
      "website": "公司网站",
      "status": 1,
      "statusDesc": "正常",
      "authStatus": 0,
      "authStatusDesc": "未认证",
      "jobCount": 10,
      "viewCount": 100,
      "favoriteCount": 20,
      "applyCount": 50,
      "createTime": "2025-11-11T10:00:00",
      "updateTime": "2025-11-11T10:00:00"
    },
    "timestamp": "2025-11-11T10:00:00"
  }
  ```
- **功能**: 根据ID更新企业信息

### 获取企业详情
- **URL**: `GET /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 企业ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取企业详情成功",
    "data": {
      "id": 1,
      "companyName": "企业名称",
      "companyShortName": "企业简称",
      "companyLogo": "企业Logo",
      "companyType": 1,
      "companyTypeDesc": "有限责任公司",
      "industry": "所属行业",
      "scale": 3,
      "scaleDesc": "101-500人",
      "legalPerson": "法人代表",
      "registeredCapital": 1000.00,
      "establishDate": "2020-01-01",
      "businessLicenseNo": "营业执照号",
      "businessLicenseImage": "营业执照图片",
      "businessScope": "经营范围",
      "province": "省份",
      "city": "城市",
      "district": "区县",
      "address": "详细地址",
      "latitude": 39.9042,
      "longitude": 116.4074,
      "contactPerson": "联系人",
      "contactPhone": "联系电话",
      "contactEmail": "联系邮箱",
      "website": "公司网站",
      "introduction": "公司介绍",
      "culture": "企业文化",
      "welfare": "公司福利",
      "development": "发展历程",
      "honors": "荣誉资质",
      "status": 1,
      "statusDesc": "正常",
      "authStatus": 2,
      "authStatusDesc": "已认证",
      "authTime": "2025-11-11T10:00:00",
      "authOperatorId": 1,
      "jobCount": 10,
      "viewCount": 100,
      "favoriteCount": 20,
      "applyCount": 50,
      "createTime": "2025-11-11T10:00:00",
      "updateTime": "2025-11-11T10:00:00"
    },
    "timestamp": "2025-11-11T10:00:00"
  }
  ```
- **功能**: 根据ID获取企业详细信息

### 根据名称获取企业
- **URL**: `GET /name/{companyName}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | companyName | 路径 | String | 是 | 企业名称 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取企业信息成功",
    "data": {
      "id": 1,
      "companyName": "企业名称",
      "companyShortName": "企业简称",
      "companyType": 1,
      "companyTypeDesc": "有限责任公司",
      "industry": "所属行业",
      "scale": 3,
      "scaleDesc": "101-500人",
      "city": "城市",
      "status": 1,
      "statusDesc": "正常",
      "authStatus": 2,
      "authStatusDesc": "已认证",
      "jobCount": 10,
      "viewCount": 100,
      "createTime": "2025-11-11T10:00:00"
    },
    "timestamp": "2025-11-11T10:00:00"
  }
  ```
- **功能**: 根据企业名称获取企业信息

### 删除企业
- **URL**: `DELETE /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 企业ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "删除企业成功",
    "data": null,
    "timestamp": "2025-11-11T10:00:00"
  }
  ```
- **功能**: 根据ID删除企业（逻辑删除）

### 分页查询企业列表
- **URL**: `GET /`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认10 |
  | sort | 查询 | String | 否 | 排序字段，默认createTime |
  | direction | 查询 | String | 否 | 排序方向，默认desc |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询企业列表成功",
    "data": {
      "content": [
        {
          "id": 1,
          "companyName": "企业名称",
          "companyShortName": "企业简称",
          "companyType": 1,
          "companyTypeDesc": "有限责任公司",
          "industry": "所属行业",
          "scale": 3,
          "scaleDesc": "101-500人",
          "city": "城市",
          "status": 1,
          "statusDesc": "正常",
          "authStatus": 2,
          "authStatusDesc": "已认证",
          "jobCount": 10,
          "viewCount": 100,
          "favoriteCount": 20,
          "applyCount": 50,
          "createTime": "2025-11-11T10:00:00"
        }
      ],
      "totalElements": 100,
      "totalPages": 10,
      "size": 10,
      "number": 0
    },
    "timestamp": "2025-11-11T10:00:00"
  }
  ```
- **功能**: 分页获取企业列表

### 统一查询企业列表
- **URL**: `POST /query`
- **请求参数**:
  ```json
  {
    "keyword": "搜索关键词",           // 字符串，可选，搜索企业名称、简称
    "city": "城市",                  // 字符串，可选，城市筛选
    "industry": "行业",              // 字符串，可选，行业筛选
    "companyType": 1,               // 整数，可选，企业类型筛选：1-有限责任公司，2-股份有限公司，3-个人独资企业，4-合伙企业，5-外资企业，6-其他
    "scale": 3,                     // 整数，可选，企业规模筛选：1-1-50人，2-51-100人，3-101-500人，4-501-1000人，5-1000人以上
    "status": 1,                    // 整数，可选，状态筛选：0-禁用，1-正常，2-审核中，3-审核失败
    "authStatus": 2,                // 整数，可选，认证状态筛选：0-未认证，1-审核中，2-已认证
    "province": "省份",              // 字符串，可选，省份筛选
    "district": "区县",              // 字符串，可选，区县筛选
    "page": 0,                      // 整数，可选，页码，从0开始，默认0
    "size": 10,                     // 整数，可选，每页大小，默认10
    "sort": "createTime",           // 字符串，可选，排序字段，默认createTime
    "direction": "desc"             // 字符串，可选，排序方向，默认desc
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询企业列表成功",
    "data": {
      "content": [
        {
          "id": 1,
          "companyName": "企业名称",
          "companyShortName": "企业简称",
          "companyType": 1,
          "companyTypeDesc": "有限责任公司",
          "industry": "所属行业",
          "scale": 3,
          "scaleDesc": "101-500人",
          "city": "城市",
          "status": 1,
          "statusDesc": "正常",
          "authStatus": 2,
          "authStatusDesc": "已认证",
          "jobCount": 10,
          "viewCount": 100,
          "favoriteCount": 20,
          "applyCount": 50,
          "createTime": "2025-11-11T10:00:00"
        }
      ],
      "totalElements": 100,
      "totalPages": 10,
      "size": 10,
      "number": 0
    },
    "timestamp": "2025-11-11T10:00:00"
  }
  ```
- **功能**: 支持各种筛选和分页查询企业列表，可组合多个查询条件

### 根据状态查询企业列表
- **URL**: `GET /status/{status}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | status | 路径 | Integer | 是 | 状态：0-禁用，1-正常，2-审核中，3-审核失败 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询企业列表成功",
    "data": [
      {
        "id": 1,
        "companyName": "企业名称",
        "companyType": 1,
        "companyTypeDesc": "有限责任公司",
        "industry": "所属行业",
        "city": "城市",
        "status": 1,
        "statusDesc": "正常",
        "authStatus": 2,
        "authStatusDesc": "已认证",
        "jobCount": 10,
        "viewCount": 100,
        "createTime": "2025-11-11T10:00:00"
      }
    ],
    "timestamp": "2025-11-11T10:00:00"
  }
  ```
- **功能**: 根据状态获取企业列表

### 根据认证状态查询企业列表
- **URL**: `GET /auth-status/{authStatus}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | authStatus | 路径 | Integer | 是 | 认证状态：0-未认证，1-审核中，2-已认证 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询企业列表成功",
    "data": [
      {
        "id": 1,
        "companyName": "企业名称",
        "companyType": 1,
        "companyTypeDesc": "有限责任公司",
        "industry": "所属行业",
        "city": "城市",
        "status": 1,
        "statusDesc": "正常",
        "authStatus": 2,
        "authStatusDesc": "已认证",
        "jobCount": 10,
        "viewCount": 100,
        "createTime": "2025-11-11T10:00:00"
      }
    ],
    "timestamp": "2025-11-11T10:00:00"
  }
  ```
- **功能**: 根据认证状态获取企业列表

### 根据行业查询企业列表
- **URL**: `GET /industry/{industry}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | industry | 路径 | String | 是 | 行业 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询企业列表成功",
    "data": [
      {
        "id": 1,
        "companyName": "企业名称",
        "companyType": 1,
        "companyTypeDesc": "有限责任公司",
        "industry": "互联网",
        "city": "城市",
        "status": 1,
        "statusDesc": "正常",
        "authStatus": 2,
        "authStatusDesc": "已认证",
        "jobCount": 10,
        "viewCount": 100,
        "createTime": "2025-11-11T10:00:00"
      }
    ],
    "timestamp": "2025-11-11T10:00:00"
  }
  ```
- **功能**: 根据行业获取企业列表

### 根据城市查询企业列表
- **URL**: `GET /city/{city}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | city | 路径 | String | 是 | 城市 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询企业列表成功",
    "data": [
      {
        "id": 1,
        "companyName": "企业名称",
        "companyType": 1,
        "companyTypeDesc": "有限责任公司",
        "industry": "所属行业",
        "city": "城市",
        "status": 1,
        "statusDesc": "正常",
        "authStatus": 2,
        "authStatusDesc": "已认证",
        "jobCount": 10,
        "viewCount": 100,
        "createTime": "2025-11-11T10:00:00"
      }
    ],
    "timestamp": "2025-11-11T10:00:00"
  }
  ```
- **功能**: 根据城市获取企业列表

### 搜索企业
- **URL**: `GET /search`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | keyword | 查询 | String | 是 | 搜索关键词 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "搜索企业成功",
    "data": [
      {
        "id": 1,
        "companyName": "企业名称",
        "companyType": 1,
        "companyTypeDesc": "有限责任公司",
        "industry": "所属行业",
        "city": "城市",
        "status": 1,
        "statusDesc": "正常",
        "authStatus": 2,
        "authStatusDesc": "已认证",
        "jobCount": 10,
        "viewCount": 100,
        "createTime": "2025-11-11T10:00:00"
      }
    ],
    "timestamp": "2025-11-11T10:00:00"
  }
  ```
- **功能**: 根据关键词搜索企业

### 审核企业认证
- **URL**: `POST /{id}/audit`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 企业ID |
  | authStatus | 查询 | Integer | 是 | 认证状态：0-未认证，1-审核中，2-已认证 |
  | authFailReason | 查询 | String | 否 | 认证失败原因 |
  | operatorId | 查询 | Long | 是 | 操作员ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "审核企业认证成功",
    "data": {
      "id": 1,
      "companyName": "企业名称",
      "authStatus": 2,
      "authStatusDesc": "已认证",
      "authTime": "2025-11-11T10:00:00",
      "authOperatorId": 1,
      "authFailReason": null,
      "updateTime": "2025-11-11T10:00:00"
    },
    "timestamp": "2025-11-11T10:00:00"
  }
  ```
- **功能**: 审核企业认证申请

### 更新企业状态
- **URL**: `PUT /{id}/status`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 企业ID |
  | status | 查询 | Integer | 是 | 状态：0-禁用，1-正常，2-审核中，3-审核失败 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新企业状态成功",
    "data": {
      "id": 1,
      "companyName": "企业名称",
      "status": 1,
      "statusDesc": "正常",
      "updateTime": "2025-11-11T10:00:00"
    },
    "timestamp": "2025-11-11T10:00:00"
  }
  ```
- **功能**: 更新企业状态

### 统计企业数量
- **URL**: `GET /count`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | status | 查询 | Integer | 否 | 状态：0-禁用，1-正常，2-审核中，3-审核失败 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "统计企业数量成功",
    "data": 100,
    "timestamp": "2025-11-11T10:00:00"
  }
  ```
- **功能**: 根据状态统计企业数量

### 统计认证企业数量
- **URL**: `GET /count/auth`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | authStatus | 查询 | Integer | 是 | 认证状态：0-未认证，1-审核中，2-已认证 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "统计认证企业数量成功",
    "data": 50,
    "timestamp": "2025-11-11T10:00:00"
  }
  ```
- **功能**: 根据认证状态统计企业数量

### 检查企业名称是否存在
- **URL**: `GET /check/name`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | companyName | 查询 | String | 是 | 企业名称 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "检查企业名称成功",
    "data": true,
    "timestamp": "2025-11-11T10:00:00"
  }
  ```
- **功能**: 检查企业名称是否已存在

### 检查营业执照号是否存在
- **URL**: `GET /check/license`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | businessLicenseNo | 查询 | String | 是 | 营业执照号 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "检查营业执照号成功",
    "data": false,
    "timestamp": "2025-11-11T10:00:00"
  }
  ```
- **功能**: 检查营业执照号是否已存在

### 获取企业选项列表
- **URL**: `GET /options`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | keyword | 查询 | String | 否 | 搜索关键词 |
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取企业选项列表成功",
    "data": {
      "content": [
        {
          "id": 1,
          "companyName": "企业名称"
        },
        {
          "id": 2,
          "companyName": "另一家企业"
        }
      ],
      "totalElements": 100,
      "totalPages": 5,
      "size": 20,
      "number": 0
    },
    "timestamp": "2025-11-11T10:00:00"
  }
  ```
- **功能**: 用于用户关联公司时查询公司列表，支持名称搜索，返回简化的企业信息（仅包含ID和名称）

## 使用示例

### 创建企业示例
```bash
curl -X POST "http://localhost:8080/api/companies" \
  -H "Content-Type: application/json" \
  -d '{
    "companyName": "示例科技有限公司",
    "companyShortName": "示例科技",
    "companyType": 1,
    "industry": "互联网",
    "scale": 3,
    "legalPerson": "张三",
    "registeredCapital": 1000.00,
    "establishDate": "2020-01-01",
    "businessLicenseNo": "123456789012345",
    "province": "北京市",
    "city": "北京市",
    "district": "海淀区",
    "address": "中关村大街1号",
    "contactPerson": "李四",
    "contactPhone": "13800138000",
    "contactEmail": "contact@example.com",
    "introduction": "公司介绍..."
  }'
```

### 获取企业详情示例
```bash
curl -X GET "http://localhost:8080/api/companies/1" \
  -H "Authorization: Bearer {token}"
```

### 审核企业认证示例
```bash
curl -X POST "http://localhost:8080/api/companies/1/audit?authStatus=2&operatorId=1" \
  -H "Authorization: Bearer {token}"
```

### 统一查询企业列表示例
```bash
# 基础分页查询
curl -X POST "http://localhost:8080/api/companies/query" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "page": 0,
    "size": 10
  }'

# 关键词搜索
curl -X POST "http://localhost:8080/api/companies/query" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "keyword": "科技",
    "page": 0,
    "size": 10
  }'

# 多条件组合查询
curl -X POST "http://localhost:8080/api/companies/query" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "keyword": "示例",
    "city": "北京市",
    "industry": "互联网",
    "authStatus": 2,
    "page": 0,
    "size": 10
  }'
```

### 获取企业选项列表示例
```bash
# 获取所有企业选项
curl -X GET "http://localhost:8080/api/companies/options" \
  -H "Authorization: Bearer {token}"

# 搜索企业选项
curl -X GET "http://localhost:8080/api/companies/options?keyword=科技" \
  -H "Authorization: Bearer {token}"

# 分页获取企业选项
curl -X GET "http://localhost:8080/api/companies/options?page=0&size=10" \
  -H "Authorization: Bearer {token}"
```

## 注意事项

- 企业名称必须唯一
- 营业执照号必须唯一（如果提供）
- 企业状态：0-禁用，1-正常，2-审核中，3-审核失败
- 认证状态：0-未认证，1-审核中，2-已认证
- 企业类型：1-有限责任公司，2-股份有限公司，3-个人独资企业，4-合伙企业，5-外资企业，6-其他
- 企业规模：1-1-50人，2-51-100人，3-101-500人，4-501-1000人，5-1000人以上

[返回主文档](../docs/README.md)
