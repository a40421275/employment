package com.shera.framework.employment.employment.modules.user.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 重置密码请求DTO
 * 支持邮箱+验证码+新密码的重置方式
 */
@Data
public class ResetPasswordDTO {

    /**
     * 邮箱地址
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码必须是6位数字")
    private String verificationCode;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String newPassword;

    /**
     * 验证码类型
     */
    private String codeType = "reset_password";


    /**
     * 获取验证码类型
     */
    public String getCodeType() {
        return codeType != null ? codeType : "reset_password";
    }
}
