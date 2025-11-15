package com.shera.framework.employment.employment.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色数据传输对象
 */
@Data
public class RoleDTO {
    
    /**
     * 角色ID
     */
    private Long id;
    
    /**
     * 角色名称
     */
    private String name;
    
    /**
     * 角色代码
     */
    private String code;
    
    /**
     * 角色描述
     */
    private String description;
    
    /**
     * 角色类型：1-系统角色，2-业务角色，3-自定义角色
     */
    private Integer type;
    
    /**
     * 角色状态：1-启用，0-禁用
     */
    private Integer status;
    
    /**
     * 用户数量
     */
    private Long userCount;
    
    /**
     * 权限数量
     */
    private Long permissionCount;
    
    /**
     * 权限列表
     */
    private List<PermissionDTO> permissions;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
