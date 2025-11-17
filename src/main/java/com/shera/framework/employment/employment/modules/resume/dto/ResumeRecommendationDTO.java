package com.shera.framework.employment.employment.modules.resume.dto;

import lombok.Data;

/**
 * 简历推荐DTO
 */
@Data
public class ResumeRecommendationDTO {
    
    /**
     * 简历ID
     */
    private Long resumeId;
    
    /**
     * 简历标题
     */
    private String resumeTitle;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 匹配度分数
     */
    private Double matchScore;
    
    /**
     * 推荐理由
     */
    private String recommendationReason;
    
    /**
     * 技能匹配度
     */
    private Double skillMatchRate;
    
    /**
     * 经验匹配度
     */
    private Double experienceMatchRate;
    
    /**
     * 薪资匹配度
     */
    private Double salaryMatchRate;
    
    /**
     * 地理位置匹配度
     */
    private Double locationMatchRate;
    
    /**
     * 综合匹配度
     */
    private Double overallMatchRate;
    
    public ResumeRecommendationDTO() {}
    
    public ResumeRecommendationDTO(Long resumeId, String resumeTitle, Long userId, String username, 
                                  Double matchScore, String recommendationReason) {
        this.resumeId = resumeId;
        this.resumeTitle = resumeTitle;
        this.userId = userId;
        this.username = username;
        this.matchScore = matchScore;
        this.recommendationReason = recommendationReason;
    }
    
    public ResumeRecommendationDTO(Long resumeId, String resumeTitle, Long userId, String username, 
                                  Double matchScore, String recommendationReason,
                                  Double skillMatchRate, Double experienceMatchRate, 
                                  Double salaryMatchRate, Double locationMatchRate, 
                                  Double overallMatchRate) {
        this.resumeId = resumeId;
        this.resumeTitle = resumeTitle;
        this.userId = userId;
        this.username = username;
        this.matchScore = matchScore;
        this.recommendationReason = recommendationReason;
        this.skillMatchRate = skillMatchRate;
        this.experienceMatchRate = experienceMatchRate;
        this.salaryMatchRate = salaryMatchRate;
        this.locationMatchRate = locationMatchRate;
        this.overallMatchRate = overallMatchRate;
    }
}
