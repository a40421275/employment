package com.shera.framework.employment.employment.modules.message.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 邮件发送请求DTO
 */
@Data
public class EmailSendRequest {
    
    /**
     * 模板编码
     */
    private String templateCode;
    
    /**
     * 收件人邮箱列表
     */
    private List<String> toEmails;
    
    /**
     * 邮件主题（如使用模板则忽略）
     */
    private String subject;
    
    /**
     * 邮件内容（如使用模板则忽略）
     */
    private String content;
    
    /**
     * 模板变量
     */
    private Map<String, Object> variables;
    
    /**
     * 附件列表
     */
    private List<EmailAttachment> attachments;
    
    /**
     * 发送者ID
     */
    private Long senderId;
    
    /**
     * 业务ID
     */
    private Long businessId;
    
    /**
     * 业务类型
     */
    private String businessType;
    
    /**
     * 邮件附件
     */
    @Data
    public static class EmailAttachment {
        private String name;
        private String url;
    }
}