package com.shera.framework.employment.employment.modules.user.service.impl;

import com.shera.framework.employment.employment.modules.user.dto.UserDTO;
import com.shera.framework.employment.employment.modules.user.dto.UserProfileDTO;
import com.shera.framework.employment.employment.modules.user.dto.UserWithCompanyDTO;
import com.shera.framework.employment.employment.modules.user.entity.User;
import com.shera.framework.employment.employment.modules.user.entity.UserProfile;
import com.shera.framework.employment.employment.modules.message.service.EmailVerificationService;
import com.shera.framework.employment.employment.modules.user.repository.UserProfileRepository;
import com.shera.framework.employment.employment.modules.user.repository.UserRepository;
import com.shera.framework.employment.employment.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    
    @Override
    @Transactional
    public User register(UserDTO userDTO) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查手机号是否已存在
        if (userDTO.getPhone() != null && userRepository.existsByPhone(userDTO.getPhone())) {
            throw new RuntimeException("手机号已存在");
        }
        
        // 检查邮箱是否已存在
        if (userDTO.getEmail() != null && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setWxOpenid(userDTO.getWxOpenid());
        user.setWxUnionid(userDTO.getWxUnionid());
        user.setUserType(userDTO.getUserType());
        user.setStatus(userDTO.getStatus());
        user.setAuthStatus(userDTO.getAuthStatus());
        
        User savedUser = userRepository.save(user);
        
        // 创建用户资料
        UserProfile profile = new UserProfile();
        profile.setUserId(savedUser.getId());
        userProfileRepository.save(profile);
        
        log.info("用户注册成功: {}", savedUser.getUsername());
        return savedUser;
    }
    
    @Override
    public User login(String account, String password) {
        Optional<User> userOpt = userRepository.findByUsernameOrPhoneOrEmail(account);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }
        
        User user = userOpt.get();
        
        // 检查用户状态
        if (user.getStatus() == User.Status.DISABLED.getCode()) {
            throw new RuntimeException("用户已被禁用");
        }
        
        if (user.getStatus() == User.Status.BLACKLIST.getCode()) {
            throw new RuntimeException("用户已被加入黑名单");
        }
        
        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);
        
        log.info("用户登录成功: {}", user.getUsername());
        return user;
    }
    
    @Override
    public User wxLogin(String openid, String unionid) {
        Optional<User> userOpt = userRepository.findByWxOpenid(openid);
        if (userOpt.isEmpty()) {
            // 微信用户首次登录，自动注册
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername("wx_" + openid.substring(0, 8));
            userDTO.setWxOpenid(openid);
            userDTO.setWxUnionid(unionid);
            userDTO.setPassword(""); // 微信登录不需要密码
            return register(userDTO);
        }
        
        User user = userOpt.get();
        
        // 检查用户状态
        if (user.getStatus() == User.Status.DISABLED.getCode()) {
            throw new RuntimeException("用户已被禁用");
        }
        
        if (user.getStatus() == User.Status.BLACKLIST.getCode()) {
            throw new RuntimeException("用户已被加入黑名单");
        }
        
        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);
        
        log.info("微信用户登录成功: {}", user.getUsername());
        return user;
    }
    
    @Override
    public User phoneLogin(String phone, String verificationCode) {
        // 这里应该验证验证码，暂时简化处理
        if (!verifyCode(phone, verificationCode)) {
            throw new RuntimeException("验证码错误");
        }
        
        Optional<User> userOpt = userRepository.findByPhone(phone);
        if (userOpt.isEmpty()) {
            // 手机用户首次登录，自动注册
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername("phone_" + phone);
            userDTO.setPhone(phone);
            userDTO.setPassword(""); // 手机登录不需要密码
            return register(userDTO);
        }
        
        User user = userOpt.get();
        
        // 检查用户状态
        if (user.getStatus() == User.Status.DISABLED.getCode()) {
            throw new RuntimeException("用户已被禁用");
        }
        
        if (user.getStatus() == User.Status.BLACKLIST.getCode()) {
            throw new RuntimeException("用户已被加入黑名单");
        }
        
        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);
        
        log.info("手机用户登录成功: {}", user.getUsername());
        return user;
    }
    
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Override
    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }
    
    @Override
    public Optional<User> findByWxOpenid(String wxOpenid) {
        return userRepository.findByWxOpenid(wxOpenid);
    }

    // ========== 重复校验接口实现 ==========

    @Override
    public boolean checkUsernameExists(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return userRepository.existsByUsername(username.trim());
    }

    @Override
    public boolean checkPhoneExists(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return userRepository.existsByPhone(phone.trim());
    }

    @Override
    public boolean checkEmailExists(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return userRepository.existsByEmail(email.trim());
    }

    @Override
    public Map<String, Boolean> checkDuplicates(String username, String phone, String email) {
        Map<String, Boolean> result = new HashMap<>();
        
        // 检查用户名是否重复
        if (username != null && !username.trim().isEmpty()) {
            result.put("usernameExists", userRepository.existsByUsername(username.trim()));
        } else {
            result.put("usernameExists", false);
        }
        
        // 检查手机号是否重复
        if (phone != null && !phone.trim().isEmpty()) {
            result.put("phoneExists", userRepository.existsByPhone(phone.trim()));
        } else {
            result.put("phoneExists", false);
        }
        
        // 检查邮箱是否重复
        if (email != null && !email.trim().isEmpty()) {
            result.put("emailExists", userRepository.existsByEmail(email.trim()));
        } else {
            result.put("emailExists", false);
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public User updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 更新用户信息
        if (userDTO.getPhone() != null && !userDTO.getPhone().equals(user.getPhone())) {
            if (userRepository.existsByPhone(userDTO.getPhone())) {
                throw new RuntimeException("手机号已存在");
            }
            user.setPhone(userDTO.getPhone());
        }
        
        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                throw new RuntimeException("邮箱已存在");
            }
            user.setEmail(userDTO.getEmail());
        }
        
        // 更新用户类型
        if (userDTO.getUserType() != null) {
            user.setUserType(userDTO.getUserType());
        }
        
        // 更新所属企业ID
        if (userDTO.getCompanyId() != null) {
            user.setCompanyId(userDTO.getCompanyId());
        }
        
        return userRepository.save(user);
    }
    
    @Override
    public User updateStatus(Long id, Integer status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        user.setStatus(status);
        return userRepository.save(user);
    }
    
    @Override
    public User updateAuthStatus(Long id, Integer authStatus) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        user.setAuthStatus(authStatus);
        return userRepository.save(user);
    }
    
    @Override
    public Optional<UserProfile> getUserProfile(Long userId) {
        return userProfileRepository.findByUserId(userId);
    }

    @Override
    public Page<UserProfile> listUserProfiles(String city, String education, Integer minWorkYears, 
                                            Integer maxWorkYears, Double minExpectedSalary, 
                                            Double maxExpectedSalary, String skill, Pageable pageable) {
        return userProfileRepository.findProfilesByConditions(
                city, education, minWorkYears, maxWorkYears, 
                minExpectedSalary, maxExpectedSalary, skill, pageable);
    }
    
    @Override
    @Transactional
    public UserProfile updateUserProfile(Long userId, UserProfileDTO profileDTO) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("用户资料不存在"));
        
        // 更新用户资料
        if (profileDTO.getRealName() != null) {
            profile.setRealName(profileDTO.getRealName());
        }
        if (profileDTO.getGender() != null) {
            profile.setGender(profileDTO.getGender());
        }
        if (profileDTO.getBirthday() != null) {
            profile.setBirthday(profileDTO.getBirthday());
        }
        if (profileDTO.getAvatar() != null) {
            profile.setAvatar(profileDTO.getAvatar());
        }
        if (profileDTO.getEducation() != null) {
            profile.setEducation(profileDTO.getEducation());
        }
        if (profileDTO.getWorkYears() != null) {
            profile.setWorkYears(profileDTO.getWorkYears());
        }
        if (profileDTO.getCurrentSalary() != null) {
            profile.setCurrentSalary(profileDTO.getCurrentSalary());
        }
        if (profileDTO.getExpectedSalary() != null) {
            profile.setExpectedSalary(profileDTO.getExpectedSalary());
        }
        if (profileDTO.getCity() != null) {
            profile.setCity(profileDTO.getCity());
        }
        if (profileDTO.getSkills() != null) {
            profile.setSkills(profileDTO.getSkills());
        }
        if (profileDTO.getSelfIntro() != null) {
            profile.setSelfIntro(profileDTO.getSelfIntro());
        }
        
        return userProfileRepository.save(profile);
    }
    
    @Override
    public boolean realNameAuth(Long userId, String realName, String idCard, String idCardFront, String idCardBack) {
        // 这里应该调用第三方人脸识别和OCR服务
        // 暂时简化处理，直接设置为审核中状态
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        user.setAuthStatus(User.AuthStatus.PENDING.getCode());
        userRepository.save(user);
        
        // 更新用户资料中的实名信息
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("用户资料不存在"));
        
        profile.setRealName(realName);
        profile.setIdCardFront(idCardFront);
        profile.setIdCardBack(idCardBack);
        userProfileRepository.save(profile);
        
        log.info("用户实名认证申请提交: {}", userId);
        return true;
    }
    
    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        log.info("用户修改密码成功: {}", userId);
        return true;
    }
    
    @Override
    public boolean resetPassword(String phone, String verificationCode, String newPassword) {
        // 验证验证码
        if (!verifyCode(phone, verificationCode)) {
            throw new RuntimeException("验证码错误");
        }
        
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        log.info("用户重置密码成功: {}", phone);
        return true;
    }

    @Override
    @Transactional
    public boolean resetPasswordByEmail(String email, String verificationCode, String newPassword) {
        // 使用消息管理模块验证邮箱验证码
        if (!emailVerificationService.verifyCode(email, verificationCode, "reset_password")) {
            throw new RuntimeException("邮箱验证码错误或已过期");
        }
        
        // 根据邮箱查找用户
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("该邮箱未注册"));
        
        // 检查用户状态
        if (user.getStatus() == User.Status.DISABLED.getCode()) {
            throw new RuntimeException("用户已被禁用");
        }
        
        if (user.getStatus() == User.Status.BLACKLIST.getCode()) {
            throw new RuntimeException("用户已被加入黑名单");
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        // 清除验证码，确保一次性使用
        emailVerificationService.clearCode(email, "reset_password");
        
        log.info("用户通过邮箱重置密码成功: {}", email);
        return true;
    }
    
    @Override
    public Page<User> listUsers(Integer userType, Integer status, Integer authStatus, Pageable pageable) {
        // 根据条件进行查询
        if (userType != null || status != null || authStatus != null) {
            return userRepository.findByUserTypeAndStatusAndAuthStatus(userType, status, authStatus, pageable);
        } else {
            return userRepository.findAll(pageable);
        }
    }
    
    @Override
    public long countUsers(Integer userType, Integer status, Integer authStatus) {
        // 根据条件进行统计
        if (userType != null || status != null || authStatus != null) {
            return userRepository.countByUserTypeAndStatusAndAuthStatus(userType, status, authStatus);
        } else {
            return userRepository.count();
        }
    }
    
    @Override
    public Page<User> listUsers(Integer userType, Integer status, Integer authStatus, String keyword, Pageable pageable) {
        // 根据条件和关键词进行查询
        if (keyword != null && !keyword.trim().isEmpty()) {
            return userRepository.findByKeywordAndConditions(keyword.trim(), userType, status, authStatus, pageable);
        } else if (userType != null || status != null || authStatus != null) {
            return userRepository.findByUserTypeAndStatusAndAuthStatus(userType, status, authStatus, pageable);
        } else {
            return userRepository.findAll(pageable);
        }
    }
    
    @Override
    public Page<User> searchUsers(String keyword, Integer userType, Pageable pageable) {
        // 根据关键词搜索用户
        if (keyword != null && !keyword.trim().isEmpty()) {
            return userRepository.findByKeywordAndUserType(keyword.trim(), userType, pageable);
        } else if (userType != null) {
            return userRepository.findByUserType(userType, pageable);
        } else {
            return userRepository.findAll(pageable);
        }
    }
    
    @Override
    public Map<String, Object> getUserStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 模拟统计数据
        stats.put("totalUsers", userRepository.count());
        stats.put("todayNewUsers", 50L);
        stats.put("weekNewUsers", 350L);
        stats.put("monthNewUsers", 1000L);
        
        Map<String, Long> userTypeDistribution = new HashMap<>();
        userTypeDistribution.put("jobSeekers", 600L);
        userTypeDistribution.put("enterpriseUsers", 300L);
        userTypeDistribution.put("admins", 100L);
        stats.put("userTypeDistribution", userTypeDistribution);
        
        Map<String, Long> statusDistribution = new HashMap<>();
        statusDistribution.put("active", 850L);
        statusDistribution.put("inactive", 150L);
        stats.put("statusDistribution", statusDistribution);
        
        Map<String, Long> authDistribution = new HashMap<>();
        authDistribution.put("verified", 450L);
        authDistribution.put("unverified", 550L);
        stats.put("authDistribution", authDistribution);
        
        stats.put("dailyActiveUsers", 500L);
        stats.put("weeklyActiveUsers", 800L);
        stats.put("monthlyActiveUsers", 950L);
        stats.put("averageLoginFrequency", 3.5);
        stats.put("userRetentionRate", 0.85);
        
        return stats;
    }
    
    @Override
    @Transactional
    public Map<String, Object> batchUpdateStatus(List<Long> userIds, Integer status) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        List<Long> failedUserIds = new java.util.ArrayList<>();
        
        for (Long userId : userIds) {
            try {
                updateStatus(userId, status);
                successCount++;
            } catch (Exception e) {
                failedUserIds.add(userId);
            }
        }
        
        result.put("successCount", successCount);
        result.put("failedCount", failedUserIds.size());
        result.put("failedUserIds", failedUserIds);
        result.put("updatedStatus", status);
        result.put("updateTime", LocalDateTime.now());
        
        return result;
    }
    
    @Override
    @Transactional
    public Map<String, Object> batchDeleteUsers(List<Long> userIds) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        List<Long> failedUserIds = new java.util.ArrayList<>();
        
        for (Long userId : userIds) {
            try {
                deleteUser(userId);
                successCount++;
            } catch (Exception e) {
                failedUserIds.add(userId);
            }
        }
        
        result.put("successCount", successCount);
        result.put("failedCount", failedUserIds.size());
        result.put("failedUserIds", failedUserIds);
        result.put("deletedCount", successCount);
        result.put("deleteTime", LocalDateTime.now());
        
        return result;
    }
    
    @Override
    public Map<String, Object> getUserActivity(Long userId, int days) {
        Map<String, Object> activity = new HashMap<>();
        
        // 模拟用户活跃度数据
        activity.put("userId", userId);
        activity.put("username", "用户名");
        activity.put("totalLoginCount", 150);
        activity.put("lastLoginTime", LocalDateTime.now());
        activity.put("loginDays", 25);
        activity.put("averageDailyLogin", 6);
        activity.put("activityScore", 85);
        activity.put("activityLevel", "高活跃");
        
        // 模拟每日活动数据
        List<Map<String, Object>> dailyActivity = new java.util.ArrayList<>();
        for (int i = 0; i < days; i++) {
            Map<String, Object> dayActivity = new HashMap<>();
            dayActivity.put("date", LocalDateTime.now().minusDays(i).toLocalDate().toString());
            dayActivity.put("loginCount", 5 + i % 3);
            dayActivity.put("jobViews", 20 + i % 10);
            dayActivity.put("resumeUpdates", i % 2);
            dayActivity.put("jobApplies", 3 + i % 4);
            dailyActivity.add(dayActivity);
        }
        activity.put("dailyActivity", dailyActivity);
        
        // 模拟周活动数据
        Map<String, Object> weeklyActivity = new HashMap<>();
        weeklyActivity.put("loginCount", 42);
        weeklyActivity.put("jobViews", 150);
        weeklyActivity.put("resumeUpdates", 3);
        weeklyActivity.put("jobApplies", 15);
        activity.put("weeklyActivity", weeklyActivity);
        
        // 模拟月活动数据
        Map<String, Object> monthlyActivity = new HashMap<>();
        monthlyActivity.put("loginCount", 150);
        monthlyActivity.put("jobViews", 600);
        monthlyActivity.put("resumeUpdates", 12);
        monthlyActivity.put("jobApplies", 60);
        activity.put("monthlyActivity", monthlyActivity);
        
        return activity;
    }
    
    @Override
    public Map<String, Object> getUserBehaviorStats(Long userId, String startDate, String endDate) {
        Map<String, Object> behaviorStats = new HashMap<>();
        
        // 模拟用户行为统计数据
        behaviorStats.put("userId", userId);
        behaviorStats.put("username", "用户名");
        
        Map<String, String> period = new HashMap<>();
        period.put("startDate", startDate != null ? startDate : "2025-11-01");
        period.put("endDate", endDate != null ? endDate : "2025-11-08");
        behaviorStats.put("period", period);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalLogins", 42);
        stats.put("totalJobViews", 150);
        stats.put("totalJobApplies", 15);
        stats.put("totalResumeUpdates", 3);
        stats.put("totalMessages", 25);
        stats.put("totalNotifications", 10);
        stats.put("averageSessionDuration", 1800);
        stats.put("favoriteJobs", 8);
        stats.put("followedCompanies", 5);
        behaviorStats.put("behaviorStats", stats);
        
        // 模拟每日行为数据
        List<Map<String, Object>> dailyBehavior = new java.util.ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Map<String, Object> dayBehavior = new HashMap<>();
            dayBehavior.put("date", LocalDateTime.now().minusDays(i).toLocalDate().toString());
            dayBehavior.put("logins", 5 + i % 3);
            dayBehavior.put("jobViews", 20 + i % 10);
            dayBehavior.put("jobApplies", 3 + i % 4);
            dayBehavior.put("resumeUpdates", i % 2);
            dayBehavior.put("messages", 4 + i % 3);
            dayBehavior.put("notifications", 2 + i % 2);
            dailyBehavior.add(dayBehavior);
        }
        behaviorStats.put("dailyBehavior", dailyBehavior);
        
        // 模拟行为趋势
        Map<String, String> behaviorTrend = new HashMap<>();
        behaviorTrend.put("loginTrend", "上升");
        behaviorTrend.put("jobViewTrend", "稳定");
        behaviorTrend.put("jobApplyTrend", "上升");
        behaviorTrend.put("activityLevel", "高活跃");
        behaviorStats.put("behaviorTrend", behaviorTrend);
        
        return behaviorStats;
    }

    /**
     * 验证验证码
     * @param phone 手机号
     * @param code 验证码
     * @return 验证结果
     */
    private boolean verifyCode(String phone, String code) {
        // 这里应该调用短信验证服务
        // 暂时简化处理，假设验证码为"123456"
        return "123456".equals(code);
    }
    
    @Override
    @Transactional
    public boolean adminResetPassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        log.info("管理员重置用户密码成功: {}", userId);
        return true;
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        // 删除用户资料
        userProfileRepository.findByUserId(id).ifPresent(userProfileRepository::delete);
        
        // 删除用户
        userRepository.deleteById(id);
        
        log.info("删除用户: {}", id);
    }

    // ========== 投影查询方法实现（优化企业信息查询） ==========

    @Override
    public Optional<UserWithCompanyDTO> findUserWithCompanyById(Long userId) {
        return userRepository.findUserWithCompanyById(userId);
    }

    @Override
    public Page<UserWithCompanyDTO> listUsersWithCompany(Pageable pageable) {
        return userRepository.findUsersWithCompany(pageable);
    }

    @Override
    public Page<UserWithCompanyDTO> listUsersWithCompanyByUserType(Integer userType, Pageable pageable) {
        return userRepository.findUsersWithCompanyByUserType(userType, pageable);
    }

    @Override
    public Page<UserWithCompanyDTO> listUsersWithCompanyByConditions(Integer userType, Integer status, Integer authStatus, Pageable pageable) {
        return userRepository.findUsersWithCompanyByConditions(userType, status, authStatus, pageable);
    }

    @Override
    public Page<UserWithCompanyDTO> searchUsersWithCompany(String keyword, Integer userType, Pageable pageable) {
        return userRepository.searchUsersWithCompany(keyword, userType, pageable);
    }
}
