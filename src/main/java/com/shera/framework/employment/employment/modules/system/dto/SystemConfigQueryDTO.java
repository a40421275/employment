package com.shera.framework.employment.employment.modules.system.dto;

import lombok.Data;
import org.springframework.data.domain.Pageable;

/**
 * 系统配置查询DTO
 */
@Data
public class SystemConfigQueryDTO {
    
    /**
     * 配置键（精确匹配）
     */
    private String configKey;
    
    /**
     * 配置键前缀
     */
    private String configKeyPrefix;
    
    /**
     * 配置键后缀
     */
    private String configKeySuffix;
    
    /**
     * 配置键包含的关键词
     */
    private String configKeyKeyword;
    
    /**
     * 配置值包含的关键词
     */
    private String configValueKeyword;
    
    /**
     * 描述包含的关键词
     */
    private String descriptionKeyword;
    
    /**
     * 配置分组
     */
    private String configGroup;
    
    /**
     * 配置值类型
     */
    private String valueType;
    
    /**
     * 是否可编辑
     */
    private Boolean editable;
    
    /**
     * 是否为系统配置
     */
    private Boolean systemConfig;
    
    /**
     * 创建时间范围 - 开始时间
     */
    private String createTimeStart;
    
    /**
     * 创建时间范围 - 结束时间
     */
    private String createTimeEnd;
    
    /**
     * 更新时间范围 - 开始时间
     */
    private String updateTimeStart;
    
    /**
     * 更新时间范围 - 结束时间
     */
    private String updateTimeEnd;
    
    /**
     * 排序字段
     * 支持：id, configKey, configValue, description, createTime, updateTime
     */
    private String sortField = "id";
    
    /**
     * 排序方向：asc, desc
     */
    private String sortDirection = "asc";
    
    /**
     * 页码
     */
    private Integer page = 0;
    
    /**
     * 每页大小
     */
    private Integer size = 20;
    
    /**
     * 是否启用分页
     */
    private Boolean enablePagination = true;
    
    /**
     * 获取分页信息
     */
    public Pageable getPageable() {
        if (!enablePagination) {
            return Pageable.unpaged();
        }
        
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by(
            "desc".equalsIgnoreCase(sortDirection) ? 
            org.springframework.data.domain.Sort.Direction.DESC : 
            org.springframework.data.domain.Sort.Direction.ASC, 
            sortField
        );
        
        return org.springframework.data.domain.PageRequest.of(page, size, sort);
    }
    
    /**
     * 检查是否有查询条件
     */
    public boolean hasQueryConditions() {
        return configKey != null || configKeyPrefix != null || configKeySuffix != null ||
               configKeyKeyword != null || configValueKeyword != null || descriptionKeyword != null ||
               configGroup != null || valueType != null || editable != null || systemConfig != null ||
               createTimeStart != null || createTimeEnd != null || updateTimeStart != null || updateTimeEnd != null;
    }
}
