package com.shera.framework.employment.employment.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 匹配记录实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "match_log")
public class MatchLog extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "job_id", nullable = false)
    private Long jobId;
    
    @Column(name = "match_score", nullable = false)
    private Double matchScore; // 匹配分数
    
    @Column(name = "match_reason", columnDefinition = "TEXT")
    private String matchReason; // 匹配原因
    
    @Column(name = "is_viewed", nullable = false)
    private Boolean isViewed = false; // 是否已查看：0-否，1-是
    
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    @PrePersist
    protected void onCreate() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
    }
    
    // 匹配级别枚举
    public enum MatchLevel {
        PERFECT(90, 100, "完美匹配"),
        EXCELLENT(80, 89, "优秀匹配"),
        GOOD(70, 79, "良好匹配"),
        AVERAGE(60, 69, "一般匹配"),
        LOW(0, 59, "低匹配");
        
        private final int minScore;
        private final int maxScore;
        private final String desc;
        
        MatchLevel(int minScore, int maxScore, String desc) {
            this.minScore = minScore;
            this.maxScore = maxScore;
            this.desc = desc;
        }
        
        public int getMinScore() {
            return minScore;
        }
        
        public int getMaxScore() {
            return maxScore;
        }
        
        public String getDesc() {
            return desc;
        }
        
        public static MatchLevel fromScore(double score) {
            for (MatchLevel level : values()) {
                if (score >= level.minScore && score <= level.maxScore) {
                    return level;
                }
            }
            return LOW;
        }
    }
    
    // 匹配原因类型枚举
    public enum MatchReasonType {
        SKILL_MATCH("技能匹配"),
        EXPERIENCE_MATCH("经验匹配"),
        EDUCATION_MATCH("学历匹配"),
        SALARY_MATCH("薪资匹配"),
        LOCATION_MATCH("地点匹配"),
        COMPANY_MATCH("公司匹配"),
        INDUSTRY_MATCH("行业匹配"),
        JOB_TYPE_MATCH("岗位类型匹配"),
        OTHER("其他");
        
        private final String desc;
        
        MatchReasonType(String desc) {
            this.desc = desc;
        }
        
        public String getDesc() {
            return desc;
        }
    }
}
