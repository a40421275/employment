package com.shera.framework.employment.employment.modules.auth.repository;

import com.shera.framework.employment.employment.modules.auth.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户角色关联仓库接口
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    
    /**
     * 根据用户ID查找关联关系
     */
    List<UserRole> findByUserId(Long userId);
    
    /**
     * 根据角色ID查找关联关系
     */
    List<UserRole> findByRoleId(Long roleId);
    
    /**
     * 根据用户ID和角色ID查找关联关系
     */
    Optional<UserRole> findByUserIdAndRoleId(Long userId, Long roleId);
    
    /**
     * 检查用户角色关联是否存在
     */
    boolean existsByUserIdAndRoleId(Long userId, Long roleId);
    
    /**
     * 根据用户ID删除所有关联关系
     */
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);
    
    /**
     * 根据角色ID删除所有关联关系
     */
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.roleId = :roleId")
    void deleteByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 根据用户ID列表删除关联关系
     */
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.userId IN :userIds")
    void deleteByUserIdIn(@Param("userIds") List<Long> userIds);
    
    /**
     * 根据角色ID列表删除关联关系
     */
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.roleId IN :roleIds")
    void deleteByRoleIdIn(@Param("roleIds") List<Long> roleIds);
    
    /**
     * 根据用户ID和角色ID删除关联关系
     */
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.userId = :userId AND ur.roleId = :roleId")
    void deleteByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);
    
    /**
     * 根据用户ID和角色ID列表删除关联关系
     */
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.userId = :userId AND ur.roleId IN :roleIds")
    void deleteByUserIdAndRoleIdIn(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
    
    /**
     * 根据角色ID统计用户数量
     */
    @Query("SELECT COUNT(ur) FROM UserRole ur WHERE ur.roleId = :roleId")
    long countByRoleId(@Param("roleId") Long roleId);
}
