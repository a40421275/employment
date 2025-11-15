package com.shera.framework.employment.employment.controller;

import com.shera.framework.employment.employment.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 数据分析控制器
 */
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    
    private final AnalyticsService analyticsService;
    
    /**
     * 获取用户行为分析
     */
    @GetMapping("/user-behavior/{userId}")
    public ResponseEntity<Map<String, Object>> getUserBehaviorAnalysis(@PathVariable Long userId) {
        Map<String, Object> analysis = analyticsService.getUserBehaviorAnalysis(userId);
        return ResponseEntity.ok(analysis);
    }
    
    /**
     * 获取岗位热度分析
     */
    @GetMapping("/job-heat/{jobId}")
    public ResponseEntity<Map<String, Object>> getJobHeatAnalysis(@PathVariable Long jobId) {
        Map<String, Object> analysis = analyticsService.getJobHeatAnalysis(jobId);
        return ResponseEntity.ok(analysis);
    }
    
    /**
     * 获取申请成功率分析
     */
    @GetMapping("/apply-success-rate/{userId}")
    public ResponseEntity<Map<String, Object>> getApplySuccessRateAnalysis(@PathVariable Long userId) {
        Map<String, Object> analysis = analyticsService.getApplySuccessRateAnalysis(userId);
        return ResponseEntity.ok(analysis);
    }
    
    /**
     * 获取系统整体统计
     */
    @GetMapping("/system-statistics")
    public ResponseEntity<Map<String, Object>> getSystemStatistics() {
        Map<String, Object> stats = analyticsService.getSystemStatistics();
        return ResponseEntity.ok(stats);
    }
    
    /**
     * 获取用户增长趋势
     */
    @GetMapping("/user-growth-trend")
    public ResponseEntity<Map<String, Object>> getUserGrowthTrend(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        Map<String, Object> trend = analyticsService.getUserGrowthTrend(startTime, endTime);
        return ResponseEntity.ok(trend);
    }
    
    /**
     * 获取岗位发布趋势
     */
    @GetMapping("/job-publish-trend")
    public ResponseEntity<Map<String, Object>> getJobPublishTrend(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        Map<String, Object> trend = analyticsService.getJobPublishTrend(startTime, endTime);
        return ResponseEntity.ok(trend);
    }
    
    /**
     * 获取申请趋势分析
     */
    @GetMapping("/apply-trend")
    public ResponseEntity<Map<String, Object>> getApplyTrendAnalysis(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        Map<String, Object> trend = analyticsService.getApplyTrendAnalysis(startTime, endTime);
        return ResponseEntity.ok(trend);
    }
    
    /**
     * 获取热门技能分析
     */
    @GetMapping("/hot-skills")
    public ResponseEntity<Map<String, Object>> getHotSkillsAnalysis(
            @RequestParam(defaultValue = "10") int limit) {
        Map<String, Object> analysis = analyticsService.getHotSkillsAnalysis(limit);
        return ResponseEntity.ok(analysis);
    }
    
    /**
     * 获取热门城市分析
     */
    @GetMapping("/hot-cities")
    public ResponseEntity<Map<String, Object>> getHotCitiesAnalysis(
            @RequestParam(defaultValue = "10") int limit) {
        Map<String, Object> analysis = analyticsService.getHotCitiesAnalysis(limit);
        return ResponseEntity.ok(analysis);
    }
    
    /**
     * 获取薪资分布分析
     */
    @GetMapping("/salary-distribution")
    public ResponseEntity<Map<String, Object>> getSalaryDistributionAnalysis() {
        Map<String, Object> analysis = analyticsService.getSalaryDistributionAnalysis();
        return ResponseEntity.ok(analysis);
    }
    
    /**
     * 获取用户画像分析
     */
    @GetMapping("/user-profile")
    public ResponseEntity<Map<String, Object>> getUserProfileAnalysis() {
        Map<String, Object> analysis = analyticsService.getUserProfileAnalysis();
        return ResponseEntity.ok(analysis);
    }
    
    /**
     * 获取岗位匹配度分析
     */
    @GetMapping("/job-match")
    public ResponseEntity<Map<String, Object>> getJobMatchAnalysis() {
        Map<String, Object> analysis = analyticsService.getJobMatchAnalysis();
        return ResponseEntity.ok(analysis);
    }
    
    /**
     * 获取活跃用户分析
     */
    @GetMapping("/active-users")
    public ResponseEntity<Map<String, Object>> getActiveUserAnalysis(
            @RequestParam(defaultValue = "7") int days) {
        Map<String, Object> analysis = analyticsService.getActiveUserAnalysis(days);
        return ResponseEntity.ok(analysis);
    }
    
    /**
     * 获取企业招聘分析
     */
    @GetMapping("/company-recruitment/{companyId}")
    public ResponseEntity<Map<String, Object>> getCompanyRecruitmentAnalysis(@PathVariable Long companyId) {
        Map<String, Object> analysis = analyticsService.getCompanyRecruitmentAnalysis(companyId);
        return ResponseEntity.ok(analysis);
    }
    
    /**
     * 获取简历质量分析
     */
    @GetMapping("/resume-quality/{userId}")
    public ResponseEntity<Map<String, Object>> getResumeQualityAnalysis(@PathVariable Long userId) {
        Map<String, Object> analysis = analyticsService.getResumeQualityAnalysis(userId);
        return ResponseEntity.ok(analysis);
    }
    
    /**
     * 获取面试通过率分析
     */
    @GetMapping("/interview-pass-rate")
    public ResponseEntity<Map<String, Object>> getInterviewPassRateAnalysis() {
        Map<String, Object> analysis = analyticsService.getInterviewPassRateAnalysis();
        return ResponseEntity.ok(analysis);
    }
    
    /**
     * 获取用户留存分析
     */
    @GetMapping("/user-retention")
    public ResponseEntity<Map<String, Object>> getUserRetentionAnalysis(
            @RequestParam(defaultValue = "30") int days) {
        Map<String, Object> analysis = analyticsService.getUserRetentionAnalysis(days);
        return ResponseEntity.ok(analysis);
    }
    
    /**
     * 获取转化率分析
     */
    @GetMapping("/conversion-rate")
    public ResponseEntity<Map<String, Object>> getConversionRateAnalysis() {
        Map<String, Object> analysis = analyticsService.getConversionRateAnalysis();
        return ResponseEntity.ok(analysis);
    }
    
    /**
     * 获取推荐效果分析
     */
    @GetMapping("/recommendation-effect")
    public ResponseEntity<Map<String, Object>> getRecommendationEffectAnalysis() {
        Map<String, Object> analysis = analyticsService.getRecommendationEffectAnalysis();
        return ResponseEntity.ok(analysis);
    }
    
    /**
     * 获取数据质量报告
     */
    @GetMapping("/data-quality")
    public ResponseEntity<Map<String, Object>> getDataQualityReport() {
        Map<String, Object> report = analyticsService.getDataQualityReport();
        return ResponseEntity.ok(report);
    }
    
    /**
     * 导出分析报告
     */
    @PostMapping("/export-report")
    public ResponseEntity<String> exportAnalysisReport(
            @RequestParam String reportType,
            @RequestBody Map<String, Object> parameters) {
        String result = analyticsService.exportAnalysisReport(reportType, parameters);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取实时数据监控
     */
    @GetMapping("/real-time-monitoring")
    public ResponseEntity<Map<String, Object>> getRealTimeMonitoring() {
        Map<String, Object> monitoring = analyticsService.getRealTimeMonitoring();
        return ResponseEntity.ok(monitoring);
    }
    
    /**
     * 获取异常检测结果
     */
    @GetMapping("/anomaly-detection")
    public ResponseEntity<Map<String, Object>> getAnomalyDetection() {
        Map<String, Object> anomalies = analyticsService.getAnomalyDetection();
        return ResponseEntity.ok(anomalies);
    }
    
    /**
     * 获取综合仪表板数据
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        Map<String, Object> dashboard = analyticsService.getSystemStatistics();
        
        // 添加实时监控数据
        Map<String, Object> realTimeData = analyticsService.getRealTimeMonitoring();
        dashboard.put("realTimeData", realTimeData);
        
        // 添加热门分析数据
        Map<String, Object> hotSkills = analyticsService.getHotSkillsAnalysis(5);
        Map<String, Object> hotCities = analyticsService.getHotCitiesAnalysis(5);
        dashboard.put("hotSkills", hotSkills);
        dashboard.put("hotCities", hotCities);
        
        return ResponseEntity.ok(dashboard);
    }
    
    /**
     * 获取权限系统统计信息
     */
    @GetMapping("/permissions/stats")
    public ResponseEntity<Map<String, Object>> getPermissionStats() {
        // 这里可以集成权限服务的统计信息
        Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalRoles", 5);
        stats.put("totalPermissions", 50);
        stats.put("activeUsers", 150);
        stats.put("permissionUsage", "85%");
        return ResponseEntity.ok(stats);
    }
}
