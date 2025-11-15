package com.shera.framework.employment.employment.service.impl;

import com.shera.framework.employment.employment.dto.JobApplyDTO;
import com.shera.framework.employment.employment.entity.JobApply;
import com.shera.framework.employment.employment.repository.JobApplyRepository;
import com.shera.framework.employment.employment.service.JobApplyService;
import com.shera.framework.employment.employment.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 岗位申请服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JobApplyServiceImpl implements JobApplyService {
    
    private final JobApplyRepository jobApplyRepository;
    private final JobService jobService;
    
    @Override
    @Transactional
    public JobApply createJobApply(JobApplyDTO jobApplyDTO) {
        // 检查用户是否已申请该岗位
        if (hasUserAppliedJob(jobApplyDTO.getUserId(), jobApplyDTO.getJobId())) {
            throw new RuntimeException("用户已申请该岗位");
        }
        
        JobApply jobApply = new JobApply();
        copyJobApplyProperties(jobApply, jobApplyDTO);
        
        // 计算匹配度
        BigDecimal matchScore = calculateMatchScore(jobApplyDTO.getJobId(), jobApplyDTO.getResumeId());
        jobApply.setMatchScore(matchScore);
        
        JobApply savedApply = jobApplyRepository.save(jobApply);
        
        // 增加岗位申请量
        jobService.increaseApplyCount(jobApplyDTO.getJobId());
        
        return savedApply;
    }
    
    @Override
    @Transactional
    public JobApply updateJobApply(Long id, JobApplyDTO jobApplyDTO) {
        JobApply jobApply = getJobApplyById(id);
        copyJobApplyProperties(jobApply, jobApplyDTO);
        return jobApplyRepository.save(jobApply);
    }
    
    @Override
    @Transactional
    public void deleteJobApply(Long id) {
        JobApply jobApply = getJobApplyById(id);
        jobApplyRepository.delete(jobApply);
    }
    
    @Override
    public JobApply getJobApplyById(Long id) {
        return jobApplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("岗位申请不存在: " + id));
    }
    
    @Override
    public JobApply getJobApplyDetail(Long id) {
        JobApply jobApply = getJobApplyById(id);
        // 这里可以加载关联的实体信息，如用户、岗位、简历等
        return jobApply;
    }
    
    @Override
    public Page<JobApply> getJobApplies(Pageable pageable) {
        return jobApplyRepository.findAll(pageable);
    }
    
    @Override
    public List<JobApply> getJobAppliesByUserId(Long userId) {
        return jobApplyRepository.findByUserId(userId);
    }
    
    @Override
    public Page<JobApply> getJobAppliesByUserId(Long userId, Pageable pageable) {
        return jobApplyRepository.findByUserId(userId, pageable);
    }
    
    @Override
    public List<JobApply> getJobAppliesByJobId(Long jobId) {
        return jobApplyRepository.findByJobId(jobId);
    }
    
    @Override
    public Page<JobApply> getJobAppliesByJobId(Long jobId, Pageable pageable) {
        return jobApplyRepository.findByJobId(jobId, pageable);
    }
    
    @Override
    public List<JobApply> getJobAppliesByResumeId(Long resumeId) {
        return jobApplyRepository.findByResumeId(resumeId);
    }
    
    @Override
    public List<JobApply> getJobAppliesByStatus(Integer status) {
        return jobApplyRepository.findByStatus(status);
    }
    
    @Override
    public List<JobApply> getJobAppliesByUserIdAndStatus(Long userId, Integer status) {
        return jobApplyRepository.findByUserIdAndStatus(userId, status);
    }
    
    @Override
    public List<JobApply> getJobAppliesByJobIdAndStatus(Long jobId, Integer status) {
        return jobApplyRepository.findByJobIdAndStatus(jobId, status);
    }
    
    @Override
    @Transactional
    public JobApply updateStatus(Long id, Integer status) {
        JobApply jobApply = getJobApplyById(id);
        jobApply.setStatus(status);
        return jobApplyRepository.save(jobApply);
    }
    
    @Override
    @Transactional
    public JobApply setInterviewTime(Long id, String interviewTime, String interviewLocation) {
        JobApply jobApply = getJobApplyById(id);
        
        // 解析面试时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime interviewDateTime = LocalDateTime.parse(interviewTime, formatter);
        
        jobApply.setInterviewTime(interviewDateTime);
        jobApply.setInterviewLocation(interviewLocation);
        jobApply.setStatus(3); // 设置为已通知面试状态
        
        return jobApplyRepository.save(jobApply);
    }
    
    @Override
    @Transactional
    public JobApply addFeedback(Long id, String feedback) {
        JobApply jobApply = getJobApplyById(id);
        jobApply.setFeedback(feedback);
        return jobApplyRepository.save(jobApply);
    }
    
    @Override
    public BigDecimal calculateMatchScore(Long jobId, Long resumeId) {
        // 这里可以实现复杂的匹配度计算逻辑
        // 简化版本：返回一个随机匹配度
        double randomScore = 60 + Math.random() * 40; // 60-100之间的随机数
        return BigDecimal.valueOf(randomScore).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    @Override
    public boolean hasUserAppliedJob(Long userId, Long jobId) {
        return jobApplyRepository.findByUserIdAndJobId(userId, jobId).isPresent();
    }
    
    @Override
    public Map<Integer, Long> countJobAppliesByStatus() {
        List<Object[]> results = jobApplyRepository.countByStatus();
        Map<Integer, Long> countMap = new HashMap<>();
        for (Object[] result : results) {
            countMap.put((Integer) result[0], (Long) result[1]);
        }
        return countMap;
    }
    
    @Override
    public Long countJobAppliesByUserId(Long userId) {
        return jobApplyRepository.countByUserId(userId);
    }
    
    @Override
    public Long countJobAppliesByJobId(Long jobId) {
        return jobApplyRepository.countByJobId(jobId);
    }
    
    @Override
    public List<Object[]> getHotAppliedJobs(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "applyCount"));
        return jobApplyRepository.findHotAppliedJobs(pageable);
    }
    
    @Override
    public Map<String, Object> getUserApplyStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        // 统计各状态的申请数量
        List<Object[]> statusCounts = jobApplyRepository.countByUserIdAndStatus(userId);
        for (Object[] result : statusCounts) {
            stats.put("status_" + result[0], result[1]);
        }
        
        // 总申请数
        Long totalApplies = countJobAppliesByUserId(userId);
        stats.put("totalApplies", totalApplies);
        
        // 面试邀请数
        Long interviewInvites = jobApplyRepository.countByUserIdAndStatus(userId, 3);
        stats.put("interviewInvites", interviewInvites);
        
        return stats;
    }
    
    /**
     * 复制JobApplyDTO属性到JobApply实体
     */
    private void copyJobApplyProperties(JobApply jobApply, JobApplyDTO jobApplyDTO) {
        jobApply.setUserId(jobApplyDTO.getUserId());
        jobApply.setJobId(jobApplyDTO.getJobId());
        jobApply.setResumeId(jobApplyDTO.getResumeId());
        jobApply.setStatus(jobApplyDTO.getStatus());
        jobApply.setApplyNotes(jobApplyDTO.getApplyNotes());
        jobApply.setFeedback(jobApplyDTO.getFeedback());
        
        // 面试时间和地点
        if (jobApplyDTO.getInterviewTime() != null) {
            jobApply.setInterviewTime(jobApplyDTO.getInterviewTime());
        }
        if (jobApplyDTO.getInterviewLocation() != null) {
            jobApply.setInterviewLocation(jobApplyDTO.getInterviewLocation());
        }
    }
}
