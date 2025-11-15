package com.shera.framework.employment.employment.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限数据传输对象
 */
@Data
public class PermissionDTO {
    
    /**
     * 权限ID
     */
    private Long id;
    
    /**
     * 权限名称
     */
    private String name;
    
    /**
     * 权限代码
     */
    private String code;
    
    /**
     * 权限描述
     */
    private String description;
    
    /**
     * 权限类型：1-菜单，2-按钮，3-接口
     */
    private Integer type;
    
    /**
     * 父权限ID
     */
    private Long parentId;
    
    /**
     * 父权限名称
     */
    private String parentName;
    
    /**
     * 权限URL
     */
    private String url;
    
    /**
     * 请求方法：GET, POST, PUT, DELETE等
     */
    private String method;
    
    /**
     * 排序序号
     */
    private Integer sortOrder;
    
    /**
     * 图标
     */
    private String icon;
    
    /**
     * 所属模块
     */
    private String module;
    
    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
    
    /**
     * 子权限列表
     */
    private List<PermissionDTO> children;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
