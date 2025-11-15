package com.shera.framework.employment.employment.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 岗位分类数据传输对象
 */
@Data
public class JobCategoryDTO {
    
    /**
     * 分类ID
     */
    private Long id;
    
    /**
     * 分类名称
     */
    private String name;
    
    /**
     * 分类描述
     */
    private String description;
    
    /**
     * 父分类ID
     */
    private Long parentId;
    
    /**
     * 分类层级
     */
    private Integer level;
    
    /**
     * 排序序号
     */
    private Integer sortOrder;
    
    /**
     * 分类状态（0: 禁用, 1: 启用）
     */
    private Integer status;
    
    /**
     * 分类图标
     */
    private String icon;
    
    /**
     * 分类颜色
     */
    private String color;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 子分类数量
     */
    private Long childrenCount;
    
    /**
     * 岗位数量
     */
    private Long jobCount;
}
