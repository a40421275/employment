# H3C对象存储测试功能指南

## 概述

本文档介绍了就业服务平台中新增的H3C对象存储测试功能和目录结构查询功能。该功能提供了统一的存储策略接口，支持本地存储和H3C对象存储两种存储方式，并提供了丰富的测试接口。

## 功能特性

### 1. 存储策略抽象
- **StorageStrategy接口**: 定义了统一的文件存储操作
- **LocalStorageStrategy**: 本地文件系统存储实现
- **H3CObjectStorageStrategy**: H3C对象存储模拟实现（可扩展为真实H3C SDK集成）
- **StorageStrategyFactory**: 存储策略工厂，统一管理存储策略

### 2. 测试功能
- 存储策略信息查询
- 存储连接测试
- 文件上传/下载/删除测试
- 文件信息查询
- 目录结构查询
- 存储统计信息获取
- 目录树结构获取

### 3. H3C对象存储特定功能
- H3C配置信息查询
- H3C批量测试
- 模拟预签名URL生成
- 批量删除功能

## API接口

### 基础URL
```
/api/storage/test
```

### 1. 获取所有存储策略信息
```
GET /api/storage/test/strategies
```

**响应示例:**
```json
{
  "code": 200,
  "message": "获取存储策略信息成功",
  "data": {
    "local": {
      "storageType": "local",
      "connectionTest": true,
      "statistics": {
        "totalFiles": 10,
        "totalSize": 1048576,
        "totalSizeMB": 1,
        "directoryExists": true,
        "uploadDir": "uploads/",
        "lastChecked": "2025-12-04T14:00:00"
      }
    },
    "h3c-object-storage": {
      "storageType": "h3c-object-storage",
      "connectionTest": true,
      "config": {
        "endpoint": "https://h3c-object-storage.example.com",
        "bucketName": "employment-bucket",
        "region": "cn-east-1",
        "accessKey": "***隐藏***",
        "secretKey": "***隐藏***"
      }
    }
  },
  "timestamp": "2025-12-04T14:00:00"
}
```

### 2. 测试存储连接
```
POST /api/storage/test/connection-test
```

**响应示例:**
```json
{
  "code": 200,
  "message": "存储连接测试完成",
  "data": {
    "local": true,
    "h3c-object-storage": true
  },
  "timestamp": "2025-12-04T14:00:00"
}
```

### 3. 测试文件上传
```
POST /api/storage/test/upload
Content-Type: multipart/form-data

参数:
- file: 文件 (必填)
- storageType: 存储类型 (可选，默认: local)
- storagePath: 存储路径 (可选)
- metadata: 元数据，格式: key1=value1,key2=value2 (可选)
```

**响应示例:**
```json
{
  "code": 200,
  "message": "文件上传测试成功",
  "data": {
    "storageType": "h3c-object-storage",
    "originalFileName": "test.jpg",
    "fileSize": 102400,
    "contentType": "image/jpeg",
    "fileUrl": "https://h3c-object-storage.example.com/employment-bucket/2025/12/04/abc123_test.jpg",
    "storagePath": "2025/12/04/abc123_test.jpg"
  },
  "timestamp": "2025-12-04T14:00:00"
}
```

### 4. 测试文件下载
```
GET /api/storage/test/download?storageType=h3c-object-storage&storagePath=2025/12/04/abc123_test.jpg
```

**响应示例:**
```json
{
  "code": 200,
  "message": "文件下载测试成功",
  "data": {
    "storageType": "h3c-object-storage",
    "storagePath": "2025/12/04/abc123_test.jpg",
    "fileSize": 102400,
    "downloadSuccess": true
  },
  "timestamp": "2025-12-04T14:00:00"
}
```

### 5. 获取文件信息
```
GET /api/storage/test/file-info?storageType=h3c-object-storage&storagePath=2025/12/04/abc123_test.jpg
```

**响应示例:**
```json
{
  "code": 200,
  "message": "获取文件信息成功",
  "data": {
    "exists": true,
    "storagePath": "2025/12/04/abc123_test.jpg",
    "fileSize": 102400,
    "fileUrl": "https://h3c-object-storage.example.com/employment-bucket/2025/12/04/abc123_test.jpg",
    "originalFileName": "test.jpg",
    "contentType": "image/jpeg",
    "uploadTime": "2025-12-04T14:00:00"
  },
  "timestamp": "2025-12-04T14:00:00"
}
```

### 6. 测试文件删除
```
DELETE /api/storage/test/delete?storageType=h3c-object-storage&storagePath=2025/12/04/abc123_test.jpg
```

**响应示例:**
```json
{
  "code": 200,
  "message": "文件删除测试成功",
  "data": {
    "storageType": "h3c-object-storage",
    "storagePath": "2025/12/04/abc123_test.jpg",
    "deleted": true
  },
  "timestamp": "2025-12-04T14:00:00"
}
```

### 7. 列出目录结构
```
GET /api/storage/test/list-directory?storageType=h3c-object-storage&directoryPath=2025/12/04
```

**响应示例:**
```json
{
  "code": 200,
  "message": "列出目录结构成功",
  "data": {
    "exists": true,
    "isDirectory": true,
    "directoryPath": "2025/12/04",
    "absolutePath": "employment-bucket/2025/12/04",
    "files": {
      "abc123_test.jpg": {
        "name": "abc123_test.jpg",
        "isDirectory": false,
        "size": 102400,
        "lastModified": "2025-12-04T14:00:00"
      }
    },
    "directories": {},
    "fileCount": 1,
    "directoryCount": 0,
    "totalItems": 1
  },
  "timestamp": "2025-12-04T14:00:00"
}
```

### 8. 获取存储统计
```
GET /api/storage/test/statistics?storageType=h3c-object-storage
```

**响应示例:**
```json
{
  "code": 200,
  "message": "获取存储统计成功",
  "data": {
    "totalFiles": 5,
    "totalSize": 512000,
    "totalSizeMB": 0.5,
    "bucketName": "employment-bucket",
    "region": "cn-east-1",
    "endpoint": "https://h3c-object-storage.example.com",
    "lastChecked": "2025-12-04T14:00:00",
    "storageType": "h3c-object-storage"
  },
  "timestamp": "2025-12-04T14:00:00"
}
```

### 9. H3C对象存储批量测试
```
POST /api/storage/test/h3c/batch-test?operation=all
```

**参数:**
- operation: 测试操作 (可选，默认: all)
  - all: 所有测试
  - connection: 仅连接测试
  - statistics: 仅统计测试
  - list: 仅目录列表测试

**响应示例:**
```json
{
  "code": 200,
  "message": "H3C对象存储批量测试完成",
  "data": {
    "storageType": "h3c-object-storage",
    "operation": "all",
    "connectionTest": true,
    "connectionMessage": "连接成功",
    "statistics": {
      "totalFiles": 5,
      "totalSize": 512000,
      "totalSizeMB": 0.5
    },
    "rootDirectory": {
      "exists": true,
      "isDirectory": true,
      "directoryPath": "",
      "fileCount": 0,
      "directoryCount": 1,
      "totalItems": 1
    }
  },
  "timestamp": "2025-12-04T14:00:00"
}
```

### 10. 获取目录树
```
GET /api/storage/test/directory-tree?storageType=h3c-object-storage&rootPath=&maxDepth=3
```

**参数:**
- storageType: 存储类型 (必填)
- rootPath: 根路径 (可选，默认: "")
- maxDepth: 最大深度 (可选，默认: 3)

**响应示例:**
```json
{
  "code": 200,
  "message": "获取目录树成功",
  "data": {
    "valid": true,
    "path": "",
    "absolutePath": "employment-bucket",
    "fileCount": 0,
    "directoryCount": 1,
    "totalItems": 1,
    "files": {},
    "subdirectories": {
      "2025": {
        "valid": true,
        "path": "2025",
        "absolutePath": "employment-bucket/2025",
        "fileCount": 0,
        "directoryCount": 1,
        "totalItems": 1,
        "files": {},
        "subdirectories": {
          "12": {
            "valid": true,
            "path": "2025/12",
            "absolutePath": "employment-bucket/2025/12",
            "fileCount": 0,
            "directoryCount": 1,
            "totalItems": 1,
            "files": {},
            "subdirectories": {
              "04": {
                "valid": true,
                "path": "2025/12/04",
                "absolutePath": "employment-bucket/2025/12/04",
                "fileCount": 1,
                "directoryCount": 0,
                "totalItems": 1,
                "files": {
                  "abc123_test.jpg": {
                    "name": "abc123_test.jpg",
                    "isDirectory": false,
                    "size": 102400,
                    "lastModified": "2025-12-04T14:00:00"
                  }
                },
                "subdirectories": {},
                "currentDepth": 3
              }
            },
            "currentDepth": 2
          }
        },
        "currentDepth": 1
      }
    },
    "currentDepth": 0
  },
  "timestamp": "2025-12-04T14:00:00"
}
```

## 配置说明

### H3C对象存储配置
在`application.properties`或`application-{profile}.properties`中添加以下配置：

```properties
# H3C对象存储配置
h3c.object-storage.endpoint=https://h3c-object-storage.example.com
h3c.object-storage.access-key=your-access-key
h3c.object-storage.secret-key=your-secret-key
h3c.object-storage.bucket-name=employment-bucket
h3c.object-storage.region=cn-east-1
```

### 本地存储配置
```properties
# 文件上传目录
file.storage.path=uploads/
# 文件访问基础URL
file.base.url=http://localhost:8080
# 最大文件大小（字节）
file.upload.max.size=10485760
# 允许的文件类型
file.allowed.types=pdf,doc,docx,jpg,jpeg,png,gif,txt
# 存储类型
file.storage.type=local
```

## 使用示例

### 1. 使用curl测试H3C对象存储
```bash
# 获取存储策略信息
curl -X GET "http://localhost:8080/api/storage/test/strategies"

# 测试H3C连接
curl -X POST "http://localhost:8080/api/storage/test/connection-test"

# 上传文件到H3C对象存储
curl -X POST "http://localhost:8080/api/storage/test/upload" \
  -F "file=@test.jpg" \
  -F "storageType=h3c-object-storage" \
  -F "metadata=description=测试文件,author=系统"

# 列出H3C对象存储目录
curl -X GET "http://localhost:8080/api/storage/test/list-directory?storageType=h3c-object-storage&directoryPath="

# 获取H3C存储统计
curl -X GET "http://localhost:8080/api/storage/test/statistics?storageType=h3c-object-storage"

# 执行H3C批量测试
curl -X POST "http://localhost:8080/api/storage/test/h3c/batch-test?operation=all"
```

### 2. 使用Postman测试
1. 导入Postman集合（根据API文档创建）
2. 设置环境变量：`baseUrl = http://localhost:8080`
3. 依次调用各个接口进行测试

## 扩展说明

### 集成真实H3C SDK
当前实现为模拟实现，如需集成真实H3C对象存储SDK：

1. 添加H3C SDK依赖到`pom.xml`
2. 修改`H3CObjectStorageStrategy`类，替换模拟实现为真实SDK调用
3. 配置H3C认证信息
4. 实现H3C特定的功能（如预签名URL、分片上传等）

### 添加新的存储策略
如需添加新的存储策略（如阿里云OSS、腾讯云COS等）：

1. 创建新的存储策略类，实现`StorageStrategy`接口
2. 在`StorageStrategyFactory`中注册新的存储策略
3. 添加相应的配置项
4. 更新API文档

## 注意事项

1. **安全性**: H3C访问密钥和秘密密钥应妥善保管，建议使用环境变量或配置中心
2. **性能**: 目录树查询可能较慢，建议设置合理的`maxDepth`参数
3. **错误处理**: 所有接口都有统一的错误处理，返回标准格式的错误信息
4. **日志**: 所有操作都有详细的日志记录，便于问题排查
5. **测试**: 建议在生产环境使用前进行充分的测试

## 故障排除

### 常见问题
1. **连接失败**: 检查H3C配置信息是否正确，网络是否通畅
2. **权限不足**: 检查H3C访问密钥是否有足够的权限
3. **存储桶不存在**: 检查`bucket-name`配置是否正确
4. **文件上传失败**: 检查文件大小是否超过限制，文件类型是否允许

### 日志查看
查看应用日志获取详细错误信息：
```bash
tail -f logs/application.log
```

### 联系支持
如遇无法解决的问题，请联系系统管理员或H3C技术支持。
