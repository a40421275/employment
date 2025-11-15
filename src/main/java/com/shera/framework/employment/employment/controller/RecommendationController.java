package com.shera.framework.employment.employment.controller;

import com.shera.framework.employment.employment.dto.ResumeRecommendationDTO;
import com.shera.framework.employment.employment.entity.Job;
import com.shera.framework.employment.employment.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 智能推荐控制器
 */
@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    
    private final RecommendationService recommendationService;
    
    /**
     * 为用户推荐岗位
     */
    @GetMapping("/jobs/user/{userId}")
    public ResponseEntity<List<Job>> recommendJobsForUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        List<Job> recommendedJobs = recommendationService.recommendJobsForUser(userId, limit);
        return ResponseEntity.ok(recommendedJobs);
    }
    
    /**
     * 为岗位推荐匹配的简历
     */
    @GetMapping("/resumes/job/{jobId}")
    public ResponseEntity<List<ResumeRecommendationDTO>> recommendResumesForJob(
            @PathVariable Long jobId,
            @RequestParam(defaultValue = "10") int limit) {
        List<ResumeRecommendationDTO> recommendedResumes = recommendationService.recommendResumesForJob(jobId, limit);
        return ResponseEntity.ok(recommendedResumes);
    }
    
    /**
     * 基于协同过滤的岗位推荐
     */
    @GetMapping("/collaborative/{userId}")
    public ResponseEntity<List<Job>> collaborativeFilteringRecommendation(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        List<Job> recommendedJobs = recommendationService.collaborativeFilteringRecommendation(userId, limit);
        return ResponseEntity.ok(recommendedJobs);
    }
    
    /**
     * 基于内容的岗位推荐
     */
    @GetMapping("/content/{userId}")
    public ResponseEntity<List<Job>> contentBasedRecommendation(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        List<Job> recommendedJobs = recommendationService.contentBasedRecommendation(userId, limit);
        return ResponseEntity.ok(recommendedJobs);
    }
    
    /**
     * 基于用户画像的岗位推荐
     */
    @GetMapping("/profile/{userId}")
    public ResponseEntity<List<Job>> userProfileBasedRecommendation(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        List<Job> recommendedJobs = recommendationService.userProfileBasedRecommendation(userId, limit);
        return ResponseEntity.ok(recommendedJobs);
    }
    
    /**
     * 热门岗位推荐
     */
    @GetMapping("/hot")
    public ResponseEntity<List<Job>> hotJobsRecommendation(
            @RequestParam(defaultValue = "10") int limit) {
        List<Job> recommendedJobs = recommendationService.hotJobsRecommendation(limit);
        return ResponseEntity.ok(recommendedJobs);
    }
    
    /**
     * 最新岗位推荐
     */
    @GetMapping("/latest")
    public ResponseEntity<List<Job>> latestJobsRecommendation(
            @RequestParam(defaultValue = "10") int limit) {
        List<Job> recommendedJobs = recommendationService.latestJobsRecommendation(limit);
        return ResponseEntity.ok(recommendedJobs);
    }
    
    /**
     * 相似岗位推荐
     */
    @GetMapping("/similar/{jobId}")
    public ResponseEntity<List<Job>> similarJobsRecommendation(
            @PathVariable Long jobId,
            @RequestParam(defaultValue = "10") int limit) {
        List<Job> recommendedJobs = recommendationService.similarJobsRecommendation(jobId, limit);
        return ResponseEntity.ok(recommendedJobs);
    }
    
    /**
     * 基于地理位置的岗位推荐
     */
    @GetMapping("/location/{userId}")
    public ResponseEntity<List<Job>> locationBasedRecommendation(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        List<Job> recommendedJobs = recommendationService.locationBasedRecommendation(userId, limit);
        return ResponseEntity.ok(recommendedJobs);
    }
    
    /**
     * 基于薪资期望的岗位推荐
     */
    @GetMapping("/salary/{userId}")
    public ResponseEntity<List<Job>> salaryBasedRecommendation(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        List<Job> recommendedJobs = recommendationService.salaryBasedRecommendation(userId, limit);
        return ResponseEntity.ok(recommendedJobs);
    }
    
    /**
     * 基于技能匹配的岗位推荐
     */
    @GetMapping("/skill/{userId}")
    public ResponseEntity<List<Job>> skillBasedRecommendation(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        List<Job> recommendedJobs = recommendationService.skillBasedRecommendation(userId, limit);
        return ResponseEntity.ok(recommendedJobs);
    }
    
    /**
     * 混合推荐算法
     */
    @GetMapping("/hybrid/{userId}")
    public ResponseEntity<List<Job>> hybridRecommendation(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        List<Job> recommendedJobs = recommendationService.hybridRecommendation(userId, limit);
        return ResponseEntity.ok(recommendedJobs);
    }
    
    /**
     * 计算岗位与用户的匹配度
     */
    @PostMapping("/match-scores/user/{userId}")
    public ResponseEntity<Map<Long, Double>> calculateJobUserMatchScores(
            @PathVariable Long userId,
            @RequestBody List<Long> jobIds) {
        Map<Long, Double> matchScores = recommendationService.calculateJobUserMatchScores(userId, jobIds);
        return ResponseEntity.ok(matchScores);
    }
    
    /**
     * 计算简历与岗位的匹配度
     */
    @PostMapping("/match-scores/job/{jobId}")
    public ResponseEntity<Map<Long, Double>> calculateResumeJobMatchScores(
            @PathVariable Long jobId,
            @RequestBody List<Long> resumeIds) {
        Map<Long, Double> matchScores = recommendationService.calculateResumeJobMatchScores(jobId, resumeIds);
        return ResponseEntity.ok(matchScores);
    }
    
    /**
     * 获取用户推荐偏好
     */
    @GetMapping("/preferences/{userId}")
    public ResponseEntity<Map<String, Object>> getUserRecommendationPreferences(@PathVariable Long userId) {
        Map<String, Object> preferences = recommendationService.getUserRecommendationPreferences(userId);
        return ResponseEntity.ok(preferences);
    }
    
    /**
     * 更新用户推荐偏好
     */
    @PutMapping("/preferences/{userId}")
    public ResponseEntity<Void> updateUserRecommendationPreferences(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> preferences) {
        recommendationService.updateUserRecommendationPreferences(userId, preferences);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 获取推荐系统统计信息
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getRecommendationStats() {
        Map<String, Object> stats = recommendationService.getRecommendationStats();
        return ResponseEntity.ok(stats);
    }
    
    /**
     * 训练推荐模型
     */
    @PostMapping("/train")
    public ResponseEntity<Void> trainRecommendationModel() {
        recommendationService.trainRecommendationModel();
        return ResponseEntity.ok().build();
    }
    
    /**
     * 清除推荐缓存
     */
    @DeleteMapping("/cache/{userId}")
    public ResponseEntity<Void> clearRecommendationCache(@PathVariable Long userId) {
        recommendationService.clearRecommendationCache(userId);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 获取推荐解释
     */
    @PostMapping("/explanation/{userId}")
    public ResponseEntity<Map<Long, String>> getRecommendationExplanation(
            @PathVariable Long userId,
            @RequestBody List<Long> jobIds) {
        Map<Long, String> explanations = recommendationService.getRecommendationExplanation(userId, jobIds);
        return ResponseEntity.ok(explanations);
    }
    
    /**
     * 评估推荐效果
     */
    @GetMapping("/performance")
    public ResponseEntity<Map<String, Object>> evaluateRecommendationPerformance() {
        Map<String, Object> performance = recommendationService.evaluateRecommendationPerformance();
        return ResponseEntity.ok(performance);
    }
}
