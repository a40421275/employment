package com.shera.framework.employment.employment.modules.message.dto;

import lombok.Data;

import java.util.List;

/**
 * 邮件发送结果DTO
 */
@Data
public class EmailSendResult {
    
    /**
     * 是否成功
     */
    private boolean success;
    
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
     * 邮件发送任务ID
     */
    private String taskId;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 成功结果
     */
    public static EmailSendResult success(int successCount, String taskId) {
        EmailSendResult result = new EmailSendResult();
        result.setSuccess(true);
        result.setSuccessCount(successCount);
        result.setFailedCount(0);
        result.setTaskId(taskId);
        return result;
    }
    
    /**
     * 失败结果
     */
    public static EmailSendResult failed(String errorMessage) {
        EmailSendResult result = new EmailSendResult();
        result.setSuccess(false);
        result.setSuccessCount(0);
        result.setFailedCount(1);
        result.setErrorMessage(errorMessage);
        return result;
    }
    
    /**
     * 批量发送结果
     */
    public static EmailSendResult batchResult(int successCount, int failedCount, 
                                            List<String> failedEmails, String taskId) {
        EmailSendResult result = new EmailSendResult();
        result.setSuccess(failedCount == 0);
        result.setSuccessCount(successCount);
        result.setFailedCount(failedCount);
        result.setFailedEmails(failedEmails);
        result.setTaskId(taskId);
        return result;
    }
}