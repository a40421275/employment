package com.shera.framework.employment.employment.modules.auth.controller;

import com.shera.framework.employment.employment.modules.auth.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    // ==================== 角色管理接口 ====================

    /**
     * 创建角色
     */
    @PostMapping("/roles")
    public ResponseEntity<?> createRole(@RequestBody Map<String, Object> roleRequest) {
        try {
            String name = (String) roleRequest.get("name");
            String code = (String) roleRequest.get("code");
            String description = (String) roleRequest.get("description");
            Integer type = (Integer) roleRequest.get("type");
            Integer status = (Integer) roleRequest.get("status");
            @SuppressWarnings("unchecked")
            List<String> permissions = (List<String>) roleRequest.get("permissions");
            
            Map<String, Object> role = permissionService.createRole(name, code, description, type, status, permissions);
            return ResponseEntity.ok(createSuccessResponse("创建成功", role));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 更新角色
     */
    @PutMapping("/roles/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody Map<String, Object> roleRequest) {
        try {
            Map<String, Object> role = permissionService.updateRole(id, roleRequest);
            return ResponseEntity.ok(createSuccessResponse("更新成功", role));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        try {
            Map<String, Object> result = permissionService.deleteRole(id);
            return ResponseEntity.ok(createSuccessResponse("删除成功", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 获取角色详情
     */
    @GetMapping("/roles/{id}")
    public ResponseEntity<?> getRole(@PathVariable Long id) {
        try {
            Map<String, Object> role = permissionService.getRole(id);
            return ResponseEntity.ok(createSuccessResponse("获取成功", role));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 分页查询角色列表（支持关键字模糊搜索）
     */
    @GetMapping("/roles")
    public ResponseEntity<?> listRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Map<String, Object>> roles = permissionService.listRoles(keyword, status, pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        
        Map<String, Object> data = new HashMap<>();
        data.put("content", roles.getContent());
        data.put("totalElements", roles.getTotalElements());
        data.put("totalPages", roles.getTotalPages());
        data.put("size", roles.getSize());
        data.put("number", roles.getNumber());
        
        response.put("data", data);
        
        return ResponseEntity.ok(response);
    }

    /**
     * 获取所有角色
     */
    @GetMapping("/roles/all")
    public ResponseEntity<?> getAllRoles() {
        try {
            List<Map<String, Object>> roles = permissionService.getAllRoles();
            return ResponseEntity.ok(createSuccessResponse("获取成功", roles));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 获取启用角色
     */
    @GetMapping("/roles/enabled")
    public ResponseEntity<?> getEnabledRoles() {
        try {
            List<Map<String, Object>> roles = permissionService.getEnabledRoles();
            return ResponseEntity.ok(createSuccessResponse("获取成功", roles));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 启用角色
     */
    @PostMapping("/roles/{id}/enable")
    public ResponseEntity<?> enableRole(@PathVariable Long id) {
        try {
            Map<String, Object> role = permissionService.enableRole(id);
            return ResponseEntity.ok(createSuccessResponse("启用成功", role));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 禁用角色
     */
    @PostMapping("/roles/{id}/disable")
    public ResponseEntity<?> disableRole(@PathVariable Long id) {
        try {
            Map<String, Object> role = permissionService.disableRole(id);
            return ResponseEntity.ok(createSuccessResponse("禁用成功", role));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    // ==================== 用户角色管理接口 ====================

    /**
     * 分配角色给用户
     */
    @PostMapping("/users/{userId}/roles")
    public ResponseEntity<?> assignRolesToUser(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        try {
            Map<String, Object> result = permissionService.assignRolesToUser(userId, roleIds);
            return ResponseEntity.ok(createSuccessResponse("分配成功", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 移除用户角色
     */
    @DeleteMapping("/users/{userId}/roles/{roleId}")
    public ResponseEntity<?> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        try {
            Map<String, Object> result = permissionService.removeRoleFromUser(userId, roleId);
            return ResponseEntity.ok(createSuccessResponse("移除成功", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 获取用户角色
     */
    @GetMapping("/users/{userId}/roles")
    public ResponseEntity<?> getUserRoles(@PathVariable Long userId) {
        try {
            List<Map<String, Object>> roles = permissionService.getUserRoles(userId);
            return ResponseEntity.ok(createSuccessResponse("获取成功", roles));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 获取用户权限
     */
    @GetMapping("/users/{userId}/permissions")
    public ResponseEntity<?> getUserPermissions(@PathVariable Long userId) {
        try {
            Map<String, Object> permissions = permissionService.getUserPermissions(userId);
            return ResponseEntity.ok(createSuccessResponse("获取成功", permissions));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 验证用户权限
     */
    @GetMapping("/users/{userId}/has-permission")
    public ResponseEntity<?> hasPermission(@PathVariable Long userId, @RequestParam String permission) {
        try {
            Map<String, Object> result = permissionService.hasPermission(userId, permission);
            return ResponseEntity.ok(createSuccessResponse("验证成功", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    // ==================== 权限管理接口 ====================

    /**
     * 获取所有权限
     */
    @GetMapping("/permissions")
    public ResponseEntity<?> getAllPermissions() {
        try {
            List<Map<String, Object>> permissions = permissionService.getAllPermissions();
            return ResponseEntity.ok(createSuccessResponse("获取成功", permissions));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 获取权限树
     */
    @GetMapping("/permissions/tree")
    public ResponseEntity<?> getPermissionTree() {
        try {
            List<Map<String, Object>> permissionTree = permissionService.getPermissionTree();
            return ResponseEntity.ok(createSuccessResponse("获取成功", permissionTree));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 创建权限
     */
    @PostMapping("/permissions")
    public ResponseEntity<?> createPermission(@RequestBody Map<String, Object> permissionRequest) {
        try {
            Map<String, Object> permission = permissionService.createPermission(permissionRequest);
            return ResponseEntity.ok(createSuccessResponse("创建成功", permission));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 更新权限
     */
    @PutMapping("/permissions/{id}")
    public ResponseEntity<?> updatePermission(@PathVariable Long id, @RequestBody Map<String, Object> permissionRequest) {
        try {
            Map<String, Object> permission = permissionService.updatePermission(id, permissionRequest);
            return ResponseEntity.ok(createSuccessResponse("更新成功", permission));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<?> deletePermission(@PathVariable Long id) {
        try {
            Map<String, Object> result = permissionService.deletePermission(id);
            return ResponseEntity.ok(createSuccessResponse("删除成功", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 获取权限详情
     */
    @GetMapping("/permissions/{id}")
    public ResponseEntity<?> getPermission(@PathVariable Long id) {
        try {
            Map<String, Object> permission = permissionService.getPermission(id);
            return ResponseEntity.ok(createSuccessResponse("获取成功", permission));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 分页查询权限列表（支持名称模糊搜索）
     */
    @GetMapping("/permissions/list")
    public ResponseEntity<?> listPermissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String name) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Map<String, Object>> permissions = permissionService.listPermissions(name, pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        
        Map<String, Object> data = new HashMap<>();
        data.put("content", permissions.getContent());
        data.put("totalElements", permissions.getTotalElements());
        data.put("totalPages", permissions.getTotalPages());
        data.put("size", permissions.getSize());
        data.put("number", permissions.getNumber());
        
        response.put("data", data);
        
        return ResponseEntity.ok(response);
    }

    // ==================== 角色权限管理接口 ====================

    /**
     * 分配权限给角色
     */
    @PostMapping("/roles/{roleId}/permissions")
    public ResponseEntity<?> assignPermissionsToRole(@PathVariable Long roleId, @RequestBody List<Long> permissionIds) {
        try {
            Map<String, Object> result = permissionService.assignPermissionsToRole(roleId, permissionIds);
            return ResponseEntity.ok(createSuccessResponse("分配成功", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 移除角色权限
     */
    @DeleteMapping("/roles/{roleId}/permissions/{permissionId}")
    public ResponseEntity<?> removePermissionFromRole(@PathVariable Long roleId, @PathVariable Long permissionId) {
        try {
            Map<String, Object> result = permissionService.removePermissionFromRole(roleId, permissionId);
            return ResponseEntity.ok(createSuccessResponse("移除成功", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 获取角色权限
     */
    @GetMapping("/roles/{roleId}/permissions")
    public ResponseEntity<?> getRolePermissions(@PathVariable Long roleId) {
        try {
            Map<String, Object> permissions = permissionService.getRolePermissions(roleId);
            return ResponseEntity.ok(createSuccessResponse("获取成功", permissions));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    // ==================== 用户菜单和按钮权限接口 ====================

    /**
     * 获取用户菜单
     */
    @GetMapping("/users/{userId}/menus")
    public ResponseEntity<?> getUserMenus(@PathVariable Long userId) {
        try {
            List<Map<String, Object>> menus = permissionService.getUserMenus(userId);
            return ResponseEntity.ok(createSuccessResponse("获取成功", menus));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 获取用户按钮权限
     */
    @GetMapping("/users/{userId}/buttons")
    public ResponseEntity<?> getUserButtons(@PathVariable Long userId) {
        try {
            List<Map<String, Object>> buttons = permissionService.getUserButtons(userId);
            return ResponseEntity.ok(createSuccessResponse("获取成功", buttons));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 获取用户接口权限
     */
    @GetMapping("/users/{userId}/apis")
    public ResponseEntity<?> getUserApis(@PathVariable Long userId) {
        try {
            List<Map<String, Object>> apis = permissionService.getUserApis(userId);
            return ResponseEntity.ok(createSuccessResponse("获取成功", apis));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 创建成功响应
     */
    private Map<String, Object> createSuccessResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return response;
    }

    /**
     * 创建错误响应
     */
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 400);
        response.put("message", message);
        response.put("data", null);
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return response;
    }
}
