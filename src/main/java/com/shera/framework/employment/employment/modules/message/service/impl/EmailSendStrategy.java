package com.shera.framework.employment.employment.modules.message.service.impl;

import com.shera.framework.employment.employment.modules.message.dto.EmailSendRequest;
import com.shera.framework.employment.employment.modules.message.dto.EmailSendResult;
import com.shera.framework.employment.employment.modules.message.entity.Message;
import com.shera.framework.employment.employment.modules.message.service.MessageSendStrategy;
import com.shera.framework.employment.employment.modules.message.service.MessageSenderService;
import com.shera.framework.employment.employment.modules.user.entity.User;
import com.shera.framework.employment.employment.modules.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

/**
 * 邮件发送策略
 */
@Component
@Slf4j
public class EmailSendStrategy implements MessageSendStrategy {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSenderService messageSenderService;

    @Override
    public boolean supports(String channelType) {
        return "email".equalsIgnoreCase(channelType);
    }

    @Override
    public boolean send(Message message) {
        try {
            log.info("线程[{}]开始发送邮件: 消息ID={}, 接收者ID={}", 
                    Thread.currentThread().getName(), message.getId(), message.getReceiverId());
            
            // 获取接收者信息
            Optional<User> receiverOpt = userRepository.findById(message.getReceiverId());
            if (!receiverOpt.isPresent()) {
                log.warn("邮件发送失败: 接收者不存在, 消息ID={}, 接收者ID={}", message.getId(), message.getReceiverId());
                return false;
            }

            User receiver = receiverOpt.get();
            if (receiver.getEmail() == null || receiver.getEmail().trim().isEmpty()) {
                log.warn("邮件发送失败: 接收者没有邮箱地址, 消息ID={}, 接收者ID={}", message.getId(), message.getReceiverId());
                return false;
            }

            // 构建邮件发送请求
            EmailSendRequest request = new EmailSendRequest();
            request.setToEmails(Arrays.asList(receiver.getEmail()));
            request.setSubject(message.getTitle());
            request.setContent(message.getContent());
            request.setSenderId(message.getSenderId());
            request.setBusinessId(message.getBusinessId());
            request.setBusinessType(message.getBusinessType());

            // 发送邮件
            EmailSendResult result = messageSenderService.sendEmail(request);
            
            if (result.isSuccess()) {
                log.info("线程[{}]邮件发送成功: 消息ID={}, 接收者={}, 任务ID={}", 
                        Thread.currentThread().getName(), message.getId(), receiver.getEmail(), result.getTaskId());
            } else {
                log.warn("线程[{}]邮件发送失败: 消息ID={}, 接收者={}, 错误={}", 
                        Thread.currentThread().getName(), message.getId(), receiver.getEmail(), result.getErrorMessage());
            }
            
            return result.isSuccess();
            
        } catch (Exception e) {
            log.error("线程[{}]邮件发送异常: 消息ID={}, 错误={}", 
                    Thread.currentThread().getName(), message.getId(), e.getMessage(), e);
            return false;
        }
    }
}
