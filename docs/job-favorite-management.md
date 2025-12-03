# 岗位收藏管理模块 (JobFavoriteController)

**基础路径**: `/api/job-favorites`

## 概述
岗位收藏功能允许用户收藏感兴趣的岗位，方便后续查看和管理。该功能基于现有的JobFavorite实体和Repository进行扩展开发。

## 功能特性

### 核心功能
- **收藏岗位**: 用户可以将感兴趣的岗位添加到收藏夹
- **取消收藏**: 用户可以从收藏夹中移除岗位
- **收藏状态检查**: 检查用户是否收藏了指定岗位
- **收藏列表查看**: 查看用户的收藏列表和详情
- **批量操作**: 支持批量检查收藏状态、批量删除等操作

### 高级功能
- **热门收藏统计**: 统计收藏数量最多的热门岗位
- **收藏统计信息**: 提供用户的收藏统计数据分析
- **切换收藏状态**: 一键切换收藏/取消收藏状态
- **分页查询**: 支持分页查看收藏列表

## 技术架构

### 数据模型
```java
@Entity
@Table(name = "job_favorite", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "job_id"})
})
public class JobFavorite extends BaseEntity {
    private Long id;
    private Long userId;
    private Long jobId;
    private LocalDateTime createTime;
}
```

### 服务层接口
- `JobFavoriteService`: 岗位收藏服务接口
- `JobFavoriteServiceImpl`: 岗位收藏服务实现类

### 控制器层
- `JobFavoriteController`: 岗位收藏REST API控制器

## 接口列表

### 收藏岗位
- **URL**: `POST /{jobId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 路径 | Long | 是 | 岗位ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "收藏成功",
    "data": {
      "id": 1,
      "userId": 1,
      "jobId": 100,
      "createTime": "2025-11-24T22:00:00"
    },
    "timestamp": "2025-11-24T22:00:00"
  }
  ```
- **功能**: 用户收藏指定岗位

### 取消收藏
- **URL**: `DELETE /{jobId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 路径 | Long | 是 | 岗位ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "取消收藏成功",
    "data": null,
    "timestamp": "2025-11-24T22:00:00"
  }
  ```
- **功能**: 用户取消收藏指定岗位

### 检查收藏状态
- **URL**: `GET /check/{jobId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 路径 | Long | 是 | 岗位ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "查询成功",
    "data": {
      "isFavorited": true
    },
    "timestamp": "2025-11-24T22:00:00"
  }
  ```
- **功能**: 检查用户是否收藏了指定岗位

### 获取我的收藏列表
- **URL**: `GET /my-favorites`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
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
          "userId": 1,
          "jobId": 100,
          "createTime": "2025-11-24T22:00:00"
        }
      ],
      "totalElements": 50,
      "totalPages": 3,
      "size": 20,
      "number": 0
    },
    "timestamp": "2025-11-24T22:00:00"
  }
  ```
- **功能**: 获取当前用户的岗位收藏列表

### 获取我的收藏岗位详情
- **URL**: `GET /my-favorite-jobs`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
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
          "jobId": 100,
          "jobTitle": "Java开发工程师",
          "companyId": 1,
          "companyName": "测试企业有限公司",
          "workCity": "北京",
          "minSalary": 15000.00,
          "maxSalary": 25000.00,
          "jobType": 1,
          "education": "本科",
          "workYears": 3,
          "publishTime": "2025-11-24T22:00:00"
        }
      ],
      "totalElements": 50,
      "totalPages": 3,
      "size": 20,
      "number": 0
    },
    "timestamp": "2025-11-24T22:00:00"
  }
  ```
- **功能**: 获取当前用户收藏的岗位详情列表（包含企业信息）

### 获取我的收藏岗位ID列表
- **URL**: `GET /my-favorite-job-ids`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [100, 200, 300, 400, 500],
    "timestamp": "2025-11-24T22:00:00"
  }
  ```
- **功能**: 获取当前用户收藏的岗位ID列表

### 批量检查收藏状态
- **URL**: `POST /batch-check`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobIds | 请求体 | List<Long> | 是 | 岗位ID列表 |
  
  **请求体示例**:
  ```json
  [100, 200, 300, 400, 500]
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "批量检查成功",
    "data": {
      "100": true,
      "200": false,
      "300": true,
      "400": false,
      "500": true
    },
    "timestamp": "2025-11-24T22:00:00"
  }
  ```
- **功能**: 批量检查用户是否收藏了指定岗位列表

### 获取我的收藏数量
- **URL**: `GET /my-count`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "count": 25
    },
    "timestamp": "2025-11-24T22:00:00"
  }
  ```
- **功能**: 获取当前用户的收藏岗位数量

### 获取岗位收藏数量
- **URL**: `GET /job/{jobId}/count`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 路径 | Long | 是 | 岗位ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "count": 150
    },
    "timestamp": "2025-11-24T22:00:00"
  }
  ```
- **功能**: 获取指定岗位的收藏数量

### 批量获取岗位收藏数量
- **URL**: `POST /batch-job-counts`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobIds | 请求体 | List<Long> | 是 | 岗位ID列表 |
  
  **请求体示例**:
  ```json
  [100, 200, 300, 400, 500]
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "批量获取成功",
    "data": {
      "100": 150,
      "200": 80,
      "300": 200,
      "400": 50,
      "500": 120
    },
    "timestamp": "2025-11-24T22:00:00"
  }
  ```
- **功能**: 批量获取多个岗位的收藏数量

### 获取热门收藏岗位
- **URL**: `GET /hot-favorites`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | limit | 查询 | Integer | 否 | 返回数量限制，默认10 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [300, 100, 500, 200, 400],
    "timestamp": "2025-11-24T22:00:00"
  }
  ```
- **功能**: 获取收藏数量最多的热门岗位

### 获取我的最近收藏
- **URL**: `GET /my-latest-favorites`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | limit | 查询 | Integer | 否 | 返回数量限制，默认10 |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": [
      {
        "id": 5,
        "userId": 1,
        "jobId": 500,
        "createTime": "2025-11-24T22:00:00"
      },
      {
        "id": 4,
        "userId": 1,
        "jobId": 400,
        "createTime": "2025-11-24T21:00:00"
      },
      {
        "id": 3,
        "userId": 1,
        "jobId": 300,
        "createTime": "2025-11-24T20:00:00"
      }
    ],
    "timestamp": "2025-11-24T22:00:00"
  }
  ```
- **功能**: 获取当前用户最近收藏的岗位

### 清空我的收藏
- **URL**: `DELETE /my-all`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "清空收藏成功",
    "data": null,
    "timestamp": "2025-11-24T22:00:00"
  }
  ```
- **功能**: 清空当前用户的所有收藏

### 批量删除收藏
- **URL**: `POST /batch-delete`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobIds | 请求体 | List<Long> | 是 | 岗位ID列表 |
  
  **请求体示例**:
  ```json
  [100, 200, 300]
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "批量删除收藏成功",
    "data": null,
    "timestamp": "2025-11-24T22:00:00"
  }
  ```
- **功能**: 批量删除当前用户的指定收藏

### 获取我的收藏统计
- **URL**: `GET /my-statistics`
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "获取统计成功",
    "data": {
      "totalFavorites": 25,
      "recentWeekFavorites": 5,
      "hotFavoriteJobs": [300, 100, 500, 200, 400],
      "latestFavorites": [
        {
          "id": 5,
          "userId": 1,
          "jobId": 500,
          "createTime": "2025-11-24T22:00:00"
        },
        {
          "id": 4,
          "userId": 1,
          "jobId": 400,
          "createTime": "2025-11-24T21:00:00"
        }
      ],
      "favoriteJobTypes": {},
      "lastUpdated": "2025-11-24T22:00:00"
    },
    "timestamp": "2025-11-24T22:00:00"
  }
  ```
- **功能**: 获取当前用户的收藏统计信息

### 切换收藏状态
- **URL**: `GET /toggle/{jobId}`
- **请求参数**:
  | 参数名 | 位置 | 数据类型 | 必填 | 说明 |
  |--------|------|----------|------|------|
  | jobId | 路径 | Long | 是 | 岗位ID |
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "收藏成功",
    "data": {
      "action": "favorite",
      "favorite": {
        "id": 1,
        "userId": 1,
        "jobId": 100,
        "createTime": "2025-11-24T22:00:00"
      }
    },
    "timestamp": "2025-11-24T22:00:00"
  }
  ```
- **功能**: 切换指定岗位的收藏状态（收藏/取消收藏）

## 数据库设计

### 表结构
```sql
CREATE TABLE job_favorite (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    job_id BIGINT NOT NULL,
    create_time DATETIME NOT NULL,
    UNIQUE KEY uk_user_job (user_id, job_id),
    INDEX idx_user_id (user_id),
    INDEX idx_job_id (job_id),
    INDEX idx_create_time (create_time)
);
```

### 索引优化
- `uk_user_job`: 唯一索引，确保用户对同一岗位只能收藏一次
- `idx_user_id`: 用户ID索引，优化按用户查询
- `idx_job_id`: 岗位ID索引，优化按岗位查询
- `idx_create_time`: 创建时间索引，优化按时间排序查询

## 业务逻辑

### 收藏限制
- 同一用户对同一岗位只能收藏一次
- 收藏操作会自动检查重复收藏
- 取消收藏操作会验证收藏记录是否存在

### 数据一致性
- 使用数据库唯一约束确保数据一致性
- 事务管理确保操作的原子性
- 异常处理确保系统稳定性

## 测试覆盖

### 单元测试
- 收藏岗位功能测试
- 取消收藏功能测试
- 收藏状态检查测试
- 批量操作功能测试
- 统计功能测试

### 集成测试
- API接口集成测试
- 数据库操作集成测试
- 权限验证集成测试

## 部署说明

### 依赖服务
- 岗位服务 (JobService)
- 用户认证服务 (SecurityContextUtil)
- 数据库连接池

### 配置要求
- 数据库连接配置
- 事务管理配置
- 日志配置

## 性能考虑

### 查询优化
- 使用索引优化查询性能
- 分页查询避免大数据量传输
- 批量操作减少数据库交互次数

### 缓存策略
- 热门收藏数据可考虑缓存
- 用户收藏列表可考虑缓存
- 收藏统计信息可考虑缓存

## 安全考虑

### 权限控制
- 所有操作都需要用户认证
- 用户只能操作自己的收藏记录
- 敏感操作需要权限验证

### 数据保护
- 防止SQL注入攻击
- 输入参数验证
- 异常信息脱敏

## 扩展性设计

### 功能扩展
- 支持收藏分类
- 支持收藏标签
- 支持收藏分享
- 支持收藏推荐

### 性能扩展
- 支持读写分离
- 支持分库分表
- 支持缓存集群

## 监控指标

### 业务指标
- 收藏总数
- 活跃收藏用户数
- 热门岗位收藏数
- 收藏转化率

### 技术指标
- API响应时间
- 数据库查询性能
- 系统资源使用率
- 错误率统计

## 故障处理

### 常见问题
- 重复收藏处理
- 数据一致性维护
- 性能瓶颈处理
- 异常情况恢复

### 应急预案
- 数据库连接失败处理
- 服务降级策略
- 数据备份恢复
- 监控告警机制
