package com.shera.framework.employment.employment.modules.resume.entity;

import com.shera.framework.employment.employment.util.BaseEntity;
import com.shera.framework.employment.employment.modules.user.entity.User;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 简历实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "resume")
public class Resume extends BaseEntity {
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    
    @Column(name = "content", columnDefinition = "TEXT")
    private String content; // JSON格式的简历内容
    
    @Column(name = "file_url", length = 500)
    private String fileUrl;
    
    @Column(name = "file_type", length = 20)
    private String fileType; // pdf/doc/docx
    
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;
    
    @Column(name = "privacy_level", nullable = false)
    private Integer privacyLevel = 1; // 1-公开，2-仅投递企业，3-隐藏
    
    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;
    
    // 文件类型枚举
    public enum FileType {
        PDF("pdf", "PDF"),
        DOC("doc", "Word文档"),
        DOCX("docx", "Word文档");
        
        private final String code;
        private final String desc;
        
        FileType(String code, String desc) {
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
    
    // 隐私级别枚举
    public enum PrivacyLevel {
        PUBLIC(1, "公开"),
        COMPANY_ONLY(2, "仅投递企业"),
        HIDDEN(3, "隐藏");
        
        private final int code;
        private final String desc;
        
        PrivacyLevel(int code, String desc) {
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
