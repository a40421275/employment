package com.shera.framework.employment.employment.modules.file.controller;

import com.shera.framework.employment.employment.modules.file.service.FileService;
import com.shera.framework.employment.employment.modules.file.dto.FileQueryDTO;
import com.shera.framework.employment.employment.modules.file.dto.AttachmentWithFileDTO;
import com.shera.framework.employment.employment.modules.file.entity.Attachment;
import com.shera.framework.employment.employment.config.FileConfig;
import com.shera.framework.employment.employment.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 文件管理控制器（基于两步式上传绑定方案）
 */
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {
    
    private final FileService fileService;
    private final FileConfig fileConfig;
    
    // ==================== 两步式上传绑定方案 ====================
    
    /**
     * 第一步：上传文件到文件池
     */
    @PostMapping("/upload/pool")
    public ResponseEntity<?> uploadFileToPool(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long userId,
            @RequestParam String fileType) {
        try {
            Long fileId = fileService.uploadFileToPool(file, userId, fileType);
            return ResponseUtil.success("文件上传到文件池成功", Map.of("fileId", fileId));
        } catch (Exception e) {
            return ResponseUtil.error("文件上传到文件池失败: " + e.getMessage());
        }
    }
    
    /**
     * 第二步：创建附件记录
     */
    @PostMapping("/create/attachment")
    public ResponseEntity<?> createAttachment(@RequestBody Map<String, Object> request) {
        try {
            Long fileId = Long.valueOf(request.get("fileId").toString());
            Long userId = Long.valueOf(request.get("userId").toString());
            String businessType = (String) request.get("businessType");
            Long businessId = Long.valueOf(request.get("businessId").toString());
            String description = (String) request.get("description");
            String tags = (String) request.get("tags");
            Boolean isPublic = (Boolean) request.get("isPublic");
            
            Long attachmentId = fileService.createAttachment(fileId, userId, businessType, businessId, description, tags, isPublic);
            
            return ResponseUtil.success("附件创建成功", Map.of("attachmentId", attachmentId));
        } catch (Exception e) {
            return ResponseUtil.error("附件创建失败: " + e.getMessage());
        }
    }
    
    /**
     * 两步式上传绑定的便捷方法
     */
    @PostMapping("/upload/attachment")
    public ResponseEntity<?> uploadAndCreateAttachment(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long userId,
            @RequestParam String fileType,
            @RequestParam String businessType,
            @RequestParam Long businessId,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) Boolean isPublic) {
        try {
            Long attachmentId = fileService.uploadAndCreateAttachment(file, userId, fileType, businessType, businessId, description, tags, isPublic);
            return ResponseUtil.success("文件上传和附件创建成功", Map.of("attachmentId", attachmentId));
        } catch (Exception e) {
            return ResponseUtil.error("文件上传和附件创建失败: " + e.getMessage());
        }
    }
    
    // ==================== 临时文件管理 ====================
    
    /**
     * 上传临时文件（7天过期）
     */
    @PostMapping("/upload/temporary")
    public ResponseEntity<?> uploadTemporaryFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long userId,
            @RequestParam String fileType) {
        try {
            Long attachmentId = fileService.uploadTemporaryFile(file, userId, fileType);
            return ResponseUtil.success("临时文件上传成功", Map.of(
                "attachmentId", attachmentId,
                "isTemporary", true,
                "expireDays", 7
            ));
        } catch (Exception e) {
            return ResponseUtil.error("临时文件上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 绑定临时文件到业务
     */
    @PostMapping("/bind/temporary")
    public ResponseEntity<?> bindTemporaryFileToBusiness(@RequestBody Map<String, Object> request) {
        try {
            Long attachmentId = Long.valueOf(request.get("attachmentId").toString());
            String businessType = (String) request.get("businessType");
            Long businessId = Long.valueOf(request.get("businessId").toString());
            String description = (String) request.get("description");
            String tags = (String) request.get("tags");
            Boolean isPublic = (Boolean) request.get("isPublic");
            
            boolean success = fileService.bindTemporaryFileToBusiness(attachmentId, businessType, businessId, description, tags, isPublic);
            
            return ResponseUtil.success(success ? "临时文件绑定业务成功" : "临时文件绑定业务失败", Map.of("success", success));
        } catch (Exception e) {
            return ResponseUtil.error("临时文件绑定业务失败: " + e.getMessage());
        }
    }
    
    // ==================== 核心文件操作 ====================
    
    /**
     * 获取文件信息
     */
    @GetMapping("/{attachmentId}")
    public ResponseEntity<?> getFileInfo(@PathVariable Long attachmentId) {
        try {
            log.info("获取文件信息 - 附件ID: {}", attachmentId);
            
            Map<String, Object> fileInfo = fileService.getFileInfo(attachmentId);
            
            log.info("获取文件信息成功 - 附件ID: {}, 文件名: {}", attachmentId, fileInfo.get("originalName"));
            return ResponseUtil.success("获取文件信息成功", fileInfo);
            
        } catch (Exception e) {
            log.error("获取文件信息失败 - 附件ID: {}, 错误: {}", attachmentId, e.getMessage());
            return ResponseUtil.error("获取文件信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除文件
     */
    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<?> deleteFile(@PathVariable Long attachmentId) {
        try {
            log.info("删除文件 - 附件ID: {}", attachmentId);
            
            boolean success = fileService.deleteFile(attachmentId);
            
            if (success) {
                log.info("文件删除成功 - 附件ID: {}", attachmentId);
                return ResponseUtil.success("文件删除成功", Map.of("success", true));
            } else {
                log.warn("文件删除失败 - 附件ID: {}", attachmentId);
                return ResponseUtil.error("文件删除失败");
            }
            
        } catch (Exception e) {
            log.error("文件删除失败 - 附件ID: {}, 错误: {}", attachmentId, e.getMessage());
            return ResponseUtil.error("文件删除失败: " + e.getMessage());
        }
    }
    
    /**
     * 下载文件
     */
    @GetMapping("/download/{attachmentId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long attachmentId) {
        log.info("开始文件下载 - 附件ID: {}", attachmentId);
        
        try {
            // 首先检查文件是否存在
            log.debug("检查文件是否存在 - 附件ID: {}", attachmentId);
            if (!fileService.fileExists(attachmentId)) {
                log.warn("文件不存在 - 附件ID: {}", attachmentId);
                return ResponseEntity.notFound().build();
            }
            log.debug("文件存在 - 附件ID: {}", attachmentId);
            
            // 获取文件信息
            log.debug("获取文件信息 - 附件ID: {}", attachmentId);
            Map<String, Object> fileInfo = fileService.getFileInfo(attachmentId);
            log.debug("文件信息获取成功 - 附件ID: {}, 文件名: {}, 文件大小: {}", 
                     attachmentId, fileInfo.get("originalName"), fileInfo.get("fileSize"));
            
            // 检查文件是否可下载
            boolean downloadable = Boolean.TRUE.equals(fileInfo.get("downloadable"));
            log.debug("文件可下载状态 - 附件ID: {}, downloadable: {}", attachmentId, downloadable);
            
            if (!downloadable) {
                log.warn("文件不可下载 - 附件ID: {}, 状态: {}", attachmentId, fileInfo.get("status"));
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            // 下载文件内容
            log.debug("开始下载文件内容 - 附件ID: {}", attachmentId);
            byte[] fileContent = fileService.downloadFile(attachmentId);
            log.debug("文件内容下载完成 - 附件ID: {}, 文件大小: {} bytes", attachmentId, fileContent.length);
            
            // 处理中文文件名编码
            String originalFileName = fileInfo.get("originalName").toString();
            String encodedFileName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.set("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
            headers.setContentLength(fileContent.length);
            
            log.info("文件下载成功 - 附件ID: {}, 文件名: {}, 文件大小: {} bytes", 
                     attachmentId, originalFileName, fileContent.length);
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("文件下载失败 - 附件ID: {}, 错误类型: {}, 错误信息: {}", 
                     attachmentId, e.getClass().getSimpleName(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    
    /**
     * 获取用户文件列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserFiles(
            @PathVariable Long userId,
            @RequestParam(required = false) String businessType) {
        try {
            List<Map<String, Object>> files = fileService.getUserFiles(userId, businessType);
            return ResponseUtil.success("获取用户文件列表成功", files);
        } catch (Exception e) {
            return ResponseUtil.error("获取用户文件列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据业务类型和业务ID获取附件列表
     */
    @GetMapping("/business")
    public ResponseEntity<?> getAttachmentsByBusiness(
            @RequestParam String businessType,
            @RequestParam Long businessId,
            @RequestParam(required = false) String fileType) {
        try {
            List<Map<String, Object>> attachments = fileService.getAttachmentsByBusiness(businessType, businessId, fileType);
            return ResponseUtil.success("获取业务附件列表成功", attachments);
        } catch (Exception e) {
            return ResponseUtil.error("获取业务附件列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查文件是否存在
     */
    @GetMapping("/exists/{attachmentId}")
    public ResponseEntity<?> fileExists(@PathVariable Long attachmentId) {
        try {
            boolean exists = fileService.fileExists(attachmentId);
            return ResponseUtil.success("检查文件存在成功", Map.of("exists", exists));
        } catch (Exception e) {
            return ResponseUtil.error("检查文件存在失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取文件URL
     */
    @GetMapping("/url/{attachmentId}")
    public ResponseEntity<?> getFileUrl(@PathVariable Long attachmentId) {
        try {
            String url = fileService.getFileUrl(attachmentId);
            return ResponseUtil.success("获取文件URL成功", Map.of("url", url));
        } catch (Exception e) {
            return ResponseUtil.error("获取文件URL失败: " + e.getMessage());
        }
    }
    
    /**
     * 统一查询附件列表（支持分页和复杂条件查询）
     */
    @PostMapping("/query")
    public ResponseEntity<?> queryAttachments(@RequestBody FileQueryDTO queryDTO) {
        try {
            Page<AttachmentWithFileDTO> result = fileService.queryAttachments(queryDTO);
            return ResponseUtil.success("查询附件列表成功", result);
        } catch (Exception e) {
            return ResponseUtil.error("查询附件列表失败: " + e.getMessage());
        }
    }
    
    // ==================== 文件验证 ====================
    
    /**
     * 验证文件类型（使用文件配置）
     */
    @PostMapping("/validate/type")
    public ResponseEntity<?> validateFileType(@RequestParam("file") MultipartFile file) {
        try {
            // 从文件配置获取允许的文件类型
            String allowedTypes = fileConfig.getAllowedFileTypes();
            boolean isValid = fileService.validateFileType(file, allowedTypes);
            return ResponseUtil.success("文件类型验证完成", Map.of(
                "isValid", isValid,
                "allowedTypes", allowedTypes
            ));
        } catch (Exception e) {
            return ResponseUtil.error("文件类型验证失败: " + e.getMessage());
        }
    }
    
    /**
     * 验证文件大小（使用文件配置）
     */
    @PostMapping("/validate/size")
    public ResponseEntity<?> validateFileSize(@RequestParam("file") MultipartFile file) {
        try {
            // 从文件配置获取最大文件大小
            long maxSize = fileConfig.getMaxFileSize();
            boolean isValid = fileService.validateFileSize(file, maxSize);
            return ResponseUtil.success("文件大小验证完成", Map.of(
                "isValid", isValid,
                "maxSize", maxSize
            ));
        } catch (Exception e) {
            return ResponseUtil.error("文件大小验证失败: " + e.getMessage());
        }
    }
    
    // ==================== 文件池管理 ====================
    
    /**
     * 根据文件哈希查找文件
     */
    @GetMapping("/find/by-hash")
    public ResponseEntity<?> findFileByHash(@RequestParam String fileHash) {
        try {
            Long fileId = fileService.findFileByHash(fileHash);
            return ResponseUtil.success("查找文件完成", Map.of(
                "fileId", fileId,
                "exists", fileId != null
            ));
        } catch (Exception e) {
            return ResponseUtil.error("查找文件失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取文件存储统计
     */
    @GetMapping("/statistics/storage")
    public ResponseEntity<?> getStorageStatistics() {
        try {
            Map<String, Object> stats = fileService.getStorageStatistics();
            return ResponseUtil.success("获取存储统计成功", stats);
        } catch (Exception e) {
            return ResponseUtil.error("获取存储统计失败: " + e.getMessage());
        }
    }
    
    /**
     * 清理过期文件
     */
    @DeleteMapping("/cleanup/expired")
    public ResponseEntity<?> cleanupExpiredFiles(@RequestParam int days) {
        try {
            boolean result = fileService.cleanupExpiredFiles(days);
            return ResponseUtil.success(result ? "过期文件清理成功" : "过期文件清理失败", Map.of("success", result));
        } catch (Exception e) {
            return ResponseUtil.error("过期文件清理失败: " + e.getMessage());
        }
    }
    
    /**
     * 清理未引用的文件
     */
    @DeleteMapping("/cleanup/unreferenced")
    public ResponseEntity<?> cleanupUnreferencedFiles(@RequestParam int days) {
        try {
            boolean result = fileService.cleanupUnreferencedFiles(days);
            return ResponseUtil.success(result ? "未引用文件清理成功" : "未引用文件清理失败", Map.of("success", result));
        } catch (Exception e) {
            return ResponseUtil.error("未引用文件清理失败: " + e.getMessage());
        }
    }
    
    // ==================== 签名URL方案 ====================
    
    /**
     * 生成签名文件URL（用于前端图片访问）
     */
    @GetMapping("/signed/generate/{attachmentId}")
    public ResponseEntity<?> generateSignedFileUrl(@PathVariable Long attachmentId) {
        try {
            String signedUrl = fileService.generateSignedFileUrl(attachmentId);
            return ResponseUtil.success("生成签名URL成功", Map.of(
                "signedUrl", signedUrl,
                "attachmentId", attachmentId
            ));
        } catch (Exception e) {
            return ResponseUtil.error("生成签名URL失败: " + e.getMessage());
        }
    }
    
    /**
     * 通过签名URL下载文件（无需认证）
     */
    @GetMapping("/signed/")
    public ResponseEntity<byte[]> downloadFileWithSignature(
            @RequestParam Long attachmentId,
            @RequestParam Long expires,
            @RequestParam String signature) {
        log.info("开始签名URL文件下载 - 附件ID: {}, 过期时间: {}, 签名: {}", attachmentId, expires, signature);
        
        try {
            // 验证签名并下载文件
            byte[] fileContent = fileService.downloadFileWithSignature(attachmentId, expires, signature);
            
            // 获取文件信息以设置正确的Content-Type
            Map<String, Object> fileInfo = fileService.getFileInfo(attachmentId);
            String mimeType = (String) fileInfo.get("mimeType");
            String originalName = (String) fileInfo.get("originalName");
            
            // 处理中文文件名编码
            String encodedFileName = URLEncoder.encode(originalName, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(mimeType != null ? mimeType : "application/octet-stream"));
            headers.set("Content-Disposition", "inline; filename*=UTF-8''" + encodedFileName);
            headers.setContentLength(fileContent.length);
            
            log.info("签名URL文件下载成功 - 附件ID: {}, 文件名: {}, 文件大小: {} bytes", 
                     attachmentId, originalName, fileContent.length);
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("签名URL文件下载失败 - 附件ID: {}, 错误类型: {}, 错误信息: {}", 
                     attachmentId, e.getClass().getSimpleName(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
