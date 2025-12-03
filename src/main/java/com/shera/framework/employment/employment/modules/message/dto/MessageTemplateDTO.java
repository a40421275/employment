package com.shera.framework.employment.employment.modules.message.dto;

import lombok.Data;

/**
 * 消息模板DTO
 */
@Data
public class MessageTemplateDTO {

    private Long id;
    private String code;
    private String name;
    private String channelType;
    private String typeName;
    private String businessType;
    private String subject;
    private String content;
    private String variables;
    private Integer status;
    private String statusName;
    private String description;
    private String createTime;
    private String updateTime;
}
