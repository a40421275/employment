package com.shera.framework.employment.employment.config;

import com.shera.framework.employment.employment.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * JWT认证过滤器
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // 检查是否为无需认证的接口
        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // 从请求头中获取Token
        String token = getTokenFromRequest(request);
        
        if (token != null && jwtUtil.validateToken(token)) {
            // Token验证成功，设置认证信息
            Long userId = jwtUtil.getUserIdFromToken(token);
            String username = jwtUtil.getUsernameFromToken(token);
            Integer userType = jwtUtil.getUserTypeFromToken(token);
            
            // 根据用户类型设置权限
            List<SimpleGrantedAuthority> authorities = getAuthoritiesByUserType(userType);
            
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(userId, null, authorities);
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 将用户信息添加到请求属性中，便于后续使用
            request.setAttribute("userId", userId);
            request.setAttribute("username", username);
            request.setAttribute("userType", userType);
            
            filterChain.doFilter(request, response);
        } else {
            // Token验证失败，返回401未授权
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String jsonResponse = "{\"code\": 401, \"message\": \"未授权访问，请提供有效的Token\", \"data\": null}";
            response.getWriter().write(jsonResponse);
        }
    }
    
    /**
     * 从请求头中获取Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
    /**
     * 检查是否为公开接口（无需认证）
     */
    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        
        // 用户注册和登录相关接口
        if (path.equals("/api/users/register") && "POST".equalsIgnoreCase(method)) {
            return true;
        }
        if (path.equals("/api/users/login") && "POST".equalsIgnoreCase(method)) {
            return true;
        }
        if (path.equals("/api/users/login/wx") && "POST".equalsIgnoreCase(method)) {
            return true;
        }
        if (path.equals("/api/users/login/phone") && "POST".equalsIgnoreCase(method)) {
            return true;
        }
        if (path.equals("/api/users/send-verification-code") && "POST".equalsIgnoreCase(method)) {
            return true;
        }
        if (path.equals("/api/users/reset-password") && "POST".equalsIgnoreCase(method)) {
            return true;
        }
        
        // 签名URL文件访问接口
        if (path.startsWith("/api/files/signed/")) {
            return true;
        }
        
        // Swagger UI 相关路径
        if (path.startsWith("/swagger-ui") || 
            path.startsWith("/v3/api-docs") || 
            path.startsWith("/webjars") ||
            path.equals("/swagger-ui.html") ||
            path.equals("/") ||
            path.equals("/favicon.ico")) {
            return true;
        }
        
        return false;
    }
    
    /**
     * 根据用户类型获取权限
     */
    private List<SimpleGrantedAuthority> getAuthoritiesByUserType(Integer userType) {
        if (userType == null) {
            return Collections.emptyList();
        }
        
        switch (userType) {
            case 1: // 求职者
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_JOB_SEEKER"));
            case 2: // 企业用户
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_EMPLOYER"));
            case 3: // 管理员
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            default:
                return Collections.emptyList();
        }
    }
}
