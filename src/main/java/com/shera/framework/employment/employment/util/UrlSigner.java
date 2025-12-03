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
 * 优化版本：将附件/文件ID和过期时间加密到签名token中，提高安全性
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class UrlSigner {
    
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String TOKEN_SEPARATOR = "|";
    
    @Value("${app.url-signer.secret:default-secret-key-change-in-production}")
    private String secretKey;
    
    private final SystemConfigUtil systemConfigUtil;
    
    /**
     * 生成签名URL（附件ID版本）
     * @param attachmentId 附件ID
     * @return 签名URL
     */
    public String generateSignedUrl(Long attachmentId) {
        try {
            // 从系统配置获取过期时间
            long expirySeconds = systemConfigUtil.getUrlSignerExpirySeconds();
            long expiryTime = Instant.now().getEpochSecond() + expirySeconds;
            
            // 生成加密token（包含附件ID和过期时间）
            String token = generateEncryptedToken(attachmentId, expiryTime, "attachment");
            
            // 构建URL
            String url = "/api/files/signed/?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);
            
            log.debug("生成签名URL成功 - 附件ID: {}, 过期时间: {}", attachmentId, expiryTime);
            return url;
            
        } catch (Exception e) {
            log.error("生成签名URL失败 - 附件ID: {}, 错误: {}", attachmentId, e.getMessage());
            throw new RuntimeException("生成签名URL失败", e);
        }
    }
    
    /**
     * 生成签名URL（文件ID版本）
     * @param fileId 文件ID
     * @return 签名URL
     */
    public String generateSignedUrlByFileId(Long fileId) {
        try {
            // 从系统配置获取过期时间
            long expirySeconds = systemConfigUtil.getUrlSignerExpirySeconds();
            long expiryTime = Instant.now().getEpochSecond() + expirySeconds;
            
            // 生成加密token（包含文件ID和过期时间）
            String token = generateEncryptedToken(fileId, expiryTime, "file");
            
            // 构建URL
            String url = "/api/files/signed/?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);
            
            log.debug("生成签名URL成功 - 文件ID: {}, 过期时间: {}", fileId, expiryTime);
            return url;
            
        } catch (Exception e) {
            log.error("生成签名URL失败 - 文件ID: {}, 错误: {}", fileId, e.getMessage());
            throw new RuntimeException("生成签名URL失败", e);
        }
    }
    
    /**
     * 验证签名URL（通过token）
     * @param token 加密token
     * @return 验证结果对象，包含验证状态和解析出的信息
     */
    public SignatureVerificationResult verifySignature(String token) {
        try {
            // 解析token
            TokenData tokenData = parseToken(token);
            if (tokenData == null) {
                return new SignatureVerificationResult(false, null, null, null, "Token格式错误");
            }
            
            // 检查是否过期
            if (tokenData.getExpires() < Instant.now().getEpochSecond()) {
                log.warn("签名URL已过期 - 资源类型: {}, 资源ID: {}, 过期时间: {}", 
                        tokenData.getResourceType(), tokenData.getResourceId(), tokenData.getExpires());
                return new SignatureVerificationResult(false, tokenData.getResourceId(), 
                        tokenData.getResourceType(), tokenData.getExpires(), "签名URL已过期");
            }
            
            // 重新计算签名并验证
            String expectedToken = generateEncryptedToken(tokenData.getResourceId(), 
                    tokenData.getExpires(), tokenData.getResourceType());
            
            boolean isValid = token.equals(expectedToken);
            
            if (!isValid) {
                log.warn("签名验证失败 - 资源类型: {}, 资源ID: {}", 
                        tokenData.getResourceType(), tokenData.getResourceId());
                return new SignatureVerificationResult(false, tokenData.getResourceId(), 
                        tokenData.getResourceType(), tokenData.getExpires(), "签名验证失败");
            }
            
            return new SignatureVerificationResult(true, tokenData.getResourceId(), 
                    tokenData.getResourceType(), tokenData.getExpires(), "验证成功");
            
        } catch (Exception e) {
            log.error("验证签名失败 - token: {}, 错误: {}", token, e.getMessage());
            return new SignatureVerificationResult(false, null, null, null, "验证签名失败: " + e.getMessage());
        }
    }
    
    /**
     * 验证签名URL（兼容旧版本）
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
     * 生成加密token
     * @param resourceId 资源ID（附件ID或文件ID）
     * @param expires 过期时间戳
     * @param resourceType 资源类型（attachment或file）
     * @return 加密token
     */
    private String generateEncryptedToken(Long resourceId, Long expires, String resourceType) {
        try {
            // 构建原始数据：资源类型|资源ID|过期时间
            String rawData = resourceType + TOKEN_SEPARATOR + resourceId + TOKEN_SEPARATOR + expires;
            
            // 生成签名
            String signature = generateSignature(rawData);
            
            // 构建token：原始数据 + 签名
            String tokenData = rawData + TOKEN_SEPARATOR + signature;
            
            // Base64编码
            return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenData.getBytes(StandardCharsets.UTF_8));
            
        } catch (Exception e) {
            log.error("生成加密token失败 - 资源ID: {}, 资源类型: {}, 错误: {}", resourceId, resourceType, e.getMessage());
            throw new RuntimeException("生成加密token失败", e);
        }
    }
    
    /**
     * 解析token
     * @param token 加密token
     * @return token数据对象
     */
    private TokenData parseToken(String token) {
        try {
            // Base64解码
            byte[] decodedBytes = Base64.getUrlDecoder().decode(token);
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
            
            // 分割token
            String[] parts = decodedString.split("\\" + TOKEN_SEPARATOR);
            if (parts.length != 4) {
                log.warn("Token格式错误 - 期望4部分，实际: {}", parts.length);
                return null;
            }
            
            String resourceType = parts[0];
            Long resourceId = Long.parseLong(parts[1]);
            Long expires = Long.parseLong(parts[2]);
            String signature = parts[3];
            
            return new TokenData(resourceId, expires, resourceType, signature);
            
        } catch (Exception e) {
            log.error("解析token失败 - token: {}, 错误: {}", token, e.getMessage());
            return null;
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
     * 生成文件访问的完整URL（附件ID版本）
     * @param baseUrl 基础URL
     * @param attachmentId 附件ID
     * @return 完整的签名URL
     */
    public String generateFullSignedUrl(String baseUrl, Long attachmentId) {
        String signedPath = generateSignedUrl(attachmentId);
        return baseUrl + signedPath;
    }
    
    /**
     * 生成文件访问的完整URL（文件ID版本）
     * @param baseUrl 基础URL
     * @param fileId 文件ID
     * @return 完整的签名URL
     */
    public String generateFullSignedUrlByFileId(String baseUrl, Long fileId) {
        String signedPath = generateSignedUrlByFileId(fileId);
        return baseUrl + signedPath;
    }
    
    /**
     * Token数据内部类
     */
    private static class TokenData {
        private final Long resourceId;
        private final Long expires;
        private final String resourceType;
        private final String signature;
        
        public TokenData(Long resourceId, Long expires, String resourceType, String signature) {
            this.resourceId = resourceId;
            this.expires = expires;
            this.resourceType = resourceType;
            this.signature = signature;
        }
        
        public Long getResourceId() { return resourceId; }
        public Long getExpires() { return expires; }
        public String getResourceType() { return resourceType; }
        public String getSignature() { return signature; }
    }
    
    /**
     * 签名验证结果类
     */
    public static class SignatureVerificationResult {
        private final boolean valid;
        private final Long resourceId;
        private final String resourceType;
        private final Long expires;
        private final String message;
        
        public SignatureVerificationResult(boolean valid, Long resourceId, String resourceType, Long expires, String message) {
            this.valid = valid;
            this.resourceId = resourceId;
            this.resourceType = resourceType;
            this.expires = expires;
            this.message = message;
        }
        
        public boolean isValid() { return valid; }
        public Long getResourceId() { return resourceId; }
        public String getResourceType() { return resourceType; }
        public Long getExpires() { return expires; }
        public String getMessage() { return message; }
    }
}