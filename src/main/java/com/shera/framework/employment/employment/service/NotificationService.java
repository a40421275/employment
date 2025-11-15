package com.shera.framework.employment.employment.service;

import com.shera.framework.employment.employment.dto.NotificationDTO;
import com.shera.framework.employment.employment.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 通知服务接口
 */
public interface NotificationService {
    
    /**
     * 创建通知
     */
    Notification createNotification(NotificationDTO notificationDTO);
    
    /**
     * 批量创建通知
     */
    List<Notification> createNotifications(List<NotificationDTO> notificationDTOs);
    
    /**
     * 更新通知
     */
    Notification updateNotification(Long id, NotificationDTO notificationDTO);
    
    /**
     * 删除通知
     */
    void deleteNotification(Long id);
    
    /**
     * 根据ID获取通知
     */
    Notification getNotificationById(Long id);
    
    /**
     * 分页查询通知列表
     */
    Page<Notification> getNotifications(Pageable pageable);
    
    /**
     * 根据用户ID查询通知列表
     */
    List<Notification> getNotificationsByUserId(Long userId);
    
    /**
     * 根据用户ID分页查询通知列表
     */
    Page<Notification> getNotificationsByUserId(Long userId, Pageable pageable);
    
    /**
     * 根据类型查询通知列表
     */
    List<Notification> getNotificationsByType(Integer type);
    
    /**
     * 根据状态查询通知列表
     */
    List<Notification> getNotificationsByStatus(Integer status);
    
    /**
     * 根据用户ID和状态查询通知列表
     */
    List<Notification> getNotificationsByUserIdAndStatus(Long userId, Integer status);
    
    /**
     * 根据用户ID和类型查询通知列表
     */
    List<Notification> getNotificationsByUserIdAndType(Long userId, Integer type);
    
    /**
     * 标记通知为已读
     */
    Notification markAsRead(Long id);
    
    /**
     * 批量标记通知为已读
     */
    List<Notification> markMultipleAsRead(List<Long> ids);
    
    /**
     * 标记用户所有通知为已读
     */
    void markAllAsRead(Long userId);
    
    /**
     * 获取用户未读通知数量
     */
    Long getUnreadCountByUserId(Long userId);
    
    /**
     * 获取用户最新通知
     */
    List<Notification> getLatestNotificationsByUserId(Long userId, int limit);
    
    /**
     * 发送申请状态通知
     */
    Notification sendApplyStatusNotification(Long userId, Long applyId, String status);
    
    /**
     * 发送面试通知
     */
    Notification sendInterviewNotification(Long userId, Long applyId, String interviewTime, String location);
    
    /**
     * 发送系统通知
     */
    Notification sendSystemNotification(Long userId, String title, String content);
    
    /**
     * 删除用户所有通知
     */
    void deleteNotificationsByUserId(Long userId);
    
    /**
     * 获取通知统计信息
     */
    Object getNotificationStats(Long userId);
}
