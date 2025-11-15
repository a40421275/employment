package com.shera.framework.employment.employment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
