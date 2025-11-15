# 岗位分类模块 (JobCategoryController)

**基础路径**: `/api/job-categories`

## 接口列表

### 创建岗位分类
- **URL**: `POST /`
- **请求参数**:
  ```json
  {
    "name": "分类名称",
    "description": "分类描述",
    "parentId": 0, // 父分类ID，0表示根分类
    "level": 1, // 分类层级
    "sortOrder": 1, // 排序序号
    "status": 1, // 1-启用，0-禁用
    "icon": "icon-name", // 分类图标
    "color": "#007bff" // 分类颜色
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "创建成功",
    "data": {
      "id": 1,
      "name": "分类名称",
      "parentId": 0,
      "sortOrder": 1,
      "status": 1
    }
  }
  ```
- **功能**: 创建岗位分类

### 更新岗位分类
- **URL**: `PUT /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 分类ID |
  | JobCategoryDTO | 请求体 | Object | 是 | 分类信息对象 |
  
  **JobCategoryDTO 请求体结构**:
  ```json
  {
    "name": "分类名称",
    "description": "分类描述",
    "parentId": 0, // 父分类ID，0表示根分类
    "level": 1, // 分类层级
    "sortOrder": 1, // 排序序号
    "status": 1, // 1-启用，0-禁用
    "icon": "icon-name", // 分类图标
    "color": "#007bff" // 分类颜色
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新成功",
    "data": {
      "id": 1,
      "name": "分类名称",
      "description": "分类描述",
      "parentId": 0,
      "sortOrder": 1,
      "status": 1,
      "updateTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 更新岗位分类

### 删除岗位分类
- **URL**: `DELETE /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 分类ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "删除成功",
    "data": null
  }
  ```
- **功能**: 删除岗位分类

### 获取分类详情
- **URL**: `GET /{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 分类ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "id": 1,
      "name": "分类名称",
      "description": "分类描述",
      "parentId": 0,
      "sortOrder": 1,
      "status": 1,
      "createTime": "2025-11-08T12:00:00",
      "updateTime": "2025-11-08T12:00:00",
      "jobCount": 50,
      "childrenCount": 3
    }
  }
  ```
- **功能**: 获取分类详情

### 查询分类列表
- **URL**: `GET /`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | sort | 查询 | String | 否 | 排序字段，如：id，默认id |
  | direction | 查询 | String | 否 | 排序方向：ASC/DESC，默认desc |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": [
      {
        "id": 1,
        "name": "分类名称",
        "description": "分类描述",
        "parentId": 0,
        "sortOrder": 1,
        "status": 1,
        "createTime": "2025-11-08T12:00:00",
        "jobCount": 50
      }
    ]
  }
  ```
- **功能**: 查询所有分类列表（支持排序）

### 获取所有分类列表
- **URL**: `GET /all`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "name": "分类名称",
        "description": "分类描述",
        "parentId": 0,
        "sortOrder": 1,
        "status": 1,
        "jobCount": 50
      }
    ]
  }
  ```
- **功能**: 获取所有分类列表

### 根据父分类ID查询子分类
- **URL**: `GET /parent/{parentId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | parentId | 路径 | Long | 是 | 父分类ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": [
      {
        "id": 2,
        "name": "子分类名称",
        "description": "子分类描述",
        "parentId": 1,
        "sortOrder": 1,
        "status": 1,
        "jobCount": 20
      }
    ]
  }
  ```
- **功能**: 根据父分类ID查询子分类

### 获取顶级分类列表
- **URL**: `GET /top-level`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "name": "顶级分类名称",
        "description": "顶级分类描述",
        "parentId": 0,
        "sortOrder": 1,
        "status": 1,
        "jobCount": 50
      }
    ]
  }
  ```
- **功能**: 获取所有顶级分类（parentId为0的分类）

### 根据状态查询分类列表
- **URL**: `GET /status/{status}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | status | 路径 | Integer | 是 | 分类状态：1-启用，0-禁用 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": [
      {
        "id": 1,
        "name": "分类名称",
        "description": "分类描述",
        "parentId": 0,
        "sortOrder": 1,
        "status": 1,
        "jobCount": 50
      }
    ]
  }
  ```
- **功能**: 根据状态查询分类

### 根据分类名称搜索
- **URL**: `GET /search`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | name | 查询 | String | 是 | 分类名称关键词 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "搜索成功",
    "data": [
      {
        "id": 1,
        "name": "分类名称",
        "description": "分类描述",
        "parentId": 0,
        "sortOrder": 1,
        "status": 1,
        "jobCount": 50
      }
    ]
  }
  ```
- **功能**: 根据分类名称搜索

### 更新分类状态
- **URL**: `PUT /{id}/status`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 分类ID |
  | status | 查询 | Integer | 是 | 分类状态：1-启用，0-禁用 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新状态成功",
    "data": {
      "id": 1,
      "name": "分类名称",
      "status": 1,
      "updateTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 更新分类状态

### 更新排序序号
- **URL**: `PUT /{id}/sort-order`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 分类ID |
  | sortOrder | 查询 | Integer | 是 | 排序序号 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新排序成功",
    "data": {
      "id": 1,
      "name": "分类名称",
      "sortOrder": 2,
      "updateTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 更新分类排序序号

### 获取分类树形结构
- **URL**: `GET /tree`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "name": "根分类",
        "description": "根分类描述",
        "parentId": 0,
        "sortOrder": 1,
        "status": 1,
        "jobCount": 50,
        "children": [
          {
            "id": 2,
            "name": "子分类",
            "description": "子分类描述",
            "parentId": 1,
            "sortOrder": 1,
            "status": 1,
            "jobCount": 20
          }
        ]
      }
    ]
  }
  ```
- **功能**: 获取完整的分类树结构

### 获取分类路径
- **URL**: `GET /{id}/path`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 分类ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "name": "根分类"
      },
      {
        "id": 2,
        "name": "子分类"
      },
      {
        "id": 3,
        "name": "当前分类"
      }
    ]
  }
  ```
- **功能**: 获取分类的完整路径

### 统计分类下的岗位数量
- **URL**: `GET /stats/job-count`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "统计成功",
    "data": {
      "1": 50,
      "2": 30,
      "3": 20
    }
  }
  ```
- **功能**: 统计每个分类下的岗位数量

### 获取热门分类
- **URL**: `GET /hot`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      ["分类ID", "分类名称", "岗位数量"]
    ]
  }
  ```
- **功能**: 获取热门分类

### 检查分类名称是否已存在
- **URL**: `GET /check-name`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | name | 查询 | String | 是 | 分类名称 |
  | excludeId | 查询 | Long | 否 | 分类ID（排除自身） |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "验证成功",
    "data": {
      "exists": false,
      "valid": true,
      "message": "分类名称可用"
    }
  }
  ```
- **功能**: 验证分类名称是否重复

### 获取启用的分类列表
- **URL**: `GET /enabled`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "name": "分类名称",
        "description": "分类描述",
        "parentId": 0,
        "sortOrder": 1,
        "status": 1,
        "jobCount": 50
      }
    ]
  }
  ```
- **功能**: 获取所有启用状态的分类

### 根据层级查询分类
- **URL**: `GET /level/{level}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | level | 路径 | Integer | 是 | 分类层级 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": [
      {
        "id": 1,
        "name": "分类名称",
        "description": "分类描述",
        "parentId": 0,
        "level": 1,
        "sortOrder": 1,
        "status": 1,
        "jobCount": 50
      }
    ]
  }
  ```
- **功能**: 根据层级查询分类

### 批量更新分类状态
- **URL**: `PUT /batch-status`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | ids | 查询 | List<Long> | 是 | 分类ID列表 |
  | status | 查询 | Integer | 是 | 分类状态：1-启用，0-禁用 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "批量更新状态成功",
    "data": [
      {
        "id": 1,
        "name": "分类名称",
        "status": 1,
        "updateTime": "2025-11-08T12:00:00"
      }
    ]
  }
  ```
- **功能**: 批量更新多个分类的状态

### 获取分类统计信息
- **URL**: `GET /stats`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "统计成功",
    "data": {
      "totalCategories": 50,
      "enabledCategories": 40,
      "disabledCategories": 10,
      "rootCategories": 5,
      "totalJobs": 1000,
      "averageJobsPerCategory": 20
    }
  }
  ```
- **功能**: 获取分类统计信息

## 使用示例

### 创建分类示例
```bash
curl -X POST "http://localhost:8080/api/job-categories" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "name": "技术开发",
    "description": "技术开发相关岗位",
    "parentId": 0,
    "sortOrder": 1,
    "status": 1
  }'
```

### 获取分类树示例
```bash
curl -X GET "http://localhost:8080/api/job-categories/tree" \
  -H "Authorization: Bearer {token}"
```

### 获取子分类示例
```bash
curl -X GET "http://localhost:8080/api/job-categories/parent/1" \
  -H "Authorization: Bearer {token}"
```

### 更新状态示例
```bash
curl -X PUT "http://localhost:8080/api/job-categories/1/status?status=0" \
  -H "Authorization: Bearer {token}"
```

### 更新排序示例
```bash
curl -X PUT "http://localhost:8080/api/job-categories/1/sort-order?sortOrder=2" \
  -H "Authorization: Bearer {token}"
```

### 批量更新状态示例
```bash
curl -X PUT "http://localhost:8080/api/job-categories/batch-status?ids=1,2,3&status=1" \
  -H "Authorization: Bearer {token}"
```

### 验证分类名称示例
```bash
curl -X GET "http://localhost:8080/api/job-categories/check-name?name=技术开发" \
  -H "Authorization: Bearer {token}"
```

## 注意事项

- 所有岗位分类接口都需要有效的JWT Token进行认证
- 分类状态：1-启用，0-禁用
- 父分类ID为0表示根分类
- 分类支持树形结构，支持无限层级
- 所有列表查询接口均返回完整数据，不进行分页
- 分类名称在同一层级下不能重复
- 删除分类时会检查是否有子分类或关联岗位
- 分类排序支持批量操作，提高效率
- 所有接口都使用统一的响应格式：包含code、message、data字段

[返回主文档](../docs/README.md)
