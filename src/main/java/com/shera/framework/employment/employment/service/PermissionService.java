package com.shera.framework.employment.employment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 权限服务接口
 */
public interface PermissionService {
    
    // ==================== 角色管理方法 ====================
    
    /**
     * 创建角色
     */
    Map<String, Object> createRole(String name, String code, String description, Integer type, Integer status, List<String> permissions);
    
    /**
     * 更新角色
     */
    Map<String, Object> updateRole(Long id, Map<String, Object> roleRequest);
    
    /**
     * 删除角色
     */
    Map<String, Object> deleteRole(Long id);
    
    /**
     * 获取角色详情
     */
    Map<String, Object> getRole(Long id);
    
    /**
     * 分页查询角色列表（支持关键字模糊搜索）
     */
    Page<Map<String, Object>> listRoles(String keyword, Integer status, Pageable pageable);
    
    /**
     * 获取所有角色
     */
    List<Map<String, Object>> getAllRoles();
    
    /**
     * 获取启用角色
     */
    List<Map<String, Object>> getEnabledRoles();
    
    /**
     * 启用角色
     */
    Map<String, Object> enableRole(Long id);
    
    /**
     * 禁用角色
     */
    Map<String, Object> disableRole(Long id);
    
    // ==================== 用户角色管理方法 ====================
    
    /**
     * 分配角色给用户
     */
    Map<String, Object> assignRolesToUser(Long userId, List<Long> roleIds);
    
    /**
     * 移除用户角色
     */
    Map<String, Object> removeRoleFromUser(Long userId, Long roleId);
    
    /**
     * 获取用户角色
     */
    List<Map<String, Object>> getUserRoles(Long userId);
    
    /**
     * 获取用户权限
     */
    Map<String, Object> getUserPermissions(Long userId);
    
    /**
     * 验证用户权限
     */
    Map<String, Object> hasPermission(Long userId, String permission);
    
    // ==================== 权限管理方法 ====================
    
    /**
     * 获取所有权限
     */
    List<Map<String, Object>> getAllPermissions();
    
    /**
     * 获取权限树
     */
    List<Map<String, Object>> getPermissionTree();
    
    /**
     * 创建权限
     */
    Map<String, Object> createPermission(Map<String, Object> permissionRequest);
    
    /**
     * 更新权限
     */
    Map<String, Object> updatePermission(Long id, Map<String, Object> permissionRequest);
    
    /**
     * 删除权限
     */
    Map<String, Object> deletePermission(Long id);
    
    /**
     * 获取权限详情
     */
    Map<String, Object> getPermission(Long id);

    /**
     * 分页查询权限列表（支持名称模糊搜索）
     */
    Page<Map<String, Object>> listPermissions(String name, Pageable pageable);

    // ==================== 角色权限管理方法 ====================
    
    /**
     * 分配权限给角色
     */
    Map<String, Object> assignPermissionsToRole(Long roleId, List<Long> permissionIds);
    
    /**
     * 移除角色权限
     */
    Map<String, Object> removePermissionFromRole(Long roleId, Long permissionId);
    
    /**
     * 获取角色权限
     */
    Map<String, Object> getRolePermissions(Long roleId);
    
    // ==================== 用户菜单和按钮权限方法 ====================
    
    /**
     * 获取用户菜单
     */
    List<Map<String, Object>> getUserMenus(Long userId);
    
    /**
     * 获取用户按钮权限
     */
    List<Map<String, Object>> getUserButtons(Long userId);
    
    /**
     * 获取用户接口权限
     */
    List<Map<String, Object>> getUserApis(Long userId);
    
    // ==================== 原有方法（保持兼容性） ====================
    
    /**
     * 检查用户是否有权限
     */
    boolean hasPermissionOld(Long userId, String permission);
    
    /**
     * 检查用户是否有角色
     */
    boolean hasRole(Long userId, String role);
    
    /**
     * 获取用户所有权限
     */
    Set<String> getUserPermissionsOld(Long userId);
    
    /**
     * 获取用户所有角色
     */
    Set<String> getUserRolesOld(Long userId);
    
    /**
     * 为用户分配角色
     */
    boolean assignRoleToUser(Long userId, Long roleId);
    
    /**
     * 移除用户的角色
     */
    boolean removeRoleFromUserOld(Long userId, Long roleId);
    
    /**
     * 为角色分配权限
     */
    boolean assignPermissionToRole(Long roleId, Long permissionId);
    
    /**
     * 移除角色的权限
     */
    boolean removePermissionFromRoleOld(Long roleId, Long permissionId);
    
    /**
     * 获取角色权限列表
     */
    Set<String> getRolePermissionsOld(Long roleId);
    
    /**
     * 获取所有可用权限
     */
    List<String> getAllPermissionsOld();
    
    /**
     * 获取所有可用角色
     */
    List<String> getAllRolesOld();
}
