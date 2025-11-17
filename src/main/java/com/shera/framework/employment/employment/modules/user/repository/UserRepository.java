package com.shera.framework.employment.employment.modules.user.repository;

import com.shera.framework.employment.employment.modules.user.dto.UserWithCompanyDTO;
import com.shera.framework.employment.employment.modules.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户Repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 根据手机号查找用户
     */
    Optional<User> findByPhone(String phone);
    
    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 根据微信openid查找用户
     */
    Optional<User> findByWxOpenid(String wxOpenid);
    
    /**
     * 根据微信unionid查找用户
     */
    Optional<User> findByWxUnionid(String wxUnionid);
    
    /**
     * 根据用户名或手机号或邮箱查找用户
     */
    @Query("SELECT u FROM User u WHERE u.username = :account OR u.phone = :account OR u.email = :account")
    Optional<User> findByUsernameOrPhoneOrEmail(@Param("account") String account);
    
    /**
     * 根据状态查找用户列表
     */
    List<User> findByStatus(Integer status);
    
    /**
     * 根据用户类型查找用户列表
     */
    List<User> findByUserType(Integer userType);
    
    /**
     * 根据认证状态查找用户列表
     */
    List<User> findByAuthStatus(Integer authStatus);
    
    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查手机号是否存在
     */
    boolean existsByPhone(String phone);
    
    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);
    
    /**
     * 检查微信openid是否存在
     */
    boolean existsByWxOpenid(String wxOpenid);
    
    /**
     * 根据状态统计用户数量
     */
    long countByStatus(Integer status);
    
    /**
     * 根据创建时间统计用户数量
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.createTime > :startTime")
    long countByCreateTimeAfter(@Param("startTime") java.time.LocalDateTime startTime);
    
    /**
     * 根据创建时间范围统计用户数量
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.createTime BETWEEN :startTime AND :endTime")
    long countByCreateTimeBetween(@Param("startTime") java.time.LocalDateTime startTime, @Param("endTime") java.time.LocalDateTime endTime);
    
    /**
     * 根据用户类型、状态、认证状态分页查询用户（包含企业信息）
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.company WHERE " +
           "(:userType IS NULL OR u.userType = :userType) AND " +
           "(:status IS NULL OR u.status = :status) AND " +
           "(:authStatus IS NULL OR u.authStatus = :authStatus)")
    org.springframework.data.domain.Page<User> findByUserTypeAndStatusAndAuthStatus(
            @Param("userType") Integer userType,
            @Param("status") Integer status,
            @Param("authStatus") Integer authStatus,
            org.springframework.data.domain.Pageable pageable);
    
    /**
     * 根据用户类型、状态、认证状态统计用户数量
     */
    @Query("SELECT COUNT(u) FROM User u WHERE " +
           "(:userType IS NULL OR u.userType = :userType) AND " +
           "(:status IS NULL OR u.status = :status) AND " +
           "(:authStatus IS NULL OR u.authStatus = :authStatus)")
    long countByUserTypeAndStatusAndAuthStatus(
            @Param("userType") Integer userType,
            @Param("status") Integer status,
            @Param("authStatus") Integer authStatus);
    
    /**
     * 根据关键词和条件分页查询用户
     */
    @Query("SELECT u FROM User u WHERE " +
           "(u.username LIKE %:keyword% OR u.phone LIKE %:keyword% OR u.email LIKE %:keyword%) AND " +
           "(:userType IS NULL OR u.userType = :userType) AND " +
           "(:status IS NULL OR u.status = :status) AND " +
           "(:authStatus IS NULL OR u.authStatus = :authStatus)")
    org.springframework.data.domain.Page<User> findByKeywordAndConditions(
            @Param("keyword") String keyword,
            @Param("userType") Integer userType,
            @Param("status") Integer status,
            @Param("authStatus") Integer authStatus,
            org.springframework.data.domain.Pageable pageable);
    
    /**
     * 根据关键词和用户类型分页查询用户
     */
    @Query("SELECT u FROM User u WHERE " +
           "(u.username LIKE %:keyword% OR u.phone LIKE %:keyword% OR u.email LIKE %:keyword%) AND " +
           "(:userType IS NULL OR u.userType = :userType)")
    org.springframework.data.domain.Page<User> findByKeywordAndUserType(
            @Param("keyword") String keyword,
            @Param("userType") Integer userType,
            org.springframework.data.domain.Pageable pageable);
    
    /**
     * 根据用户类型分页查询用户
     */
    org.springframework.data.domain.Page<User> findByUserType(Integer userType, org.springframework.data.domain.Pageable pageable);

    // ========== 投影查询方法（优化企业信息查询） ==========

    /**
     * 根据用户ID查询用户与企业信息
     */
    @Query("SELECT new com.shera.framework.employment.employment.modules.user.dto.UserWithCompanyDTO(" +
           "u.id, u.username, u.phone, u.email, u.userType, u.status, u.authStatus, " +
           "u.companyId, c.companyName, u.lastLoginTime, u.createTime, u.updateTime) " +
           "FROM User u LEFT JOIN Company c ON u.companyId = c.id " +
           "WHERE u.id = :userId")
    Optional<UserWithCompanyDTO> findUserWithCompanyById(@Param("userId") Long userId);

    /**
     * 分页查询用户列表（包含企业信息）
     */
    @Query("SELECT new com.shera.framework.employment.employment.modules.user.dto.UserWithCompanyDTO(" +
           "u.id, u.username, u.phone, u.email, u.userType, u.status, u.authStatus, " +
           "u.companyId, c.companyName, u.lastLoginTime, u.createTime, u.updateTime) " +
           "FROM User u LEFT JOIN Company c ON u.companyId = c.id")
    Page<UserWithCompanyDTO> findUsersWithCompany(Pageable pageable);

    /**
     * 根据用户类型分页查询用户列表（包含企业信息）
     */
    @Query("SELECT new com.shera.framework.employment.employment.modules.user.dto.UserWithCompanyDTO(" +
           "u.id, u.username, u.phone, u.email, u.userType, u.status, u.authStatus, " +
           "u.companyId, c.companyName, u.lastLoginTime, u.createTime, u.updateTime) " +
           "FROM User u LEFT JOIN Company c ON u.companyId = c.id " +
           "WHERE (:userType IS NULL OR u.userType = :userType)")
    Page<UserWithCompanyDTO> findUsersWithCompanyByUserType(@Param("userType") Integer userType, Pageable pageable);

    /**
     * 根据用户类型、状态、认证状态分页查询用户列表（包含企业信息）
     */
    @Query("SELECT new com.shera.framework.employment.employment.modules.user.dto.UserWithCompanyDTO(" +
           "u.id, u.username, u.phone, u.email, u.userType, u.status, u.authStatus, " +
           "u.companyId, c.companyName, u.lastLoginTime, u.createTime, u.updateTime) " +
           "FROM User u LEFT JOIN Company c ON u.companyId = c.id " +
           "WHERE (:userType IS NULL OR u.userType = :userType) AND " +
           "(:status IS NULL OR u.status = :status) AND " +
           "(:authStatus IS NULL OR u.authStatus = :authStatus)")
    Page<UserWithCompanyDTO> findUsersWithCompanyByConditions(@Param("userType") Integer userType,
                                                             @Param("status") Integer status,
                                                             @Param("authStatus") Integer authStatus,
                                                             Pageable pageable);

    /**
     * 根据关键词搜索用户（包含企业信息）
     */
    @Query("SELECT new com.shera.framework.employment.employment.modules.user.dto.UserWithCompanyDTO(" +
           "u.id, u.username, u.phone, u.email, u.userType, u.status, u.authStatus, " +
           "u.companyId, c.companyName, u.lastLoginTime, u.createTime, u.updateTime) " +
           "FROM User u LEFT JOIN Company c ON u.companyId = c.id " +
           "WHERE (u.username LIKE %:keyword% OR u.phone LIKE %:keyword% OR u.email LIKE %:keyword%) AND " +
           "(:userType IS NULL OR u.userType = :userType)")
    Page<UserWithCompanyDTO> searchUsersWithCompany(@Param("keyword") String keyword,
                                                   @Param("userType") Integer userType,
                                                   Pageable pageable);
}
