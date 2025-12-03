package com.shera.framework.employment.employment.modules.message.service.impl;

import com.shera.framework.employment.employment.modules.message.dto.EmailSendRequest;
import com.shera.framework.employment.employment.modules.message.dto.EmailSendResult;
import com.shera.framework.employment.employment.modules.message.dto.EmailVerificationRequest;
import com.shera.framework.employment.employment.modules.message.dto.EmailVerificationResponse;
import com.shera.framework.employment.employment.modules.message.service.EmailVerificationService;
import com.shera.framework.employment.employment.modules.message.service.MessageSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 邮件验证服务实现类（内存存储版本）
 */
@Service
@Slf4j
public class EmailVerificationServiceImpl implements EmailVerificationService {

    @Autowired
    private MessageSenderService messageSenderService;

    // 使用内存存储验证码信息
    private static final Map<String, VerificationCodeInfo> verificationCodeStore = new ConcurrentHashMap<>();
    private static final Map<String, Integer> sendCountStore = new ConcurrentHashMap<>();
    
    private static final String VERIFICATION_CODE_PREFIX = "email_verification:";
    private static final String SEND_COUNT_PREFIX = "email_send_count:";
    private static final int MAX_SEND_COUNT_PER_DAY = 10;
    private static final Random random = new Random();

    @Override
    public EmailVerificationResponse sendVerificationCode(EmailVerificationRequest request) {
        try {
            String email = request.getEmail();
            String codeType = request.getCodeType();
            Integer expireMinutes = request.getExpireMinutes();
            Integer codeLength = request.getCodeLength();

            // 检查发送频率限制
            if (!checkSendLimit(email, codeType)) {
                return EmailVerificationResponse.failed("今日发送次数已达上限，请明天再试");
            }

            // 生成验证码
            String verificationCode = generateVerificationCode(codeLength);

            // 构建邮件内容
            String subject = buildEmailSubject(codeType, request.getBusinessScene());
            String content = buildEmailContent(verificationCode, codeType, expireMinutes, request.getBusinessScene());

            // 构建邮件发送请求
            EmailSendRequest emailRequest = new EmailSendRequest();
            emailRequest.setToEmails(java.util.Arrays.asList(email));
            emailRequest.setSubject(subject);
            emailRequest.setContent(content);
            emailRequest.setBusinessType("VERIFICATION");
            emailRequest.setBusinessId(1L); // 使用默认业务ID

            // 发送邮件
            EmailSendResult sendResult = messageSenderService.sendEmail(emailRequest);

            if (sendResult.isSuccess()) {
                // 保存验证码到内存
                saveVerificationCode(email, codeType, verificationCode, expireMinutes);

                // 记录发送次数
                recordSendCount(email, codeType);

                log.info("邮件验证码发送成功: 邮箱={}, 类型={}, 任务ID={}", email, codeType, sendResult.getTaskId());

                // 根据环境决定是否返回验证码（测试环境返回，生产环境不返回）
                boolean isTestEnvironment = isTestEnvironment();
                if (isTestEnvironment) {
                    return EmailVerificationResponse.success(sendResult.getTaskId(), verificationCode, expireMinutes);
                } else {
                    return EmailVerificationResponse.success(sendResult.getTaskId(), expireMinutes);
                }
            } else {
                log.warn("邮件验证码发送失败: 邮箱={}, 类型={}, 错误={}", email, codeType, sendResult.getErrorMessage());
                return EmailVerificationResponse.failed("邮件发送失败: " + sendResult.getErrorMessage());
            }

        } catch (Exception e) {
            log.error("邮件验证码发送异常: 邮箱={}, 类型={}, 错误={}", request.getEmail(), request.getCodeType(), e.getMessage(), e);
            return EmailVerificationResponse.failed("系统异常，请稍后重试");
        }
    }

    @Override
    public boolean verifyCode(String email, String code, String codeType) {
        try {
            String storeKey = buildStoreKey(email, codeType);
            VerificationCodeInfo codeInfo = verificationCodeStore.get(storeKey);

            if (codeInfo == null) {
                log.warn("验证码验证失败: 验证码不存在或已过期, 邮箱={}, 类型={}", email, codeType);
                return false;
            }

            // 检查是否过期
            if (codeInfo.isExpired()) {
                verificationCodeStore.remove(storeKey);
                log.warn("验证码验证失败: 验证码已过期, 邮箱={}, 类型={}", email, codeType);
                return false;
            }

            if (!codeInfo.getCode().equals(code)) {
                log.warn("验证码验证失败: 验证码不匹配, 邮箱={}, 类型={}", email, codeType);
                return false;
            }

            // 验证成功后删除验证码
            verificationCodeStore.remove(storeKey);
            log.info("验证码验证成功: 邮箱={}, 类型={}", email, codeType);
            return true;

        } catch (Exception e) {
            log.error("验证码验证异常: 邮箱={}, 类型={}, 错误={}", email, codeType, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean isCodeValid(String email, String codeType) {
        try {
            String storeKey = buildStoreKey(email, codeType);
            VerificationCodeInfo codeInfo = verificationCodeStore.get(storeKey);
            return codeInfo != null && !codeInfo.isExpired();
        } catch (Exception e) {
            log.error("检查验证码有效性异常: 邮箱={}, 类型={}, 错误={}", email, codeType, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Long getRemainingTime(String email, String codeType) {
        try {
            String storeKey = buildStoreKey(email, codeType);
            VerificationCodeInfo codeInfo = verificationCodeStore.get(storeKey);
            if (codeInfo == null || codeInfo.isExpired()) {
                return 0L;
            }
            return codeInfo.getRemainingSeconds();
        } catch (Exception e) {
            log.error("获取验证码剩余时间异常: 邮箱={}, 类型={}, 错误={}", email, codeType, e.getMessage(), e);
            return 0L;
        }
    }

    @Override
    public void clearCode(String email, String codeType) {
        try {
            String storeKey = buildStoreKey(email, codeType);
            verificationCodeStore.remove(storeKey);
            log.info("验证码已清除: 邮箱={}, 类型={}", email, codeType);
        } catch (Exception e) {
            log.error("清除验证码异常: 邮箱={}, 类型={}, 错误={}", email, codeType, e.getMessage(), e);
        }
    }

    /**
     * 生成验证码
     */
    private String generateVerificationCode(int length) {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    /**
     * 构建存储键
     */
    private String buildStoreKey(String email, String codeType) {
        return VERIFICATION_CODE_PREFIX + email + ":" + codeType;
    }

    /**
     * 构建发送次数存储键
     */
    private String buildSendCountKey(String email, String codeType) {
        String today = LocalDateTime.now().toLocalDate().toString();
        return SEND_COUNT_PREFIX + email + ":" + codeType + ":" + today;
    }

    /**
     * 保存验证码到内存
     */
    private void saveVerificationCode(String email, String codeType, String code, int expireMinutes) {
        String storeKey = buildStoreKey(email, codeType);
        VerificationCodeInfo codeInfo = new VerificationCodeInfo(code, expireMinutes);
        verificationCodeStore.put(storeKey, codeInfo);
    }

    /**
     * 检查发送频率限制
     */
    private boolean checkSendLimit(String email, String codeType) {
        String countKey = buildSendCountKey(email, codeType);
        Integer count = sendCountStore.get(countKey);
        
        if (count == null) {
            count = 0;
        }

        if (count >= MAX_SEND_COUNT_PER_DAY) {
            return false;
        }

        return true;
    }

    /**
     * 记录发送次数
     */
    private void recordSendCount(String email, String codeType) {
        String countKey = buildSendCountKey(email, codeType);
        Integer count = sendCountStore.get(countKey);
        if (count == null) {
            count = 0;
        }
        sendCountStore.put(countKey, count + 1);
        
        // 清理过期的发送记录（简化处理，实际应该定期清理）
        cleanupExpiredSendCounts();
    }

    /**
     * 清理过期的发送记录
     */
    private void cleanupExpiredSendCounts() {
        // 简化处理：定期清理过期的发送记录
        // 实际应该使用定时任务清理
        if (sendCountStore.size() > 1000) {
            sendCountStore.clear();
        }
    }

    /**
     * 构建邮件主题
     */
    private String buildEmailSubject(String codeType, String businessScene) {
        String sceneText = businessScene != null ? " - " + businessScene : "";
        
        switch (codeType.toLowerCase()) {
            case "register":
                return "注册验证码" + sceneText;
            case "login":
                return "登录验证码" + sceneText;
            case "reset_password":
                return "重置密码验证码" + sceneText;
            case "change_email":
                return "修改邮箱验证码" + sceneText;
            default:
                return "验证码" + sceneText;
        }
    }

    /**
     * 构建邮件内容
     */
    private String buildEmailContent(String code, String codeType, int expireMinutes, String businessScene) {
        String actionText = getActionText(codeType);
        String sceneText = businessScene != null ? businessScene : "操作";
        
        return String.format(
            "尊敬的用户：\n\n" +
            "您正在进行%s%s，验证码为：\n\n" +
            "【%s】\n\n" +
            "验证码有效期：%d分钟\n\n" +
            "请勿向任何人泄露此验证码。\n\n" +
            "如非本人操作，请忽略此邮件。\n\n" +
            "感谢您的使用！",
            sceneText, actionText, code, expireMinutes
        );
    }

    /**
     * 获取操作文本
     */
    private String getActionText(String codeType) {
        switch (codeType.toLowerCase()) {
            case "register":
                return "注册";
            case "login":
                return "登录";
            case "reset_password":
                return "重置密码";
            case "change_email":
                return "修改邮箱";
            default:
                return "";
        }
    }

    /**
     * 判断是否为测试环境
     */
    private boolean isTestEnvironment() {
        // 这里可以根据实际环境配置来判断
        // 例如：检查配置文件中的环境变量
        return true; // 暂时返回true用于测试
    }

    /**
     * 验证码信息类
     */
    private static class VerificationCodeInfo {
        private final String code;
        private final LocalDateTime createTime;
        private final int expireMinutes;

        public VerificationCodeInfo(String code, int expireMinutes) {
            this.code = code;
            this.createTime = LocalDateTime.now();
            this.expireMinutes = expireMinutes;
        }

        public String getCode() {
            return code;
        }

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(createTime.plusMinutes(expireMinutes));
        }

        public long getRemainingSeconds() {
            LocalDateTime expireTime = createTime.plusMinutes(expireMinutes);
            return java.time.Duration.between(LocalDateTime.now(), expireTime).getSeconds();
        }
    }
}