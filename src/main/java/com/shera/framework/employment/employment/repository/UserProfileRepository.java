package com.shera.framework.employment.employment.repository;

import com.shera.framework.employment.employment.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户资料Repository
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    
    /**
     * 根据用户ID查找用户资料
     */
    Optional<UserProfile> findByUserId(Long userId);
    
    /**
     * 根据城市查找用户资料列表
     */
    List<UserProfile> findByCity(String city);
    
    /**
     * 根据学历查找用户资料列表
     */
    List<UserProfile> findByEducation(String education);
    
    /**
     * 根据工作年限范围查找用户资料列表
     */
    @Query("SELECT up FROM UserProfile up WHERE up.workYears BETWEEN :minYears AND :maxYears")
    List<UserProfile> findByWorkYearsBetween(@Param("minYears") Integer minYears, @Param("maxYears") Integer maxYears);
    
    /**
     * 根据期望薪资范围查找用户资料列表
     */
    @Query("SELECT up FROM UserProfile up WHERE up.expectedSalary BETWEEN :minSalary AND :maxSalary")
    List<UserProfile> findByExpectedSalaryBetween(@Param("minSalary") Double minSalary, @Param("maxSalary") Double maxSalary);
    
    /**
     * 根据技能关键词查找用户资料列表
     */
    @Query("SELECT up FROM UserProfile up WHERE up.skills LIKE %:skill%")
    List<UserProfile> findBySkillsContaining(@Param("skill") String skill);
    
    /**
     * 分页查询用户资料列表（支持多条件筛选，只返回求职者类型的用户资料）
     */
    @Query("SELECT up FROM UserProfile up JOIN User u ON up.userId = u.id WHERE " +
           "u.userType = 1 AND " +  // 只查询求职者类型的用户
           "(:city IS NULL OR up.city = :city) AND " +
           "(:education IS NULL OR up.education = :education) AND " +
           "(:minWorkYears IS NULL OR up.workYears >= :minWorkYears) AND " +
           "(:maxWorkYears IS NULL OR up.workYears <= :maxWorkYears) AND " +
           "(:minExpectedSalary IS NULL OR up.expectedSalary >= :minExpectedSalary) AND " +
           "(:maxExpectedSalary IS NULL OR up.expectedSalary <= :maxExpectedSalary) AND " +
           "(:skill IS NULL OR up.skills LIKE %:skill%)")
    Page<UserProfile> findProfilesByConditions(
            @Param("city") String city,
            @Param("education") String education,
            @Param("minWorkYears") Integer minWorkYears,
            @Param("maxWorkYears") Integer maxWorkYears,
            @Param("minExpectedSalary") Double minExpectedSalary,
            @Param("maxExpectedSalary") Double maxExpectedSalary,
            @Param("skill") String skill,
            Pageable pageable);
    
    /**
     * 检查用户资料是否存在
     */
    boolean existsByUserId(Long userId);
}
