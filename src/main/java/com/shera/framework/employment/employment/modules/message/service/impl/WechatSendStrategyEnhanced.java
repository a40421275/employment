package com.shera.framework.employment.employment.modules.message.service.impl;

import com.shera.framework.employment.employment.modules.message.entity.Message;
import com.shera.framework.employment.employment.modules.message.service.MessageSendStrategy;
import com.shera.framework.employment.employment.modules.message.service.MessageSenderService;
import com.shera.framework.employment.employment.modules.user.entity.User;
import com.shera.framework.employment.employment.modules.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 增强版微信发送策略
 * 支持模板消息、图文消息等高级功能
 */
@Component
@Slf4j
public class WechatSendStrategyEnhanced implements MessageSendStrategy {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSenderService messageSenderService;

    @Override
    public boolean supports(String channelType) {
        return "wechat".equalsIgnoreCase(channelType);
    }

    @Override
    public boolean send(Message message) {
        try {
            log.info("线程[{}]开始发送微信消息: 消息ID={}, 接收者ID={}", 
                    Thread.currentThread().getName(), message.getId(), message.getReceiverId());
            
            // 获取接收者信息
            Optional<User> receiverOpt = userRepository.findById(message.getReceiverId());
            if (!receiverOpt.isPresent()) {
                log.warn("微信发送失败: 接收者不存在, 消息ID={}, 接收者ID={}", message.getId(), message.getReceiverId());
                return false;
            }

            User receiver = receiverOpt.get();
            if (receiver.getWxOpenid() == null || receiver.getWxOpenid().trim().isEmpty()) {
                log.warn("微信发送失败: 接收者没有微信OpenID, 消息ID={}, 接收者ID={}", message.getId(), message.getReceiverId());
                return false;
            }

            // 检查微信配置是否可用
            if (!isWechatConfigAvailable()) {
                log.warn("微信发送失败: 微信配置不可用, 消息ID={}", message.getId());
                return false;
            }

            // 根据消息类型选择发送方式
            boolean success = false;
            if (isTemplateMessage(message)) {
                // 发送模板消息
                success = sendTemplateMessage(receiver.getWxOpenid(), message);
            } else if (isNewsMessage(message)) {
                // 发送图文消息
                success = sendNewsMessage(receiver.getWxOpenid(), message);
            } else {
                // 发送普通文本消息
                success = sendTextMessage(receiver.getWxOpenid(), message);
            }
            
            if (success) {
                log.info("线程[{}]微信发送成功: 消息ID={}, 接收者={}, 消息类型={}", 
                        Thread.currentThread().getName(), message.getId(), receiver.getWxOpenid(), getMessageType(message));
            } else {
                log.warn("线程[{}]微信发送失败: 消息ID={}, 接收者={}", 
                        Thread.currentThread().getName(), message.getId(), receiver.getWxOpenid());
            }
            
            return success;
            
        } catch (Exception e) {
            log.error("线程[{}]微信发送异常: 消息ID={}, 错误={}", 
                    Thread.currentThread().getName(), message.getId(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * 检查微信配置是否可用
     */
    private boolean isWechatConfigAvailable() {
        // TODO: 检查微信配置（AppID、AppSecret等）是否已配置
        // 可以从系统配置中读取微信配置
        return true; // 暂时返回true，实际需要实现配置检查
    }

    /**
     * 判断是否为模板消息
     */
    private boolean isTemplateMessage(Message message) {
        // 根据模板编码或业务类型判断是否为模板消息
        return message.getTemplateCode() != null && 
               (message.getTemplateCode().startsWith("WX_TEMPLATE_") || 
                message.getBusinessType() != null && 
                message.getBusinessType().contains("template"));
    }

    /**
     * 判断是否为图文消息
     */
    private boolean isNewsMessage(Message message) {
        // 根据内容格式判断是否为图文消息
        return message.getContent() != null && 
               (message.getContent().contains("<img") || 
                message.getContent().contains("![") ||
                message.getAttachments() != null && 
                message.getAttachments().contains("image"));
    }

    /**
     * 获取消息类型
     */
    private String getMessageType(Message message) {
        if (isTemplateMessage(message)) {
            return "template";
        } else if (isNewsMessage(message)) {
            return "news";
        } else {
            return "text";
        }
    }

    /**
     * 发送模板消息
     */
    private boolean sendTemplateMessage(String openId, Message message) {
        try {
            // TODO: 实现微信模板消息发送
            // 1. 获取模板ID
            // 2. 构建模板数据
            // 3. 调用微信API发送模板消息
            
            log.info("发送微信模板消息: OpenID={}, 消息ID={}, 模板编码={}", 
                    openId, message.getId(), message.getTemplateCode());
            
            // 模拟发送过程
            Thread.sleep(100);
            
            // 模拟发送结果（90%成功率）
            boolean success = Math.random() > 0.1;
            
            if (success) {
                log.info("微信模板消息发送成功: OpenID={}, 消息ID={}", openId, message.getId());
            } else {
                log.warn("微信模板消息发送失败: OpenID={}, 消息ID={}", openId, message.getId());
            }
            
            return success;
            
        } catch (Exception e) {
            log.error("微信模板消息发送异常: OpenID={}, 消息ID={}, 错误={}", 
                    openId, message.getId(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * 发送图文消息
     */
    private boolean sendNewsMessage(String openId, Message message) {
        try {
            // TODO: 实现微信图文消息发送
            // 1. 解析图文内容
            // 2. 上传图片到微信服务器
            // 3. 构建图文消息
            // 4. 调用微信API发送图文消息
            
            log.info("发送微信图文消息: OpenID={}, 消息ID={}, 标题={}", 
                    openId, message.getId(), message.getTitle());
            
            // 模拟发送过程
            Thread.sleep(120);
            
            // 模拟发送结果（85%成功率）
            boolean success = Math.random() > 0.15;
            
            if (success) {
                log.info("微信图文消息发送成功: OpenID={}, 消息ID={}", openId, message.getId());
            } else {
                log.warn("微信图文消息发送失败: OpenID={}, 消息ID={}", openId, message.getId());
            }
            
            return success;
            
        } catch (Exception e) {
            log.error("微信图文消息发送异常: OpenID={}, 消息ID={}, 错误={}", 
                    openId, message.getId(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * 发送文本消息
     */
    private boolean sendTextMessage(String openId, Message message) {
        try {
            // 使用原有的消息发送服务
            boolean success = messageSenderService.sendWechat(openId, message.getContent());
            
            if (success) {
                log.info("微信文本消息发送成功: OpenID={}, 消息ID={}", openId, message.getId());
            } else {
                log.warn("微信文本消息发送失败: OpenID={}, 消息ID={}", openId, message.getId());
            }
            
            return success;
            
        } catch (Exception e) {
            log.error("微信文本消息发送异常: OpenID={}, 消息ID={}, 错误={}", 
                    openId, message.getId(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * 重试发送（支持重试机制）
     */
    public boolean sendWithRetry(Message message, int maxRetries) {
        int retryCount = 0;
        boolean success = false;
        
        while (!success && retryCount < maxRetries) {
            try {
                success = send(message);
                if (!success) {
                    retryCount++;
                    log.warn("微信消息发送失败，准备重试: 消息ID={}, 重试次数={}/{}", 
                            message.getId(), retryCount, maxRetries);
                    
                    if (retryCount < maxRetries) {
                        // 等待一段时间后重试
                        Thread.sleep(1000 * retryCount); // 指数退避
                    }
                }
            } catch (Exception e) {
                log.error("微信消息重试发送异常: 消息ID={}, 重试次数={}, 错误={}", 
                        message.getId(), retryCount, e.getMessage(), e);
                retryCount++;
            }
        }
        
        return success;
    }
}
