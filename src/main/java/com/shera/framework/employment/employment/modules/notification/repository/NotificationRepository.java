package com.shera.framework.employment.employment.modules.notification.repository;

import com.shera.framework.employment.employment.modules.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知Repository
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    /**
     * 根据用户ID查找通知列表
     */
    List<Notification> findByUserId(Long userId);
    
    /**
     * 根据用户ID分页查找通知列表
     */
    Page<Notification> findByUserId(Long userId, Pageable pageable);
    
    /**
     * 根据类型查找通知列表
     */
    List<Notification> findByType(Integer type);
    
    /**
     * 根据用户ID和是否已读查找通知列表
     */
    List<Notification> findByUserIdAndIsRead(Long userId, Boolean isRead);
    
    /**
     * 根据用户ID和类型查找通知列表
     */
    List<Notification> findByUserIdAndType(Long userId, Integer type);
    
    /**
     * 根据是否已读查找通知列表
     */
    List<Notification> findByIsRead(Boolean isRead);
    
    /**
     * 根据用户ID和类型分页查找通知列表
     */
    Page<Notification> findByUserIdAndType(Long userId, Integer type, Pageable pageable);
    
    /**
     * 统计用户未读通知数量
     */
    Long countByUserIdAndIsRead(Long userId, Boolean isRead);
    
    /**
     * 根据用户ID删除通知
     */
    void deleteByUserId(Long userId);
    
    /**
     * 标记用户所有通知为已读
     */
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readTime = :readTime WHERE n.userId = :userId AND n.isRead = false")
    int markAllAsReadByUserId(@Param("userId") Long userId, @Param("readTime") LocalDateTime readTime);
    
    /**
     * 标记多个通知为已读
     */
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readTime = :readTime WHERE n.id IN :ids AND n.isRead = false")
    int markMultipleAsRead(@Param("ids") List<Long> ids, @Param("readTime") LocalDateTime readTime);
    
    /**
     * 获取用户最新通知
     */
    @Query("SELECT n FROM Notification n WHERE n.userId = :userId ORDER BY n.createTime DESC")
    List<Notification> findTopByUserIdOrderByCreateTimeDesc(@Param("userId") Long userId, Pageable pageable);
    
    
    /**
     * 根据创建时间范围查找通知
     */
    @Query("SELECT n FROM Notification n WHERE n.createTime BETWEEN :start AND :end")
    List<Notification> findByCreateTimeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    /**
     * 根据用户ID和创建时间范围查找通知
     */
    @Query("SELECT n FROM Notification n WHERE n.userId = :userId AND n.createTime BETWEEN :start AND :end")
    List<Notification> findByUserIdAndCreateTimeBetween(@Param("userId") Long userId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
