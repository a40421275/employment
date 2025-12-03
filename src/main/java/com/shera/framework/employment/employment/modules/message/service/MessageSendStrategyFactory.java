package com.shera.framework.employment.employment.modules.message.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 消息发送策略工厂
 */
@Component
@Slf4j
public class MessageSendStrategyFactory {

    private final Map<String, MessageSendStrategy> strategyMap;

    @Autowired
    public MessageSendStrategyFactory(List<MessageSendStrategy> strategies) {
        // 将所有策略按支持的渠道类型分组
        this.strategyMap = strategies.stream()
                .flatMap(strategy -> {
                    // 这里可以根据需要扩展支持多个渠道类型
                    String[] supportedTypes = getSupportedTypes(strategy);
                    return java.util.Arrays.stream(supportedTypes)
                            .map(type -> Map.entry(type, strategy));
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existing, replacement) -> {
                            log.warn("发现重复的策略配置: {}，使用第一个策略", existing);
                            return existing;
                        }
                ));
        
        log.info("消息发送策略工厂初始化完成，支持的渠道类型: {}", strategyMap.keySet());
    }

    /**
     * 根据渠道类型获取对应的发送策略
     */
    public MessageSendStrategy getStrategy(String channelType) {
        if (channelType == null) {
            return null;
        }
        
        MessageSendStrategy strategy = strategyMap.get(channelType.toLowerCase());
        if (strategy == null) {
            log.warn("未找到对应的消息发送策略: {}", channelType);
        }
        
        return strategy;
    }

    /**
     * 检查是否支持该渠道类型
     */
    public boolean supports(String channelType) {
        return channelType != null && strategyMap.containsKey(channelType.toLowerCase());
    }

    /**
     * 获取策略支持的所有渠道类型
     */
    public String[] getSupportedTypes(MessageSendStrategy strategy) {
        // 这里可以根据策略类返回支持的类型数组
        if (strategy.supports("email")) {
            return new String[]{"email"};
        } else if (strategy.supports("sms")) {
            return new String[]{"sms"};
        } else if (strategy.supports("wechat")) {
            return new String[]{"wechat"};
        } else if (strategy.supports("push")) {
            return new String[]{"push"};
        } else {
            return new String[0];
        }
    }

    /**
     * 获取所有支持的渠道类型
     */
    public java.util.Set<String> getAllSupportedTypes() {
        return strategyMap.keySet();
    }

    /**
     * 获取策略数量
     */
    public int getStrategyCount() {
        return strategyMap.size();
    }
}
