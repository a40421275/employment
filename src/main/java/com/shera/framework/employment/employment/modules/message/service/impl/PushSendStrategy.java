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
 * 推送发送策略
 */
@Component
@Slf4j
public class PushSendStrategy implements MessageSendStrategy {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSenderService messageSenderService;

    @Override
    public boolean supports(String channelType) {
        return "push".equalsIgnoreCase(channelType);
    }

    @Override
    public boolean send(Message message) {
        try {
            log.info("线程[{}]开始发送推送通知: 消息ID={}, 接收者ID={}", 
                    Thread.currentThread().getName(), message.getId(), message.getReceiverId());
            
            // 获取接收者信息
            Optional<User> receiverOpt = userRepository.findById(message.getReceiverId());
            if (!receiverOpt.isPresent()) {
                log.warn("推送发送失败: 接收者不存在, 消息ID={}, 接收者ID={}", message.getId(), message.getReceiverId());
                return false;
            }

            User receiver = receiverOpt.get();
            
            // 获取设备Token（这里需要根据实际业务实现）
            String deviceToken = getDeviceToken(receiver.getId());
            if (deviceToken == null || deviceToken.trim().isEmpty()) {
                log.warn("推送发送失败: 接收者没有设备Token, 消息ID={}, 接收者ID={}", message.getId(), message.getReceiverId());
                return false;
            }

            // 发送推送通知
            boolean success = messageSenderService.sendPush(deviceToken, message.getTitle(), message.getContent());
            
            if (success) {
                log.info("线程[{}]推送发送成功: 消息ID={}, 接收者={}", 
                        Thread.currentThread().getName(), message.getId(), receiver.getId());
            } else {
                log.warn("线程[{}]推送发送失败: 消息ID={}, 接收者={}", 
                        Thread.currentThread().getName(), message.getId(), receiver.getId());
            }
            
            return success;
            
        } catch (Exception e) {
            log.error("线程[{}]推送发送异常: 消息ID={}, 错误={}", 
                    Thread.currentThread().getName(), message.getId(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取用户的设备Token（这里需要根据实际业务实现）
     */
    private String getDeviceToken(Long userId) {
        // TODO: 从用户设备信息表中获取设备Token
        // 这里返回一个模拟的设备Token
        return "device_token_" + userId;
    }
}
