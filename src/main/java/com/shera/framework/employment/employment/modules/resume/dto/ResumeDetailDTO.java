package com.shera.framework.employment.employment.modules.resume.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shera.framework.employment.employment.modules.resume.entity.Resume;
import com.shera.framework.employment.employment.modules.user.dto.UserDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 简历详情DTO
 * 根据简历类型返回不同格式的数据
 */
@Data
public class ResumeDetailDTO {
    
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
    private LocalDateTime lastViewTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdateTime;
    
    private Boolean syncProfileData;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastSyncTime;
    
    // 基础信息（从用户资料同步或独立设置）
    private BasicInfo basicInfo;
    
    // 结构化简历数据（仅结构化简历使用）
    private StructuredResumeData structuredData;
    
    // 显示设置
    private ResumeSettingsDTO settings;
    
    // 统计信息
    private Map<String, Object> stats;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    /**
     * 从简历实体构建详情DTO
     */
    public static ResumeDetailDTO fromEntity(Resume resume) {
        ResumeDetailDTO dto = new ResumeDetailDTO();
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
        dto.setLastViewTime(resume.getLastViewTime());
        dto.setLastUpdateTime(resume.getLastUpdateTime());
        dto.setSyncProfileData(resume.getSyncProfileData());
        dto.setLastSyncTime(resume.getLastSyncTime());
        dto.setCreateTime(resume.getCreateTime());
        dto.setUpdateTime(resume.getUpdateTime());
        
        // 设置显示设置
        ResumeSettingsDTO settings = new ResumeSettingsDTO();
        settings.setTemplateStyle(resume.getTemplateStyle());
        settings.setColorScheme(resume.getColorScheme());
        settings.setFontFamily(resume.getFontFamily());
        settings.setShowPhoto(resume.getShowPhoto());
        settings.setShowSalary(resume.getShowSalary());
        dto.setSettings(settings);
        
        return dto;
    }
    
    
    /**
     * 设置基础信息
     */
    public void setBasicInfo(BasicInfo basicInfo) {
        this.basicInfo = basicInfo;
    }
    
    /**
     * 设置结构化数据
     */
    public void setStructuredData(StructuredResumeData structuredData) {
        this.structuredData = structuredData;
    }
    
    /**
     * 设置统计信息
     */
    public void setStats(Map<String, Object> stats) {
        this.stats = stats;
    }
}
