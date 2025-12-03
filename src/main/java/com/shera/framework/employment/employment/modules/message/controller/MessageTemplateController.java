package com.shera.framework.employment.employment.modules.message.controller;

import com.shera.framework.employment.employment.modules.message.dto.MessageTemplateDTO;
import com.shera.framework.employment.employment.modules.message.service.MessageTemplateService;
import com.shera.framework.employment.employment.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息模板控制器
 */
@RestController
@RequestMapping("/api/message/templates")
@Tag(name = "消息模板管理", description = "消息模板增删改查API")
@Slf4j
public class MessageTemplateController {

    @Autowired
    private MessageTemplateService messageTemplateService;

    @PostMapping
    @Operation(summary = "创建消息模板", description = "创建新的消息模板")
    public ResponseEntity<?> createTemplate(@RequestBody MessageTemplateDTO templateDTO) {
        try {
            MessageTemplateDTO createdTemplate = messageTemplateService.createTemplate(templateDTO);
            return ResponseUtil.success("模板创建成功", createdTemplate);
        } catch (Exception e) {
            log.error("创建模板失败: {}", e.getMessage(), e);
            return ResponseUtil.error("创建模板失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新消息模板", description = "更新指定ID的消息模板")
    public ResponseEntity<?> updateTemplate(@PathVariable Long id, @RequestBody MessageTemplateDTO templateDTO) {
        try {
            MessageTemplateDTO updatedTemplate = messageTemplateService.updateTemplate(id, templateDTO);
            return ResponseUtil.success("模板更新成功", updatedTemplate);
        } catch (Exception e) {
            log.error("更新模板失败: {}", e.getMessage(), e);
            return ResponseUtil.error("更新模板失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取模板详情", description = "根据ID获取模板详情")
    public ResponseEntity<?> getTemplate(@PathVariable Long id) {
        try {
            MessageTemplateDTO template = messageTemplateService.getTemplate(id);
            return ResponseUtil.success(template);
        } catch (Exception e) {
            log.error("获取模板详情失败: {}", e.getMessage(), e);
            return ResponseUtil.error("获取模板详情失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除模板", description = "删除指定ID的模板")
    public ResponseEntity<?> deleteTemplate(@PathVariable Long id) {
        try {
            messageTemplateService.deleteTemplate(id);
            return ResponseUtil.success("模板删除成功");
        } catch (Exception e) {
            log.error("删除模板失败: {}", e.getMessage(), e);
            return ResponseUtil.error("删除模板失败: " + e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "分页查询模板", description = "根据类型和状态分页查询模板")
    public ResponseEntity<?> listTemplates(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        try {
            Page<MessageTemplateDTO> templates = messageTemplateService.listTemplates(type, status, page, size);
            return ResponseUtil.success(templates);
        } catch (Exception e) {
            log.error("查询模板列表失败: {}", e.getMessage(), e);
            return ResponseUtil.error("查询模板列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "根据类型查询模板", description = "根据模板类型查询模板列表")
    public ResponseEntity<?> listTemplatesByType(@PathVariable String type) {
        try {
            List<MessageTemplateDTO> templates = messageTemplateService.listTemplatesByType(type);
            return ResponseUtil.success(templates);
        } catch (Exception e) {
            log.error("根据类型查询模板失败: {}", e.getMessage(), e);
            return ResponseUtil.error("根据类型查询模板失败: " + e.getMessage());
        }
    }

    @GetMapping("/type/{type}/status/{status}")
    @Operation(summary = "根据类型和状态查询模板", description = "根据模板类型和状态查询模板列表")
    public ResponseEntity<?> listTemplatesByTypeAndStatus(
            @PathVariable String type, 
            @PathVariable Integer status) {
        try {
            List<MessageTemplateDTO> templates = messageTemplateService.listTemplatesByTypeAndStatus(type, status);
            return ResponseUtil.success(templates);
        } catch (Exception e) {
            log.error("根据类型和状态查询模板失败: {}", e.getMessage(), e);
            return ResponseUtil.error("根据类型和状态查询模板失败: " + e.getMessage());
        }
    }

    @GetMapping("/name/{name}/type/{type}")
    @Operation(summary = "根据名称和类型查询模板", description = "根据模板名称和类型查询模板详情")
    public ResponseEntity<?> getTemplateByNameAndType(
            @PathVariable String name, 
            @PathVariable String type) {
        try {
            MessageTemplateDTO template = messageTemplateService.getTemplateByNameAndType(name, type);
            return ResponseUtil.success(template);
        } catch (Exception e) {
            log.error("根据名称和类型查询模板失败: {}", e.getMessage(), e);
            return ResponseUtil.error("根据名称和类型查询模板失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/enable")
    @Operation(summary = "启用模板", description = "启用指定ID的模板")
    public ResponseEntity<?> enableTemplate(@PathVariable Long id) {
        try {
            messageTemplateService.enableTemplate(id);
            return ResponseUtil.success("模板启用成功");
        } catch (Exception e) {
            log.error("启用模板失败: {}", e.getMessage(), e);
            return ResponseUtil.error("启用模板失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/disable")
    @Operation(summary = "禁用模板", description = "禁用指定ID的模板")
    public ResponseEntity<?> disableTemplate(@PathVariable Long id) {
        try {
            messageTemplateService.disableTemplate(id);
            return ResponseUtil.success("模板禁用成功");
        } catch (Exception e) {
            log.error("禁用模板失败: {}", e.getMessage(), e);
            return ResponseUtil.error("禁用模板失败: " + e.getMessage());
        }
    }

    @PostMapping("/render")
    @Operation(summary = "渲染模板", description = "根据变量渲染模板内容")
    public ResponseEntity<?> renderTemplate(
            @RequestParam String templateContent,
            @RequestParam(required = false) String variables) {
        try {
            String renderedContent = messageTemplateService.renderTemplate(templateContent, variables);
            return ResponseUtil.success("模板渲染成功", renderedContent);
        } catch (Exception e) {
            log.error("渲染模板失败: {}", e.getMessage(), e);
            return ResponseUtil.error("渲染模板失败: " + e.getMessage());
        }
    }

    @PostMapping("/validate")
    @Operation(summary = "验证模板变量", description = "验证模板变量是否完整")
    public ResponseEntity<?> validateTemplateVariables(
            @RequestParam String templateContent,
            @RequestParam(required = false) String variables) {
        try {
            boolean isValid = messageTemplateService.validateTemplateVariables(templateContent, variables);
            return ResponseUtil.success(isValid ? "变量验证通过" : "变量验证失败", isValid);
        } catch (Exception e) {
            log.error("验证模板变量失败: {}", e.getMessage(), e);
            return ResponseUtil.error("验证模板变量失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "获取模板统计", description = "获取模板统计信息")
    public ResponseEntity<?> getTemplateStats() {
        try {
            Object stats = messageTemplateService.getTemplateStats();
            return ResponseUtil.success(stats);
        } catch (Exception e) {
            log.error("获取模板统计失败: {}", e.getMessage(), e);
            return ResponseUtil.error("获取模板统计失败: " + e.getMessage());
        }
    }
}
