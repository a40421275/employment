package com.shera.framework.employment.employment.modules.resume.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;
    
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    
    @Column(name = "resume_type", nullable = false)
    private Integer resumeType = 1; // 1-附件简历，2-结构化简历
    
    @Column(name = "file_url", length = 500)
    private String fileUrl; // 附件简历文件路径
    
    @Column(name = "file_type", length = 20)
    private String fileType; // pdf/doc/docx
    
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;
    
    @Column(name = "privacy_level", nullable = false)
    private Integer privacyLevel = 1; // 1-公开，2-仅投递企业，3-隐藏
    
    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;
    
    @Column(name = "download_count", nullable = false)
    private Integer downloadCount = 0; // 下载次数
    
    @Column(name = "share_count", nullable = false)
    private Integer shareCount = 0; // 分享次数
    
    @Column(name = "last_view_time")
    private java.time.LocalDateTime lastViewTime; // 最后查看时间
    
    @Column(name = "last_update_time")
    private java.time.LocalDateTime lastUpdateTime; // 最后更新时间
    
    @Column(name = "sync_profile_data", nullable = false)
    private Boolean syncProfileData = true; // 是否同步用户资料
    
    @Column(name = "last_sync_time")
    private java.time.LocalDateTime lastSyncTime; // 最后同步时间
    
    @Column(name = "structured_data", columnDefinition = "TEXT")
    private String structuredData; // JSON格式的结构化简历数据
    
    @Column(name = "basic_info", columnDefinition = "TEXT")
    private String basicInfo; // JSON格式的基础信息
    
    @Column(name = "template_style", length = 50)
    private String templateStyle; // 模板样式
    
    @Column(name = "color_scheme", length = 50)
    private String colorScheme; // 配色方案
    
    @Column(name = "font_family", length = 50)
    private String fontFamily; // 字体
    
    @Column(name = "show_photo", nullable = false)
    private Boolean showPhoto = true; // 是否显示照片
    
    @Column(name = "show_salary", nullable = false)
    private Boolean showSalary = false; // 是否显示薪资
    
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
