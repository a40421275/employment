package com.shera.framework.employment.employment.modules.resume.dto;

import lombok.Data;
import java.util.List;

/**
 * 结构化简历数据
 */
@Data
public class StructuredResumeData {
    private List<Education> educations;
    private List<WorkExperience> workExperiences;
    private List<Skill> skills;
    private List<Project> projects;
    private List<Certificate> certificates;
    
    /**
     * 教育经历
     */
    @Data
    public static class Education {
        private String school;
        private String major;
        private String degree;
        private String startDate;
        private String endDate;
        private String description;
    }
    
    /**
     * 工作经历
     */
    @Data
    public static class WorkExperience {
        private String company;
        private String position;
        private String startDate;
        private String endDate;
        private String description;
        private List<String> achievements;
    }
    
    /**
     * 技能
     */
    @Data
    public static class Skill {
        private String name;
        private String level;
        private String description;
    }
    
    /**
     * 项目经验
     */
    @Data
    public static class Project {
        private String name;
        private String role;
        private String startDate;
        private String endDate;
        private String description;
        private List<String> technologies;
        private List<String> responsibilities;
    }
    
    /**
     * 证书荣誉
     */
    @Data
    public static class Certificate {
        private String name;
        private String issuingAuthority;
        private String issueDate;
        private String description;
    }
}
