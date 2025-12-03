package com.shera.framework.employment.employment.modules.resume.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 结构化简历数据传输对象
 * 包含详细的简历结构信息
 */
@Data
public class StructuredResumeDTO {
    
    private Long id;
    
    private Long userId;
    
    private String title;
    
    private ResumeUserDTO user; // 安全的用户信息，不包含敏感数据
    
    // 基本信息
    private BasicInfo basicInfo;
    
    // 教育经历
    private List<Education> educations;
    
    // 工作经历
    private List<WorkExperience> workExperiences;
    
    // 专业技能
    private List<Skill> skills;
    
    // 项目经验
    private List<Project> projects;
    
    // 证书荣誉
    private List<Certificate> certificates;
    
    // 简历设置
    private ResumeSettings settings;
    
    // 基本信息类
    @Data
    public static class BasicInfo {
        private String realName;
        private Integer gender; // 0-未知，1-男，2-女
        private LocalDate birthday;
        private String phone;
        private String email;
        private String avatar;
        private String city;
        private String education; // 最高学历
        private Integer workYears; // 工作年限
        private BigDecimal currentSalary;
        private BigDecimal expectedSalary;
        private String selfIntro;
    }
    
    // 教育经历类
    @Data
    public static class Education {
        private String school; // 学校名称
        private String major; // 专业
        private String degree; // 学历：高中/大专/本科/硕士/博士
        private LocalDate startDate; // 开始时间
        private LocalDate endDate; // 结束时间
        private Boolean isCurrent; // 是否在读
        private String description; // 描述
    }
    
    // 工作经历类
    @Data
    public static class WorkExperience {
        private String company; // 公司名称
        private String position; // 职位
        private String industry; // 行业
        private LocalDate startDate; // 开始时间
        private LocalDate endDate; // 结束时间
        private Boolean isCurrent; // 是否在职
        private String department; // 部门
        private BigDecimal salary; // 薪资
        private String description; // 工作描述
        private List<String> achievements; // 工作成就
    }
    
    // 专业技能类
    @Data
    public static class Skill {
        private String category; // 技能分类：编程语言/框架/数据库/工具等
        private String name; // 技能名称
        private Integer proficiency; // 熟练度：1-了解，2-熟悉，3-熟练，4-精通
        private Integer years; // 使用年限
        private String description; // 技能描述
    }
    
    // 项目经验类
    @Data
    public static class Project {
        private String name; // 项目名称
        private String role; // 担任角色
        private LocalDate startDate; // 开始时间
        private LocalDate endDate; // 结束时间
        private String description; // 项目描述
        private List<String> technologies; // 使用技术
        private List<String> responsibilities; // 职责描述
        private List<String> achievements; // 项目成果
    }
    
    // 证书荣誉类
    @Data
    public static class Certificate {
        private String name; // 证书名称
        private String issuingAuthority; // 颁发机构
        private LocalDate issueDate; // 颁发时间
        private LocalDate expiryDate; // 过期时间
        private String level; // 证书级别
        private String description; // 证书描述
    }
    
    // 简历设置类
    @Data
    public static class ResumeSettings {
        private Boolean isDefault = false; // 是否默认简历
        private Integer privacyLevel = 1; // 隐私级别：1-公开，2-仅投递企业，3-隐藏
        private String templateStyle; // 模板样式
        private String colorScheme; // 配色方案
        private String fontFamily; // 字体
        private Boolean showPhoto = true; // 是否显示照片
        private Boolean showSalary = false; // 是否显示薪资
    }
}
