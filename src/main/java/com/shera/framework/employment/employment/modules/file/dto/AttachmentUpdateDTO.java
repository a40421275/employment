package com.shera.framework.employment.employment.modules.file.dto;

import lombok.Data;

/**
 * 附件更新DTO
 * 用于支持后期编辑更改文件关联
 */
@Data
public class AttachmentUpdateDTO {
    
    /**
     * 附件ID
     */
    private Long attachmentId;
    
    /**
     * 用户ID（用于权限验证）
     */
    private Long userId;
    
    /**
     * 新的文件ID（可选，用于更换文件）
     */
    private Long newFileId;
    
    /**
     * 新的业务类型（可选）
     */
    private String businessType;
    
    /**
     * 新的业务ID（可选）
     */
    private Long businessId;
    
    /**
     * 新的文件描述（可选）
     */
    private String description;
    
    /**
     * 新的文件标签（可选）
     */
    private String tags;
    
    /**
     * 是否公开（可选）
     */
    private Boolean isPublic;
    
    /**
     * 是否临时文件（可选）
     */
    private Boolean isTemporary;
    
    /**
     * 过期时间（可选）
     */
    private String expireTime;
    
    /**
     * 状态（可选）
     */
    private Integer status;
    
    /**
     * 验证DTO的有效性
     */
    public boolean isValid() {
        return attachmentId != null && userId != null;
    }
    
    /**
     * 检查是否有业务信息更新
     */
    public boolean hasBusinessUpdate() {
        return businessType != null || businessId != null;
    }
    
    /**
     * 检查是否有文件更换
     */
    public boolean hasFileChange() {
        return newFileId != null;
    }
    
    /**
     * 检查是否有元数据更新
     */
    public boolean hasMetadataUpdate() {
        return description != null || tags != null || isPublic != null || 
               isTemporary != null || expireTime != null || status != null;
    }
}
