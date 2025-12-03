package com.shera.framework.employment.employment.modules.message.controller;

import com.shera.framework.employment.employment.modules.message.dto.EmailVerificationRequest;
import com.shera.framework.employment.employment.modules.message.dto.EmailVerificationResponse;
import com.shera.framework.employment.employment.modules.message.service.EmailVerificationService;
import com.shera.framework.employment.employment.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 验证码控制器（无需鉴权）
 * 专门用于注册、登录等无需认证的场景
 */
@RestController
@RequestMapping("/api/verification")
@Tag(name = "验证码管理", description = "验证码发送和验证接口")
@Slf4j
public class VerificationCodeController {

    @Autowired
    private EmailVerificationService emailVerificationService;

    /**
     * 发送注册验证码
     */
    @PostMapping("/register")
    @Operation(summary = "发送注册验证码", description = "用于用户注册场景，无需鉴权")
    public Object sendRegisterCode(@Valid @RequestBody EmailVerificationRequest request) {
        try {
            // 设置默认值
            if (request.getCodeType() == null) {
                request.setCodeType("reset_password");
            }
            if (request.getExpireMinutes() == null) {
                request.setExpireMinutes(10); // 注册验证码默认10分钟
            }
            if (request.getCodeLength() == null) {
                request.setCodeLength(6); // 默认6位验证码
            }
            if (request.getBusinessScene() == null) {
                request.setBusinessScene("用户注册");
            }

            EmailVerificationResponse response = emailVerificationService.sendVerificationCode(request);
            
            if (response.isSuccess()) {
                log.info("注册验证码发送成功: 邮箱={}, 类型={}", request.getEmail(), request.getCodeType());
                return ResponseUtil.success("验证码发送成功", response);
            } else {
                log.warn("注册验证码发送失败: 邮箱={}, 类型={}, 错误={}", 
                        request.getEmail(), request.getCodeType(), response.getMessage());
                return ResponseUtil.error(response.getMessage());
            }
        } catch (Exception e) {
            log.error("注册验证码发送异常: 邮箱={}, 错误={}", request.getEmail(), e.getMessage(), e);
            return ResponseUtil.error("系统异常，请稍后重试");
        }
    }

    

    /**
     * 验证注册验证码
     */
    @PostMapping("/register/verify")
    @Operation(summary = "验证注册验证码", description = "验证注册场景的验证码，无需鉴权")
    public Object verifyRegisterCode(@RequestParam String email,
                                    @RequestParam String code) {
        return verifyCode(email, code, "register");
    }


    /**
     * 通用验证码验证方法
     */
    private Object verifyCode(String email, String code, String codeType) {
        try {
            boolean isValid = emailVerificationService.verifyCode(email, code, codeType);
            
            if (isValid) {
                log.info("验证码验证成功: 邮箱={}, 类型={}", email, codeType);
                return ResponseUtil.success("验证码验证成功");
            } else {
                log.warn("验证码验证失败: 邮箱={}, 类型={}", email, codeType);
                return ResponseUtil.error("验证码验证失败");
            }
        } catch (Exception e) {
            log.error("验证码验证异常: 邮箱={}, 类型={}, 错误={}", email, codeType, e.getMessage(), e);
            return ResponseUtil.error("系统异常，请稍后重试");
        }
    }

    /**
     * 检查验证码是否有效
     */
    @GetMapping("/valid")
    @Operation(summary = "检查验证码有效性", description = "检查验证码是否有效，无需鉴权")
    public Object checkCodeValid(@RequestParam String email,
                                @RequestParam String codeType) {
        try {
            boolean isValid = emailVerificationService.isCodeValid(email, codeType);
            return ResponseUtil.success(isValid);
        } catch (Exception e) {
            log.error("检查验证码有效性异常: 邮箱={}, 类型={}, 错误={}", email, codeType, e.getMessage(), e);
            return ResponseUtil.error("系统异常，请稍后重试");
        }
    }

    /**
     * 获取验证码剩余时间
     */
    @GetMapping("/remaining-time")
    @Operation(summary = "获取验证码剩余时间", description = "获取验证码剩余有效时间，无需鉴权")
    public Object getRemainingTime(@RequestParam String email,
                                  @RequestParam String codeType) {
        try {
            Long remainingTime = emailVerificationService.getRemainingTime(email, codeType);
            return ResponseUtil.success(remainingTime);
        } catch (Exception e) {
            log.error("获取验证码剩余时间异常: 邮箱={}, 类型={}, 错误={}", email, codeType, e.getMessage(), e);
            return ResponseUtil.error("系统异常，请稍后重试");
        }
    }

    /**
     * 清除验证码
     */
    @DeleteMapping
    @Operation(summary = "清除验证码", description = "清除指定邮箱和类型的验证码，无需鉴权")
    public Object clearVerificationCode(@RequestParam String email,
                                       @RequestParam String codeType) {
        try {
            emailVerificationService.clearCode(email, codeType);
            log.info("验证码已清除: 邮箱={}, 类型={}", email, codeType);
            return ResponseUtil.success("验证码已清除");
        } catch (Exception e) {
            log.error("清除验证码异常: 邮箱={}, 类型={}, 错误={}", email, codeType, e.getMessage(), e);
            return ResponseUtil.error("系统异常，请稍后重试");
        }
    }
}
