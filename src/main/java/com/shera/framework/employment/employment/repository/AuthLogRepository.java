package com.shera.framework.employment.employment.repository;

import com.shera.framework.employment.employment.entity.AuthLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 实名认证记录数据访问接口
 */
@Repository
public interface AuthLogRepository extends JpaRepository<AuthLog, Long> {
    
    /**
     * 根据用户ID查询认证记录
     */
    List<AuthLog> findByUserId(Long userId);
    
    /**
     * 根据用户ID分页查询认证记录
     */
    Page<AuthLog> findByUserId(Long userId, Pageable pageable);
    
    /**
     * 根据认证类型查询认证记录
     */
    List<AuthLog> findByAuthType(Integer authType);
    
    /**
     * 根据状态查询认证记录
     */
    List<AuthLog> findByStatus(Integer status);
    
    /**
     * 根据用户ID和状态查询认证记录
     */
    List<AuthLog> findByUserIdAndStatus(Long userId, Integer status);
    
    /**
     * 根据用户ID和认证类型查询认证记录
     */
    List<AuthLog> findByUserIdAndAuthType(Long userId, Integer authType);
    
    /**
     * 根据用户ID、认证类型和状态查询认证记录
     */
    List<AuthLog> findByUserIdAndAuthTypeAndStatus(Long userId, Integer authType, Integer status);
    
    /**
     * 根据操作员ID查询认证记录
     */
    List<AuthLog> findByOperatorId(Long operatorId);
    
    /**
     * 根据创建时间范围查询认证记录
     */
    List<AuthLog> findByCreateTimeBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * 根据用户ID和创建时间范围查询认证记录
     */
    List<AuthLog> findByUserIdAndCreateTimeBetween(Long userId, LocalDateTime start, LocalDateTime end);
    
    /**
     * 统计用户认证记录数量
     */
    Long countByUserId(Long userId);
    
    /**
     * 统计认证类型数量
     */
    @Query("SELECT a.authType, COUNT(a) FROM AuthLog a GROUP BY a.authType")
    List<Object[]> countByAuthType();
    
    /**
     * 统计认证状态数量
     */
    @Query("SELECT a.status, COUNT(a) FROM AuthLog a GROUP BY a.status")
    List<Object[]> countByStatus();
    
    /**
     * 统计用户认证状态数量
     */
    @Query("SELECT a.status, COUNT(a) FROM AuthLog a WHERE a.userId = :userId GROUP BY a.status")
    List<Object[]> countByUserIdAndStatus(@Param("userId") Long userId);
    
    /**
     * 查询最新的认证记录
     */
    @Query("SELECT a FROM AuthLog a WHERE a.userId = :userId ORDER BY a.createTime DESC")
    List<AuthLog> findLatestByUserId(@Param("userId") Long userId, Pageable pageable);
    
    /**
     * 检查用户是否有成功的认证记录
     */
    boolean existsByUserIdAndStatus(Long userId, Integer status);
    
    /**
     * 检查用户是否有特定类型的认证记录
     */
    boolean existsByUserIdAndAuthType(Long userId, Integer authType);
    
    /**
     * 删除用户的认证记录
     */
    void deleteByUserId(Long userId);
    
    /**
     * 根据用户ID列表删除认证记录
     */
    void deleteByUserIdIn(List<Long> userIds);
}
