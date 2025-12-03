package com.shera.framework.employment.employment.modules.message.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shera.framework.employment.employment.modules.message.dto.EmailSendRequest;
import com.shera.framework.employment.employment.modules.message.dto.EmailSendResult;
import com.shera.framework.employment.employment.modules.message.dto.MessageCreateDTO;
import com.shera.framework.employment.employment.modules.message.dto.MessageDTO;
import com.shera.framework.employment.employment.modules.message.dto.MessageQueryDTO;
import com.shera.framework.employment.employment.modules.message.entity.Message;
import com.shera.framework.employment.employment.modules.message.entity.MessageTemplate;
import com.shera.framework.employment.employment.modules.message.repository.MessageRepository;
import com.shera.framework.employment.employment.modules.message.repository.MessageTemplateRepository;
import com.shera.framework.employment.employment.modules.message.service.MessageSenderService;
import com.shera.framework.employment.employment.modules.message.service.MessageService;
import com.shera.framework.employment.employment.modules.message.service.MessageTemplateService;
import com.shera.framework.employment.employment.modules.user.entity.User;
import com.shera.framework.employment.employment.modules.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 消息服务实现类
 */
@Service
@Transactional
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageSenderService messageSenderService;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private MessageTemplateRepository messageTemplateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AsyncMessageSendService asyncMessageSendService;

    @Override
    public void sendMessage(MessageCreateDTO messageCreateDTO) {
        // 批量发送给多个接收者
        for (Long receiverId : messageCreateDTO.getReceiverIds()) {
            Message message = new Message();
            message.setTitle(messageCreateDTO.getTitle());
            message.setContent(messageCreateDTO.getContent());
            message.setSenderId(messageCreateDTO.getSenderId());
            message.setReceiverId(receiverId);
            message.setType(messageCreateDTO.getType());
            message.setPriority(messageCreateDTO.getPriority());
            message.setExpireTime(messageCreateDTO.getExpireTime());
            message.setBusinessId(messageCreateDTO.getBusinessId());
            message.setBusinessType(messageCreateDTO.getBusinessType());
        message.setTemplateCode(messageCreateDTO.getTemplateCode());
            message.setChannelType(messageCreateDTO.getChannelType());
            message.setAttachments(messageCreateDTO.getAttachments());
            
            // 保存消息到数据库
            Message savedMessage = messageRepository.save(message);
            
            // 异步触发消息发送
            asyncMessageSendService.asyncSendMessage(savedMessage);
        }
    }

    @Override
    public MessageDTO getMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("消息不存在"));
        return convertToDTO(message);
    }

    @Override
    public Page<MessageDTO> listMessages(MessageQueryDTO queryDTO) {
        Pageable pageable = Pageable.ofSize(queryDTO.getSize()).withPage(queryDTO.getPage());
        
        Page<Message> messages;
        if (queryDTO.getUserId() != null) {
            if (queryDTO.getType() != null && queryDTO.getStatus() != null) {
                messages = messageRepository.findByReceiverIdAndTypeAndStatus(
                    queryDTO.getUserId(), queryDTO.getType(), queryDTO.getStatus(), pageable);
            } else if (queryDTO.getType() != null) {
                messages = messageRepository.findByReceiverIdAndType(queryDTO.getUserId(), queryDTO.getType(), pageable);
            } else if (queryDTO.getStatus() != null) {
                messages = messageRepository.findByReceiverIdAndStatus(queryDTO.getUserId(), queryDTO.getStatus(), pageable);
            } else {
                messages = messageRepository.findByReceiverId(queryDTO.getUserId(), pageable);
            }
        } else {
            messages = messageRepository.findAll(pageable);
        }
        
        return messages.map(this::convertToDTO);
    }

    @Override
    public List<MessageDTO> listUserMessages(Long userId) {
        List<Message> messages = messageRepository.findByReceiverId(userId, Pageable.unpaged()).getContent();
        return messages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<MessageDTO> listLatestMessages(Long userId, Integer limit) {
        List<Message> messages = messageRepository.findTop10ByReceiverIdOrderByCreateTimeDesc(userId);
        return messages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<MessageDTO> listImportantMessages(Long userId, Integer limit) {
        List<Message> messages = messageRepository.findTop10ByReceiverIdAndPriorityOrderByCreateTimeDesc(userId, Message.Priority.HIGH.getCode());
        return messages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void markAsRead(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("消息不存在"));
        message.setStatus(Message.Status.READ.getCode());
        message.setReadTime(LocalDateTime.now());
        messageRepository.save(message);
    }

    @Override
    public void batchMarkAsRead(List<Long> ids) {
        messageRepository.updateStatusByIds(ids, Message.Status.READ.getCode(), LocalDateTime.now());
    }

    @Override
    public void markAllAsRead(Long userId) {
        messageRepository.updateStatusByReceiverId(userId, Message.Status.READ.getCode(), LocalDateTime.now());
    }

    @Override
    public void deleteMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("消息不存在"));
        message.setStatus(Message.Status.DELETED.getCode());
        messageRepository.save(message);
    }

    @Override
    public void batchDeleteMessages(List<Long> ids) {
        messageRepository.updateStatusByIds(ids, Message.Status.DELETED.getCode(), null);
    }

    @Override
    public void clearUserMessages(Long userId) {
        messageRepository.deleteByReceiverId(userId);
    }

    @Override
    public Long getUnreadCount(Long userId) {
        return messageRepository.countUnreadByReceiverId(userId);
    }

    @Override
    public Object getMessageStats(Long userId) {
        Long totalCount = messageRepository.countByReceiverId(userId);
        Long unreadCount = messageRepository.countUnreadByReceiverId(userId);
        Long readCount = messageRepository.countByReceiverIdAndStatus(userId, Message.Status.READ.getCode());
        
        return new Object() {
            public Long total = totalCount;
            public Long unread = unreadCount;
            public Long read = readCount;
        };
    }

    /**
     * 将Message实体转换为MessageDTO
     */
    private MessageDTO convertToDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setTitle(message.getTitle());
        dto.setContent(message.getContent());
        dto.setSenderId(message.getSenderId());
        dto.setReceiverId(message.getReceiverId());
        dto.setType(message.getType());
        dto.setPriority(message.getPriority());
        dto.setStatus(message.getStatus());
        dto.setCreateTime(message.getCreateTime());
        dto.setReadTime(message.getReadTime());
        dto.setExpireTime(message.getExpireTime());
        dto.setBusinessId(message.getBusinessId());
        dto.setBusinessType(message.getBusinessType());
        dto.setTemplateId(message.getTemplateId());
        dto.setChannelType(message.getChannelType());
        dto.setSendResult(message.getSendResult());
        dto.setAttachments(message.getAttachments());
        
        // 设置枚举名称
        dto.setTypeName(Message.Type.fromCode(message.getType()).getDesc());
        dto.setPriorityName(Message.Priority.fromCode(message.getPriority()).getDesc());
        dto.setStatusName(Message.Status.fromCode(message.getStatus()).getDesc());
        
        return dto;
    }
}
