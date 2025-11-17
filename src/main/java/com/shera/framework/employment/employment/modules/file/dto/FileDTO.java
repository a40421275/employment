package com.shera.framework.employment.employment.modules.file.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文件数据传输对象
 */
@Data
public class FileDTO {
    
    /**
     * 文件ID
     */
    private Long id;
    
    /**
     * 文件名
     */
    private String fileName;
    
    /**
     * 原始文件名
     */
    private String originalName;
    
    /**
     * 文件存储路径
     */
    private String filePath;
    
    /**
     * 文件大小（字节）
     */
    private Long fileSize;
    
    /**
     * 格式化文件大小
     */
    private String formattedFileSize;
    
    /**
     * 文件类型
     */
    private String fileType;
    
    /**
     * MIME类型
     */
    private String mimeType;
    
    /**
     * 文件扩展名
     */
    private String fileExtension;
    
    /**
     * 文件哈希值（SHA-256）
     */
    private String fileHash;
    
    /**
     * 上传用户ID
     */
    private Long userId;
    
    /**
     * 上传用户名
     */
    private String userName;
    
    /**
     * 业务类型
     */
    private String businessType;
    
    /**
     * 业务类型描述
     */
    private String businessTypeDesc;
    
    /**
     * 业务ID
     */
    private Long businessId;
    
    /**
     * 文件描述
     */
    private String description;
    
    /**
     * 文件标签，逗号分隔
     */
    private String tags;
    
    /**
     * 下载次数
     */
    private Integer downloadCount = 0;
    
    /**
     * 查看次数
     */
    private Integer viewCount = 0;
    
    /**
     * 是否公开：0-私有，1-公开
     */
    private Boolean isPublic = false;
    
    /**
     * 是否临时文件：0-否，1-是
     */
    private Boolean isTemporary = false;
    
    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;
    
    /**
     * 状态：1-正常，2-已删除，3-已过期
     */
    private Integer status = 1;
    
    /**
     * 状态描述
     */
    private String statusDesc;
    
    /**
     * 版本号
     */
    private Integer version = 1;
    
    /**
     * 父文件ID（用于版本管理）
     */
    private Long parentFileId;
    
    /**
     * 文件URL
     */
    private String fileUrl;
    
    /**
     * 文件预览URL
     */
    private String previewUrl;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    /**
     * 检查文件是否可下载
     */
    public boolean isDownloadable() {
        return status == 1 && (expireTime == null || LocalDateTime.now().isBefore(expireTime));
    }
    
    /**
     * 检查文件是否过期
     */
    public boolean isExpired() {
        return expireTime != null && LocalDateTime.now().isAfter(expireTime);
    }
}
