package com.shera.framework.employment.employment.service;

import com.shera.framework.employment.employment.dto.ResumeRecommendationDTO;
import com.shera.framework.employment.employment.entity.Job;
import com.shera.framework.employment.employment.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 智能推荐服务接口
 */
public interface RecommendationService {
    
    /**
     * 为用户推荐岗位
     */
    List<Job> recommendJobsForUser(Long userId, int limit);
    
    /**
     * 为岗位推荐匹配的简历
     */
    List<ResumeRecommendationDTO> recommendResumesForJob(Long jobId, int limit);
    
    /**
     * 基于协同过滤的岗位推荐
     */
    List<Job> collaborativeFilteringRecommendation(Long userId, int limit);
    
    /**
     * 基于内容的岗位推荐
     */
    List<Job> contentBasedRecommendation(Long userId, int limit);
    
    /**
     * 基于用户画像的岗位推荐
     */
    List<Job> userProfileBasedRecommendation(Long userId, int limit);
    
    /**
     * 热门岗位推荐
     */
    List<Job> hotJobsRecommendation(int limit);
    
    /**
     * 最新岗位推荐
     */
    List<Job> latestJobsRecommendation(int limit);
    
    /**
     * 相似岗位推荐
     */
    List<Job> similarJobsRecommendation(Long jobId, int limit);
    
    /**
     * 基于地理位置的岗位推荐
     */
    List<Job> locationBasedRecommendation(Long userId, int limit);
    
    /**
     * 基于薪资期望的岗位推荐
     */
    List<Job> salaryBasedRecommendation(Long userId, int limit);
    
    /**
     * 基于技能匹配的岗位推荐
     */
    List<Job> skillBasedRecommendation(Long userId, int limit);
    
    /**
     * 混合推荐算法
     */
    List<Job> hybridRecommendation(Long userId, int limit);
    
    /**
     * 计算岗位与用户的匹配度
     */
    Map<Long, Double> calculateJobUserMatchScores(Long userId, List<Long> jobIds);
    
    /**
     * 计算简历与岗位的匹配度
     */
    Map<Long, Double> calculateResumeJobMatchScores(Long jobId, List<Long> resumeIds);
    
    /**
     * 获取用户推荐偏好
     */
    Map<String, Object> getUserRecommendationPreferences(Long userId);
    
    /**
     * 更新用户推荐偏好
     */
    void updateUserRecommendationPreferences(Long userId, Map<String, Object> preferences);
    
    /**
     * 获取推荐系统统计信息
     */
    Map<String, Object> getRecommendationStats();
    
    /**
     * 训练推荐模型
     */
    void trainRecommendationModel();
    
    /**
     * 清除推荐缓存
     */
    void clearRecommendationCache(Long userId);
    
    /**
     * 获取推荐解释
     */
    Map<Long, String> getRecommendationExplanation(Long userId, List<Long> jobIds);
    
    /**
     * 评估推荐效果
     */
    Map<String, Object> evaluateRecommendationPerformance();
}
