package com.shera.framework.employment.employment.modules.notification.controller;

import com.shera.framework.employment.employment.modules.notification.dto.NotificationDTO;
import com.shera.framework.employment.employment.modules.notification.entity.Notification;
import com.shera.framework.employment.employment.modules.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知控制器
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    
    private final NotificationService notificationService;
    
    /**
     * 创建通知
     */
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody NotificationDTO notificationDTO) {
        Notification notification = notificationService.createNotification(notificationDTO);
        return ResponseEntity.ok(notification);
    }
    
    /**
     * 批量创建通知
     */
    @PostMapping("/batch")
    public ResponseEntity<List<Notification>> createNotifications(@RequestBody List<NotificationDTO> notificationDTOs) {
        List<Notification> notifications = notificationService.createNotifications(notificationDTOs);
        return ResponseEntity.ok(notifications);
    }
    
    /**
     * 更新通知
     */
    @PutMapping("/{id}")
    public ResponseEntity<Notification> updateNotification(@PathVariable Long id, @RequestBody NotificationDTO notificationDTO) {
        Notification notification = notificationService.updateNotification(id, notificationDTO);
        return ResponseEntity.ok(notification);
    }
    
    /**
     * 删除通知
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 获取通知详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotification(@PathVariable Long id) {
        Notification notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }
    
    /**
     * 分页查询通知列表
     */
    @GetMapping
    public ResponseEntity<Page<Notification>> getNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        Page<Notification> notifications = notificationService.getNotifications(pageable);
        return ResponseEntity.ok(notifications);
    }
    
    /**
     * 根据用户ID查询通知列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsByUser(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }
    
    /**
     * 根据用户ID分页查询通知列表
     */
    @GetMapping("/user/{userId}/page")
    public ResponseEntity<Page<Notification>> getNotificationsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationService.getNotificationsByUserId(userId, pageable);
        return ResponseEntity.ok(notifications);
    }
    
    /**
     * 根据类型查询通知列表
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Notification>> getNotificationsByType(@PathVariable Integer type) {
        List<Notification> notifications = notificationService.getNotificationsByType(type);
        return ResponseEntity.ok(notifications);
    }
    
    /**
     * 根据状态查询通知列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Notification>> getNotificationsByStatus(@PathVariable Integer status) {
        List<Notification> notifications = notificationService.getNotificationsByStatus(status);
        return ResponseEntity.ok(notifications);
    }
    
    /**
     * 根据用户ID和状态查询通知列表
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<Notification>> getNotificationsByUserAndStatus(
            @PathVariable Long userId,
            @PathVariable Integer status) {
        List<Notification> notifications = notificationService.getNotificationsByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(notifications);
    }
    
    /**
     * 根据用户ID和类型查询通知列表
     */
    @GetMapping("/user/{userId}/type/{type}")
    public ResponseEntity<List<Notification>> getNotificationsByUserAndType(
            @PathVariable Long userId,
            @PathVariable Integer type) {
        List<Notification> notifications = notificationService.getNotificationsByUserIdAndType(userId, type);
        return ResponseEntity.ok(notifications);
    }
    
    /**
     * 标记通知为已读
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id) {
        Notification notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(notification);
    }
    
    /**
     * 批量标记通知为已读
     */
    @PutMapping("/batch-read")
    public ResponseEntity<List<Notification>> markMultipleAsRead(@RequestBody List<Long> ids) {
        List<Notification> notifications = notificationService.markMultipleAsRead(ids);
        return ResponseEntity.ok(notifications);
    }
    
    /**
     * 标记用户所有通知为已读
     */
    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 获取用户未读通知数量
     */
    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long userId) {
        Long count = notificationService.getUnreadCountByUserId(userId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * 获取用户最新通知
     */
    @GetMapping("/user/{userId}/latest")
    public ResponseEntity<List<Notification>> getLatestNotifications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        List<Notification> notifications = notificationService.getLatestNotificationsByUserId(userId, limit);
        return ResponseEntity.ok(notifications);
    }
    
    /**
     * 发送申请状态通知
     */
    @PostMapping("/apply-status")
    public ResponseEntity<Notification> sendApplyStatusNotification(
            @RequestParam Long userId,
            @RequestParam Long applyId,
            @RequestParam String status) {
        Notification notification = notificationService.sendApplyStatusNotification(userId, applyId, status);
        return ResponseEntity.ok(notification);
    }
    
    /**
     * 发送面试通知
     */
    @PostMapping("/interview")
    public ResponseEntity<Notification> sendInterviewNotification(
            @RequestParam Long userId,
            @RequestParam Long applyId,
            @RequestParam String interviewTime,
            @RequestParam String location) {
        Notification notification = notificationService.sendInterviewNotification(userId, applyId, interviewTime, location);
        return ResponseEntity.ok(notification);
    }
    
    /**
     * 发送系统通知
     */
    @PostMapping("/system")
    public ResponseEntity<Notification> sendSystemNotification(
            @RequestParam Long userId,
            @RequestParam String title,
            @RequestParam String content) {
        Notification notification = notificationService.sendSystemNotification(userId, title, content);
        return ResponseEntity.ok(notification);
    }
    
    /**
     * 删除用户所有通知
     */
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteNotificationsByUser(@PathVariable Long userId) {
        notificationService.deleteNotificationsByUserId(userId);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 获取通知统计信息
     */
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<Object> getNotificationStats(@PathVariable Long userId) {
        Object stats = notificationService.getNotificationStats(userId);
        return ResponseEntity.ok(stats);
    }
}
