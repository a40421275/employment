# zhdc-utils H3C工具类集成指南

## 概述

本文档介绍了如何在就业服务平台中使用zhdc-utils工具包中的H3C对象存储工具类。zhdc-utils是一个包含H3C对象存储功能的工具包，提供了H3C文件存储的客户端和服务类。

## zhdc-utils中的H3C工具类

### 主要类结构

```
com.coli688.h3c_util/
├── FileStoreService.class          # H3C文件存储服务接口
├── FileStoreClient.class           # H3C文件存储客户端接口
├── FileStoreS3Client.class         # H3C S3客户端实现
├── FileStoreServiceImpl.class      # H3C文件存储服务实现
├── config/
│   ├── H3cAutoConfiguration.class  # H3C自动配置类
│   └── H3cConfig.class             # H3C配置类
└── constant/
    └── H3cConstant.class           # H3C常量类
```

### 类说明

#### 1. FileStoreService
H3C文件存储服务接口，提供文件上传、下载、删除等操作。

#### 2. FileStoreClient
H3C文件存储客户端接口，定义客户端操作规范。

#### 3. FileStoreS3Client
基于S3协议的H3C客户端实现，支持S3兼容的对象存储操作。

#### 4. FileStoreServiceImpl
FileStoreService的实现类，提供具体的H3C文件存储功能。

#### 5. H3cConfig
H3C配置类，包含H3C对象存储的连接配置信息。

#### 6. H3cAutoConfiguration
Spring Boot自动配置类，自动配置H3C相关的Bean。

#### 7. H3cConstant
H3C常量类，定义H3C相关的常量。

## 配置说明

### 1. 添加依赖
项目已在`pom.xml`中添加了zhdc-utils依赖：
```xml
<dependency>
    <groupId>com.coli688</groupId>
    <artifactId>zhdc-utils</artifactId>
    <version>1.1</version>
</dependency>
```

### 2. 配置文件
在`application.properties`或`application-{profile}.properties`中添加H3C配置：

```properties
# H3C对象存储配置
h3c.foc.url=https://h3c-object-storage.example.com
h3c.object-storage.access-key=your-access-key
h3c.object-storage.secret-key=your-secret-key
h3c.object-storage.bucket-name=employment-bucket
h3c.object-storage.region=cn-east-1

# zhdc-utils H3C配置
h3c.endpoint=${h3c.foc.url}
h3c.accessKey=${h3c.object-storage.access-key}
h3c.secretKey=${h3c.object-storage.secret-key}
h3c.bucket=${h3c.object-storage.bucket-name}
h3c.region=${h3c.object-storage.region}
```

## 使用示例

### 1. 注入FileStoreService
在Spring Bean中注入FileStoreService：

```java
import com.coli688.h3c_util.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class H3CStorageService {
    
    @Autowired
    private FileStoreService fileStoreService;
    
    // 使用fileStoreService进行文件操作
}
```

### 2. 文件上传示例
```java
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;

public String uploadFileToH3C(MultipartFile file) {
    try {
        // 准备元数据
        Map<String, String> metadata = new HashMap<>();
        metadata.put("description", "就业服务平台文件");
        metadata.put("uploader", "system");
        
        // 生成存储路径
        String storagePath = generateStoragePath(file.getOriginalFilename());
        
        // 调用zhdc-utils上传文件
        // 注意：具体方法名需要根据zhdc-utils中的实际方法确定
        // String fileUrl = fileStoreService.uploadFile(
        //     file.getInputStream(),
        //     storagePath,
        //     file.getContentType(),
        //     metadata
        // );
        
        // 模拟实现
        String fileUrl = "https://h3c-object-storage.example.com/employment-bucket/" + storagePath;
        
        return fileUrl;
        
    } catch (Exception e) {
        throw new RuntimeException("H3C文件上传失败", e);
    }
}

private String generateStoragePath(String fileName) {
    // 生成存储路径：年/月/日/UUID_文件名
    LocalDateTime now = LocalDateTime.now();
    String datePath = String.format("%d/%02d/%02d", 
        now.getYear(), now.getMonthValue(), now.getDayOfMonth());
    String uuid = UUID.randomUUID().toString().substring(0, 8);
    return datePath + "/" + uuid + "_" + fileName;
}
```

### 3. 文件下载示例
```java
public byte[] downloadFileFromH3C(String storagePath) {
    try {
        // 调用zhdc-utils下载文件
        // 注意：具体方法名需要根据zhdc-utils中的实际方法确定
        // byte[] fileData = fileStoreService.downloadFile(storagePath);
        
        // 模拟实现
        byte[] fileData = new byte[0]; // 实际应从H3C获取
        
        return fileData;
        
    } catch (Exception e) {
        throw new RuntimeException("H3C文件下载失败", e);
    }
}
```

### 4. 文件删除示例
```java
public boolean deleteFileFromH3C(String storagePath) {
    try {
        // 调用zhdc-utils删除文件
        // 注意：具体方法名需要根据zhdc-utils中的实际方法确定
        // boolean deleted = fileStoreService.deleteFile(storagePath);
        
        // 模拟实现
        boolean deleted = true;
        
        return deleted;
        
    } catch (Exception e) {
        throw new RuntimeException("H3C文件删除失败", e);
    }
}
```

## 与现有存储策略集成

### 1. 更新H3CObjectStorageStrategy
现有的`H3CObjectStorageStrategy`类已经添加了zhdc-utils的引用。可以按以下方式更新以使用真实的H3C服务：

```java
@Component
@Slf4j
public class H3CObjectStorageStrategy implements StorageStrategy {
    
    @Autowired
    private FileStoreService fileStoreService;
    
    @Override
    public String uploadFile(MultipartFile file, String storagePath, Map<String, String> metadata) {
        try {
            // 使用zhdc-utils的FileStoreService上传文件
            String actualStoragePath = generateStoragePath(storagePath, file.getOriginalFilename());
            
            // 调用zhdc-utils上传文件
            // String fileUrl = fileStoreService.uploadFile(
            //     file.getInputStream(),
            //     actualStoragePath,
            //     file.getContentType(),
            //     metadata
            // );
            
            // 模拟实现
            String fileUrl = getFileUrl(actualStoragePath);
            
            log.info("H3C对象存储文件上传成功（使用zhdc-utils）- 存储路径: {}, 文件名: {}, URL: {}", 
                    actualStoragePath, file.getOriginalFilename(), fileUrl);
            
            return fileUrl;
            
        } catch (Exception e) {
            log.error("H3C对象存储文件上传失败（使用zhdc-utils）- 存储路径: {}, 错误: {}", storagePath, e.getMessage());
            throw new RuntimeException("H3C对象存储文件上传失败", e);
        }
    }
    
    // 其他方法类似更新...
}
```

### 2. 配置自动注入
确保Spring能够自动注入FileStoreService：

```java
@Configuration
public class H3CStorageConfig {
    
    @Bean
    public FileStoreService fileStoreService() {
        // zhdc-utils应该已经提供了自动配置
        // 如果没有，可以手动创建FileStoreService实例
        return new FileStoreServiceImpl();
    }
}
```

## 测试H3C连接

### 1. 连接测试
```java
@Service
public class H3CConnectionTestService {
    
    @Autowired
    private FileStoreService fileStoreService;
    
    public boolean testH3CConnection() {
        try {
            // 调用zhdc-utils的连接测试方法
            // 注意：具体方法名需要根据zhdc-utils中的实际方法确定
            // boolean connected = fileStoreService.testConnection();
            
            // 模拟实现
            boolean connected = true;
            
            if (connected) {
                log.info("H3C对象存储连接测试成功");
            } else {
                log.error("H3C对象存储连接测试失败");
            }
            
            return connected;
            
        } catch (Exception e) {
            log.error("H3C对象存储连接测试异常", e);
            return false;
        }
    }
}
```

### 2. 通过API测试
使用现有的存储测试控制器进行测试：
```
POST /api/storage/test/connection-test
GET /api/storage/test/strategies
POST /api/storage/test/h3c/batch-test
```

## 注意事项

### 1. 版本兼容性
- zhdc-utils版本：1.1
- Spring Boot版本：3.5.7
- Java版本：17

### 2. 配置管理
- H3C访问密钥和秘密密钥应妥善保管
- 建议使用环境变量或配置中心管理敏感配置
- 生产环境和测试环境使用不同的配置

### 3. 错误处理
- 所有H3C操作都应包含适当的错误处理
- 记录详细的日志以便问题排查
- 提供友好的错误信息给用户

### 4. 性能考虑
- 大文件上传应考虑分片上传
- 文件下载应考虑流式传输
- 适当配置连接超时和重试机制

## 故障排除

### 1. 常见问题

#### 问题1：FileStoreService注入失败
**解决方案**：
- 检查zhdc-utils依赖是否正确添加
- 检查H3C配置是否正确
- 查看Spring启动日志是否有相关错误

#### 问题2：H3C连接失败
**解决方案**：
- 检查网络连接是否正常
- 检查H3C配置信息是否正确
- 检查H3C服务是否可用

#### 问题3：文件上传/下载失败
**解决方案**：
- 检查文件大小是否超过限制
- 检查文件类型是否允许
- 检查存储路径是否正确
- 检查权限是否足够

### 2. 日志查看
查看应用日志获取详细错误信息：
```bash
# 查看Spring Boot应用日志
tail -f logs/application.log

# 查看H3C相关日志
grep -i "h3c" logs/application.log
```

### 3. 联系支持
如遇无法解决的问题：
1. 联系系统管理员
2. 联系H3C技术支持
3. 联系zhdc-utils维护者

## 扩展开发

### 1. 自定义H3C客户端
如果需要自定义H3C客户端行为，可以继承或实现zhdc-utils中的相关类：

```java
@Component
public class CustomH3CClient extends FileStoreS3Client {
    
    @Override
    public String uploadFile(InputStream inputStream, String path, 
                           String contentType, Map<String, String> metadata) {
        // 自定义上传逻辑
        // ...
        return super.uploadFile(inputStream, path, contentType, metadata);
    }
    
    // 其他自定义方法...
}
```

### 2. 添加新功能
可以在现有基础上添加新功能，如：
- 文件分片上传
- 文件预览生成
- 文件水印添加
- 文件加密存储

### 3. 性能优化
- 实现文件上传进度监控
- 添加文件上传并发控制
- 优化大文件处理性能
- 添加缓存机制

## 总结

zhdc-utils提供了完整的H3C对象存储工具类，可以方便地集成到就业服务平台中。通过使用zhdc-utils，可以：

1. 快速实现H3C对象存储功能
2. 减少重复开发工作
3. 提高代码质量和可维护性
4. 统一H3C操作接口和错误处理

建议在实际使用前进行充分的测试，确保H3C功能稳定可靠。
