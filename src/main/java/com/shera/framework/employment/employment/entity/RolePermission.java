package com.shera.framework.employment.employment.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色权限关联实体类
 */
@Entity
@Table(name = "role_permission", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"role_id", "permission_id"})
})
@Data
public class RolePermission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 角色ID
     */
    @Column(name = "role_id", nullable = false)
    private Long roleId;
    
    /**
     * 权限ID
     */
    @Column(name = "permission_id", nullable = false)
    private Long permissionId;
    
    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime = LocalDateTime.now();
    
    /**
     * 角色实体
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;
    
    /**
     * 权限实体
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", insertable = false, updatable = false)
    private Permission permission;
}
