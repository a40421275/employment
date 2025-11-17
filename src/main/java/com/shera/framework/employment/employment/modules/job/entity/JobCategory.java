package com.shera.framework.employment.employment.modules.job.entity;

import com.shera.framework.employment.employment.util.BaseEntity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 岗位分类实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "job_category")
public class JobCategory extends BaseEntity {
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "parent_id")
    private Long parentId;
    
    @Column(name = "level", nullable = false)
    private Integer level = 1;
    
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;
    
    @Column(name = "description", length = 500)
    private String description;
    
    @Column(name = "icon", length = 200)
    private String icon;
    
    @Column(name = "color", length = 50)
    private String color;
    
    @Column(name = "status", nullable = false)
    private Integer status = 1; // 0-禁用，1-启用
    
    // 状态枚举
    public enum Status {
        DISABLED(0, "禁用"),
        ENABLED(1, "启用");
        
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
    }
}
