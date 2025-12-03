package com.shera.framework.employment.employment.modules.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shera.framework.employment.employment.util.BaseEntity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 用户资料实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_profile")
public class UserProfile extends BaseEntity {
    
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private User user;
    
    @Column(name = "real_name", length = 50)
    private String realName;
    
    @Column(name = "gender")
    private Integer gender; // 0-未知，1-男，2-女
    
    @Column(name = "birthday")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    
    @Column(name = "avatar", length = 500)
    private String avatar;
    
    @Column(name = "id_card_front", length = 500)
    private String idCardFront;
    
    @Column(name = "id_card_back", length = 500)
    private String idCardBack;
    
    @Column(name = "education", length = 50)
    private String education;
    
    @Column(name = "work_years")
    private Integer workYears;
    
    @Column(name = "current_salary", precision = 10, scale = 2)
    private BigDecimal currentSalary;
    
    @Column(name = "expected_salary", precision = 10, scale = 2)
    private BigDecimal expectedSalary;
    
    @Column(name = "city", length = 100)
    private String city;
    
    @Column(name = "skills", columnDefinition = "TEXT")
    private String skills; // JSON格式的技能标签
    
    @Column(name = "self_intro", columnDefinition = "TEXT")
    private String selfIntro;
    
    // 求职偏好
    @Column(name = "preferred_cities", columnDefinition = "TEXT")
    private String preferredCities; // JSON格式的期望城市列表
    
    @Column(name = "job_types", columnDefinition = "TEXT")
    private String jobTypes; // JSON格式的工作类型列表
    
    @Column(name = "industries", columnDefinition = "TEXT")
    private String industries; // JSON格式的行业偏好列表
    
    @Column(name = "work_mode", length = 20)
    private String workMode; // 工作模式：全职/兼职/实习
    
    @Column(name = "job_status", length = 20)
    private String jobStatus; // 求职状态：积极求职/观望中/在职看机会
    
    // 统计信息
    @Column(name = "total_resumes")
    private Integer totalResumes = 0; // 简历总数
    
    @Column(name = "job_apply_count")
    private Integer jobApplyCount = 0; // 投递次数
    
    @Column(name = "interview_count")
    private Integer interviewCount = 0; // 面试次数
    
    @Column(name = "offer_count")
    private Integer offerCount = 0; // 收到offer数
    
    // 性别枚举
    public enum Gender {
        UNKNOWN(0, "未知"),
        MALE(1, "男"),
        FEMALE(2, "女");
        
        private final int code;
        private final String desc;
        
        Gender(int code, String desc) {
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
    
    // 学历枚举
    public enum Education {
        HIGH_SCHOOL("高中", "高中"),
        JUNIOR_COLLEGE("大专", "大专"),
        BACHELOR("本科", "本科"),
        MASTER("硕士", "硕士"),
        DOCTOR("博士", "博士"),
        OTHER("其他", "其他");
        
        private final String code;
        private final String desc;
        
        Education(String code, String desc) {
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
