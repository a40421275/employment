package com.shera.framework.employment.employment.modules.resume.dto;

import lombok.Data;

/**
 * 简历显示设置DTO
 */
@Data
public class ResumeSettingsDTO {
    
    private String templateStyle; // 模板样式
    private String colorScheme; // 配色方案
    private String fontFamily; // 字体
    private Boolean showPhoto = true; // 是否显示照片
    private Boolean showSalary = false; // 是否显示薪资
}
