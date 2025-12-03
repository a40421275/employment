package com.shera.framework.employment.employment.modules.message.service;

import com.shera.framework.employment.employment.modules.message.dto.EmailVerificationRequest;
import com.shera.framework.employment.employment.modules.message.dto.EmailVerificationResponse;

/**
 * 邮件验证服务接口
 */
public interface EmailVerificationService {

    /**
     * 发送邮件验证码
     *
     * @param request 邮件验证请求
     * @return 发送结果
     */
    EmailVerificationResponse sendVerificationCode(EmailVerificationRequest request);

    /**
     * 验证邮件验证码
     *
     * @param email 邮箱
     * @param code 验证码
     * @param codeType 验证码类型
     * @return 验证结果
     */
    boolean verifyCode(String email, String code, String codeType);

    /**
     * 检查验证码是否有效
     *
     * @param email 邮箱
     * @param codeType 验证码类型
     * @return 是否有效
     */
    boolean isCodeValid(String email, String codeType);

    /**
     * 获取剩余有效时间
     *
     * @param email 邮箱
     * @param codeType 验证码类型
     * @return 剩余时间（秒）
     */
    Long getRemainingTime(String email, String codeType);

    /**
     * 清除验证码
     *
     * @param email 邮箱
     * @param codeType 验证码类型
     */
    void clearCode(String email, String codeType);
}