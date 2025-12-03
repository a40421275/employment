package com.shera.framework.employment.employment.modules.message.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shera.framework.employment.employment.modules.message.entity.Message;
import com.shera.framework.employment.employment.modules.message.entity.MessageTemplate;
import com.shera.framework.employment.employment.modules.message.repository.MessageRepository;
import com.shera.framework.employment.employment.modules.message.repository.MessageTemplateRepository;
import com.shera.framework.employment.employment.modules.message.service.MessageSendStrategy;
import com.shera.framework.employment.employment.modules.message.service.MessageSendStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * 异步消息发送服务
 */
@Service
@Slf4j
public class AsyncMessageSendService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageSendStrategyFactory strategyFactory;

    @Autowired
    private MessageTemplateRepository messageTemplateRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 异步发送消息
     */
    @Async("taskExecutor")
    public void asyncSendMessage(Message message) {
        log.info("开始异步发送消息: 消息ID={}, 渠道类型={}", message.getId(), message.getChannelType());

        try {
            // 解析多选渠道类型
            List<String> channelTypes = parseChannelTypes(message.getChannelType());
            if (channelTypes.isEmpty()) {
                log.warn("消息没有指定发送渠道: 消息ID={}", message.getId());
                updateMessageSendResult(message, false, "未指定发送渠道");
                return;
            }

            // 自动匹配模板
            MessageTemplate template = findSuitableTemplate(message);
            if (template != null) {
                log.info("找到匹配模板: 消息ID={}, 模板编码={}, 模板名称={}", 
                         message.getId(), template.getCode(), template.getName());
                // 应用模板内容
                applyTemplateToMessage(message, template);
            } else {
                log.warn("未找到匹配模板: 消息ID={}, 使用原始内容发送", message.getId());
            }

            // 遍历所有渠道类型进行发送
            Map<String, Boolean> sendResults = new HashMap<>();
            for (String channelType : channelTypes) {
                try {
                    // 获取对应的发送策略
                    MessageSendStrategy strategy = strategyFactory.getStrategy(channelType);
                    if (strategy == null) {
                        log.warn("未找到对应的发送策略: 消息ID={}, 渠道类型={}", message.getId(), channelType);
                        sendResults.put(channelType, false);
                        continue;
                    }

                    // 执行发送
                    boolean sendSuccess = strategy.send(message);
                    sendResults.put(channelType, sendSuccess);
                    
                    log.info("消息渠道发送完成: 消息ID={}, 渠道类型={}, 结果={}", 
                            message.getId(), channelType, sendSuccess ? "成功" : "失败");
                    
                } catch (Exception e) {
                    log.error("消息渠道发送异常: 消息ID={}, 渠道类型={}, 错误={}", 
                            message.getId(), channelType, e.getMessage(), e);
                    sendResults.put(channelType, false);
                }
            }

            // 更新发送结果
            boolean overallSuccess = sendResults.values().stream().anyMatch(Boolean::booleanValue);
            String resultMessage = buildResultMessage(sendResults);
            updateMessageSendResult(message, overallSuccess, resultMessage);

            log.info("异步消息发送完成: 消息ID={}, 结果={}, 详情={}", 
                    message.getId(), overallSuccess ? "成功" : "失败", resultMessage);

        } catch (Exception e) {
            log.error("异步消息发送异常: 消息ID={}, 错误={}", message.getId(), e.getMessage(), e);
            updateMessageSendResult(message, false, "发送异常: " + e.getMessage());
        }
    }

    /**
     * 批量异步发送消息
     */
    @Async("taskExecutor")
    public void asyncBatchSendMessages(java.util.List<Message> messages) {
        log.info("开始批量异步发送消息，数量: {}", messages.size());

        int successCount = 0;
        int failedCount = 0;

        for (Message message : messages) {
            try {
                // 获取对应的发送策略
                MessageSendStrategy strategy = strategyFactory.getStrategy(message.getChannelType());
                if (strategy == null) {
                    log.warn("未找到对应的发送策略: 消息ID={}, 渠道类型={}", message.getId(), message.getChannelType());
                    updateMessageSendResult(message, false, "不支持的渠道类型: " + message.getChannelType());
                    failedCount++;
                    continue;
                }

                // 执行发送
                boolean sendSuccess = strategy.send(message);

                // 更新发送结果
                updateMessageSendResult(message, sendSuccess, sendSuccess ? "发送成功" : "发送失败");

                if (sendSuccess) {
                    successCount++;
                } else {
                    failedCount++;
                }

            } catch (Exception e) {
                log.error("批量异步消息发送异常: 消息ID={}, 错误={}", message.getId(), e.getMessage(), e);
                updateMessageSendResult(message, false, "发送异常: " + e.getMessage());
                failedCount++;
            }
        }

        log.info("批量异步消息发送完成: 成功={}, 失败={}, 总数={}", successCount, failedCount, messages.size());
    }

    /**
     * 更新消息发送结果
     */
    private void updateMessageSendResult(Message message, boolean success, String resultMessage) {
        try {
            // 构建发送结果
            Map<String, Object> sendResult = new HashMap<>();
            sendResult.put("success", success);
            sendResult.put("message", resultMessage);
            sendResult.put("sendTime", LocalDateTime.now().toString());
            sendResult.put("channelType", message.getChannelType());

            // 更新消息状态
            if (success) {
                message.setStatus(Message.Status.READ.getCode()); // 发送成功标记为已读
            } else {
                message.setStatus(Message.Status.UNREAD.getCode()); // 发送失败保持未读状态
            }

            // 保存发送结果
            message.setSendResult(objectMapper.writeValueAsString(sendResult));
            messageRepository.save(message);

            log.debug("消息发送结果已更新: 消息ID={}, 结果={}", message.getId(), resultMessage);

        } catch (JsonProcessingException e) {
            log.error("更新消息发送结果失败: 消息ID={}, 错误={}", message.getId(), e.getMessage(), e);
        } catch (Exception e) {
            log.error("更新消息状态失败: 消息ID={}, 错误={}", message.getId(), e.getMessage(), e);
        }
    }

    /**
     * 重试发送失败的消息
     */
    @Async("taskExecutor")
    public void asyncRetryFailedMessages(java.util.List<Message> failedMessages) {
        log.info("开始重试发送失败的消息，数量: {}", failedMessages.size());

        int retrySuccessCount = 0;
        int retryFailedCount = 0;

        for (Message message : failedMessages) {
            try {
                log.info("重试发送消息: 消息ID={}, 渠道类型={}", message.getId(), message.getChannelType());

                // 获取对应的发送策略
                MessageSendStrategy strategy = strategyFactory.getStrategy(message.getChannelType());
                if (strategy == null) {
                    log.warn("重试失败: 未找到对应的发送策略: 消息ID={}, 渠道类型={}", message.getId(), message.getChannelType());
                    retryFailedCount++;
                    continue;
                }

                // 执行重试发送
                boolean sendSuccess = strategy.send(message);

                // 更新重试结果
                updateRetryResult(message, sendSuccess, sendSuccess ? "重试成功" : "重试失败");

                if (sendSuccess) {
                    retrySuccessCount++;
                } else {
                    retryFailedCount++;
                }

            } catch (Exception e) {
                log.error("重试发送消息异常: 消息ID={}, 错误={}", message.getId(), e.getMessage(), e);
                updateRetryResult(message, false, "重试异常: " + e.getMessage());
                retryFailedCount++;
            }
        }

        log.info("重试发送完成: 成功={}, 失败={}, 总数={}", retrySuccessCount, retryFailedCount, failedMessages.size());
    }

    /**
     * 解析渠道类型字符串为列表
     */
    private List<String> parseChannelTypes(String channelType) {
        if (channelType == null || channelType.trim().isEmpty()) {
            return List.of("internal"); // 默认站内消息
        }
        return List.of(channelType.split("\\s*,\\s*"));
    }

    /**
     * 构建结果消息
     */
    private String buildResultMessage(Map<String, Boolean> sendResults) {
        StringBuilder sb = new StringBuilder();
        int successCount = 0;
        int totalCount = sendResults.size();
        
        for (Map.Entry<String, Boolean> entry : sendResults.entrySet()) {
            if (entry.getValue()) {
                successCount++;
                sb.append(entry.getKey()).append(":成功; ");
            } else {
                sb.append(entry.getKey()).append(":失败; ");
            }
        }
        
        return String.format("成功%d/%d个渠道: %s", successCount, totalCount, sb.toString());
    }

    /**
     * 更新重试结果
     */
    private void updateRetryResult(Message message, boolean success, String resultMessage) {
        try {
            // 解析原有的发送结果
            Map<String, Object> originalResult = new HashMap<>();
            if (message.getSendResult() != null) {
                originalResult = objectMapper.readValue(message.getSendResult(), Map.class);
            }

            // 添加重试信息
            Map<String, Object> retryInfo = new HashMap<>();
            retryInfo.put("retryTime", LocalDateTime.now().toString());
            retryInfo.put("retrySuccess", success);
            retryInfo.put("retryMessage", resultMessage);

            originalResult.put("retryInfo", retryInfo);

            // 更新消息状态
            if (success) {
                message.setStatus(Message.Status.READ.getCode()); // 重试成功标记为已读
            }

            // 保存重试结果
            message.setSendResult(objectMapper.writeValueAsString(originalResult));
            messageRepository.save(message);

            log.debug("消息重试结果已更新: 消息ID={}, 结果={}", message.getId(), resultMessage);

        } catch (Exception e) {
            log.error("更新重试结果失败: 消息ID={}, 错误={}", message.getId(), e.getMessage(), e);
        }
    }

    /**
     * 查找合适的模板
     */
    private MessageTemplate findSuitableTemplate(Message message) {
        try {
            // 1. 优先根据模板编码查找
            if (message.getTemplateCode() != null && !message.getTemplateCode().trim().isEmpty()) {
                Optional<MessageTemplate> templateByCode = messageTemplateRepository.findByCodeAndChannelTypeAndStatus(
                    message.getTemplateCode(), message.getChannelType(), MessageTemplate.Status.ENABLED.getCode());
                if (templateByCode.isPresent()) {
                    return templateByCode.get();
                }
            }

            // 2. 根据业务类型和渠道类型查找默认模板
            if (message.getBusinessType() != null && !message.getBusinessType().trim().isEmpty()) {
                Optional<MessageTemplate> templateByBusiness = messageTemplateRepository.findByBusinessTypeAndChannelTypeAndStatus(
                    message.getBusinessType(), message.getChannelType(), MessageTemplate.Status.ENABLED.getCode());
                if (templateByBusiness.isPresent()) {
                    return templateByBusiness.get();
                }
            }

            // 3. 查找渠道类型的默认模板
            Optional<MessageTemplate> defaultTemplate = messageTemplateRepository.findByCodeAndChannelTypeAndStatus(
                "default", message.getChannelType(), MessageTemplate.Status.ENABLED.getCode());
            if (defaultTemplate.isPresent()) {
                return defaultTemplate.get();
            }

            // 4. 查找系统默认模板
            Optional<MessageTemplate> systemDefaultTemplate = messageTemplateRepository.findByCodeAndStatus(
                "system_default", MessageTemplate.Status.ENABLED.getCode());
            if (systemDefaultTemplate.isPresent()) {
                return systemDefaultTemplate.get();
            }

            return null;

        } catch (Exception e) {
            log.error("查找模板异常: 消息ID={}, 错误={}", message.getId(), e.getMessage(), e);
            return null;
        }
    }

    /**
     * 应用模板到消息
     */
    private void applyTemplateToMessage(Message message, MessageTemplate template) {
        try {
            // 设置模板ID
            message.setTemplateId(template.getId());
            message.setTemplateCode(template.getCode());

            // 如果消息没有标题，使用模板主题（邮件模板）或模板名称
            if (message.getTitle() == null || message.getTitle().trim().isEmpty()) {
                if (template.getSubject() != null && !template.getSubject().trim().isEmpty()) {
                    message.setTitle(template.getSubject());
                } else {
                    message.setTitle(template.getName());
                }
            }

            // 如果消息没有内容，使用模板内容
            if (message.getContent() == null || message.getContent().trim().isEmpty()) {
                message.setContent(template.getContent());
            } else {
                // 如果消息有内容，但模板有内容，可以合并或替换（这里选择替换）
                message.setContent(template.getContent());
            }

            // 记录模板应用信息
            log.debug("模板已应用到消息: 消息ID={}, 模板ID={}, 模板编码={}", 
                     message.getId(), template.getId(), template.getCode());

        } catch (Exception e) {
            log.error("应用模板到消息异常: 消息ID={}, 模板ID={}, 错误={}", 
                     message.getId(), template.getId(), e.getMessage(), e);
        }
    }
}
