package com.shera.framework.employment.employment.modules.job.service.impl;

import com.shera.framework.employment.employment.modules.job.dto.JobApplyDTO;
import com.shera.framework.employment.employment.modules.job.entity.JobApply;
import com.shera.framework.employment.employment.modules.job.repository.JobApplyRepository;
import com.shera.framework.employment.employment.modules.job.service.JobApplyService;
import com.shera.framework.employment.employment.modules.job.service.JobService;
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
    public JobApplyDTO createJobApply(JobApplyDTO jobApplyDTO) {
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
        
        return convertToDTO(savedApply);
    }
    
    @Override
    @Transactional
    public JobApplyDTO updateJobApply(Long id, JobApplyDTO jobApplyDTO) {
        JobApply jobApply = jobApplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("岗位申请不存在: " + id));
        copyJobApplyProperties(jobApply, jobApplyDTO);
        JobApply updatedApply = jobApplyRepository.save(jobApply);
        return convertToDTO(updatedApply);
    }
    
    @Override
    @Transactional
    public void deleteJobApply(Long id) {
        JobApply jobApply = jobApplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("岗位申请不存在: " + id));
        jobApplyRepository.delete(jobApply);
    }
    
    @Override
    public JobApplyDTO getJobApplyById(Long id) {
        JobApply jobApply = jobApplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("岗位申请不存在: " + id));
        return convertToDTO(jobApply);
    }
    
    @Override
    public JobApplyDTO getJobApplyDetail(Long id) {
        JobApply jobApply = jobApplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("岗位申请不存在: " + id));
        // 这里可以加载关联的实体信息，如用户、岗位、简历等
        return convertToDTO(jobApply);
    }
    
    @Override
    public Page<JobApplyDTO> getJobApplies(Pageable pageable) {
        return jobApplyRepository.findAll(pageable).map(this::convertToDTO);
    }
    
    @Override
    public List<JobApplyDTO> getJobAppliesByUserId(Long userId) {
        return jobApplyRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    @Override
    public Page<JobApplyDTO> getJobAppliesByUserId(Long userId, Pageable pageable) {
        return jobApplyRepository.findByUserId(userId, pageable).map(this::convertToDTO);
    }
    
    @Override
    public List<JobApplyDTO> getJobAppliesByJobId(Long jobId) {
        return jobApplyRepository.findByJobId(jobId).stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    @Override
    public Page<JobApplyDTO> getJobAppliesByJobId(Long jobId, Pageable pageable) {
        return jobApplyRepository.findByJobId(jobId, pageable).map(this::convertToDTO);
    }
    
    @Override
    public List<JobApplyDTO> getJobAppliesByResumeId(Long resumeId) {
        return jobApplyRepository.findByResumeId(resumeId).stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    @Override
    public List<JobApplyDTO> getJobAppliesByStatus(Integer status) {
        return jobApplyRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    @Override
    public List<JobApplyDTO> getJobAppliesByUserIdAndStatus(Long userId, Integer status) {
        return jobApplyRepository.findByUserIdAndStatus(userId, status).stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    @Override
    public List<JobApplyDTO> getJobAppliesByJobIdAndStatus(Long jobId, Integer status) {
        return jobApplyRepository.findByJobIdAndStatus(jobId, status).stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    @Override
    @Transactional
    public JobApplyDTO updateStatus(Long id, Integer status) {
        JobApply jobApply = jobApplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("岗位申请不存在: " + id));
        jobApply.setStatus(status);
        JobApply updatedApply = jobApplyRepository.save(jobApply);
        return convertToDTO(updatedApply);
    }
    
    @Override
    @Transactional
    public JobApplyDTO setInterviewTime(Long id, String interviewTime, String interviewLocation) {
        JobApply jobApply = jobApplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("岗位申请不存在: " + id));
        
        // 解析面试时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime interviewDateTime = LocalDateTime.parse(interviewTime, formatter);
        
        jobApply.setInterviewTime(interviewDateTime);
        jobApply.setInterviewLocation(interviewLocation);
        jobApply.setStatus(JobApply.Status.INTERVIEW_NOTIFIED.getCode()); // 设置为已通知面试状态
        
        JobApply updatedApply = jobApplyRepository.save(jobApply);
        return convertToDTO(updatedApply);
    }
    
    @Override
    @Transactional
    public JobApplyDTO addFeedback(Long id, String feedback) {
        JobApply jobApply = jobApplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("岗位申请不存在: " + id));
        jobApply.setFeedback(feedback);
        JobApply updatedApply = jobApplyRepository.save(jobApply);
        return convertToDTO(updatedApply);
    }
    
    @Override
    @Transactional
    public JobApplyDTO passInterview(Long id, String feedback) {
        JobApply jobApply = jobApplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("岗位申请不存在: " + id));
        jobApply.setStatus(JobApply.Status.INTERVIEW_PASSED.getCode());
        if (feedback != null && !feedback.trim().isEmpty()) {
            jobApply.setFeedback(feedback);
        }
        JobApply updatedApply = jobApplyRepository.save(jobApply);
        return convertToDTO(updatedApply);
    }
    
    @Override
    @Transactional
    public JobApplyDTO rejectApplication(Long id, String reason) {
        JobApply jobApply = jobApplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("岗位申请不存在: " + id));
        jobApply.setStatus(JobApply.Status.REJECTED.getCode());
        if (reason != null && !reason.trim().isEmpty()) {
            jobApply.setFeedback(reason);
        }
        JobApply updatedApply = jobApplyRepository.save(jobApply);
        return convertToDTO(updatedApply);
    }
    
    @Override
    @Transactional
    public JobApplyDTO cancelApplication(Long id) {
        JobApply jobApply = jobApplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("岗位申请不存在: " + id));
        jobApply.setStatus(JobApply.Status.REJECTED.getCode()); // 取消申请视为拒绝
        jobApply.setFeedback("用户主动取消申请");
        JobApply updatedApply = jobApplyRepository.save(jobApply);
        return convertToDTO(updatedApply);
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
    public Map<String, Object> checkAppliedStatus(Long userId, Long jobId) {
        Map<String, Object> result = new HashMap<>();
        boolean applied = hasUserAppliedJob(userId, jobId);
        result.put("applied", applied);
        
        if (applied) {
            JobApply jobApply = jobApplyRepository.findByUserIdAndJobId(userId, jobId).orElse(null);
            if (jobApply != null) {
                result.put("applyId", jobApply.getId());
                result.put("applyTime", jobApply.getCreateTime());
                result.put("status", jobApply.getStatus());
                result.put("statusDesc", getStatusDescription(jobApply.getStatus()));
            }
        }
        
        return result;
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
    public List<JobApplyDTO> getLatestApplications(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createTime"));
        return jobApplyRepository.findAll(pageable).getContent().stream()
                .map(this::convertToDTO)
                .toList();
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
        Long interviewInvites = jobApplyRepository.countByUserIdAndStatus(userId, JobApply.Status.INTERVIEW_NOTIFIED.getCode());
        stats.put("interviewInvites", interviewInvites);
        
        // 面试通过数
        Long interviewPassed = jobApplyRepository.countByUserIdAndStatus(userId, JobApply.Status.INTERVIEW_PASSED.getCode());
        stats.put("interviewPassed", interviewPassed);
        
        return stats;
    }
    
    /**
     * 复制JobApplyDTO属性到JobApply实体
     */
    private void copyJobApplyProperties(JobApply jobApply, JobApplyDTO jobApplyDTO) {
        jobApply.setUserId(jobApplyDTO.getUserId());
        jobApply.setJobId(jobApplyDTO.getJobId());
        jobApply.setResumeId(jobApplyDTO.getResumeId());
        jobApply.setStatus(jobApplyDTO.getStatus() != null ? jobApplyDTO.getStatus() : JobApply.Status.APPLIED.getCode());
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
    
    /**
     * 获取状态描述
     */
    private String getStatusDescription(Integer status) {
        for (JobApply.Status statusEnum : JobApply.Status.values()) {
            if (statusEnum.getCode() == status) {
                return statusEnum.getDesc();
            }
        }
        return "未知状态";
    }

    /**
     * 将JobApply实体转换为JobApplyDTO
     */
    private JobApplyDTO convertToDTO(JobApply jobApply) {
        if (jobApply == null) {
            return null;
        }
        
        JobApplyDTO dto = new JobApplyDTO();
        dto.setId(jobApply.getId());
        dto.setUserId(jobApply.getUserId());
        dto.setJobId(jobApply.getJobId());
        dto.setResumeId(jobApply.getResumeId());
        dto.setStatus(jobApply.getStatus());
        dto.setMatchScore(jobApply.getMatchScore());
        dto.setApplyNotes(jobApply.getApplyNotes());
        dto.setInterviewTime(jobApply.getInterviewTime());
        dto.setInterviewLocation(jobApply.getInterviewLocation());
        dto.setFeedback(jobApply.getFeedback());
        dto.setCreateTime(jobApply.getCreateTime());
        dto.setUpdateTime(jobApply.getUpdateTime());
        
        // 加载关联信息
        loadAssociatedInfo(dto, jobApply);
        
        return dto;
    }

    /**
     * 加载关联信息
     */
    private void loadAssociatedInfo(JobApplyDTO dto, JobApply jobApply) {
        // 加载岗位信息
        if (jobApply.getJob() != null) {
            dto.setJobTitle(jobApply.getJob().getTitle());
            
            // 加载公司信息
            if (jobApply.getJob().getCompany() != null) {
                dto.setCompanyName(jobApply.getJob().getCompany().getCompanyName());
            }
        }
        
        // 加载简历信息
        if (jobApply.getResume() != null) {
            dto.setResumeName(jobApply.getResume().getTitle());
        }
        
        // 加载用户信息
        if (jobApply.getUser() != null) {
            dto.setUserName(jobApply.getUser().getUsername());
            dto.setUserEmail(jobApply.getUser().getEmail());
        }
    }
}
