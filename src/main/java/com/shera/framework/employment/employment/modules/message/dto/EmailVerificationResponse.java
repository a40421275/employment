package com.shera.framework.employment.employment.modules.message.dto;

import lombok.Data;

/**
 * 邮件验证发送响应
 */
@Data
public class EmailVerificationResponse {

    /**
     * 是否发送成功
     */
    private boolean success;

    /**
     * 发送结果消息
     */
    private String message;

    /**
     * 验证码（仅用于测试环境）
     */
    private String verificationCode;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 过期时间（分钟）
     */
    private Integer expireMinutes;

    /**
     * 发送时间
     */
    private String sendTime;

    /**
     * 成功响应
     */
    public static EmailVerificationResponse success(String taskId, String verificationCode, Integer expireMinutes) {
        EmailVerificationResponse response = new EmailVerificationResponse();
        response.setSuccess(true);
        response.setMessage("邮件验证码发送成功");
        response.setTaskId(taskId);
        response.setVerificationCode(verificationCode);
        response.setExpireMinutes(expireMinutes);
        response.setSendTime(java.time.LocalDateTime.now().toString());
        return response;
    }

    /**
     * 失败响应
     */
    public static EmailVerificationResponse failed(String message) {
        EmailVerificationResponse response = new EmailVerificationResponse();
        response.setSuccess(false);
        response.setMessage(message);
        response.setSendTime(java.time.LocalDateTime.now().toString());
        return response;
    }

    /**
     * 成功响应（不包含验证码）
     */
    public static EmailVerificationResponse success(String taskId, Integer expireMinutes) {
        EmailVerificationResponse response = new EmailVerificationResponse();
        response.setSuccess(true);
        response.setMessage("邮件验证码发送成功");
        response.setTaskId(taskId);
        response.setExpireMinutes(expireMinutes);
        response.setSendTime(java.time.LocalDateTime.now().toString());
        return response;
    }
}