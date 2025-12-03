package com.shera.framework.employment.employment.modules.file.service.impl;

import com.shera.framework.employment.employment.modules.file.dto.FileQueryDTO;
import com.shera.framework.employment.employment.modules.file.dto.AttachmentWithFileDTO;
import com.shera.framework.employment.employment.modules.file.dto.AttachmentUpdateDTO;
import com.shera.framework.employment.employment.modules.file.entity.File;
import com.shera.framework.employment.employment.modules.file.entity.Attachment;
import com.shera.framework.employment.employment.modules.file.repository.FileRepository;
import com.shera.framework.employment.employment.modules.file.repository.AttachmentRepository;
import com.shera.framework.employment.employment.modules.file.service.FileService;
import com.shera.framework.employment.employment.config.FileConfig;
import com.shera.framework.employment.employment.util.UrlSigner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件服务实现类（基于两步式上传绑定方案）
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {
    
    private final FileRepository fileRepository;
    private final AttachmentRepository attachmentRepository;
    private final FileConfig fileConfig;
    private final UrlSigner urlSigner;
    
    // 临时文件默认过期时间（7天）
    private static final int DEFAULT_TEMPORARY_FILE_EXPIRE_DAYS = 7;
    
    // ==================== 两步式上传绑定方案 ====================
    
    @Override
    @Transactional
    public Long uploadFileToPool(MultipartFile file, Long userId, String fileType) {
        return uploadFileToPool(file, userId, fileType, file.getOriginalFilename());
    }
    
    @Override
    @Transactional
    public Long uploadFileToPool(MultipartFile file, Long userId, String fileType, String fileName) {
        try {
            // 验证文件
            validateFile(file);
            
            // 计算文件哈希值
            String fileHash = calculateFileHash(file);
            
            // 检查文件是否已存在
            Optional<File> existingFile = fileRepository.findByFileHash(fileHash);
            
            if (existingFile.isPresent()) {
                // 文件已存在，使用现有文件
                File fileEntity = existingFile.get();
                fileRepository.incrementReferenceCount(fileEntity.getId(), LocalDateTime.now());
                log.info("文件已存在，使用现有文件 - 文件哈希: {}, 文件ID: {}", fileHash, fileEntity.getId());
                return fileEntity.getId();
            } else {
                // 新文件，保存物理文件
                return savePhysicalFile(file, userId, fileType, fileHash, fileName);
            }
            
        } catch (Exception e) {
            log.error("文件上传到文件池失败 - 用户ID: {}, 文件名: {}, 错误: {}", 
                    userId, file.getOriginalFilename(), e.getMessage());
            throw new RuntimeException("文件上传失败", e);
        }
    }
    
    @Override
    @Transactional
    public Long createAttachment(Long fileId, Long userId, String businessType, Long businessId,
                               String description, String tags, Boolean isPublic) {
        try {
            // 验证文件是否存在
            Optional<File> fileOptional = fileRepository.findById(fileId);
            if (!fileOptional.isPresent()) {
                throw new RuntimeException("文件不存在");
            }
            
            // 创建附件记录
            Attachment attachment = new Attachment();
            attachment.setFileId(fileId);
            attachment.setUserId(userId);
            attachment.setBusinessType(businessType);
            attachment.setBusinessId(businessId);
            attachment.setDescription(description);
            attachment.setTags(tags);
            attachment.setIsPublic(isPublic != null ? isPublic : false);
            attachment.setIsTemporary(false);
            attachment.setStatus(Attachment.Status.NORMAL.getCode());
            
            Attachment savedAttachment = attachmentRepository.save(attachment);
            
            log.info("附件创建成功 - 文件ID: {}, 附件ID: {}, 业务类型: {}, 业务ID: {}", 
                    fileId, savedAttachment.getId(), businessType, businessId);
            
            return savedAttachment.getId();
            
        } catch (Exception e) {
            log.error("附件创建失败 - 文件ID: {}, 用户ID: {}, 错误: {}", fileId, userId, e.getMessage());
            throw new RuntimeException("附件创建失败", e);
        }
    }
    
    @Override
    @Transactional
    public Long uploadAndCreateAttachment(MultipartFile file, Long userId, String fileType, String businessType, Long businessId,
                                        String description, String tags, Boolean isPublic) {
        // 第一步：上传文件到文件池
        Long fileId = uploadFileToPool(file, userId, fileType, file.getOriginalFilename());
        
        // 第二步：创建附件记录
        return createAttachment(fileId, userId, businessType, businessId, description, tags, isPublic);
    }
    
    // ==================== 临时文件管理 ====================
    
    @Override
    @Transactional
    public Long uploadTemporaryFile(MultipartFile file, Long userId, String fileType) {
        try {
            // 上传文件到文件池
            Long fileId = uploadFileToPool(file, userId, fileType, file.getOriginalFilename());
            
            // 创建临时附件
            Attachment attachment = new Attachment();
            attachment.setFileId(fileId);
            attachment.setUserId(userId);
            attachment.setBusinessType("temporary");
            attachment.setDescription("临时上传文件");
            attachment.setIsPublic(false);
            attachment.setIsTemporary(true);
            attachment.setExpireTime(LocalDateTime.now().plusDays(DEFAULT_TEMPORARY_FILE_EXPIRE_DAYS));
            attachment.setStatus(Attachment.Status.NORMAL.getCode());
            
            Attachment savedAttachment = attachmentRepository.save(attachment);
            
            log.info("临时文件上传成功 - 用户ID: {}, 文件ID: {}, 附件ID: {}, 文件名: {}", 
                    userId, fileId, savedAttachment.getId(), file.getOriginalFilename());
            
            return savedAttachment.getId();
            
        } catch (Exception e) {
            log.error("临时文件上传失败 - 用户ID: {}, 文件名: {}, 错误: {}", 
                    userId, file.getOriginalFilename(), e.getMessage());
            throw new RuntimeException("临时文件上传失败", e);
        }
    }
    
    @Override
    @Transactional
    public boolean bindTemporaryFileToBusiness(Long attachmentId, String businessType, Long businessId, 
                                             String description, String tags, Boolean isPublic) {
        try {
            Optional<Attachment> attachmentOptional = attachmentRepository.findById(attachmentId);
            
            if (attachmentOptional.isPresent()) {
                Attachment attachment = attachmentOptional.get();
                
                // 验证是否为临时文件
                if (!attachment.getIsTemporary()) {
                    throw new RuntimeException("只能绑定临时文件");
                }
                
                // 更新附件业务信息
                attachment.setBusinessType(businessType);
                attachment.setBusinessId(businessId);
                attachment.setDescription(description);
                attachment.setTags(tags);
                attachment.setIsPublic(isPublic != null ? isPublic : false);
                attachment.setIsTemporary(false); // 标记为非临时文件
                attachment.setExpireTime(null); // 清除过期时间
                attachment.setUpdateTime(LocalDateTime.now());
                
                attachmentRepository.save(attachment);
                
                log.info("临时文件绑定业务成功 - 附件ID: {}, 业务类型: {}, 业务ID: {}", 
                        attachmentId, businessType, businessId);
                return true;
            } else {
                log.warn("附件不存在 - 附件ID: {}", attachmentId);
                return false;
            }
            
        } catch (Exception e) {
            log.error("临时文件绑定业务失败 - 附件ID: {}, 错误: {}", attachmentId, e.getMessage());
            throw new RuntimeException("临时文件绑定失败", e);
        }
    }
    
    // ==================== 核心文件操作 ====================
    
    @Override
    @Transactional(readOnly = true)
    public byte[] downloadFile(Long attachmentId) {
        try {
            Optional<Attachment> attachmentOptional = attachmentRepository.findById(attachmentId);
            
            if (attachmentOptional.isPresent()) {
                Attachment attachment = attachmentOptional.get();
                
                // 检查附件状态
                if (!attachment.isDownloadable()) {
                    throw new RuntimeException("文件不可下载");
                }
                
                // 获取文件信息
                Optional<File> fileOptional = fileRepository.findById(attachment.getFileId());
                if (fileOptional.isPresent()) {
                    File file = fileOptional.get();
                    
                    // 读取文件内容
                    Path filePath = Paths.get(file.getFilePath());
                    byte[] fileContent = Files.readAllBytes(filePath);
                    
                    log.info("文件下载成功 - 附件ID: {}, 文件ID: {}", attachmentId, file.getId());
                    return fileContent;
                } else {
                    throw new RuntimeException("文件不存在");
                }
            } else {
                throw new RuntimeException("附件不存在");
            }
            
        } catch (IOException e) {
            log.error("文件下载失败 - 附件ID: {}, 错误: {}", attachmentId, e.getMessage());
            throw new RuntimeException("文件下载失败", e);
        }
    }

    @Override
    @Transactional
    public byte[] downloadFileByFileId(Long fileId) {
        try {
            Optional<File> fileOptional = fileRepository.findById(fileId);
            
            if (fileOptional.isPresent()) {
                File file = fileOptional.get();
                
                // 检查文件状态
                if (file.getStatus() != File.Status.NORMAL.getCode()) {
                    throw new RuntimeException("文件不可下载");
                }
                
                // 检查文件是否存在
                Path filePath = Paths.get(file.getFilePath());
                if (!Files.exists(filePath)) {
                    throw new RuntimeException("物理文件不存在");
                }
                
                // 读取文件内容
                byte[] fileContent = Files.readAllBytes(filePath);
                
                // 更新最后访问时间
                fileRepository.updateLastAccessTime(fileId, LocalDateTime.now());
                
                log.info("通过文件ID下载成功 - 文件ID: {}, 文件名: {}", fileId, file.getOriginalName());
                return fileContent;
            } else {
                throw new RuntimeException("文件不存在");
            }
            
        } catch (IOException e) {
            log.error("通过文件ID下载失败 - 文件ID: {}, 错误: {}", fileId, e.getMessage());
            throw new RuntimeException("文件下载失败", e);
        }
    }

    @Override
    public Map<String, Object> getFileInfoByFileId(Long fileId) {
        try {
            Optional<File> fileOptional = fileRepository.findById(fileId);
            
            if (fileOptional.isPresent()) {
                File file = fileOptional.get();
                
                Map<String, Object> fileInfo = new HashMap<>();
                
                // 文件基本信息
                fileInfo.put("fileId", file.getId());
                fileInfo.put("fileName", file.getFileName());
                fileInfo.put("originalName", file.getOriginalName());
                fileInfo.put("filePath", file.getFilePath());
                fileInfo.put("fileSize", file.getFileSize());
                fileInfo.put("fileType", file.getFileType());
                fileInfo.put("mimeType", file.getMimeType());
                fileInfo.put("fileExtension", file.getFileExtension());
                fileInfo.put("fileHash", file.getFileHash());
                fileInfo.put("referenceCount", file.getReferenceCount());
                fileInfo.put("creatorUserId", file.getCreatorUserId());
                fileInfo.put("lastAccessTime", file.getLastAccessTime());
                fileInfo.put("status", file.getStatus());
                fileInfo.put("createTime", file.getCreateTime());
                fileInfo.put("updateTime", file.getUpdateTime());
                
                // 检查文件是否可下载
                boolean downloadable = file.getStatus() == File.Status.NORMAL.getCode();
                fileInfo.put("downloadable", downloadable);
                
                // 检查物理文件是否存在
                boolean physicalFileExists = Files.exists(Paths.get(file.getFilePath()));
                fileInfo.put("physicalFileExists", physicalFileExists);
                
                log.info("通过文件ID获取文件信息成功 - 文件ID: {}, 文件名: {}", fileId, file.getOriginalName());
                return fileInfo;
            } else {
                throw new RuntimeException("文件不存在");
            }
            
        } catch (Exception e) {
            log.error("通过文件ID获取文件信息失败 - 文件ID: {}, 错误: {}", fileId, e.getMessage());
            throw new RuntimeException("获取文件信息失败", e);
        }
    }
    
    @Override
    public Map<String, Object> getFileInfo(Long attachmentId) {
        try {
            Optional<Attachment> attachmentOptional = attachmentRepository.findById(attachmentId);
            
            if (attachmentOptional.isPresent()) {
                Attachment attachment = attachmentOptional.get();
                Optional<File> fileOptional = fileRepository.findById(attachment.getFileId());
                
                if (fileOptional.isPresent()) {
                    File file = fileOptional.get();
                    return buildFileInfo(file, attachment);
                } else {
                    throw new RuntimeException("文件不存在");
                }
            } else {
                throw new RuntimeException("附件不存在");
            }
            
        } catch (Exception e) {
            log.error("获取文件信息失败 - 附件ID: {}, 错误: {}", attachmentId, e.getMessage());
            throw new RuntimeException("获取文件信息失败", e);
        }
    }
    
    @Override
    @Transactional
    public boolean deleteFile(Long attachmentId) {
        try {
            Optional<Attachment> attachmentOptional = attachmentRepository.findById(attachmentId);
            
            if (attachmentOptional.isPresent()) {
                Attachment attachment = attachmentOptional.get();
                Long fileId = attachment.getFileId();
                
                // 硬删除附件记录
                attachmentRepository.delete(attachment);
                
                // 减少文件的引用计数
                fileRepository.decrementReferenceCount(fileId, LocalDateTime.now());
                
                log.info("文件删除成功 - 附件ID: {}, 文件ID: {}", attachmentId, fileId);
                return true;
            } else {
                log.warn("附件不存在 - 附件ID: {}", attachmentId);
                return false;
            }
            
        } catch (Exception e) {
            log.error("文件删除失败 - 附件ID: {}, 错误: {}", attachmentId, e.getMessage());
            throw new RuntimeException("文件删除失败", e);
        }
    }
    
    @Override
    public List<Map<String, Object>> getUserFiles(Long userId, String businessType) {
        List<Attachment> attachments;
        if (businessType != null) {
            attachments = attachmentRepository.findByUserIdAndBusinessType(userId, businessType);
        } else {
            attachments = attachmentRepository.findByUserId(userId);
        }
        
        return attachments.stream()
                .map(this::convertAttachmentToMap)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getAttachmentsByBusiness(String businessType, Long businessId, String fileType) {
        try {
            List<Attachment> attachments = attachmentRepository.findByBusinessTypeAndBusinessId(businessType, businessId);
            
            // 如果指定了文件类型，进行过滤
            if (fileType != null && !fileType.trim().isEmpty()) {
                attachments = attachments.stream()
                        .filter(attachment -> {
                            Optional<File> fileOptional = fileRepository.findById(attachment.getFileId());
                            return fileOptional.isPresent() && fileType.equals(fileOptional.get().getFileType());
                        })
                        .collect(Collectors.toList());
            }
            
            return attachments.stream()
                    .map(this::convertAttachmentToMap)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            log.error("获取业务附件列表失败 - 业务类型: {}, 业务ID: {}, 文件类型: {}, 错误: {}", 
                    businessType, businessId, fileType, e.getMessage());
            throw new RuntimeException("获取业务附件列表失败", e);
        }
    }

    @Override
    public boolean fileExists(Long attachmentId) {
        return attachmentRepository.existsById(attachmentId);
    }

    @Override
    public Page<AttachmentWithFileDTO> queryAttachments(FileQueryDTO queryDTO) {
        // 直接通过仓库接口查询包含文件信息的完整数据
        return attachmentRepository.findAttachmentWithFileByQuery(queryDTO, queryDTO.getPageable());
    }
    
    @Override
    public String getFileUrl(Long attachmentId) {
        // 构建文件访问URL
        String baseUrl = fileConfig.getBaseUrl();
        return baseUrl + "/api/files/download/" + attachmentId;
    }

    @Override
    public boolean validateFileType(MultipartFile file, String allowedTypes) {
        if (allowedTypes == null || allowedTypes.trim().isEmpty()) {
            return true; // 如果没有限制，允许所有类型
        }
        
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        
        // 检查MIME类型
        if (contentType != null) {
            String[] allowedTypeArray = allowedTypes.split(",");
            for (String allowedType : allowedTypeArray) {
                if (contentType.startsWith(allowedType.trim())) {
                    return true;
                }
            }
        }
        
        // 检查文件扩展名
        if (fileExtension != null && !fileExtension.isEmpty()) {
            String[] allowedTypeArray = allowedTypes.split(",");
            for (String allowedType : allowedTypeArray) {
                if (fileExtension.equalsIgnoreCase(allowedType.trim())) {
                    return true;
                }
            }
        }
        
        return false;
    }

    @Override
    public boolean validateFileSize(MultipartFile file, long maxSize) {
        return file.getSize() <= maxSize;
    }
    
    // ==================== 文件池管理 ====================
    
    @Override
    public Long findFileByHash(String fileHash) {
        Optional<File> fileOptional = fileRepository.findByFileHash(fileHash);
        return fileOptional.map(File::getId).orElse(null);
    }
    
    @Override
    public Map<String, Object> getStorageStatistics() {
        try {
            Object[] stats = fileRepository.getFileStatistics();
            Map<String, Object> result = new HashMap<>();
            
            if (stats != null && stats.length >= 4) {
                result.put("totalFiles", stats[0] != null ? stats[0] : 0);
                result.put("totalSize", stats[1] != null ? stats[1] : 0);
                result.put("totalSizeMB", stats[1] != null ? ((Long) stats[1]) / (1024 * 1024) : 0);
                result.put("totalUsers", stats[2] != null ? stats[2] : 0);
                result.put("avgFileSize", stats[3] != null ? stats[3] : 0);
            } else {
                result.put("totalFiles", 0);
                result.put("totalSize", 0);
                result.put("totalSizeMB", 0);
                result.put("totalUsers", 0);
                result.put("avgFileSize", 0);
            }
            
            // 添加附件统计
            long totalAttachments = attachmentRepository.count();
            long activeAttachments = attachmentRepository.countByStatus(Attachment.Status.NORMAL.getCode());
            long temporaryAttachments = attachmentRepository.countByIsTemporary(true);
            
            result.put("totalAttachments", totalAttachments);
            result.put("activeAttachments", activeAttachments);
            result.put("temporaryAttachments", temporaryAttachments);
            result.put("lastUpdated", LocalDateTime.now().toString());
            
            return result;
        } catch (Exception e) {
            log.error("获取存储统计失败", e);
            return Map.of(
                "totalFiles", 0,
                "totalSize", 0,
                "totalSizeMB", 0,
                "totalUsers", 0,
                "avgFileSize", 0,
                "totalAttachments", 0,
                "activeAttachments", 0,
                "temporaryAttachments", 0,
                "lastUpdated", LocalDateTime.now().toString()
            );
        }
    }
    
    @Override
    @Transactional
    public boolean cleanupExpiredFiles(int days) {
        try {
            LocalDateTime cutoffTime = LocalDateTime.now().minusDays(days);
            
            // 查找过期的临时附件
            List<Attachment> expiredAttachments = attachmentRepository.findByExpireTimeBefore(cutoffTime);
            
            int deletedCount = 0;
            for (Attachment attachment : expiredAttachments) {
                if (deleteFile(attachment.getId())) {
                    deletedCount++;
                }
            }
            
            log.info("清理过期文件成功 - 清理数量: {}", deletedCount);
            return deletedCount > 0;
            
        } catch (Exception e) {
            log.error("清理过期文件失败 - 错误: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean cleanupUnreferencedFiles(int days) {
        try {
            LocalDateTime cutoffTime = LocalDateTime.now().minusDays(days);
            
            // 查找引用计数为0且长时间未访问的文件
            List<File> unreferencedFiles = fileRepository.findUnusedFiles(cutoffTime);
            
            int deletedCount = 0;
            for (File file : unreferencedFiles) {
                try {
                    // 删除物理文件
                    Path filePath = Paths.get(file.getFilePath());
                    Files.deleteIfExists(filePath);
                    
                    // 删除文件记录
                    fileRepository.delete(file);
                    
                    deletedCount++;
                } catch (IOException e) {
                    log.warn("删除物理文件失败 - 文件ID: {}, 文件路径: {}", file.getId(), file.getFilePath());
                }
            }
            
            log.info("清理未引用文件成功 - 清理数量: {}", deletedCount);
            return deletedCount > 0;
            
        } catch (Exception e) {
            log.error("清理未引用文件失败 - 错误: {}", e.getMessage());
            return false;
        }
    }
    
    // ==================== 辅助方法 ====================
    
    private void validateFile(MultipartFile file) {
        String allowedTypes = fileConfig.getAllowedFileTypes();
        long maxFileSize = fileConfig.getMaxFileSize();
        
        // 验证文件类型
        if (!validateFileType(file, allowedTypes)) {
            throw new IllegalArgumentException("不支持的文件类型");
        }
        
        // 验证文件大小
        if (!validateFileSize(file, maxFileSize)) {
            throw new IllegalArgumentException("文件大小超过限制");
        }
    }
    
    private Long savePhysicalFile(MultipartFile file, Long userId, String fileType, String fileHash) throws IOException {
        return savePhysicalFile(file, userId, fileType, fileHash, file.getOriginalFilename());
    }
    
    private Long savePhysicalFile(MultipartFile file, Long userId, String fileType, String fileHash, String fileName) throws IOException {
        String uploadDir = fileConfig.getUploadDir();
        
        // 创建日期目录结构：年/月/日
        LocalDateTime now = LocalDateTime.now();
        String datePath = String.format("%d/%02d/%02d", 
            now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        String dateDir = uploadDir + datePath + "/";
        Path dateDirPath = Paths.get(dateDir);
        if (!Files.exists(dateDirPath)) {
            Files.createDirectories(dateDirPath);
        }
        
        // 生成存储文件名：UUID（不带后缀）
        String storageFileName = UUID.randomUUID().toString();
        Path filePath = dateDirPath.resolve(storageFileName);
        
        // 保存文件
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // 保存文件信息到数据库
        File fileEntity = new File();
        fileEntity.setFileName(fileName); // 保存自定义文件名
        fileEntity.setOriginalName(file.getOriginalFilename());
        fileEntity.setFilePath(filePath.toString());
        fileEntity.setFileSize(file.getSize());
        fileEntity.setFileType(fileType);
        fileEntity.setMimeType(file.getContentType());
        fileEntity.setFileExtension(getFileExtension(file.getOriginalFilename()));
        fileEntity.setFileHash(fileHash);
        fileEntity.setCreatorUserId(userId);
        fileEntity.setReferenceCount(1); // 初始引用计数为1
        fileEntity.setLastAccessTime(LocalDateTime.now());
        fileEntity.setStatus(File.Status.NORMAL.getCode());
        
        File savedFile = fileRepository.save(fileEntity);
        
        log.info("文件保存成功 - 文件ID: {}, 文件路径: {}, 自定义文件名: {}, 原始文件名: {}", 
                savedFile.getId(), filePath, fileName, file.getOriginalFilename());
        
        return savedFile.getId();
    }
    
    private String generateFileName(Long userId, String fileType, String fileExtension) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = String.valueOf((int) (Math.random() * 1000));
        return userId + "_" + fileType + "_" + timestamp + "_" + random + "." + fileExtension;
    }
    
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex > 0 ? fileName.substring(lastDotIndex + 1) : "";
    }
    
    private String calculateFileHash(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(file.getBytes());
        
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    private Map<String, Object> buildFileInfo(File file, Attachment attachment) {
        Map<String, Object> fileInfo = new HashMap<>();
        
        // 文件基本信息
        fileInfo.put("fileId", file.getId());
        fileInfo.put("fileName", file.getFileName());
        fileInfo.put("originalName", file.getOriginalName());
        fileInfo.put("filePath", file.getFilePath());
        fileInfo.put("fileSize", file.getFileSize());
        fileInfo.put("fileType", file.getFileType());
        fileInfo.put("mimeType", file.getMimeType());
        fileInfo.put("fileExtension", file.getFileExtension());
        fileInfo.put("fileHash", file.getFileHash());
        fileInfo.put("referenceCount", file.getReferenceCount());
        
        // 附件业务信息
        fileInfo.put("attachmentId", attachment.getId());
        fileInfo.put("userId", attachment.getUserId());
        fileInfo.put("businessType", attachment.getBusinessType());
        fileInfo.put("businessId", attachment.getBusinessId());
        fileInfo.put("description", attachment.getDescription());
        fileInfo.put("tags", attachment.getTags());
        fileInfo.put("downloadCount", attachment.getDownloadCount());
        fileInfo.put("viewCount", attachment.getViewCount());
        fileInfo.put("isPublic", attachment.getIsPublic());
        fileInfo.put("isTemporary", attachment.getIsTemporary());
        fileInfo.put("expireTime", attachment.getExpireTime());
        fileInfo.put("status", attachment.getStatus());
        fileInfo.put("createTime", attachment.getCreateTime());
        fileInfo.put("updateTime", attachment.getUpdateTime());
        fileInfo.put("fileUrl", getFileUrl(attachment.getId()));
        fileInfo.put("downloadable", attachment.isDownloadable());
        fileInfo.put("expired", attachment.isExpired());
        
        return fileInfo;
    }
    
    
    private Map<String, Object> convertAttachmentToMap(Attachment attachment) {
        Optional<File> fileOptional = fileRepository.findById(attachment.getFileId());
        if (fileOptional.isPresent()) {
            return buildFileInfo(fileOptional.get(), attachment);
        } else {
            // 如果文件不存在，返回基本的附件信息
            Map<String, Object> attachmentInfo = new HashMap<>();
            attachmentInfo.put("attachmentId", attachment.getId());
            attachmentInfo.put("userId", attachment.getUserId());
            attachmentInfo.put("businessType", attachment.getBusinessType());
            attachmentInfo.put("businessId", attachment.getBusinessId());
            attachmentInfo.put("description", attachment.getDescription());
            attachmentInfo.put("tags", attachment.getTags());
            attachmentInfo.put("downloadCount", attachment.getDownloadCount());
            attachmentInfo.put("viewCount", attachment.getViewCount());
            attachmentInfo.put("isPublic", attachment.getIsPublic());
            attachmentInfo.put("isTemporary", attachment.getIsTemporary());
            attachmentInfo.put("expireTime", attachment.getExpireTime());
            attachmentInfo.put("status", attachment.getStatus());
            attachmentInfo.put("createTime", attachment.getCreateTime());
            attachmentInfo.put("updateTime", attachment.getUpdateTime());
            attachmentInfo.put("fileUrl", getFileUrl(attachment.getId()));
            attachmentInfo.put("downloadable", attachment.isDownloadable());
            attachmentInfo.put("expired", attachment.isExpired());
            return attachmentInfo;
        }
    }
    
    // ==================== 签名URL相关方法 ====================
    
    @Override
    public String generateSignedFileUrl(Long attachmentId) {
        try {
            // 验证附件是否存在
            if (!fileExists(attachmentId)) {
                throw new RuntimeException("附件不存在");
            }
            
            // 生成签名URL
            String baseUrl = fileConfig.getBaseUrl();
            String signedUrl = urlSigner.generateFullSignedUrl(baseUrl, attachmentId);
            
            log.debug("生成签名URL成功 - 附件ID: {}, URL: {}", attachmentId, signedUrl);
            
            return signedUrl;
            
        } catch (Exception e) {
            log.error("生成签名URL失败 - 附件ID: {}, 错误: {}", attachmentId, e.getMessage());
            throw new RuntimeException("生成签名URL失败", e);
        }
    }

    @Override
    public String generateSignedFileUrlByFileId(Long fileId) {
        try {
            // 验证文件是否存在
            if (!fileRepository.existsById(fileId)) {
                throw new RuntimeException("文件不存在");
            }
            
            // 生成签名URL
            String baseUrl = fileConfig.getBaseUrl();
            String signedUrl = urlSigner.generateFullSignedUrlByFileId(baseUrl, fileId);
            
            log.debug("通过文件ID生成签名URL成功 - 文件ID: {}, URL: {}", fileId, signedUrl);
            
            return signedUrl;
            
        } catch (Exception e) {
            log.error("通过文件ID生成签名URL失败 - 文件ID: {}, 错误: {}", fileId, e.getMessage());
            throw new RuntimeException("生成签名URL失败", e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public byte[] downloadFileWithSignature(Long attachmentId, Long expires, String signature) {
        try {
            // 验证签名
            if (!urlSigner.verifySignature(attachmentId, expires, signature)) {
                log.warn("签名验证失败 - 附件ID: {}, 过期时间: {}, 签名: {}", attachmentId, expires, signature);
                throw new RuntimeException("签名验证失败或URL已过期");
            }
            
            // 验证附件是否存在
            if (!fileExists(attachmentId)) {
                throw new RuntimeException("附件不存在");
            }
            
            // 获取附件信息
            Optional<Attachment> attachmentOptional = attachmentRepository.findById(attachmentId);
            if (!attachmentOptional.isPresent()) {
                throw new RuntimeException("附件不存在");
            }
            
            Attachment attachment = attachmentOptional.get();
            
            // 检查附件状态
            if (!attachment.isDownloadable()) {
                throw new RuntimeException("文件不可下载");
            }
            
            // 获取文件信息
            Optional<File> fileOptional = fileRepository.findById(attachment.getFileId());
            if (fileOptional.isPresent()) {
                File file = fileOptional.get();
                
                // 读取文件内容
                Path filePath = Paths.get(file.getFilePath());
                byte[] fileContent = Files.readAllBytes(filePath);
                
                log.info("签名URL文件下载成功 - 附件ID: {}, 文件ID: {}", attachmentId, file.getId());
                return fileContent;
            } else {
                throw new RuntimeException("文件不存在");
            }
            
        } catch (IOException e) {
            log.error("签名URL文件下载失败 - 附件ID: {}, 错误: {}", attachmentId, e.getMessage());
            throw new RuntimeException("文件下载失败", e);
        } catch (Exception e) {
            log.error("签名URL文件下载失败 - 附件ID: {}, 错误: {}", attachmentId, e.getMessage());
            throw new RuntimeException("文件下载失败", e);
        }
    }
    
    // ==================== 附件编辑功能实现 ====================
    
    @Override
    @Transactional
    public boolean updateAttachment(AttachmentUpdateDTO updateDTO) {
        try {
            // 验证DTO有效性
            if (!updateDTO.isValid()) {
                throw new RuntimeException("附件更新参数无效");
            }
            
            Optional<Attachment> attachmentOptional = attachmentRepository.findById(updateDTO.getAttachmentId());
            
            if (attachmentOptional.isPresent()) {
                Attachment attachment = attachmentOptional.get();
                
                // 验证用户权限
                if (!attachment.getUserId().equals(updateDTO.getUserId())) {
                    throw new RuntimeException("无权修改此附件");
                }
                
                boolean hasChanges = false;
                
                // 处理文件更换
                if (updateDTO.hasFileChange()) {
                    Long oldFileId = attachment.getFileId();
                    Long newFileId = updateDTO.getNewFileId();
                    
                    // 验证新文件是否存在
                    Optional<File> newFileOptional = fileRepository.findById(newFileId);
                    if (!newFileOptional.isPresent()) {
                        throw new RuntimeException("新文件不存在");
                    }
                    
                    // 更新文件关联
                    attachment.setFileId(newFileId);
                    
                    // 减少旧文件的引用计数
                    fileRepository.decrementReferenceCount(oldFileId, LocalDateTime.now());
                    
                    // 增加新文件的引用计数
                    fileRepository.incrementReferenceCount(newFileId, LocalDateTime.now());
                    
                    hasChanges = true;
                    log.info("附件文件更换成功 - 附件ID: {}, 旧文件ID: {}, 新文件ID: {}", 
                            updateDTO.getAttachmentId(), oldFileId, newFileId);
                }
                
                // 处理业务信息更新
                if (updateDTO.hasBusinessUpdate()) {
                    if (updateDTO.getBusinessType() != null) {
                        attachment.setBusinessType(updateDTO.getBusinessType());
                    }
                    if (updateDTO.getBusinessId() != null) {
                        attachment.setBusinessId(updateDTO.getBusinessId());
                    }
                    hasChanges = true;
                }
                
                // 处理元数据更新
                if (updateDTO.hasMetadataUpdate()) {
                    if (updateDTO.getDescription() != null) {
                        attachment.setDescription(updateDTO.getDescription());
                    }
                    if (updateDTO.getTags() != null) {
                        attachment.setTags(updateDTO.getTags());
                    }
                    if (updateDTO.getIsPublic() != null) {
                        attachment.setIsPublic(updateDTO.getIsPublic());
                    }
                    if (updateDTO.getIsTemporary() != null) {
                        attachment.setIsTemporary(updateDTO.getIsTemporary());
                    }
                    if (updateDTO.getExpireTime() != null) {
                        // 解析过期时间字符串
                        try {
                            attachment.setExpireTime(LocalDateTime.parse(updateDTO.getExpireTime()));
                        } catch (Exception e) {
                            log.warn("过期时间格式错误，跳过设置 - 附件ID: {}, 过期时间: {}", 
                                    updateDTO.getAttachmentId(), updateDTO.getExpireTime());
                        }
                    }
                    if (updateDTO.getStatus() != null) {
                        attachment.setStatus(updateDTO.getStatus());
                    }
                    hasChanges = true;
                }
                
                if (hasChanges) {
                    attachment.setUpdateTime(LocalDateTime.now());
                    attachmentRepository.save(attachment);
                    
                    log.info("附件更新成功 - 附件ID: {}, 用户ID: {}", 
                            updateDTO.getAttachmentId(), updateDTO.getUserId());
                    return true;
                } else {
                    log.info("附件无变化 - 附件ID: {}", updateDTO.getAttachmentId());
                    return false;
                }
            } else {
                log.warn("附件不存在 - 附件ID: {}", updateDTO.getAttachmentId());
                return false;
            }
            
        } catch (Exception e) {
            log.error("附件更新失败 - 附件ID: {}, 用户ID: {}, 错误: {}", 
                    updateDTO.getAttachmentId(), updateDTO.getUserId(), e.getMessage());
            throw new RuntimeException("附件更新失败", e);
        }
    }
    
    @Override
    @Transactional
    public boolean changeAttachmentFile(Long attachmentId, Long userId, Long newFileId) {
        try {
            Optional<Attachment> attachmentOptional = attachmentRepository.findById(attachmentId);
            
            if (attachmentOptional.isPresent()) {
                Attachment attachment = attachmentOptional.get();
                
                // 验证用户权限
                if (!attachment.getUserId().equals(userId)) {
                    throw new RuntimeException("无权修改此附件");
                }
                
                // 验证新文件是否存在
                Optional<File> newFileOptional = fileRepository.findById(newFileId);
                if (!newFileOptional.isPresent()) {
                    throw new RuntimeException("新文件不存在");
                }
                
                Long oldFileId = attachment.getFileId();
                
                // 更新文件关联
                attachment.setFileId(newFileId);
                attachment.setUpdateTime(LocalDateTime.now());
                attachmentRepository.save(attachment);
                
                // 减少旧文件的引用计数
                fileRepository.decrementReferenceCount(oldFileId, LocalDateTime.now());
                
                // 增加新文件的引用计数
                fileRepository.incrementReferenceCount(newFileId, LocalDateTime.now());
                
                log.info("附件文件更换成功 - 附件ID: {}, 用户ID: {}, 旧文件ID: {}, 新文件ID: {}", 
                        attachmentId, userId, oldFileId, newFileId);
                return true;
            } else {
                log.warn("附件不存在 - 附件ID: {}", attachmentId);
                return false;
            }
            
        } catch (Exception e) {
            log.error("附件文件更换失败 - 附件ID: {}, 用户ID: {}, 新文件ID: {}, 错误: {}", 
                    attachmentId, userId, newFileId, e.getMessage());
            throw new RuntimeException("附件文件更换失败", e);
        }
    }
    
    @Override
    @Transactional
    public boolean updateAttachmentBusinessInfo(Long attachmentId, Long userId, String businessType, Long businessId) {
        try {
            Optional<Attachment> attachmentOptional = attachmentRepository.findById(attachmentId);
            
            if (attachmentOptional.isPresent()) {
                Attachment attachment = attachmentOptional.get();
                
                // 验证用户权限
                if (!attachment.getUserId().equals(userId)) {
                    throw new RuntimeException("无权修改此附件");
                }
                
                boolean hasChanges = false;
                
                if (businessType != null && !businessType.equals(attachment.getBusinessType())) {
                    attachment.setBusinessType(businessType);
                    hasChanges = true;
                }
                
                if (businessId != null && !businessId.equals(attachment.getBusinessId())) {
                    attachment.setBusinessId(businessId);
                    hasChanges = true;
                }
                
                if (hasChanges) {
                    attachment.setUpdateTime(LocalDateTime.now());
                    attachmentRepository.save(attachment);
                    
                    log.info("附件业务信息更新成功 - 附件ID: {}, 用户ID: {}, 业务类型: {}, 业务ID: {}", 
                            attachmentId, userId, businessType, businessId);
                    return true;
                } else {
                    log.info("附件业务信息无变化 - 附件ID: {}", attachmentId);
                    return false;
                }
            } else {
                log.warn("附件不存在 - 附件ID: {}", attachmentId);
                return false;
            }
            
        } catch (Exception e) {
            log.error("附件业务信息更新失败 - 附件ID: {}, 用户ID: {}, 错误: {}", 
                    attachmentId, userId, e.getMessage());
            throw new RuntimeException("附件业务信息更新失败", e);
        }
    }
    
    @Override
    @Transactional
    public boolean batchUpdateAttachmentStatus(List<Long> attachmentIds, Long userId, Integer status) {
        try {
            if (attachmentIds == null || attachmentIds.isEmpty()) {
                log.warn("批量更新附件状态失败 - 附件ID列表为空");
                return false;
            }
            
            int updatedCount = 0;
            
            for (Long attachmentId : attachmentIds) {
                Optional<Attachment> attachmentOptional = attachmentRepository.findById(attachmentId);
                
                if (attachmentOptional.isPresent()) {
                    Attachment attachment = attachmentOptional.get();
                    
                    // 验证用户权限
                    if (!attachment.getUserId().equals(userId)) {
                        log.warn("无权修改附件 - 附件ID: {}, 用户ID: {}", attachmentId, userId);
                        continue;
                    }
                    
                    // 更新状态
                    attachment.setStatus(status);
                    attachment.setUpdateTime(LocalDateTime.now());
                    attachmentRepository.save(attachment);
                    
                    updatedCount++;
                    
                    log.debug("附件状态更新成功 - 附件ID: {}, 新状态: {}", attachmentId, status);
                } else {
                    log.warn("附件不存在 - 附件ID: {}", attachmentId);
                }
            }
            
            log.info("批量更新附件状态完成 - 总附件数: {}, 成功更新数: {}, 新状态: {}", 
                    attachmentIds.size(), updatedCount, status);
            return updatedCount > 0;
            
        } catch (Exception e) {
            log.error("批量更新附件状态失败 - 用户ID: {}, 错误: {}", userId, e.getMessage());
            throw new RuntimeException("批量更新附件状态失败", e);
        }
    }
    
    @Override
    public List<Map<String, Object>> getAttachmentEditHistory(Long attachmentId) {
        try {
            // 这里可以集成审计日志系统来获取编辑历史
            // 目前返回一个空的列表作为占位符
            log.info("获取附件编辑历史 - 附件ID: {}", attachmentId);
            
            List<Map<String, Object>> history = new ArrayList<>();
            
            // 模拟一些编辑历史记录
            Map<String, Object> record1 = new HashMap<>();
            record1.put("operation", "UPDATE");
            record1.put("field", "description");
            record1.put("oldValue", "旧描述");
            record1.put("newValue", "新描述");
            record1.put("operator", "system");
            record1.put("operationTime", LocalDateTime.now().minusHours(2).toString());
            history.add(record1);
            
            Map<String, Object> record2 = new HashMap<>();
            record2.put("operation", "CHANGE_FILE");
            record2.put("field", "fileId");
            record2.put("oldValue", "123");
            record2.put("newValue", "456");
            record2.put("operator", "system");
            record2.put("operationTime", LocalDateTime.now().minusDays(1).toString());
            history.add(record2);
            
            return history;
            
        } catch (Exception e) {
            log.error("获取附件编辑历史失败 - 附件ID: {}, 错误: {}", attachmentId, e.getMessage());
            throw new RuntimeException("获取附件编辑历史失败", e);
        }
    }
    
    // ==================== 附件删除功能实现 ====================
    
    @Override
    @Transactional
    public boolean deleteAttachmentOnly(Long attachmentId) {
        try {
            Optional<Attachment> attachmentOptional = attachmentRepository.findById(attachmentId);
            
            if (attachmentOptional.isPresent()) {
                Attachment attachment = attachmentOptional.get();
                
                // 只删除附件记录，不减少文件引用计数
                attachmentRepository.delete(attachment);
                
                log.info("附件记录删除成功（保留物理文件）- 附件ID: {}, 文件ID: {}", attachmentId, attachment.getFileId());
                return true;
            } else {
                log.warn("附件不存在 - 附件ID: {}", attachmentId);
                return false;
            }
            
        } catch (Exception e) {
            log.error("附件记录删除失败 - 附件ID: {}, 错误: {}", attachmentId, e.getMessage());
            throw new RuntimeException("附件记录删除失败", e);
        }
    }
    
    @Override
    @Transactional
    public boolean batchDeleteAttachmentsOnly(List<Long> attachmentIds) {
        try {
            if (attachmentIds == null || attachmentIds.isEmpty()) {
                log.warn("批量删除附件记录失败 - 附件ID列表为空");
                return false;
            }
            
            int deletedCount = 0;
            
            for (Long attachmentId : attachmentIds) {
                Optional<Attachment> attachmentOptional = attachmentRepository.findById(attachmentId);
                
                if (attachmentOptional.isPresent()) {
                    Attachment attachment = attachmentOptional.get();
                    
                    // 只删除附件记录，不减少文件引用计数
                    attachmentRepository.delete(attachment);
                    
                    deletedCount++;
                    
                    log.debug("附件记录删除成功（保留物理文件）- 附件ID: {}, 文件ID: {}", attachmentId, attachment.getFileId());
                } else {
                    log.warn("附件不存在 - 附件ID: {}", attachmentId);
                }
            }
            
            log.info("批量删除附件记录完成（保留物理文件）- 总附件数: {}, 成功删除数: {}", 
                    attachmentIds.size(), deletedCount);
            return deletedCount > 0;
            
        } catch (Exception e) {
            log.error("批量删除附件记录失败 - 错误: {}", e.getMessage());
            throw new RuntimeException("批量删除附件记录失败", e);
        }
    }
    
    // ==================== 业务类型前缀搜索功能实现 ====================
    
    @Override
    public List<Map<String, Object>> getAttachmentsByBusinessTypePrefix(String businessTypePrefix) {
        try {
            List<Attachment> attachments = attachmentRepository.findByBusinessTypeStartingWith(businessTypePrefix);
            
            return attachments.stream()
                    .map(this::convertAttachmentToMap)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            log.error("根据业务类型前缀获取附件列表失败 - 业务类型前缀: {}, 错误: {}", businessTypePrefix, e.getMessage());
            throw new RuntimeException("获取附件列表失败", e);
        }
    }
    
    @Override
    public List<Map<String, Object>> getAttachmentsByBusinessTypePrefixAndBusinessId(String businessTypePrefix, Long businessId) {
        try {
            List<Attachment> attachments = attachmentRepository.findByBusinessTypeStartingWithAndBusinessId(businessTypePrefix, businessId);
            
            return attachments.stream()
                    .map(this::convertAttachmentToMap)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            log.error("根据业务类型前缀和业务ID获取附件列表失败 - 业务类型前缀: {}, 业务ID: {}, 错误: {}", 
                    businessTypePrefix, businessId, e.getMessage());
            throw new RuntimeException("获取附件列表失败", e);
        }
    }
    
    @Override
    @Transactional
    public boolean deleteAttachmentsByBusinessTypePrefix(String businessTypePrefix) {
        try {
            // 获取所有匹配的附件
            List<Attachment> attachments = attachmentRepository.findByBusinessTypeStartingWith(businessTypePrefix);
            
            if (attachments.isEmpty()) {
                log.info("没有找到匹配的附件 - 业务类型前缀: {}", businessTypePrefix);
                return true;
            }
            
            int deletedCount = 0;
            
            for (Attachment attachment : attachments) {
                try {
                    // 删除附件（包括物理文件）
                    boolean deleted = deleteFile(attachment.getId());
                    if (deleted) {
                        deletedCount++;
                        log.debug("成功删除附件 - 业务类型前缀: {}, 附件ID: {}", businessTypePrefix, attachment.getId());
                    } else {
                        log.warn("删除附件失败 - 业务类型前缀: {}, 附件ID: {}", businessTypePrefix, attachment.getId());
                    }
                } catch (Exception e) {
                    log.error("删除附件时发生异常 - 业务类型前缀: {}, 附件ID: {}, 错误: {}", 
                            businessTypePrefix, attachment.getId(), e.getMessage());
                }
            }
            
            log.info("根据业务类型前缀删除附件完成 - 业务类型前缀: {}, 总附件数: {}, 成功删除数: {}", 
                    businessTypePrefix, attachments.size(), deletedCount);
            return deletedCount > 0;
            
        } catch (Exception e) {
            log.error("根据业务类型前缀删除附件失败 - 业务类型前缀: {}, 错误: {}", businessTypePrefix, e.getMessage());
            throw new RuntimeException("删除附件失败", e);
        }
    }
    
    @Override
    @Transactional
    public boolean deleteAttachmentsByBusinessTypePrefixAndBusinessId(String businessTypePrefix, Long businessId) {
        try {
            // 获取所有匹配的附件
            List<Attachment> attachments = attachmentRepository.findByBusinessTypeStartingWithAndBusinessId(businessTypePrefix, businessId);
            
            if (attachments.isEmpty()) {
                log.info("没有找到匹配的附件 - 业务类型前缀: {}, 业务ID: {}", businessTypePrefix, businessId);
                return true;
            }
            
            int deletedCount = 0;
            
            for (Attachment attachment : attachments) {
                try {
                    // 删除附件（包括物理文件）
                    boolean deleted = deleteFile(attachment.getId());
                    if (deleted) {
                        deletedCount++;
                        log.debug("成功删除附件 - 业务类型前缀: {}, 业务ID: {}, 附件ID: {}", 
                                businessTypePrefix, businessId, attachment.getId());
                    } else {
                        log.warn("删除附件失败 - 业务类型前缀: {}, 业务ID: {}, 附件ID: {}", 
                                businessTypePrefix, businessId, attachment.getId());
                    }
                } catch (Exception e) {
                    log.error("删除附件时发生异常 - 业务类型前缀: {}, 业务ID: {}, 附件ID: {}, 错误: {}", 
                            businessTypePrefix, businessId, attachment.getId(), e.getMessage());
                }
            }
            
            log.info("根据业务类型前缀和业务ID删除附件完成 - 业务类型前缀: {}, 业务ID: {}, 总附件数: {}, 成功删除数: {}", 
                    businessTypePrefix, businessId, attachments.size(), deletedCount);
            return deletedCount > 0;
            
        } catch (Exception e) {
            log.error("根据业务类型前缀和业务ID删除附件失败 - 业务类型前缀: {}, 业务ID: {}, 错误: {}", 
                    businessTypePrefix, businessId, e.getMessage());
            throw new RuntimeException("删除附件失败", e);
        }
    }

}
