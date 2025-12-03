package com.shera.framework.employment.employment.modules.user.service;

import com.shera.framework.employment.employment.modules.user.dto.UserProfileDetailDTO;
import com.shera.framework.employment.employment.modules.user.dto.UserProfileCreateDTO;
import com.shera.framework.employment.employment.modules.user.dto.UserProfileUpdateDTO;
import com.shera.framework.employment.employment.modules.user.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 用户资料服务接口
 */
public interface UserProfileService {
    
    /**
     * 创建用户资料
     */
    UserProfile createUserProfile(Long userId, UserProfileCreateDTO userProfileCreateDTO);
    
    /**
     * 更新用户资料
     */
    UserProfile updateUserProfile(Long userId, UserProfileUpdateDTO userProfileUpdateDTO);
    
    /**
     * 获取用户资料详情
     */
    UserProfileDetailDTO getUserProfileDetail(Long userId);
    
    /**
     * 获取用户资料
     */
    UserProfile getUserProfile(Long userId);
    
    /**
     * 更新技能标签
     */
    UserProfile updateSkills(Long userId, List<String> skills);
    
    /**
     * 更新求职偏好
     */
    UserProfile updateJobPreferences(Long userId, 
                                   List<String> preferredCities,
                                   List<String> jobTypes,
                                   List<String> industries,
                                   String workMode,
                                   String jobStatus);
    
    /**
     * 分页查询用户资料列表
     */
    Page<UserProfileDetailDTO> getUserProfiles(Pageable pageable, 
                                             String city,
                                             String education,
                                             Integer minWorkYears,
                                             Integer maxWorkYears,
                                             Double minExpectedSalary,
                                             Double maxExpectedSalary,
                                             String skill);
    
    /**
     * 根据技能搜索用户资料
     */
    Page<UserProfileDetailDTO> searchBySkills(List<String> skills, Pageable pageable);
    
    /**
     * 根据城市搜索用户资料
     */
    Page<UserProfileDetailDTO> searchByCity(String city, Pageable pageable);
    
    /**
     * 根据工作年限搜索用户资料
     */
    Page<UserProfileDetailDTO> searchByWorkYears(Integer minYears, Integer maxYears, Pageable pageable);
    
    /**
     * 根据期望薪资搜索用户资料
     */
    Page<UserProfileDetailDTO> searchByExpectedSalary(Double minSalary, Double maxSalary, Pageable pageable);
    
    /**
     * 获取用户资料统计
     */
    Map<String, Object> getUserProfileStats(Long userId);
    
    /**
     * 增加投递次数
     */
    void incrementJobApplyCount(Long userId);
    
    /**
     * 增加面试次数
     */
    void incrementInterviewCount(Long userId);
    
    /**
     * 增加offer数
     */
    void incrementOfferCount(Long userId);
    
    /**
     * 更新简历数量
     */
    void updateResumeCount(Long userId, Integer count);
    
    /**
     * 获取热门技能标签
     */
    List<Map<String, Object>> getPopularSkills(Integer limit);
    
    /**
     * 获取城市分布统计
     */
    List<Map<String, Object>> getCityDistribution();
    
    /**
     * 获取工作年限分布统计
     */
    List<Map<String, Object>> getWorkYearsDistribution();
    
    /**
     * 获取期望薪资分布统计
     */
    List<Map<String, Object>> getExpectedSalaryDistribution();
}
