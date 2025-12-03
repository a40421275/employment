package com.shera.framework.employment.employment.modules.message.service.impl;

import com.shera.framework.employment.employment.modules.message.entity.Message;
import com.shera.framework.employment.employment.modules.message.service.MessageSendStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 站内消息发送策略
 * 用于处理站内消息通知
 */
@Component
@Slf4j
public class InternalMessageSendStrategy implements MessageSendStrategy {

    @Override
    public boolean supports(String channelType) {
        return "internal".equalsIgnoreCase(channelType);
    }

    @Override
    public boolean send(Message message) {
        try {
            log.info("开始发送站内消息: 消息ID={}, 接收者ID={}, 标题={}", 
                    message.getId(), message.getReceiverId(), message.getTitle());
            
            // 站内消息不需要实际发送，只需要标记为已创建
            // 这里可以添加站内消息的特定处理逻辑，比如：
            // 1. 更新用户未读消息计数
            // 2. 发送WebSocket通知
            // 3. 记录消息日志
            
            // 模拟处理过程
            Thread.sleep(20);
            
            // 站内消息总是成功（因为只是创建记录）
            log.info("站内消息处理完成: 消息ID={}, 接收者ID={}", 
                    message.getId(), message.getReceiverId());
            
            return true;
            
        } catch (Exception e) {
            log.error("站内消息处理异常: 消息ID={}, 错误={}", 
                    message.getId(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取站内消息的特定处理结果
     */
    public String getInternalMessageResult(Message message) {
        return String.format("站内消息已创建: ID=%d, 接收者=%d, 标题=%s", 
                message.getId(), message.getReceiverId(), message.getTitle());
    }

    /**
     * 检查消息是否需要推送通知
     */
    public boolean needsPushNotification(Message message) {
        // 根据消息优先级判断是否需要推送通知
        return message.getPriority() != null && message.getPriority() >= 2; // 中高优先级需要推送
    }

    /**
     * 获取推送通知内容
     */
    public String getPushNotificationContent(Message message) {
        if (message.getTitle() != null && !message.getTitle().isEmpty()) {
            return message.getTitle();
        }
        if (message.getContent() != null && message.getContent().length() > 50) {
            return message.getContent().substring(0, 50) + "...";
        }
        return message.getContent();
    }
}
