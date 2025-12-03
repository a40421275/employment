package com.shera.framework.employment.employment.modules.resume.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 创建简历请求DTO
 */
@Data
public class ResumeCreateDTO {
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotBlank(message = "简历标题不能为空")
    private String title;
    
    @NotNull(message = "简历类型不能为空")
    private Integer resumeType; // 1-附件简历，2-结构化简历
    
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
        private Boolean isDefault = false;
        private Integer privacyLevel = 1; // 1-公开，2-仅投递企业，3-隐藏
        private String templateStyle = "modern";
        private Boolean showPhoto = true;
        private Boolean showSalary = false;
    }
    
    // 简历类型枚举
    public enum ResumeType {
        ATTACHMENT(1, "附件简历"),
        STRUCTURED(2, "在线简历");
        
        private final int code;
        private final String desc;
        
        ResumeType(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        
        public int getCode() {
            return code;
        }
        
        public String getDesc() {
            return desc;
        }
    }
}
