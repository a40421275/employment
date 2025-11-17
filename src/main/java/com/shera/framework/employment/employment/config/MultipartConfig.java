package com.shera.framework.employment.employment.config;

import com.shera.framework.employment.employment.util.SystemConfigUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import jakarta.servlet.MultipartConfigElement;

/**
 * 文件上传配置类
 * 动态从系统配置获取文件上传大小限制
 */
@Configuration
@RequiredArgsConstructor
public class MultipartConfig {
    
    private final SystemConfigUtil systemConfigUtil;
    
    /**
     * 配置文件上传大小限制
     * 从系统配置动态获取最大文件大小和请求大小
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        
        // 从系统配置获取最大文件大小和请求大小
        long maxFileSize = systemConfigUtil.getFileUploadMaxSize();
        long maxRequestSize = systemConfigUtil.getFileUploadMaxRequestSize();
        
        // 设置文件大小限制
        factory.setMaxFileSize(DataSize.ofBytes(maxFileSize));
        factory.setMaxRequestSize(DataSize.ofBytes(maxRequestSize));
        
        // 设置临时文件位置（可选）
        factory.setLocation("temp");
        
        return factory.createMultipartConfig();
    }
    
    /**
     * 获取当前文件上传配置信息
     */
    public String getMultipartConfigInfo() {
        long maxFileSize = systemConfigUtil.getFileUploadMaxSize();
        long maxRequestSize = systemConfigUtil.getFileUploadMaxRequestSize();
        String allowedTypes = systemConfigUtil.getAllowedFileTypes();
        
        return String.format(
            "文件上传配置: 最大文件大小=%d bytes (%.2f MB), 最大请求大小=%d bytes (%.2f MB), 允许类型=%s",
            maxFileSize, maxFileSize / (1024.0 * 1024.0),
            maxRequestSize, maxRequestSize / (1024.0 * 1024.0),
            allowedTypes
        );
    }
}
