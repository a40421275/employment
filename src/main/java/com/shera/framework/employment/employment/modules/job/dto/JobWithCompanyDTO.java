package com.shera.framework.employment.employment.modules.job.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 岗位与企业信息投影DTO
 * 用于JOIN查询优化，避免返回整个企业对象
 */
@Data
public class JobWithCompanyDTO {
    
    private Long id;
    private String title;
    private Long categoryId;
    private String categoryName;
    private Long companyId;
    private String companyName;
    private String department;
    private Integer jobType;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String salaryUnit;
    private String workCity;
    private String workAddress;
    private String workDistrict;
    private Double workLatitude;
    private Double workLongitude;
    private String description;
    private String requirements;
    private String benefits;
    private String contactInfo;
    private String educationRequirement;
    private String experienceRequirement;
    private Integer recruitNumber;
    private Integer urgentLevel;
    private Integer priorityLevel;
    private Boolean isRecommended;
    private String recommendReason;
    private String keywords;
    private Integer status;
    private Integer viewCount;
    private Integer applyCount;
    private Integer favoriteCount;
    private LocalDateTime publishTime;
    private LocalDateTime expireTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /**
     * 投影查询构造方法
     */
    public JobWithCompanyDTO(
            Long id, String title, Long categoryId, String categoryName, 
            Long companyId, String companyName, String department, 
            Integer jobType, BigDecimal salaryMin, BigDecimal salaryMax, 
            String salaryUnit, String workCity, String workAddress, 
            String workDistrict, Double workLatitude, Double workLongitude, 
            String description, String requirements, String benefits, 
            String contactInfo, String educationRequirement, 
            String experienceRequirement, Integer recruitNumber, 
            Integer urgentLevel, Integer priorityLevel, Boolean isRecommended, 
            String recommendReason, String keywords, Integer status, 
            Integer viewCount, Integer applyCount, Integer favoriteCount, 
            LocalDateTime publishTime, LocalDateTime expireTime, 
            LocalDateTime createTime, LocalDateTime updateTime) {
        
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.companyId = companyId;
        this.companyName = companyName;
        this.department = department;
        this.jobType = jobType;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.salaryUnit = salaryUnit;
        this.workCity = workCity;
        this.workAddress = workAddress;
        this.workDistrict = workDistrict;
        this.workLatitude = workLatitude;
        this.workLongitude = workLongitude;
        this.description = description;
        this.requirements = requirements;
        this.benefits = benefits;
        this.contactInfo = contactInfo;
        this.educationRequirement = educationRequirement;
        this.experienceRequirement = experienceRequirement;
        this.recruitNumber = recruitNumber;
        this.urgentLevel = urgentLevel;
        this.priorityLevel = priorityLevel;
        this.isRecommended = isRecommended;
        this.recommendReason = recommendReason;
        this.keywords = keywords;
        this.status = status;
        this.viewCount = viewCount;
        this.applyCount = applyCount;
        this.favoriteCount = favoriteCount;
        this.publishTime = publishTime;
        this.expireTime = expireTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
