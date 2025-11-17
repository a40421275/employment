package com.shera.framework.employment.employment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 安全配置类
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    /**
     * 配置密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 生成测试密码的BCrypt哈希值
        String testPassword = "qwe123!@#";
        String encodedPassword = encoder.encode(testPassword);
        System.out.println("测试密码 '" + testPassword + "' 的BCrypt哈希值: " + encodedPassword);
        
        return encoder;
    }
    
    /**
     * 配置安全过滤器链
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 禁用CSRF保护，便于API测试
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/users/register").permitAll() // 用户注册
                .requestMatchers("/api/users/login").permitAll() // 用户登录
                .requestMatchers("/api/users/login/wx").permitAll() // 微信登录
                .requestMatchers("/api/users/login/phone").permitAll() // 手机号登录
                .requestMatchers("/api/users/send-verification-code").permitAll() // 发送验证码
                .requestMatchers("/api/users/reset-password").permitAll() // 重置密码
                // 签名URL文件访问接口允许匿名访问
                .requestMatchers("/api/files/signed/**").permitAll()
                // Swagger UI 相关路径允许匿名访问
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/webjars/**", "/swagger-ui.html", "/").permitAll()
                .anyRequest().authenticated() // 其他所有请求都需要认证
            )
            .formLogin(form -> form.disable()) // 禁用表单登录
            .httpBasic(httpBasic -> httpBasic.disable()) // 禁用HTTP Basic认证
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // 添加JWT过滤器
        
        return http.build();
    }
}
