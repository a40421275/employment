package com.shera.framework.employment.employment.modules.log.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import com.shera.framework.employment.employment.util.BaseEntity;

/**
 * 操作日志实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "operation_log")
public class OperationLog extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "admin_id")
    private Long adminId; // 管理员ID
    
    @Column(name = "operation", nullable = false, length = 100)
    private String operation; // 操作类型
    
    @Column(name = "module", nullable = false, length = 50)
    private String module; // 模块名称
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 操作描述
    
    @Column(name = "ip_address", length = 50)
    private String ipAddress; // IP地址
    
    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent; // 用户代理
    
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    @PrePersist
    protected void onCreate() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
    }
    
    // 操作类型枚举
    public enum OperationType {
        CREATE("创建"),
        UPDATE("更新"),
        DELETE("删除"),
        QUERY("查询"),
        LOGIN("登录"),
        LOGOUT("登出"),
        EXPORT("导出"),
        IMPORT("导入"),
        APPROVE("审批"),
        REJECT("拒绝"),
        ENABLE("启用"),
        DISABLE("禁用"),
        RESET("重置"),
        BACKUP("备份"),
        RESTORE("恢复"),
        OTHER("其他");
        
        private final String desc;
        
        OperationType(String desc) {
            this.desc = desc;
        }
        
        public String getDesc() {
            return desc;
        }
    }
    
    // 模块类型枚举
    public enum ModuleType {
        USER("用户管理"),
        JOB("岗位管理"),
        RESUME("简历管理"),
        JOB_APPLY("申请管理"),
        NOTIFICATION("消息管理"),
        AUTH("认证管理"),
        SYSTEM("系统管理"),
        PERMISSION("权限管理"),
        ROLE("角色管理"),
        CATEGORY("分类管理"),
        ANALYTICS("统计分析"),
        FILE("文件管理"),
        CONFIG("配置管理"),
        OTHER("其他");
        
        private final String desc;
        
        ModuleType(String desc) {
            this.desc = desc;
        }
        
        public String getDesc() {
            return desc;
        }
    }
}
