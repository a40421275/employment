package com.shera.framework.employment.employment.modules.resume;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shera.framework.employment.employment.modules.resume.entity.Resume;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 测试日期格式序列化
 */
@SpringBootTest
public class DateFormatTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testDateFormatSerialization() throws Exception {
        // 创建一个简历对象
        Resume resume = new Resume();
        resume.setId(1L);
        resume.setUserId(1L);
        resume.setTitle("测试简历");
        resume.setResumeType(1); // 附件简历
        
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        resume.setCreateTime(now);
        resume.setUpdateTime(now);

        // 序列化为JSON
        String json = objectMapper.writeValueAsString(resume);
        
        // 验证日期字段是否以字符串格式序列化
        assertTrue(json.contains("\"createTime\""), "应该包含createTime字段");
        assertTrue(json.contains("\"updateTime\""), "应该包含updateTime字段");
        
        // 验证日期格式是否为字符串格式（不是数组格式）
        assertFalse(json.contains("[2025,11,25"), "日期不应该以数组格式序列化");
        assertTrue(json.matches(".*\"createTime\"\\s*:\\s*\"\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\".*"), 
                   "createTime应该以字符串格式序列化");
        assertTrue(json.matches(".*\"updateTime\"\\s*:\\s*\"\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\".*"), 
                   "updateTime应该以字符串格式序列化");
        
        System.out.println("序列化后的JSON: " + json);
    }
}
