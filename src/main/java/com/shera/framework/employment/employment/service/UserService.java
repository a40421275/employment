package com.shera.framework.employment.employment.service;

import com.shera.framework.employment.employment.dto.UserDTO;
import com.shera.framework.employment.employment.dto.UserProfileDTO;
import com.shera.framework.employment.employment.dto.UserWithCompanyDTO;
import com.shera.framework.employment.employment.entity.User;
import com.shera.framework.employment.employment.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户注册
     */
    User register(UserDTO userDTO);
    
    /**
     * 用户登录
     */
    User login(String account, String password);
    
    /**
     * 微信登录
     */
    User wxLogin(String openid, String unionid);
    
    /**
     * 手机号登录
     */
    User phoneLogin(String phone, String verificationCode);
    
    /**
     * 根据ID查找用户
     */
    Optional<User> findById(Long id);
    
    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 根据手机号查找用户
     */
    Optional<User> findByPhone(String phone);
    
    /**
     * 根据微信openid查找用户
     */
    Optional<User> findByWxOpenid(String wxOpenid);
    
    /**
     * 更新用户信息
     */
    User updateUser(Long id, UserDTO userDTO);
    
    /**
     * 更新用户状态
     */
    User updateStatus(Long id, Integer status);
    
    /**
     * 更新认证状态
     */
    User updateAuthStatus(Long id, Integer authStatus);
    
    /**
     * 获取用户资料
     */
    Optional<UserProfile> getUserProfile(Long userId);
    
    /**
     * 更新用户资料
     */
    UserProfile updateUserProfile(Long userId, UserProfileDTO profileDTO);
    
    /**
     * 分页查询用户资料列表（支持多条件筛选）
     */
    Page<UserProfile> listUserProfiles(String city, String education, Integer minWorkYears, 
                                     Integer maxWorkYears, Double minExpectedSalary, 
                                     Double maxExpectedSalary, String skill, Pageable pageable);
    
    /**
     * 实名认证
     */
    boolean realNameAuth(Long userId, String realName, String idCard, String idCardFront, String idCardBack);
    
    /**
     * 修改密码
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);
    
    /**
     * 重置密码
     */
    boolean resetPassword(String phone, String verificationCode, String newPassword);
    
    /**
     * 发送验证码
     */
    boolean sendVerificationCode(String phone);
    
    /**
     * 验证验证码
     */
    boolean verifyCode(String phone, String code);
    
    /**
     * 分页查询用户列表
     */
    Page<User> listUsers(Integer userType, Integer status, Integer authStatus, Pageable pageable);
    
    /**
     * 分页查询用户列表（带搜索）
     */
    Page<User> listUsers(Integer userType, Integer status, Integer authStatus, String keyword, Pageable pageable);
    
    /**
     * 搜索用户
     */
    Page<User> searchUsers(String keyword, Integer userType, Pageable pageable);
    
    /**
     * 获取用户统计
     */
    Map<String, Object> getUserStats();
    
    /**
     * 批量更新用户状态
     */
    Map<String, Object> batchUpdateStatus(List<Long> userIds, Integer status);
    
    /**
     * 批量删除用户
     */
    Map<String, Object> batchDeleteUsers(List<Long> userIds);
    
    /**
     * 获取用户活跃度
     */
    Map<String, Object> getUserActivity(Long userId, int days);
    
    /**
     * 获取用户行为统计
     */
    Map<String, Object> getUserBehaviorStats(Long userId, String startDate, String endDate);
    
    /**
     * 统计用户数量
     */
    long countUsers(Integer userType, Integer status, Integer authStatus);
    
    /**
     * 管理员重置用户密码
     */
    boolean adminResetPassword(Long userId, String newPassword);

    /**
     * 删除用户
     */
    void deleteUser(Long id);

    // ========== 投影查询方法（优化企业信息查询） ==========

    /**
     * 根据用户ID查询用户与企业信息
     */
    Optional<UserWithCompanyDTO> findUserWithCompanyById(Long userId);

    /**
     * 分页查询用户列表（包含企业信息）
     */
    Page<UserWithCompanyDTO> listUsersWithCompany(Pageable pageable);

    /**
     * 根据用户类型分页查询用户列表（包含企业信息）
     */
    Page<UserWithCompanyDTO> listUsersWithCompanyByUserType(Integer userType, Pageable pageable);

    /**
     * 根据用户类型、状态、认证状态分页查询用户列表（包含企业信息）
     */
    Page<UserWithCompanyDTO> listUsersWithCompanyByConditions(Integer userType, Integer status, Integer authStatus, Pageable pageable);

    /**
     * 根据关键词搜索用户（包含企业信息）
     */
    Page<UserWithCompanyDTO> searchUsersWithCompany(String keyword, Integer userType, Pageable pageable);
}
