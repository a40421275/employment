package com.shera.framework.employment.employment.modules.auth.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import com.shera.framework.employment.employment.util.BaseEntity;

/**
 * 权限实体类
 */
@Entity
@Table(name = "permission")
@Data
@EqualsAndHashCode(callSuper = true)
public class Permission extends BaseEntity {
    
    /**
     * 权限名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    /**
     * 权限代码
     */
    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code;
    
    /**
     * 权限描述
     */
    @Column(name = "description", length = 500)
    private String description;
    
    /**
     * 权限类型：1-菜单，2-按钮，3-接口
     */
    @Column(name = "type", nullable = false)
    private Integer type = 1;
    
    /**
     * 父权限ID
     */
    @Column(name = "parent_id")
    private Long parentId;
    
    /**
     * 权限URL
     */
    @Column(name = "url", length = 200)
    private String url;
    
    /**
     * 请求方法：GET, POST, PUT, DELETE等
     */
    @Column(name = "method", length = 50)
    private String method;
    
    /**
     * 排序序号
     */
    @Column(name = "sort_order")
    private Integer sortOrder = 1;
    
    /**
     * 图标
     */
    @Column(name = "icon", length = 50)
    private String icon;
    
    /**
     * 所属模块
     */
    @Column(name = "module", length = 50)
    private String module;
    
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
