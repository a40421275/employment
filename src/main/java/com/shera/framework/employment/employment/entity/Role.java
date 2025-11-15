package com.shera.framework.employment.employment.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 角色实体类
 */
@Entity
@Table(name = "role")
@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {
    
    /**
     * 角色名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    /**
     * 角色代码
     */
    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code;
    
    /**
     * 角色描述
     */
    @Column(name = "description", length = 500)
    private String description;
    
    /**
     * 角色类型：1-系统角色，2-业务角色，3-自定义角色
     */
    @Column(name = "type", nullable = false)
    private Integer type = 3;
    
    /**
     * 状态：0-禁用，1-启用
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;
    
    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime = LocalDateTime.now();
    
    /**
     * 更新时间
     */
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime = LocalDateTime.now();
}
