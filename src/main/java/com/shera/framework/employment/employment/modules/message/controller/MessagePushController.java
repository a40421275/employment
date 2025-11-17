package com.shera.framework.employment.employment.modules.message.controller;

import com.shera.framework.employment.employment.modules.message.service.MessagePushService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 消息推送控制器
 */
@RestController
@RequestMapping("/api/message-push")
@RequiredArgsConstructor
public class MessagePushController {
    
    private final MessagePushService messagePushService;
    
    /**
     * 发送站内消息
     */
    @PostMapping("/internal")
    public ResponseEntity<Map<String, Object>> sendInternalMessage(
            @RequestParam Long userId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam Integer type) {
        try {
            boolean result = messagePushService.sendInternalMessage(userId, title, content, type);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "站内消息发送成功" : "站内消息发送失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "站内消息发送异常: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 批量发送站内消息
     */
    @PostMapping("/internal/batch")
    public ResponseEntity<Map<String, Object>> sendInternalMessages(
            @RequestParam List<Long> userIds,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam Integer type) {
        try {
            boolean result = messagePushService.sendInternalMessages(userIds, title, content, type);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "批量站内消息发送成功" : "批量站内消息发送失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "批量站内消息发送异常: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 发送邮件通知
     */
    @PostMapping("/email")
    public ResponseEntity<Map<String, Object>> sendEmail(
            @RequestParam String toEmail,
            @RequestParam String subject,
            @RequestParam String content) {
        try {
            boolean result = messagePushService.sendEmail(toEmail, subject, content);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "邮件发送成功" : "邮件发送失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "邮件发送异常: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 发送短信通知
     */
    @PostMapping("/sms")
    public ResponseEntity<Map<String, Object>> sendSMS(
            @RequestParam String phone,
            @RequestParam String content) {
        try {
            boolean result = messagePushService.sendSMS(phone, content);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "短信发送成功" : "短信发送失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "短信发送异常: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 发送申请状态通知
     */
    @PostMapping("/apply-status")
    public ResponseEntity<Map<String, Object>> sendApplyStatusNotification(
            @RequestParam Long userId,
            @RequestParam Long applyId,
            @RequestParam String status,
            @RequestParam String jobTitle) {
        try {
            boolean result = messagePushService.sendApplyStatusNotification(userId, applyId, status, jobTitle);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "申请状态通知发送成功" : "申请状态通知发送失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "申请状态通知发送异常: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 发送面试通知
     */
    @PostMapping("/interview")
    public ResponseEntity<Map<String, Object>> sendInterviewNotification(
            @RequestParam Long userId,
            @RequestParam Long applyId,
            @RequestParam String interviewTime,
            @RequestParam String location,
            @RequestParam String jobTitle) {
        try {
            boolean result = messagePushService.sendInterviewNotification(userId, applyId, interviewTime, location, jobTitle);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "面试通知发送成功" : "面试通知发送失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "面试通知发送异常: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 发送录用通知
     */
    @PostMapping("/offer")
    public ResponseEntity<Map<String, Object>> sendOfferNotification(
            @RequestParam Long userId,
            @RequestParam Long applyId,
            @RequestParam String jobTitle,
            @RequestParam String companyName) {
        try {
            boolean result = messagePushService.sendOfferNotification(userId, applyId, jobTitle, companyName);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "录用通知发送成功" : "录用通知发送失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "录用通知发送异常: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 发送系统维护通知
     */
    @PostMapping("/system-maintenance")
    public ResponseEntity<Map<String, Object>> sendSystemMaintenanceNotification(
            @RequestParam String title,
            @RequestParam String content) {
        try {
            boolean result = messagePushService.sendSystemMaintenanceNotification(title, content);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "系统维护通知发送成功" : "系统维护通知发送失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "系统维护通知发送异常: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 发送岗位推荐通知
     */
    @PostMapping("/job-recommendation")
    public ResponseEntity<Map<String, Object>> sendJobRecommendationNotification(
            @RequestParam Long userId,
            @RequestParam List<Long> jobIds) {
        try {
            boolean result = messagePushService.sendJobRecommendationNotification(userId, jobIds);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "岗位推荐通知发送成功" : "岗位推荐通知发送失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "岗位推荐通知发送异常: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 发送简历被查看通知
     */
    @PostMapping("/resume-viewed")
    public ResponseEntity<Map<String, Object>> sendResumeViewedNotification(
            @RequestParam Long userId,
            @RequestParam Long resumeId,
            @RequestParam String companyName) {
        try {
            boolean result = messagePushService.sendResumeViewedNotification(userId, resumeId, companyName);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "简历被查看通知发送成功" : "简历被查看通知发送失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "简历被查看通知发送异常: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 发送实名认证结果通知
     */
    @PostMapping("/realname-auth-result")
    public ResponseEntity<Map<String, Object>> sendRealnameAuthResultNotification(
            @RequestParam Long userId,
            @RequestParam boolean success,
            @RequestParam(required = false) String reason) {
        try {
            boolean result = messagePushService.sendRealnameAuthResultNotification(userId, success, reason);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "实名认证结果通知发送成功" : "实名认证结果通知发送失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "实名认证结果通知发送异常: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 发送密码重置通知
     */
    @PostMapping("/password-reset")
    public ResponseEntity<Map<String, Object>> sendPasswordResetNotification(
            @RequestParam Long userId,
            @RequestParam String newPassword) {
        try {
            boolean result = messagePushService.sendPasswordResetNotification(userId, newPassword);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "密码重置通知发送成功" : "密码重置通知发送失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "密码重置通知发送异常: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 发送账户安全通知
     */
    @PostMapping("/account-security")
    public ResponseEntity<Map<String, Object>> sendAccountSecurityNotification(
            @RequestParam Long userId,
            @RequestParam String eventType,
            @RequestParam String description) {
        try {
            boolean result = messagePushService.sendAccountSecurityNotification(userId, eventType, description);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "账户安全通知发送成功" : "账户安全通知发送失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "账户安全通知发送异常: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 发送活动通知
     */
    @PostMapping("/activity")
    public ResponseEntity<Map<String, Object>> sendActivityNotification(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam List<Long> userIds) {
        try {
            boolean result = messagePushService.sendActivityNotification(title, content, userIds);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "活动通知发送成功" : "活动通知发送失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "活动通知发送异常: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 发送紧急通知
     */
    @PostMapping("/urgent")
    public ResponseEntity<Map<String, Object>> sendUrgentNotification(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam List<Long> userIds) {
        try {
            boolean result = messagePushService.sendUrgentNotification(title, content, userIds);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "紧急通知发送成功" : "紧急通知发送失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "紧急通知发送异常: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 获取消息发送统计
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getMessageStatistics() {
        try {
            Map<String, Object> statistics = messagePushService.getMessageStatistics();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", statistics
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "获取消息统计异常: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 获取用户消息偏好设置
     */
    @GetMapping("/preferences/{userId}")
    public ResponseEntity<Map<String, Object>> getUserMessagePreferences(@PathVariable Long userId) {
        try {
            Map<String, Boolean> preferences = messagePushService.getUserMessagePreferences(userId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", preferences
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "获取用户消息偏好设置异常: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 更新用户消息偏好设置
     */
    @PutMapping("/preferences/{userId}")
    public ResponseEntity<Map<String, Object>> updateUserMessagePreferences(
            @PathVariable Long userId,
            @RequestBody Map<String, Boolean> preferences) {
        try {
            boolean result = messagePushService.updateUserMessagePreferences(userId, preferences);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "用户消息偏好设置更新成功" : "用户消息偏好设置更新失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "更新用户消息偏好设置异常: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 检查消息发送状态
     */
    @GetMapping("/status/{messageId}")
    public ResponseEntity<Map<String, Object>> checkMessageStatus(@PathVariable String messageId) {
        try {
            Map<String, Object> status = messagePushService.checkMessageStatus(messageId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", status
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "检查消息状态异常: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 重试发送失败的消息
     */
    @PostMapping("/retry/{messageId}")
    public ResponseEntity<Map<String, Object>> retryFailedMessage(@PathVariable String messageId) {
        try {
            boolean result = messagePushService.retryFailedMessage(messageId);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "失败消息重试成功" : "失败消息重试失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "重试失败消息异常: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 清理过期消息
     */
    @DeleteMapping("/cleanup")
    public ResponseEntity<Map<String, Object>> cleanupExpiredMessages(@RequestParam int days) {
        try {
            boolean result = messagePushService.cleanupExpiredMessages(days);
            return ResponseEntity.ok(Map.of(
                "success", result,
                "message", result ? "过期消息清理成功" : "过期消息清理失败"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "清理过期消息异常: " + e.getMessage()
            ));
        }
    }
}
