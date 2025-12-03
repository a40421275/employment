package com.shera.framework.employment.employment.modules.message.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息DTO
 */
@Data
public class MessageDTO {

    private Long id;
    private String title;
    private String content;
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String receiverName;
    private Integer type;
    private String typeName;
    private Integer priority;
    private String priorityName;
    private Integer status;
    private String statusName;
    private LocalDateTime createTime;
    private LocalDateTime readTime;
    private LocalDateTime expireTime;
    private Long businessId;
    private String businessType;
    private Long templateId;
    private String channelType;
    private String sendResult;
    private String attachments;
    
    /**
     * 获取渠道类型列表
     */
    public List<String> getChannelTypes() {
        if (channelType == null || channelType.trim().isEmpty()) {
            return List.of("internal"); // 默认站内消息
        }
        return List.of(channelType.split("\\s*,\\s*"));
    }
}
