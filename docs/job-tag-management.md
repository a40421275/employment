# 岗位标签管理模块 (JobTagController)

**基础路径**: `/api/job-tags`

## 接口列表

### 新建标签
- **URL**: `POST /`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | tagName | 查询 | String | 是 | 标签名称 |
  | jobId | 查询 | Long | 否 | 岗位ID（可选） |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "新建成功",
    "data": {
      "id": 1,
      "jobId": 1,
      "tagName": "高薪",
      "tagType": 0,
      "createTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 新建标签，岗位ID为可选参数

### 创建完整标签信息
- **URL**: `POST /create`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | JobTagDTO | 请求体 | Object | 是 | 标签信息对象 |

  **JobTagDTO 请求体结构**:
  ```json
  {
    "tagName": "标签名称",
    "jobId": 1,
    "tagType": 0,
    "tagColor": "#ff6b6b",
    "description": "标签描述"
  }
  ```
  
  **字段说明**:
  | 字段名 | 数据类型 | 必填 | 说明 |
  |--------|----------|------|------|
  | tagName | String | 是 | 标签名称 |
  | jobId | Long | 否 | 岗位ID（可选，如果提供会关联到具体岗位） |
  | tagType | Integer | 否 | 标签类型：0-系统标签，1-自定义标签（默认1） |
  | tagColor | String | 否 | 标签颜色（十六进制颜色代码） |
  | description | String | 否 | 标签描述 |

- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "创建成功",
    "data": {
      "id": 1,
      "jobId": 1,
      "tagName": "高薪",
      "tagType": 0,
      "tagColor": "#ff6b6b",
      "description": "薪资待遇优厚",
      "createTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 创建完整标签信息，支持设置标签名称、颜色、类型、描述等完整信息

### 为岗位添加标签
- **URL**: `POST /job/{jobId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 路径 | Long | 是 | 岗位ID |
  | tagName | 查询 | String | 是 | 标签名称 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "添加成功",
    "data": {
      "id": 1,
      "jobId": 1,
      "tagName": "高薪",
      "tagType": 0,
      "createTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 为指定岗位添加标签

### 为岗位批量添加标签
- **URL**: `POST /job/{jobId}/batch`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 路径 | Long | 是 | 岗位ID |
  | tagNames | 请求体 | List<String> | 是 | 标签名称列表 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "批量添加成功",
    "data": [
      {
        "id": 1,
        "jobId": 1,
        "tagName": "高薪",
        "tagType": 0,
        "createTime": "2025-11-08T12:00:00"
      },
      {
        "id": 2,
        "jobId": 1,
        "tagName": "远程办公",
        "tagType": 0,
        "createTime": "2025-11-08T12:00:00"
      }
    ]
  }
  ```
- **功能**: 为指定岗位批量添加标签

### 从岗位移除标签
- **URL**: `DELETE /job/{jobId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 路径 | Long | 是 | 岗位ID |
  | tagName | 查询 | String | 是 | 标签名称 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "移除成功",
    "data": null
  }
  ```
- **功能**: 从指定岗位移除标签

### 从岗位批量移除标签
- **URL**: `DELETE /job/{jobId}/batch`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 路径 | Long | 是 | 岗位ID |
  | tagNames | 请求体 | List<String> | 是 | 标签名称列表 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "批量移除成功",
    "data": null
  }
  ```
- **功能**: 从指定岗位批量移除标签

### 获取岗位的所有标签
- **URL**: `GET /job/{jobId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 路径 | Long | 是 | 岗位ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "jobId": 1,
        "tagName": "高薪",
        "tagType": 0,
        "tagColor": "#ff6b6b",
        "description": "薪资待遇优厚",
        "createTime": "2025-11-08T12:00:00"
      },
      {
        "id": 2,
        "jobId": 1,
        "tagName": "远程办公",
        "tagType": 0,
        "tagColor": "#4ecdc4",
        "description": "支持远程工作",
        "createTime": "2025-11-08T12:00:00"
      }
    ]
  }
  ```
- **功能**: 获取指定岗位的所有标签

### 获取岗位的标签名称列表
- **URL**: `GET /job/{jobId}/names`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 路径 | Long | 是 | 岗位ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": ["高薪", "远程办公", "技术驱动"]
  }
  ```
- **功能**: 获取指定岗位的标签名称列表

### 根据标签名称获取岗位列表
- **URL**: `GET /tag/{tagName}/jobs`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | tagName | 路径 | String | 是 | 标签名称 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [1, 2, 3, 5, 8]
  }
  ```
- **功能**: 根据标签名称获取关联的岗位ID列表

### 根据多个标签名称获取岗位列表
- **URL**: `GET /tags/jobs`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | tagNames | 查询 | List<String> | 是 | 标签名称列表 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [1, 3, 7]
  }
  ```
- **功能**: 根据多个标签名称获取关联的岗位ID列表

### 根据标签名称搜索岗位
- **URL**: `GET /search/jobs`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | tagName | 查询 | String | 是 | 标签名称（支持模糊搜索） |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "搜索成功",
    "data": [1, 2, 4, 6]
  }
  ```
- **功能**: 根据标签名称模糊搜索关联的岗位ID列表

### 获取热门标签
- **URL**: `GET /hot`
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
      ["高薪", 50],
      ["远程办公", 45],
      ["技术驱动", 40],
      ["五险一金", 38],
      ["弹性工作", 35]
    ]
  }
  ```
- **功能**: 获取热门标签及其使用次数

### 统计标签使用次数
- **URL**: `GET /stats/usage`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "统计成功",
    "data": {
      "高薪": 50,
      "远程办公": 45,
      "技术驱动": 40,
      "五险一金": 38,
      "弹性工作": 35,
      "年终奖": 30
    }
  }
  ```
- **功能**: 统计所有标签的使用次数

### 获取所有标签
- **URL**: `GET /all`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": ["高薪", "远程办公", "技术驱动", "五险一金", "弹性工作", "年终奖"]
  }
  ```
- **功能**: 获取系统中所有唯一的标签名称

### 获取所有标签（支持分页和筛选）
- **URL**: `GET /all/paged`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | tagName | 查询 | String | 否 | 标签名称（支持模糊搜索） |
  | tagType | 查询 | Integer | 否 | 标签类型：0-系统标签，1-自定义标签 |
  | jobId | 查询 | Long | 否 | 岗位ID（用于查询特定岗位的标签） |
  | sortBy | 查询 | String | 否 | 排序字段：tagName, createTime, usageCount（默认tagName） |
  | sortDirection | 查询 | String | 否 | 排序方向：asc, desc（默认asc） |
  | page | 查询 | Integer | 否 | 页码（默认0） |
  | size | 查询 | Integer | 否 | 每页大小（默认20） |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "content": [
        {
          "id": 1,
          "tagName": "高薪",
          "tagColor": "#ff6b6b",
          "tagType": 0,
          "description": "薪资待遇优厚",
          "sortOrder": 0,
          "status": 0,
          "usageCount": 50,
          "createTime": "2025-11-08T12:00:00",
          "updateTime": "2025-11-08T12:00:00",
          "jobId": null,
          "independent": true,
          "systemTag": true
        },
        {
          "id": 2,
          "tagName": "远程办公",
          "tagColor": "#4ecdc4",
          "tagType": 0,
          "description": "支持远程工作",
          "sortOrder": 0,
          "status": 0,
          "usageCount": 45,
          "createTime": "2025-11-08T12:00:00",
          "updateTime": "2025-11-08T12:00:00",
          "jobId": 1,
          "independent": false,
          "systemTag": true
        }
      ],
      "totalElements": 150,
      "totalPages": 15,
      "currentPage": 0,
      "pageSize": 10
    }
  }
  ```
- **功能**: 获取系统中所有标签，支持分页和多种筛选条件，返回完整的标签信息

### 获取系统常用标签
- **URL**: `GET /system`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      "急招",
      "高薪",
      "弹性工作",
      "远程办公",
      "实习",
      "兼职",
      "全职",
      "创业公司",
      "大厂",
      "技术驱动",
      "成长空间大",
      "福利待遇好",
      "培训体系完善",
      "团队氛围好",
      "领导好",
      "加班少",
      "年终奖",
      "股票期权",
      "五险一金",
      "餐补",
      "交通补贴",
      "住房补贴"
    ]
  }
  ```
- **功能**: 获取系统预定义的常用标签

### 获取系统常用标签（支持分页）
- **URL**: `GET /system/paged`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | tagName | 查询 | String | 否 | 标签名称（支持模糊搜索） |
  | sortBy | 查询 | String | 否 | 排序字段：tagName（默认tagName） |
  | sortDirection | 查询 | String | 否 | 排序方向：asc, desc（默认asc） |
  | page | 查询 | Integer | 否 | 页码（默认0） |
  | size | 查询 | Integer | 否 | 每页大小（默认20） |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "content": [
        {
          "tagName": "急招",
          "tagColor": "#4ecdc4",
          "tagType": 0,
          "sortOrder": 0,
          "status": 0,
          "usageCount": 25,
          "independent": true,
          "systemTag": true
        },
        {
          "tagName": "高薪",
          "tagColor": "#4ecdc4",
          "tagType": 0,
          "sortOrder": 0,
          "status": 0,
          "usageCount": 50,
          "independent": true,
          "systemTag": true
        },
        {
          "tagName": "弹性工作",
          "tagColor": "#4ecdc4",
          "tagType": 0,
          "sortOrder": 0,
          "status": 0,
          "usageCount": 35,
          "independent": true,
          "systemTag": true
        }
      ],
      "totalElements": 22,
      "totalPages": 3,
      "currentPage": 0,
      "pageSize": 10
    }
  }
  ```
- **功能**: 获取系统预定义的常用标签，支持分页和名称筛选，返回完整的标签信息

### 统计符合条件的标签数量
- **URL**: `GET /count`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | tagName | 查询 | String | 否 | 标签名称（支持模糊搜索） |
  | tagType | 查询 | Integer | 否 | 标签类型：0-系统标签，1-自定义标签 |
  | jobId | 查询 | Long | 否 | 岗位ID（用于查询特定岗位的标签） |
  | independentOnly | 查询 | Boolean | 否 | 是否只查询独立标签（jobId为null的标签） |
  | associatedOnly | 查询 | Boolean | 否 | 是否只查询关联标签（jobId不为null的标签） |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "统计成功",
    "data": 150
  }
  ```
- **功能**: 统计符合条件的标签数量

### 检查标签是否已存在
- **URL**: `GET /check`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 查询 | Long | 是 | 岗位ID |
  | tagName | 查询 | String | 是 | 标签名称 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "检查成功",
    "data": true
  }
  ```
- **功能**: 检查指定岗位是否已存在指定标签

### 更新标签
- **URL**: `PUT /{tagId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | tagId | 路径 | Long | 是 | 标签ID |
  | JobTagDTO | 请求体 | Object | 是 | 标签信息对象 |

  **JobTagDTO 请求体结构**:
  ```json
  {
    "tagName": "更新后的标签名称",
    "jobId": 1,
    "tagType": 0,
    "tagColor": "#ff6b6b",
    "description": "标签描述"
  }
  ```
  
  **字段说明**:
  | 字段名 | 数据类型 | 必填 | 说明 |
  |--------|----------|------|------|
  | tagName | String | 是 | 标签名称 |
  | jobId | Long | 否 | 岗位ID（可选，如果提供会更新关联的岗位） |
  | tagType | Integer | 否 | 标签类型：0-系统标签，1-自定义标签 |
  | tagColor | String | 否 | 标签颜色 |
  | description | String | 否 | 标签描述 |

- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新成功",
    "data": {
      "id": 1,
      "jobId": 1,
      "tagName": "更新后的标签",
      "tagType": 0,
      "tagColor": "#ff6b6b",
      "description": "更新后的描述",
      "createTime": "2025-11-08T12:00:00"
    }
  }
  ```
- **功能**: 更新标签信息，支持更新标签名称、关联岗位、标签类型、颜色和描述

### 删除标签
- **URL**: `DELETE /{tagId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | tagId | 路径 | Long | 是 | 标签ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "删除成功",
    "data": null
  }
  ```
- **功能**: 删除指定标签

### 批量删除标签
- **URL**: `DELETE /batch`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | tagIds | 请求体 | List<Long> | 是 | 标签ID列表 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "批量删除成功",
    "data": null
  }
  ```
- **功能**: 批量删除标签

### 获取标签统计信息
- **URL**: `GET /stats`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "totalTags": 150,
      "hotTags": [
        ["高薪", 50],
        ["远程办公", 45],
        ["技术驱动", 40]
      ],
      "tagUsage": {
        "高薪": 50,
        "远程办公": 45,
        "技术驱动": 40
      },
      "systemTagCount": 22,
      "customTagCount": 128
    }
  }
  ```
- **功能**: 获取标签统计信息

### 根据标签类型获取标签
- **URL**: `GET /type/{tagType}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | tagType | 路径 | Integer | 是 | 标签类型：0-系统标签，1-自定义标签 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "jobId": 1,
        "tagName": "高薪",
        "tagType": 0,
        "tagColor": "#ff6b6b",
        "description": "薪资待遇优厚",
        "createTime": "2025-11-08T12:00:00"
      }
    ]
  }
  ```
- **功能**: 根据标签类型获取标签列表

## 使用示例

### 新建标签示例
```bash
# 新建标签（不带岗位ID）
curl -X POST "http://localhost:8080/api/job-tags/?tagName=新标签" \
  -H "Authorization: Bearer {token}"

# 新建标签（带岗位ID）
curl -X POST "http://localhost:8080/api/job-tags/?tagName=新标签&jobId=1" \
  -H "Authorization: Bearer {token}"

# 创建完整标签信息
curl -X POST "http://localhost:8080/api/job-tags/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "tagName": "高薪",
    "jobId": 1,
    "tagType": 0,
    "tagColor": "#ff6b6b",
    "description": "薪资待遇优厚"
  }'

# 创建独立标签（不关联岗位）
curl -X POST "http://localhost:8080/api/job-tags/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "tagName": "热门标签",
    "tagType": 1,
    "tagColor": "#4ecdc4",
    "description": "热门岗位标签"
  }'
```

### 为岗位添加标签示例
```bash
curl -X POST "http://localhost:8080/api/job-tags/job/1?tagName=高薪" \
  -H "Authorization: Bearer {token}"
```

### 为岗位批量添加标签示例
```bash
curl -X POST "http://localhost:8080/api/job-tags/job/1/batch" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '["高薪", "远程办公", "技术驱动"]'
```

### 获取岗位标签示例
```bash
curl -X GET "http://localhost:8080/api/job-tags/job/1" \
  -H "Authorization: Bearer {token}"
```

### 根据标签搜索岗位示例
```bash
curl -X GET "http://localhost:8080/api/job-tags/search/jobs?tagName=高薪" \
  -H "Authorization: Bearer {token}"
```

### 更新标签示例
```bash
# 更新标签名称
curl -X PUT "http://localhost:8080/api/job-tags/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "tagName": "更新后的标签名称"
  }'

# 更新标签名称和关联岗位
curl -X PUT "http://localhost:8080/api/job-tags/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "tagName": "更新后的标签名称",
    "jobId": 2
  }'

# 更新标签完整信息
curl -X PUT "http://localhost:8080/api/job-tags/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "tagName": "更新后的标签名称",
    "jobId": 2,
    "tagType": 1,
    "tagColor": "#ff6b6b",
    "description": "更新后的标签描述"
  }'
```

### 获取热门标签示例
```bash
curl -X GET "http://localhost:8080/api/job-tags/hot?limit=5" \
  -H "Authorization: Bearer {token}"
```

## 注意事项

- 所有岗位标签接口都需要有效的JWT Token进行认证
- 标签类型：0-系统标签，1-自定义标签
- 系统标签为预定义标签，用户不能修改系统标签
- 同一个岗位不能重复添加相同的标签
- 标签名称支持中文、英文、数字和下划线
- 热门标签按使用次数降序排列
- 标签搜索支持模糊匹配
- 批量操作时，如果部分操作失败，会继续执行其他操作
- **岗位ID为可选参数**：新建标签和更新标签时，jobId字段可以为null，表示创建独立标签
- **独立标签**：没有关联岗位的标签可以作为系统常用标签使用
- **关联标签**：关联了具体岗位的标签用于岗位特征描述

## 标签类型说明

### 1. 独立标签
- **定义**: 没有关联具体岗位的标签
- **用途**: 作为系统常用标签库，供用户选择使用
- **创建方式**: 新建标签时不提供jobId参数
- **示例**: `POST /api/job-tags/?tagName=新标签`

### 2. 关联标签
- **定义**: 关联了具体岗位的标签
- **用途**: 描述具体岗位的特征和属性
- **创建方式**: 新建标签时提供jobId参数
- **示例**: `POST /api/job-tags/?tagName=高薪&jobId=1`

### 3. 标签转换
- 独立标签可以通过更新接口关联到具体岗位
- 关联标签可以通过更新接口解除关联（设置jobId为null）
- 支持灵活的标签管理策略

## 系统预定义标签

系统提供了22个常用标签，涵盖常见的岗位特征：
- 急招、高薪、弹性工作、远程办公
- 实习、兼职、全职
- 创业公司、大厂、技术驱动
- 成长空间大、福利待遇好、培训体系完善
- 团队氛围好、领导好、加班少
- 年终奖、股票期权、五险一金
- 餐补、交通补贴、住房补贴

[返回主文档](../docs/README.md)
