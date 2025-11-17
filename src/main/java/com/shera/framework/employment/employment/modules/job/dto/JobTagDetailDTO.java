package com.shera.framework.employment.employment.modules.job.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 标签详情DTO
 */
@Data
public class JobTagDetailDTO {
    
    /**
     * 标签ID
     */
    private Long id;
    
    /**
     * 标签名称
     */
    private String tagName;
    
    /**
     * 标签颜色
     */
    private String tagColor;
    
    /**
     * 标签类型：0-系统标签，1-自定义标签
     */
    private Integer tagType;
    
    /**
     * 标签描述
     */
    private String description;
    
    /**
     * 排序权重
     */
    private Integer sortOrder;
    
    /**
     * 标签状态：0-正常，1-禁用
     */
    private Integer status;
    
    /**
     * 使用次数
     */
    private Long usageCount;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 岗位ID（如果有关联岗位）
     */
    private Long jobId;
    
    /**
     * 是否独立标签
     */
    private Boolean independent;
    
    /**
     * 是否系统预定义标签
     */
    private Boolean systemTag;
    
    public JobTagDetailDTO() {
        this.sortOrder = 0;
        this.status = 0;
        this.usageCount = 0L;
        this.independent = false;
        this.systemTag = false;
    }
}
