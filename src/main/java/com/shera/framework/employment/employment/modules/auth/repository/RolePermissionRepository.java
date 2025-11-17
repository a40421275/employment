package com.shera.framework.employment.employment.modules.auth.repository;

import com.shera.framework.employment.employment.modules.auth.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 角色权限关联仓库接口
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    
    /**
     * 根据角色ID查找关联关系
     */
    List<RolePermission> findByRoleId(Long roleId);
    
    /**
     * 根据权限ID查找关联关系
     */
    List<RolePermission> findByPermissionId(Long permissionId);
    
    /**
     * 根据角色ID和权限ID查找关联关系
     */
    Optional<RolePermission> findByRoleIdAndPermissionId(Long roleId, Long permissionId);
    
    /**
     * 检查角色权限关联是否存在
     */
    boolean existsByRoleIdAndPermissionId(Long roleId, Long permissionId);
    
    /**
     * 根据角色ID删除所有关联关系
     */
    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.roleId = :roleId")
    void deleteByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 根据权限ID删除所有关联关系
     */
    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.permissionId = :permissionId")
    void deleteByPermissionId(@Param("permissionId") Long permissionId);
    
    /**
     * 根据角色ID列表删除关联关系
     */
    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.roleId IN :roleIds")
    void deleteByRoleIdIn(@Param("roleIds") List<Long> roleIds);
    
    /**
     * 根据权限ID列表删除关联关系
     */
    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.permissionId IN :permissionIds")
    void deleteByPermissionIdIn(@Param("permissionIds") List<Long> permissionIds);
    
    /**
     * 根据角色ID列表查找关联关系
     */
    List<RolePermission> findByRoleIdIn(List<Long> roleIds);
    
    /**
     * 根据角色ID统计权限数量
     */
    @Query("SELECT COUNT(rp) FROM RolePermission rp WHERE rp.roleId = :roleId")
    long countByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据权限ID统计角色数量
     */
    @Query("SELECT COUNT(rp) FROM RolePermission rp WHERE rp.permissionId = :permissionId")
    long countByPermissionId(@Param("permissionId") Long permissionId);
}
