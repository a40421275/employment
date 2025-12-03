package com.shera.framework.employment.employment.modules.message.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 创建消息DTO
 */
@Data
public class MessageCreateDTO {

    private String title;
    private String content;
    private Long senderId;
    private List<Long> receiverIds;
    private Integer type;
    private Integer priority = 1;
    private LocalDateTime expireTime;
    private Long businessId;
    private String businessType;
    private String templateCode;
    private String channelType;
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
