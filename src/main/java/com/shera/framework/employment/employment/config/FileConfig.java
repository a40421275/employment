package com.shera.framework.employment.employment.config;

import com.shera.framework.employment.employment.util.SystemConfigUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 文件配置管理类
 * 统一管理文件相关的配置，提供便捷的配置获取方法
 * 使用系统配置工具类，提高配置访问性能
 */
@Component
@RequiredArgsConstructor
public class FileConfig {
    
    private final SystemConfigUtil systemConfigUtil;
    
    // 默认配置值
    private static final String DEFAULT_UPLOAD_DIR = "uploads/";
    private static final String DEFAULT_BASE_URL = "http://localhost:8080";
    private static final long DEFAULT_MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final Set<String> DEFAULT_ALLOWED_FILE_TYPES = Set.of(
        "pdf", "doc", "docx", "jpg", "jpeg", "png", "gif", "txt"
    );
    
    /**
     * 获取文件上传目录
     */
    public String getUploadDir() {
        return systemConfigUtil.getString(SystemConfigUtil.FILE_STORAGE_PATH, DEFAULT_UPLOAD_DIR);
    }
    
    /**
     * 获取文件访问基础URL
     */
    public String getBaseUrl() {
        return systemConfigUtil.getString(SystemConfigUtil.FILE_BASE_URL, DEFAULT_BASE_URL);
    }
    
    /**
     * 获取最大文件大小（字节）
     */
    public long getMaxFileSize() {
        return systemConfigUtil.getLong(SystemConfigUtil.FILE_UPLOAD_MAX_SIZE, DEFAULT_MAX_FILE_SIZE);
    }
    
    /**
     * 获取允许的文件类型（逗号分隔的字符串）
     */
    public String getAllowedFileTypes() {
        return systemConfigUtil.getString(SystemConfigUtil.FILE_ALLOWED_TYPES, String.join(",", DEFAULT_ALLOWED_FILE_TYPES));
    }
    
    /**
     * 获取允许的文件类型（Set集合）
     */
    public Set<String> getAllowedFileTypesSet() {
        String allowedTypes = getAllowedFileTypes();
        return Set.of(allowedTypes.split(","));
    }
    
    /**
     * 获取文件存储类型
     */
    public String getStorageType() {
        return systemConfigUtil.getString(SystemConfigUtil.FILE_STORAGE_TYPE, "local");
    }
    
    /**
     * 验证文件类型是否允许
     */
    public boolean isFileTypeAllowed(String fileExtension) {
        Set<String> allowedTypes = getAllowedFileTypesSet();
        return allowedTypes.contains(fileExtension.toLowerCase());
    }
    
    /**
     * 验证文件大小是否允许
     */
    public boolean isFileSizeAllowed(long fileSize) {
        return fileSize <= getMaxFileSize();
    }
    
    /**
     * 获取格式化后的最大文件大小（用于显示）
     */
    public String getFormattedMaxFileSize() {
        long maxSize = getMaxFileSize();
        if (maxSize < 1024) {
            return maxSize + " B";
        } else if (maxSize < 1024 * 1024) {
            return (maxSize / 1024) + " KB";
        } else {
            return (maxSize / (1024 * 1024)) + " MB";
        }
    }
    
    /**
     * 刷新文件配置缓存
     */
    public void refreshConfig() {
        systemConfigUtil.refreshConfig(SystemConfigUtil.FILE_UPLOAD_MAX_SIZE);
        systemConfigUtil.refreshConfig(SystemConfigUtil.FILE_ALLOWED_TYPES);
        systemConfigUtil.refreshConfig(SystemConfigUtil.FILE_STORAGE_PATH);
        systemConfigUtil.refreshConfig(SystemConfigUtil.FILE_STORAGE_TYPE);
    }
    
    /**
     * 获取文件配置信息（用于调试和监控）
     */
    public String getConfigInfo() {
        return String.format(
            "文件配置信息: 上传目录=%s, 最大大小=%s, 允许类型=%s, 存储类型=%s",
            getUploadDir(),
            getFormattedMaxFileSize(),
            getAllowedFileTypes(),
            getStorageType()
        );
    }
}
