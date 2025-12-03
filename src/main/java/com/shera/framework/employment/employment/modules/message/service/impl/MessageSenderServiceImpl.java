package com.shera.framework.employment.employment.modules.message.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shera.framework.employment.employment.modules.message.dto.EmailSendRequest;
import com.shera.framework.employment.employment.modules.message.dto.EmailSendResult;
import com.shera.framework.employment.employment.modules.message.service.MessageSenderService;
import com.shera.framework.employment.employment.util.SystemConfigUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 消息发送服务实现类
 */
@Service
@Slf4j
public class MessageSenderServiceImpl implements MessageSenderService {

    @Autowired
    private SystemConfigUtil systemConfigUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private JavaMailSender mailSender;
    private OkHttpClient httpClient;
    private String wechatAccessToken;
    private long tokenExpireTime;

    /**
     * 初始化邮件发送器
     */
    private void initMailSender() {
        if (mailSender != null) {
            return;
        }

        if (!systemConfigUtil.isEmailConfigValid()) {
            log.warn("邮件配置不完整，无法初始化邮件发送器: {}", systemConfigUtil.getEmailConfigSummary());
            return;
        }

        try {
            JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
            mailSenderImpl.setHost(systemConfigUtil.getEmailHost());
            mailSenderImpl.setPort(systemConfigUtil.getEmailPort());
            mailSenderImpl.setUsername(systemConfigUtil.getEmailUsername());
            mailSenderImpl.setPassword(systemConfigUtil.getEmailPassword());

            // 配置邮件属性
            Properties props = mailSenderImpl.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");
            props.put("mail.smtp.socketFactory.port", systemConfigUtil.getEmailPort().toString());
            props.put("mail.debug", "true"); // 调试模式，生产环境设为false

            mailSender = mailSenderImpl;
            log.info("邮件发送器初始化成功: {}", systemConfigUtil.getEmailConfigSummary());
        } catch (Exception e) {
            log.error("邮件发送器初始化失败", e);
        }
    }

    @Override
    public EmailSendResult sendEmail(EmailSendRequest request) {
        try {
            log.info("开始发送邮件: 收件人={}, 主题={}", request.getToEmails(), request.getSubject());
            
            // 检查邮件配置
            if (!systemConfigUtil.isEmailEnabled()) {
                log.warn("邮件服务未启用，跳过邮件发送");
                return EmailSendResult.failed("邮件服务未启用");
            }

            if (!systemConfigUtil.isEmailConfigValid()) {
                log.warn("邮件配置不完整，无法发送邮件: {}", systemConfigUtil.getEmailConfigSummary());
                return EmailSendResult.failed("邮件配置不完整");
            }

            // 初始化邮件发送器
            initMailSender();
            if (mailSender == null) {
                log.error("邮件发送器初始化失败");
                return EmailSendResult.failed("邮件发送器初始化失败");
            }

            // 创建邮件消息
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(systemConfigUtil.getEmailFrom());
            message.setTo(request.getToEmails().toArray(new String[0]));
            message.setSubject(request.getSubject());
            message.setText(request.getContent());

            // 发送邮件
            mailSender.send(message);
            
            String taskId = UUID.randomUUID().toString();
            log.info("邮件发送成功: 收件人={}, 任务ID={}", request.getToEmails(), taskId);
            return EmailSendResult.success(request.getToEmails().size(), taskId);
            
        } catch (Exception e) {
            log.error("邮件发送异常: 收件人={}, 错误={}", request.getToEmails(), e.getMessage(), e);
            return EmailSendResult.failed("邮件发送异常: " + e.getMessage());
        }
    }

    @Override
    public boolean sendSms(String phone, String content) {
        try {
            log.info("开始发送短信: 手机号={}, 内容长度={}", phone, content.length());
            
            // 模拟短信发送过程
            // TODO: 这里需要集成实际的短信发送服务
            // 例如：使用阿里云短信、腾讯云短信等
            
            // 模拟发送延迟
            Thread.sleep(50);
            
            // 模拟发送结果
            boolean success = Math.random() > 0.05; // 95%成功率
            
            if (success) {
                log.info("短信发送成功: 手机号={}", phone);
            } else {
                log.warn("短信发送失败: 手机号={}", phone);
            }
            
            return success;
            
        } catch (Exception e) {
            log.error("短信发送异常: 手机号={}, 错误={}", phone, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean sendWechat(String openId, String content) {
        try {
            log.info("开始发送微信消息: OpenID={}, 内容长度={}", openId, content.length());
            
            // 检查微信配置
            if (!isWechatConfigValid()) {
                log.warn("微信配置不完整，无法发送微信消息");
                return false;
            }
            
            // 获取微信访问令牌
            String accessToken = getWechatAccessToken();
            if (accessToken == null) {
                log.error("获取微信访问令牌失败");
                return false;
            }
            
            // 发送文本消息
            boolean success = sendWechatTextMessage(openId, content, accessToken);
            
            if (success) {
                log.info("微信消息发送成功: OpenID={}", openId);
            } else {
                log.warn("微信消息发送失败: OpenID={}", openId);
            }
            
            return success;
            
        } catch (Exception e) {
            log.error("微信消息发送异常: OpenID={}, 错误={}", openId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean sendPush(String deviceToken, String title, String content) {
        try {
            log.info("开始发送推送通知: 设备Token={}, 标题={}", deviceToken, title);
            
            // 模拟推送通知发送过程
            // TODO: 这里需要集成实际的推送通知服务
            // 例如：使用极光推送、个推、Firebase等
            
            // 模拟发送延迟
            Thread.sleep(60);
            
            // 模拟发送结果
            boolean success = Math.random() > 0.08; // 92%成功率
            
            if (success) {
                log.info("推送通知发送成功: 设备Token={}", deviceToken);
            } else {
                log.warn("推送通知发送失败: 设备Token={}", deviceToken);
            }
            
            return success;
            
        } catch (Exception e) {
            log.error("推送通知发送异常: 设备Token={}, 错误={}", deviceToken, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean sendByChannel(String channelType, String target, String title, String content) {
        try {
            log.info("通过渠道发送消息: 渠道类型={}, 目标={}, 标题={}", channelType, target, title);
            
            // 根据渠道类型调用相应的发送方法
            switch (channelType.toLowerCase()) {
                case "email":
                    EmailSendRequest emailRequest = new EmailSendRequest();
                    emailRequest.setToEmails(java.util.Collections.singletonList(target));
                    emailRequest.setSubject(title);
                    emailRequest.setContent(content);
                    EmailSendResult emailResult = sendEmail(emailRequest);
                    return emailResult.isSuccess();
                    
                case "sms":
                    return sendSms(target, content);
                    
                case "wechat":
                    return sendWechat(target, content);
                    
                case "push":
                    return sendPush(target, title, content);
                    
                default:
                    log.warn("不支持的渠道类型: {}", channelType);
                    return false;
            }
            
        } catch (Exception e) {
            log.error("通过渠道发送消息异常: 渠道类型={}, 目标={}, 错误={}", channelType, target, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 检查微信配置是否有效
     */
    private boolean isWechatConfigValid() {
        String appId = systemConfigUtil.getWechatAppId();
        String appSecret = systemConfigUtil.getWechatAppSecret();
        
        if (appId == null || appId.trim().isEmpty()) {
            log.warn("微信AppID未配置");
            return false;
        }
        
        if (appSecret == null || appSecret.trim().isEmpty()) {
            log.warn("微信AppSecret未配置");
            return false;
        }
        
        return true;
    }

    /**
     * 获取微信访问令牌
     */
    private synchronized String getWechatAccessToken() {
        // 检查令牌是否过期（提前5分钟刷新）
        if (wechatAccessToken != null && System.currentTimeMillis() < tokenExpireTime - 5 * 60 * 1000) {
            return wechatAccessToken;
        }
        
        try {
            String appId = systemConfigUtil.getWechatAppId();
            String appSecret = systemConfigUtil.getWechatAppSecret();
            
            String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
                    appId, appSecret);
            
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            
            Response response = getHttpClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                log.error("获取微信访问令牌失败: HTTP {}", response.code());
                return null;
            }
            
            String responseBody = response.body().string();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            
            if (jsonNode.has("errcode") && jsonNode.get("errcode").asInt() != 0) {
                log.error("获取微信访问令牌失败: {}", responseBody);
                return null;
            }
            
            String accessToken = jsonNode.get("access_token").asText();
            int expiresIn = jsonNode.get("expires_in").asInt();
            
            wechatAccessToken = accessToken;
            tokenExpireTime = System.currentTimeMillis() + expiresIn * 1000L;
            
            log.info("微信访问令牌获取成功，有效期: {}秒", expiresIn);
            return accessToken;
            
        } catch (Exception e) {
            log.error("获取微信访问令牌异常", e);
            return null;
        }
    }

    /**
     * 发送微信文本消息
     */
    private boolean sendWechatTextMessage(String openId, String content, String accessToken) {
        try {
            String url = String.format("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s", accessToken);
            
            Map<String, Object> message = new HashMap<>();
            message.put("touser", openId);
            message.put("msgtype", "text");
            
            Map<String, String> textContent = new HashMap<>();
            textContent.put("content", content);
            message.put("text", textContent);
            
            String jsonBody = objectMapper.writeValueAsString(message);
            
            RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json; charset=utf-8")
            );
            
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            
            Response response = getHttpClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                log.error("发送微信消息失败: HTTP {}", response.code());
                return false;
            }
            
            String responseBody = response.body().string();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            
            int errcode = jsonNode.get("errcode").asInt();
            if (errcode == 0) {
                log.info("微信消息发送成功: OpenID={}", openId);
                return true;
            } else {
                log.error("发送微信消息失败: {}", responseBody);
                
                // 如果令牌过期，清除缓存重新获取
                if (errcode == 40001 || errcode == 42001) {
                    log.warn("微信访问令牌过期，清除缓存");
                    wechatAccessToken = null;
                }
                
                return false;
            }
            
        } catch (Exception e) {
            log.error("发送微信文本消息异常", e);
            return false;
        }
    }

    /**
     * 获取HTTP客户端
     */
    private OkHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();
        }
        return httpClient;
    }

    /**
     * 发送微信模板消息
     */
    public boolean sendWechatTemplateMessage(String openId, String templateId, Map<String, Object> data, String url) {
        try {
            String accessToken = getWechatAccessToken();
            if (accessToken == null) {
                log.error("获取微信访问令牌失败");
                return false;
            }
            
            String apiUrl = String.format("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s", accessToken);
            
            Map<String, Object> message = new HashMap<>();
            message.put("touser", openId);
            message.put("template_id", templateId);
            message.put("data", data);
            
            if (url != null && !url.trim().isEmpty()) {
                message.put("url", url);
            }
            
            String jsonBody = objectMapper.writeValueAsString(message);
            
            RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json; charset=utf-8")
            );
            
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(body)
                    .build();
            
            Response response = getHttpClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                log.error("发送微信模板消息失败: HTTP {}", response.code());
                return false;
            }
            
            String responseBody = response.body().string();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            
            int errcode = jsonNode.get("errcode").asInt();
            if (errcode == 0) {
                log.info("微信模板消息发送成功: OpenID={}, TemplateID={}", openId, templateId);
                return true;
            } else {
                log.error("发送微信模板消息失败: {}", responseBody);
                return false;
            }
            
        } catch (Exception e) {
            log.error("发送微信模板消息异常", e);
            return false;
        }
    }

    /**
     * 发送微信图文消息
     */
    public boolean sendWechatNewsMessage(String openId, String title, String description, String url, String picUrl) {
        try {
            String accessToken = getWechatAccessToken();
            if (accessToken == null) {
                log.error("获取微信访问令牌失败");
                return false;
            }
            
            String apiUrl = String.format("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s", accessToken);
            
            Map<String, Object> message = new HashMap<>();
            message.put("touser", openId);
            message.put("msgtype", "news");
            
            Map<String, Object> news = new HashMap<>();
            Map<String, Object> article = new HashMap<>();
            article.put("title", title);
            article.put("description", description);
            article.put("url", url);
            article.put("picurl", picUrl);
            
            news.put("articles", new Map[]{article});
            message.put("news", news);
            
            String jsonBody = objectMapper.writeValueAsString(message);
            
            RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json; charset=utf-8")
            );
            
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(body)
                    .build();
            
            Response response = getHttpClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                log.error("发送微信图文消息失败: HTTP {}", response.code());
                return false;
            }
            
            String responseBody = response.body().string();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            
            int errcode = jsonNode.get("errcode").asInt();
            if (errcode == 0) {
                log.info("微信图文消息发送成功: OpenID={}, Title={}", openId, title);
                return true;
            } else {
                log.error("发送微信图文消息失败: {}", responseBody);
                return false;
            }
            
        } catch (Exception e) {
            log.error("发送微信图文消息异常", e);
            return false;
        }
    }
}
