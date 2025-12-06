package com.shera.framework.employment.employment.modules.file.storage;

import com.shera.framework.employment.employment.config.FileConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 本地存储策略实现
 * 基于本地文件系统的存储方式
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LocalStorageStrategy implements StorageStrategy {
    
    private final FileConfig fileConfig;
    
    @Override
    public String uploadFile(MultipartFile file, String storagePath, Map<String, String> metadata) {
        try {
            String uploadDir = fileConfig.getUploadDir();
            Path targetPath = buildTargetPath(uploadDir, storagePath, file.getOriginalFilename());
            
            // 确保目录存在
            Files.createDirectories(targetPath.getParent());
            
            // 保存文件
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            
            log.info("本地文件上传成功 - 存储路径: {}, 文件名: {}", storagePath, file.getOriginalFilename());
            return getFileUrl(storagePath);
            
        } catch (IOException e) {
            log.error("本地文件上传失败 - 存储路径: {}, 错误: {}", storagePath, e.getMessage());
            throw new RuntimeException("文件上传失败", e);
        }
    }
    
    @Override
    public String uploadFile(InputStream inputStream, String storagePath, String fileName, 
                           String contentType, Map<String, String> metadata) {
        try {
            String uploadDir = fileConfig.getUploadDir();
            Path targetPath = buildTargetPath(uploadDir, storagePath, fileName);
            
            // 确保目录存在
            Files.createDirectories(targetPath.getParent());
            
            // 保存文件
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            
            log.info("本地文件上传成功（通过输入流）- 存储路径: {}, 文件名: {}", storagePath, fileName);
            return getFileUrl(storagePath);
            
        } catch (IOException e) {
            log.error("本地文件上传失败（通过输入流）- 存储路径: {}, 错误: {}", storagePath, e.getMessage());
            throw new RuntimeException("文件上传失败", e);
        }
    }
    
    @Override
    public byte[] downloadFile(String storagePath) {
        try {
            String uploadDir = fileConfig.getUploadDir();
            Path filePath = buildFilePath(uploadDir, storagePath);
            
            if (!Files.exists(filePath)) {
                throw new RuntimeException("文件不存在: " + storagePath);
            }
            
            log.debug("本地文件下载成功 - 存储路径: {}", storagePath);
            return Files.readAllBytes(filePath);
            
        } catch (IOException e) {
            log.error("本地文件下载失败 - 存储路径: {}, 错误: {}", storagePath, e.getMessage());
            throw new RuntimeException("文件下载失败", e);
        }
    }
    
    @Override
    public InputStream getFileInputStream(String storagePath) {
        try {
            String uploadDir = fileConfig.getUploadDir();
            Path filePath = buildFilePath(uploadDir, storagePath);
            
            if (!Files.exists(filePath)) {
                throw new RuntimeException("文件不存在: " + storagePath);
            }
            
            log.debug("获取本地文件输入流成功 - 存储路径: {}", storagePath);
            return Files.newInputStream(filePath);
            
        } catch (IOException e) {
            log.error("获取本地文件输入流失败 - 存储路径: {}, 错误: {}", storagePath, e.getMessage());
            throw new RuntimeException("获取文件输入流失败", e);
        }
    }
    
    @Override
    public boolean deleteFile(String storagePath) {
        try {
            String uploadDir = fileConfig.getUploadDir();
            Path filePath = buildFilePath(uploadDir, storagePath);
            
            if (!Files.exists(filePath)) {
                log.warn("要删除的文件不存在 - 存储路径: {}", storagePath);
                return false;
            }
            
            boolean deleted = Files.deleteIfExists(filePath);
            if (deleted) {
                log.info("本地文件删除成功 - 存储路径: {}", storagePath);
            } else {
                log.warn("本地文件删除失败 - 存储路径: {}", storagePath);
            }
            
            return deleted;
            
        } catch (IOException e) {
            log.error("本地文件删除失败 - 存储路径: {}, 错误: {}", storagePath, e.getMessage());
            throw new RuntimeException("文件删除失败", e);
        }
    }
    
    @Override
    public String getFileUrl(String storagePath) {
        String baseUrl = fileConfig.getBaseUrl();
        // 这里需要根据实际URL路由来构建
        return baseUrl + "/api/files/storage/local/" + storagePath;
    }
    
    @Override
    public boolean fileExists(String storagePath) {
        String uploadDir = fileConfig.getUploadDir();
        Path filePath = buildFilePath(uploadDir, storagePath);
        return Files.exists(filePath);
    }
    
    @Override
    public Map<String, Object> getFileInfo(String storagePath) {
        Map<String, Object> fileInfo = new HashMap<>();
        
        try {
            String uploadDir = fileConfig.getUploadDir();
            Path filePath = buildFilePath(uploadDir, storagePath);
            
            if (!Files.exists(filePath)) {
                fileInfo.put("exists", false);
                return fileInfo;
            }
            
            fileInfo.put("exists", true);
            fileInfo.put("storagePath", storagePath);
            fileInfo.put("fileName", filePath.getFileName().toString());
            fileInfo.put("fileSize", Files.size(filePath));
            fileInfo.put("lastModified", Files.getLastModifiedTime(filePath).toString());
            fileInfo.put("isDirectory", Files.isDirectory(filePath));
            fileInfo.put("isRegularFile", Files.isRegularFile(filePath));
            fileInfo.put("fileUrl", getFileUrl(storagePath));
            
        } catch (IOException e) {
            log.error("获取本地文件信息失败 - 存储路径: {}, 错误: {}", storagePath, e.getMessage());
            fileInfo.put("exists", false);
            fileInfo.put("error", e.getMessage());
        }
        
        return fileInfo;
    }
    
    @Override
    public String getStorageType() {
        return "local";
    }
    
    @Override
    public boolean testConnection() {
        try {
            String uploadDir = fileConfig.getUploadDir();
            Path uploadPath = Paths.get(uploadDir);
            
            // 检查目录是否存在，如果不存在则尝试创建
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // 尝试写入测试文件
            Path testFile = uploadPath.resolve("connection_test.txt");
            Files.writeString(testFile, "Connection test at " + LocalDateTime.now());
            
            // 读取验证
            String content = Files.readString(testFile);
            
            // 清理测试文件
            Files.deleteIfExists(testFile);
            
            log.info("本地存储连接测试成功 - 上传目录: {}", uploadDir);
            return true;
            
        } catch (IOException e) {
            log.error("本地存储连接测试失败 - 错误: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public Map<String, Object> getStorageStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            String uploadDir = fileConfig.getUploadDir();
            Path uploadPath = Paths.get(uploadDir);
            
            if (!Files.exists(uploadPath)) {
                stats.put("totalFiles", 0);
                stats.put("totalSize", 0);
                stats.put("directoryExists", false);
                return stats;
            }
            
            // 递归计算文件统计
            long[] totalInfo = calculateDirectoryStats(uploadPath);
            
            stats.put("totalFiles", totalInfo[0]);
            stats.put("totalSize", totalInfo[1]);
            stats.put("totalSizeMB", totalInfo[1] / (1024 * 1024));
            stats.put("directoryExists", true);
            stats.put("uploadDir", uploadDir);
            stats.put("lastChecked", LocalDateTime.now().toString());
            
        } catch (IOException e) {
            log.error("获取本地存储统计失败 - 错误: {}", e.getMessage());
            stats.put("error", e.getMessage());
        }
        
        return stats;
    }
    
    @Override
    public Map<String, Object> listDirectory(String directoryPath) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String uploadDir = fileConfig.getUploadDir();
            Path targetPath = buildDirectoryPath(uploadDir, directoryPath);
            
            if (!Files.exists(targetPath)) {
                result.put("exists", false);
                result.put("message", "目录不存在: " + directoryPath);
                return result;
            }
            
            if (!Files.isDirectory(targetPath)) {
                result.put("exists", true);
                result.put("isDirectory", false);
                result.put("message", "路径不是目录: " + directoryPath);
                return result;
            }
            
            result.put("exists", true);
            result.put("isDirectory", true);
            result.put("directoryPath", directoryPath);
            result.put("absolutePath", targetPath.toAbsolutePath().toString());
            
            // 列出目录内容
            Map<String, Object> files = new HashMap<>();
            Map<String, Object> directories = new HashMap<>();
            
            Files.list(targetPath).forEach(path -> {
                try {
                    String name = path.getFileName().toString();
                    boolean isDir = Files.isDirectory(path);
                    
                    Map<String, Object> itemInfo = new HashMap<>();
                    itemInfo.put("name", name);
                    itemInfo.put("isDirectory", isDir);
                    itemInfo.put("size", isDir ? 0 : Files.size(path));
                    itemInfo.put("lastModified", Files.getLastModifiedTime(path).toString());
                    
                    if (isDir) {
                        directories.put(name, itemInfo);
                    } else {
                        files.put(name, itemInfo);
                    }
                } catch (IOException e) {
                    log.warn("获取文件信息失败: {}", path, e);
                }
            });
            
            result.put("files", files);
            result.put("directories", directories);
            result.put("fileCount", files.size());
            result.put("directoryCount", directories.size());
            result.put("totalItems", files.size() + directories.size());
            
        } catch (IOException e) {
            log.error("列出目录失败 - 目录路径: {}, 错误: {}", directoryPath, e.getMessage());
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    // ==================== 辅助方法 ====================
    
    private Path buildTargetPath(String baseDir, String storagePath, String fileName) {
        if (storagePath == null || storagePath.trim().isEmpty()) {
            // 使用日期目录结构
            LocalDateTime now = LocalDateTime.now();
            String datePath = String.format("%d/%02d/%02d", 
                now.getYear(), now.getMonthValue(), now.getDayOfMonth());
            storagePath = datePath + "/" + UUID.randomUUID().toString();
        }
        
        Path basePath = Paths.get(baseDir);
        Path targetPath = basePath.resolve(storagePath);
        
        // 如果storagePath不包含文件名，添加文件名
        if (fileName != null && !fileName.trim().isEmpty() && 
            !targetPath.getFileName().toString().contains(".")) {
            targetPath = targetPath.resolveSibling(targetPath.getFileName() + "_" + fileName);
        }
        
        return targetPath;
    }
    
    private Path buildFilePath(String baseDir, String storagePath) {
        Path basePath = Paths.get(baseDir);
        return basePath.resolve(storagePath);
    }
    
    private Path buildDirectoryPath(String baseDir, String directoryPath) {
        Path basePath = Paths.get(baseDir);
        if (directoryPath == null || directoryPath.trim().isEmpty()) {
            return basePath;
        }
        return basePath.resolve(directoryPath);
    }
    
    private long[] calculateDirectoryStats(Path directory) throws IOException {
        long[] stats = new long[2]; // [文件数量, 总大小]
        
        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            return stats;
        }
        
        Files.walk(directory)
            .filter(Files::isRegularFile)
            .forEach(path -> {
                try {
                    stats[0]++; // 文件数量
                    stats[1] += Files.size(path); // 总大小
                } catch (IOException e) {
                    log.warn("计算文件大小失败: {}", path, e);
                }
            });
        
        return stats;
    }
}
