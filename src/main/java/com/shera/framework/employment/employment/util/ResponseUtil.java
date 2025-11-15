package com.shera.framework.employment.employment.util;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一响应格式工具类
 * 用于简化Controller层重复的响应构建代码
 */
public class ResponseUtil {
    
    private static final String DEFAULT_SUCCESS_MESSAGE = "操作成功";
    private static final String DEFAULT_ERROR_MESSAGE = "操作失败";
    
    /**
     * 创建成功响应
     */
    public static ResponseEntity<?> success() {
        return success(DEFAULT_SUCCESS_MESSAGE, null);
    }
    
    /**
     * 创建成功响应
     */
    public static ResponseEntity<?> success(String message) {
        return success(message, null);
    }
    
    /**
     * 创建成功响应
     */
    public static ResponseEntity<?> success(Object data) {
        return success(DEFAULT_SUCCESS_MESSAGE, data);
    }
    
    /**
     * 创建成功响应
     */
    public static ResponseEntity<?> success(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return ResponseEntity.ok(response);
    }
    
    /**
     * 创建错误响应
     */
    public static ResponseEntity<?> error() {
        return error(DEFAULT_ERROR_MESSAGE);
    }
    
    /**
     * 创建错误响应
     */
    public static ResponseEntity<?> error(String message) {
        return error(400, message);
    }
    
    /**
     * 创建错误响应
     */
    public static ResponseEntity<?> error(int code, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("message", message);
        response.put("data", null);
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return ResponseEntity.ok(response);
    }
    
    /**
     * 创建分页查询成功响应
     */
    public static ResponseEntity<?> successPage(Page<?> page, String message) {
        Map<String, Object> data = new HashMap<>();
        data.put("content", page.getContent());
        data.put("totalElements", page.getTotalElements());
        data.put("totalPages", page.getTotalPages());
        data.put("size", page.getSize());
        data.put("number", page.getNumber());
        
        return success(message, data);
    }
    
    /**
     * 创建分页查询成功响应（带关键词）
     */
    public static ResponseEntity<?> successPage(Page<?> page, String message, String keyword) {
        Map<String, Object> data = new HashMap<>();
        data.put("content", page.getContent());
        data.put("totalElements", page.getTotalElements());
        data.put("totalPages", page.getTotalPages());
        data.put("size", page.getSize());
        data.put("number", page.getNumber());
        data.put("keyword", keyword);
        
        return success(message, data);
    }
    
    /**
     * 创建分页查询成功响应（带筛选条件）
     */
    public static ResponseEntity<?> successPage(Page<?> page, String message, Map<String, Object> filters) {
        Map<String, Object> data = new HashMap<>();
        data.put("content", page.getContent());
        data.put("totalElements", page.getTotalElements());
        data.put("totalPages", page.getTotalPages());
        data.put("size", page.getSize());
        data.put("number", page.getNumber());
        data.put("filters", filters);
        
        return success(message, data);
    }
    
    /**
     * 创建分页查询成功响应（默认消息）
     */
    public static ResponseEntity<?> successPage(Page<?> page) {
        return successPage(page, "查询成功");
    }
    
    /**
     * 创建列表查询成功响应
     */
    public static ResponseEntity<?> successList(java.util.List<?> list, String message) {
        Map<String, Object> data = new HashMap<>();
        data.put("content", list);
        data.put("totalElements", list.size());
        data.put("totalPages", 1);
        data.put("size", list.size());
        data.put("number", 0);
        
        return success(message, data);
    }
    
    /**
     * 创建列表查询成功响应（默认消息）
     */
    public static ResponseEntity<?> successList(java.util.List<?> list) {
        return successList(list, "查询成功");
    }
    
    /**
     * 创建统计查询成功响应
     */
    public static ResponseEntity<?> successStats(Map<String, Object> stats, String message) {
        return success(message, stats);
    }
    
    /**
     * 创建统计查询成功响应（默认消息）
     */
    public static ResponseEntity<?> successStats(Map<String, Object> stats) {
        return successStats(stats, "统计成功");
    }
    
    /**
     * 创建操作成功响应
     */
    public static ResponseEntity<?> successOperation(String message) {
        return success(message, null);
    }
    
    /**
     * 创建操作成功响应（默认消息）
     */
    public static ResponseEntity<?> successOperation() {
        return successOperation("操作成功");
    }
}
