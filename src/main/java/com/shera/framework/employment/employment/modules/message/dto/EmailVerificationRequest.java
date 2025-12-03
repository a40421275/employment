package com.shera.framework.employment.employment.modules.message.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 邮件验证发送请求
 */
@Data
public class EmailVerificationRequest {

    /**
     * 接收者邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 验证码类型
     */
    @NotBlank(message = "验证码类型不能为空")
    private String codeType;

    /**
     * 业务场景
     */
    private String businessScene;

    /**
     * 过期时间（分钟）
     */
    private Integer expireMinutes = 30;

    /**
     * 验证码长度
     */
    private Integer codeLength = 6;
}
