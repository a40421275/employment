package com.shera.framework.employment.employment.modules.file.controller;

import com.shera.framework.employment.employment.modules.file.service.FileService;
import com.shera.framework.employment.employment.modules.file.dto.FileQueryDTO;
import com.shera.framework.employment.employment.modules.file.dto.AttachmentWithFileDTO;
import com.shera.framework.employment.employment.modules.file.dto.AttachmentUpdateDTO;
import com.shera.framework.employment.employment.modules.file.entity.Attachment;
import com.shera.framework.employment.employment.config.FileConfig;
import com.shera.framework.employment.employment.util.ResponseUtil;
import com.shera.framework.employment.employment.util.UrlSigner;
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
    private final UrlSigner urlSigner;
    
    // ==================== 两步式上传绑定方案 ====================
    
    /**
     * 第一步：上传文件到文件池
     */
    @PostMapping("/upload/pool")
    public ResponseEntity<?> uploadFileToPool(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long userId,
            @RequestParam String fileType,
            @RequestParam(required = false) String fileName) {
        try {
            // 如果未提供自定义文件名，使用原始文件名
            String actualFileName = fileName != null ? fileName : file.getOriginalFilename();
            Long fileId = fileService.uploadFileToPool(file, userId, fileType, actualFileName);
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
     * 下载文件（通过附件ID）
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
     * 通过文件ID下载文件
     */
    @GetMapping("/download/by-file-id/{fileId}")
    public ResponseEntity<byte[]> downloadFileByFileId(@PathVariable Long fileId) {
        log.info("开始通过文件ID下载文件 - 文件ID: {}", fileId);
        
        try {
            // 获取文件信息
            log.debug("获取文件信息 - 文件ID: {}", fileId);
            Map<String, Object> fileInfo = fileService.getFileInfoByFileId(fileId);
            log.debug("文件信息获取成功 - 文件ID: {}, 文件名: {}, 文件大小: {}", 
                     fileId, fileInfo.get("originalName"), fileInfo.get("fileSize"));
            
            // 检查文件是否可下载
            boolean downloadable = Boolean.TRUE.equals(fileInfo.get("downloadable"));
            log.debug("文件可下载状态 - 文件ID: {}, downloadable: {}", fileId, downloadable);
            
            if (!downloadable) {
                log.warn("文件不可下载 - 文件ID: {}, 状态: {}", fileId, fileInfo.get("status"));
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            // 检查物理文件是否存在
            boolean physicalFileExists = Boolean.TRUE.equals(fileInfo.get("physicalFileExists"));
            if (!physicalFileExists) {
                log.warn("物理文件不存在 - 文件ID: {}", fileId);
                return ResponseEntity.notFound().build();
            }
            
            // 下载文件内容
            log.debug("开始下载文件内容 - 文件ID: {}", fileId);
            byte[] fileContent = fileService.downloadFileByFileId(fileId);
            log.debug("文件内容下载完成 - 文件ID: {}, 文件大小: {} bytes", fileId, fileContent.length);
            
            // 处理中文文件名编码
            String originalFileName = fileInfo.get("originalName").toString();
            String encodedFileName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.set("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
            headers.setContentLength(fileContent.length);
            
            log.info("通过文件ID下载成功 - 文件ID: {}, 文件名: {}, 文件大小: {} bytes", 
                     fileId, originalFileName, fileContent.length);
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("通过文件ID下载失败 - 文件ID: {}, 错误类型: {}, 错误信息: {}", 
                     fileId, e.getClass().getSimpleName(), e.getMessage(), e);
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
     * 通过文件ID生成签名文件URL（用于前端图片访问）
     */
    @GetMapping("/signed/generate/by-file-id/{fileId}")
    public ResponseEntity<?> generateSignedFileUrlByFileId(@PathVariable Long fileId) {
        try {
            String signedUrl = fileService.generateSignedFileUrlByFileId(fileId);
            return ResponseUtil.success("通过文件ID生成签名URL成功", Map.of(
                "signedUrl", signedUrl,
                "fileId", fileId
            ));
        } catch (Exception e) {
            return ResponseUtil.error("通过文件ID生成签名URL失败: " + e.getMessage());
        }
    }
    
    /**
     * 通过签名URL下载文件（无需认证）- 使用加密token
     */
    @GetMapping("/signed/")
    public ResponseEntity<byte[]> downloadFileWithSignature(@RequestParam String token) {
        log.info("开始通过token下载文件 - token: {}", token);
        
        try {
            // 验证token
            UrlSigner.SignatureVerificationResult verificationResult = urlSigner.verifySignature(token);
            
            if (!verificationResult.isValid()) {
                log.warn("token验证失败 - 原因: {}", verificationResult.getMessage());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            Long resourceId = verificationResult.getResourceId();
            String resourceType = verificationResult.getResourceType();
            
            byte[] fileContent;
            Map<String, Object> fileInfo;
            
            if ("attachment".equals(resourceType)) {
                // 通过附件ID下载
                fileContent = fileService.downloadFile(resourceId);
                fileInfo = fileService.getFileInfo(resourceId);
            } else if ("file".equals(resourceType)) {
                // 通过文件ID下载
                fileContent = fileService.downloadFileByFileId(resourceId);
                fileInfo = fileService.getFileInfoByFileId(resourceId);
            } else {
                log.warn("不支持的资源类型 - 资源类型: {}, 资源ID: {}", resourceType, resourceId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            // 获取文件信息以设置正确的Content-Type
            String mimeType = (String) fileInfo.get("mimeType");
            String originalName = (String) fileInfo.get("originalName");
            
            // 处理中文文件名编码
            String encodedFileName = URLEncoder.encode(originalName, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(mimeType != null ? mimeType : "application/octet-stream"));
            headers.set("Content-Disposition", "inline; filename*=UTF-8''" + encodedFileName);
            headers.setContentLength(fileContent.length);
            
            log.info("通过token下载文件成功 - 资源类型: {}, 资源ID: {}, 文件名: {}, 文件大小: {} bytes", 
                     resourceType, resourceId, originalName, fileContent.length);
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("通过token下载文件失败 - token: {}, 错误类型: {}, 错误信息: {}", 
                     token, e.getClass().getSimpleName(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    // ==================== 附件编辑功能 ====================
    
    /**
     * 更新附件信息
     * 支持更新描述、标签、公开状态等元数据，以及更换文件关联
     */
    @PutMapping("/attachment/update")
    public ResponseEntity<?> updateAttachment(@RequestBody AttachmentUpdateDTO updateDTO) {
        try {
            log.info("更新附件信息 - 附件ID: {}, 用户ID: {}", updateDTO.getAttachmentId(), updateDTO.getUserId());
            
            boolean success = fileService.updateAttachment(updateDTO);
            
            if (success) {
                log.info("附件更新成功 - 附件ID: {}", updateDTO.getAttachmentId());
                return ResponseUtil.success("附件更新成功", Map.of("success", true));
            } else {
                log.warn("附件更新失败 - 附件ID: {}", updateDTO.getAttachmentId());
                return ResponseUtil.error("附件更新失败");
            }
            
        } catch (Exception e) {
            log.error("附件更新失败 - 附件ID: {}, 用户ID: {}, 错误: {}", 
                    updateDTO.getAttachmentId(), updateDTO.getUserId(), e.getMessage());
            return ResponseUtil.error("附件更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 更换附件关联的文件
     * 支持后期更改文件关联，自动处理引用计数
     */
    @PutMapping("/attachment/change-file")
    public ResponseEntity<?> changeAttachmentFile(@RequestBody Map<String, Object> request) {
        try {
            Long attachmentId = Long.valueOf(request.get("attachmentId").toString());
            Long userId = Long.valueOf(request.get("userId").toString());
            Long newFileId = Long.valueOf(request.get("newFileId").toString());
            
            log.info("更换附件文件 - 附件ID: {}, 用户ID: {}, 新文件ID: {}", attachmentId, userId, newFileId);
            
            boolean success = fileService.changeAttachmentFile(attachmentId, userId, newFileId);
            
            if (success) {
                log.info("附件文件更换成功 - 附件ID: {}", attachmentId);
                return ResponseUtil.success("附件文件更换成功", Map.of("success", true));
            } else {
                log.warn("附件文件更换失败 - 附件ID: {}", attachmentId);
                return ResponseUtil.error("附件文件更换失败");
            }
            
        } catch (Exception e) {
            log.error("附件文件更换失败 - 附件ID: {}, 用户ID: {}, 新文件ID: {}, 错误: {}", 
                    request.get("attachmentId"), request.get("userId"), request.get("newFileId"), e.getMessage());
            return ResponseUtil.error("附件文件更换失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新附件业务信息
     * 支持更改业务类型和业务ID
     */
    @PutMapping("/attachment/update-business")
    public ResponseEntity<?> updateAttachmentBusinessInfo(@RequestBody Map<String, Object> request) {
        try {
            Long attachmentId = Long.valueOf(request.get("attachmentId").toString());
            Long userId = Long.valueOf(request.get("userId").toString());
            String businessType = (String) request.get("businessType");
            Long businessId = Long.valueOf(request.get("businessId").toString());
            
            log.info("更新附件业务信息 - 附件ID: {}, 用户ID: {}, 业务类型: {}, 业务ID: {}", 
                    attachmentId, userId, businessType, businessId);
            
            boolean success = fileService.updateAttachmentBusinessInfo(attachmentId, userId, businessType, businessId);
            
            if (success) {
                log.info("附件业务信息更新成功 - 附件ID: {}", attachmentId);
                return ResponseUtil.success("附件业务信息更新成功", Map.of("success", true));
            } else {
                log.warn("附件业务信息更新失败 - 附件ID: {}", attachmentId);
                return ResponseUtil.error("附件业务信息更新失败");
            }
            
        } catch (Exception e) {
            log.error("附件业务信息更新失败 - 附件ID: {}, 用户ID: {}, 错误: {}", 
                    request.get("attachmentId"), request.get("userId"), e.getMessage());
            return ResponseUtil.error("附件业务信息更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量更新附件状态
     * 支持批量更新附件状态（正常、删除、过期等）
     */
    @PutMapping("/attachment/batch-update-status")
    public ResponseEntity<?> batchUpdateAttachmentStatus(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> attachmentIds = (List<Long>) request.get("attachmentIds");
            Long userId = Long.valueOf(request.get("userId").toString());
            Integer status = Integer.valueOf(request.get("status").toString());
            
            log.info("批量更新附件状态 - 附件数量: {}, 用户ID: {}, 新状态: {}", 
                    attachmentIds.size(), userId, status);
            
            boolean success = fileService.batchUpdateAttachmentStatus(attachmentIds, userId, status);
            
            if (success) {
                log.info("批量更新附件状态成功 - 附件数量: {}", attachmentIds.size());
                return ResponseUtil.success("批量更新附件状态成功", Map.of("success", true));
            } else {
                log.warn("批量更新附件状态失败 - 附件数量: {}", attachmentIds.size());
                return ResponseUtil.error("批量更新附件状态失败");
            }
            
        } catch (Exception e) {
            log.error("批量更新附件状态失败 - 用户ID: {}, 错误: {}", request.get("userId"), e.getMessage());
            return ResponseUtil.error("批量更新附件状态失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取附件编辑历史
     * 记录附件的编辑操作历史
     */
    @GetMapping("/attachment/{attachmentId}/history")
    public ResponseEntity<?> getAttachmentEditHistory(@PathVariable Long attachmentId) {
        try {
            log.info("获取附件编辑历史 - 附件ID: {}", attachmentId);
            
            List<Map<String, Object>> history = fileService.getAttachmentEditHistory(attachmentId);
            
            log.info("获取附件编辑历史成功 - 附件ID: {}, 历史记录数: {}", attachmentId, history.size());
            return ResponseUtil.success("获取附件编辑历史成功", history);
            
        } catch (Exception e) {
            log.error("获取附件编辑历史失败 - 附件ID: {}, 错误: {}", attachmentId, e.getMessage());
            return ResponseUtil.error("获取附件编辑历史失败: " + e.getMessage());
        }
    }
    
    // ==================== 附件删除功能 ====================
    
    /**
     * 只删除附件记录，不删除物理文件
     * 适用于需要保留文件但删除业务关联的场景
     */
    @DeleteMapping("/attachment/{attachmentId}/only")
    public ResponseEntity<?> deleteAttachmentOnly(@PathVariable Long attachmentId) {
        try {
            log.info("只删除附件记录（保留物理文件）- 附件ID: {}", attachmentId);
            
            boolean success = fileService.deleteAttachmentOnly(attachmentId);
            
            if (success) {
                log.info("附件记录删除成功（保留物理文件）- 附件ID: {}", attachmentId);
                return ResponseUtil.success("附件记录删除成功（保留物理文件）", Map.of("success", true));
            } else {
                log.warn("附件记录删除失败 - 附件ID: {}", attachmentId);
                return ResponseUtil.error("附件记录删除失败");
            }
            
        } catch (Exception e) {
            log.error("附件记录删除失败 - 附件ID: {}, 错误: {}", attachmentId, e.getMessage());
            return ResponseUtil.error("附件记录删除失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量只删除附件记录，不删除物理文件
     */
    @DeleteMapping("/attachment/batch-delete-only")
    public ResponseEntity<?> batchDeleteAttachmentsOnly(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> attachmentIds = (List<Long>) request.get("attachmentIds");
            
            log.info("批量只删除附件记录（保留物理文件）- 附件数量: {}", attachmentIds.size());
            
            boolean success = fileService.batchDeleteAttachmentsOnly(attachmentIds);
            
            if (success) {
                log.info("批量附件记录删除成功（保留物理文件）- 附件数量: {}", attachmentIds.size());
                return ResponseUtil.success("批量附件记录删除成功（保留物理文件）", Map.of("success", true));
            } else {
                log.warn("批量附件记录删除失败 - 附件数量: {}", attachmentIds.size());
                return ResponseUtil.error("批量附件记录删除失败");
            }
            
        } catch (Exception e) {
            log.error("批量附件记录删除失败 - 错误: {}", e.getMessage());
            return ResponseUtil.error("批量附件记录删除失败: " + e.getMessage());
        }
    }
}
