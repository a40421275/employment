package com.shera.framework.employment.employment.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 实名认证记录实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "auth_log")
public class AuthLog extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "auth_type", nullable = false)
    private Integer authType; // 认证类型：1-实名认证，2-企业认证
    
    @Column(name = "status", nullable = false)
    private Integer status; // 状态：0-失败，1-成功，2-审核中
    
    @Column(name = "auth_data", columnDefinition = "TEXT")
    private String authData; // 认证数据（JSON格式）
    
    @Column(name = "fail_reason", length = 500)
    private String failReason; // 失败原因
    
    @Column(name = "operator_id")
    private Long operatorId; // 操作员ID
    
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    @PrePersist
    protected void onCreate() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
    }
    
    // 认证类型枚举
    public enum AuthType {
        REAL_NAME(1, "实名认证"),
        COMPANY(2, "企业认证");
        
        private final int code;
        private final String desc;
        
        AuthType(int code, String desc) {
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
    
    // 认证状态枚举
    public enum AuthStatus {
        FAILED(0, "失败"),
        SUCCESS(1, "成功"),
        PENDING(2, "审核中");
        
        private final int code;
        private final String desc;
        
        AuthStatus(int code, String desc) {
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
