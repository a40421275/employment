package com.shera.framework.employment.employment.modules.log.repository;

import com.shera.framework.employment.employment.modules.log.entity.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志数据访问接口
 */
@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {
    
    /**
     * 根据管理员ID查询操作日志
     */
    List<OperationLog> findByAdminId(Long adminId);
    
    /**
     * 根据管理员ID分页查询操作日志
     */
    Page<OperationLog> findByAdminId(Long adminId, Pageable pageable);
    
    /**
     * 根据操作类型查询操作日志
     */
    List<OperationLog> findByOperation(String operation);
    
    /**
     * 根据模块名称查询操作日志
     */
    List<OperationLog> findByModule(String module);
    
    /**
     * 根据操作类型和模块名称查询操作日志
     */
    List<OperationLog> findByOperationAndModule(String operation, String module);
    
    /**
     * 根据管理员ID和操作类型查询操作日志
     */
    List<OperationLog> findByAdminIdAndOperation(Long adminId, String operation);
    
    /**
     * 根据管理员ID和模块名称查询操作日志
     */
    List<OperationLog> findByAdminIdAndModule(Long adminId, String module);
    
    /**
     * 根据IP地址查询操作日志
     */
    List<OperationLog> findByIpAddress(String ipAddress);
    
    /**
     * 根据创建时间范围查询操作日志
     */
    List<OperationLog> findByCreateTimeBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * 根据管理员ID和创建时间范围查询操作日志
     */
    List<OperationLog> findByAdminIdAndCreateTimeBetween(Long adminId, LocalDateTime start, LocalDateTime end);
    
    /**
     * 根据操作类型和创建时间范围查询操作日志
     */
    List<OperationLog> findByOperationAndCreateTimeBetween(String operation, LocalDateTime start, LocalDateTime end);
    
    /**
     * 根据模块名称和创建时间范围查询操作日志
     */
    List<OperationLog> findByModuleAndCreateTimeBetween(String module, LocalDateTime start, LocalDateTime end);
    
    /**
     * 统计管理员的操作日志数量
     */
    Long countByAdminId(Long adminId);
    
    /**
     * 统计操作类型的数量
     */
    @Query("SELECT ol.operation, COUNT(ol) FROM OperationLog ol GROUP BY ol.operation ORDER BY COUNT(ol) DESC")
    List<Object[]> countByOperation();
    
    /**
     * 统计模块名称的数量
     */
    @Query("SELECT ol.module, COUNT(ol) FROM OperationLog ol GROUP BY ol.module ORDER BY COUNT(ol) DESC")
    List<Object[]> countByModule();
    
    /**
     * 统计管理员的操作类型数量
     */
    @Query("SELECT ol.operation, COUNT(ol) FROM OperationLog ol WHERE ol.adminId = :adminId GROUP BY ol.operation ORDER BY COUNT(ol) DESC")
    List<Object[]> countByAdminIdAndOperation(@Param("adminId") Long adminId);
    
    /**
     * 统计管理员的模块名称数量
     */
    @Query("SELECT ol.module, COUNT(ol) FROM OperationLog ol WHERE ol.adminId = :adminId GROUP BY ol.module ORDER BY COUNT(ol) DESC")
    List<Object[]> countByAdminIdAndModule(@Param("adminId") Long adminId);
    
    /**
     * 查询热门操作类型
     */
    @Query("SELECT ol.operation, COUNT(ol) as count FROM OperationLog ol GROUP BY ol.operation ORDER BY count DESC")
    List<Object[]> findHotOperations(@Param("limit") int limit);
    
    /**
     * 查询热门模块
     */
    @Query("SELECT ol.module, COUNT(ol) as count FROM OperationLog ol GROUP BY ol.module ORDER BY count DESC")
    List<Object[]> findHotModules(@Param("limit") int limit);
    
    /**
     * 查询管理员的最新操作日志
     */
    @Query("SELECT ol FROM OperationLog ol WHERE ol.adminId = :adminId ORDER BY ol.createTime DESC")
    List<OperationLog> findLatestByAdminId(@Param("adminId") Long adminId, Pageable pageable);
    
    /**
     * 查询最新的操作日志
     */
    @Query("SELECT ol FROM OperationLog ol ORDER BY ol.createTime DESC")
    List<OperationLog> findLatestLogs(Pageable pageable);
    
    /**
     * 根据关键词搜索操作日志
     */
    @Query("SELECT ol FROM OperationLog ol WHERE ol.operation LIKE %:keyword% OR ol.module LIKE %:keyword% OR ol.description LIKE %:keyword% OR ol.ipAddress LIKE %:keyword%")
    List<OperationLog> searchByKeyword(@Param("keyword") String keyword);
    
    /**
     * 根据关键词分页搜索操作日志
     */
    @Query("SELECT ol FROM OperationLog ol WHERE ol.operation LIKE %:keyword% OR ol.module LIKE %:keyword% OR ol.description LIKE %:keyword% OR ol.ipAddress LIKE %:keyword%")
    Page<OperationLog> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * 统计每日操作数量
     */
    @Query("SELECT DATE(ol.createTime), COUNT(ol) FROM OperationLog ol GROUP BY DATE(ol.createTime) ORDER BY DATE(ol.createTime) DESC")
    List<Object[]> countByDay();
    
    /**
     * 统计管理员每日操作数量
     */
    @Query("SELECT DATE(ol.createTime), COUNT(ol) FROM OperationLog ol WHERE ol.adminId = :adminId GROUP BY DATE(ol.createTime) ORDER BY DATE(ol.createTime) DESC")
    List<Object[]> countByAdminIdAndDay(@Param("adminId") Long adminId);
    
    /**
     * 统计模块每日操作数量
     */
    @Query("SELECT DATE(ol.createTime), ol.module, COUNT(ol) FROM OperationLog ol GROUP BY DATE(ol.createTime), ol.module ORDER BY DATE(ol.createTime) DESC, COUNT(ol) DESC")
    List<Object[]> countByModuleAndDay();
    
    /**
     * 删除指定时间之前的操作日志
     */
    @Query("DELETE FROM OperationLog ol WHERE ol.createTime < :beforeTime")
    int deleteByCreateTimeBefore(@Param("beforeTime") LocalDateTime beforeTime);
    
    /**
     * 删除管理员的操作日志
     */
    void deleteByAdminId(Long adminId);
    
    /**
     * 根据管理员ID列表删除操作日志
     */
    void deleteByAdminIdIn(List<Long> adminIds);
    
    /**
     * 查询操作日志统计信息
     */
    @Query("SELECT COUNT(ol) as totalCount, COUNT(DISTINCT ol.adminId) as adminCount, COUNT(DISTINCT ol.operation) as operationCount, COUNT(DISTINCT ol.module) as moduleCount FROM OperationLog ol")
    Object[] findStatistics();
}
