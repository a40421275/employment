package com.shera.framework.employment.employment.modules.message.dto;

import lombok.Data;

/**
 * 消息查询DTO
 */
@Data
public class MessageQueryDTO {

    private Long userId;
    private Integer type;
    private Integer status;
    private String keyword;
    private Integer page = 0;
    private Integer size = 20;
}
