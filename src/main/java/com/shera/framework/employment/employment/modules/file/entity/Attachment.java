package com.shera.framework.employment.employment.modules.file.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.shera.framework.employment.employment.util.BaseEntity;

/**
 * 附件实体类 - 保存文件的业务属性
 * 一个物理文件可以被多个附件引用
 */
@Entity
@Table(name = "attachment")
@Data
@EqualsAndHashCode(callSuper = true)
public class Attachment extends BaseEntity {
    
    /**
     * 文件ID（关联文件表）
     */
    @Column(name = "file_id", nullable = false)
    private Long fileId;
    
    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    /**
     * 业务类型
     */
    @Column(name = "business_type", nullable = false, length = 50)
    private String businessType;
    
    /**
     * 业务ID
     */
    @Column(name = "business_id")
    private Long businessId;
    
    /**
     * 文件描述
     */
    @Column(name = "description", length = 500)
    private String description;
    
    /**
     * 文件标签，逗号分隔
     */
    @Column(name = "tags", length = 500)
    private String tags;
    
    /**
     * 是否公开：0-私有，1-公开
     */
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = false;
    
    /**
     * 是否临时文件：0-否，1-是
     */
    @Column(name = "is_temporary", nullable = false)
    private Boolean isTemporary = false;
    
    /**
     * 过期时间
     */
    @Column(name = "expire_time")
    private LocalDateTime expireTime;
    
    /**
     * 状态：1-正常，2-已删除，3-已过期
     */
    @Column(name = "status", nullable = false)
    private Integer status = Status.NORMAL.getCode();
    
    /**
     * 下载次数
     */
    @Column(name = "download_count", nullable = false)
    private Integer downloadCount = 0;
    
    /**
     * 查看次数
     */
    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;
    
    /**
     * 附件状态枚举
     */
    public enum Status {
        NORMAL(1, "正常"),
        DELETED(2, "已删除"),
        EXPIRED(3, "已过期");
        
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
     * 业务类型枚举
     */
    public enum BusinessType {
        RESUME("resume", "简历"),
        AVATAR("avatar", "头像"),
        ID_CARD("id_card", "身份证"),
        COMPANY_LOGO("company_logo", "企业Logo"),
        JOB_ATTACHMENT("job_attachment", "岗位附件"),
        OTHER("other", "其他");
        
        private final String code;
        private final String desc;
        
        BusinessType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        
        public String getCode() {
            return code;
        }
        
        public String getDesc() {
            return desc;
        }
        
        public static BusinessType fromCode(String code) {
            for (BusinessType type : BusinessType.values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            return OTHER;
        }
    }
    
    /**
     * 增加下载次数
     */
    public void incrementDownloadCount() {
        this.downloadCount++;
    }
    
    /**
     * 增加查看次数
     */
    public void incrementViewCount() {
        this.viewCount++;
    }
    
    /**
     * 检查附件是否过期
     */
    public boolean isExpired() {
        if (expireTime == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(expireTime);
    }
    
    /**
     * 检查附件是否可下载
     */
    public boolean isDownloadable() {
        return status == Status.NORMAL.getCode() && !isExpired();
    }
}
