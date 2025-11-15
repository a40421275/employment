package com.shera.framework.employment.employment.service.impl;

import com.shera.framework.employment.employment.dto.NotificationDTO;
import com.shera.framework.employment.employment.entity.Notification;
import com.shera.framework.employment.employment.repository.NotificationRepository;
import com.shera.framework.employment.employment.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通知服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    
    private final NotificationRepository notificationRepository;
    
    @Override
    @Transactional
    public Notification createNotification(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        copyNotificationProperties(notification, notificationDTO);
        notification.setSendTime(LocalDateTime.now());
        return notificationRepository.save(notification);
    }
    
    @Override
    @Transactional
    public List<Notification> createNotifications(List<NotificationDTO> notificationDTOs) {
        List<Notification> notifications = new ArrayList<>();
        for (NotificationDTO dto : notificationDTOs) {
            Notification notification = new Notification();
            copyNotificationProperties(notification, dto);
            notification.setSendTime(LocalDateTime.now());
            notifications.add(notification);
        }
        return notificationRepository.saveAll(notifications);
    }
    
    @Override
    @Transactional
    public Notification updateNotification(Long id, NotificationDTO notificationDTO) {
        Notification notification = getNotificationById(id);
        copyNotificationProperties(notification, notificationDTO);
        return notificationRepository.save(notification);
    }
    
    @Override
    @Transactional
    public void deleteNotification(Long id) {
        Notification notification = getNotificationById(id);
        notificationRepository.delete(notification);
    }
    
    @Override
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("通知不存在: " + id));
    }
    
    @Override
    public Page<Notification> getNotifications(Pageable pageable) {
        return notificationRepository.findAll(pageable);
    }
    
    @Override
    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId);
    }
    
    @Override
    public Page<Notification> getNotificationsByUserId(Long userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable);
    }
    
    @Override
    public List<Notification> getNotificationsByType(Integer type) {
        return notificationRepository.findByType(type);
    }
    
    @Override
    public List<Notification> getNotificationsByStatus(Integer status) {
        // 由于实体使用isRead而不是status，这里需要转换
        Boolean isRead = status == 2; // 2表示已读
        return notificationRepository.findByIsRead(isRead);
    }
    
    @Override
    public List<Notification> getNotificationsByUserIdAndStatus(Long userId, Integer status) {
        // 由于实体使用isRead而不是status，这里需要转换
        Boolean isRead = status == 2; // 2表示已读
        return notificationRepository.findByUserIdAndIsRead(userId, isRead);
    }
    
    @Override
    public List<Notification> getNotificationsByUserIdAndType(Long userId, Integer type) {
        return notificationRepository.findByUserIdAndType(userId, type);
    }
    
    @Override
    @Transactional
    public Notification markAsRead(Long id) {
        Notification notification = getNotificationById(id);
        notification.setIsRead(true); // 已读
        notification.setReadTime(LocalDateTime.now());
        return notificationRepository.save(notification);
    }
    
    @Override
    @Transactional
    public List<Notification> markMultipleAsRead(List<Long> ids) {
        List<Notification> notifications = new ArrayList<>();
        for (Long id : ids) {
            try {
                Notification notification = markAsRead(id);
                notifications.add(notification);
            } catch (Exception e) {
                log.warn("标记通知为已读失败: {}", id, e);
            }
        }
        return notifications;
    }
    
    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndIsRead(userId, false);
        for (Notification notification : unreadNotifications) {
            notification.setIsRead(true);
            notification.setReadTime(LocalDateTime.now());
        }
        notificationRepository.saveAll(unreadNotifications);
    }
    
    @Override
    public Long getUnreadCountByUserId(Long userId) {
        return notificationRepository.countByUserIdAndIsRead(userId, false); // false表示未读
    }
    
    @Override
    public List<Notification> getLatestNotificationsByUserId(Long userId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return notificationRepository.findTopByUserIdOrderByCreateTimeDesc(userId, pageable);
    }
    
    @Override
    @Transactional
    public Notification sendApplyStatusNotification(Long userId, Long applyId, String status) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserId(userId);
        notificationDTO.setType(2); // 申请结果通知
        notificationDTO.setTitle("申请状态更新");
        notificationDTO.setContent("您的岗位申请状态已更新为: " + status);
        
        return createNotification(notificationDTO);
    }
    
    @Override
    @Transactional
    public Notification sendInterviewNotification(Long userId, Long applyId, String interviewTime, String location) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserId(userId);
        notificationDTO.setType(4); // 面试通知
        notificationDTO.setTitle("面试邀请");
        notificationDTO.setContent("您收到面试邀请，时间: " + interviewTime + "，地点: " + location);
        
        return createNotification(notificationDTO);
    }
    
    @Override
    @Transactional
    public Notification sendSystemNotification(Long userId, String title, String content) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserId(userId);
        notificationDTO.setType(1); // 系统通知
        notificationDTO.setTitle(title);
        notificationDTO.setContent(content);
        
        return createNotification(notificationDTO);
    }
    
    @Override
    @Transactional
    public void deleteNotificationsByUserId(Long userId) {
        notificationRepository.deleteByUserId(userId);
    }
    
    @Override
    public Object getNotificationStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        // 总通知数
        Long totalCount = (long) notificationRepository.findByUserId(userId).size();
        stats.put("totalCount", totalCount);
        
        // 未读通知数
        Long unreadCount = getUnreadCountByUserId(userId);
        stats.put("unreadCount", unreadCount);
        
        // 各类型通知数量
        for (int type = 1; type <= 4; type++) {
            Long count = (long) notificationRepository.findByUserIdAndType(userId, type).size();
            stats.put("type_" + type + "_count", count);
        }
        
        // 最新通知
        List<Notification> latestNotifications = getLatestNotificationsByUserId(userId, 5);
        stats.put("latestNotifications", latestNotifications);
        
        return stats;
    }
    
    /**
     * 复制NotificationDTO属性到Notification实体
     */
    private void copyNotificationProperties(Notification notification, NotificationDTO notificationDTO) {
        notification.setUserId(notificationDTO.getUserId());
        notification.setTitle(notificationDTO.getTitle());
        notification.setContent(notificationDTO.getContent());
        notification.setType(notificationDTO.getType());
        
        // 设置是否已读状态
        if (notificationDTO.getStatus() != null) {
            notification.setIsRead(notificationDTO.getStatus() == 2); // 2表示已读
        }
        
        if (notificationDTO.getReadTime() != null) {
            notification.setReadTime(notificationDTO.getReadTime());
        }
    }
}
