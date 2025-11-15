package com.shera.framework.employment.employment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 管理员实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "admin_user")
public class AdminUser extends BaseEntity {
    
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    
    @Column(name = "real_name", nullable = false, length = 50)
    private String realName;
    
    @Column(name = "role", nullable = false, length = 50)
    private String role; // super_admin, auditor, operator
    
    @Column(name = "status", nullable = false)
    private Integer status = 1; // 0-禁用，1-启用
    
    @Column(name = "last_login_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginTime;
    
    // 角色枚举
    public enum Role {
        SUPER_ADMIN("super_admin", "超级管理员"),
        AUDITOR("auditor", "审核员"),
        OPERATOR("operator", "运营员");
        
        private final String code;
        private final String desc;
        
        Role(String code, String desc) {
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
    
    // 状态枚举
    public enum Status {
        DISABLED(0, "禁用"),
        ENABLED(1, "启用");
        
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
