package com.shera.framework.employment.employment.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 系统配置实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "system_config")
public class SystemConfig extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "config_key", nullable = false, unique = true, length = 100)
    private String configKey; // 配置键
    
    @Column(name = "config_value", nullable = false, columnDefinition = "TEXT")
    private String configValue; // 配置值
    
    @Column(name = "description", length = 500)
    private String description; // 配置描述
    
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;
    
    @PrePersist
    protected void onCreate() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
        if (updateTime == null) {
            updateTime = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
    
    // 系统配置键枚举
    public enum ConfigKey {
        SITE_NAME("site_name", "网站名称"),
        SITE_DESCRIPTION("site_description", "网站描述"),
        SITE_KEYWORDS("site_keywords", "网站关键词"),
        SITE_LOGO("site_logo", "网站Logo"),
        SITE_FAVICON("site_favicon", "网站图标"),
        SITE_COPYRIGHT("site_copyright", "版权信息"),
        SITE_ICP("site_icp", "备案号"),
        SITE_CONTACT("site_contact", "联系方式"),
        SITE_ADDRESS("site_address", "联系地址"),
        
        RESUME_PRIVACY_DEFAULT("resume_privacy_default", "简历默认隐私级别"),
        JOB_EXPIRE_DAYS("job_expire_days", "岗位默认过期天数"),
        MATCH_THRESHOLD("match_threshold", "匹配阈值"),
        MAX_RESUME_COUNT("max_resume_count", "最大简历数量"),
        MAX_JOB_APPLY_COUNT("max_job_apply_count", "最大申请数量"),
        
        SMS_ENABLED("sms_enabled", "短信服务是否启用"),
        SMS_PROVIDER("sms_provider", "短信服务提供商"),
        SMS_ACCESS_KEY("sms_access_key", "短信访问密钥"),
        SMS_SECRET_KEY("sms_secret_key", "短信密钥"),
        SMS_SIGN_NAME("sms_sign_name", "短信签名"),
        
        EMAIL_ENABLED("email_enabled", "邮件服务是否启用"),
        EMAIL_HOST("email_host", "邮件服务器"),
        EMAIL_PORT("email_port", "邮件端口"),
        EMAIL_USERNAME("email_username", "邮件用户名"),
        EMAIL_PASSWORD("email_password", "邮件密码"),
        EMAIL_FROM("email_from", "发件人邮箱"),
        
        FILE_UPLOAD_MAX_SIZE("file_upload_max_size", "文件上传最大大小"),
        FILE_ALLOWED_TYPES("file_allowed_types", "允许的文件类型"),
        FILE_STORAGE_TYPE("file_storage_type", "文件存储类型"),
        FILE_STORAGE_PATH("file_storage_path", "文件存储路径"),
        
        SECURITY_LOGIN_ATTEMPTS("security_login_attempts", "登录尝试次数"),
        SECURITY_LOCKOUT_TIME("security_lockout_time", "锁定时间"),
        SECURITY_PASSWORD_MIN_LENGTH("security_password_min_length", "密码最小长度"),
        SECURITY_PASSWORD_COMPLEXITY("security_password_complexity", "密码复杂度"),
        
        NOTIFICATION_ENABLED("notification_enabled", "通知服务是否启用"),
        NOTIFICATION_TYPES("notification_types", "通知类型"),
        NOTIFICATION_PUSH_INTERVAL("notification_push_interval", "推送间隔"),
        
        ANALYTICS_ENABLED("analytics_enabled", "分析服务是否启用"),
        ANALYTICS_PROVIDER("analytics_provider", "分析服务提供商"),
        ANALYTICS_TRACKING_ID("analytics_tracking_id", "跟踪ID"),
        
        CACHE_ENABLED("cache_enabled", "缓存是否启用"),
        CACHE_TTL("cache_ttl", "缓存生存时间"),
        CACHE_MAX_SIZE("cache_max_size", "缓存最大大小"),
        
        BACKUP_ENABLED("backup_enabled", "备份是否启用"),
        BACKUP_INTERVAL("backup_interval", "备份间隔"),
        BACKUP_RETENTION("backup_retention", "备份保留天数");
        
        private final String key;
        private final String description;
        
        ConfigKey(String key, String description) {
            this.key = key;
            this.description = description;
        }
        
        public String getKey() {
            return key;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    // 配置值类型枚举
    public enum ValueType {
        STRING("字符串"),
        INTEGER("整数"),
        BOOLEAN("布尔值"),
        DECIMAL("小数"),
        JSON("JSON"),
        ARRAY("数组"),
        OBJECT("对象");
        
        private final String desc;
        
        ValueType(String desc) {
            this.desc = desc;
        }
        
        public String getDesc() {
            return desc;
        }
    }
}
