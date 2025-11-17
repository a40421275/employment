package com.shera.framework.employment.employment.modules.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知数据传输对象
 */
@Data
public class NotificationDTO {
    
    private Long id;
    
    private Long userId;
    
    private String title;
    
    private String content;
    
    private Integer type = 1; // 1-系统通知，2-申请状态通知，3-面试通知，4-消息提醒
    
    private Integer status = 1; // 1-未读，2-已读
    
    private String relatedId; // 关联的业务ID
    
    private String relatedType; // 关联的业务类型
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime readTime;
}
