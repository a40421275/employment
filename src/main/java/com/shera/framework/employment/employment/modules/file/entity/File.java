package com.shera.framework.employment.employment.modules.file.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.shera.framework.employment.employment.util.BaseEntity;

/**
 * 文件实体类 - 只保存物理文件信息
 * 一个物理文件在系统中只存储一份，可以被多个附件引用
 */
@Entity
@Table(name = "file")
@Data
@EqualsAndHashCode(callSuper = true)
public class File extends BaseEntity {
    
    /**
     * 存储文件名
     */
    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;
    
    /**
     * 原始文件名
     */
    @Column(name = "original_name", nullable = false, length = 255)
    private String originalName;
    
    /**
     * 文件存储路径
     */
    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;
    
    /**
     * 文件大小（字节）
     */
    @Column(name = "file_size", nullable = false)
    private Long fileSize;
    
    /**
     * 文件类型
     */
    @Column(name = "file_type", nullable = false, length = 50)
    private String fileType;
    
    /**
     * MIME类型
     */
    @Column(name = "mime_type", nullable = false, length = 100)
    private String mimeType;
    
    /**
     * 文件扩展名
     */
    @Column(name = "file_extension", length = 20)
    private String fileExtension;
    
    /**
     * 文件哈希值（SHA-256） - 用于重复文件检测
     */
    @Column(name = "file_hash", nullable = false, unique = true, length = 64)
    private String fileHash;
    
    /**
     * 创建者用户ID
     */
    @Column(name = "creator_user_id", nullable = false)
    private Long creatorUserId;
    
    /**
     * 引用计数 - 记录有多少个附件在使用这个文件
     */
    @Column(name = "reference_count", nullable = false)
    private Integer referenceCount = 0;
    
    /**
     * 状态：1-正常，2-已删除
     */
    @Column(name = "status", nullable = false)
    private Integer status = Status.NORMAL.getCode();
    
    /**
     * 最后访问时间
     */
    @Column(name = "last_access_time")
    private LocalDateTime lastAccessTime;
    
    /**
     * 文件状态枚举
     */
    public enum Status {
        NORMAL(1, "正常"),
        DELETED(2, "已删除");
        
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
        
        public static Status fromCode(int code) {
            for (Status status : Status.values()) {
                if (status.code == code) {
                    return status;
                }
            }
            return NORMAL;
        }
    }
    
    /**
     * 检查文件是否可下载
     */
    public boolean isDownloadable() {
        return status == Status.NORMAL.getCode();
    }
    
    /**
     * 获取文件大小格式化显示
     */
    public String getFormattedFileSize() {
        if (fileSize == null) {
            return "0 B";
        }
        
        if (fileSize < 1024) {
            return fileSize + " B";
        } else if (fileSize < 1024 * 1024) {
            return String.format("%.2f KB", fileSize / 1024.0);
        } else if (fileSize < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", fileSize / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", fileSize / (1024.0 * 1024.0 * 1024.0));
        }
    }
    
    /**
     * 增加引用计数
     */
    public void incrementReferenceCount() {
        this.referenceCount++;
        this.lastAccessTime = LocalDateTime.now();
    }
    
    /**
     * 减少引用计数
     */
    public void decrementReferenceCount() {
        if (this.referenceCount > 0) {
            this.referenceCount--;
            this.lastAccessTime = LocalDateTime.now();
        }
    }
    
    /**
     * 检查是否可以被删除（引用计数为0）
     */
    public boolean canBeDeleted() {
        return this.referenceCount <= 0;
    }
}
