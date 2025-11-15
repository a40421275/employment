package com.shera.framework.employment.employment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 岗位申请实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "job_apply")
public class JobApply extends BaseEntity {
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    @Column(name = "job_id", nullable = false)
    private Long jobId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", insertable = false, updatable = false)
    private Job job;
    
    @Column(name = "resume_id", nullable = false)
    private Long resumeId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", insertable = false, updatable = false)
    private Resume resume;
    
    @Column(name = "status", nullable = false)
    private Integer status = 1; // 1-已投递，2-已查看，3-已通知面试，4-面试通过，5-已拒绝
    
    @Column(name = "match_score", precision = 5, scale = 2)
    private BigDecimal matchScore;
    
    @Column(name = "apply_notes", columnDefinition = "TEXT")
    private String applyNotes;
    
    @Column(name = "interview_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime interviewTime;
    
    @Column(name = "interview_location", length = 500)
    private String interviewLocation;
    
    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;
    
    // 申请状态枚举
    public enum Status {
        APPLIED(1, "已投递"),
        VIEWED(2, "已查看"),
        INTERVIEW_NOTIFIED(3, "已通知面试"),
        INTERVIEW_PASSED(4, "面试通过"),
        REJECTED(5, "已拒绝");
        
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
}
