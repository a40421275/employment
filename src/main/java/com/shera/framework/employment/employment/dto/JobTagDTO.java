package com.shera.framework.employment.employment.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 岗位标签数据传输对象
 */
@Data
public class JobTagDTO {
    
    /**
     * 标签ID
     */
    private Long id;
    
    /**
     * 岗位ID
     */
    private Long jobId;
    
    /**
     * 标签名称
     */
    private String tagName;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 使用次数（统计字段）
     */
    private Long usageCount;
    
    /**
     * 标签类型（0-系统标签，1-自定义标签）
     */
    private Integer tagType = 0;
    
    /**
     * 标签颜色
     */
    private String tagColor;
    
    /**
     * 标签描述
     */
    private String description;
}
