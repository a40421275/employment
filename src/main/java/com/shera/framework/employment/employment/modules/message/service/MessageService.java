package com.shera.framework.employment.employment.modules.message.service;

import com.shera.framework.employment.employment.modules.message.dto.MessageCreateDTO;
import com.shera.framework.employment.employment.modules.message.dto.MessageDTO;
import com.shera.framework.employment.employment.modules.message.dto.MessageQueryDTO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 消息服务接口
 */
public interface MessageService {

    /**
     * 发送消息
     */
    void sendMessage(MessageCreateDTO messageCreateDTO);

    /**
     * 获取消息详情
     */
    MessageDTO getMessage(Long id);

    /**
     * 分页查询消息
     */
    Page<MessageDTO> listMessages(MessageQueryDTO queryDTO);

    /**
     * 获取用户消息列表
     */
    List<MessageDTO> listUserMessages(Long userId);

    /**
     * 获取用户最新消息
     */
    List<MessageDTO> listLatestMessages(Long userId, Integer limit);

    /**
     * 获取用户重要消息
     */
    List<MessageDTO> listImportantMessages(Long userId, Integer limit);

    /**
     * 标记消息为已读
     */
    void markAsRead(Long id);

    /**
     * 批量标记为已读
     */
    void batchMarkAsRead(List<Long> ids);

    /**
     * 标记用户所有消息为已读
     */
    void markAllAsRead(Long userId);

    /**
     * 删除消息
     */
    void deleteMessage(Long id);

    /**
     * 批量删除消息
     */
    void batchDeleteMessages(List<Long> ids);

    /**
     * 清空用户消息
     */
    void clearUserMessages(Long userId);

    /**
     * 获取未读消息数量
     */
    Long getUnreadCount(Long userId);

    /**
     * 获取消息统计
     */
    Object getMessageStats(Long userId);
}