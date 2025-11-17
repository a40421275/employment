package com.shera.framework.employment.employment.modules.file.dto;

import com.shera.framework.employment.employment.modules.file.entity.Attachment;
import com.shera.framework.employment.employment.modules.file.entity.File;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 附件与文件关联DTO
 * 用于统一查询接口返回包含文件信息的完整数据
 */
@Data
public class AttachmentWithFileDTO {
    
    // 附件信息
    private Long attachmentId;
    private Long userId;
    private String businessType;
    private Long businessId;
    private String description;
    private String tags;
    private Boolean isPublic;
    private Boolean isTemporary;
    private LocalDateTime expireTime;
    private Integer status;
    private Integer downloadCount;
    private Integer viewCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 文件信息
    private Long fileId;
    private String fileName;
    private String originalName;
    private String filePath;
    private Long fileSize;
    private String fileType;
    private String mimeType;
    private String fileExtension;
    private String fileHash;
    private Integer referenceCount;
    
    // 计算属性
    private String fileUrl;
    private Boolean downloadable;
    private Boolean expired;
    
    /**
     * 从Attachment和File实体构建DTO
     */
    public static AttachmentWithFileDTO from(Attachment attachment, File file, String baseUrl) {
        AttachmentWithFileDTO dto = new AttachmentWithFileDTO();
        
        // 附件信息
        dto.setAttachmentId(attachment.getId());
        dto.setUserId(attachment.getUserId());
        dto.setBusinessType(attachment.getBusinessType());
        dto.setBusinessId(attachment.getBusinessId());
        dto.setDescription(attachment.getDescription());
        dto.setTags(attachment.getTags());
        dto.setIsPublic(attachment.getIsPublic());
        dto.setIsTemporary(attachment.getIsTemporary());
        dto.setExpireTime(attachment.getExpireTime());
        dto.setStatus(attachment.getStatus());
        dto.setDownloadCount(attachment.getDownloadCount());
        dto.setViewCount(attachment.getViewCount());
        dto.setCreateTime(attachment.getCreateTime());
        dto.setUpdateTime(attachment.getUpdateTime());
        
        // 文件信息
        if (file != null) {
            dto.setFileId(file.getId());
            dto.setFileName(file.getFileName());
            dto.setOriginalName(file.getOriginalName());
            dto.setFilePath(file.getFilePath());
            dto.setFileSize(file.getFileSize());
            dto.setFileType(file.getFileType());
            dto.setMimeType(file.getMimeType());
            dto.setFileExtension(file.getFileExtension());
            dto.setFileHash(file.getFileHash());
            dto.setReferenceCount(file.getReferenceCount());
        }
        
        // 计算属性
        dto.setFileUrl(baseUrl + "/api/files/download/" + attachment.getId());
        dto.setDownloadable(attachment.isDownloadable());
        dto.setExpired(attachment.isExpired());
        
        return dto;
    }
    
    /**
     * 从Attachment实体构建DTO（文件信息为空）
     */
    public static AttachmentWithFileDTO from(Attachment attachment, String baseUrl) {
        AttachmentWithFileDTO dto = new AttachmentWithFileDTO();
        
        // 附件信息
        dto.setAttachmentId(attachment.getId());
        dto.setUserId(attachment.getUserId());
        dto.setBusinessType(attachment.getBusinessType());
        dto.setBusinessId(attachment.getBusinessId());
        dto.setDescription(attachment.getDescription());
        dto.setTags(attachment.getTags());
        dto.setIsPublic(attachment.getIsPublic());
        dto.setIsTemporary(attachment.getIsTemporary());
        dto.setExpireTime(attachment.getExpireTime());
        dto.setStatus(attachment.getStatus());
        dto.setDownloadCount(attachment.getDownloadCount());
        dto.setViewCount(attachment.getViewCount());
        dto.setCreateTime(attachment.getCreateTime());
        dto.setUpdateTime(attachment.getUpdateTime());
        
        // 文件信息为空
        dto.setFileId(null);
        dto.setFileName(null);
        dto.setOriginalName(null);
        dto.setFilePath(null);
        dto.setFileSize(null);
        dto.setFileType(null);
        dto.setMimeType(null);
        dto.setFileExtension(null);
        dto.setFileHash(null);
        dto.setReferenceCount(null);
        
        // 计算属性
        dto.setFileUrl(baseUrl + "/api/files/download/" + attachment.getId());
        dto.setDownloadable(attachment.isDownloadable());
        dto.setExpired(attachment.isExpired());
        
        return dto;
    }
}
