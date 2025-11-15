# 权限管理模块 (PermissionController)

**基础路径**: `/api/permissions`

## 接口列表

### 创建角色
- **URL**: `POST /roles`
- **请求参数**:
  ```json
  {
    "name": "角色名称",
    "code": "ROLE_ADMIN", // 角色代码
    "description": "角色描述",
    "type": 1, // 角色类型：1-系统角色，2-业务角色，3-自定义角色
    "status": 1, // 1-启用，0-禁用
    "permissions": ["user:read", "user:write"] // 权限代码列表
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "创建成功",
    "data": {
      "id": 1,
      "name": "角色名称",
      "code": "ROLE_ADMIN",
      "status": 1
    }
  }
  ```
- **功能**: 创建角色

### 更新角色
- **URL**: `PUT /roles/{id}`
- **请求参数**:
  ```json
  {
    "name": "管理员",
    "description": "系统管理员角色",
    "type": 1, // 角色类型：1-系统角色，2-业务角色，3-自定义角色
    "status": 1
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新成功",
    "data": {
      "id": 1,
      "name": "管理员",
      "code": "ROLE_ADMIN",
      "description": "系统管理员角色",
      "type": 1,
      "status": 1,
      "updateTime": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 更新角色

### 删除角色
- **URL**: `DELETE /roles/{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 角色ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "删除成功",
    "data": {
      "id": 1,
      "name": "管理员"
    }
  }
  ```
- **功能**: 删除角色

### 获取角色详情
- **URL**: `GET /roles/{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 角色ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "id": 1,
      "name": "管理员",
      "code": "ROLE_ADMIN",
      "description": "系统管理员角色",
      "type": 1,
      "typeName": "系统角色",
      "status": 1,
      "permissions": [
        {
          "id": 1,
          "name": "用户读取",
          "code": "user:read",
          "type": 3
        },
        {
          "id": 2,
          "name": "用户写入",
          "code": "user:write",
          "type": 3
        }
      ],
      "createTime": "2025-11-08T12:00:00",
      "updateTime": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 获取角色详情

### 分页查询角色列表（支持关键字模糊搜索）
- **URL**: `GET /roles`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
  | keyword | 查询 | String | 否 | 搜索关键字（支持角色名称和角色编码模糊搜索） |
  | status | 查询 | Integer | 否 | 角色状态：1-启用，0-禁用 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "content": [
        {
          "id": 1,
          "name": "管理员",
          "code": "ROLE_ADMIN",
          "description": "系统管理员角色",
          "type": 1,
          "typeName": "系统角色",
          "status": 1,
          "userCount": 5,
          "permissionCount": 20,
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
- **功能**: 分页查询角色，支持按关键字模糊搜索角色名称和角色编码

### 获取所有角色
- **URL**: `GET /roles/all`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "name": "管理员",
        "code": "ROLE_ADMIN",
        "description": "系统管理员角色",
        "status": 1
      },
      {
        "id": 2,
        "name": "普通用户",
        "code": "ROLE_USER",
        "description": "普通用户角色",
        "status": 1
      }
    ]
  }
  ```
- **功能**: 获取所有角色

### 获取启用角色
- **URL**: `GET /roles/enabled`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "name": "管理员",
        "code": "ROLE_ADMIN",
        "description": "系统管理员角色"
      },
      {
        "id": 2,
        "name": "普通用户",
        "code": "ROLE_USER",
        "description": "普通用户角色"
      }
    ]
  }
  ```
- **功能**: 获取所有启用状态的角色

### 启用角色
- **URL**: `POST /roles/{id}/enable`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 角色ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "启用成功",
    "data": {
      "id": 1,
      "name": "管理员",
      "status": 1,
      "updateTime": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 启用角色

### 禁用角色
- **URL**: `POST /roles/{id}/disable`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 角色ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "禁用成功",
    "data": {
      "id": 1,
      "name": "管理员",
      "status": 0,
      "updateTime": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 禁用角色

### 分配角色给用户
- **URL**: `POST /users/{userId}/roles`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
  | roleIds | 请求体 | List<Long> | 是 | 角色ID列表 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "分配成功",
    "data": {
      "userId": 1,
      "assignedRoles": [
        {
          "id": 1,
          "name": "管理员",
          "code": "ROLE_ADMIN"
        },
        {
          "id": 2,
          "name": "普通用户",
          "code": "ROLE_USER"
        }
      ],
      "assignedAt": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 分配角色给用户

### 移除用户角色
- **URL**: `DELETE /users/{userId}/roles/{roleId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
  | roleId | 路径 | Long | 是 | 角色ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "移除成功",
    "data": {
      "userId": 1,
      "roleId": 1,
      "roleName": "管理员"
    }
  }
  ```
- **功能**: 移除用户角色

### 获取用户角色
- **URL**: `GET /users/{userId}/roles`
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
        "name": "管理员",
        "code": "ROLE_ADMIN",
        "description": "系统管理员角色",
        "status": 1,
        "assignTime": "2025-11-08T12:00:00"
      },
      {
        "id": 2,
        "name": "普通用户",
        "code": "ROLE_USER",
        "description": "普通用户角色",
        "status": 1,
        "assignTime": "2025-11-08T12:00:00"
      }
    ]
  }
  ```
- **功能**: 获取用户角色

### 获取用户权限
- **URL**: `GET /users/{userId}/permissions`
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
      "permissions": [
        {
          "id": 1,
          "name": "用户读取",
          "code": "user:read",
          "type": 3,
          "url": "/api/users",
          "method": "GET"
        },
        {
          "id": 2,
          "name": "用户写入",
          "code": "user:write",
          "type": 3,
          "url": "/api/users",
          "method": "POST"
        }
      ],
      "totalCount": 20
    }
  }
  ```
- **功能**: 获取用户权限

### 验证用户权限
- **URL**: `GET /users/{userId}/has-permission`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | userId | 路径 | Long | 是 | 用户ID |
  | permission | 查询 | String | 是 | 权限代码 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "验证成功",
    "data": {
      "userId": 1,
      "permission": "user:read",
      "hasPermission": true,
      "roles": ["ROLE_ADMIN", "ROLE_USER"]
    }
  }
  ```
- **功能**: 验证用户权限

### 获取所有权限
- **URL**: `GET /permissions`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "name": "用户读取",
        "code": "user:read",
        "type": 3,
        "url": "/api/users",
        "method": "GET",
        "status": 1,
        "sortOrder": 1,
        "description": "用户读取权限",
        "icon": "user",
        "module": "user",
        "parentId": 0,
        "createTime": "2025-11-08T12:00:00",
        "updateTime": "2025-11-08T22:00:00"
      },
      {
        "id": 2,
        "name": "用户写入",
        "code": "user:write",
        "type": 3,
        "url": "/api/users",
        "method": "POST",
        "status": 1,
        "sortOrder": 2,
        "description": "用户写入权限",
        "icon": "user",
        "module": "user",
        "parentId": 0,
        "createTime": "2025-11-08T12:00:00",
        "updateTime": "2025-11-08T22:00:00"
      }
    ]
  }
  ```
- **功能**: 获取所有权限

### 获取权限树
- **URL**: `GET /permissions/tree`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 1,
        "name": "用户管理",
        "code": "user:manage",
        "type": 1,
        "description": "用户管理模块",
        "icon": "user",
        "module": "user",
        "sortOrder": 1,
        "status": 1,
        "parentId": 0,
        "children": [
          {
            "id": 2,
            "name": "用户读取",
            "code": "user:read",
            "type": 3,
            "url": "/api/users",
            "method": "GET",
            "description": "用户读取权限",
            "icon": "user",
            "module": "user",
            "sortOrder": 1,
            "status": 1,
            "parentId": 1
          },
          {
            "id": 3,
            "name": "用户写入",
            "code": "user:write",
            "type": 3,
            "url": "/api/users",
            "method": "POST",
            "description": "用户写入权限",
            "icon": "user",
            "module": "user",
            "sortOrder": 2,
            "status": 1,
            "parentId": 1
          }
        ]
      },
      {
        "id": 4,
        "name": "岗位管理",
        "code": "job:manage",
        "type": 1,
        "description": "岗位管理模块",
        "icon": "job",
        "module": "job",
        "sortOrder": 2,
        "status": 1,
        "parentId": 0,
        "children": [
          {
            "id": 5,
            "name": "岗位读取",
            "code": "job:read",
            "type": 3,
            "url": "/api/jobs",
            "method": "GET",
            "description": "岗位读取权限",
            "icon": "job",
            "module": "job",
            "sortOrder": 1,
            "status": 1,
            "parentId": 4
          }
        ]
      }
    ]
  }
  ```
- **功能**: 获取权限树结构

### 创建权限
- **URL**: `POST /permissions`
- **请求参数**:
  ```json
  {
    "name": "用户读取",
    "code": "user:read",
    "type": 3,
    "parentId": 0,
    "url": "/api/users",
    "method": "GET",
    "sortOrder": 1,
    "status": 1,
    "description": "用户读取权限",
    "icon": "user",
    "module": "user"
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "创建成功",
    "data": {
      "id": 1,
      "name": "用户读取",
      "code": "user:read",
      "type": 3,
      "parentId": 0,
      "url": "/api/users",
      "method": "GET",
      "sortOrder": 1,
      "status": 1,
      "description": "用户读取权限",
      "icon": "user",
      "module": "user",
      "createTime": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 创建权限

### 更新权限
- **URL**: `PUT /permissions/{id}`
- **请求参数**:
  ```json
  {
    "name": "用户读取权限",
    "type": 3,
    "parentId": 0,
    "url": "/api/users",
    "method": "GET",
    "sortOrder": 1,
    "status": 1,
    "description": "用户读取权限描述",
    "icon": "user",
    "module": "user"
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "更新成功",
    "data": {
      "id": 1,
      "name": "用户读取权限",
      "code": "user:read",
      "type": 3,
      "parentId": 0,
      "url": "/api/users",
      "method": "GET",
      "sortOrder": 1,
      "status": 1,
      "description": "用户读取权限描述",
      "icon": "user",
      "module": "user",
      "updateTime": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 更新权限

### 删除权限
- **URL**: `DELETE /permissions/{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 权限ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "删除成功",
    "data": {
      "id": 1,
      "name": "权限名称"
    }
  }
  ```
- **功能**: 删除权限

### 获取权限详情
- **URL**: `GET /permissions/{id}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | id | 路径 | Long | 是 | 权限ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "id": 1,
      "name": "用户读取",
      "code": "user:read",
      "type": 3,
      "parentId": 0,
      "parentName": "根节点",
      "url": "/api/users",
      "method": "GET",
      "sortOrder": 1,
      "status": 1,
      "description": "用户读取权限",
      "icon": "user",
      "module": "user",
      "createTime": "2025-11-08T12:00:00",
      "updateTime": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 获取权限详情

### 分页查询权限列表（支持名称模糊搜索）
- **URL**: `GET /permissions/list`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | page | 查询 | Integer | 否 | 页码，从0开始，默认0 |
  | size | 查询 | Integer | 否 | 每页大小，默认20 |
  | name | 查询 | String | 否 | 权限名称（支持模糊搜索） |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "content": [
        {
          "id": 1,
          "name": "用户读取",
          "code": "user:read",
          "type": 3,
          "typeName": "接口",
          "parentId": 0,
          "url": "/api/users",
          "method": "GET",
          "sortOrder": 1,
          "status": 1,
          "statusName": "启用",
          "description": "用户读取权限",
          "icon": "user",
          "module": "user",
          "createTime": "2025-11-08T12:00:00",
          "updateTime": "2025-11-08T22:00:00",
          "roleCount": 5
        },
        {
          "id": 2,
          "name": "用户写入",
          "code": "user:write",
          "type": 3,
          "typeName": "接口",
          "parentId": 0,
          "url": "/api/users",
          "method": "POST",
          "sortOrder": 2,
          "status": 1,
          "statusName": "启用",
          "description": "用户写入权限",
          "icon": "user",
          "module": "user",
          "createTime": "2025-11-08T12:00:00",
          "updateTime": "2025-11-08T22:00:00",
          "roleCount": 3
        }
      ],
      "totalElements": 50,
      "totalPages": 3,
      "size": 20,
      "number": 0
    }
  }
  ```
- **功能**: 分页查询权限列表，支持按权限名称模糊搜索

### 分配权限给角色
- **URL**: `POST /roles/{roleId}/permissions`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | roleId | 路径 | Long | 是 | 角色ID |
  | permissionIds | 请求体 | List<Long> | 是 | 权限ID列表 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "分配成功",
    "data": {
      "roleId": 1,
      "roleName": "管理员",
      "assignedPermissions": [
        {
          "id": 1,
          "name": "用户读取",
          "code": "user:read"
        },
        {
          "id": 2,
          "name": "用户写入",
          "code": "user:write"
        }
      ],
      "assignedAt": "2025-11-08T22:00:00"
    }
  }
  ```
- **功能**: 分配权限给角色

### 移除角色权限
- **URL**: `DELETE /roles/{roleId}/permissions/{permissionId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | roleId | 路径 | Long | 是 | 角色ID |
  | permissionId | 路径 | Long | 是 | 权限ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "移除成功",
    "data": {
      "roleId": 1,
      "permissionId": 1,
      "permissionName": "用户读取"
    }
  }
  ```
- **功能**: 移除角色权限

### 获取角色权限
- **URL**: `GET /roles/{roleId}/permissions`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | roleId | 路径 | Long | 是 | 角色ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "roleId": 1,
      "roleName": "管理员",
      "permissions": [
        {
          "id": 1,
          "name": "用户读取",
          "code": "user:read",
          "type": 3,
          "url": "/api/users",
          "method": "GET"
        },
        {
          "id": 2,
          "name": "用户写入",
          "code": "user:write",
          "type": 3,
          "url": "/api/users",
          "method": "POST"
        }
      ],
      "totalCount": 20
    }
  }
  ```
- **功能**: 获取角色权限

### 获取用户菜单
- **URL**: `GET /users/{userId}/menus`
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
        "name": "用户管理",
        "code": "user:manage",
        "type": 1,
        "url": "/user",
        "icon": "user",
        "sortOrder": 1,
        "children": [
          {
            "id": 2,
            "name": "用户列表",
            "code": "user:list",
            "type": 1,
            "url": "/user/list",
            "icon": "list",
            "sortOrder": 1
          }
        ]
      },
      {
        "id": 3,
        "name": "岗位管理",
        "code": "job:manage",
        "type": 1,
        "url": "/job",
        "icon": "job",
        "sortOrder": 2
      }
    ]
  }
  ```
- **功能**: 获取用户菜单

### 获取用户按钮权限
- **URL**: `GET /users/{userId}/buttons`
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
        "name": "新增用户",
        "code": "user:add",
        "type": 2
      },
      {
        "id": 2,
        "name": "编辑用户",
        "code": "user:edit",
        "type": 2
      },
      {
        "id": 3,
        "name": "删除用户",
        "code": "user:delete",
        "type": 2
      }
    ]
  }
  ```
- **功能**: 获取用户按钮权限

### 获取用户接口权限
- **URL**: `GET /users/{userId}/apis`
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
        "name": "用户读取",
        "code": "user:read",
        "type": 3,
        "url": "/api/users",
        "method": "GET"
      },
      {
        "id": 2,
        "name": "用户写入",
        "code": "user:write",
        "type": 3,
        "url": "/api/users",
        "method": "POST"
      },
      {
        "id": 3,
        "name": "岗位读取",
        "code": "job:read",
        "type": 3,
        "url": "/api/jobs",
        "method": "GET"
      }
    ]
  }
  ```

### 创建角色示例
```bash
curl -X POST "http://localhost:8080/api/permissions/roles" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "name": "管理员",
    "code": "ROLE_ADMIN",
    "description": "系统管理员角色",
    "status": 1,
    "permissions": ["user:read", "user:write", "job:read", "job:write"]
  }'
```

### 分配角色给用户示例
```bash
curl -X POST "http://localhost:8080/api/permissions/users/1/roles" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '[1, 2, 3]'
```

### 获取用户权限示例
```bash
curl -X GET "http://localhost:8080/api/permissions/users/1/permissions" \
  -H "Authorization: Bearer {token}"
```

### 验证用户权限示例
```bash
curl -X GET "http://localhost:8080/api/permissions/users/1/has-permission?permission=user:read" \
  -H "Authorization: Bearer {token}"
```

### 获取权限树示例
```bash
curl -X GET "http://localhost:8080/api/permissions/permissions/tree" \
  -H "Authorization: Bearer {token}"
```

## 注意事项

- 所有权限管理接口都需要有效的JWT Token进行认证
- 角色状态：1-启用，0-禁用
- 角色类型：1-系统角色，2-业务角色，3-自定义角色
- 权限类型：1-菜单，2-按钮，3-接口
- 权限代码格式：模块:操作，如 user:read
- 权限支持树形结构，支持无限层级
- 用户权限通过角色间接获取
- 接口权限验证支持URL和方法匹配
- 权限数据支持导入导出和同步
- 权限缓存提高性能，支持手动清除

[返回主文档](../docs/README.md)
