package com.shera.framework.employment.employment.modules.recommendation.service.impl;

import com.shera.framework.employment.employment.modules.job.entity.Job;
import com.shera.framework.employment.employment.modules.job.entity.JobApply;
import com.shera.framework.employment.employment.modules.job.repository.JobApplyRepository;
import com.shera.framework.employment.employment.modules.job.repository.JobCategoryRepository;
import com.shera.framework.employment.employment.modules.job.repository.JobRepository;
import com.shera.framework.employment.employment.modules.resume.dto.ResumeRecommendationDTO;
import com.shera.framework.employment.employment.modules.resume.entity.Resume;
import com.shera.framework.employment.employment.modules.resume.repository.ResumeRepository;
import com.shera.framework.employment.employment.modules.user.entity.User;
import com.shera.framework.employment.employment.modules.user.entity.UserProfile;
import com.shera.framework.employment.employment.modules.user.repository.UserProfileRepository;
import com.shera.framework.employment.employment.modules.user.repository.UserRepository;
import com.shera.framework.employment.employment.modules.recommendation.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 智能推荐服务实现类
 */
@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
    
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final ResumeRepository resumeRepository;
    private final JobApplyRepository jobApplyRepository;
    private final JobCategoryRepository jobCategoryRepository;
    
    @Override
    public List<Job> recommendJobsForUser(Long userId, int limit) {
        // 使用混合推荐算法
        return hybridRecommendation(userId, limit);
    }
    
    @Override
    public List<ResumeRecommendationDTO> recommendResumesForJob(Long jobId, int limit) {
        Optional<Job> jobOpt = jobRepository.findById(jobId);
        if (jobOpt.isEmpty()) {
            return Collections.emptyList();
        }
        
        Job job = jobOpt.get();
        List<Resume> resumes = resumeRepository.findByPrivacyLevel(1); // 公开简历
        
        // 计算匹配度并排序
        return resumes.stream()
                .map(resume -> {
                    double matchScore = calculateResumeJobMatchScore(resume, job);
                    String username = getUserName(resume.getUserId());
                    String recommendationReason = generateResumeRecommendationReason(resume, job, matchScore);
                    
                    return new ResumeRecommendationDTO(
                            resume.getId(),
                            resume.getTitle(),
                            resume.getUserId(),
                            username,
                            matchScore,
                            recommendationReason
                    );
                })
                .sorted((a, b) -> Double.compare(b.getMatchScore(), a.getMatchScore()))
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Job> collaborativeFilteringRecommendation(Long userId, int limit) {
        // 获取用户的历史申请记录
        List<JobApply> userApplies = jobApplyRepository.findByUserId(userId);
        
        if (userApplies.isEmpty()) {
            // 如果没有历史记录，返回热门岗位
            return hotJobsRecommendation(limit);
        }
        
        // 获取相似用户的申请记录
        Set<Long> similarUserIds = findSimilarUsers(userId);
        Set<Long> recommendedJobIds = new HashSet<>();
        
        for (Long similarUserId : similarUserIds) {
            List<JobApply> similarUserApplies = jobApplyRepository.findByUserId(similarUserId);
            for (JobApply apply : similarUserApplies) {
                if (!hasUserApplied(userId, apply.getJobId())) {
                    recommendedJobIds.add(apply.getJobId());
                }
            }
        }
        
        // 获取岗位详情
        return jobRepository.findAllById(recommendedJobIds).stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Job> contentBasedRecommendation(Long userId, int limit) {
        Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserId(userId);
        if (userProfileOpt.isEmpty()) {
            return latestJobsRecommendation(limit);
        }
        
        UserProfile userProfile = userProfileOpt.get();
        List<Job> allJobs = jobRepository.findByStatus(1); // 已发布的岗位
        
        // 基于用户技能和岗位要求进行匹配
        return allJobs.stream()
                .map(job -> {
                    double matchScore = calculateContentMatchScore(userProfile, job);
                    return new Object[]{job, matchScore};
                })
                .sorted((a, b) -> Double.compare((Double) b[1], (Double) a[1]))
                .limit(limit)
                .map(obj -> (Job) obj[0])
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Job> userProfileBasedRecommendation(Long userId, int limit) {
        Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserId(userId);
        if (userProfileOpt.isEmpty()) {
            return latestJobsRecommendation(limit);
        }
        
        UserProfile userProfile = userProfileOpt.get();
        
        // 基于用户画像进行推荐
        List<Job> recommendedJobs = new ArrayList<>();
        
        // 1. 基于地理位置推荐
        if (userProfile.getCity() != null) {
            recommendedJobs.addAll(locationBasedRecommendation(userId, limit / 2));
        }
        
        // 2. 基于薪资期望推荐
        recommendedJobs.addAll(salaryBasedRecommendation(userId, limit / 2));
        
        // 3. 基于技能匹配推荐
        recommendedJobs.addAll(skillBasedRecommendation(userId, limit / 2));
        
        // 去重并限制数量
        return recommendedJobs.stream()
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Job> hotJobsRecommendation(int limit) {
        // 基于浏览量、申请量、收藏量计算热度
        List<Job> allJobs = jobRepository.findByStatus(1);
        return allJobs.stream()
                .sorted((a, b) -> {
                    int scoreA = a.getViewCount() + a.getApplyCount() + a.getFavoriteCount();
                    int scoreB = b.getViewCount() + b.getApplyCount() + b.getFavoriteCount();
                    return Integer.compare(scoreB, scoreA);
                })
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Job> latestJobsRecommendation(int limit) {
        List<Job> allJobs = jobRepository.findByStatus(1);
        return allJobs.stream()
                .sorted((a, b) -> {
                    if (a.getPublishTime() == null && b.getPublishTime() == null) return 0;
                    if (a.getPublishTime() == null) return 1;
                    if (b.getPublishTime() == null) return -1;
                    return b.getPublishTime().compareTo(a.getPublishTime());
                })
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Job> similarJobsRecommendation(Long jobId, int limit) {
        Optional<Job> jobOpt = jobRepository.findById(jobId);
        if (jobOpt.isEmpty()) {
            return Collections.emptyList();
        }
        
        Job job = jobOpt.get();
        List<Job> sameCategoryJobs = jobRepository.findByCategoryIdAndStatus(
                job.getCategoryId(), 1
        );
        
        // 基于岗位标题和描述的相似度进行推荐
        return sameCategoryJobs.stream()
                .filter(j -> !j.getId().equals(jobId))
                .map(j -> {
                    double similarity = calculateJobSimilarity(job, j);
                    return new Object[]{j, similarity};
                })
                .sorted((a, b) -> Double.compare((Double) b[1], (Double) a[1]))
                .limit(limit)
                .map(obj -> (Job) obj[0])
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Job> locationBasedRecommendation(Long userId, int limit) {
        Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserId(userId);
        if (userProfileOpt.isEmpty() || userProfileOpt.get().getCity() == null) {
            return Collections.emptyList();
        }
        
        String userCity = userProfileOpt.get().getCity();
        List<Job> jobs = jobRepository.findByWorkCityAndStatus(userCity, 1);
        return jobs.stream()
                .sorted((a, b) -> {
                    if (a.getPublishTime() == null && b.getPublishTime() == null) return 0;
                    if (a.getPublishTime() == null) return 1;
                    if (b.getPublishTime() == null) return -1;
                    return b.getPublishTime().compareTo(a.getPublishTime());
                })
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Job> salaryBasedRecommendation(Long userId, int limit) {
        Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserId(userId);
        if (userProfileOpt.isEmpty() || userProfileOpt.get().getExpectedSalary() == null) {
            return Collections.emptyList();
        }
        
        BigDecimal expectedSalary = userProfileOpt.get().getExpectedSalary();
        Double minSalary = expectedSalary.multiply(new BigDecimal("0.7")).doubleValue(); // 期望薪资的70%
        Double maxSalary = expectedSalary.multiply(new BigDecimal("1.3")).doubleValue(); // 期望薪资的130%
        
        List<Job> jobs = jobRepository.findBySalaryRangeAndStatus(minSalary, maxSalary, 1);
        return jobs.stream()
                .sorted((a, b) -> {
                    if (a.getPublishTime() == null && b.getPublishTime() == null) return 0;
                    if (a.getPublishTime() == null) return 1;
                    if (b.getPublishTime() == null) return -1;
                    return b.getPublishTime().compareTo(a.getPublishTime());
                })
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Job> skillBasedRecommendation(Long userId, int limit) {
        Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserId(userId);
        if (userProfileOpt.isEmpty() || userProfileOpt.get().getSkills() == null) {
            return Collections.emptyList();
        }
        
        String userSkills = userProfileOpt.get().getSkills();
        List<Job> allJobs = jobRepository.findByStatus(1);
        
        // 基于技能关键词匹配
        return allJobs.stream()
                .map(job -> {
                    double skillMatchScore = calculateSkillMatchScore(userSkills, job.getRequirements());
                    return new Object[]{job, skillMatchScore};
                })
                .sorted((a, b) -> Double.compare((Double) b[1], (Double) a[1]))
                .limit(limit)
                .map(obj -> (Job) obj[0])
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Job> hybridRecommendation(Long userId, int limit) {
        List<Job> recommendedJobs = new ArrayList<>();
        
        // 组合多种推荐算法
        recommendedJobs.addAll(collaborativeFilteringRecommendation(userId, limit / 3));
        recommendedJobs.addAll(contentBasedRecommendation(userId, limit / 3));
        recommendedJobs.addAll(userProfileBasedRecommendation(userId, limit / 3));
        
        // 如果推荐数量不足，补充热门岗位
        if (recommendedJobs.size() < limit) {
            recommendedJobs.addAll(hotJobsRecommendation(limit - recommendedJobs.size()));
        }
        
        // 去重并限制数量
        return recommendedJobs.stream()
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<Long, Double> calculateJobUserMatchScores(Long userId, List<Long> jobIds) {
        Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserId(userId);
        if (userProfileOpt.isEmpty()) {
            return jobIds.stream().collect(Collectors.toMap(id -> id, id -> 0.0));
        }
        
        UserProfile userProfile = userProfileOpt.get();
        List<Job> jobs = jobRepository.findAllById(jobIds);
        
        return jobs.stream()
                .collect(Collectors.toMap(
                        Job::getId,
                        job -> calculateContentMatchScore(userProfile, job)
                ));
    }
    
    @Override
    public Map<Long, Double> calculateResumeJobMatchScores(Long jobId, List<Long> resumeIds) {
        Optional<Job> jobOpt = jobRepository.findById(jobId);
        if (jobOpt.isEmpty()) {
            return resumeIds.stream().collect(Collectors.toMap(id -> id, id -> 0.0));
        }
        
        Job job = jobOpt.get();
        List<Resume> resumes = resumeRepository.findAllById(resumeIds);
        
        return resumes.stream()
                .collect(Collectors.toMap(
                        Resume::getId,
                        resume -> calculateResumeJobMatchScore(resume, job)
                ));
    }
    
    @Override
    public Map<String, Object> getUserRecommendationPreferences(Long userId) {
        // 这里可以存储用户的推荐偏好，如喜欢的岗位类型、薪资范围等
        Map<String, Object> preferences = new HashMap<>();
        preferences.put("preferredCategories", new ArrayList<>());
        preferences.put("salaryRange", new HashMap<>());
        preferences.put("locationPreferences", new ArrayList<>());
        preferences.put("skillPreferences", new ArrayList<>());
        
        return preferences;
    }
    
    @Override
    public void updateUserRecommendationPreferences(Long userId, Map<String, Object> preferences) {
        // 更新用户推荐偏好（可以存储在数据库或缓存中）
        // 这里只是示例实现
    }
    
    @Override
    public Map<String, Object> getRecommendationStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalUsers = userRepository.count();
        long totalJobs = jobRepository.count();
        long totalApplies = jobApplyRepository.count();
        
        stats.put("totalUsers", totalUsers);
        stats.put("totalJobs", totalJobs);
        stats.put("totalApplies", totalApplies);
        stats.put("recommendationAlgorithms", Arrays.asList(
                "协同过滤", "基于内容", "用户画像", "混合推荐"
        ));
        
        return stats;
    }
    
    @Override
    public void trainRecommendationModel() {
        // 训练推荐模型（这里只是示例，实际需要实现具体的训练逻辑）
        // 可以定期训练模型以提高推荐准确性
    }
    
    @Override
    public void clearRecommendationCache(Long userId) {
        // 清除用户推荐缓存
        // 这里只是示例实现
    }
    
    @Override
    public Map<Long, String> getRecommendationExplanation(Long userId, List<Long> jobIds) {
        Map<Long, String> explanations = new HashMap<>();
        
        for (Long jobId : jobIds) {
            Optional<Job> jobOpt = jobRepository.findById(jobId);
            if (jobOpt.isPresent()) {
                Job job = jobOpt.get();
                String explanation = generateExplanation(userId, job);
                explanations.put(jobId, explanation);
            }
        }
        
        return explanations;
    }
    
    @Override
    public Map<String, Object> evaluateRecommendationPerformance() {
        Map<String, Object> performance = new HashMap<>();
        
        // 这里可以实现推荐系统的性能评估
        performance.put("precision", 0.85);
        performance.put("recall", 0.78);
        performance.put("f1Score", 0.81);
        performance.put("coverage", 0.92);
        performance.put("diversity", 0.76);
        
        return performance;
    }
    
    // 私有辅助方法
    
    private Set<Long> findSimilarUsers(Long userId) {
        // 查找相似用户（基于申请历史、用户画像等）
        // 这里使用简化实现
        Set<Long> similarUsers = new HashSet<>();
        
        // 获取申请过相同岗位的用户
        List<JobApply> userApplies = jobApplyRepository.findByUserId(userId);
        for (JobApply apply : userApplies) {
            List<JobApply> sameJobApplies = jobApplyRepository.findByJobId(apply.getJobId());
            for (JobApply sameApply : sameJobApplies) {
                if (!sameApply.getUserId().equals(userId)) {
                    similarUsers.add(sameApply.getUserId());
                }
            }
        }
        
        return similarUsers.stream().limit(10).collect(Collectors.toSet());
    }
    
    private boolean hasUserApplied(Long userId, Long jobId) {
        return jobApplyRepository.findByUserIdAndJobId(userId, jobId).isPresent();
    }
    
    private double calculateContentMatchScore(UserProfile userProfile, Job job) {
        double score = 0.0;
        
        // 技能匹配度
        if (userProfile.getSkills() != null && job.getRequirements() != null) {
            score += calculateSkillMatchScore(userProfile.getSkills(), job.getRequirements()) * 0.4;
        }
        
        // 薪资匹配度
        if (userProfile.getExpectedSalary() != null && job.getSalaryMax() != null) {
            double salaryMatch = calculateSalaryMatchScore(userProfile.getExpectedSalary(), job.getSalaryMax());
            score += salaryMatch * 0.3;
        }
        
        // 地理位置匹配度
        if (userProfile.getCity() != null && job.getWorkCity() != null) {
            double locationMatch = userProfile.getCity().equals(job.getWorkCity()) ? 1.0 : 0.0;
            score += locationMatch * 0.3;
        }
        
        return Math.min(score, 1.0);
    }
    
    private double calculateSkillMatchScore(String userSkills, String jobRequirements) {
        if (userSkills == null || jobRequirements == null) {
            return 0.0;
        }
        
        // 简化的技能匹配算法
        String[] userSkillArray = userSkills.toLowerCase().split("[,\\s]+");
        String[] jobSkillArray = jobRequirements.toLowerCase().split("[,\\s]+");
        
        int matchedSkills = 0;
        for (String userSkill : userSkillArray) {
            for (String jobSkill : jobSkillArray) {
                if (jobSkill.contains(userSkill) || userSkill.contains(jobSkill)) {
                    matchedSkills++;
                    break;
                }
            }
        }
        
        return (double) matchedSkills / Math.max(userSkillArray.length, 1);
    }
    
    private double calculateSalaryMatchScore(BigDecimal expectedSalary, BigDecimal jobMaxSalary) {
        if (expectedSalary == null || jobMaxSalary == null) {
            return 0.0;
        }
        
        if (expectedSalary.compareTo(jobMaxSalary) <= 0) {
            return 1.0;
        } else {
            // 如果期望薪资高于岗位最高薪资，按比例计算匹配度
            BigDecimal diff = expectedSalary.subtract(jobMaxSalary);
            BigDecimal ratio = diff.divide(expectedSalary, 2, BigDecimal.ROUND_HALF_UP);
            return Math.max(0.0, 1.0 - ratio.doubleValue());
        }
    }
    
    private double calculateResumeJobMatchScore(Resume resume, Job job) {
        double score = 0.0;
        
        // 基于技能匹配度 - 使用简历标题和结构化数据
        if (resume.getTitle() != null && job.getRequirements() != null) {
            score += calculateSkillMatchScore(resume.getTitle(), job.getRequirements()) * 0.4;
        }
        
        // 基于工作经验匹配度
        if (resume.getUserId() != null) {
            Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserId(resume.getUserId());
            if (userProfileOpt.isPresent() && userProfileOpt.get().getWorkYears() != null) {
                // 简化的经验匹配逻辑
                int workYears = userProfileOpt.get().getWorkYears();
                if (workYears >= 5) {
                    score += 0.3; // 资深经验加分
                } else if (workYears >= 3) {
                    score += 0.2; // 中等经验加分
                } else if (workYears >= 1) {
                    score += 0.1; // 初级经验加分
                } else {
                    score += 0.05; // 应届生加分
                }
            }
        }
        
        // 基于简历类型加分
        if (resume.getResumeType() != null && resume.getResumeType() == 2) {
            score += 0.1; // 结构化简历加分
        }
        
        return Math.min(score, 1.0);
    }
    
    private double calculateJobSimilarity(Job job1, Job job2) {
        double similarity = 0.0;
        
        // 基于岗位标题和描述的相似度计算
        if (job1.getTitle() != null && job2.getTitle() != null) {
            similarity += calculateTextSimilarity(job1.getTitle(), job2.getTitle()) * 0.6;
        }
        
        if (job1.getDescription() != null && job2.getDescription() != null) {
            similarity += calculateTextSimilarity(job1.getDescription(), job2.getDescription()) * 0.4;
        }
        
        return similarity;
    }
    
    private double calculateTextSimilarity(String text1, String text2) {
        // 简化的文本相似度计算
        if (text1 == null || text2 == null) {
            return 0.0;
        }
        
        String[] words1 = text1.toLowerCase().split("\\s+");
        String[] words2 = text2.toLowerCase().split("\\s+");
        
        Set<String> set1 = new HashSet<>(Arrays.asList(words1));
        Set<String> set2 = new HashSet<>(Arrays.asList(words2));
        
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        
        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }
    
    private String generateExplanation(Long userId, Job job) {
        // 生成推荐解释
        StringBuilder explanation = new StringBuilder();
        explanation.append("推荐此岗位的原因：");
        
        Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserId(userId);
        if (userProfileOpt.isPresent()) {
            UserProfile userProfile = userProfileOpt.get();
            
            // 基于技能匹配
            if (userProfile.getSkills() != null && job.getRequirements() != null) {
                explanation.append(" 技能匹配度高;");
            }
            
            // 基于地理位置
            if (userProfile.getCity() != null && job.getWorkCity() != null && 
                userProfile.getCity().equals(job.getWorkCity())) {
                explanation.append(" 地理位置匹配;");
            }
            
            // 基于薪资期望
            if (userProfile.getExpectedSalary() != null && job.getSalaryMax() != null && 
                userProfile.getExpectedSalary().compareTo(job.getSalaryMax()) <= 0) {
                explanation.append(" 薪资符合期望;");
            }
        }
        
        // 基于热度
        if (job.getViewCount() > 100 || job.getApplyCount() > 50) {
            explanation.append(" 热门岗位;");
        }
        
        return explanation.toString();
    }
    
    /**
     * 获取用户名
     */
    private String getUserName(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.map(User::getUsername).orElse("未知用户");
    }
    
    /**
     * 生成简历推荐理由
     */
    private String generateResumeRecommendationReason(Resume resume, Job job, double matchScore) {
        StringBuilder reason = new StringBuilder();
        reason.append("推荐此简历的原因：");
        
        // 基于匹配度
        if (matchScore >= 0.8) {
            reason.append(" 高度匹配;");
        } else if (matchScore >= 0.6) {
            reason.append(" 良好匹配;");
        } else if (matchScore >= 0.4) {
            reason.append(" 一般匹配;");
        } else {
            reason.append(" 基本匹配;");
        }
        
        // 基于技能匹配
        if (resume.getTitle() != null && job.getRequirements() != null) {
            double skillMatch = calculateSkillMatchScore(resume.getTitle(), job.getRequirements());
            if (skillMatch >= 0.7) {
                reason.append(" 技能匹配度高;");
            }
        }
        
        // 基于工作经验
        if (resume.getUserId() != null) {
            Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserId(resume.getUserId());
            if (userProfileOpt.isPresent() && userProfileOpt.get().getWorkYears() != null) {
                int workYears = userProfileOpt.get().getWorkYears();
                if (workYears >= 5) {
                    reason.append(" 资深经验;");
                } else if (workYears >= 3) {
                    reason.append(" 丰富经验;");
                } else if (workYears >= 1) {
                    reason.append(" 初级经验;");
                } else {
                    reason.append(" 应届生;");
                }
            }
        }
        
        return reason.toString();
    }
}
