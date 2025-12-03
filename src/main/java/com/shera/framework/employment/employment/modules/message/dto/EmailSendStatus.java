package com.shera.framework.employment.employment.modules.message.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 邮件发送状态DTO
 */
@Data
public class EmailSendStatus {
    
    /**
     * 邮件发送任务ID
     */
    private String taskId;
    
    /**
     * 发送状态
     */
    private Status status;
    
    /**
     * 成功发送数量
     */
    private int successCount;
    
    /**
     * 发送失败数量
     */
    private int failedCount;
    
    /**
     * 发送失败的邮箱列表
     */
    private List<String> failedEmails;
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 发送状态枚举
     */
    public enum Status {
        PENDING("pending", "待发送"),
        SENDING("sending", "发送中"),
        COMPLETED("completed", "已完成"),
        FAILED("failed", "失败");
        
        private final String code;
        private final String desc;
        
        Status(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        
        public String getCode() {
            return code;
        }
        
        public String getDesc() {
            return desc;
        }
        
        public static Status fromCode(String code) {
            for (Status status : Status.values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            return PENDING;
        }
    }
}