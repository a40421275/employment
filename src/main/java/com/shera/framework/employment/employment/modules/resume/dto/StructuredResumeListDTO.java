package com.shera.framework.employment.employment.modules.resume.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 结构化简历列表数据传输对象
 * 用于列表查询，只包含基本信息，不包含详细的简历内容
 */
@Data
public class StructuredResumeListDTO {
    
    private Long id;
    
    private Long userId;
    
    private String title;
    
    private String fileType;
    
    private Boolean isDefault;
    
    private Integer privacyLevel;
    
    private Integer viewCount;
    
    private Integer downloadCount;
    
    private Integer shareCount;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    // 简历基本信息（用于列表展示）
    private String realName;
    
    private String avatar;
    
    private String education;
    
    private Integer workYears;
    
    private String city;
    
    private String selfIntro;
    
    // 统计信息
    private Integer educationCount;
    
    private Integer workExperienceCount;
    
    private Integer skillCount;
    
    private Integer projectCount;
    
    private Integer certificateCount;
    
    // 简历完整性评分
    private Integer completenessScore;
}