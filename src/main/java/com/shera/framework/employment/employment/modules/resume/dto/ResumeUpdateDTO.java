package com.shera.framework.employment.employment.modules.resume.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 更新简历请求DTO
 */
@Data
public class ResumeUpdateDTO {
    
    @NotBlank(message = "简历标题不能为空")
    private String title;
    
    // 附件简历相关字段
    private String fileUrl;
    private String fileType;
    
    // 结构化简历数据
    private StructuredResumeData structuredData;
    
    // 基础信息
    private BasicInfo basicInfo;
    
    // 简历设置
    private ResumeSettings settings;
    
    /**
     * 简历设置
     */
    @Data
    public static class ResumeSettings {
        private Boolean isDefault;
        private Integer privacyLevel;
        private String templateStyle;
        private Boolean showPhoto;
        private Boolean showSalary;
    }
}
