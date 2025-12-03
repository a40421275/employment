package com.shera.framework.employment.employment.modules.resume.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shera.framework.employment.employment.modules.resume.entity.Resume;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 简历列表DTO
 * 用于简历列表展示，包含基本信息
 */
@Data
public class ResumeListDTO {
    
    private Long id;
    private Long userId;
    private String title;
    private Integer resumeType; // 1-附件简历，2-结构化简历
    private String fileUrl;
    private String fileType;
    private Boolean isDefault;
    private Integer privacyLevel;
    private Integer viewCount;
    private Integer downloadCount;
    private Integer shareCount;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdateTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    /**
     * 从简历实体构建列表DTO
     */
    public static ResumeListDTO fromEntity(Resume resume) {
        ResumeListDTO dto = new ResumeListDTO();
        dto.setId(resume.getId());
        dto.setUserId(resume.getUserId());
        dto.setTitle(resume.getTitle());
        dto.setResumeType(resume.getResumeType());
        dto.setFileUrl(resume.getFileUrl());
        dto.setFileType(resume.getFileType());
        dto.setIsDefault(resume.getIsDefault());
        dto.setPrivacyLevel(resume.getPrivacyLevel());
        dto.setViewCount(resume.getViewCount());
        dto.setDownloadCount(resume.getDownloadCount());
        dto.setShareCount(resume.getShareCount());
        dto.setLastUpdateTime(resume.getLastUpdateTime());
        dto.setCreateTime(resume.getCreateTime());
        return dto;
    }
}
