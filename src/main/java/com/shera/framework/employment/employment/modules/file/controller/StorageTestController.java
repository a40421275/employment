package com.shera.framework.employment.employment.modules.file.controller;

import com.shera.framework.employment.employment.modules.file.storage.StorageStrategy;
import com.shera.framework.employment.employment.modules.file.storage.StorageStrategyFactory;
import com.shera.framework.employment.employment.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储测试控制器
 * 提供H3C对象存储测试功能和目录结构查询功能
 */
@RestController
@RequestMapping("/api/storage/test")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "存储测试", description = "H3C对象存储测试和目录结构查询")
public class StorageTestController {
    
    private final StorageStrategyFactory storageStrategyFactory;
    
    @GetMapping("/strategies")
    @Operation(summary = "获取所有存储策略信息", description = "获取系统中所有可用的存储策略信息")
    public ResponseEntity<?> getAllStorageStrategies() {
        try {
            Map<String, Map<String, Object>> strategiesInfo = 
                    storageStrategyFactory.getAllStorageStrategiesInfo();
            
            return ResponseUtil.success("获取存储策略信息成功", strategiesInfo);
            
        } catch (Exception e) {
            log.error("获取存储策略信息失败", e);
            return ResponseUtil.error("获取存储策略信息失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/connection-test")
    @Operation(summary = "测试存储连接", description = "测试所有存储策略的连接状态")
    public ResponseEntity<?> testStorageConnections() {
        try {
            Map<String, Boolean> testResults = 
                    storageStrategyFactory.testAllStorageConnections();
            
            return ResponseUtil.success("存储连接测试完成", testResults);
            
        } catch (Exception e) {
            log.error("存储连接测试失败", e);
            return ResponseUtil.error("存储连接测试失败: " + e.getMessage());
        }
    }
    
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "测试文件上传", description = "测试文件上传到指定存储策略")
    public ResponseEntity<?> testFileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "storageType", defaultValue = "local") String storageType,
            @RequestParam(value = "storagePath", required = false) String storagePath,
            @RequestParam(value = "metadata", required = false) String metadataJson) {
        
        try {
            StorageStrategy strategy = storageStrategyFactory.getStorageStrategy(storageType);
            
            Map<String, String> metadata = new HashMap<>();
            if (metadataJson != null && !metadataJson.trim().isEmpty()) {
                // 简单解析metadata，实际应用中可以使用JSON解析器
                String[] pairs = metadataJson.split(",");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    if (keyValue.length == 2) {
                        metadata.put(keyValue[0].trim(), keyValue[1].trim());
                    }
                }
            }
            
            String fileUrl = strategy.uploadFile(file, storagePath, metadata);
            
            Map<String, Object> result = new HashMap<>();
            result.put("storageType", storageType);
            result.put("originalFileName", file.getOriginalFilename());
            result.put("fileSize", file.getSize());
            result.put("contentType", file.getContentType());
            result.put("fileUrl", fileUrl);
            result.put("storagePath", storagePath);
            
            log.info("存储测试文件上传成功 - 存储类型: {}, 文件名: {}, URL: {}", 
                    storageType, file.getOriginalFilename(), fileUrl);
            
            return ResponseUtil.success("文件上传测试成功", result);
            
        } catch (Exception e) {
            log.error("存储测试文件上传失败 - 存储类型: {}, 文件名: {}", 
                    storageType, file.getOriginalFilename(), e);
            return ResponseUtil.error("文件上传测试失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/download")
    @Operation(summary = "测试文件下载", description = "测试从指定存储策略下载文件")
    public ResponseEntity<?> testFileDownload(
            @RequestParam("storageType") String storageType,
            @RequestParam("storagePath") String storagePath) {
        
        try {
            StorageStrategy strategy = storageStrategyFactory.getStorageStrategy(storageType);
            
            if (!strategy.fileExists(storagePath)) {
                return ResponseUtil.error("文件不存在: " + storagePath);
            }
            
            byte[] fileContent = strategy.downloadFile(storagePath);
            
            Map<String, Object> result = new HashMap<>();
            result.put("storageType", storageType);
            result.put("storagePath", storagePath);
            result.put("fileSize", fileContent.length);
            result.put("downloadSuccess", true);
            
            log.info("存储测试文件下载成功 - 存储类型: {}, 存储路径: {}, 文件大小: {} bytes", 
                    storageType, storagePath, fileContent.length);
            
            return ResponseUtil.success("文件下载测试成功", result);
            
        } catch (Exception e) {
            log.error("存储测试文件下载失败 - 存储类型: {}, 存储路径: {}", 
                    storageType, storagePath, e);
            return ResponseUtil.error("文件下载测试失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/file-info")
    @Operation(summary = "获取文件信息", description = "获取指定存储路径的文件信息")
    public ResponseEntity<?> getFileInfo(
            @RequestParam("storageType") String storageType,
            @RequestParam("storagePath") String storagePath) {
        
        try {
            StorageStrategy strategy = storageStrategyFactory.getStorageStrategy(storageType);
            
            Map<String, Object> fileInfo = strategy.getFileInfo(storagePath);
            
            log.info("获取文件信息成功 - 存储类型: {}, 存储路径: {}", storageType, storagePath);
            
            return ResponseUtil.success("获取文件信息成功", fileInfo);
            
        } catch (Exception e) {
            log.error("获取文件信息失败 - 存储类型: {}, 存储路径: {}", storageType, storagePath, e);
            return ResponseUtil.error("获取文件信息失败: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/delete")
    @Operation(summary = "测试文件删除", description = "测试从指定存储策略删除文件")
    public ResponseEntity<?> testFileDelete(
            @RequestParam("storageType") String storageType,
            @RequestParam("storagePath") String storagePath) {
        
        try {
            StorageStrategy strategy = storageStrategyFactory.getStorageStrategy(storageType);
            
            boolean deleted = strategy.deleteFile(storagePath);
            
            Map<String, Object> result = new HashMap<>();
            result.put("storageType", storageType);
            result.put("storagePath", storagePath);
            result.put("deleted", deleted);
            
            if (deleted) {
                log.info("存储测试文件删除成功 - 存储类型: {}, 存储路径: {}", storageType, storagePath);
                return ResponseUtil.success("文件删除测试成功", result);
            } else {
                log.warn("存储测试文件删除失败 - 存储类型: {}, 存储路径: {}", storageType, storagePath);
                return ResponseUtil.error("文件删除测试失败: 文件可能不存在或无权限删除");
            }
            
        } catch (Exception e) {
            log.error("存储测试文件删除失败 - 存储类型: {}, 存储路径: {}", storageType, storagePath, e);
            return ResponseUtil.error("文件删除测试失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/list-directory")
    @Operation(summary = "列出目录结构", description = "列出指定存储策略的目录结构")
    public ResponseEntity<?> listDirectory(
            @RequestParam("storageType") String storageType,
            @RequestParam(value = "directoryPath", defaultValue = "") String directoryPath) {
        
        try {
            StorageStrategy strategy = storageStrategyFactory.getStorageStrategy(storageType);
            
            Map<String, Object> directoryInfo = strategy.listDirectory(directoryPath);
            
            log.info("列出目录结构成功 - 存储类型: {}, 目录路径: {}", storageType, directoryPath);
            
            return ResponseUtil.success("列出目录结构成功", directoryInfo);
            
        } catch (Exception e) {
            log.error("列出目录结构失败 - 存储类型: {}, 目录路径: {}", storageType, directoryPath, e);
            return ResponseUtil.error("列出目录结构失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/statistics")
    @Operation(summary = "获取存储统计", description = "获取指定存储策略的统计信息")
    public ResponseEntity<?> getStorageStatistics(
            @RequestParam("storageType") String storageType) {
        
        try {
            StorageStrategy strategy = storageStrategyFactory.getStorageStrategy(storageType);
            
            Map<String, Object> statistics = strategy.getStorageStatistics();
            
            log.info("获取存储统计成功 - 存储类型: {}", storageType);
            
            return ResponseUtil.success("获取存储统计成功", statistics);
            
        } catch (Exception e) {
            log.error("获取存储统计失败 - 存储类型: {}", storageType, e);
            return ResponseUtil.error("获取存储统计失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/h3c/batch-test")
    @Operation(summary = "H3C对象存储批量测试", description = "执行H3C对象存储的批量测试操作")
    public ResponseEntity<?> h3cBatchTest(
            @RequestParam(value = "operation", defaultValue = "all") String operation) {
        
        try {
            StorageStrategy h3cStrategy = storageStrategyFactory.getH3CObjectStorageStrategy();
            
            Map<String, Object> testResults = new HashMap<>();
            testResults.put("storageType", "h3c-object-storage");
            testResults.put("operation", operation);
            
            // 连接测试
            if ("all".equals(operation) || "connection".equals(operation)) {
                boolean connected = h3cStrategy.testConnection();
                testResults.put("connectionTest", connected);
                testResults.put("connectionMessage", connected ? "连接成功" : "连接失败");
            }
            
            // 统计信息
            if ("all".equals(operation) || "statistics".equals(operation)) {
                Map<String, Object> statistics = h3cStrategy.getStorageStatistics();
                testResults.put("statistics", statistics);
            }
            
            // 列出根目录
            if ("all".equals(operation) || "list".equals(operation)) {
                Map<String, Object> rootDirectory = h3cStrategy.listDirectory("");
                testResults.put("rootDirectory", rootDirectory);
            }
            
            log.info("H3C对象存储批量测试完成 - 操作: {}", operation);
            
            return ResponseUtil.success("H3C对象存储批量测试完成", testResults);
            
        } catch (Exception e) {
            log.error("H3C对象存储批量测试失败", e);
            return ResponseUtil.error("H3C对象存储批量测试失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/directory-tree")
    @Operation(summary = "获取目录树", description = "获取指定存储策略的完整目录树结构")
    public ResponseEntity<?> getDirectoryTree(
            @RequestParam("storageType") String storageType,
            @RequestParam(value = "rootPath", defaultValue = "") String rootPath,
            @RequestParam(value = "maxDepth", defaultValue = "3") int maxDepth) {
        
        try {
            StorageStrategy strategy = storageStrategyFactory.getStorageStrategy(storageType);
            
            Map<String, Object> directoryTree = buildDirectoryTree(strategy, rootPath, maxDepth, 0);
            
            log.info("获取目录树成功 - 存储类型: {}, 根路径: {}, 最大深度: {}", 
                    storageType, rootPath, maxDepth);
            
            return ResponseUtil.success("获取目录树成功", directoryTree);
            
        } catch (Exception e) {
            log.error("获取目录树失败 - 存储类型: {}, 根路径: {}", storageType, rootPath, e);
            return ResponseUtil.error("获取目录树失败: " + e.getMessage());
        }
    }
    
    // ==================== 辅助方法 ====================
    
    private Map<String, Object> buildDirectoryTree(StorageStrategy strategy, String currentPath, 
                                                  int maxDepth, int currentDepth) {
        Map<String, Object> treeNode = new HashMap<>();
        
        if (currentDepth >= maxDepth) {
            treeNode.put("depthLimitReached", true);
            return treeNode;
        }
        
        // 获取当前目录信息
        Map<String, Object> directoryInfo = strategy.listDirectory(currentPath);
        
        if (!Boolean.TRUE.equals(directoryInfo.get("exists")) || 
            !Boolean.TRUE.equals(directoryInfo.get("isDirectory"))) {
            treeNode.put("valid", false);
            treeNode.put("message", "无效的目录路径: " + currentPath);
            return treeNode;
        }
        
        treeNode.put("valid", true);
        treeNode.put("path", currentPath);
        treeNode.put("absolutePath", directoryInfo.get("absolutePath"));
        treeNode.put("fileCount", directoryInfo.get("fileCount"));
        treeNode.put("directoryCount", directoryInfo.get("directoryCount"));
        treeNode.put("totalItems", directoryInfo.get("totalItems"));
        
        // 添加文件列表
        treeNode.put("files", directoryInfo.get("files"));
        
        // 递归构建子目录树
        Map<String, Object> directories = (Map<String, Object>) directoryInfo.get("directories");
        Map<String, Object> subTree = new HashMap<>();
        
        if (directories != null && !directories.isEmpty()) {
            for (String dirName : directories.keySet()) {
                String subPath = currentPath.isEmpty() ? dirName : currentPath + "/" + dirName;
                Map<String, Object> childNode = buildDirectoryTree(strategy, subPath, maxDepth, currentDepth + 1);
                subTree.put(dirName, childNode);
            }
        }
        
        treeNode.put("subdirectories", subTree);
        treeNode.put("currentDepth", currentDepth);
        
        return treeNode;
    }
}
