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
 * 短信发送策略
 */
@Component
@Slf4j
public class SmsSendStrategy implements MessageSendStrategy {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSenderService messageSenderService;

    @Override
    public boolean supports(String channelType) {
        return "sms".equalsIgnoreCase(channelType);
    }

    @Override
    public boolean send(Message message) {
        try {
            log.info("线程[{}]开始发送短信: 消息ID={}, 接收者ID={}", 
                    Thread.currentThread().getName(), message.getId(), message.getReceiverId());
            
            // 获取接收者信息
            Optional<User> receiverOpt = userRepository.findById(message.getReceiverId());
            if (!receiverOpt.isPresent()) {
                log.warn("短信发送失败: 接收者不存在, 消息ID={}, 接收者ID={}", message.getId(), message.getReceiverId());
                return false;
            }

            User receiver = receiverOpt.get();
            if (receiver.getPhone() == null || receiver.getPhone().trim().isEmpty()) {
                log.warn("短信发送失败: 接收者没有手机号, 消息ID={}, 接收者ID={}", message.getId(), message.getReceiverId());
                return false;
            }

            // 发送短信
            boolean success = messageSenderService.sendSms(receiver.getPhone(), message.getContent());
            
            if (success) {
                log.info("线程[{}]短信发送成功: 消息ID={}, 接收者={}", 
                        Thread.currentThread().getName(), message.getId(), receiver.getPhone());
            } else {
                log.warn("线程[{}]短信发送失败: 消息ID={}, 接收者={}", 
                        Thread.currentThread().getName(), message.getId(), receiver.getPhone());
            }
            
            return success;
            
        } catch (Exception e) {
            log.error("线程[{}]短信发送异常: 消息ID={}, 错误={}", 
                    Thread.currentThread().getName(), message.getId(), e.getMessage(), e);
            return false;
        }
    }
}