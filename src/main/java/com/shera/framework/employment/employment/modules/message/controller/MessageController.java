package com.shera.framework.employment.employment.modules.message.controller;

import com.shera.framework.employment.employment.modules.message.dto.EmailVerificationRequest;
import com.shera.framework.employment.employment.modules.message.dto.EmailVerificationResponse;
import com.shera.framework.employment.employment.modules.message.dto.MessageCreateDTO;
import com.shera.framework.employment.employment.modules.message.dto.MessageDTO;
import com.shera.framework.employment.employment.modules.message.dto.MessageQueryDTO;
import com.shera.framework.employment.employment.modules.message.service.EmailVerificationService;
import com.shera.framework.employment.employment.modules.message.service.MessageService;
import com.shera.framework.employment.employment.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息控制器
 */
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    /**
     * 发送消息
     */
    @PostMapping
    public Object sendMessage(@RequestBody MessageCreateDTO messageCreateDTO) {
        messageService.sendMessage(messageCreateDTO);
        return ResponseUtil.success("消息发送成功");
    }

    /**
     * 获取消息详情
     */
    @GetMapping("/{id}")
    public Object getMessage(@PathVariable Long id) {
        MessageDTO message = messageService.getMessage(id);
        return ResponseUtil.success(message);
    }

    /**
     * 分页查询消息
     */
    @GetMapping
    public Object listMessages(MessageQueryDTO queryDTO) {
        Page<MessageDTO> messages = messageService.listMessages(queryDTO);
        return ResponseUtil.success(messages);
    }

    /**
     * 获取用户消息列表
     */
    @GetMapping("/user/{userId}")
    public Object listUserMessages(@PathVariable Long userId) {
        List<MessageDTO> messages = messageService.listUserMessages(userId);
        return ResponseUtil.success(messages);
    }

    /**
     * 获取用户最新消息
     */
    @GetMapping("/user/{userId}/latest")
    public Object listLatestMessages(@PathVariable Long userId,
                                    @RequestParam(defaultValue = "10") Integer limit) {
        List<MessageDTO> messages = messageService.listLatestMessages(userId, limit);
        return ResponseUtil.success(messages);
    }

    /**
     * 获取用户重要消息
     */
    @GetMapping("/user/{userId}/important")
    public Object listImportantMessages(@PathVariable Long userId,
                                       @RequestParam(defaultValue = "10") Integer limit) {
        List<MessageDTO> messages = messageService.listImportantMessages(userId, limit);
        return ResponseUtil.success(messages);
    }

    /**
     * 标记消息为已读
     */
    @PutMapping("/{id}/read")
    public Object markAsRead(@PathVariable Long id) {
        messageService.markAsRead(id);
        return ResponseUtil.success("消息已标记为已读");
    }

    /**
     * 批量标记为已读
     */
    @PutMapping("/batch-read")
    public Object batchMarkAsRead(@RequestBody List<Long> ids) {
        messageService.batchMarkAsRead(ids);
        return ResponseUtil.success("消息已批量标记为已读");
    }

    /**
     * 标记用户所有消息为已读
     */
    @PutMapping("/user/{userId}/read-all")
    public Object markAllAsRead(@PathVariable Long userId) {
        messageService.markAllAsRead(userId);
        return ResponseUtil.success("所有消息已标记为已读");
    }

    /**
     * 删除消息
     */
    @DeleteMapping("/{id}")
    public Object deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseUtil.success("消息删除成功");
    }

    /**
     * 批量删除消息
     */
    @DeleteMapping("/batch")
    public Object batchDeleteMessages(@RequestBody List<Long> ids) {
        messageService.batchDeleteMessages(ids);
        return ResponseUtil.success("消息批量删除成功");
    }

    /**
     * 清空用户消息
     */
    @DeleteMapping("/user/{userId}")
    public Object clearUserMessages(@PathVariable Long userId) {
        messageService.clearUserMessages(userId);
        return ResponseUtil.success("用户消息清空成功");
    }

    /**
     * 获取未读消息数量
     */
    @GetMapping("/user/{userId}/unread-count")
    public Object getUnreadCount(@PathVariable Long userId) {
        Long count = messageService.getUnreadCount(userId);
        return ResponseUtil.success(count);
    }

    /**
     * 获取消息统计
     */
    @GetMapping("/user/{userId}/stats")
    public Object getMessageStats(@PathVariable Long userId) {
        Object stats = messageService.getMessageStats(userId);
        return ResponseUtil.success(stats);
    }

    /**
     * 发送邮件验证码
     */
    @PostMapping("/email-verification")
    public Object sendEmailVerification(@RequestBody EmailVerificationRequest request) {
        EmailVerificationResponse response = emailVerificationService.sendVerificationCode(request);
        return ResponseUtil.success(response);
    }

    /**
     * 验证邮件验证码
     */
    @PostMapping("/email-verification/verify")
    public Object verifyEmailCode(@RequestParam String email,
                                 @RequestParam String code,
                                 @RequestParam String codeType) {
        boolean isValid = emailVerificationService.verifyCode(email, code, codeType);
        if (isValid) {
            return ResponseUtil.success("验证码验证成功");
        } else {
            return ResponseUtil.error("验证码验证失败");
        }
    }

    /**
     * 检查验证码是否有效
     */
    @GetMapping("/email-verification/valid")
    public Object checkCodeValid(@RequestParam String email,
                                @RequestParam String codeType) {
        boolean isValid = emailVerificationService.isCodeValid(email, codeType);
        return ResponseUtil.success(isValid);
    }

    /**
     * 获取验证码剩余时间
     */
    @GetMapping("/email-verification/remaining-time")
    public Object getRemainingTime(@RequestParam String email,
                                  @RequestParam String codeType) {
        Long remainingTime = emailVerificationService.getRemainingTime(email, codeType);
        return ResponseUtil.success(remainingTime);
    }

    /**
     * 清除验证码
     */
    @DeleteMapping("/email-verification")
    public Object clearVerificationCode(@RequestParam String email,
                                       @RequestParam String codeType) {
        emailVerificationService.clearCode(email, codeType);
        return ResponseUtil.success("验证码已清除");
    }
}
