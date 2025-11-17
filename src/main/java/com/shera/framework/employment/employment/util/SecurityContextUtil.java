package com.shera.framework.employment.employment.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 安全上下文工具类
 * 用于获取当前会话中的用户信息
 */
@Component
public class SecurityContextUtil {

    /**
     * 获取当前登录用户的ID
     * @return 用户ID，如果未登录返回null
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof Long) {
            return (Long) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * 获取当前登录用户的ID（Optional方式）
     * @return 用户ID的Optional对象
     */
    public static Optional<Long> getCurrentUserIdOptional() {
        return Optional.ofNullable(getCurrentUserId());
    }

    /**
     * 获取当前登录用户的ID，如果未登录则抛出异常
     * @return 用户ID
     * @throws IllegalStateException 如果用户未登录
     */
    public static Long getCurrentUserIdRequired() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new IllegalStateException("用户未登录");
        }
        return userId;
    }

    /**
     * 检查当前用户是否已登录
     * @return true表示已登录，false表示未登录
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof Long;
    }

    /**
     * 检查当前用户是否具有指定角色
     * @param role 角色名称，如 "ROLE_ADMIN", "ROLE_JOB_SEEKER", "ROLE_EMPLOYER"
     * @return true表示具有该角色，false表示不具有
     */
    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals(role));
        }
        return false;
    }

    /**
     * 检查当前用户是否是管理员
     * @return true表示是管理员，false表示不是
     */
    public static boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    /**
     * 检查当前用户是否是求职者
     * @return true表示是求职者，false表示不是
     */
    public static boolean isJobSeeker() {
        return hasRole("ROLE_JOB_SEEKER");
    }

    /**
     * 检查当前用户是否是企业用户
     * @return true表示是企业用户，false表示不是
     */
    public static boolean isEmployer() {
        return hasRole("ROLE_EMPLOYER");
    }

    /**
     * 从HttpServletRequest中获取用户ID
     * @param request HttpServletRequest对象
     * @return 用户ID，如果未登录返回null
     */
    public static Long getUserIdFromRequest(HttpServletRequest request) {
        Object userIdObj = request.getAttribute("userId");
        if (userIdObj instanceof Long) {
            return (Long) userIdObj;
        }
        return null;
    }

    /**
     * 从HttpServletRequest中获取用户名
     * @param request HttpServletRequest对象
     * @return 用户名，如果未登录返回null
     */
    public static String getUsernameFromRequest(HttpServletRequest request) {
        Object usernameObj = request.getAttribute("username");
        if (usernameObj instanceof String) {
            return (String) usernameObj;
        }
        return null;
    }

    /**
     * 从HttpServletRequest中获取用户类型
     * @param request HttpServletRequest对象
     * @return 用户类型，如果未登录返回null
     */
    public static Integer getUserTypeFromRequest(HttpServletRequest request) {
        Object userTypeObj = request.getAttribute("userType");
        if (userTypeObj instanceof Integer) {
            return (Integer) userTypeObj;
        }
        return null;
    }

    /**
     * 获取当前用户的完整信息
     * @return 包含用户ID、用户名、用户类型的Map，如果未登录返回null
     */
    public static UserInfo getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof Long) {
            Long userId = (Long) authentication.getPrincipal();
            String username = null;
            Integer userType = null;
            
            // 从认证详情中获取额外信息
            Object details = authentication.getDetails();
            if (details instanceof org.springframework.security.web.authentication.WebAuthenticationDetails) {
                // 这里可以扩展从其他来源获取用户信息
            }
            
            return new UserInfo(userId, username, userType);
        }
        return null;
    }

    /**
     * 获取当前用户的完整信息（从HttpServletRequest）
     * @param request HttpServletRequest对象
     * @return 包含用户ID、用户名、用户类型的UserInfo对象
     */
    public static UserInfo getUserInfoFromRequest(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        String username = getUsernameFromRequest(request);
        Integer userType = getUserTypeFromRequest(request);
        
        if (userId != null) {
            return new UserInfo(userId, username, userType);
        }
        return null;
    }

    /**
     * 用户信息封装类
     */
    public static class UserInfo {
        private final Long userId;
        private final String username;
        private final Integer userType;

        public UserInfo(Long userId, String username, Integer userType) {
            this.userId = userId;
            this.username = username;
            this.userType = userType;
        }

        public Long getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }

        public Integer getUserType() {
            return userType;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "userId=" + userId +
                    ", username='" + username + '\'' +
                    ", userType=" + userType +
                    '}';
        }
    }

    /**
     * 验证当前用户是否有权限访问指定资源
     * @param resourceOwnerId 资源所有者的用户ID
     * @return true表示有权限，false表示无权限
     */
    public static boolean hasPermission(Long resourceOwnerId) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return false;
        }
        
        // 管理员可以访问所有资源
        if (isAdmin()) {
            return true;
        }
        
        // 用户只能访问自己的资源
        return currentUserId.equals(resourceOwnerId);
    }

    /**
     * 验证当前用户是否有权限访问指定资源，如果无权限则抛出异常
     * @param resourceOwnerId 资源所有者的用户ID
     * @throws SecurityException 如果用户无权限访问该资源
     */
    public static void checkPermission(Long resourceOwnerId) {
        if (!hasPermission(resourceOwnerId)) {
            throw new SecurityException("无权限访问该资源");
        }
    }

    /**
     * 获取当前用户的角色列表
     * @return 角色列表，如果未登录返回空数组
     */
    public static String[] getCurrentUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .toArray(String[]::new);
        }
        return new String[0];
    }

    /**
     * 获取当前用户的认证信息
     * @return Authentication对象，如果未登录返回null
     */
    public static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 清除当前线程的安全上下文
     */
    public static void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
}
