package com.shera.framework.employment.employment.service.impl;

import com.shera.framework.employment.employment.entity.Permission;
import com.shera.framework.employment.employment.entity.Role;
import com.shera.framework.employment.employment.entity.RolePermission;
import com.shera.framework.employment.employment.entity.UserRole;
import com.shera.framework.employment.employment.repository.PermissionRepository;
import com.shera.framework.employment.employment.repository.RolePermissionRepository;
import com.shera.framework.employment.employment.repository.RoleRepository;
import com.shera.framework.employment.employment.repository.UserRoleRepository;
import com.shera.framework.employment.employment.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    // ==================== 角色管理方法实现 ====================

    @Override
    @Transactional
    public Map<String, Object> createRole(String name, String code, String description, Integer type, Integer status, List<String> permissions) {
        try {
            // 检查角色代码是否已存在
            if (roleRepository.existsByCode(code)) {
                throw new RuntimeException("角色代码已存在");
            }
            
            // 创建角色
            Role role = new Role();
            role.setName(name);
            role.setCode(code);
            role.setDescription(description);
            role.setType(type != null ? type : 3); // 默认为自定义角色
            role.setStatus(status != null ? status : 1);
            role.setCreateTime(LocalDateTime.now());
            role.setUpdateTime(LocalDateTime.now());
            
            Role savedRole = roleRepository.save(role);
            
            // 分配权限
            if (permissions != null && !permissions.isEmpty()) {
                for (String permissionCode : permissions) {
                    Optional<Permission> permission = permissionRepository.findByCode(permissionCode);
                    if (permission.isPresent()) {
                        RolePermission rolePermission = new RolePermission();
                        rolePermission.setRoleId(savedRole.getId());
                        rolePermission.setPermissionId(permission.get().getId());
                        rolePermissionRepository.save(rolePermission);
                    }
                }
            }
            
            // 构建响应
            Map<String, Object> response = new HashMap<>();
            response.put("id", savedRole.getId());
            response.put("name", savedRole.getName());
            response.put("code", savedRole.getCode());
            response.put("status", savedRole.getStatus());
            
            log.info("创建角色成功: {}", savedRole.getName());
            return response;
        } catch (Exception e) {
            log.error("创建角色失败: {}", e.getMessage(), e);
            throw new RuntimeException("创建角色失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Map<String, Object> updateRole(Long id, Map<String, Object> roleRequest) {
        try {
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("角色不存在"));
            
            // 更新角色信息
            if (roleRequest.containsKey("name")) {
                role.setName((String) roleRequest.get("name"));
            }
            if (roleRequest.containsKey("description")) {
                role.setDescription((String) roleRequest.get("description"));
            }
            if (roleRequest.containsKey("type")) {
                role.setType((Integer) roleRequest.get("type"));
            }
            if (roleRequest.containsKey("status")) {
                role.setStatus((Integer) roleRequest.get("status"));
            }
            role.setUpdateTime(LocalDateTime.now());
            
            Role updatedRole = roleRepository.save(role);
            
            // 构建响应
            Map<String, Object> response = new HashMap<>();
            response.put("id", updatedRole.getId());
            response.put("name", updatedRole.getName());
            response.put("code", updatedRole.getCode());
            response.put("description", updatedRole.getDescription());
            response.put("type", updatedRole.getType());
            response.put("status", updatedRole.getStatus());
            response.put("updateTime", updatedRole.getUpdateTime());
            
            log.info("更新角色成功: {}", updatedRole.getName());
            return response;
        } catch (Exception e) {
            log.error("更新角色失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新角色失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Map<String, Object> deleteRole(Long id) {
        try {
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("角色不存在"));
            
            // 删除角色权限关系
            rolePermissionRepository.deleteByRoleId(id);
            
            // 删除用户角色关系
            userRoleRepository.deleteByRoleId(id);
            
            // 删除角色
            roleRepository.deleteById(id);
            
            // 构建响应
            Map<String, Object> response = new HashMap<>();
            response.put("id", role.getId());
            response.put("name", role.getName());
            
            log.info("删除角色成功: {}", role.getName());
            return response;
        } catch (Exception e) {
            log.error("删除角色失败: {}", e.getMessage(), e);
            throw new RuntimeException("删除角色失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getRole(Long id) {
        try {
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("角色不存在"));
            
            // 获取角色权限
            List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(id);
            List<Map<String, Object>> permissions = rolePermissions.stream()
                    .map(rp -> {
                        Optional<Permission> permission = permissionRepository.findById(rp.getPermissionId());
                        if (permission.isPresent()) {
                            Map<String, Object> perm = new HashMap<>();
                            perm.put("id", permission.get().getId());
                            perm.put("name", permission.get().getName());
                            perm.put("code", permission.get().getCode());
                            perm.put("type", permission.get().getType());
                            return perm;
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            
            // 构建响应
            Map<String, Object> response = new HashMap<>();
            response.put("id", role.getId());
            response.put("name", role.getName());
            response.put("code", role.getCode());
            response.put("description", role.getDescription());
            response.put("type", role.getType());
            response.put("typeName", getRoleTypeName(role.getType()));
            response.put("status", role.getStatus());
            response.put("permissions", permissions);
            response.put("createTime", role.getCreateTime());
            response.put("updateTime", role.getUpdateTime());
            
            return response;
        } catch (Exception e) {
            log.error("获取角色详情失败: {}", e.getMessage(), e);
            throw new RuntimeException("获取角色详情失败: " + e.getMessage());
        }
    }

    @Override
    public Page<Map<String, Object>> listRoles(String keyword, Integer status, Pageable pageable) {
        try {
            Page<Role> roles;
            if (keyword != null && !keyword.trim().isEmpty()) {
                // 支持关键字模糊搜索（角色名称和角色编码）
                String searchKeyword = keyword.trim();
                if (status != null) {
                    roles = roleRepository.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCaseAndStatus(
                            searchKeyword, status, pageable);
                } else {
                    roles = roleRepository.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(
                            searchKeyword, pageable);
                }
            } else {
                if (status != null) {
                    roles = roleRepository.findByStatus(status, pageable);
                } else {
                    roles = roleRepository.findAll(pageable);
                }
            }
            
            List<Map<String, Object>> roleList = roles.getContent().stream()
                    .map(role -> {
                        Map<String, Object> roleMap = new HashMap<>();
                        roleMap.put("id", role.getId());
                        roleMap.put("name", role.getName());
                        roleMap.put("code", role.getCode());
                        roleMap.put("description", role.getDescription());
                        roleMap.put("type", role.getType());
                        roleMap.put("typeName", getRoleTypeName(role.getType()));
                        roleMap.put("status", role.getStatus());
                        
                        // 统计用户数量
                        long userCount = userRoleRepository.countByRoleId(role.getId());
                        roleMap.put("userCount", userCount);
                        
                        // 统计权限数量
                        long permissionCount = rolePermissionRepository.countByRoleId(role.getId());
                        roleMap.put("permissionCount", permissionCount);
                        
                        roleMap.put("createTime", role.getCreateTime());
                        return roleMap;
                    })
                    .collect(Collectors.toList());
            
            return new PageImpl<>(roleList, pageable, roles.getTotalElements());
        } catch (Exception e) {
            log.error("查询角色列表失败: {}", e.getMessage(), e);
            throw new RuntimeException("查询角色列表失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getAllRoles() {
        try {
            List<Role> roles = roleRepository.findAll();
            return roles.stream()
                    .map(role -> {
                        Map<String, Object> roleMap = new HashMap<>();
                        roleMap.put("id", role.getId());
                        roleMap.put("name", role.getName());
                        roleMap.put("code", role.getCode());
                        roleMap.put("description", role.getDescription());
                        roleMap.put("type", role.getType());
                        roleMap.put("typeName", getRoleTypeName(role.getType()));
                        roleMap.put("status", role.getStatus());
                        return roleMap;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取所有角色失败: {}", e.getMessage(), e);
            throw new RuntimeException("获取所有角色失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getEnabledRoles() {
        try {
            List<Role> roles = roleRepository.findByStatus(1);
            return roles.stream()
                    .map(role -> {
                        Map<String, Object> roleMap = new HashMap<>();
                        roleMap.put("id", role.getId());
                        roleMap.put("name", role.getName());
                        roleMap.put("code", role.getCode());
                        roleMap.put("description", role.getDescription());
                        roleMap.put("type", role.getType());
                        roleMap.put("typeName", getRoleTypeName(role.getType()));
                        return roleMap;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取启用角色失败: {}", e.getMessage(), e);
            throw new RuntimeException("获取启用角色失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Map<String, Object> enableRole(Long id) {
        try {
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("角色不存在"));
            
            role.setStatus(1);
            role.setUpdateTime(LocalDateTime.now());
            Role updatedRole = roleRepository.save(role);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", updatedRole.getId());
            response.put("name", updatedRole.getName());
            response.put("status", updatedRole.getStatus());
            response.put("updateTime", updatedRole.getUpdateTime());
            
            log.info("启用角色成功: {}", updatedRole.getName());
            return response;
        } catch (Exception e) {
            log.error("启用角色失败: {}", e.getMessage(), e);
            throw new RuntimeException("启用角色失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Map<String, Object> disableRole(Long id) {
        try {
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("角色不存在"));
            
            role.setStatus(0);
            role.setUpdateTime(LocalDateTime.now());
            Role updatedRole = roleRepository.save(role);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", updatedRole.getId());
            response.put("name", updatedRole.getName());
            response.put("status", updatedRole.getStatus());
            response.put("updateTime", updatedRole.getUpdateTime());
            
            log.info("禁用角色成功: {}", updatedRole.getName());
            return response;
        } catch (Exception e) {
            log.error("禁用角色失败: {}", e.getMessage(), e);
            throw new RuntimeException("禁用角色失败: " + e.getMessage());
        }
    }

    // ==================== 用户角色管理方法实现 ====================

    @Override
    @Transactional
    public Map<String, Object> assignRolesToUser(Long userId, List<Long> roleIds) {
        try {
            List<Map<String, Object>> assignedRoles = new ArrayList<>();
            
            for (Long roleId : roleIds) {
                Optional<Role> role = roleRepository.findById(roleId);
                if (role.isPresent()) {
                    // 检查是否已经分配
                    Optional<UserRole> existing = userRoleRepository.findByUserIdAndRoleId(userId, roleId);
                    if (existing.isEmpty()) {
                        UserRole userRole = new UserRole();
                        userRole.setUserId(userId);
                        userRole.setRoleId(roleId);
                        userRoleRepository.save(userRole);
                    }
                    
                    Map<String, Object> roleInfo = new HashMap<>();
                    roleInfo.put("id", role.get().getId());
                    roleInfo.put("name", role.get().getName());
                    roleInfo.put("code", role.get().getCode());
                    assignedRoles.add(roleInfo);
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("userId", userId);
            response.put("assignedRoles", assignedRoles);
            response.put("assignedAt", LocalDateTime.now());
            
            log.info("为用户 {} 分配角色成功", userId);
            return response;
        } catch (Exception e) {
            log.error("分配角色给用户失败: {}", e.getMessage(), e);
            throw new RuntimeException("分配角色给用户失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Map<String, Object> removeRoleFromUser(Long userId, Long roleId) {
        try {
            Optional<UserRole> userRole = userRoleRepository.findByUserIdAndRoleId(userId, roleId);
            if (userRole.isEmpty()) {
                throw new RuntimeException("用户没有该角色");
            }
            
            Optional<Role> role = roleRepository.findById(roleId);
            String roleName = role.map(Role::getName).orElse("未知角色");
            
            userRoleRepository.delete(userRole.get());
            
            Map<String, Object> response = new HashMap<>();
            response.put("userId", userId);
            response.put("roleId", roleId);
            response.put("roleName", roleName);
            
            log.info("移除用户 {} 的角色 {} 成功", userId, roleId);
            return response;
        } catch (Exception e) {
            log.error("移除用户角色失败: {}", e.getMessage(), e);
            throw new RuntimeException("移除用户角色失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getUserRoles(Long userId) {
        try {
            List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
            return userRoles.stream()
                    .map(userRole -> {
                        Optional<Role> role = roleRepository.findById(userRole.getRoleId());
                        if (role.isPresent()) {
                            Map<String, Object> roleMap = new HashMap<>();
                            roleMap.put("id", role.get().getId());
                            roleMap.put("name", role.get().getName());
                            roleMap.put("code", role.get().getCode());
                            roleMap.put("description", role.get().getDescription());
                            roleMap.put("status", role.get().getStatus());
                            roleMap.put("assignTime", userRole.getCreateTime());
                            return roleMap;
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取用户角色失败: {}", e.getMessage(), e);
            throw new RuntimeException("获取用户角色失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getUserPermissions(Long userId) {
        try {
            Set<String> permissions = getUserPermissionsOld(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("userId", userId);
            response.put("permissions", permissions.stream()
                    .map(permissionCode -> {
                        Optional<Permission> permission = permissionRepository.findByCode(permissionCode);
                        if (permission.isPresent()) {
                            Map<String, Object> perm = new HashMap<>();
                            perm.put("id", permission.get().getId());
                            perm.put("name", permission.get().getName());
                            perm.put("code", permission.get().getCode());
                            perm.put("type", permission.get().getType());
                            perm.put("url", permission.get().getUrl());
                            perm.put("method", permission.get().getMethod());
                            return perm;
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
            response.put("totalCount", permissions.size());
            
            return response;
        } catch (Exception e) {
            log.error("获取用户权限失败: {}", e.getMessage(), e);
            throw new RuntimeException("获取用户权限失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> hasPermission(Long userId, String permission) {
        try {
            boolean hasPermission = hasPermissionOld(userId, permission);
            Set<String> userRoles = getUserRolesOld(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("userId", userId);
            response.put("permission", permission);
            response.put("hasPermission", hasPermission);
            response.put("roles", new ArrayList<>(userRoles));
            
            return response;
        } catch (Exception e) {
            log.error("验证用户权限失败: {}", e.getMessage(), e);
            throw new RuntimeException("验证用户权限失败: " + e.getMessage());
        }
    }

    // ==================== 权限管理方法实现 ====================

    @Override
    public List<Map<String, Object>> getAllPermissions() {
        try {
            List<Permission> permissions = permissionRepository.findAll();
            return permissions.stream()
                    .map(permission -> {
                        Map<String, Object> perm = new HashMap<>();
                        perm.put("id", permission.getId());
                        perm.put("name", permission.getName());
                        perm.put("code", permission.getCode());
                        perm.put("type", permission.getType());
                        perm.put("url", permission.getUrl());
                        perm.put("method", permission.getMethod());
                        perm.put("status", permission.getStatus());
                        perm.put("sortOrder", permission.getSortOrder());
                        perm.put("description", permission.getDescription());
                        perm.put("icon", permission.getIcon());
                        perm.put("module", permission.getModule());
                        perm.put("parentId", permission.getParentId());
                        perm.put("createTime", permission.getCreateTime());
                        perm.put("updateTime", permission.getUpdateTime());
                        return perm;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取所有权限失败: {}", e.getMessage(), e);
            throw new RuntimeException("获取所有权限失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getPermissionTree() {
        try {
            List<Permission> permissions = permissionRepository.findAll();
            
            // 构建权限树
            Map<Long, Map<String, Object>> permissionMap = new HashMap<>();
            List<Map<String, Object>> rootPermissions = new ArrayList<>();
            
            // 第一遍：创建所有权限节点
            for (Permission permission : permissions) {
                Map<String, Object> permNode = new HashMap<>();
                permNode.put("id", permission.getId());
                permNode.put("name", permission.getName());
                permNode.put("code", permission.getCode());
                permNode.put("type", permission.getType());
                permNode.put("url", permission.getUrl());
                permNode.put("method", permission.getMethod());
                permNode.put("description", permission.getDescription());
                permNode.put("icon", permission.getIcon());
                permNode.put("module", permission.getModule());
                permNode.put("sortOrder", permission.getSortOrder());
                permNode.put("status", permission.getStatus());
                permNode.put("parentId", permission.getParentId());
                permNode.put("children", new ArrayList<Map<String, Object>>());
                permissionMap.put(permission.getId(), permNode);
            }
            
            // 第二遍：构建树形结构
            for (Permission permission : permissions) {
                Map<String, Object> permNode = permissionMap.get(permission.getId());
                Long parentId = permission.getParentId();
                
                if (parentId == null || parentId == 0) {
                    rootPermissions.add(permNode);
                } else {
                    Map<String, Object> parentNode = permissionMap.get(parentId);
                    if (parentNode != null) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> children = (List<Map<String, Object>>) parentNode.get("children");
                        children.add(permNode);
                    }
                }
            }
            
            return rootPermissions;
        } catch (Exception e) {
            log.error("获取权限树失败: {}", e.getMessage(), e);
            throw new RuntimeException("获取权限树失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Map<String, Object> createPermission(Map<String, Object> permissionRequest) {
        try {
            String name = (String) permissionRequest.get("name");
            String code = (String) permissionRequest.get("code");
            Integer type = (Integer) permissionRequest.get("type");
            Long parentId = permissionRequest.containsKey("parentId") ? 
                (permissionRequest.get("parentId") != null ? ((Number) permissionRequest.get("parentId")).longValue() : 0L) : 0L;
            String url = (String) permissionRequest.get("url");
            String method = (String) permissionRequest.get("method");
            Integer sortOrder = (Integer) permissionRequest.get("sortOrder");
            Integer status = (Integer) permissionRequest.get("status");
            String description = (String) permissionRequest.get("description");
            String icon = (String) permissionRequest.get("icon");
            String module = (String) permissionRequest.get("module");
            
            // 检查权限代码是否已存在
            if (permissionRepository.existsByCode(code)) {
                throw new RuntimeException("权限代码已存在");
            }
            
            Permission permission = new Permission();
            permission.setName(name);
            permission.setCode(code);
            permission.setType(type);
            permission.setParentId(parentId);
            permission.setUrl(url);
            permission.setMethod(method);
            permission.setSortOrder(sortOrder != null ? sortOrder : 1);
            permission.setStatus(status != null ? status : 1);
            permission.setDescription(description);
            permission.setIcon(icon);
            permission.setModule(module);
            permission.setCreateTime(LocalDateTime.now());
            permission.setUpdateTime(LocalDateTime.now());
            
            Permission savedPermission = permissionRepository.save(permission);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", savedPermission.getId());
            response.put("name", savedPermission.getName());
            response.put("code", savedPermission.getCode());
            response.put("type", savedPermission.getType());
            response.put("parentId", savedPermission.getParentId());
            response.put("url", savedPermission.getUrl());
            response.put("method", savedPermission.getMethod());
            response.put("sortOrder", savedPermission.getSortOrder());
            response.put("status", savedPermission.getStatus());
            response.put("description", savedPermission.getDescription());
            response.put("icon", savedPermission.getIcon());
            response.put("module", savedPermission.getModule());
            response.put("createTime", savedPermission.getCreateTime());
            
            log.info("创建权限成功: {}", savedPermission.getName());
            return response;
        } catch (Exception e) {
            log.error("创建权限失败: {}", e.getMessage(), e);
            throw new RuntimeException("创建权限失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Map<String, Object> updatePermission(Long id, Map<String, Object> permissionRequest) {
        try {
            Permission permission = permissionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("权限不存在"));
            
            // 更新权限信息
            if (permissionRequest.containsKey("name")) {
                permission.setName((String) permissionRequest.get("name"));
            }
            if (permissionRequest.containsKey("type")) {
                permission.setType((Integer) permissionRequest.get("type"));
            }
            if (permissionRequest.containsKey("parentId")) {
                Object parentIdObj = permissionRequest.get("parentId");
                if (parentIdObj != null) {
                    permission.setParentId(((Number) parentIdObj).longValue());
                } else {
                    permission.setParentId(0L);
                }
            }
            if (permissionRequest.containsKey("url")) {
                permission.setUrl((String) permissionRequest.get("url"));
            }
            if (permissionRequest.containsKey("method")) {
                permission.setMethod((String) permissionRequest.get("method"));
            }
            if (permissionRequest.containsKey("sortOrder")) {
                permission.setSortOrder((Integer) permissionRequest.get("sortOrder"));
            }
            if (permissionRequest.containsKey("status")) {
                permission.setStatus((Integer) permissionRequest.get("status"));
            }
            if (permissionRequest.containsKey("description")) {
                permission.setDescription((String) permissionRequest.get("description"));
            }
            if (permissionRequest.containsKey("icon")) {
                permission.setIcon((String) permissionRequest.get("icon"));
            }
            if (permissionRequest.containsKey("module")) {
                permission.setModule((String) permissionRequest.get("module"));
            }
            permission.setUpdateTime(LocalDateTime.now());
            
            Permission updatedPermission = permissionRepository.save(permission);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", updatedPermission.getId());
            response.put("name", updatedPermission.getName());
            response.put("code", updatedPermission.getCode());
            response.put("type", updatedPermission.getType());
            response.put("parentId", updatedPermission.getParentId());
            response.put("url", updatedPermission.getUrl());
            response.put("method", updatedPermission.getMethod());
            response.put("sortOrder", updatedPermission.getSortOrder());
            response.put("status", updatedPermission.getStatus());
            response.put("description", updatedPermission.getDescription());
            response.put("icon", updatedPermission.getIcon());
            response.put("module", updatedPermission.getModule());
            response.put("updateTime", updatedPermission.getUpdateTime());
            
            log.info("更新权限成功: {}", updatedPermission.getName());
            return response;
        } catch (Exception e) {
            log.error("更新权限失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新权限失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Map<String, Object> deletePermission(Long id) {
        try {
            Permission permission = permissionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("权限不存在"));
            
            // 删除角色权限关系
            rolePermissionRepository.deleteByPermissionId(id);
            
            // 删除权限
            permissionRepository.deleteById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", permission.getId());
            response.put("name", permission.getName());
            
            log.info("删除权限成功: {}", permission.getName());
            return response;
        } catch (Exception e) {
            log.error("删除权限失败: {}", e.getMessage(), e);
            throw new RuntimeException("删除权限失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getPermission(Long id) {
        try {
            Permission permission = permissionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("权限不存在"));
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", permission.getId());
            response.put("name", permission.getName());
            response.put("code", permission.getCode());
            response.put("type", permission.getType());
            response.put("parentId", permission.getParentId());
            response.put("parentName", "根节点"); // 这里可以查询父权限名称
            response.put("url", permission.getUrl());
            response.put("method", permission.getMethod());
            response.put("sortOrder", permission.getSortOrder());
            response.put("status", permission.getStatus());
            response.put("description", permission.getDescription());
            response.put("icon", permission.getIcon());
            response.put("module", permission.getModule());
            response.put("createTime", permission.getCreateTime());
            response.put("updateTime", permission.getUpdateTime());
            
            return response;
        } catch (Exception e) {
            log.error("获取权限详情失败: {}", e.getMessage(), e);
            throw new RuntimeException("获取权限详情失败: " + e.getMessage());
        }
    }

    @Override
    public Page<Map<String, Object>> listPermissions(String name, Pageable pageable) {
        try {
            Page<Permission> permissions;
            if (name != null && !name.trim().isEmpty()) {
                // 支持名称模糊搜索
                permissions = permissionRepository.findByNameContainingIgnoreCase(name.trim(), pageable);
            } else {
                permissions = permissionRepository.findAll(pageable);
            }
            
            List<Map<String, Object>> permissionList = permissions.getContent().stream()
                    .map(permission -> {
                        Map<String, Object> permMap = new HashMap<>();
                        permMap.put("id", permission.getId());
                        permMap.put("name", permission.getName());
                        permMap.put("code", permission.getCode());
                        permMap.put("type", permission.getType());
                        permMap.put("typeName", getPermissionTypeName(permission.getType()));
                        permMap.put("parentId", permission.getParentId());
                        permMap.put("url", permission.getUrl());
                        permMap.put("method", permission.getMethod());
                        permMap.put("sortOrder", permission.getSortOrder());
                        permMap.put("status", permission.getStatus());
                        permMap.put("statusName", permission.getStatus() == 1 ? "启用" : "禁用");
                        permMap.put("description", permission.getDescription());
                        permMap.put("icon", permission.getIcon());
                        permMap.put("module", permission.getModule());
                        permMap.put("createTime", permission.getCreateTime());
                        permMap.put("updateTime", permission.getUpdateTime());
                        
                        // 统计角色数量
                        long roleCount = rolePermissionRepository.countByPermissionId(permission.getId());
                        permMap.put("roleCount", roleCount);
                        
                        return permMap;
                    })
                    .collect(Collectors.toList());
            
            return new PageImpl<>(permissionList, pageable, permissions.getTotalElements());
        } catch (Exception e) {
            log.error("查询权限列表失败: {}", e.getMessage(), e);
            throw new RuntimeException("查询权限列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取权限类型名称
     */
    private String getPermissionTypeName(Integer type) {
        if (type == null) return "未知";
        switch (type) {
            case 1: return "菜单";
            case 2: return "按钮";
            case 3: return "接口";
            default: return "未知";
        }
    }

    /**
     * 获取角色类型名称
     */
    private String getRoleTypeName(Integer type) {
        if (type == null) return "未知";
        switch (type) {
            case 1: return "系统角色";
            case 2: return "业务角色";
            case 3: return "自定义角色";
            default: return "未知";
        }
    }

    // ==================== 角色权限管理方法实现 ====================

    @Override
    @Transactional
    public Map<String, Object> assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        try {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("角色不存在"));
            
            List<Map<String, Object>> assignedPermissions = new ArrayList<>();
            
            for (Long permissionId : permissionIds) {
                Optional<Permission> permission = permissionRepository.findById(permissionId);
                if (permission.isPresent()) {
                    // 检查是否已经分配
                    Optional<RolePermission> existing = rolePermissionRepository.findByRoleIdAndPermissionId(roleId, permissionId);
                    if (existing.isEmpty()) {
                        RolePermission rolePermission = new RolePermission();
                        rolePermission.setRoleId(roleId);
                        rolePermission.setPermissionId(permissionId);
                        rolePermissionRepository.save(rolePermission);
                    }
                    
                    Map<String, Object> permInfo = new HashMap<>();
                    permInfo.put("id", permission.get().getId());
                    permInfo.put("name", permission.get().getName());
                    permInfo.put("code", permission.get().getCode());
                    assignedPermissions.add(permInfo);
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("roleId", roleId);
            response.put("roleName", role.getName());
            response.put("assignedPermissions", assignedPermissions);
            response.put("assignedAt", LocalDateTime.now());
            
            log.info("为角色 {} 分配权限成功", roleId);
            return response;
        } catch (Exception e) {
            log.error("分配权限给角色失败: {}", e.getMessage(), e);
            throw new RuntimeException("分配权限给角色失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Map<String, Object> removePermissionFromRole(Long roleId, Long permissionId) {
        try {
            Optional<RolePermission> rolePermission = rolePermissionRepository.findByRoleIdAndPermissionId(roleId, permissionId);
            if (rolePermission.isEmpty()) {
                throw new RuntimeException("角色没有该权限");
            }
            
            Optional<Permission> permission = permissionRepository.findById(permissionId);
            String permissionName = permission.map(Permission::getName).orElse("未知权限");
            
            rolePermissionRepository.delete(rolePermission.get());
            
            Map<String, Object> response = new HashMap<>();
            response.put("roleId", roleId);
            response.put("permissionId", permissionId);
            response.put("permissionName", permissionName);
            
            log.info("移除角色 {} 的权限 {} 成功", roleId, permissionId);
            return response;
        } catch (Exception e) {
            log.error("移除角色权限失败: {}", e.getMessage(), e);
            throw new RuntimeException("移除角色权限失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getRolePermissions(Long roleId) {
        try {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("角色不存在"));
            
            List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(roleId);
            List<Map<String, Object>> permissions = rolePermissions.stream()
                    .map(rp -> {
                        Optional<Permission> permission = permissionRepository.findById(rp.getPermissionId());
                        if (permission.isPresent()) {
                            Map<String, Object> perm = new HashMap<>();
                            perm.put("id", permission.get().getId());
                            perm.put("name", permission.get().getName());
                            perm.put("code", permission.get().getCode());
                            perm.put("type", permission.get().getType());
                            perm.put("url", permission.get().getUrl());
                            perm.put("method", permission.get().getMethod());
                            return perm;
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("roleId", roleId);
            response.put("roleName", role.getName());
            response.put("permissions", permissions);
            response.put("totalCount", permissions.size());
            
            return response;
        } catch (Exception e) {
            log.error("获取角色权限失败: {}", e.getMessage(), e);
            throw new RuntimeException("获取角色权限失败: " + e.getMessage());
        }
    }

    // ==================== 用户菜单和按钮权限方法实现 ====================

    @Override
    public List<Map<String, Object>> getUserMenus(Long userId) {
        try {
            Set<String> userPermissions = getUserPermissionsOld(userId);
            
            // 获取所有启用的菜单权限并按排序序号排序
            List<Permission> menuPermissions = permissionRepository.findByStatusAndType(1, 1);
            
            // 构建菜单树
            Map<Long, Map<String, Object>> menuMap = new HashMap<>();
            List<Map<String, Object>> rootMenus = new ArrayList<>();
            
            // 第一遍：创建所有菜单节点
            for (Permission permission : menuPermissions) {
                if (userPermissions.contains(permission.getCode())) {
                    Map<String, Object> menu = new HashMap<>();
                    menu.put("id", permission.getId());
                    menu.put("name", permission.getName());
                    menu.put("code", permission.getCode());
                    menu.put("type", permission.getType());
                    menu.put("url", permission.getUrl());
                    menu.put("icon", permission.getIcon() != null ? permission.getIcon() : "default");
                    menu.put("sortOrder", permission.getSortOrder());
                    menu.put("children", new ArrayList<Map<String, Object>>());
                    menuMap.put(permission.getId(), menu);
                }
            }
            
            // 第二遍：构建树形结构
            for (Permission permission : menuPermissions) {
                if (userPermissions.contains(permission.getCode())) {
                    Map<String, Object> menuNode = menuMap.get(permission.getId());
                    Long parentId = permission.getParentId();
                    
                    if (parentId == null || parentId == 0) {
                        rootMenus.add(menuNode);
                    } else {
                        Map<String, Object> parentNode = menuMap.get(parentId);
                        if (parentNode != null) {
                            @SuppressWarnings("unchecked")
                            List<Map<String, Object>> children = (List<Map<String, Object>>) parentNode.get("children");
                            children.add(menuNode);
                        }
                    }
                }
            }
            
            // 对根菜单和子菜单按排序序号排序
            rootMenus.sort((a, b) -> {
                Integer aOrder = (Integer) a.get("sortOrder");
                Integer bOrder = (Integer) b.get("sortOrder");
                return aOrder.compareTo(bOrder);
            });
            
            for (Map<String, Object> menu : rootMenus) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> children = (List<Map<String, Object>>) menu.get("children");
                children.sort((a, b) -> {
                    Integer aOrder = (Integer) a.get("sortOrder");
                    Integer bOrder = (Integer) b.get("sortOrder");
                    return aOrder.compareTo(bOrder);
                });
            }
            
            return rootMenus;
        } catch (Exception e) {
            log.error("获取用户菜单失败: {}", e.getMessage(), e);
            throw new RuntimeException("获取用户菜单失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getUserButtons(Long userId) {
        try {
            Set<String> userPermissions = getUserPermissionsOld(userId);
            
            // 获取所有按钮权限
            List<Permission> buttonPermissions = permissionRepository.findByType(2);
            
            return buttonPermissions.stream()
                    .filter(permission -> userPermissions.contains(permission.getCode()))
                    .map(permission -> {
                        Map<String, Object> button = new HashMap<>();
                        button.put("id", permission.getId());
                        button.put("name", permission.getName());
                        button.put("code", permission.getCode());
                        button.put("type", permission.getType());
                        return button;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取用户按钮权限失败: {}", e.getMessage(), e);
            throw new RuntimeException("获取用户按钮权限失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getUserApis(Long userId) {
        try {
            Set<String> userPermissions = getUserPermissionsOld(userId);
            
            // 获取所有接口权限
            List<Permission> apiPermissions = permissionRepository.findByType(3);
            
            return apiPermissions.stream()
                    .filter(permission -> userPermissions.contains(permission.getCode()))
                    .map(permission -> {
                        Map<String, Object> api = new HashMap<>();
                        api.put("id", permission.getId());
                        api.put("name", permission.getName());
                        api.put("code", permission.getCode());
                        api.put("type", permission.getType());
                        api.put("url", permission.getUrl());
                        api.put("method", permission.getMethod());
                        return api;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取用户接口权限失败: {}", e.getMessage(), e);
            throw new RuntimeException("获取用户接口权限失败: " + e.getMessage());
        }
    }

    // ==================== 原有方法实现（保持兼容性） ====================

    @Override
    public boolean hasPermissionOld(Long userId, String permission) {
        return hasPermission(userId, permission).get("hasPermission").equals(true);
    }

    @Override
    public boolean hasRole(Long userId, String role) {
        Set<String> userRoles = getUserRolesOld(userId);
        return userRoles.contains(role);
    }

    @Override
    public Set<String> getUserPermissionsOld(Long userId) {
        // 获取用户的所有角色
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        if (userRoles.isEmpty()) {
            return Collections.emptySet();
        }
        
        // 获取用户所有角色的权限
        Set<Long> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toSet());
        
        List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleIdIn(new ArrayList<>(roleIds));
        Set<Long> permissionIds = rolePermissions.stream()
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toSet());
        
        // 获取权限代码
        List<Permission> permissions = permissionRepository.findByIdIn(new ArrayList<>(permissionIds));
        return permissions.stream()
                .map(Permission::getCode)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<String> getUserRolesOld(Long userId) {
        // 获取用户的所有角色
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        if (userRoles.isEmpty()) {
            return Collections.emptySet();
        }
        
        // 获取角色代码
        Set<Long> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toSet());
        
        List<Role> roles = roleRepository.findByIdIn(new ArrayList<>(roleIds));
        return roles.stream()
                .map(Role::getCode)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean assignRoleToUser(Long userId, Long roleId) {
        try {
            assignRolesToUser(userId, Collections.singletonList(roleId));
            return true;
        } catch (Exception e) {
            log.error("分配角色给用户失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean removeRoleFromUserOld(Long userId, Long roleId) {
        try {
            removeRoleFromUser(userId, roleId);
            return true;
        } catch (Exception e) {
            log.error("移除用户角色失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean assignPermissionToRole(Long roleId, Long permissionId) {
        try {
            assignPermissionsToRole(roleId, Collections.singletonList(permissionId));
            return true;
        } catch (Exception e) {
            log.error("分配权限给角色失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean removePermissionFromRoleOld(Long roleId, Long permissionId) {
        try {
            removePermissionFromRole(roleId, permissionId);
            return true;
        } catch (Exception e) {
            log.error("移除角色权限失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Set<String> getRolePermissionsOld(Long roleId) {
        try {
            Map<String, Object> rolePermissions = getRolePermissions(roleId);
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> permissions = (List<Map<String, Object>>) rolePermissions.get("permissions");
            return permissions.stream()
                    .map(perm -> (String) perm.get("code"))
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            log.error("获取角色权限失败: {}", e.getMessage(), e);
            return Collections.emptySet();
        }
    }

    @Override
    public List<String> getAllPermissionsOld() {
        try {
            List<Map<String, Object>> permissions = getAllPermissions();
            return permissions.stream()
                    .map(perm -> (String) perm.get("code"))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取所有权限失败: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> getAllRolesOld() {
        try {
            List<Map<String, Object>> roles = getAllRoles();
            return roles.stream()
                    .map(role -> (String) role.get("code"))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取所有角色失败: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
