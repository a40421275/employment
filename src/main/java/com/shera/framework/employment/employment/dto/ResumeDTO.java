package com.shera.framework.employment.employment.dto;

import lombok.Data;

/**
 * 简历数据传输对象
 */
@Data
public class ResumeDTO {
    
    private Long id;
    
    private Long userId;
    
    private String title;
    
    private String content; // JSON格式的简历内容
    
    private String fileUrl;
    
    private String fileType; // pdf/doc/docx
    
    private Boolean isDefault = false;
    
    private Integer privacyLevel = 1; // 1-公开，2-仅投递企业，3-隐藏
}
