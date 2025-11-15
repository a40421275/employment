package com.shera.framework.employment.employment.dto;

import lombok.Data;
import org.springframework.data.domain.Pageable;

/**
 * 岗位标签查询DTO
 */
@Data
public class JobTagQueryDTO {
    
    /**
     * 标签名称（支持模糊搜索）
     */
    private String tagName;
    
    /**
     * 标签类型（0-系统标签，1-自定义标签）
     */
    private Integer tagType;
    
    /**
     * 岗位ID（可选，用于查询特定岗位的标签）
     */
    private Long jobId;
    
    /**
     * 是否只查询独立标签（jobId为null的标签）
     */
    private Boolean independentOnly = false;
    
    /**
     * 是否只查询关联标签（jobId不为null的标签）
     */
    private Boolean associatedOnly = false;
    
    /**
     * 排序字段（tagName, createTime, usageCount）
     */
    private String sortBy = "tagName";
    
    /**
     * 排序方向（asc, desc）
     */
    private String sortDirection = "asc";
    
    /**
     * 分页信息
     */
    private Pageable pageable;
    
    /**
     * 获取排序字段
     */
    public String getSortField() {
        switch (sortBy) {
            case "createTime":
                return "createTime";
            case "usageCount":
                return "usageCount";
            case "tagName":
            default:
                return "tagName";
        }
    }
    
    /**
     * 检查是否按使用次数排序
     */
    public boolean isSortByUsageCount() {
        return "usageCount".equals(sortBy);
    }
    
    /**
     * 检查是否按创建时间排序
     */
    public boolean isSortByCreateTime() {
        return "createTime".equals(sortBy);
    }
    
    /**
     * 检查是否按标签名称排序
     */
    public boolean isSortByTagName() {
        return "tagName".equals(sortBy);
    }
    
    /**
     * 检查是否降序排序
     */
    public boolean isDescending() {
        return "desc".equalsIgnoreCase(sortDirection);
    }
}
