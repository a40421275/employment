package com.shera.framework.employment.employment.controller;

import com.shera.framework.employment.employment.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 文件管理控制器
 */
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    
    private final FileService fileService;
    
    /**
     * 上传单个文件
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long userId,
            @RequestParam String fileType) {
        try {
            String fileId = fileService.uploadFile(file, userId, fileType);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "fileId", fileId,
                "message", "文件上传成功"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "文件上传失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 批量上传文件
     */
    @PostMapping("/upload/batch")
    public ResponseEntity<Map<String, Object>> uploadFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam Long userId,
            @RequestParam String fileType) {
        try {
            List<String> fileIds = fileService.uploadFiles(files, userId, fileType);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "fileIds", fileIds,
                "message", "批量文件上传成功"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "批量文件上传失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 下载文件
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId) {
        try {
            byte[] fileContent = fileService.downloadFile(fileId);
            Map<String, Object> fileInfo = fileService.getFileInfo(fileId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileInfo.get("fileName").toString());
            headers.setContentLength(fileContent.length);
            
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 获取文件信息
     */
    @GetMapping("/info/{fileId}")
    public ResponseEntity<Map<String, Object>> getFileInfo(@PathVariable String fileId) {
        try {
            Map<String, Object> fileInfo = fileService.getFileInfo(fileId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", fileInfo
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "获取文件信息失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 删除文件
     */
    @DeleteMapping("/{fileId}")
    public ResponseEntity<Map<String, Object>> deleteFile(@PathVariable String fileId) {
        try {
            boolean result = fileService.deleteFile(fileId);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "文件删除成功" : "文件删除失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "文件删除失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 批量删除文件
     */
    @DeleteMapping("/batch")
    public ResponseEntity<Map<String, Object>> deleteFiles(@RequestBody List<String> fileIds) {
        try {
            boolean result = fileService.deleteFiles(fileIds);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "批量文件删除成功" : "批量文件删除失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "批量文件删除失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 获取用户文件列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserFiles(
            @PathVariable Long userId,
            @RequestParam(required = false) String fileType) {
        try {
            List<Map<String, Object>> files = fileService.getUserFiles(userId, fileType);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", files
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "获取用户文件列表失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 获取用户文件数量
     */
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Map<String, Object>> getUserFileCount(
            @PathVariable Long userId,
            @RequestParam(required = false) String fileType) {
        try {
            long count = fileService.getUserFileCount(userId, fileType);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "count", count
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "获取用户文件数量失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 检查文件是否存在
     */
    @GetMapping("/exists/{fileId}")
    public ResponseEntity<Map<String, Object>> fileExists(@PathVariable String fileId) {
        try {
            boolean exists = fileService.fileExists(fileId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "exists", exists
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "检查文件存在失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 获取文件URL
     */
    @GetMapping("/url/{fileId}")
    public ResponseEntity<Map<String, Object>> getFileUrl(@PathVariable String fileId) {
        try {
            String url = fileService.getFileUrl(fileId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "url", url
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "获取文件URL失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 获取文件预览URL
     */
    @GetMapping("/preview/{fileId}")
    public ResponseEntity<Map<String, Object>> getFilePreviewUrl(@PathVariable String fileId) {
        try {
            String previewUrl = fileService.getFilePreviewUrl(fileId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "previewUrl", previewUrl
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "获取文件预览URL失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 验证文件类型
     */
    @PostMapping("/validate/type")
    public ResponseEntity<Map<String, Object>> validateFileType(
            @RequestParam("file") MultipartFile file,
            @RequestParam String allowedTypes) {
        try {
            boolean isValid = fileService.validateFileType(file, allowedTypes);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "isValid", isValid,
                "message", isValid ? "文件类型验证通过" : "文件类型验证失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "文件类型验证失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 验证文件大小
     */
    @PostMapping("/validate/size")
    public ResponseEntity<Map<String, Object>> validateFileSize(
            @RequestParam("file") MultipartFile file,
            @RequestParam long maxSize) {
        try {
            boolean isValid = fileService.validateFileSize(file, maxSize);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "isValid", isValid,
                "message", isValid ? "文件大小验证通过" : "文件大小验证失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "文件大小验证失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 获取文件存储统计
     */
    @GetMapping("/statistics/storage")
    public ResponseEntity<Map<String, Object>> getStorageStatistics() {
        try {
            Map<String, Object> stats = fileService.getStorageStatistics();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", stats
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "获取存储统计失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 清理过期文件
     */
    @DeleteMapping("/cleanup/expired")
    public ResponseEntity<Map<String, Object>> cleanupExpiredFiles(@RequestParam int days) {
        try {
            boolean result = fileService.cleanupExpiredFiles(days);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "过期文件清理成功" : "过期文件清理失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "过期文件清理失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 压缩文件
     */
    @PostMapping("/compress/{fileId}")
    public ResponseEntity<Map<String, Object>> compressFile(
            @PathVariable String fileId,
            @RequestParam String format) {
        try {
            String compressedFileId = fileService.compressFile(fileId, format);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "compressedFileId", compressedFileId,
                "message", "文件压缩成功"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "文件压缩失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 转换文件格式
     */
    @PostMapping("/convert/{fileId}")
    public ResponseEntity<Map<String, Object>> convertFileFormat(
            @PathVariable String fileId,
            @RequestParam String targetFormat) {
        try {
            String convertedFileId = fileService.convertFileFormat(fileId, targetFormat);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "convertedFileId", convertedFileId,
                "message", "文件格式转换成功"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "文件格式转换失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 提取文件元数据
     */
    @GetMapping("/metadata/{fileId}")
    public ResponseEntity<Map<String, Object>> extractFileMetadata(@PathVariable String fileId) {
        try {
            Map<String, Object> metadata = fileService.extractFileMetadata(fileId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", metadata
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "提取文件元数据失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 生成文件缩略图
     */
    @PostMapping("/thumbnail/{fileId}")
    public ResponseEntity<Map<String, Object>> generateThumbnail(
            @PathVariable String fileId,
            @RequestParam int width,
            @RequestParam int height) {
        try {
            String thumbnailId = fileService.generateThumbnail(fileId, width, height);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "thumbnailId", thumbnailId,
                "message", "缩略图生成成功"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "缩略图生成失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 文件重命名
     */
    @PutMapping("/rename/{fileId}")
    public ResponseEntity<Map<String, Object>> renameFile(
            @PathVariable String fileId,
            @RequestParam String newName) {
        try {
            boolean result = fileService.renameFile(fileId, newName);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "文件重命名成功" : "文件重命名失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "文件重命名失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 移动文件
     */
    @PutMapping("/move/{fileId}")
    public ResponseEntity<Map<String, Object>> moveFile(
            @PathVariable String fileId,
            @RequestParam String newPath) {
        try {
            boolean result = fileService.moveFile(fileId, newPath);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "文件移动成功" : "文件移动失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "文件移动失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 复制文件
     */
    @PostMapping("/copy/{fileId}")
    public ResponseEntity<Map<String, Object>> copyFile(
            @PathVariable String fileId,
            @RequestParam String newPath) {
        try {
            String copiedFileId = fileService.copyFile(fileId, newPath);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "copiedFileId", copiedFileId,
                "message", "文件复制成功"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "文件复制失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 搜索文件
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchFiles(
            @RequestParam Long userId,
            @RequestParam String keyword,
            @RequestParam(required = false) String fileType) {
        try {
            List<Map<String, Object>> results = fileService.searchFiles(userId, keyword, fileType);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", results
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "搜索文件失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 获取文件版本历史
     */
    @GetMapping("/versions/{fileId}")
    public ResponseEntity<Map<String, Object>> getFileVersionHistory(@PathVariable String fileId) {
        try {
            List<Map<String, Object>> versions = fileService.getFileVersionHistory(fileId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", versions
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "获取文件版本历史失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 恢复文件版本
     */
    @PostMapping("/restore/{fileId}")
    public ResponseEntity<Map<String, Object>> restoreFileVersion(
            @PathVariable String fileId,
            @RequestParam String versionId) {
        try {
            boolean result = fileService.restoreFileVersion(fileId, versionId);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "文件版本恢复成功" : "文件版本恢复失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "文件版本恢复失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 设置文件权限
     */
    @PutMapping("/permissions/{fileId}")
    public ResponseEntity<Map<String, Object>> setFilePermissions(
            @PathVariable String fileId,
            @RequestBody Map<String, Boolean> permissions) {
        try {
            boolean result = fileService.setFilePermissions(fileId, permissions);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "文件权限设置成功" : "文件权限设置失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "文件权限设置失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 获取文件权限
     */
    @GetMapping("/permissions/{fileId}")
    public ResponseEntity<Map<String, Object>> getFilePermissions(@PathVariable String fileId) {
        try {
            Map<String, Boolean> permissions = fileService.getFilePermissions(fileId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", permissions
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "获取文件权限失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 分享文件
     */
    @PostMapping("/share/{fileId}")
    public ResponseEntity<Map<String, Object>> shareFile(
            @PathVariable String fileId,
            @RequestParam Long targetUserId,
            @RequestParam String permission) {
        try {
            String shareId = fileService.shareFile(fileId, targetUserId, permission);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "shareId", shareId,
                "message", "文件分享成功"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "文件分享失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 取消文件分享
     */
    @DeleteMapping("/share/{fileId}")
    public ResponseEntity<Map<String, Object>> unshareFile(
            @PathVariable String fileId,
            @RequestParam Long targetUserId) {
        try {
            boolean result = fileService.unshareFile(fileId, targetUserId);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "文件取消分享成功" : "文件取消分享失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "文件取消分享失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 获取分享的文件列表
     */
    @GetMapping("/shared/{userId}")
    public ResponseEntity<Map<String, Object>> getSharedFiles(@PathVariable Long userId) {
        try {
            List<Map<String, Object>> sharedFiles = fileService.getSharedFiles(userId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", sharedFiles
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "获取分享的文件列表失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 获取文件分享列表
     */
    @GetMapping("/shares/{fileId}")
    public ResponseEntity<Map<String, Object>> getFileShares(@PathVariable String fileId) {
        try {
            List<Map<String, Object>> shares = fileService.getFileShares(fileId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", shares
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "获取文件分享列表失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 验证文件完整性
     */
    @GetMapping("/verify/{fileId}")
    public ResponseEntity<Map<String, Object>> verifyFileIntegrity(@PathVariable String fileId) {
        try {
            boolean isValid = fileService.verifyFileIntegrity(fileId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "isValid", isValid,
                "message", isValid ? "文件完整性验证通过" : "文件完整性验证失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "文件完整性验证失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 修复损坏的文件
     */
    @PostMapping("/repair/{fileId}")
    public ResponseEntity<Map<String, Object>> repairFile(@PathVariable String fileId) {
        try {
            boolean result = fileService.repairFile(fileId);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "文件修复成功" : "文件修复失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "文件修复失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 批量处理文件
     */
    @PostMapping("/batch-process")
    public ResponseEntity<Map<String, Object>> batchProcessFiles(
            @RequestBody List<String> fileIds,
            @RequestParam String operation) {
        try {
            List<String> results = fileService.batchProcessFiles(fileIds, operation);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "results", results,
                "message", "批量文件处理成功"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "批量文件处理失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 获取文件处理进度
     */
    @GetMapping("/progress/{processId}")
    public ResponseEntity<Map<String, Object>> getFileProcessProgress(@PathVariable String processId) {
        try {
            Map<String, Object> progress = fileService.getFileProcessProgress(processId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", progress
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "获取文件处理进度失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 取消文件处理
     */
    @DeleteMapping("/cancel/{processId}")
    public ResponseEntity<Map<String, Object>> cancelFileProcess(@PathVariable String processId) {
        try {
            boolean result = fileService.cancelFileProcess(processId);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "文件处理取消成功" : "文件处理取消失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "文件处理取消失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 获取文件操作日志
     */
    @GetMapping("/logs/{fileId}")
    public ResponseEntity<Map<String, Object>> getFileOperationLogs(
            @PathVariable String fileId,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<Map<String, Object>> logs = fileService.getFileOperationLogs(fileId, limit);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", logs
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "获取文件操作日志失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 清理文件操作日志
     */
    @DeleteMapping("/logs/cleanup")
    public ResponseEntity<Map<String, Object>> cleanupFileOperationLogs(@RequestParam int days) {
        try {
            boolean result = fileService.cleanupFileOperationLogs(days);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "文件操作日志清理成功" : "文件操作日志清理失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "文件操作日志清理失败: " + e.getMessage()
            ));
        }
    }
}
