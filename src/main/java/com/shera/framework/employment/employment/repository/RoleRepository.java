package com.shera.framework.employment.employment.repository;

import com.shera.framework.employment.employment.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 角色仓库接口
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    /**
     * 根据角色代码查找角色
     */
    Optional<Role> findByCode(String code);
    
    /**
     * 根据状态查找角色列表
     */
    List<Role> findByStatus(Integer status);
    
    /**
     * 根据状态分页查找角色列表
     */
    Page<Role> findByStatus(Integer status, Pageable pageable);
    
    /**
     * 根据用户ID查找角色列表
     */
    @Query("SELECT r FROM Role r " +
           "JOIN UserRole ur ON r.id = ur.roleId " +
           "WHERE ur.userId = :userId AND r.status = 1")
    List<Role> findByUserId(@Param("userId") Long userId);
    
    /**
     * 检查角色代码是否存在
     */
    boolean existsByCode(String code);
    
    /**
     * 检查角色代码是否存在（排除指定ID）
     */
    boolean existsByCodeAndIdNot(String code, Long id);
    
    /**
     * 根据ID列表查找角色
     */
    List<Role> findByIdIn(List<Long> ids);
    
    /**
     * 根据名称或代码模糊搜索角色（支持关键字搜索）
     */
    @Query("SELECT r FROM Role r WHERE " +
           "(LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.code) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Role> findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(
            @Param("keyword") String keyword, Pageable pageable);
    
    /**
     * 根据名称或代码模糊搜索角色并按状态过滤（支持关键字搜索）
     */
    @Query("SELECT r FROM Role r WHERE " +
           "(LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.code) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "r.status = :status")
    Page<Role> findByNameContainingIgnoreCaseOrCodeContainingIgnoreCaseAndStatus(
            @Param("keyword") String keyword, @Param("status") Integer status, Pageable pageable);
}
