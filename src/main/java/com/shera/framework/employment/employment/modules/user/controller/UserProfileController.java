package com.shera.framework.employment.employment.modules.user.controller;

import com.shera.framework.employment.employment.modules.user.dto.UserProfileDetailDTO;
import com.shera.framework.employment.employment.modules.user.dto.UserProfileCreateDTO;
import com.shera.framework.employment.employment.modules.user.dto.UserProfileUpdateDTO;
import com.shera.framework.employment.employment.modules.user.entity.UserProfile;
import com.shera.framework.employment.employment.modules.user.service.UserProfileService;
import com.shera.framework.employment.employment.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户资料控制器
 */
@RestController
@RequestMapping("/api/user-profiles")
public class UserProfileController {
    
    @Autowired
    private UserProfileService userProfileService;
    
    /**
     * 创建用户资料
     */
    @PostMapping("/{userId}")
    public ResponseEntity<?> createUserProfile(@PathVariable Long userId, 
                                             @RequestBody UserProfileCreateDTO userProfileCreateDTO) {
        try {
            UserProfile userProfile = userProfileService.createUserProfile(userId, userProfileCreateDTO);
            return ResponseUtil.success("创建用户资料成功", userProfile);
        } catch (Exception e) {
            return ResponseUtil.error("创建用户资料失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户资料详情
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfileDetail(@PathVariable Long userId) {
        try {
            UserProfileDetailDTO userProfileDetail = userProfileService.getUserProfileDetail(userId);
            return ResponseUtil.success("获取用户资料详情成功", userProfileDetail);
        } catch (Exception e) {
            return ResponseUtil.error("获取用户资料详情失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新用户资料
     */
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long userId, 
                                             @RequestBody UserProfileUpdateDTO userProfileUpdateDTO) {
        try {
            UserProfile userProfile = userProfileService.updateUserProfile(userId, userProfileUpdateDTO);
            return ResponseUtil.success("更新用户资料成功", userProfile);
        } catch (Exception e) {
            return ResponseUtil.error("更新用户资料失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新技能标签
     */
    @PutMapping("/{userId}/skills")
    public ResponseEntity<?> updateSkills(@PathVariable Long userId, 
                                        @RequestBody List<String> skills) {
        try {
            UserProfile userProfile = userProfileService.updateSkills(userId, skills);
            return ResponseUtil.success("更新技能标签成功", userProfile);
        } catch (Exception e) {
            return ResponseUtil.error("更新技能标签失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新求职偏好
     */
    @PutMapping("/{userId}/job-preferences")
    public ResponseEntity<?> updateJobPreferences(@PathVariable Long userId,
                                                @RequestBody Map<String, Object> preferences) {
        try {
            @SuppressWarnings("unchecked")
            List<String> preferredCities = (List<String>) preferences.get("preferredCities");
            @SuppressWarnings("unchecked")
            List<String> jobTypes = (List<String>) preferences.get("jobTypes");
            @SuppressWarnings("unchecked")
            List<String> industries = (List<String>) preferences.get("industries");
            String workMode = (String) preferences.get("workMode");
            String jobStatus = (String) preferences.get("jobStatus");
            
            UserProfile userProfile = userProfileService.updateJobPreferences(
                userId, preferredCities, jobTypes, industries, workMode, jobStatus);
            return ResponseUtil.success("更新求职偏好成功", userProfile);
        } catch (Exception e) {
            return ResponseUtil.error("更新求职偏好失败: " + e.getMessage());
        }
    }
    
    /**
     * 分页查询用户资料列表
     */
    @GetMapping
    public ResponseEntity<?> getUserProfiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String education,
            @RequestParam(required = false) Integer minWorkYears,
            @RequestParam(required = false) Integer maxWorkYears,
            @RequestParam(required = false) Double minExpectedSalary,
            @RequestParam(required = false) Double maxExpectedSalary,
            @RequestParam(required = false) String skill) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
            Page<UserProfileDetailDTO> userProfiles = userProfileService.getUserProfiles(
                pageable, city, education, minWorkYears, maxWorkYears, 
                minExpectedSalary, maxExpectedSalary, skill);
            return ResponseUtil.success("查询用户资料列表成功", userProfiles);
        } catch (Exception e) {
            return ResponseUtil.error("查询用户资料列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据技能搜索用户资料
     */
    @PostMapping("/search/skills")
    public ResponseEntity<?> searchBySkills(@RequestBody List<String> skills,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<UserProfileDetailDTO> userProfiles = userProfileService.searchBySkills(skills, pageable);
            return ResponseUtil.success("根据技能搜索用户资料成功", userProfiles);
        } catch (Exception e) {
            return ResponseUtil.error("根据技能搜索用户资料失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据城市搜索用户资料
     */
    @GetMapping("/search/city/{city}")
    public ResponseEntity<?> searchByCity(@PathVariable String city,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "20") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<UserProfileDetailDTO> userProfiles = userProfileService.searchByCity(city, pageable);
            return ResponseUtil.success("根据城市搜索用户资料成功", userProfiles);
        } catch (Exception e) {
            return ResponseUtil.error("根据城市搜索用户资料失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据工作年限搜索用户资料
     */
    @GetMapping("/search/work-years")
    public ResponseEntity<?> searchByWorkYears(@RequestParam(required = false) Integer minYears,
                                             @RequestParam(required = false) Integer maxYears,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<UserProfileDetailDTO> userProfiles = userProfileService.searchByWorkYears(minYears, maxYears, pageable);
            return ResponseUtil.success("根据工作年限搜索用户资料成功", userProfiles);
        } catch (Exception e) {
            return ResponseUtil.error("根据工作年限搜索用户资料失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据期望薪资搜索用户资料
     */
    @GetMapping("/search/expected-salary")
    public ResponseEntity<?> searchByExpectedSalary(@RequestParam(required = false) Double minSalary,
                                                  @RequestParam(required = false) Double maxSalary,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "20") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<UserProfileDetailDTO> userProfiles = userProfileService.searchByExpectedSalary(minSalary, maxSalary, pageable);
            return ResponseUtil.success("根据期望薪资搜索用户资料成功", userProfiles);
        } catch (Exception e) {
            return ResponseUtil.error("根据期望薪资搜索用户资料失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户资料统计
     */
    @GetMapping("/{userId}/stats")
    public ResponseEntity<?> getUserProfileStats(@PathVariable Long userId) {
        try {
            Map<String, Object> stats = userProfileService.getUserProfileStats(userId);
            return ResponseUtil.success("获取用户资料统计成功", stats);
        } catch (Exception e) {
            return ResponseUtil.error("获取用户资料统计失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取热门技能标签
     */
    @GetMapping("/popular-skills")
    public ResponseEntity<?> getPopularSkills(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<Map<String, Object>> popularSkills = userProfileService.getPopularSkills(limit);
            return ResponseUtil.success("获取热门技能标签成功", popularSkills);
        } catch (Exception e) {
            return ResponseUtil.error("获取热门技能标签失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取城市分布统计
     */
    @GetMapping("/city-distribution")
    public ResponseEntity<?> getCityDistribution() {
        try {
            List<Map<String, Object>> cityDistribution = userProfileService.getCityDistribution();
            return ResponseUtil.success("获取城市分布统计成功", cityDistribution);
        } catch (Exception e) {
            return ResponseUtil.error("获取城市分布统计失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取工作年限分布统计
     */
    @GetMapping("/work-years-distribution")
    public ResponseEntity<?> getWorkYearsDistribution() {
        try {
            List<Map<String, Object>> workYearsDistribution = userProfileService.getWorkYearsDistribution();
            return ResponseUtil.success("获取工作年限分布统计成功", workYearsDistribution);
        } catch (Exception e) {
            return ResponseUtil.error("获取工作年限分布统计失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取期望薪资分布统计
     */
    @GetMapping("/expected-salary-distribution")
    public ResponseEntity<?> getExpectedSalaryDistribution() {
        try {
            List<Map<String, Object>> salaryDistribution = userProfileService.getExpectedSalaryDistribution();
            return ResponseUtil.success("获取期望薪资分布统计成功", salaryDistribution);
        } catch (Exception e) {
            return ResponseUtil.error("获取期望薪资分布统计失败: " + e.getMessage());
        }
    }
    
    /**
     * 增加投递次数
     */
    @PostMapping("/{userId}/increment-apply")
    public ResponseEntity<?> incrementJobApplyCount(@PathVariable Long userId) {
        try {
            userProfileService.incrementJobApplyCount(userId);
            return ResponseUtil.success("增加投递次数成功");
        } catch (Exception e) {
            return ResponseUtil.error("增加投递次数失败: " + e.getMessage());
        }
    }
    
    /**
     * 增加面试次数
     */
    @PostMapping("/{userId}/increment-interview")
    public ResponseEntity<?> incrementInterviewCount(@PathVariable Long userId) {
        try {
            userProfileService.incrementInterviewCount(userId);
            return ResponseUtil.success("增加面试次数成功");
        } catch (Exception e) {
            return ResponseUtil.error("增加面试次数失败: " + e.getMessage());
        }
    }
    
    /**
     * 增加offer数
     */
    @PostMapping("/{userId}/increment-offer")
    public ResponseEntity<?> incrementOfferCount(@PathVariable Long userId) {
        try {
            userProfileService.incrementOfferCount(userId);
            return ResponseUtil.success("增加offer数成功");
        } catch (Exception e) {
            return ResponseUtil.error("增加offer数失败: " + e.getMessage());
        }
    }
}
