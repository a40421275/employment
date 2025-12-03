package com.shera.framework.employment.employment.modules.message.service;

import com.shera.framework.employment.employment.modules.message.dto.EmailSendRequest;
import com.shera.framework.employment.employment.modules.message.dto.EmailSendResult;

/**
 * 消息发送服务接口
 */
public interface MessageSenderService {

    /**
     * 发送邮件
     */
    EmailSendResult sendEmail(EmailSendRequest request);

    /**
     * 发送短信
     */
    boolean sendSms(String phone, String content);

    /**
     * 发送微信消息
     */
    boolean sendWechat(String openId, String content);

    /**
     * 发送推送通知
     */
    boolean sendPush(String deviceToken, String title, String content);

    /**
     * 根据渠道类型发送消息
     */
    boolean sendByChannel(String channelType, String target, String title, String content);
}
