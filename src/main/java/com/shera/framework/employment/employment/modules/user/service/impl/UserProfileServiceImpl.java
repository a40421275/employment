package com.shera.framework.employment.employment.modules.user.service.impl;

import com.shera.framework.employment.employment.modules.user.dto.UserProfileDetailDTO;
import com.shera.framework.employment.employment.modules.user.dto.UserProfileCreateDTO;
import com.shera.framework.employment.employment.modules.user.dto.UserProfileUpdateDTO;
import com.shera.framework.employment.employment.modules.user.entity.User;
import com.shera.framework.employment.employment.modules.user.entity.UserProfile;
import com.shera.framework.employment.employment.modules.user.repository.UserProfileRepository;
import com.shera.framework.employment.employment.modules.user.repository.UserRepository;
import com.shera.framework.employment.employment.modules.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户资料服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    
    @Override
    @Transactional
    public UserProfile createUserProfile(Long userId, UserProfileCreateDTO userProfileCreateDTO) {
        // 检查用户是否存在
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在: " + userId));
        
        // 检查用户资料是否已存在
        if (userProfileRepository.existsByUserId(userId)) {
            throw new IllegalStateException("用户资料已存在: " + userId);
        }
        
        // 验证必填字段
        if (userProfileCreateDTO.getRealName() == null || userProfileCreateDTO.getRealName().trim().isEmpty()) {
            throw new IllegalArgumentException("真实姓名为必填项");
        }
        
        // 创建用户资料
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(userId);
        copyUserProfileProperties(userProfile, userProfileCreateDTO);
        
        log.info("创建用户资料成功: userId={}, realName={}", userId, userProfileCreateDTO.getRealName());
        return userProfileRepository.save(userProfile);
    }
    
    @Override
    @Transactional
    public UserProfile updateUserProfile(Long userId, UserProfileUpdateDTO userProfileUpdateDTO) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户资料不存在: " + userId));
        
        // 验证必填字段
        if (userProfileUpdateDTO.getRealName() != null && userProfileUpdateDTO.getRealName().trim().isEmpty()) {
            throw new IllegalArgumentException("真实姓名不能为空");
        }
        
        copyUserProfileProperties(userProfile, userProfileUpdateDTO);
        
        log.info("更新用户资料成功: userId={}", userId);
        return userProfileRepository.save(userProfile);
    }
    
    @Override
    public UserProfileDetailDTO getUserProfileDetail(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户资料不存在: " + userId));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在: " + userId));
        
        // 构建详细DTO
        UserProfileDetailDTO detailDTO = new UserProfileDetailDTO();
        detailDTO.setId(userProfile.getId());
        detailDTO.setUserId(userProfile.getUserId());
        detailDTO.setRealName(userProfile.getRealName());
        detailDTO.setGender(userProfile.getGender());
        detailDTO.setBirthday(userProfile.getBirthday());
        detailDTO.setAvatar(userProfile.getAvatar());
        detailDTO.setIdCardFront(userProfile.getIdCardFront());
        detailDTO.setIdCardBack(userProfile.getIdCardBack());
        detailDTO.setEducation(userProfile.getEducation());
        detailDTO.setWorkYears(userProfile.getWorkYears());
        detailDTO.setCurrentSalary(userProfile.getCurrentSalary());
        detailDTO.setExpectedSalary(userProfile.getExpectedSalary());
        detailDTO.setCity(userProfile.getCity());
        // 将逗号分隔的技能字符串转换为列表
        if (userProfile.getSkills() != null) {
            detailDTO.setSkills(List.of(userProfile.getSkills().split(",")));
        }
        detailDTO.setSelfIntro(userProfile.getSelfIntro());
        
        // 求职偏好字段
        if (userProfile.getPreferredCities() != null) {
            detailDTO.setPreferredCities(List.of(userProfile.getPreferredCities().split(",")));
        }
        if (userProfile.getJobTypes() != null) {
            detailDTO.setJobTypes(List.of(userProfile.getJobTypes().split(",")));
        }
        if (userProfile.getIndustries() != null) {
            detailDTO.setIndustries(List.of(userProfile.getIndustries().split(",")));
        }
        detailDTO.setWorkMode(userProfile.getWorkMode());
        detailDTO.setJobStatus(userProfile.getJobStatus());
        
        // 统计信息字段
        detailDTO.setTotalResumes(userProfile.getTotalResumes());
        detailDTO.setJobApplyCount(userProfile.getJobApplyCount());
        detailDTO.setInterviewCount(userProfile.getInterviewCount());
        detailDTO.setOfferCount(userProfile.getOfferCount());
        
        // 转换时间字段为字符串
        if (userProfile.getCreateTime() != null) {
            detailDTO.setCreateTime(userProfile.getCreateTime().toString());
        }
        if (userProfile.getUpdateTime() != null) {
            detailDTO.setUpdateTime(userProfile.getUpdateTime().toString());
        }
        
        // 添加用户信息
        detailDTO.setUsername(user.getUsername());
        detailDTO.setPhone(user.getPhone());
        detailDTO.setEmail(user.getEmail());
        detailDTO.setUserType(user.getUserType());
        detailDTO.setStatus(user.getStatus());
        detailDTO.setAuthStatus(user.getAuthStatus());
        
        log.debug("获取用户资料详情成功: userId={}", userId);
        return detailDTO;
    }
    
    @Override
    public UserProfile getUserProfile(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户资料不存在: " + userId));
        log.debug("获取用户资料成功: userId={}", userId);
        return userProfile;
    }
    
    @Override
    @Transactional
    public UserProfile updateSkills(Long userId, List<String> skills) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户资料不存在: " + userId));
        
        // 验证技能列表
        if (skills == null || skills.isEmpty()) {
            throw new IllegalArgumentException("技能列表不能为空");
        }
        
        // 验证技能名称
        for (String skill : skills) {
            if (skill == null || skill.trim().isEmpty()) {
                throw new IllegalArgumentException("技能名称不能为空");
            }
        }
        
        // 将技能列表转换为逗号分隔的字符串
        String skillsString = String.join(",", skills);
        userProfile.setSkills(skillsString);
        
        log.info("更新用户技能成功: userId={}, skills={}", userId, skills);
        return userProfileRepository.save(userProfile);
    }
    
    @Override
    @Transactional
    public UserProfile updateJobPreferences(Long userId, 
                                          List<String> preferredCities,
                                          List<String> jobTypes,
                                          List<String> industries,
                                          String workMode,
                                          String jobStatus) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户资料不存在: " + userId));
        
        // 更新求职偏好信息
        if (preferredCities != null) {
            userProfile.setPreferredCities(String.join(",", preferredCities));
        }
        if (jobTypes != null) {
            userProfile.setJobTypes(String.join(",", jobTypes));
        }
        if (industries != null) {
            userProfile.setIndustries(String.join(",", industries));
        }
        if (workMode != null) {
            userProfile.setWorkMode(workMode);
        }
        if (jobStatus != null) {
            userProfile.setJobStatus(jobStatus);
        }
        
        log.info("更新用户求职偏好成功: userId={}", userId);
        return userProfileRepository.save(userProfile);
    }
    
    @Override
    public Page<UserProfileDetailDTO> getUserProfiles(Pageable pageable, 
                                                    String city,
                                                    String education,
                                                    Integer minWorkYears,
                                                    Integer maxWorkYears,
                                                    Double minExpectedSalary,
                                                    Double maxExpectedSalary,
                                                    String skill) {
        Page<UserProfile> userProfiles = userProfileRepository.findProfilesByConditions(
                city, education, minWorkYears, maxWorkYears, 
                minExpectedSalary, maxExpectedSalary, skill, pageable);
        
        // 转换为DTO列表
        List<UserProfileDetailDTO> dtoList = new ArrayList<>();
        for (UserProfile userProfile : userProfiles.getContent()) {
            User user = userRepository.findById(userProfile.getUserId())
                    .orElse(null);
            if (user != null) {
                UserProfileDetailDTO dto = convertToDetailDTO(userProfile, user);
                dtoList.add(dto);
            }
        }
        
        return new PageImpl<>(dtoList, pageable, userProfiles.getTotalElements());
    }
    
    @Override
    public Page<UserProfileDetailDTO> searchBySkills(List<String> skills, Pageable pageable) {
        // 简化实现：只搜索第一个技能
        if (skills == null || skills.isEmpty()) {
            return Page.empty(pageable);
        }
        
        String firstSkill = skills.get(0);
        Page<UserProfile> userProfiles = userProfileRepository.findProfilesByConditions(
                null, null, null, null, null, null, firstSkill, pageable);
        
        // 转换为DTO列表
        List<UserProfileDetailDTO> dtoList = new ArrayList<>();
        for (UserProfile userProfile : userProfiles.getContent()) {
            User user = userRepository.findById(userProfile.getUserId())
                    .orElse(null);
            if (user != null) {
                UserProfileDetailDTO dto = convertToDetailDTO(userProfile, user);
                dtoList.add(dto);
            }
        }
        
        return new PageImpl<>(dtoList, pageable, userProfiles.getTotalElements());
    }
    
    @Override
    public Page<UserProfileDetailDTO> searchByCity(String city, Pageable pageable) {
        Page<UserProfile> userProfiles = userProfileRepository.findProfilesByConditions(
                city, null, null, null, null, null, null, pageable);
        
        // 转换为DTO列表
        List<UserProfileDetailDTO> dtoList = new ArrayList<>();
        for (UserProfile userProfile : userProfiles.getContent()) {
            User user = userRepository.findById(userProfile.getUserId())
                    .orElse(null);
            if (user != null) {
                UserProfileDetailDTO dto = convertToDetailDTO(userProfile, user);
                dtoList.add(dto);
            }
        }
        
        return new PageImpl<>(dtoList, pageable, userProfiles.getTotalElements());
    }
    
    @Override
    public Page<UserProfileDetailDTO> searchByWorkYears(Integer minYears, Integer maxYears, Pageable pageable) {
        Page<UserProfile> userProfiles = userProfileRepository.findProfilesByConditions(
                null, null, minYears, maxYears, null, null, null, pageable);
        
        // 转换为DTO列表
        List<UserProfileDetailDTO> dtoList = new ArrayList<>();
        for (UserProfile userProfile : userProfiles.getContent()) {
            User user = userRepository.findById(userProfile.getUserId())
                    .orElse(null);
            if (user != null) {
                UserProfileDetailDTO dto = convertToDetailDTO(userProfile, user);
                dtoList.add(dto);
            }
        }
        
        return new PageImpl<>(dtoList, pageable, userProfiles.getTotalElements());
    }
    
    @Override
    public Page<UserProfileDetailDTO> searchByExpectedSalary(Double minSalary, Double maxSalary, Pageable pageable) {
        Page<UserProfile> userProfiles = userProfileRepository.findProfilesByConditions(
                null, null, null, null, minSalary, maxSalary, null, pageable);
        
        // 转换为DTO列表
        List<UserProfileDetailDTO> dtoList = new ArrayList<>();
        for (UserProfile userProfile : userProfiles.getContent()) {
            User user = userRepository.findById(userProfile.getUserId())
                    .orElse(null);
            if (user != null) {
                UserProfileDetailDTO dto = convertToDetailDTO(userProfile, user);
                dtoList.add(dto);
            }
        }
        
        return new PageImpl<>(dtoList, pageable, userProfiles.getTotalElements());
    }
    
    @Override
    public Map<String, Object> getUserProfileStats(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户资料不存在: " + userId));
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("userId", userId);
        stats.put("jobApplyCount", userProfile.getJobApplyCount());
        stats.put("interviewCount", userProfile.getInterviewCount());
        stats.put("offerCount", userProfile.getOfferCount());
        stats.put("lastUpdateTime", userProfile.getUpdateTime());
        
        // 计算成功率
        double applySuccessRate = userProfile.getJobApplyCount() > 0 ? 
                (double) userProfile.getInterviewCount() / userProfile.getJobApplyCount() * 100 : 0;
        double interviewSuccessRate = userProfile.getInterviewCount() > 0 ? 
                (double) userProfile.getOfferCount() / userProfile.getInterviewCount() * 100 : 0;
        
        stats.put("applySuccessRate", Math.round(applySuccessRate * 100) / 100.0);
        stats.put("interviewSuccessRate", Math.round(interviewSuccessRate * 100) / 100.0);
        
        log.debug("获取用户资料统计成功: userId={}", userId);
        return stats;
    }
    
    @Override
    @Transactional
    public void incrementJobApplyCount(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户资料不存在: " + userId));
        
        userProfile.setJobApplyCount(userProfile.getJobApplyCount() + 1);
        userProfileRepository.save(userProfile);
        log.debug("增加用户投递次数: userId={}", userId);
    }
    
    @Override
    @Transactional
    public void incrementInterviewCount(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户资料不存在: " + userId));
        
        userProfile.setInterviewCount(userProfile.getInterviewCount() + 1);
        userProfileRepository.save(userProfile);
        log.debug("增加用户面试次数: userId={}", userId);
    }
    
    @Override
    @Transactional
    public void incrementOfferCount(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户资料不存在: " + userId));
        
        userProfile.setOfferCount(userProfile.getOfferCount() + 1);
        userProfileRepository.save(userProfile);
        log.debug("增加用户录用次数: userId={}", userId);
    }
    
    @Override
    @Transactional
    public void updateResumeCount(Long userId, Integer count) {
        // 简历数量统计在简历模块中处理，这里暂时不实现
        log.info("更新用户简历数量: userId={}, count={}", userId, count);
    }
    
    @Override
    public List<Map<String, Object>> getPopularSkills(Integer limit) {
        // 模拟热门技能数据
        List<Map<String, Object>> popularSkills = new ArrayList<>();
        
        String[] skills = {"Java", "Spring Boot", "MySQL", "Redis", "Vue.js", 
                          "React", "Python", "Docker", "Kubernetes", "AWS"};
        
        for (int i = 0; i < Math.min(limit, skills.length); i++) {
            Map<String, Object> skillData = new HashMap<>();
            skillData.put("skill", skills[i]);
            skillData.put("count", 100 - i * 10);
            skillData.put("percentage", (100 - i * 10) / 100.0);
            popularSkills.add(skillData);
        }
        
        return popularSkills;
    }
    
    @Override
    public List<Map<String, Object>> getCityDistribution() {
        // 模拟城市分布数据
        List<Map<String, Object>> cityDistribution = new ArrayList<>();
        
        String[] cities = {"北京", "上海", "深圳", "广州", "杭州", "成都", "武汉", "南京"};
        
        for (int i = 0; i < cities.length; i++) {
            Map<String, Object> cityData = new HashMap<>();
            cityData.put("city", cities[i]);
            cityData.put("count", 500 - i * 50);
            cityData.put("percentage", (500 - i * 50) / 5000.0);
            cityDistribution.add(cityData);
        }
        
        return cityDistribution;
    }
    
    @Override
    public List<Map<String, Object>> getWorkYearsDistribution() {
        // 模拟工作年限分布数据
        List<Map<String, Object>> workYearsDistribution = new ArrayList<>();
        
        String[] yearsRange = {"1年以下", "1-3年", "3-5年", "5-10年", "10年以上"};
        int[] counts = {200, 500, 300, 150, 50};
        
        int total = 0;
        for (int count : counts) {
            total += count;
        }
        
        for (int i = 0; i < yearsRange.length; i++) {
            Map<String, Object> yearData = new HashMap<>();
            yearData.put("yearsRange", yearsRange[i]);
            yearData.put("count", counts[i]);
            yearData.put("percentage", (double) counts[i] / total);
            workYearsDistribution.add(yearData);
        }
        
        return workYearsDistribution;
    }
    
    @Override
    public List<Map<String, Object>> getExpectedSalaryDistribution() {
        // 模拟期望薪资分布数据
        List<Map<String, Object>> salaryDistribution = new ArrayList<>();
        
        String[] salaryRanges = {"5-10k", "10-15k", "15-20k", "20-30k", "30k以上"};
        int[] counts = {100, 300, 400, 150, 50};
        
        int total = 0;
        for (int count : counts) {
            total += count;
        }
        
        for (int i = 0; i < salaryRanges.length; i++) {
            Map<String, Object> salaryData = new HashMap<>();
            salaryData.put("salaryRange", salaryRanges[i]);
            salaryData.put("count", counts[i]);
            salaryData.put("percentage", (double) counts[i] / total);
            salaryDistribution.add(salaryData);
        }
        
        return salaryDistribution;
    }
    
    /**
     * 复制用户资料属性 - 创建DTO版本
     */
    private void copyUserProfileProperties(UserProfile userProfile, UserProfileCreateDTO userProfileCreateDTO) {
        if (userProfileCreateDTO.getRealName() != null) {
            userProfile.setRealName(userProfileCreateDTO.getRealName());
        }
        if (userProfileCreateDTO.getGender() != null) {
            userProfile.setGender(userProfileCreateDTO.getGender());
        }
        if (userProfileCreateDTO.getBirthday() != null) {
            userProfile.setBirthday(userProfileCreateDTO.getBirthday());
        }
        if (userProfileCreateDTO.getAvatar() != null) {
            userProfile.setAvatar(userProfileCreateDTO.getAvatar());
        }
        if (userProfileCreateDTO.getIdCardFront() != null) {
            userProfile.setIdCardFront(userProfileCreateDTO.getIdCardFront());
        }
        if (userProfileCreateDTO.getIdCardBack() != null) {
            userProfile.setIdCardBack(userProfileCreateDTO.getIdCardBack());
        }
        if (userProfileCreateDTO.getEducation() != null) {
            userProfile.setEducation(userProfileCreateDTO.getEducation());
        }
        if (userProfileCreateDTO.getWorkYears() != null) {
            userProfile.setWorkYears(userProfileCreateDTO.getWorkYears());
        }
        if (userProfileCreateDTO.getCurrentSalary() != null) {
            userProfile.setCurrentSalary(userProfileCreateDTO.getCurrentSalary());
        }
        if (userProfileCreateDTO.getExpectedSalary() != null) {
            userProfile.setExpectedSalary(userProfileCreateDTO.getExpectedSalary());
        }
        if (userProfileCreateDTO.getCity() != null) {
            userProfile.setCity(userProfileCreateDTO.getCity());
        }
        if (userProfileCreateDTO.getSkills() != null) {
            userProfile.setSkills(String.join(",", userProfileCreateDTO.getSkills()));
        }
        if (userProfileCreateDTO.getSelfIntro() != null) {
            userProfile.setSelfIntro(userProfileCreateDTO.getSelfIntro());
        }
        // 求职偏好字段
        if (userProfileCreateDTO.getPreferredCities() != null) {
            userProfile.setPreferredCities(String.join(",", userProfileCreateDTO.getPreferredCities()));
        }
        if (userProfileCreateDTO.getJobTypes() != null) {
            userProfile.setJobTypes(String.join(",", userProfileCreateDTO.getJobTypes()));
        }
        if (userProfileCreateDTO.getIndustries() != null) {
            userProfile.setIndustries(String.join(",", userProfileCreateDTO.getIndustries()));
        }
        if (userProfileCreateDTO.getWorkMode() != null) {
            userProfile.setWorkMode(userProfileCreateDTO.getWorkMode());
        }
        if (userProfileCreateDTO.getJobStatus() != null) {
            userProfile.setJobStatus(userProfileCreateDTO.getJobStatus());
        }
    }
    
    /**
     * 复制用户资料属性 - 更新DTO版本
     */
    private void copyUserProfileProperties(UserProfile userProfile, UserProfileUpdateDTO userProfileUpdateDTO) {
        if (userProfileUpdateDTO.getRealName() != null) {
            userProfile.setRealName(userProfileUpdateDTO.getRealName());
        }
        if (userProfileUpdateDTO.getGender() != null) {
            userProfile.setGender(userProfileUpdateDTO.getGender());
        }
        if (userProfileUpdateDTO.getBirthday() != null) {
            userProfile.setBirthday(userProfileUpdateDTO.getBirthday());
        }
        if (userProfileUpdateDTO.getAvatar() != null) {
            userProfile.setAvatar(userProfileUpdateDTO.getAvatar());
        }
        if (userProfileUpdateDTO.getIdCardFront() != null) {
            userProfile.setIdCardFront(userProfileUpdateDTO.getIdCardFront());
        }
        if (userProfileUpdateDTO.getIdCardBack() != null) {
            userProfile.setIdCardBack(userProfileUpdateDTO.getIdCardBack());
        }
        if (userProfileUpdateDTO.getEducation() != null) {
            userProfile.setEducation(userProfileUpdateDTO.getEducation());
        }
        if (userProfileUpdateDTO.getWorkYears() != null) {
            userProfile.setWorkYears(userProfileUpdateDTO.getWorkYears());
        }
        if (userProfileUpdateDTO.getCurrentSalary() != null) {
            userProfile.setCurrentSalary(userProfileUpdateDTO.getCurrentSalary());
        }
        if (userProfileUpdateDTO.getExpectedSalary() != null) {
            userProfile.setExpectedSalary(userProfileUpdateDTO.getExpectedSalary());
        }
        if (userProfileUpdateDTO.getCity() != null) {
            userProfile.setCity(userProfileUpdateDTO.getCity());
        }
        if (userProfileUpdateDTO.getSkills() != null) {
            userProfile.setSkills(String.join(",", userProfileUpdateDTO.getSkills()));
        }
        if (userProfileUpdateDTO.getSelfIntro() != null) {
            userProfile.setSelfIntro(userProfileUpdateDTO.getSelfIntro());
        }
        // 求职偏好字段
        if (userProfileUpdateDTO.getPreferredCities() != null) {
            userProfile.setPreferredCities(String.join(",", userProfileUpdateDTO.getPreferredCities()));
        }
        if (userProfileUpdateDTO.getJobTypes() != null) {
            userProfile.setJobTypes(String.join(",", userProfileUpdateDTO.getJobTypes()));
        }
        if (userProfileUpdateDTO.getIndustries() != null) {
            userProfile.setIndustries(String.join(",", userProfileUpdateDTO.getIndustries()));
        }
        if (userProfileUpdateDTO.getWorkMode() != null) {
            userProfile.setWorkMode(userProfileUpdateDTO.getWorkMode());
        }
        if (userProfileUpdateDTO.getJobStatus() != null) {
            userProfile.setJobStatus(userProfileUpdateDTO.getJobStatus());
        }
    }
    
    /**
     * 转换为详细DTO
     */
    private UserProfileDetailDTO convertToDetailDTO(UserProfile userProfile, User user) {
        UserProfileDetailDTO dto = new UserProfileDetailDTO();
        dto.setId(userProfile.getId());
        dto.setUserId(userProfile.getUserId());
        dto.setRealName(userProfile.getRealName());
        dto.setGender(userProfile.getGender());
        dto.setBirthday(userProfile.getBirthday());
        dto.setAvatar(userProfile.getAvatar());
        dto.setIdCardFront(userProfile.getIdCardFront());
        dto.setIdCardBack(userProfile.getIdCardBack());
        dto.setEducation(userProfile.getEducation());
        dto.setWorkYears(userProfile.getWorkYears());
        dto.setCurrentSalary(userProfile.getCurrentSalary());
        dto.setExpectedSalary(userProfile.getExpectedSalary());
        dto.setCity(userProfile.getCity());
        // 将逗号分隔的技能字符串转换为列表
        if (userProfile.getSkills() != null) {
            dto.setSkills(List.of(userProfile.getSkills().split(",")));
        }
        dto.setSelfIntro(userProfile.getSelfIntro());
        
        // 求职偏好字段
        if (userProfile.getPreferredCities() != null) {
            dto.setPreferredCities(List.of(userProfile.getPreferredCities().split(",")));
        }
        if (userProfile.getJobTypes() != null) {
            dto.setJobTypes(List.of(userProfile.getJobTypes().split(",")));
        }
        if (userProfile.getIndustries() != null) {
            dto.setIndustries(List.of(userProfile.getIndustries().split(",")));
        }
        dto.setWorkMode(userProfile.getWorkMode());
        dto.setJobStatus(userProfile.getJobStatus());
        
        // 统计信息字段
        dto.setTotalResumes(userProfile.getTotalResumes());
        dto.setJobApplyCount(userProfile.getJobApplyCount());
        dto.setInterviewCount(userProfile.getInterviewCount());
        dto.setOfferCount(userProfile.getOfferCount());
        
        // 转换时间字段为字符串
        if (userProfile.getCreateTime() != null) {
            dto.setCreateTime(userProfile.getCreateTime().toString());
        }
        if (userProfile.getUpdateTime() != null) {
            dto.setUpdateTime(userProfile.getUpdateTime().toString());
        }
        
        // 添加用户信息
        dto.setUsername(user.getUsername());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setUserType(user.getUserType());
        dto.setStatus(user.getStatus());
        dto.setAuthStatus(user.getAuthStatus());
        
        return dto;
    }
}
