package com.shera.framework.employment.employment.modules.system.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统配置数据传输对象
 */
@Data
public class SystemConfigDTO {
    
    private Long id;
    
    /**
     * 配置键
     */
    private String configKey;
    
    /**
     * 配置值
     */
    private String configValue;
    
    /**
     * 配置描述
     */
    private String description;
    
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
    private Boolean editable = true;
    
    /**
     * 是否为系统配置
     */
    private Boolean systemConfig = false;
    
    /**
     * 排序顺序
     */
    private Integer sortOrder = 0;
    
    /**
     * 验证规则
     */
    private String validationRule;
    
    /**
     * 默认值
     */
    private String defaultValue;
    
    /**
     * 选项值（JSON格式）
     */
    private String options;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
