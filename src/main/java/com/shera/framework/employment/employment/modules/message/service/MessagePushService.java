package com.shera.framework.employment.employment.modules.message.service;

import java.util.List;
import java.util.Map;

/**
 * 消息推送服务接口
 */
public interface MessagePushService {
    
    /**
     * 发送站内消息
     */
    boolean sendInternalMessage(Long userId, String title, String content, Integer type);
    
    /**
     * 批量发送站内消息
     */
    boolean sendInternalMessages(List<Long> userIds, String title, String content, Integer type);
    
    /**
     * 发送邮件通知
     */
    boolean sendEmail(String toEmail, String subject, String content);
    
    /**
     * 批量发送邮件通知
     */
    boolean sendEmails(List<String> toEmails, String subject, String content);
    
    /**
     * 发送短信通知
     */
    boolean sendSMS(String phone, String content);
    
    /**
     * 批量发送短信通知
     */
    boolean sendSMSBatch(List<String> phones, String content);
    
    /**
     * 发送微信模板消息
     */
    boolean sendWechatTemplate(String openid, String templateId, Map<String, Object> data);
    
    /**
     * 发送申请状态通知
     */
    boolean sendApplyStatusNotification(Long userId, Long applyId, String status, String jobTitle);
    
    /**
     * 发送面试通知
     */
    boolean sendInterviewNotification(Long userId, Long applyId, String interviewTime, String location, String jobTitle);
    
    /**
     * 发送录用通知
     */
    boolean sendOfferNotification(Long userId, Long applyId, String jobTitle, String companyName);
    
    /**
     * 发送系统维护通知
     */
    boolean sendSystemMaintenanceNotification(String title, String content);
    
    /**
     * 发送新岗位推荐通知
     */
    boolean sendJobRecommendationNotification(Long userId, List<Long> jobIds);
    
    /**
     * 发送简历被查看通知
     */
    boolean sendResumeViewedNotification(Long userId, Long resumeId, String companyName);
    
    /**
     * 发送实名认证结果通知
     */
    boolean sendRealnameAuthResultNotification(Long userId, boolean success, String reason);
    
    /**
     * 发送密码重置通知
     */
    boolean sendPasswordResetNotification(Long userId, String newPassword);
    
    /**
     * 发送账户安全通知
     */
    boolean sendAccountSecurityNotification(Long userId, String eventType, String description);
    
    /**
     * 发送活动通知
     */
    boolean sendActivityNotification(String title, String content, List<Long> userIds);
    
    /**
     * 发送紧急通知
     */
    boolean sendUrgentNotification(String title, String content, List<Long> userIds);
    
    /**
     * 获取消息发送统计
     */
    Map<String, Object> getMessageStatistics();
    
    /**
     * 获取用户消息偏好设置
     */
    Map<String, Boolean> getUserMessagePreferences(Long userId);
    
    /**
     * 更新用户消息偏好设置
     */
    boolean updateUserMessagePreferences(Long userId, Map<String, Boolean> preferences);
    
    /**
     * 检查消息发送状态
     */
    Map<String, Object> checkMessageStatus(String messageId);
    
    /**
     * 重试发送失败的消息
     */
    boolean retryFailedMessage(String messageId);
    
    /**
     * 清理过期消息
     */
    boolean cleanupExpiredMessages(int days);
}
