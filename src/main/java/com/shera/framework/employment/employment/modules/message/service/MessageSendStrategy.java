package com.shera.framework.employment.employment.modules.message.service;

import com.shera.framework.employment.employment.modules.message.entity.Message;

/**
 * 消息发送策略接口
 */
public interface MessageSendStrategy {

    /**
     * 是否支持该渠道类型
     */
    boolean supports(String channelType);

    /**
     * 发送消息
     */
    boolean send(Message message);
}