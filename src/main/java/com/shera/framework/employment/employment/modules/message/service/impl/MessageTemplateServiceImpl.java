package com.shera.framework.employment.employment.modules.message.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shera.framework.employment.employment.modules.message.dto.MessageTemplateDTO;
import com.shera.framework.employment.employment.modules.message.entity.MessageTemplate;
import com.shera.framework.employment.employment.modules.message.repository.MessageTemplateRepository;
import com.shera.framework.employment.employment.modules.message.service.MessageTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 消息模板服务实现
 */
@Service
@Slf4j
public class MessageTemplateServiceImpl implements MessageTemplateService {

    @Autowired
    private MessageTemplateRepository messageTemplateRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{(.*?)\\}\\}");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional
    public MessageTemplateDTO createTemplate(MessageTemplateDTO templateDTO) {
        // 验证模板名称和渠道类型是否唯一
        MessageTemplate existingTemplate = messageTemplateRepository.findByNameAndChannelType(
                templateDTO.getName(), templateDTO.getChannelType());
        if (existingTemplate != null) {
            throw new IllegalArgumentException("相同名称和渠道类型的模板已存在");
        }

        // 创建新模板
        MessageTemplate template = new MessageTemplate();
        BeanUtils.copyProperties(templateDTO, template);
        
        // 设置默认状态
        if (templateDTO.getStatus() == null) {
            template.setStatus(MessageTemplate.Status.ENABLED.getCode());
        }

        // 保存模板
        MessageTemplate savedTemplate = messageTemplateRepository.save(template);
        return convertToDTO(savedTemplate);
    }

    @Override
    @Transactional
    public MessageTemplateDTO updateTemplate(Long id, MessageTemplateDTO templateDTO) {
        // 查找现有模板
        MessageTemplate existingTemplate = messageTemplateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("模板不存在"));

        // 验证名称和渠道类型是否唯一（排除当前模板）
        MessageTemplate duplicateTemplate = messageTemplateRepository.findByNameAndChannelType(
                templateDTO.getName(), templateDTO.getChannelType());
        if (duplicateTemplate != null && !duplicateTemplate.getId().equals(id)) {
            throw new IllegalArgumentException("相同名称和渠道类型的模板已存在");
        }

        // 更新模板属性
        BeanUtils.copyProperties(templateDTO, existingTemplate, "id", "createTime", "updateTime");
        
        // 保存更新
        MessageTemplate updatedTemplate = messageTemplateRepository.save(existingTemplate);
        return convertToDTO(updatedTemplate);
    }

    @Override
    public MessageTemplateDTO getTemplate(Long id) {
        MessageTemplate template = messageTemplateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("模板不存在"));
        return convertToDTO(template);
    }

    @Override
    @Transactional
    public void deleteTemplate(Long id) {
        MessageTemplate template = messageTemplateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("模板不存在"));
        messageTemplateRepository.delete(template);
    }

    @Override
    public Page<MessageTemplateDTO> listTemplates(String channelType, Integer status, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        
        Page<MessageTemplate> templates;
        if (channelType != null && status != null) {
            templates = messageTemplateRepository.findByChannelTypeAndStatus(channelType, status, pageable);
        } else if (channelType != null) {
            templates = messageTemplateRepository.findByChannelType(channelType, pageable);
        } else if (status != null) {
            templates = messageTemplateRepository.findByStatus(status, pageable);
        } else {
            templates = messageTemplateRepository.findAll(pageable);
        }
        
        return templates.map(this::convertToDTO);
    }

    @Override
    public List<MessageTemplateDTO> listTemplatesByType(String channelType) {
        List<MessageTemplate> templates = messageTemplateRepository.findByChannelType(channelType);
        return templates.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageTemplateDTO> listTemplatesByTypeAndStatus(String channelType, Integer status) {
        List<MessageTemplate> templates = messageTemplateRepository.findByChannelTypeAndStatus(channelType, status);
        return templates.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MessageTemplateDTO getTemplateByNameAndType(String name, String channelType) {
        MessageTemplate template = messageTemplateRepository.findByNameAndChannelType(name, channelType);
        if (template == null) {
            throw new IllegalArgumentException("模板不存在");
        }
        return convertToDTO(template);
    }

    @Override
    @Transactional
    public void enableTemplate(Long id) {
        MessageTemplate template = messageTemplateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("模板不存在"));
        template.setStatus(MessageTemplate.Status.ENABLED.getCode());
        messageTemplateRepository.save(template);
    }

    @Override
    @Transactional
    public void disableTemplate(Long id) {
        MessageTemplate template = messageTemplateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("模板不存在"));
        template.setStatus(MessageTemplate.Status.DISABLED.getCode());
        messageTemplateRepository.save(template);
    }

    @Override
    public String renderTemplate(String templateContent, String variables) {
        if (variables == null || variables.trim().isEmpty()) {
            return templateContent;
        }

        try {
            // 解析变量JSON
            Map<String, Object> variableMap = objectMapper.readValue(variables, Map.class);
            
            // 渲染模板
            String renderedContent = templateContent;
            Matcher matcher = VARIABLE_PATTERN.matcher(templateContent);
            
            while (matcher.find()) {
                String variableName = matcher.group(1).trim();
                Object value = variableMap.get(variableName);
                if (value != null) {
                    renderedContent = renderedContent.replace("{{" + variableName + "}}", value.toString());
                }
            }
            
            return renderedContent;
        } catch (JsonProcessingException e) {
            log.error("解析模板变量失败: {}", e.getMessage(), e);
            throw new IllegalArgumentException("模板变量格式错误");
        }
    }

    @Override
    public boolean validateTemplateVariables(String templateContent, String variables) {
        if (variables == null || variables.trim().isEmpty()) {
            return true;
        }

        try {
            // 解析变量JSON
            Map<String, Object> variableMap = objectMapper.readValue(variables, Map.class);
            
            // 提取模板中的所有变量
            Matcher matcher = VARIABLE_PATTERN.matcher(templateContent);
            while (matcher.find()) {
                String variableName = matcher.group(1).trim();
                if (!variableMap.containsKey(variableName)) {
                    return false;
                }
            }
            
            return true;
        } catch (JsonProcessingException e) {
            log.error("验证模板变量失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Object getTemplateStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 统计各类型模板数量
        long emailCount = messageTemplateRepository.findByChannelType("email").size();
        long smsCount = messageTemplateRepository.findByChannelType("sms").size();
        long wechatCount = messageTemplateRepository.findByChannelType("wechat").size();
        long pushCount = messageTemplateRepository.findByChannelType("push").size();
        
        // 统计启用和禁用模板数量
        long enabledCount = messageTemplateRepository.findByStatus(MessageTemplate.Status.ENABLED.getCode()).size();
        long disabledCount = messageTemplateRepository.findByStatus(MessageTemplate.Status.DISABLED.getCode()).size();
        
        stats.put("total", messageTemplateRepository.count());
        stats.put("emailCount", emailCount);
        stats.put("smsCount", smsCount);
        stats.put("wechatCount", wechatCount);
        stats.put("pushCount", pushCount);
        stats.put("enabledCount", enabledCount);
        stats.put("disabledCount", disabledCount);
        
        return stats;
    }

    /**
     * 将实体转换为DTO
     */
    private MessageTemplateDTO convertToDTO(MessageTemplate template) {
        MessageTemplateDTO dto = new MessageTemplateDTO();
        BeanUtils.copyProperties(template, dto);
        
        // 设置类型名称
        MessageTemplate.Type type = MessageTemplate.Type.fromCode(template.getChannelType());
        dto.setTypeName(type.getDesc());
        
        // 设置状态名称
        MessageTemplate.Status status = MessageTemplate.Status.fromCode(template.getStatus());
        dto.setStatusName(status.getDesc());
        
        // 设置时间格式
        if (template.getCreateTime() != null) {
            dto.setCreateTime(template.getCreateTime().format(DATE_FORMATTER));
        }
        if (template.getUpdateTime() != null) {
            dto.setUpdateTime(template.getUpdateTime().format(DATE_FORMATTER));
        }
        
        return dto;
    }
}
