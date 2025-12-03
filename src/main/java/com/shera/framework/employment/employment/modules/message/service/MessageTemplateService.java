package com.shera.framework.employment.employment.modules.message.service;

import com.shera.framework.employment.employment.modules.message.dto.MessageTemplateDTO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 消息模板服务接口
 */
public interface MessageTemplateService {

    /**
     * 创建消息模板
     */
    MessageTemplateDTO createTemplate(MessageTemplateDTO templateDTO);

    /**
     * 更新消息模板
     */
    MessageTemplateDTO updateTemplate(Long id, MessageTemplateDTO templateDTO);

    /**
     * 获取模板详情
     */
    MessageTemplateDTO getTemplate(Long id);

    /**
     * 删除模板
     */
    void deleteTemplate(Long id);

    /**
     * 分页查询模板
     */
    Page<MessageTemplateDTO> listTemplates(String type, Integer status, Integer page, Integer size);

    /**
     * 根据类型查询模板列表
     */
    List<MessageTemplateDTO> listTemplatesByType(String type);

    /**
     * 根据类型和状态查询模板列表
     */
    List<MessageTemplateDTO> listTemplatesByTypeAndStatus(String type, Integer status);

    /**
     * 根据名称和类型查询模板
     */
    MessageTemplateDTO getTemplateByNameAndType(String name, String type);

    /**
     * 启用模板
     */
    void enableTemplate(Long id);

    /**
     * 禁用模板
     */
    void disableTemplate(Long id);

    /**
     * 渲染模板内容
     */
    String renderTemplate(String templateContent, String variables);

    /**
     * 验证模板变量
     */
    boolean validateTemplateVariables(String templateContent, String variables);

    /**
     * 获取模板统计信息
     */
    Object getTemplateStats();
}