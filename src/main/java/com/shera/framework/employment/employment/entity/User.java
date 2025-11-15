package com.shera.framework.employment.employment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
public class User extends BaseEntity {
    
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(name = "phone", unique = true, length = 20)
    private String phone;
    
    @Column(name = "email", unique = true, length = 100)
    private String email;
    
    @Column(name = "password", length = 255)
    private String password;
    
    @Column(name = "wx_openid", unique = true, length = 100)
    private String wxOpenid;
    
    @Column(name = "wx_unionid", unique = true, length = 100)
    private String wxUnionid;
    
    @Column(name = "user_type", nullable = false)
    private Integer userType = 1; // 1-求职者，2-企业用户，3-管理员
    
    @Column(name = "status", nullable = false)
    private Integer status = 1; // 0-禁用，1-正常，2-黑名单
    
    @Column(name = "auth_status", nullable = false)
    private Integer authStatus = 0; // 0-未认证，1-审核中，2-已认证
    
    @Column(name = "last_login_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginTime;
    
    @Column(name = "company_id")
    private Long companyId; // 所属企业ID，仅企业用户使用
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Company company; // 所属企业
    
    // 用户类型枚举
    public enum UserType {
        JOB_SEEKER(1, "求职者"),
        COMPANY_USER(2, "企业用户"),
        ADMIN(3, "管理员");
        
        private final int code;
        private final String desc;
        
        UserType(int code, String desc) {
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
    
    // 用户状态枚举
    public enum Status {
        DISABLED(0, "禁用"),
        NORMAL(1, "正常"),
        BLACKLIST(2, "黑名单");
        
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
    
    // 认证状态枚举
    public enum AuthStatus {
        UNAUTHENTICATED(0, "未认证"),
        PENDING(1, "审核中"),
        AUTHENTICATED(2, "已认证");
        
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
