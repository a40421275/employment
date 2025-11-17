package com.shera.framework.employment.employment.modules.job.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shera.framework.employment.employment.util.BaseEntity;
import com.shera.framework.employment.employment.modules.company.entity.Company;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 岗位实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "job")
public class Job extends BaseEntity {
    
    @Column(name = "title", nullable = false, length = 200)
    private String title;
    
    @Column(name = "category_id", nullable = false)
    private Long categoryId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private JobCategory category;
    
    @Column(name = "company_id")
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Company company; // 所属企业
    
    @Column(name = "department", length = 100)
    private String department;
    
    @Column(name = "job_type", nullable = false)
    private Integer jobType = 1; // 1-全职，2-兼职，3-实习
    
    @Column(name = "salary_min", precision = 10, scale = 2)
    private BigDecimal salaryMin;
    
    @Column(name = "salary_max", precision = 10, scale = 2)
    private BigDecimal salaryMax;
    
    @Column(name = "salary_unit", length = 20)
    private String salaryUnit = "月";
    
    @Column(name = "work_city", nullable = false, length = 100)
    private String workCity;
    
    @Column(name = "work_address", length = 500)
    private String workAddress;
    
    @Column(name = "work_district", length = 100)
    private String workDistrict; // 工作区域/区县
    
    @Column(name = "work_latitude")
    private Double workLatitude; // 工作地点纬度
    
    @Column(name = "work_longitude")
    private Double workLongitude; // 工作地点经度
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "requirements", columnDefinition = "TEXT")
    private String requirements;
    
    @Column(name = "benefits", columnDefinition = "TEXT")
    private String benefits;
    
    @Column(name = "contact_info", columnDefinition = "TEXT")
    private String contactInfo; // JSON格式的联系信息
    
    @Column(name = "education_requirement", length = 50)
    private String educationRequirement; // 学历要求
    
    @Column(name = "experience_requirement", length = 50)
    private String experienceRequirement; // 经验要求
    
    @Column(name = "recruit_number")
    private Integer recruitNumber = 1; // 招聘人数
    
    @Column(name = "urgent_level")
    private Integer urgentLevel = 0; // 紧急程度：0-普通，1-紧急，2-非常紧急
    
    @Column(name = "priority_level")
    private Integer priorityLevel = 0; // 优先级：0-普通，1-重要，2-非常重要
    
    @Column(name = "is_recommended")
    private Boolean isRecommended = false; // 是否推荐
    
    @Column(name = "recommend_reason", length = 200)
    private String recommendReason; // 推荐理由
    
    @Column(name = "keywords", length = 500)
    private String keywords; // 关键词，用于搜索优化
    
    @Column(name = "status", nullable = false)
    private Integer status = 1; // 0-草稿，1-已发布，2-已下架，3-已归档
    
    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;
    
    @Column(name = "apply_count", nullable = false)
    private Integer applyCount = 0;
    
    @Column(name = "favorite_count", nullable = false)
    private Integer favoriteCount = 0;
    
    @Column(name = "publish_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;
    
    @Column(name = "expire_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;
    
    // 岗位类型枚举
    public enum JobType {
        FULL_TIME(1, "全职"),
        PART_TIME(2, "兼职"),
        INTERNSHIP(3, "实习");
        
        private final int code;
        private final String desc;
        
        JobType(int code, String desc) {
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
    
    // 岗位状态枚举
    public enum Status {
        DRAFT(0, "草稿"),
        PUBLISHED(1, "已发布"),
        OFFLINE(2, "已下架"),
        ARCHIVED(3, "已归档");
        
        private final int code;
        private final String desc;
        
        Status(int code, String desc) {
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
    
    // 薪资单位枚举
    public enum SalaryUnit {
        MONTH("月", "月薪"),
        YEAR("年", "年薪"),
        HOUR("小时", "时薪");
        
        private final String code;
        private final String desc;
        
        SalaryUnit(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        
        public String getCode() {
            return code;
        }
        
        public String getDesc() {
            return desc;
        }
    }
    
    // 紧急程度枚举
    public enum UrgentLevel {
        NORMAL(0, "普通"),
        URGENT(1, "紧急"),
        VERY_URGENT(2, "非常紧急");
        
        private final int code;
        private final String desc;
        
        UrgentLevel(int code, String desc) {
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
    
    // 优先级枚举
    public enum PriorityLevel {
        NORMAL(0, "普通"),
        IMPORTANT(1, "重要"),
        VERY_IMPORTANT(2, "非常重要");
        
        private final int code;
        private final String desc;
        
        PriorityLevel(int code, String desc) {
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
    
    // 学历要求枚举
    public enum EducationRequirement {
        NO_REQUIREMENT("不限", "不限"),
        HIGH_SCHOOL("高中", "高中"),
        COLLEGE("大专", "大专"),
        BACHELOR("本科", "本科"),
        MASTER("硕士", "硕士"),
        DOCTOR("博士", "博士");
        
        private final String code;
        private final String desc;
        
        EducationRequirement(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        
        public String getCode() {
            return code;
        }
        
        public String getDesc() {
            return desc;
        }
    }
    
    // 经验要求枚举
    public enum ExperienceRequirement {
        NO_EXPERIENCE("不限", "不限"),
        GRADUATE("应届生", "应届生"),
        ONE_YEAR("1年", "1年"),
        ONE_TO_THREE("1-3年", "1-3年"),
        THREE_TO_FIVE("3-5年", "3-5年"),
        FIVE_TO_TEN("5-10年", "5-10年"),
        TEN_PLUS("10年以上", "10年以上");
        
        private final String code;
        private final String desc;
        
        ExperienceRequirement(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        
        public String getCode() {
            return code;
        }
        
        public String getDesc() {
            return desc;
        }
    }
}
