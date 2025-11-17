package com.shera.framework.employment.employment.modules.analytics.service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 数据分析服务接口
 */
public interface AnalyticsService {
    
    /**
     * 获取用户行为分析
     */
    Map<String, Object> getUserBehaviorAnalysis(Long userId);
    
    /**
     * 获取岗位热度分析
     */
    Map<String, Object> getJobHeatAnalysis(Long jobId);
    
    /**
     * 获取申请成功率分析
     */
    Map<String, Object> getApplySuccessRateAnalysis(Long userId);
    
    /**
     * 获取系统整体统计
     */
    Map<String, Object> getSystemStatistics();
    
    /**
     * 获取用户增长趋势
     */
    Map<String, Object> getUserGrowthTrend(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 获取岗位发布趋势
     */
    Map<String, Object> getJobPublishTrend(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 获取申请趋势分析
     */
    Map<String, Object> getApplyTrendAnalysis(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 获取热门技能分析
     */
    Map<String, Object> getHotSkillsAnalysis(int limit);
    
    /**
     * 获取热门城市分析
     */
    Map<String, Object> getHotCitiesAnalysis(int limit);
    
    /**
     * 获取薪资分布分析
     */
    Map<String, Object> getSalaryDistributionAnalysis();
    
    /**
     * 获取用户画像分析
     */
    Map<String, Object> getUserProfileAnalysis();
    
    /**
     * 获取岗位匹配度分析
     */
    Map<String, Object> getJobMatchAnalysis();
    
    /**
     * 获取活跃用户分析
     */
    Map<String, Object> getActiveUserAnalysis(int days);
    
    /**
     * 获取企业招聘分析
     */
    Map<String, Object> getCompanyRecruitmentAnalysis(Long companyId);
    
    /**
     * 获取简历质量分析
     */
    Map<String, Object> getResumeQualityAnalysis(Long userId);
    
    /**
     * 获取面试通过率分析
     */
    Map<String, Object> getInterviewPassRateAnalysis();
    
    /**
     * 获取用户留存分析
     */
    Map<String, Object> getUserRetentionAnalysis(int days);
    
    /**
     * 获取转化率分析
     */
    Map<String, Object> getConversionRateAnalysis();
    
    /**
     * 获取推荐效果分析
     */
    Map<String, Object> getRecommendationEffectAnalysis();
    
    /**
     * 获取数据质量报告
     */
    Map<String, Object> getDataQualityReport();
    
    /**
     * 导出分析报告
     */
    String exportAnalysisReport(String reportType, Map<String, Object> parameters);
    
    /**
     * 获取实时数据监控
     */
    Map<String, Object> getRealTimeMonitoring();
    
    /**
     * 获取异常检测结果
     */
    Map<String, Object> getAnomalyDetection();
    
    /**
     * 获取权限系统统计信息
     */
    Map<String, Object> getPermissionStats();
}
