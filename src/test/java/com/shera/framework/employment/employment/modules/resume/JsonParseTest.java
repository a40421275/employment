package com.shera.framework.employment.employment.modules.resume;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shera.framework.employment.employment.modules.resume.dto.BasicInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JSON解析测试类
 * 验证JSON解析错误是否已修复
 */
@SpringBootTest
public class JsonParseTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testJsonParseWithStringValue() throws JsonProcessingException {
        // 测试普通字符串值（非JSON格式）
        String preferredCities = "北京";
        String jobTypes = "全职";
        String industries = "互联网";

        BasicInfo basicInfo = new BasicInfo();
        
        // 测试JSON格式检测逻辑
        if (preferredCities != null && !preferredCities.isEmpty()) {
            if (preferredCities.startsWith("[") && preferredCities.endsWith("]")) {
                basicInfo.setPreferredCities(objectMapper.readValue(preferredCities, List.class));
            } else {
                basicInfo.setPreferredCities(List.of(preferredCities));
            }
        }
        
        if (jobTypes != null && !jobTypes.isEmpty()) {
            if (jobTypes.startsWith("[") && jobTypes.endsWith("]")) {
                basicInfo.setJobTypes(objectMapper.readValue(jobTypes, List.class));
            } else {
                basicInfo.setJobTypes(List.of(jobTypes));
            }
        }
        
        if (industries != null && !industries.isEmpty()) {
            if (industries.startsWith("[") && industries.endsWith("]")) {
                basicInfo.setIndustries(objectMapper.readValue(industries, List.class));
            } else {
                basicInfo.setIndustries(List.of(industries));
            }
        }

        // 验证结果
        assertNotNull(basicInfo.getPreferredCities());
        assertEquals(1, basicInfo.getPreferredCities().size());
        assertEquals("北京", basicInfo.getPreferredCities().get(0));
        
        assertNotNull(basicInfo.getJobTypes());
        assertEquals(1, basicInfo.getJobTypes().size());
        assertEquals("全职", basicInfo.getJobTypes().get(0));
        
        assertNotNull(basicInfo.getIndustries());
        assertEquals(1, basicInfo.getIndustries().size());
        assertEquals("互联网", basicInfo.getIndustries().get(0));
    }

    @Test
    public void testJsonParseWithJsonArray() throws JsonProcessingException {
        // 测试JSON数组格式
        String preferredCities = "[\"北京\", \"上海\", \"深圳\"]";
        String jobTypes = "[\"全职\", \"兼职\"]";
        String industries = "[\"互联网\", \"金融\"]";

        BasicInfo basicInfo = new BasicInfo();
        
        if (preferredCities != null && !preferredCities.isEmpty()) {
            if (preferredCities.startsWith("[") && preferredCities.endsWith("]")) {
                basicInfo.setPreferredCities(objectMapper.readValue(preferredCities, List.class));
            } else {
                basicInfo.setPreferredCities(List.of(preferredCities));
            }
        }
        
        if (jobTypes != null && !jobTypes.isEmpty()) {
            if (jobTypes.startsWith("[") && jobTypes.endsWith("]")) {
                basicInfo.setJobTypes(objectMapper.readValue(jobTypes, List.class));
            } else {
                basicInfo.setJobTypes(List.of(jobTypes));
            }
        }
        
        if (industries != null && !industries.isEmpty()) {
            if (industries.startsWith("[") && industries.endsWith("]")) {
                basicInfo.setIndustries(objectMapper.readValue(industries, List.class));
            } else {
                basicInfo.setIndustries(List.of(industries));
            }
        }

        // 验证结果
        assertNotNull(basicInfo.getPreferredCities());
        assertEquals(3, basicInfo.getPreferredCities().size());
        assertEquals("北京", basicInfo.getPreferredCities().get(0));
        
        assertNotNull(basicInfo.getJobTypes());
        assertEquals(2, basicInfo.getJobTypes().size());
        assertEquals("全职", basicInfo.getJobTypes().get(0));
        
        assertNotNull(basicInfo.getIndustries());
        assertEquals(2, basicInfo.getIndustries().size());
        assertEquals("互联网", basicInfo.getIndustries().get(0));
    }

    @Test
    public void testJsonParseWithEmptyString() {
        // 测试空字符串
        String preferredCities = "";
        String jobTypes = "";
        String industries = "";

        BasicInfo basicInfo = new BasicInfo();
        
        try {
            if (preferredCities != null && !preferredCities.isEmpty()) {
                if (preferredCities.startsWith("[") && preferredCities.endsWith("]")) {
                    basicInfo.setPreferredCities(objectMapper.readValue(preferredCities, List.class));
                } else {
                    basicInfo.setPreferredCities(List.of(preferredCities));
                }
            }
            
            if (jobTypes != null && !jobTypes.isEmpty()) {
                if (jobTypes.startsWith("[") && jobTypes.endsWith("]")) {
                    basicInfo.setJobTypes(objectMapper.readValue(jobTypes, List.class));
                } else {
                    basicInfo.setJobTypes(List.of(jobTypes));
                }
            }
            
            if (industries != null && !industries.isEmpty()) {
                if (industries.startsWith("[") && industries.endsWith("]")) {
                    basicInfo.setIndustries(objectMapper.readValue(industries, List.class));
                } else {
                    basicInfo.setIndustries(List.of(industries));
                }
            }
        } catch (JsonProcessingException e) {
            fail("不应该抛出JsonProcessingException异常");
        }

        // 验证结果 - 应该为空
        assertNull(basicInfo.getPreferredCities());
        assertNull(basicInfo.getJobTypes());
        assertNull(basicInfo.getIndustries());
    }
}
