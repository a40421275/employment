package com.shera.framework.employment.employment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 消息通知实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "notification")
public class Notification extends BaseEntity {
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    @Column(name = "title", nullable = false, length = 200)
    private String title;
    
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "type", nullable = false)
    private Integer type; // 1-系统消息，2-申请结果，3-岗位推荐，4-面试通知
    
    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;
    
    @Column(name = "send_time", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;
    
    @Column(name = "read_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime readTime;
    
    // 消息类型枚举
    public enum Type {
        SYSTEM(1, "系统消息"),
        APPLICATION_RESULT(2, "申请结果"),
        JOB_RECOMMENDATION(3, "岗位推荐"),
        INTERVIEW_NOTICE(4, "面试通知");
        
        private final int code;
        private final String desc;
        
        Type(int code, String desc) {
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
