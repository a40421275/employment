package com.shera.framework.employment.employment.util;

import com.shera.framework.employment.employment.modules.system.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 系统配置工具类
 * 提供高性能的配置访问，支持缓存、类型转换和自动刷新
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemConfigUtil {
    
    private final SystemConfigService systemConfigService;
    
    // 配置缓存
    private final Map<String, String> configCache = new ConcurrentHashMap<>();
    
    // 定时刷新线程池
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    
    // 配置键常量（与初始化脚本保持一致）
    public static final String FILE_UPLOAD_MAX_SIZE = "file.upload.max_size";
    public static final String FILE_UPLOAD_MAX_REQUEST_SIZE = "file.upload.max_request_size";
    public static final String FILE_ALLOWED_TYPES = "file.upload.allowed_types";
    public static final String FILE_STORAGE_PATH = "file.upload.storage_path";
    public static final String FILE_STORAGE_TYPE = "file.upload.storage_type";
    public static final String FILE_BASE_URL = "file.upload.base_url";
    public static final String FILE_DOWNLOAD_SIGNER_EXPIRY_SECONDS = "file_download_signer.expiry_seconds";
    
    public static final String SITE_NAME = "site_name";
    public static final String SITE_DESCRIPTION = "site_description";
    public static final String SITE_KEYWORDS = "site_keywords";
    public static final String SITE_LOGO = "site_logo";
    
    public static final String SECURITY_LOGIN_ATTEMPTS = "security_login_attempts";
    public static final String SECURITY_LOCKOUT_TIME = "security_lockout_time";
    public static final String SECURITY_PASSWORD_MIN_LENGTH = "security_password_min_length";
    
    public static final String EMAIL_ENABLED = "email_enabled";
    public static final String EMAIL_HOST = "email_host";
    public static final String EMAIL_PORT = "email_port";
    public static final String EMAIL_USERNAME = "email_username";
    public static final String EMAIL_PASSWORD = "email_password";
    public static final String EMAIL_FROM = "email_from";
    
    public static final String SMS_ENABLED = "sms_enabled";
    public static final String SMS_PROVIDER = "sms_provider";
    
    public static final String NOTIFICATION_ENABLED = "notification_enabled";
    public static final String NOTIFICATION_TYPES = "notification_types";
    
    public static final String WECHAT_ENABLED = "wechat_enabled";
    public static final String WECHAT_APP_ID = "wechat_app_id";
    public static final String WECHAT_APP_SECRET = "wechat_app_secret";
    public static final String WECHAT_TEMPLATE_ID = "wechat_template_id";
    
    public static final String CACHE_ENABLED = "cache_enabled";
    public static final String CACHE_TTL = "cache_ttl";
    
    public static final String RESUME_PRIVACY_DEFAULT = "resume_privacy_default";
    public static final String JOB_EXPIRE_DAYS = "job_expire_days";
    public static final String MATCH_THRESHOLD = "match_threshold";
    
    // 默认配置值
    private static final Map<String, String> DEFAULT_CONFIGS = Map.ofEntries(
        Map.entry(FILE_UPLOAD_MAX_SIZE, "104857600"),
        Map.entry(FILE_UPLOAD_MAX_REQUEST_SIZE, "104857600"),
        Map.entry(FILE_ALLOWED_TYPES, "jpg,jpeg,png,gif,pdf,doc,docx"),
        Map.entry(FILE_STORAGE_PATH, "/uploads"),
        Map.entry(FILE_STORAGE_TYPE, "local"),
        Map.entry(FILE_BASE_URL, "http://localhost:8080"),
        Map.entry(SITE_NAME, "就业服务平台"),
        Map.entry(SITE_DESCRIPTION, "专业的就业服务平台"),
        Map.entry(SITE_KEYWORDS, "就业,招聘,求职,岗位"),
        Map.entry(SITE_LOGO, "/images/logo.png"),
        Map.entry(SECURITY_LOGIN_ATTEMPTS, "5"),
        Map.entry(SECURITY_LOCKOUT_TIME, "30"),
        Map.entry(SECURITY_PASSWORD_MIN_LENGTH, "8"),
        Map.entry(EMAIL_ENABLED, "true"),
        Map.entry(EMAIL_HOST, "smtp.163.com"),
        Map.entry(EMAIL_PORT, "465"),
        Map.entry(EMAIL_USERNAME, "wangcheng-0012@163.com"),
        Map.entry(EMAIL_PASSWORD, "PSRV634spjyqRcGy"),
        Map.entry(EMAIL_FROM, "wangcheng-0012@163.com"),
        Map.entry(SMS_ENABLED, "false"),
        Map.entry(SMS_PROVIDER, "aliyun"),
        Map.entry(NOTIFICATION_ENABLED, "true"),
        Map.entry(NOTIFICATION_TYPES, "email,sms,web"),
        Map.entry(WECHAT_ENABLED, "false"),
        Map.entry(WECHAT_APP_ID, ""),
        Map.entry(WECHAT_APP_SECRET, ""),
        Map.entry(WECHAT_TEMPLATE_ID, ""),
        Map.entry(CACHE_ENABLED, "true"),
        Map.entry(CACHE_TTL, "3600"),
        Map.entry(RESUME_PRIVACY_DEFAULT, "1"),
        Map.entry(JOB_EXPIRE_DAYS, "30"),
        Map.entry(MATCH_THRESHOLD, "0.7"),
        Map.entry(FILE_DOWNLOAD_SIGNER_EXPIRY_SECONDS, "3600")
    );
    
    @Autowired
    public void init() {
        log.info("初始化系统配置工具类...");
        preloadConfigs();
        startAutoRefresh();
        log.info("系统配置工具类初始化完成，共加载 {} 个配置项", configCache.size());
    }
    
    /**
     * 预加载所有配置
     */
    private void preloadConfigs() {
        DEFAULT_CONFIGS.forEach((key, defaultValue) -> {
            try {
                String value = systemConfigService.getStringValue(key, defaultValue);
                configCache.put(key, value);
                log.debug("预加载配置: {} = {}", key, value);
            } catch (Exception e) {
                log.warn("预加载配置失败: {}, 使用默认值: {}", key, defaultValue, e);
                configCache.put(key, defaultValue);
            }
        });
    }
    
    /**
     * 启动自动刷新
     */
    private void startAutoRefresh() {
        // 每30分钟自动刷新一次配置
        scheduler.scheduleAtFixedRate(this::refreshAllConfigs, 30, 30, TimeUnit.MINUTES);
        log.info("配置自动刷新已启动，刷新间隔: 30分钟");
    }
    
    // ==================== 基础配置获取方法 ====================
    
    /**
     * 获取字符串配置值
     */
    public String getString(String configKey) {
        return configCache.getOrDefault(configKey, "");
    }
    
    /**
     * 获取字符串配置值（带默认值）
     */
    public String getString(String configKey, String defaultValue) {
        return configCache.getOrDefault(configKey, defaultValue);
    }
    
    /**
     * 获取整数配置值
     */
    public Integer getInteger(String configKey) {
        return getInteger(configKey, 0);
    }
    
    /**
     * 获取整数配置值（带默认值）
     */
    public Integer getInteger(String configKey, Integer defaultValue) {
        return getValue(configKey, defaultValue, Integer::parseInt);
    }
    
    /**
     * 获取长整数配置值
     */
    public Long getLong(String configKey) {
        return getLong(configKey, 0L);
    }
    
    /**
     * 获取长整数配置值（带默认值）
     */
    public Long getLong(String configKey, Long defaultValue) {
        return getValue(configKey, defaultValue, Long::parseLong);
    }
    
    /**
     * 获取布尔配置值
     */
    public Boolean getBoolean(String configKey) {
        return getBoolean(configKey, false);
    }
    
    /**
     * 获取布尔配置值（带默认值）
     */
    public Boolean getBoolean(String configKey, Boolean defaultValue) {
        String value = configCache.get(configKey);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }
    
    /**
     * 获取双精度配置值
     */
    public Double getDouble(String configKey) {
        return getDouble(configKey, 0.0);
    }
    
    /**
     * 获取双精度配置值（带默认值）
     */
    public Double getDouble(String configKey, Double defaultValue) {
        return getValue(configKey, defaultValue, Double::parseDouble);
    }
    
    /**
     * 获取浮点数配置值
     */
    public Float getFloat(String configKey) {
        return getFloat(configKey, 0.0f);
    }
    
    /**
     * 获取浮点数配置值（带默认值）
     */
    public Float getFloat(String configKey, Float defaultValue) {
        return getValue(configKey, defaultValue, Float::parseFloat);
    }
    
    // ==================== 便捷配置获取方法 ====================
    
    /**
     * 获取文件上传最大大小（字节）
     */
    public Long getFileUploadMaxSize() {
        return getLong(FILE_UPLOAD_MAX_SIZE, 104857600L); // 100MB
    }
    
    /**
     * 获取请求最大大小（字节）
     */
    public Long getFileUploadMaxRequestSize() {
        return getLong(FILE_UPLOAD_MAX_REQUEST_SIZE, 104857600L); // 100MB
    }
    
    /**
     * 获取允许的文件类型
     */
    public String getAllowedFileTypes() {
        return getString(FILE_ALLOWED_TYPES, "jpg,jpeg,png,gif,pdf,doc,docx");
    }
    
    /**
     * 获取文件存储路径
     */
    public String getFileStoragePath() {
        return getString(FILE_STORAGE_PATH, "/uploads");
    }
    
    /**
     * 获取文件存储类型
     */
    public String getFileStorageType() {
        return getString(FILE_STORAGE_TYPE, "local");
    }
    
    /**
     * 获取网站名称
     */
    public String getSiteName() {
        return getString(SITE_NAME, "就业服务平台");
    }
    
    /**
     * 获取网站描述
     */
    public String getSiteDescription() {
        return getString(SITE_DESCRIPTION, "专业的就业服务平台");
    }
    
    /**
     * 获取最大登录尝试次数
     */
    public Integer getMaxLoginAttempts() {
        return getInteger(SECURITY_LOGIN_ATTEMPTS, 5);
    }
    
    /**
     * 获取锁定时间（分钟）
     */
    public Integer getLockoutTime() {
        return getInteger(SECURITY_LOCKOUT_TIME, 30);
    }
    
    /**
     * 获取密码最小长度
     */
    public Integer getPasswordMinLength() {
        return getInteger(SECURITY_PASSWORD_MIN_LENGTH, 8);
    }
    
    /**
     * 获取简历隐私默认设置
     */
    public Integer getResumePrivacyDefault() {
        return getInteger(RESUME_PRIVACY_DEFAULT, 1);
    }
    
    /**
     * 获取职位过期天数
     */
    public Integer getJobExpireDays() {
        return getInteger(JOB_EXPIRE_DAYS, 30);
    }
    
    /**
     * 获取匹配阈值
     */
    public Double getMatchThreshold() {
        return getDouble(MATCH_THRESHOLD, 0.7);
    }

    /**
     * 获取签名URL过期时间（秒）
     */
    public Long getUrlSignerExpirySeconds() {
        return getLong(FILE_DOWNLOAD_SIGNER_EXPIRY_SECONDS, 3600L); // 默认1小时
    }
    
    // ==================== 邮件配置获取方法 ====================
    
    /**
     * 获取邮件服务是否启用
     */
    public Boolean isEmailEnabled() {
        return getBoolean(EMAIL_ENABLED, false);
    }
    
    /**
     * 获取邮件服务器主机
     */
    public String getEmailHost() {
        return getString(EMAIL_HOST, "smtp.example.com");
    }
    
    /**
     * 获取邮件服务器端口
     */
    public Integer getEmailPort() {
        return getInteger(EMAIL_PORT, 587);
    }
    
    /**
     * 获取邮件用户名
     */
    public String getEmailUsername() {
        return getString(EMAIL_USERNAME, "");
    }
    
    /**
     * 获取邮件密码
     */
    public String getEmailPassword() {
        return getString(EMAIL_PASSWORD, "");
    }
    
    /**
     * 获取发件人邮箱
     */
    public String getEmailFrom() {
        return getString(EMAIL_FROM, "");
    }
    
    /**
     * 验证邮件配置是否完整
     */
    public boolean isEmailConfigValid() {
        return isEmailEnabled() && 
               !getEmailHost().isEmpty() && 
               !getEmailUsername().isEmpty() && 
               !getEmailPassword().isEmpty() && 
               !getEmailFrom().isEmpty();
    }
    
    /**
     * 获取邮件配置摘要信息（用于日志，不包含敏感信息）
     */
    public String getEmailConfigSummary() {
        return String.format("EmailConfig[enabled=%s, host=%s, port=%d, username=%s, from=%s]",
                isEmailEnabled(), getEmailHost(), getEmailPort(), getEmailUsername(), getEmailFrom());
    }
    
    // ==================== 微信配置获取方法 ====================
    
    /**
     * 获取微信服务是否启用
     */
    public Boolean isWechatEnabled() {
        return getBoolean(WECHAT_ENABLED, false);
    }
    
    /**
     * 获取微信AppID
     */
    public String getWechatAppId() {
        return getString(WECHAT_APP_ID, "");
    }
    
    /**
     * 获取微信AppSecret
     */
    public String getWechatAppSecret() {
        return getString(WECHAT_APP_SECRET, "");
    }
    
    /**
     * 获取微信模板ID
     */
    public String getWechatTemplateId() {
        return getString(WECHAT_TEMPLATE_ID, "");
    }
    
    /**
     * 验证微信配置是否完整
     */
    public boolean isWechatConfigValid() {
        return isWechatEnabled() && 
               !getWechatAppId().isEmpty() && 
               !getWechatAppSecret().isEmpty();
    }
    
    /**
     * 获取微信配置摘要信息（用于日志，不包含敏感信息）
     */
    public String getWechatConfigSummary() {
        return String.format("WechatConfig[enabled=%s, appId=%s, hasSecret=%s, hasTemplate=%s]",
                isWechatEnabled(), 
                getWechatAppId(), 
                !getWechatAppSecret().isEmpty() ? "yes" : "no",
                !getWechatTemplateId().isEmpty() ? "yes" : "no");
    }
    
    // ==================== 配置管理方法 ====================
    
    /**
     * 刷新单个配置
     */
    public void refreshConfig(String configKey) {
        try {
            String defaultValue = DEFAULT_CONFIGS.getOrDefault(configKey, "");
            String value = systemConfigService.getStringValue(configKey, defaultValue);
            configCache.put(configKey, value);
            log.debug("刷新配置: {} = {}", configKey, value);
        } catch (Exception e) {
            log.error("刷新配置失败: {}", configKey, e);
        }
    }
    
    /**
     * 刷新所有配置
     */
    public void refreshAllConfigs() {
        log.info("开始刷新所有配置...");
        DEFAULT_CONFIGS.forEach((key, defaultValue) -> {
            try {
                String value = systemConfigService.getStringValue(key, defaultValue);
                configCache.put(key, value);
            } catch (Exception e) {
                log.warn("刷新配置失败: {}, 使用缓存值", key, e);
            }
        });
        log.info("所有配置刷新完成，当前配置数量: {}", configCache.size());
    }
    
    /**
     * 强制刷新配置（忽略缓存）
     */
    public void forceRefreshConfig(String configKey) {
        try {
            String value = systemConfigService.getStringValue(configKey, "");
            if (!value.isEmpty()) {
                configCache.put(configKey, value);
                log.info("强制刷新配置: {} = {}", configKey, value);
            }
        } catch (Exception e) {
            log.error("强制刷新配置失败: {}", configKey, e);
        }
    }
    
    /**
     * 设置配置值（临时，不持久化）
     */
    public void setConfig(String configKey, String value) {
        configCache.put(configKey, value);
        log.debug("设置临时配置: {} = {}", configKey, value);
    }
    
    /**
     * 删除配置（从缓存中移除）
     */
    public void removeConfig(String configKey) {
        configCache.remove(configKey);
        log.debug("删除配置: {}", configKey);
    }
    
    /**
     * 检查配置是否存在
     */
    public boolean containsConfig(String configKey) {
        return configCache.containsKey(configKey);
    }
    
    /**
     * 获取所有配置
     */
    public Map<String, String> getAllConfigs() {
        return new ConcurrentHashMap<>(configCache);
    }
    
    /**
     * 获取配置数量
     */
    public int getConfigCount() {
        return configCache.size();
    }
    
    /**
     * 清空配置缓存
     */
    public void clearCache() {
        configCache.clear();
        log.info("配置缓存已清空");
    }
    
    /**
     * 销毁方法
     */
    public void destroy() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        log.info("系统配置工具类已销毁");
    }
    
    // ==================== 辅助方法 ====================
    
    /**
     * 通用值获取方法
     */
    private <T> T getValue(String configKey, T defaultValue, Function<String, T> parser) {
        try {
            String value = configCache.get(configKey);
            return value != null ? parser.apply(value) : defaultValue;
        } catch (Exception e) {
            log.warn("配置值转换失败: {}, 使用默认值: {}", configKey, defaultValue);
            return defaultValue;
        }
    }
    
    /**
     * 验证配置值是否有效
     */
    public boolean isValidConfig(String configKey) {
        String value = configCache.get(configKey);
        return value != null && !value.trim().isEmpty();
    }
    
    /**
     * 获取配置描述信息
     */
    public String getConfigInfo() {
        return String.format("系统配置工具类 - 缓存配置数量: %d, 自动刷新间隔: 30分钟", configCache.size());
    }
}
