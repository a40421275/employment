package com.shera.framework.employment.employment.repository;

import com.shera.framework.employment.employment.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 权限仓库接口
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
    /**
     * 根据权限代码查找权限
     */
    Optional<Permission> findByCode(String code);
    
    /**
     * 根据模块查找权限列表
     */
    List<Permission> findByModule(String module);
    
    /**
     * 根据状态查找权限列表
     */
    List<Permission> findByStatus(Integer status);
    
    /**
     * 根据权限类型查找权限列表
     */
    List<Permission> findByType(Integer type);
    
    /**
     * 根据父权限ID查找权限列表
     */
    List<Permission> findByParentId(Long parentId);
    
    /**
     * 根据状态和类型查找权限列表
     */
    List<Permission> findByStatusAndType(Integer status, Integer type);
    
    /**
     * 根据用户ID查找权限列表
     */
    @Query("SELECT DISTINCT p FROM Permission p " +
           "JOIN RolePermission rp ON p.id = rp.permissionId " +
           "JOIN UserRole ur ON rp.roleId = ur.roleId " +
           "WHERE ur.userId = :userId AND p.status = 1")
    List<Permission> findByUserId(@Param("userId") Long userId);
    
    /**
     * 根据角色ID查找权限列表
     */
    @Query("SELECT p FROM Permission p " +
           "JOIN RolePermission rp ON p.id = rp.permissionId " +
           "WHERE rp.roleId = :roleId AND p.status = 1")
    List<Permission> findByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 根据用户ID和权限类型查找权限列表
     */
    @Query("SELECT DISTINCT p FROM Permission p " +
           "JOIN RolePermission rp ON p.id = rp.permissionId " +
           "JOIN UserRole ur ON rp.roleId = ur.roleId " +
           "WHERE ur.userId = :userId AND p.status = 1 AND p.type = :type")
    List<Permission> findByUserIdAndType(@Param("userId") Long userId, @Param("type") Integer type);
    
    /**
     * 检查权限代码是否存在
     */
    boolean existsByCode(String code);
    
    /**
     * 检查权限代码是否存在（排除指定ID）
     */
    boolean existsByCodeAndIdNot(String code, Long id);
    
    /**
     * 根据ID列表查找权限
     */
    List<Permission> findByIdIn(List<Long> ids);
    
    /**
     * 根据排序序号升序查找权限列表
     */
    List<Permission> findAllByOrderBySortOrderAsc();
    
    /**
     * 根据父权限ID和状态查找权限列表
     */
    List<Permission> findByParentIdAndStatus(Long parentId, Integer status);
    
    /**
     * 根据父权限ID、状态和类型查找权限列表
     */
    List<Permission> findByParentIdAndStatusAndType(Long parentId, Integer status, Integer type);

    /**
     * 根据名称模糊搜索权限（分页）
     */
    Page<Permission> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
