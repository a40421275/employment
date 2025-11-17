# 系统配置管理模块 (SystemConfigController)

**基础路径**: `/api/system-configs`

## 接口列表

### 创建系统配置
- **URL**: `POST /`
- **请求参数** (SystemConfigDTO对象):
  | 字段名 | 类型 | 必填 | 说明 | 默认值 |
  |--------|------|------|------|--------|
  | configKey | String | 是 | 配置键 | - |
  | configValue | String | 是 | 配置值 | - |
  | description | String | 否 | 配置描述 | - |
  | configGroup | String | 否 | 配置分组 | - |
  | valueType | String | 否 | 配置值类型 | STRING |
  | editable | Boolean | 否 | 是否可编辑 | true |
  | systemConfig | Boolean | 否 | 是否为系统配置 | false |
  | sortOrder | Integer | 否 | 排序顺序 | 0 |
  | validationRule | String | 否 | 验证规则 | - |
  | defaultValue | String | 否 | 默认值 | - |
  | options | String | 否 | 选项值（JSON格式） | - |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "创建成功",
    "data": {
      "id": 1,
      "configKey": "site_name",
      "configValue": "就业服务平台",
      "description": "网站名称",
      "createTime": "2025-11-16T12:00:00",
      "updateTime": "2025-11-16T12:00:00"
    }
  }
  ```
- **功能**: 创建系统配置

### 更新系统配置
- **URL**: `PUT /{id}`
- **请求参数** (SystemConfigDTO对象):
  | 字段名 | 类型 | 必填 | 说明 | 默认值 |
  |--------|------|------|------|--------|
  | configKey | String | 是 | 配置键 | - |
  | configValue | String | 是 | 配置值 | - |
  | description | String | 否 | 配置描述 | - |
  | configGroup | String | 否 | 配置分组 | - |
  | valueType | String | 否 | 配置值类型 | STRING |
  | editable | Boolean | 否 | 是否可编辑 | true |
  | systemConfig | Boolean | 否 | 是否为系统配置 | false |
  | sortOrder | Integer | 否 | 排序顺序 | 0 |
  | validationRule | String | 否 | 验证规则 | - |
  | defaultValue | String | 否 | 默认值 | - |
  | options | String | 否 | 选项值（JSON格式） | - |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新成功",
    "data": {
      "id": 1,
      "configKey": "site_name",
      "configValue": "就业服务平台",
      "description": "网站名称",
      "configGroup": "site",
      "valueType": "STRING",
      "editable": true,
      "systemConfig": false,
      "sortOrder": 0,
      "validationRule": "",
      "defaultValue": "",
      "options": "",
      "createTime": "2025-11-16T12:00:00",
      "updateTime": "2025-11-16T12:05:00"
    }
  }
  ```
- **功能**: 更新系统配置

### 根据配置键更新配置值
- **URL**: `PUT /key/{configKey}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | configKey | 路径 | String | 是 | 配置键 |
  | configValue | 请求体 | String | 是 | 配置值 |
- **请求体示例**:
  ```json
  {
    "configValue": "新的网站名称"
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新成功",
    "data": {
      "id": 1,
      "configKey": "site_name",
      "configValue": "新的网站名称",
      "description": "网站名称",
      "updateTime": "2025-11-16T12:05:00"
    }
  }
  ```
- **功能**: 根据配置键更新配置值

### 删除系统配置
- **URL**: `DELETE /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 配置ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "删除成功",
    "data": null
  }
  ```
- **功能**: 删除系统配置

### 根据配置键删除配置
- **URL**: `DELETE /key/{configKey}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | configKey | 路径 | String | 是 | 配置键 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "删除成功",
    "data": null
  }
  ```
- **功能**: 根据配置键删除配置

### 根据ID获取系统配置
- **URL**: `GET /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 配置ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "id": 1,
      "configKey": "site_name",
      "configValue": "就业服务平台",
      "description": "网站名称",
      "createTime": "2025-11-16T12:00:00",
      "updateTime": "2025-11-16T12:00:00"
    }
  }
  ```
- **功能**: 根据ID获取系统配置

### 根据配置键获取系统配置
- **URL**: `GET /key/{configKey}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | configKey | 路径 | String | 是 | 配置键 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "id": 1,
      "configKey": "site_name",
      "configValue": "就业服务平台",
      "description": "网站名称",
      "createTime": "2025-11-16T12:00:00",
      "updateTime": "2025-11-16T12:00:00"
    }
  }
  ```
- **功能**: 根据配置键获取系统配置

### 获取所有系统配置
- **URL**: `GET /`
- **请求参数**: 无
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "configKey": "site_name",
        "configValue": "就业服务平台",
        "description": "网站名称",
        "createTime": "2025-11-16T12:00:00",
        "updateTime": "2025-11-16T12:00:00"
      },
      {
        "id": 2,
        "configKey": "site_description",
        "configValue": "专业的就业服务平台",
        "description": "网站描述",
        "createTime": "2025-11-16T12:00:00",
        "updateTime": "2025-11-16T12:00:00"
      }
    ]
  }
  ```
- **功能**: 获取所有系统配置

### 根据配置键列表获取系统配置
- **URL**: `POST /batch`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | configKeys | 请求体 | List<String> | 是 | 配置键列表 |
- **请求体示例**:
  ```json
  ["site_name", "site_description", "site_keywords"]
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "configKey": "site_name",
        "configValue": "就业服务平台",
        "description": "网站名称"
      },
      {
        "id": 2,
        "configKey": "site_description",
        "configValue": "专业的就业服务平台",
        "description": "网站描述"
      }
    ]
  }
  ```
- **功能**: 根据配置键列表获取系统配置

### 获取所有配置键值对
- **URL**: `GET /key-values`
- **请求参数**: 无
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "site_name": "就业服务平台",
      "site_description": "专业的就业服务平台",
      "site_keywords": "就业,招聘,求职,岗位"
    }
  }
  ```
- **功能**: 获取所有配置键值对

### 根据配置键前缀获取配置
- **URL**: `GET /prefix/{prefix}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | prefix | 路径 | String | 是 | 配置键前缀 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "configKey": "site_name",
        "configValue": "就业服务平台",
        "description": "网站名称"
      },
      {
        "id": 2,
        "configKey": "site_description",
        "configValue": "专业的就业服务平台",
        "description": "网站描述"
      }
    ]
  }
  ```
- **功能**: 根据配置键前缀获取配置

### 根据配置键后缀获取配置
- **URL**: `GET /suffix/{suffix}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | suffix | 路径 | String | 是 | 配置键后缀 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "configKey": "site_name",
        "configValue": "就业服务平台",
        "description": "网站名称"
      }
    ]
  }
  ```
- **功能**: 根据配置键后缀获取配置

### 根据配置键包含查询配置
- **URL**: `GET /search/{keyword}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | keyword | 路径 | String | 是 | 搜索关键词 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "configKey": "site_name",
        "configValue": "就业服务平台",
        "description": "网站名称"
      }
    ]
  }
  ```
- **功能**: 根据配置键包含查询配置

### 根据描述模糊查询配置
- **URL**: `GET /description/{description}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | description | 路径 | String | 是 | 描述关键词 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "configKey": "site_name",
        "configValue": "就业服务平台",
        "description": "网站名称"
      }
    ]
  }
  ```
- **功能**: 根据描述模糊查询配置

### 批量创建或更新配置
- **URL**: `POST /batch-create-update`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | configDTOs | 请求体 | List<SystemConfigDTO> | 是 | 配置对象列表 |
- **请求体示例**:
  ```json
  [
    {
      "configKey": "site_name",
      "configValue": "新的网站名称",
      "description": "网站名称"
    },
    {
      "configKey": "site_description",
      "configValue": "新的网站描述",
      "description": "网站描述"
    }
  ]
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "批量操作成功",
    "data": [
      {
        "id": 1,
        "configKey": "site_name",
        "configValue": "新的网站名称",
        "description": "网站名称"
      },
      {
        "id": 2,
        "configKey": "site_description",
        "configValue": "新的网站描述",
        "description": "网站描述"
      }
    ]
  }
  ```
- **功能**: 批量创建或更新配置

### 批量删除配置
- **URL**: `DELETE /batch`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | ids | 请求体 | List<Long> | 是 | 配置ID列表 |
- **请求体示例**:
  ```json
  [1, 2, 3]
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "批量删除成功",
    "data": null
  }
  ```
- **功能**: 批量删除配置

### 批量删除配置（根据配置键）
- **URL**: `DELETE /batch-keys`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | configKeys | 请求体 | List<String> | 是 | 配置键列表 |
- **请求体示例**:
  ```json
  ["site_name", "site_description"]
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "批量删除成功",
    "data": null
  }
  ```
- **功能**: 批量删除配置（根据配置键）

### 检查配置键是否存在
- **URL**: `GET /exists/{configKey}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | configKey | 路径 | String | 是 | 配置键 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "检查成功",
    "data": true
  }
  ```
- **功能**: 检查配置键是否存在

### 获取配置数量
- **URL**: `GET /count`
- **请求参数**: 无
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": 50
  }
  ```
- **功能**: 获取配置数量

### 获取配置分组统计
- **URL**: `GET /stats/group`
- **请求参数**: 无
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "site": 10,
      "security": 5,
      "email": 6,
      "sms": 5
    }
  }
  ```
- **功能**: 获取配置分组统计

### 获取配置值类型统计
- **URL**: `GET /stats/value-type`
- **请求参数**: 无
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "STRING": 25,
      "INTEGER": 10,
      "BOOLEAN": 8,
      "DECIMAL": 5,
      "JSON": 2
    }
  }
  ```
- **功能**: 获取配置值类型统计


### 获取最近更新的配置
- **URL**: `GET /recently-updated`
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
        "configKey": "site_name",
        "configValue": "就业服务平台",
        "description": "网站名称",
        "updateTime": "2025-11-16T12:05:00"
      }
    ]
  }
  ```
- **功能**: 获取最近更新的配置

### 获取最近创建的配置
- **URL**: `GET /recently-created`
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
        "configKey": "site_name",
        "configValue": "就业服务平台",
        "description": "网站名称",
        "createTime": "2025-11-16T12:00:00"
      }
    ]
  }
  ```
- **功能**: 获取最近创建的配置

### 初始化系统默认配置
- **URL**: `POST /init-default`
- **请求参数**: 无
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "初始化成功",
    "data": null
  }
  ```
- **功能**: 初始化系统默认配置

### 获取网站配置
- **URL**: `GET /site`
- **请求参数**: 无
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "site_name": "就业服务平台",
      "site_description": "专业的就业服务平台",
      "site_keywords": "就业,招聘,求职,岗位",
      "site_logo": "/images/logo.png",
      "site_favicon": "/images/favicon.ico",
      "site_copyright": "© 2025 就业服务平台",
      "site_icp": "京ICP备12345678号",
      "site_contact": "contact@example.com",
      "site_address": "北京市朝阳区"
    }
  }
  ```
- **功能**: 获取网站配置

### 获取安全配置
- **URL**: `GET /security`
- **请求参数**: 无
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "security_login_attempts": "5",
      "security_lockout_time": "30",
      "security_password_min_length": "8",
      "security_password_complexity": "medium"
    }
  }
  ```
- **功能**: 获取安全配置

### 获取邮件配置
- **URL**: `GET /email`
- **请求参数**: 无
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "email_enabled": "false",
      "email_host": "smtp.example.com",
      "email_port": "587",
      "email_username": "noreply@example.com",
      "email_password": "",
      "email_from": "noreply@example.com"
    }
  }
  ```
- **功能**: 获取邮件配置

### 获取短信配置
- **URL**: `GET /sms`
- **请求参数**: 无
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "sms_enabled": "false",
      "sms_provider": "aliyun",
      "sms_access_key": "",
      "sms_secret_key": "",
      "sms_sign_name": "就业平台"
    }
  }
  ```
- **功能**: 获取短信配置

### 获取文件配置
- **URL**: `GET /file`
- **请求参数**: 无
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "file_upload_max_size": "10485760",
      "file_allowed_types": "jpg,jpeg,png,gif,pdf,doc,docx",
      "file_storage_type": "local",
      "file_storage_path": "/uploads"
    }
  }
  ```
- **功能**: 获取文件配置

### 获取通知配置
- **URL**: `GET /notification`
- **请求参数**: 无
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "notification_enabled": "true",
      "notification_types": "email,sms,web",
      "notification_push_interval": "60"
    }
  }
  ```
- **功能**: 获取通知配置

### 获取分析配置
- **URL**: `GET /analytics`
- **请求参数**: 无
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "analytics_enabled": "false",
      "analytics_provider": "google",
      "analytics_tracking_id": ""
    }
  }
  ```
- **功能**: 获取分析配置

### 获取缓存配置
- **URL**: `GET /cache`
- **请求参数**: 无
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "cache_enabled": "true",
      "cache_ttl": "3600",
      "cache_max_size": "1000"
    }
  }
  ```
- **功能**: 获取缓存配置

### 获取备份配置
- **URL**: `GET /backup`
- **请求参数**: 无
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "backup_enabled": "false",
      "backup_interval": "86400",
      "backup_retention": "30"
    }
  }
  ```
- **功能**: 获取备份配置

### 获取业务配置
- **URL**: `GET /business`
- **请求参数**: 无
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "resume_privacy_default": "1",
      "job_expire_days": "30",
      "match_threshold": "0.7",
      "max_resume_count": "3",
      "max_job_apply_count": "10"
    }
  }
  ```
- **功能**: 获取业务配置


## 统一查询接口

### 统一查询系统配置（不分页）
- **URL**: `POST /query`
- **请求参数** (SystemConfigQueryDTO对象):
  | 字段名 | 类型 | 必填 | 说明 | 默认值 |
  |--------|------|------|------|--------|
  | configKey | String | 否 | 配置键（精确匹配） | - |
  | configKeyPrefix | String | 否 | 配置键前缀 | - |
  | configKeySuffix | String | 否 | 配置键后缀 | - |
  | configKeyKeyword | String | 否 | 配置键包含的关键词 | - |
  | configValueKeyword | String | 否 | 配置值包含的关键词 | - |
  | descriptionKeyword | String | 否 | 描述包含的关键词 | - |
  | configGroup | String | 否 | 配置分组 | - |
  | valueType | String | 否 | 配置值类型 | - |
  | editable | Boolean | 否 | 是否可编辑 | - |
  | systemConfig | Boolean | 否 | 是否为系统配置 | - |
  | createTimeStart | String | 否 | 创建时间范围-开始 | - |
  | createTimeEnd | String | 否 | 创建时间范围-结束 | - |
  | updateTimeStart | String | 否 | 更新时间范围-开始 | - |
  | updateTimeEnd | String | 否 | 更新时间范围-结束 | - |
  | sortField | String | 否 | 排序字段 | id |
  | sortDirection | String | 否 | 排序方向 | asc |
  | enablePagination | Boolean | 否 | 是否启用分页 | false |
- **请求体示例**:
  ```json
  {
    "configKeyPrefix": "site_",
    "sortField": "updateTime",
    "sortDirection": "desc",
    "enablePagination": false
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": [
      {
        "id": 1,
        "configKey": "site_name",
        "configValue": "就业服务平台",
        "description": "网站名称",
        "configGroup": "site",
        "valueType": "STRING",
        "editable": true,
        "systemConfig": false,
        "sortOrder": 0,
        "validationRule": "",
        "defaultValue": "",
        "options": "",
        "createTime": "2025-11-16T12:00:00",
        "updateTime": "2025-11-16T12:05:00"
      },
      {
        "id": 2,
        "configKey": "site_description",
        "configValue": "专业的就业服务平台",
        "description": "网站描述",
        "configGroup": "site",
        "valueType": "STRING",
        "editable": true,
        "systemConfig": false,
        "sortOrder": 0,
        "validationRule": "",
        "defaultValue": "",
        "options": "",
        "createTime": "2025-11-16T12:00:00",
        "updateTime": "2025-11-16T12:00:00"
      }
    ]
  }
  ```
- **功能**: 统一查询系统配置（不分页）

### 统一查询系统配置（分页）
- **URL**: `POST /query/page`
- **请求参数** (SystemConfigQueryDTO对象):
  | 字段名 | 类型 | 必填 | 说明 | 默认值 |
  |--------|------|------|------|--------|
  | configKey | String | 否 | 配置键（精确匹配） | - |
  | configKeyPrefix | String | 否 | 配置键前缀 | - |
  | configKeySuffix | String | 否 | 配置键后缀 | - |
  | configKeyKeyword | String | 否 | 配置键包含的关键词 | - |
  | configValueKeyword | String | 否 | 配置值包含的关键词 | - |
  | descriptionKeyword | String | 否 | 描述包含的关键词 | - |
  | configGroup | String | 否 | 配置分组 | - |
  | valueType | String | 否 | 配置值类型 | - |
  | editable | Boolean | 否 | 是否可编辑 | - |
  | systemConfig | Boolean | 否 | 是否为系统配置 | - |
  | createTimeStart | String | 否 | 创建时间范围-开始 | - |
  | createTimeEnd | String | 否 | 创建时间范围-结束 | - |
  | updateTimeStart | String | 否 | 更新时间范围-开始 | - |
  | updateTimeEnd | String | 否 | 更新时间范围-结束 | - |
  | sortField | String | 否 | 排序字段 | id |
  | sortDirection | String | 否 | 排序方向 | asc |
  | page | Integer | 否 | 页码 | 0 |
  | size | Integer | 否 | 每页大小 | 20 |
  | enablePagination | Boolean | 否 | 是否启用分页 | true |
- **请求体示例**:
  ```json
  {
    "configKeyPrefix": "site_",
    "sortField": "updateTime",
    "sortDirection": "desc",
    "page": 0,
    "size": 10,
    "enablePagination": true
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": {
      "content": [
        {
          "id": 1,
          "configKey": "site_name",
          "configValue": "就业服务平台",
          "description": "网站名称",
          "configGroup": "site",
          "valueType": "STRING",
          "editable": true,
          "systemConfig": false,
          "sortOrder": 0,
          "validationRule": "",
          "defaultValue": "",
          "options": "",
          "createTime": "2025-11-16T12:00:00",
          "updateTime": "2025-11-16T12:05:00"
        }
      ],
      "totalElements": 1,
      "totalPages": 1,
      "size": 10,
      "number": 0,
      "first": true,
      "last": true,
      "numberOfElements": 1
    }
  }
  ```
- **功能**: 统一查询系统配置（分页）


## 错误响应格式

所有接口在发生错误时都会返回统一的错误响应格式：

```json
{
  "code": 400,
  "message": "错误描述信息",
  "data": null
}
```

常见的错误码：
- `200`: 成功
- `400`: 请求参数错误
- `404`: 资源不存在
- `500`: 服务器内部错误

## 使用示例

### 初始化默认配置
```bash
curl -X POST "http://localhost:8080/api/system-configs/init-default"
```

### 获取网站配置
```bash
curl -X GET "http://localhost:8080/api/system-configs/site"
```

### 更新配置值
```bash
curl -X PUT "http://localhost:8080/api/system-configs/key/site_name" \
  -H "Content-Type: application/json" \
  -d '{"configValue": "新的网站名称"}'
```


### 获取字符串配置值
```bash
curl -X GET "http://localhost:8080/api/system-configs/value/string/site_name"
```

### 获取整数配置值
```bash
curl -X GET "http://localhost:8080/api/system-configs/value/integer/security_login_attempts"
```

### 获取布尔配置值
```bash
curl -X GET "http://localhost:8080/api/system-configs/value/boolean/email_enabled"
```

## 初始化配置分组信息

系统初始化时会自动创建以下配置分组，每个分组包含完整的配置项信息：

### 1. 网站配置 (site)
- **配置项数量**: 9项
- **配置键前缀**: `site_`
- **包含配置**:
  - `site_name`: 网站名称 (STRING)
  - `site_description`: 网站描述 (STRING)
  - `site_keywords`: 网站关键词 (STRING)
  - `site_logo`: 网站Logo (STRING)
  - `site_favicon`: 网站图标 (STRING)
  - `site_copyright`: 版权信息 (STRING)
  - `site_icp`: ICP备案号 (STRING)
  - `site_contact`: 联系邮箱 (STRING)
  - `site_address`: 联系地址 (STRING)

### 2. 安全配置 (security)
- **配置项数量**: 4项
- **配置键前缀**: `security_`
- **包含配置**:
  - `security_login_attempts`: 最大登录尝试次数 (INTEGER, 验证规则: min:1|max:10)
  - `security_lockout_time`: 账户锁定时间（分钟） (INTEGER, 验证规则: min:1|max:1440)
  - `security_password_min_length`: 密码最小长度 (INTEGER, 验证规则: min:6|max:20)
  - `security_password_complexity`: 密码复杂度要求 (STRING, 选项: ["low","medium","high"])

### 3. 邮件配置 (email)
- **配置项数量**: 6项
- **配置键前缀**: `email_`
- **包含配置**:
  - `email_enabled`: 邮件服务是否启用 (BOOLEAN)
  - `email_host`: 邮件服务器地址 (STRING)
  - `email_port`: 邮件服务器端口 (INTEGER, 验证规则: min:1|max:65535)
  - `email_username`: 邮件用户名 (STRING)
  - `email_password`: 邮件密码 (STRING)
  - `email_from`: 发件人邮箱 (STRING)

### 4. 短信配置 (sms)
- **配置项数量**: 5项
- **配置键前缀**: `sms_`
- **包含配置**:
  - `sms_enabled`: 短信服务是否启用 (BOOLEAN)
  - `sms_provider`: 短信服务提供商 (STRING, 选项: ["aliyun","tencent","huawei"])
  - `sms_access_key`: 短信Access Key (STRING)
  - `sms_secret_key`: 短信Secret Key (STRING)
  - `sms_sign_name`: 短信签名 (STRING)

### 5. 文件配置 (file)
- **配置项数量**: 4项
- **配置键前缀**: `file_`
- **包含配置**:
  - `file_upload_max_size`: 文件上传最大大小（字节） (INTEGER, 验证规则: min:1024|max:104857600)
  - `file_allowed_types`: 允许的文件类型 (STRING)
  - `file_storage_type`: 文件存储类型 (STRING, 选项: ["local","oss","cos"])
  - `file_storage_path`: 文件存储路径 (STRING)

### 6. 通知配置 (notification)
- **配置项数量**: 3项
- **配置键前缀**: `notification_`
- **包含配置**:
  - `notification_enabled`: 通知服务是否启用 (BOOLEAN)
  - `notification_types`: 通知类型 (STRING, 选项: ["email","sms","web"])
  - `notification_push_interval`: 通知推送间隔（秒） (INTEGER, 验证规则: min:1|max:3600)

### 7. 分析配置 (analytics)
- **配置项数量**: 3项
- **配置键前缀**: `analytics_`
- **包含配置**:
  - `analytics_enabled`: 分析服务是否启用 (BOOLEAN)
  - `analytics_provider`: 分析服务提供商 (STRING, 选项: ["google","baidu","cnzz"])
  - `analytics_tracking_id`: 分析跟踪ID (STRING)

### 8. 缓存配置 (cache)
- **配置项数量**: 3项
- **配置键前缀**: `cache_`
- **包含配置**:
  - `cache_enabled`: 缓存是否启用 (BOOLEAN)
  - `cache_ttl`: 缓存生存时间（秒） (INTEGER, 验证规则: min:1|max:86400)
  - `cache_max_size`: 缓存最大大小 (INTEGER, 验证规则: min:1|max:10000)

### 9. 备份配置 (backup)
- **配置项数量**: 3项
- **配置键前缀**: `backup_`
- **包含配置**:
  - `backup_enabled`: 备份是否启用 (BOOLEAN)
  - `backup_interval`: 备份间隔（秒） (INTEGER, 验证规则: min:3600|max:604800)
  - `backup_retention`: 备份保留天数 (INTEGER, 验证规则: min:1|max:365)

### 10. 业务配置 (business)
- **配置项数量**: 5项
- **配置键前缀**: 无特定前缀
- **包含配置**:
  - `resume_privacy_default`: 简历默认隐私级别 (INTEGER, 验证规则: min:0|max:2, 选项: ["0:公开","1:仅企业可见","2:仅自己可见"])
  - `job_expire_days`: 岗位默认过期天数 (INTEGER, 验证规则: min:1|max:365)
  - `match_threshold`: 简历岗位匹配阈值 (DOUBLE, 验证规则: min:0.1|max:1.0)
  - `max_resume_count`: 用户最大简历数量 (INTEGER, 验证规则: min:1|max:10)
  - `max_job_apply_count`: 用户最大申请数量 (INTEGER, 验证规则: min:1|max:50)

## 配置字段说明

每个配置项都包含以下完整字段信息：

| 字段名 | 类型 | 说明 | 示例 |
|--------|------|------|------|
| configKey | String | 配置键，唯一标识符 | `site_name` |
| configValue | String | 配置值 | `就业服务平台` |
| description | String | 配置描述 | `网站名称` |
| configGroup | String | 配置分组 | `site` |
| valueType | String | 配置值类型 | `STRING`, `INTEGER`, `BOOLEAN`, `DOUBLE` |
| editable | Boolean | 是否可编辑 | `true` |
| systemConfig | Boolean | 是否为系统配置 | `false` |
| sortOrder | Integer | 排序顺序 | `1` |
| validationRule | String | 验证规则 | `min:1|max:10` |
| defaultValue | String | 默认值 | `就业服务平台` |
| options | String | 选项值（JSON格式） | `["low","medium","high"]` |

## 注意事项

1. 配置键必须是唯一的，不能重复
2. 系统配置（systemConfig=true）通常不允许删除
3. 批量操作接口支持最多100个配置项
4. 配置值支持多种数据类型，系统会自动进行类型转换
5. 所有查询接口都支持无分页查询，返回所有符合条件的配置项
6. 初始化配置接口会检查配置是否已存在，避免重复创建
7. 每个配置分组都有特定的配置键前缀，便于分类管理
8. 验证规则支持范围验证，格式为 `min:值|max:值`
9. 选项值支持JSON数组格式，用于下拉选择等场景
