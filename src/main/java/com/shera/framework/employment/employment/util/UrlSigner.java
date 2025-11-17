package com.shera.framework.employment.employment.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * URL签名工具类
 * 用于生成和验证带签名的文件访问URL
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class UrlSigner {
    
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    
    @Value("${app.url-signer.secret:default-secret-key-change-in-production}")
    private String secretKey;
    
    private final SystemConfigUtil systemConfigUtil;
    
    /**
     * 生成签名URL
     * @param attachmentId 附件ID
     * @return 签名URL
     */
    public String generateSignedUrl(Long attachmentId) {
        try {
            // 从系统配置获取过期时间
            long expirySeconds = systemConfigUtil.getUrlSignerExpirySeconds();
            long expiryTime = Instant.now().getEpochSecond() + expirySeconds;
            
            // 构建签名数据
            String dataToSign = attachmentId + ":" + expiryTime;
            
            // 生成签名
            String signature = generateSignature(dataToSign);
            
            // 构建URL参数
            Map<String, String> params = new HashMap<>();
            params.put("attachmentId", attachmentId.toString());
            params.put("expires", String.valueOf(expiryTime));
            params.put("signature", signature);
            
            // 构建URL
            StringBuilder urlBuilder = new StringBuilder("/api/files/signed/");
            boolean firstParam = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (firstParam) {
                    urlBuilder.append("?");
                    firstParam = false;
                } else {
                    urlBuilder.append("&");
                }
                urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                         .append("=")
                         .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            }
            
            return urlBuilder.toString();
            
        } catch (Exception e) {
            log.error("生成签名URL失败 - 附件ID: {}, 错误: {}", attachmentId, e.getMessage());
            throw new RuntimeException("生成签名URL失败", e);
        }
    }
    
    /**
     * 验证签名URL
     * @param attachmentId 附件ID
     * @param expires 过期时间戳
     * @param signature 签名
     * @return 验证是否通过
     */
    public boolean verifySignature(Long attachmentId, Long expires, String signature) {
        try {
            // 检查是否过期
            if (expires < Instant.now().getEpochSecond()) {
                log.warn("签名URL已过期 - 附件ID: {}, 过期时间: {}", attachmentId, expires);
                return false;
            }
            
            // 重新计算签名
            String dataToSign = attachmentId + ":" + expires;
            String expectedSignature = generateSignature(dataToSign);
            
            // 比较签名
            boolean isValid = expectedSignature.equals(signature);
            
            if (!isValid) {
                log.warn("签名验证失败 - 附件ID: {}, 期望签名: {}, 实际签名: {}", 
                        attachmentId, expectedSignature, signature);
            }
            
            return isValid;
            
        } catch (Exception e) {
            log.error("验证签名失败 - 附件ID: {}, 错误: {}", attachmentId, e.getMessage());
            return false;
        }
    }
    
    /**
     * 生成HMAC签名
     * @param data 要签名的数据
     * @return Base64编码的签名
     */
    private String generateSignature(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
            mac.init(secretKeySpec);
            byte[] signatureBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
            
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("生成签名失败 - 数据: {}, 错误: {}", data, e.getMessage());
            throw new RuntimeException("生成签名失败", e);
        }
    }
    
    /**
     * 生成文件访问的完整URL
     * @param baseUrl 基础URL
     * @param attachmentId 附件ID
     * @return 完整的签名URL
     */
    public String generateFullSignedUrl(String baseUrl, Long attachmentId) {
        String signedPath = generateSignedUrl(attachmentId);
        return baseUrl + signedPath;
    }
}
