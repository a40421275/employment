package com.shera.framework.employment.employment.modules.message;

import com.shera.framework.employment.employment.modules.message.dto.MessageCreateDTO;
import com.shera.framework.employment.employment.modules.message.dto.MessageQueryDTO;
import com.shera.framework.employment.employment.modules.message.dto.MessageDTO;
import com.shera.framework.employment.employment.modules.message.entity.Message;
import com.shera.framework.employment.employment.modules.message.repository.MessageRepository;
import com.shera.framework.employment.employment.modules.message.service.impl.MessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 消息服务单元测试
 */
@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    private Message testMessage;

    @BeforeEach
    void setUp() {
        testMessage = new Message();
        testMessage.setId(1L);
        testMessage.setTitle("测试消息");
        testMessage.setContent("这是一个测试消息");
        testMessage.setSenderId(1L);
        testMessage.setReceiverId(2L);
        testMessage.setType(Message.Type.SYSTEM.getCode());
        testMessage.setPriority(Message.Priority.MEDIUM.getCode());
        testMessage.setStatus(Message.Status.UNREAD.getCode());
    }

    @Test
    void testSendMessage() {
        // 准备测试数据
        MessageCreateDTO messageCreateDTO = new MessageCreateDTO();
        messageCreateDTO.setTitle("测试消息");
        messageCreateDTO.setContent("这是一个测试消息");
        messageCreateDTO.setSenderId(1L);
        messageCreateDTO.setReceiverIds(Arrays.asList(2L, 3L));
        messageCreateDTO.setType(Message.Type.SYSTEM.getCode());
        messageCreateDTO.setPriority(Message.Priority.MEDIUM.getCode());

        // 模拟Repository行为
        when(messageRepository.save(any(Message.class))).thenReturn(testMessage);

        // 执行测试
        messageService.sendMessage(messageCreateDTO);

        // 验证
        verify(messageRepository, times(2)).save(any(Message.class));
    }

    @Test
    void testListMessages() {
        // 准备测试数据
        MessageQueryDTO queryDTO = new MessageQueryDTO();
        queryDTO.setUserId(2L);
        queryDTO.setPage(0);
        queryDTO.setSize(20);

        List<Message> messages = Arrays.asList(testMessage);
        Page<Message> messagePage = new PageImpl<>(messages, PageRequest.of(0, 20), 1);

        // 模拟Repository行为
        when(messageRepository.findByReceiverId(anyLong(), any(Pageable.class)))
                .thenReturn(messagePage);

        // 执行测试
        Page<MessageDTO> result = messageService.listMessages(queryDTO);

        // 验证
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(messageRepository).findByReceiverId(2L, PageRequest.of(0, 20));
    }

    @Test
    void testGetUnreadCount() {
        // 模拟Repository行为
        when(messageRepository.countUnreadByReceiverId(2L))
                .thenReturn(5L);

        // 执行测试
        Long count = messageService.getUnreadCount(2L);

        // 验证
        assertEquals(5L, count);
        verify(messageRepository).countUnreadByReceiverId(2L);
    }

    @Test
    void testMarkAsRead() {
        // 模拟Repository行为
        when(messageRepository.findById(1L)).thenReturn(Optional.of(testMessage));
        when(messageRepository.save(any(Message.class))).thenReturn(testMessage);

        // 执行测试
        messageService.markAsRead(1L);

        // 验证
        assertEquals(Message.Status.READ.getCode(), testMessage.getStatus());
        assertNotNull(testMessage.getReadTime());
        verify(messageRepository).save(testMessage);
    }

    @Test
    void testDeleteMessage() {
        // 模拟Repository行为
        when(messageRepository.findById(1L)).thenReturn(Optional.of(testMessage));
        when(messageRepository.save(any(Message.class))).thenReturn(testMessage);

        // 执行测试
        messageService.deleteMessage(1L);

        // 验证
        assertEquals(Message.Status.DELETED.getCode(), testMessage.getStatus());
        verify(messageRepository).save(testMessage);
    }

    @Test
    void testGetMessageStats() {
        // 模拟Repository行为
        when(messageRepository.countByReceiverId(2L)).thenReturn(10L);
        when(messageRepository.countUnreadByReceiverId(2L)).thenReturn(3L);
        when(messageRepository.countByReceiverIdAndStatus(2L, Message.Status.READ.getCode())).thenReturn(7L);

        // 执行测试
        Object stats = messageService.getMessageStats(2L);

        // 验证
        assertNotNull(stats);
        verify(messageRepository).countByReceiverId(2L);
        verify(messageRepository).countUnreadByReceiverId(2L);
        verify(messageRepository).countByReceiverIdAndStatus(2L, Message.Status.READ.getCode());
    }
}