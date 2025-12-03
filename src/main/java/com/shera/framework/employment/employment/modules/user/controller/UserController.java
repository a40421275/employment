package com.shera.framework.employment.employment.modules.user.controller;

import com.shera.framework.employment.employment.modules.user.dto.ResetPasswordDTO;
import com.shera.framework.employment.employment.modules.user.dto.UserDTO;
import com.shera.framework.employment.employment.modules.user.dto.UserProfileDTO;
import com.shera.framework.employment.employment.modules.user.dto.UserWithCompanyDTO;
import com.shera.framework.employment.employment.modules.user.entity.User;
import com.shera.framework.employment.employment.modules.user.entity.UserProfile;
import com.shera.framework.employment.employment.modules.user.service.UserService;
import com.shera.framework.employment.employment.util.JwtUtil;
import com.shera.framework.employment.employment.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final JwtUtil jwtUtil;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        try {
            User user = userService.register(userDTO);
            return ResponseUtil.success("注册成功", user);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String account = loginRequest.get("account");
        String password = loginRequest.get("password");
        
        try {
            User user = userService.login(account, password);
            
            // 生成JWT token
            String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getUserType());
            
            // 创建符合API文档要求的响应格式
            Map<String, Object> loginResponse = new HashMap<>();
            loginResponse.put("token", token);
            
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("phone", user.getPhone());
            userInfo.put("email", user.getEmail());
            userInfo.put("userType", user.getUserType());
            userInfo.put("status", user.getStatus());
            userInfo.put("authStatus", user.getAuthStatus());
            
            loginResponse.put("user", userInfo);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "登录成功");
            response.put("data", loginResponse);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 400);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * 微信登录
     */
    @PostMapping("/login/wx")
    public ResponseEntity<?> wxLogin(@RequestBody Map<String, String> wxLoginRequest) {
        String openid = wxLoginRequest.get("openid");
        String unionid = wxLoginRequest.get("unionid");
        
        try {
            User user = userService.wxLogin(openid, unionid);
            return ResponseUtil.success("微信登录成功", user);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 手机号登录
     */
    @PostMapping("/login/phone")
    public ResponseEntity<?> phoneLogin(@RequestBody Map<String, String> phoneLoginRequest) {
        String phone = phoneLoginRequest.get("phone");
        String verificationCode = phoneLoginRequest.get("verificationCode");
        
        try {
            User user = userService.phoneLogin(phone, verificationCode);
            return ResponseUtil.success("手机登录成功", user);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取用户信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        Optional<UserWithCompanyDTO> user = userService.findUserWithCompanyById(id);
        if (user.isPresent()) {
            // 直接返回投影查询结果，包含企业信息
            return ResponseUtil.success("获取用户信息成功", user.get());
        } else {
            return ResponseUtil.error("用户不存在");
        }
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            User user = userService.updateUser(id, userDTO);
            return ResponseUtil.success("更新用户信息成功", user);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取用户资料
     */
    @GetMapping("/{id}/profile")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        Optional<UserProfile> profile = userService.getUserProfile(id);
        if (profile.isPresent()) {
            // 获取用户信息
            Optional<User> user = userService.findById(id);
            if (user.isEmpty()) {
                return ResponseUtil.error("用户不存在");
            }
            
            // 手动构建响应数据，避免Hibernate懒加载序列化问题
            UserProfile userProfile = profile.get();
            Map<String, Object> profileData = new HashMap<>();
            profileData.put("id", userProfile.getId());
            profileData.put("userId", userProfile.getUserId());
            
            // 添加用户账号信息
            User userInfo = user.get();
            profileData.put("username", userInfo.getUsername());
            profileData.put("phone", userInfo.getPhone());
            profileData.put("email", userInfo.getEmail());
            profileData.put("userType", userInfo.getUserType());
            profileData.put("status", userInfo.getStatus());
            profileData.put("authStatus", userInfo.getAuthStatus());
            
            // 用户资料信息
            profileData.put("realName", userProfile.getRealName());
            profileData.put("gender", userProfile.getGender());
            profileData.put("birthday", userProfile.getBirthday());
            profileData.put("avatar", userProfile.getAvatar());
            profileData.put("education", userProfile.getEducation());
            profileData.put("workYears", userProfile.getWorkYears());
            profileData.put("currentSalary", userProfile.getCurrentSalary());
            profileData.put("expectedSalary", userProfile.getExpectedSalary());
            profileData.put("city", userProfile.getCity());
            profileData.put("skills", userProfile.getSkills());
            profileData.put("selfIntro", userProfile.getSelfIntro());
            profileData.put("createTime", userProfile.getCreateTime());
            profileData.put("updateTime", userProfile.getUpdateTime());
            
            return ResponseUtil.success("获取用户资料成功", profileData);
        } else {
            return ResponseUtil.error("用户资料不存在");
        }
    }
    
    /**
     * 更新用户资料
     */
    @PutMapping("/{id}/profile")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long id, @RequestBody UserProfileDTO profileDTO) {
        try {
            UserProfile profile = userService.updateUserProfile(id, profileDTO);
            return ResponseUtil.success("更新用户资料成功", profile);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 实名认证
     */
    @PostMapping("/{id}/realname-auth")
    public ResponseEntity<?> realNameAuth(@PathVariable Long id, @RequestBody Map<String, String> authRequest) {
        String realName = authRequest.get("realName");
        String idCard = authRequest.get("idCard");
        String idCardFront = authRequest.get("idCardFront");
        String idCardBack = authRequest.get("idCardBack");
        
        try {
            boolean result = userService.realNameAuth(id, realName, idCard, idCardFront, idCardBack);
            return ResponseUtil.success("实名认证申请提交成功", result);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 修改密码
     */
    @PostMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody Map<String, String> passwordRequest) {
        String oldPassword = passwordRequest.get("oldPassword");
        String newPassword = passwordRequest.get("newPassword");
        
        try {
            boolean result = userService.changePassword(id, oldPassword, newPassword);
            return ResponseUtil.success("修改密码成功", result);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 重置密码（手机号方式）
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> resetRequest) {
        String phone = resetRequest.get("phone");
        String verificationCode = resetRequest.get("verificationCode");
        String newPassword = resetRequest.get("newPassword");

        try {
            boolean result = userService.resetPassword(phone, verificationCode, newPassword);
            return ResponseUtil.success("重置密码成功", result);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }

    /**
     * 重置密码（邮箱+验证码方式）
     */
    @PostMapping("/reset-password/email")
    public ResponseEntity<?> resetPasswordByEmail(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        try {
            // 验证邮箱格式
            if (resetPasswordDTO.getEmail() == null || resetPasswordDTO.getEmail().trim().isEmpty()) {
                return ResponseUtil.error("邮箱不能为空");
            }

            // 验证验证码格式
            if (resetPasswordDTO.getVerificationCode() == null || resetPasswordDTO.getVerificationCode().trim().isEmpty()) {
                return ResponseUtil.error("验证码不能为空");
            }

            // 验证密码格式
            if (resetPasswordDTO.getNewPassword() == null || resetPasswordDTO.getNewPassword().trim().isEmpty()) {
                return ResponseUtil.error("新密码不能为空");
            }

            boolean result = userService.resetPasswordByEmail(
                resetPasswordDTO.getEmail(),
                resetPasswordDTO.getVerificationCode(),
                resetPasswordDTO.getNewPassword()
            );
            return ResponseUtil.success("重置密码成功", result);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }

    /**
     * 管理员重置用户密码
     */
    @PostMapping("/{id}/admin-reset-password")
    public ResponseEntity<?> adminResetPassword(@PathVariable Long id, @RequestBody Map<String, String> passwordRequest) {
        String newPassword = passwordRequest.get("newPassword");

        try {
            boolean result = userService.adminResetPassword(id, newPassword);
            return ResponseUtil.success("管理员重置密码成功", result);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 分页查询用户列表
     */
    @GetMapping
    public ResponseEntity<?> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Integer userType,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer authStatus,
            @RequestParam(required = false) String keyword) {
        
        Pageable pageable = PageRequest.of(page, size);
        
        Page<UserWithCompanyDTO> users;
        if (keyword != null && !keyword.trim().isEmpty()) {
            // 使用投影查询搜索用户
            users = userService.searchUsersWithCompany(keyword.trim(), userType, pageable);
        } else if (userType != null || status != null || authStatus != null) {
            // 使用投影查询条件查询用户
            users = userService.listUsersWithCompanyByConditions(userType, status, authStatus, pageable);
        } else {
            // 使用投影查询所有用户
            users = userService.listUsersWithCompany(pageable);
        }
        
        return ResponseUtil.successPage(users, "查询成功");
    }
    
    /**
     * 分页查询用户资料列表（支持多条件筛选）
     */
    @GetMapping("/profiles")
    public ResponseEntity<?> listUserProfiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String education,
            @RequestParam(required = false) Integer minWorkYears,
            @RequestParam(required = false) Integer maxWorkYears,
            @RequestParam(required = false) Double minExpectedSalary,
            @RequestParam(required = false) Double maxExpectedSalary,
            @RequestParam(required = false) String skill) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<UserProfile> profiles = userService.listUserProfiles(
                city, education, minWorkYears, maxWorkYears, 
                minExpectedSalary, maxExpectedSalary, skill, pageable);
        
        // 添加筛选条件信息
        Map<String, Object> filters = new HashMap<>();
        filters.put("city", city);
        filters.put("education", education);
        filters.put("minWorkYears", minWorkYears);
        filters.put("maxWorkYears", maxWorkYears);
        filters.put("minExpectedSalary", minExpectedSalary);
        filters.put("maxExpectedSalary", maxExpectedSalary);
        filters.put("skill", skill);
        
        return ResponseUtil.successPage(profiles, "查询用户资料列表成功", filters);
    }
    
    /**
     * 搜索用户
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Integer userType) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<UserWithCompanyDTO> users = userService.searchUsersWithCompany(keyword, userType, pageable);
        
        return ResponseUtil.successPage(users, "搜索成功", keyword);
    }
    
    /**
     * 获取用户统计
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getUserStats() {
        try {
            Map<String, Object> stats = userService.getUserStats();
            return ResponseUtil.successStats(stats, "获取成功");
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 批量更新用户状态
     */
    @PutMapping("/batch-status")
    public ResponseEntity<?> batchUpdateStatus(@RequestBody Map<String, Object> batchRequest) {
        try {
            @SuppressWarnings("unchecked")
            java.util.List<Long> userIds = (java.util.List<Long>) batchRequest.get("userIds");
            Integer status = (Integer) batchRequest.get("status");
            
            Map<String, Object> result = userService.batchUpdateStatus(userIds, status);
            return ResponseUtil.success("批量更新成功", result);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 批量删除用户
     */
    @DeleteMapping("/batch")
    public ResponseEntity<?> batchDeleteUsers(@RequestBody Map<String, Object> batchRequest) {
        try {
            @SuppressWarnings("unchecked")
            java.util.List<Long> userIds = (java.util.List<Long>) batchRequest.get("userIds");
            
            Map<String, Object> result = userService.batchDeleteUsers(userIds);
            return ResponseUtil.success("批量删除成功", result);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取用户活跃度
     */
    @GetMapping("/{id}/activity")
    public ResponseEntity<?> getUserActivity(@PathVariable Long id, 
                                           @RequestParam(defaultValue = "30") int days) {
        try {
            Map<String, Object> activity = userService.getUserActivity(id, days);
            return ResponseUtil.success("获取成功", activity);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 获取用户行为统计
     */
    @GetMapping("/{id}/behavior-stats")
    public ResponseEntity<?> getUserBehaviorStats(@PathVariable Long id,
                                                @RequestParam(required = false) String startDate,
                                                @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> behaviorStats = userService.getUserBehaviorStats(id, startDate, endDate);
            return ResponseUtil.success("获取成功", behaviorStats);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }

    // ========== 重复校验接口 ==========

    /**
     * 检查用户名是否已存在
     */
    @GetMapping("/check/username")
    public ResponseEntity<?> checkUsernameExists(@RequestParam String username) {
        try {
            boolean exists = userService.checkUsernameExists(username);
            Map<String, Object> result = new HashMap<>();
            result.put("username", username);
            result.put("exists", exists);
            return ResponseUtil.success("检查成功", result);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }

    /**
     * 检查手机号是否已存在
     */
    @GetMapping("/check/phone")
    public ResponseEntity<?> checkPhoneExists(@RequestParam String phone) {
        try {
            boolean exists = userService.checkPhoneExists(phone);
            Map<String, Object> result = new HashMap<>();
            result.put("phone", phone);
            result.put("exists", exists);
            return ResponseUtil.success("检查成功", result);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }

    /**
     * 检查邮箱是否已存在
     */
    @GetMapping("/check/email")
    public ResponseEntity<?> checkEmailExists(@RequestParam String email) {
        try {
            boolean exists = userService.checkEmailExists(email);
            Map<String, Object> result = new HashMap<>();
            result.put("email", email);
            result.put("exists", exists);
            return ResponseUtil.success("检查成功", result);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }

    /**
     * 批量检查账号、手机号、邮箱是否已存在
     */
    @PostMapping("/check/duplicates")
    public ResponseEntity<?> checkDuplicates(@RequestBody Map<String, String> checkRequest) {
        try {
            String username = checkRequest.get("username");
            String phone = checkRequest.get("phone");
            String email = checkRequest.get("email");
            
            Map<String, Boolean> duplicates = userService.checkDuplicates(username, phone, email);
            
            Map<String, Object> result = new HashMap<>();
            result.put("username", username);
            result.put("phone", phone);
            result.put("email", email);
            result.put("duplicates", duplicates);
            
            return ResponseUtil.success("检查成功", result);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 更新用户状态
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> statusRequest) {
        Integer status = statusRequest.get("status");
        
        try {
            User user = userService.updateStatus(id, status);
            return ResponseUtil.success("更新用户状态成功", user);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 更新认证状态
     */
    @PutMapping("/{id}/auth-status")
    public ResponseEntity<?> updateAuthStatus(@PathVariable Long id, @RequestBody Map<String, Integer> authStatusRequest) {
        Integer authStatus = authStatusRequest.get("authStatus");
        
        try {
            User user = userService.updateAuthStatus(id, authStatus);
            return ResponseUtil.success("更新认证状态成功", user);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseUtil.success("删除用户成功", null);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
    
}
