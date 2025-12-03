package com.shera.framework.employment.employment.modules.message;

import com.shera.framework.employment.employment.modules.message.controller.MessageController;
import com.shera.framework.employment.employment.modules.message.dto.MessageCreateDTO;
import com.shera.framework.employment.employment.modules.message.dto.MessageQueryDTO;
import com.shera.framework.employment.employment.modules.message.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 消息控制器测试
 */
@WebMvcTest(MessageController.class)
@ActiveProfiles("test")
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @Test
    public void testSendMessage() throws Exception {
        MessageCreateDTO messageCreateDTO = new MessageCreateDTO();
        messageCreateDTO.setTitle("测试消息");
        messageCreateDTO.setContent("这是一个测试消息");
        messageCreateDTO.setSenderId(1L);
        messageCreateDTO.setReceiverIds(Arrays.asList(2L));
        messageCreateDTO.setType(1);

        mockMvc.perform(post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"测试消息\",\"content\":\"这是一个测试消息\",\"senderId\":1,\"receiverIds\":[2],\"type\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("消息发送成功"));
    }

    @Test
    public void testListMessages() throws Exception {
        MessageQueryDTO queryDTO = new MessageQueryDTO();
        queryDTO.setUserId(1L);
        queryDTO.setPage(0);
        queryDTO.setSize(20);

        when(messageService.listMessages(any(MessageQueryDTO.class)))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/messages")
                .param("userId", "1")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testGetUnreadCount() throws Exception {
        when(messageService.getUnreadCount(1L)).thenReturn(5L);

        mockMvc.perform(get("/api/messages/user/1/unread-count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(5));
    }

    @Test
    public void testMarkAsRead() throws Exception {
        mockMvc.perform(put("/api/messages/1/read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("消息已标记为已读"));
    }

    @Test
    public void testDeleteMessage() throws Exception {
        mockMvc.perform(delete("/api/messages/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("消息删除成功"));
    }
}
