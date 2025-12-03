package com.shera.framework.employment.employment.modules.message;

import com.shera.framework.employment.employment.modules.message.dto.EmailSendRequest;
import com.shera.framework.employment.employment.modules.message.dto.EmailSendResult;
import com.shera.framework.employment.employment.modules.message.service.MessageSenderService;
import com.shera.framework.employment.employment.util.SystemConfigUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

/**
 * 邮件发送测试类
 */
@SpringBootTest
@Slf4j
public class EmailSendTest {

    @Autowired
    private MessageSenderService messageSenderService;

    @Autowired
    private SystemConfigUtil systemConfigUtil;

    @Test
    public void testEmailConfig() {
        log.info("=== 邮件配置测试 ===");
        log.info("邮件服务是否启用: {}", systemConfigUtil.isEmailEnabled());
        log.info("邮件配置是否完整: {}", systemConfigUtil.isEmailConfigValid());
        log.info("邮件配置摘要: {}", systemConfigUtil.getEmailConfigSummary());
        log.info("=== 邮件配置测试结束 ===");
    }

    @Test
    public void testSendEmail() {
        log.info("=== 邮件发送测试 ===");
        
        // 检查邮件配置
        if (!systemConfigUtil.isEmailEnabled()) {
            log.warn("邮件服务未启用，跳过测试");
            return;
        }

        if (!systemConfigUtil.isEmailConfigValid()) {
            log.warn("邮件配置不完整，跳过测试: {}", systemConfigUtil.getEmailConfigSummary());
            return;
        }

        // 创建测试邮件请求
        EmailSendRequest request = new EmailSendRequest();
        request.setToEmails(Arrays.asList("test@example.com")); // 请替换为实际的测试邮箱
        request.setSubject("就业服务平台测试邮件");
        request.setContent("这是一封来自就业服务平台的测试邮件。\n\n" +
                "如果您收到此邮件，说明邮件发送功能配置正确。\n" +
                "发送时间: " + java.time.LocalDateTime.now() + "\n\n" +
                "感谢使用我们的服务！");

        // 发送邮件
        EmailSendResult result = messageSenderService.sendEmail(request);

        // 验证结果
        if (result.isSuccess()) {
            log.info("邮件发送测试成功: 任务ID={}, 发送数量={}", result.getTaskId(), result.getSuccessCount());
        } else {
            log.warn("邮件发送测试失败: {}", result.getErrorMessage());
        }
        
        log.info("=== 邮件发送测试结束 ===");
    }

    @Test
    public void testEmailSendStrategy() {
        log.info("=== 邮件发送策略测试 ===");
        
        // 检查邮件配置
        if (!systemConfigUtil.isEmailEnabled()) {
            log.warn("邮件服务未启用，跳过测试");
            return;
        }

        if (!systemConfigUtil.isEmailConfigValid()) {
            log.warn("邮件配置不完整，跳过测试: {}", systemConfigUtil.getEmailConfigSummary());
            return;
        }

        // 测试通过渠道发送邮件
        boolean success = messageSenderService.sendByChannel(
                "email", 
                "test@example.com", // 请替换为实际的测试邮箱
                "渠道发送测试邮件", 
                "这是一封通过渠道发送的测试邮件。\n\n" +
                "发送时间: " + java.time.LocalDateTime.now()
        );

        if (success) {
            log.info("邮件发送策略测试成功");
        } else {
            log.warn("邮件发送策略测试失败");
        }
        
        log.info("=== 邮件发送策略测试结束 ===");
    }
}